package com.webcommander.client;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcommander.Dto;
import com.webcommander.Redaction;
import com.webcommander.auth.FileTokenStore;
import com.webcommander.auth.MemoryTokenStore;
import com.webcommander.auth.TokenProvider;
import com.webcommander.auth.TokenStore;
import com.webcommander.config.AuthConfig;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.InsecureTransportWarning;
import com.webcommander.errors.SdkConfigError;
import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.http.QueryParams;
import com.webcommander.http.RetryPolicy;
import com.webcommander.http.Transport;
import com.webcommander.http.UrlBuilder;
import com.webcommander.resources.aliases.Aliases;
import com.webcommander.resources.blog_categories.BlogCategories;
import com.webcommander.resources.blogs.Blogs;
import com.webcommander.resources.carts.Carts;
import com.webcommander.resources.categories.Categories;
import com.webcommander.resources.cities.Cities;
import com.webcommander.resources.common_settings.CommonSettings;
import com.webcommander.resources.countries.Countries;
import com.webcommander.resources.currencies.Currencies;
import com.webcommander.resources.customer_groups.CustomerGroups;
import com.webcommander.resources.customers.AdminCustomers;
import com.webcommander.resources.discount_profiles.DiscountProfiles;
import com.webcommander.resources.email.Email;
import com.webcommander.resources.inventory_locations.InventoryLocations;
import com.webcommander.resources.order_custom_fields.OrderCustomFields;
import com.webcommander.resources.order_flags.OrderFlags;
import com.webcommander.resources.orders.Orders;
import com.webcommander.resources.pages.Pages;
import com.webcommander.resources.payment_gateways.PaymentGateways;
import com.webcommander.resources.products.Products;
import com.webcommander.resources.settings.animation_effects.AnimationEffects;
import com.webcommander.resources.settings.customer_portal.CustomerPortal;
import com.webcommander.resources.settings.ecommerce_settings.EcommerceSettings;
import com.webcommander.resources.settings.page_settings.CategoryPage;
import com.webcommander.resources.settings.page_settings.CheckoutPage;
import com.webcommander.resources.settings.page_settings.ProductPage;
import com.webcommander.resources.settings.page_settings.SearchResultPage;
import com.webcommander.resources.settings.page_settings.ShoppingCart;
import com.webcommander.resources.settings.price_display_settings.PriceDisplaySettings;
import com.webcommander.resources.settings.product_listing.ProductListing;
import com.webcommander.resources.settings.search_settings.SearchSettings;
import com.webcommander.resources.settings.seo_configuration.SeoConfiguration;
import com.webcommander.resources.settings.shipping_settings.ShippingSettings;
import com.webcommander.resources.settings.site_settings.SiteSettings;
import com.webcommander.resources.shipping_classes.ShippingClasses;
import com.webcommander.resources.shipping_methods.ShippingMethods;
import com.webcommander.resources.tax_codes.TaxCodes;
import com.webcommander.resources.tax_country_taxes.TaxCountryTaxes;
import com.webcommander.resources.tax_custom_profiles.TaxCustomProfiles;
import com.webcommander.resources.tax_profiles.TaxProfiles;
import com.webcommander.resources.tax_rules.TaxRules;
import com.webcommander.resources.tax_settings.TaxSettings;
import com.webcommander.resources.tax_zones.TaxZones;
import com.webcommander.resources.transactions.Transactions;
import com.webcommander.resources.trash.Trash;
import com.webcommander.resources.variations.Variations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/** Authenticated access to one WebCommander store: config in, {@link ApiResponse} out. */
public final class WebCommanderClient implements AutoCloseable {

    /** The root logger name every client's logger hangs off. */
    public static final String LOGGER_NAME = "webcommander";

    private static final String API_VERSION_PARAM = "api_version";
    private static final String TOKEN_FAILURE_ERROR = "unauthorized";
    private static final Set<String> TOKEN_FAILURE_MESSAGES =
            Set.of("invalid_access_token", "invalid_request");

    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final String FORM_MEDIA_TYPE = "application/x-www-form-urlencoded";
    private static final String JSON_SUFFIX = "+json";
    private static final String JSON_TREE = "application/";

    /** Messages already written to stderr, so each is emitted once per JVM. */
    private static final Set<String> WARNED = ConcurrentHashMap.newKeySet();

    private static final ObjectMapper PROBE_JSON =
            new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    private final SdkConfig config;
    private final Transport transport;
    private final boolean ownsTransport;
    private final TokenProvider tokenProvider;
    private final Logger logger;
    private final FileLogSink logSink;
    private final int logLevelRank;

    private final Map<String, BoundResource> boundResources = new ConcurrentHashMap<>();

    /** Acceptance and closure are one decision: {@link #closed} and {@link #inFlight} share it. */
    private final ReentrantLock lifecycle = new ReentrantLock();
    private final Condition drained = lifecycle.newCondition();
    private boolean closed;
    private int inFlight;

    /** A client on the default token store, transport and token provider. */
    public WebCommanderClient(SdkConfig config) {
        this(config, null, null, null, null);
    }

    /** A client with any collaborator supplied, {@code null} taking the default. */
    public WebCommanderClient(SdkConfig config, TokenStore tokenStore, Transport transport,
            RetryPolicy retryPolicy, TokenProvider tokenProvider) {
        if (transport != null && retryPolicy != null) {
            throw new SdkUsageError("pass transport or retryPolicy, not both");
        }
        this.config = config;
        this.ownsTransport = transport == null;
        // The dots are percent-encoded so one client's logger cannot nest under another's.
        String loggerName = LOGGER_NAME + "."
                + URLEncoder.encode(config.getClientName(), StandardCharsets.UTF_8)
                        .replace(".", "%2E");
        this.logger = LoggerFactory.getLogger(loggerName);
        this.logLevelRank = FileLogSink.LEVEL_RANKS.getOrDefault(config.getLogLevel(), 30);
        this.logSink = FileLogSink.attach(loggerName, config.getLogFile(), config.getLogLevel());
        try {
            warnAboutInsecureTransport();
            this.transport = transport != null ? transport
                    : new Transport(config, retryPolicy, this::reportProgress);
            TokenStore store = tokenStore != null ? tokenStore : buildTokenStore(config);
            this.tokenProvider = tokenProvider != null ? tokenProvider
                    : new TokenProvider(config, this.transport, store);
        } catch (RuntimeException | Error failure) {
            detachLogSink();
            throw failure;
        }
    }

    private static TokenStore buildTokenStore(SdkConfig config) {
        AuthConfig auth = config.getAuth();
        String cachePath = auth.getTokenCachePath();
        try {
            if (cachePath == null) {
                return new MemoryTokenStore();
            }
            // Identity, not equals: only the sentinel itself selects the default cache path.
            if (cachePath == AuthConfig.USE_DEFAULT_TOKEN_CACHE) {
                return new FileTokenStore(
                        FileTokenStore.buildDefaultTokenCachePath(
                                FileTokenStore.DEFAULT_TOKEN_CACHE_FILENAME),
                        fingerprint(auth), FileTokenStore.DEFAULT_LOCK_WAIT_SECONDS,
                        config.getMaxTokenCacheBytes());
            }
            return new FileTokenStore(cachePath, fingerprint(auth),
                    FileTokenStore.DEFAULT_LOCK_WAIT_SECONDS, config.getMaxTokenCacheBytes());
        } catch (SdkConfigError refusal) {
            throw new SdkConfigError(
                    "instance " + config.getClientName() + ": " + refusal.getMessage(), refusal);
        }
    }

    private static String fingerprint(AuthConfig auth) {
        return FileTokenStore.buildCredentialsNamespace(auth.getBaseUrl(), auth.getClientId(),
                auth.getClientSecret(), auth.getAuthString(), auth.getGrantType());
    }

    /** The configuration this client was built from, every default already resolved. */
    public SdkConfig getConfig() {
        return config;
    }

    /** This client's name, which also names its logger. */
    public String getName() {
        return config.getClientName();
    }

    /** The sorted names {@link #resource(String)} answers to, caller-registered ones included. */
    public List<String> listResourceNames() {
        return ResourceBinding.registeredResourceNames();
    }

    /** The resource registered under a name, matched exactly, bound to this client and cached. */
    public BoundResource resource(String resourceName) {
        Function<WebCommanderClient, ? extends BoundResource> factory =
                ResourceBinding.resolveResourceModule(resourceName);
        return boundResources.computeIfAbsent(resourceName, name -> factory.apply(this));
    }

    /** The admin customer endpoints, bound to this client. */
    public AdminCustomers adminCustomers() {
        return (AdminCustomers) resource("adminCustomers");
    }

    /** The SEO configuration settings endpoints, bound to this client. */
    public SeoConfiguration seoConfiguration() {
        return (SeoConfiguration) resource("seoConfiguration");
    }

    /** The search result page settings endpoints, bound to this client. */
    public SearchResultPage searchResultPage() {
        return (SearchResultPage) resource("searchResultPage");
    }

    /** The category page settings endpoints, bound to this client. */
    public CategoryPage categoryPage() {
        return (CategoryPage) resource("categoryPage");
    }

    /** The shopping cart settings endpoints, bound to this client. */
    public ShoppingCart shoppingCart() {
        return (ShoppingCart) resource("shoppingCart");
    }

    /** The product page settings endpoints, bound to this client. */
    public ProductPage productPage() {
        return (ProductPage) resource("productPage");
    }

    /** The checkout page settings endpoints, bound to this client. */
    public CheckoutPage checkoutPage() {
        return (CheckoutPage) resource("checkoutPage");
    }

    /** The price display settings endpoints, bound to this client. */
    public PriceDisplaySettings priceDisplaySettings() {
        return (PriceDisplaySettings) resource("priceDisplaySettings");
    }

    /** The animation effects settings endpoints, bound to this client. */
    public AnimationEffects animationEffects() {
        return (AnimationEffects) resource("animationEffects");
    }

    /** The product listing settings endpoints, bound to this client. */
    public ProductListing productListing() {
        return (ProductListing) resource("productListing");
    }

    /** The customer portal settings endpoints, bound to this client. */
    public CustomerPortal customerPortal() {
        return (CustomerPortal) resource("customerPortal");
    }

    /** The site settings endpoints, bound to this client. */
    public SiteSettings siteSettings() {
        return (SiteSettings) resource("siteSettings");
    }

    /** The shipping settings endpoints, bound to this client. */
    public ShippingSettings shippingSettings() {
        return (ShippingSettings) resource("shippingSettings");
    }

    /** The search settings endpoints, bound to this client. */
    public SearchSettings searchSettings() {
        return (SearchSettings) resource("searchSettings");
    }

    /** The ecommerce settings endpoints, bound to this client. */
    public EcommerceSettings ecommerceSettings() {
        return (EcommerceSettings) resource("ecommerceSettings");
    }

    /** The order flag endpoints, bound to this client. */
    public OrderFlags orderFlags() {
        return (OrderFlags) resource("orderFlags");
    }

    /** The inventory location endpoints, bound to this client. */
    public InventoryLocations inventoryLocations() {
        return (InventoryLocations) resource("inventoryLocations");
    }

    /** The tax code endpoints, bound to this client. */
    public TaxCodes taxCodes() {
        return (TaxCodes) resource("taxCodes");
    }

    /** The tax profile endpoints, bound to this client. */
    public TaxProfiles taxProfiles() {
        return (TaxProfiles) resource("taxProfiles");
    }

    /** The tax rule endpoints, bound to this client. */
    public TaxRules taxRules() {
        return (TaxRules) resource("taxRules");
    }

    /** The tax zone endpoints, bound to this client. */
    public TaxZones taxZones() {
        return (TaxZones) resource("taxZones");
    }

    /** The tax settings facade, bound to this client. */
    public TaxSettings taxSettings() {
        return (TaxSettings) resource("taxSettings");
    }

    /** The country tax endpoints, bound to this client. */
    public TaxCountryTaxes taxCountryTaxes() {
        return (TaxCountryTaxes) resource("taxCountryTaxes");
    }

    /** The custom tax profile endpoints, bound to this client. */
    public TaxCustomProfiles taxCustomProfiles() {
        return (TaxCustomProfiles) resource("taxCustomProfiles");
    }

    /** The shipping method endpoints, bound to this client. */
    public ShippingMethods shippingMethods() {
        return (ShippingMethods) resource("shippingMethods");
    }

    /** The shipping class endpoints, bound to this client. */
    public ShippingClasses shippingClasses() {
        return (ShippingClasses) resource("shippingClasses");
    }

    /** The domain alias endpoints, bound to this client. */
    public Aliases aliases() {
        return (Aliases) resource("aliases");
    }

    /** The order custom field endpoints, bound to this client. */
    public OrderCustomFields orderCustomFields() {
        return (OrderCustomFields) resource("orderCustomFields");
    }

    /** The blog category endpoints, bound to this client. */
    public BlogCategories blogCategories() {
        return (BlogCategories) resource("blogCategories");
    }

    /** The category endpoints, bound to this client. */
    public Categories categories() {
        return (Categories) resource("categories");
    }

    /** The discount profile endpoints, bound to this client. */
    public DiscountProfiles discountProfiles() {
        return (DiscountProfiles) resource("discountProfiles");
    }

    /** The blog and blog comment endpoints, bound to this client. */
    public Blogs blogs() {
        return (Blogs) resource("blogs");
    }

    /** The cart endpoints, bound to this client. */
    public Carts carts() {
        return (Carts) resource("carts");
    }

    /** The order endpoints, bound to this client. */
    public Orders orders() {
        return (Orders) resource("orders");
    }

    /** The country and state endpoints, bound to this client. */
    public Countries countries() {
        return (Countries) resource("countries");
    }

    /** The customer group endpoints, bound to this client. */
    public CustomerGroups customerGroups() {
        return (CustomerGroups) resource("customerGroups");
    }

    /** The currency endpoints, bound to this client. */
    public Currencies currencies() {
        return (Currencies) resource("currencies");
    }

    /** The variation type and product variant endpoints, bound to this client. */
    public Variations variations() {
        return (Variations) resource("variations");
    }

    /** The product endpoints, bound to this client. */
    public Products products() {
        return (Products) resource("products");
    }

    /** The storefront city endpoints, bound to this client. */
    public Cities cities() {
        return (Cities) resource("cities");
    }

    /** The transaction-number endpoints, bound to this client. */
    public Transactions transactions() {
        return (Transactions) resource("transactions");
    }

    /** The payment gateway endpoints, bound to this client. */
    public PaymentGateways paymentGateways() {
        return (PaymentGateways) resource("paymentGateways");
    }

    /** The trash endpoints, bound to this client. */
    public Trash trash() {
        return (Trash) resource("trash");
    }

    /** The email endpoints, bound to this client. */
    public Email email() {
        return (Email) resource("email");
    }

    /** The store-wide settings and customer registration form endpoints, bound to this client. */
    public CommonSettings commonSettings() {
        return (CommonSettings) resource("commonSettings");
    }

    /** The page endpoints, bound to this client. */
    public Pages pages() {
        return (Pages) resource("pages");
    }

    /** Send one request; everything beyond the verb and path rides on {@code options}. */
    public ApiResponse sendApiRequest(String httpMethod, String path, RequestOptions options) {
        enterSend();
        try {
            return sendAcceptedRequest(httpMethod, path, options);
        } finally {
            leaveSend();
        }
    }

    private ApiResponse sendAcceptedRequest(
            String httpMethod, String path, RequestOptions options) {
        RequestOptions resolvedOptions = options == null ? RequestOptions.none() : options;
        String method = Transport.normaliseMethod(httpMethod);
        validatePath(path);
        String responseFormat = resolveResponseFormat(resolvedOptions.getResponseFormat());
        Object jsonBody = resolveJsonBody(resolvedOptions.getJsonBody());
        Map<String, Object> callerFiles = resolvedOptions.getFiles();
        validatePayload(resolvedOptions, jsonBody, callerFiles);
        byte[] jsonBodyBytes = encodeJsonBodyOnce(jsonBody);
        validateContentType(resolvedOptions, callerFiles);

        String endpointTemplate = Redaction.reduceToEndpoint(path);
        String resolvedPath = UrlBuilder.substitutePathParams(
                path, seedApiVersion(path, resolvedOptions.getPathParams()));
        String url = UrlBuilder.buildUrl(config.getBaseUrl(), resolvedPath);
        Map<String, Object> params = QueryParams.buildQueryParams(resolvedOptions.getQueryParams());
        rejectDuplicatedQueryParams(url, params);
        validateCallerHeaders(resolvedOptions.getHeaders());

        Map<String, Object> files = Transport.materialiseFileParts(callerFiles, config);
        Map<String, Object> formData =
                Transport.materialiseFormFields(resolvedOptions.getFormData());
        Object reportedBody = jsonBody != null ? jsonBody : formData;
        boolean formEncoded = jsonBody == null && formData != null;
        Transport.TransportRequest wireRequest = Transport.TransportRequest.builder(method, url)
                .pathTemplate(endpointTemplate)
                .queryParams(params)
                .jsonBody(jsonBodyBytes)
                .formData(formData)
                .files(files)
                .requestInfoBody(Redaction.redactValue(
                        Console.summarisePayload(reportedBody, files)))
                .build();

        String accessToken = resolvedOptions.isAuthenticated() ? tokenProvider.getAccessToken() : null;

        ApiResponse response = sendOnce(wireRequest, resolvedOptions.getHeaders(), accessToken,
                reportedBody, files, formEncoded, endpointTemplate);

        if (resolvedOptions.isAuthenticated() && meansExpiredToken(response, method)) {
            reportExchange(response, reportedBody, files, formEncoded, true, endpointTemplate);
            response = sendOnce(wireRequest, resolvedOptions.getHeaders(),
                    tokenProvider.refreshAccessToken(accessToken),
                    reportedBody, files, formEncoded, endpointTemplate);
        }
        reportExchange(response, reportedBody, files, formEncoded, false, endpointTemplate);

        response = response.withResponseView(responseFormat, resolvedOptions.getDtoFactory());

        boolean shouldRaise = resolvedOptions.getRaiseOnError() != null
                ? resolvedOptions.getRaiseOnError() : config.isRaiseOnError();
        return shouldRaise ? response.raiseForStatus() : response;
    }

    private String resolveResponseFormat(String responseFormat) {
        if (responseFormat == null) {
            return config.getResponseFormat();
        }
        return SdkConfig.normaliseResponseFormat(responseFormat);
    }

    private static Object resolveJsonBody(Object jsonBody) {
        if (!(jsonBody instanceof Dto payload)) {
            return jsonBody;
        }
        Map<String, Object> body = payload.toMap();
        if (body == null) {
            throw new SdkUsageError("jsonBody: " + payload.getClass().getSimpleName()
                    + ".toMap() returned null, so there is no body to send — a payload "
                    + "object must describe the body it stands for");
        }
        return Redaction.detachValue(body);
    }

    /** GET with no options. */
    public ApiResponse sendGetRequest(String path) {
        return sendApiRequest("GET", path, null);
    }

    /** GET. */
    public ApiResponse sendGetRequest(String path, RequestOptions options) {
        return sendApiRequest("GET", path, options);
    }

    /** POST with no body. */
    public ApiResponse sendPostRequest(String path) {
        return sendApiRequest("POST", path, null);
    }

    /** POST. */
    public ApiResponse sendPostRequest(String path, RequestOptions options) {
        return sendApiRequest("POST", path, options);
    }

    /** PUT with no body. */
    public ApiResponse sendPutRequest(String path) {
        return sendApiRequest("PUT", path, null);
    }

    /** PUT. */
    public ApiResponse sendPutRequest(String path, RequestOptions options) {
        return sendApiRequest("PUT", path, options);
    }

    /** PATCH with no body. */
    public ApiResponse sendPatchRequest(String path) {
        return sendApiRequest("PATCH", path, null);
    }

    /** PATCH. */
    public ApiResponse sendPatchRequest(String path, RequestOptions options) {
        return sendApiRequest("PATCH", path, options);
    }

    /** DELETE with no body. */
    public ApiResponse sendDeleteRequest(String path) {
        return sendApiRequest("DELETE", path, null);
    }

    /** DELETE. */
    public ApiResponse sendDeleteRequest(String path, RequestOptions options) {
        return sendApiRequest("DELETE", path, options);
    }

    /** Release what this client made: its log file, and the transport only if it built it. */
    public void closeClient() {
        lifecycle.lock();
        try {
            if (closed) {
                return;
            }
            closed = true;
            boolean interrupted = false;
            while (inFlight > 0) {
                try {
                    drained.await();
                } catch (InterruptedException stopped) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        } finally {
            lifecycle.unlock();
        }
        try {
            if (ownsTransport) {
                transport.close();
            }
        } finally {
            detachLogSink();
        }
    }

    private void enterSend() {
        lifecycle.lock();
        try {
            if (closed) {
                throw new SdkUsageError("client '" + config.getClientName() + "' is closed");
            }
            inFlight++;
        } finally {
            lifecycle.unlock();
        }
    }

    private void leaveSend() {
        lifecycle.lock();
        try {
            if (--inFlight == 0) {
                drained.signalAll();
            }
        } finally {
            lifecycle.unlock();
        }
    }

    /** Equivalent to {@link #closeClient()}. */
    @Override
    public void close() {
        closeClient();
    }

    private void detachLogSink() {
        if (logSink != null) {
            logSink.detach();
        }
    }

    private void warnAboutInsecureTransport() {
        if (!config.isVerifySsl()) {
            warn(new InsecureTransportWarning(
                    "TLS verification is disabled (verifySsl=false); "
                            + "responses cannot be trusted"));
        }
        warnAboutPlaintextTransport();
    }

    private void warnAboutPlaintextTransport() {
        UrlBuilder.Authority base = UrlBuilder.Authority.split(config.getBaseUrl());
        if (!"http".equals(base.getScheme()) || isLoopbackHost(base.getHost())) {
            return;
        }
        warn(new InsecureTransportWarning(
                "baseUrl uses http://, so requests to " + base.getHost()
                        + " carry the token in cleartext"));
    }

    private static boolean stderrWarningsOff() {
        return "off".equalsIgnoreCase(
                System.getProperty("webcommander.sdk.insecureTransportWarning", "on"));
    }

    private void warn(InsecureTransportWarning warning) {
        // Two channels on purpose: SLF4J with no binding is a NOP and must not swallow this.
        if (!stderrWarningsOff() && WARNED.add(warning.toString())) {
            System.err.println(warning);
        }
        if (shouldLog("WARNING")) {
            logger.warn("{}", warning);
        }
        logToFile("WARNING", warning.toString());
    }

    private boolean shouldLog(String level) {
        return FileLogSink.LEVEL_RANKS.getOrDefault(level, 30) >= logLevelRank;
    }

    private void logToFile(String level, String message) {
        if (logSink != null) {
            logSink.write(level, message);
        }
    }

    private static boolean isLoopbackHost(String host) {
        if (host == null || host.isEmpty()) {
            return false;
        }
        String bare = host.replace("[", "").replace("]", "");
        String lower = bare.toLowerCase(Locale.ROOT);
        if (lower.equals("localhost") || lower.equals("localhost.localdomain")) {
            return true;
        }
        if (bare.indexOf(':') >= 0) {
            try {
                return InetAddress.getByName(bare).isLoopbackAddress();
            } catch (UnknownHostException notALiteral) {
                return false;
            }
        }
        String[] octets = bare.split("\\.", -1);
        if (octets.length != 4) {
            return false;
        }
        for (String octet : octets) {
            if (octet.isEmpty() || octet.length() > 3
                    || !octet.chars().allMatch(Character::isDigit)
                    || Integer.parseInt(octet) > 255) {
                return false;
            }
        }
        return Integer.parseInt(octets[0]) == 127;
    }

    private ApiResponse sendOnce(Transport.TransportRequest wireRequest,
            Map<String, String> callerHeaders, String accessToken, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, String pathTemplate) {
        Map<String, String> headers = buildRequestHeaders(callerHeaders, accessToken);
        Transport.TransportRequest attempt =
                wireRequest.withHeaders(headers, Redaction.redactMapping(headers));
        try {
            return transport.sendHttpRequest(attempt);
        } catch (SdkError error) {
            RequestInfo info = error.getRequestInfo();
            if (info != null) {
                reportFailedExchange(info, reportedBody, files, formEncoded, pathTemplate);
            }
            throw error;
        }
    }

    /** Merged case-insensitively: a caller's {@code authorization} never replaces the SDK's. */
    Map<String, String> buildRequestHeaders(Map<String, String> callerHeaders, String accessToken) {
        Map<String, String> canonical = new LinkedHashMap<>();
        Map<String, String> byLowerName = new LinkedHashMap<>();

        putHeader(canonical, byLowerName, "Accept", "application/json");
        putHeader(canonical, byLowerName, "User-Agent", config.getUserAgent());
        config.getDefaultHeaders().forEach((name, value) ->
                putHeader(canonical, byLowerName, name, value));
        if (callerHeaders != null) {
            for (Map.Entry<String, String> entry : callerHeaders.entrySet()) {
                putHeader(canonical, byLowerName, entry.getKey(), entry.getValue());
            }
        }
        if (accessToken != null) {
            byLowerName.remove("authorization");
            putHeader(canonical, byLowerName, "Authorization", "Bearer " + accessToken);
        }
        Map<String, String> merged = new LinkedHashMap<>();
        byLowerName.forEach((lower, value) -> merged.put(canonical.get(lower), value));
        return merged;
    }

    private static void validateCallerHeaders(Map<String, String> callerHeaders) {
        if (callerHeaders == null) {
            return;
        }
        for (Map.Entry<String, String> entry : callerHeaders.entrySet()) {
            validateCallerHeader(entry.getKey(), entry.getValue());
        }
    }

    private static void validateCallerHeader(String name, String value) {
        if (name == null) {
            throw new SdkUsageError("headers: a header name must be text, got null. The "
                    + "value beside it is not shown: it may be a credential");
        }
        if (value == null) {
            throw new SdkUsageError("headers: the value for '" + name
                    + "' must be text, got null — a null value would silently remove "
                    + "the header rather than send it empty");
        }
        validateHeaderName(name);
        validateHeaderValue(name, value);
    }

    private static void putHeader(Map<String, String> canonical, Map<String, String> byLowerName,
            String name, String value) {
        String lower = name.toLowerCase(Locale.ROOT);
        canonical.putIfAbsent(lower, name);
        if (Set.of("authorization", "accept", "user-agent").contains(lower)) {
            canonical.put(lower, lower.equals("authorization") ? "Authorization"
                    : lower.equals("accept") ? "Accept" : "User-Agent");
        }
        byLowerName.put(lower, value);
    }

    private static void validateHeaderName(String name) {
        if (name.indexOf('\r') >= 0 || name.indexOf('\n') >= 0) {
            throw new SdkUsageError("headers: a header name may not contain a line break, "
                    + "got " + Transport.quoteForMessage(name));
        }
        if (!Transport.isHttpToken(name)) {
            throw new SdkUsageError("headers: the name " + Transport.quoteForMessage(name)
                    + " is not an RFC 9110 token");
        }
    }

    /** Also refuses an edge space or tab, which okhttp would trim silently. */
    private static void validateHeaderValue(String name, String value) {
        if (value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0) {
            throw new SdkUsageError("headers: the value for " + Transport.quoteForMessage(name)
                    + " may not contain a line break");
        }
        if (Transport.hasEdgeWhitespace(value)) {
            throw new SdkUsageError("headers: the value for " + Transport.quoteForMessage(name)
                    + " may not begin or end with a space or a tab - okhttp trims both ends, "
                    + "so such a value would be sent altered rather than refused");
        }
        if (!Transport.isSendableHeaderValue(value)) {
            throw new SdkUsageError("headers: the value for " + Transport.quoteForMessage(name)
                    + " cannot be sent - a header value may carry only visible ASCII, space or "
                    + "tab. The value is not shown: it may be a credential");
        }
    }

    /** The absolute URL one listing page resolves to, with no query string built onto it. */
    String resolvePageUrl(String path) {
        return UrlBuilder.buildUrl(config.getBaseUrl(),
                UrlBuilder.substitutePathParams(path, seedApiVersion(path, null)));
    }

    private Map<String, Object> seedApiVersion(String path, Map<String, Object> pathParams) {
        Map<String, Object> resolved = pathParams == null
                ? new LinkedHashMap<>() : new LinkedHashMap<>(pathParams);
        if (UrlBuilder.findPathParamNames(path).contains(API_VERSION_PARAM)
                && !resolved.containsKey(API_VERSION_PARAM)) {
            resolved.put(API_VERSION_PARAM, config.getApiVersion());
        }
        return resolved;
    }

    private static void validatePath(String path) {
        if (path == null || path.strip().isEmpty()) {
            throw new SdkUsageError("path must name an endpoint, got '" + path + "'");
        }
    }

    /** Judges the already-resolved {@code jsonBody} and {@code files}, not {@code options}. */
    private static void validatePayload(RequestOptions options, Object jsonBody,
            Map<String, Object> files) {
        if (options.getJsonBody() != null && options.getFormData() != null) {
            throw new SdkUsageError("pass jsonBody or formData, not both — they are "
                    + "different content types and only one can be sent");
        }
        if (jsonBody != null && files != null) {
            throw new SdkUsageError("pass jsonBody or files, not both — a multipart "
                    + "body replaces the JSON one, so the JSON would be discarded unsent");
        }
        validateFiles(files);
        Transport.checkMultipartLabels(files, options.getFormData());
    }

    /** Render the JSON body once and judge those bytes, not a second rendering of the value. */
    private static byte[] encodeJsonBodyOnce(Object jsonBody) {
        if (jsonBody == null) {
            return null;
        }
        if (jsonBody instanceof CharSequence text) {
            String document = text.toString();
            validateJsonText(document);
            return document.getBytes(StandardCharsets.UTF_8);
        }
        if (!(jsonBody instanceof Map) && !(jsonBody instanceof List)) {
            throw new SdkUsageError("jsonBody must be a JSON string, a mapping or a "
                    + "list, got " + jsonBody.getClass().getSimpleName());
        }
        return Transport.encodeJsonBody(jsonBody)
                .getBytes(StandardCharsets.UTF_8);
    }

    private static void validateJsonText(String jsonBody) {
        JsonNode parsed;
        try {
            parsed = PROBE_JSON.readTree(jsonBody);
        } catch (JsonProcessingException error) {
            throw new SdkUsageError("jsonBody: not valid JSON: " + parseRefusal(error), error);
        }
        if (parsed == null || parsed.isMissingNode()) {
            throw new SdkUsageError("jsonBody: a JSON string must hold an object or an "
                    + "array, got an empty string");
        }
        if (!parsed.isObject() && !parsed.isArray()) {
            throw new SdkUsageError("jsonBody: a JSON string must hold an object or an "
                    + "array, got " + parsed.getNodeType().name().toLowerCase(Locale.ROOT)
                    + " — a bare value is valid JSON, but no endpoint here takes one "
                    + "as a body");
        }
    }

    private static String parseRefusal(
            JsonProcessingException error) {
        String message = error.getOriginalMessage();
        int blob = message.indexOf("[Source:");
        if (blob >= 0) {
            int opened = message.lastIndexOf('(', blob);
            message = message.substring(0, opened >= 0 ? opened : blob).strip();
        }
        JsonLocation location = error.getLocation();
        if (location == null) {
            return message;
        }
        return message + " at line " + location.getLineNr()
                + ", column " + location.getColumnNr();
    }

    private static void validateFiles(Map<String, Object> files) {
        if (files == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : files.entrySet()) {
            if (!Transport.isSendableFilePart(entry.getValue())) {
                throw Transport.refuseFilePart(entry.getKey());
            }
        }
    }

    private void validateContentType(RequestOptions options, Map<String, Object> files) {
        String where = "headers";
        String value = Transport.findContentType(options.getHeaders());
        if (value != null && !Transport.isMediaType(value)) {
            throw new SdkUsageError(Transport.refuseContentType(where, value));
        }
        if (value == null) {
            where = "config.defaultHeaders";
            value = Transport.findContentType(config.getDefaultHeaders());
        }
        if (value == null) {
            return;
        }
        String conflict = bodyConflict(value, where,
                options.getJsonBody() != null,
                options.getFormData() != null,
                // In step with the transport: only a non-empty map takes the multipart branch.
                files != null && !files.isEmpty());
        if (conflict != null) {
            throw new SdkUsageError(conflict);
        }
    }

    /** Refuse a query name the URL already carries; the store would choose between two values. */
    private static void rejectDuplicatedQueryParams(String url, Map<String, Object> params) {
        if (params.isEmpty() || !url.contains("?")) {
            return;
        }
        Set<String> existing = new HashSet<>();
        for (String chunk : url.substring(url.indexOf('?') + 1).split("&")) {
            int equals = chunk.indexOf('=');
            String name = equals < 0 ? chunk : chunk.substring(0, equals);
            existing.add(decodeQueryName(name));
        }
        List<String> clash = params.keySet().stream().filter(existing::contains).sorted().toList();
        if (!clash.isEmpty()) {
            throw new SdkUsageError("queryParams " + String.join(", ", clash)
                    + " already appear in the path's own query string, so each would be "
                    + "sent twice with two values and the store chooses — pass them once, "
                    + "in one place");
        }
    }

    private static String decodeQueryName(String name) {
        try {
            return URLDecoder.decode(name, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException error) {
            return name;
        }
    }

    /** Whether a 401 means the access token rather than the request. */
    static boolean meansExpiredToken(ApiResponse response, String method) {
        if (response.getStatusCode() != 401) {
            return false;
        }
        if (!(response.getMap() instanceof Map<?, ?> body)) {
            return !RetryPolicy.isUnsafeMethod(method);
        }
        if (TOKEN_FAILURE_ERROR.equals(normalisedReason(body.get("error")))) {
            return true;
        }
        return TOKEN_FAILURE_MESSAGES.contains(normalisedReason(body.get("message")));
    }

    private static String normalisedReason(Object value) {
        return value == null ? "" : String.valueOf(value).strip().toLowerCase(Locale.ROOT);
    }

    private void reportProgress(String message) {
        if (config.isPrintApiInfo() && !config.isPrintOnlyOnError()) {
            System.out.println(message);
        }
        if (shouldLog("INFO")) {
            logger.info("{}", message);
        }
        logToFile("INFO", message);
    }

    private static String exchangeRecord(String pathTemplate, RequestInfo info, Object status) {
        return String.format("%s %s -> %s in %ss (attempt %s/%s)",
                info.getHttpMethod(), Transport.escapeForMessage(String.valueOf(pathTemplate)),
                status, info.getElapsedSeconds(), info.getAttemptCount(), info.getMaxAttempts());
    }

    /** The ERROR-level twin of {@link #reportExchange}; {@code printOnlyOnError} never hides it. */
    private void reportFailedExchange(RequestInfo info, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, String pathTemplate) {
        if (wantsPrint()) {
            System.out.println(Console.renderFailedExchange(
                    info, reportedBody, files, formEncoded, config));
        }
        String line = exchangeRecord(pathTemplate, info, Console.NO_RESPONSE_STATUS);
        if (shouldLog("ERROR")) {
            logger.error("{}", line);
        }
        logToFile("ERROR", line);
        logBodies(reportedBody, null);
    }

    private boolean wantsPrint() {
        return config.isPrintApiInfo() || config.isPrintRequestPayload()
                || config.isPrintResponseBody() || config.isPrintHeaders() || config.isPrintCurl();
    }

    private void reportExchange(ApiResponse response, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, boolean warning,
            String pathTemplate) {
        RequestInfo info = response.getRequestInfo();
        if (info != null && wantsPrint()
                && !(config.isPrintOnlyOnError() && response.succeeded())) {
            System.out.println(Console.renderExchange(
                    response, reportedBody, files, formEncoded, config));
        }
        if (info != null) {
            String line = exchangeRecord(pathTemplate, info, response.getStatusCode());
            if (warning) {
                if (shouldLog("WARNING")) {
                    logger.warn("{}", line);
                }
            } else if (shouldLog("INFO")) {
                logger.info("{}", line);
            }
            logToFile(warning ? "WARNING" : "INFO", line);
        }
        logBodies(reportedBody, response.getMap());
    }

    /** Redacted and neutralised, so a server-controlled newline cannot forge a log line. */
    private void logBodies(Object reportedBody, Object responseData) {
        if (!config.isLogBodies()) {
            return;
        }
        String bodies = "payload="
                + Console.neutralise(
                        Console.renderJsonValue(Redaction.redactValue(reportedBody), true))
                + " response="
                + Console.neutralise(
                        Console.renderJsonValue(Redaction.redactValue(responseData), true));
        if (shouldLog("DEBUG")) {
            logger.debug("{}", bodies);
        }
        logToFile("DEBUG", bodies);
    }

    private static String mediaTypeOf(String value) {
        int semicolon = value.indexOf(';');
        String head = semicolon < 0 ? value : value.substring(0, semicolon);
        return head.strip().toLowerCase(Locale.ROOT);
    }

    private static String bodyConflict(String value, String where,
            boolean hasJson, boolean hasForm, boolean hasFiles) {
        String prefix = where + ": Content-Type " + Transport.quoteForMessage(value)
                + " cannot be sent with this request";
        String mediaType = mediaTypeOf(value);

        if (hasFiles) {
            return prefix + " — it sends files; leave Content-Type unset for multipart";
        }
        if (hasForm && !FORM_MEDIA_TYPE.equals(mediaType)) {
            return prefix + " — it sends formData, which goes on the wire as " + FORM_MEDIA_TYPE;
        }
        if (hasJson && !isJsonMediaType(mediaType)) {
            return prefix + " — it sends jsonBody, which goes on the wire as " + JSON_MEDIA_TYPE;
        }
        return null;
    }

    private static boolean isJsonMediaType(String mediaType) {
        return JSON_MEDIA_TYPE.equals(mediaType)
                || (mediaType.startsWith(JSON_TREE)
                        && mediaType.endsWith(JSON_SUFFIX)
                        && mediaType.length() > JSON_TREE.length() + JSON_SUFFIX.length());
    }

}
