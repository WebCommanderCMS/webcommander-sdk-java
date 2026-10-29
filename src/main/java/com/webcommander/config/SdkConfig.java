package com.webcommander.config;

import com.webcommander.errors.SdkConfigError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.http.RetryPolicy;
import com.webcommander.http.Transport;
import com.webcommander.http.UrlBuilder;
import com.webcommander.Redaction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/** Immutable SDK configuration — timeouts, retries, TLS, logging, printing. */
public final class SdkConfig {

    public static final String DEFAULT_API_VERSION = "v4";
    public static final double DEFAULT_TIMEOUT_SECONDS = 30.0;
    public static final double DEFAULT_CONNECT_TIMEOUT_SECONDS = 10.0;
    /** Total attempts including the first, so 1 disables retrying. */
    public static final int DEFAULT_MAX_RETRIES = 1;
    public static final double DEFAULT_RETRY_BACKOFF_SECONDS = 0.5;
    /** Default ceiling on one wait, in seconds; bounds a server's {@code Retry-After} too. */
    public static final double DEFAULT_RETRY_BACKOFF_MAX_SECONDS = 20.0;
    public static final int DEFAULT_POOL_MAXSIZE = 10;
    /** Response body ceiling. */
    public static final long DEFAULT_MAX_RESPONSE_BYTES = 64L * 1024 * 1024;
    /** The encoded body with framing, not the sources. */
    public static final long DEFAULT_MAX_REQUEST_BYTES = 64L * 1024 * 1024;
    /** Every file read, every {@code byte[]} and every form field's wire text together. */
    public static final long DEFAULT_MAX_UPLOAD_BYTES = 64L * 1024 * 1024;
    /** Every boundary line and part header block, none of the content. */
    public static final long DEFAULT_MAX_MULTIPART_OVERHEAD_BYTES = 1024L * 1024;
    public static final long DEFAULT_MAX_TOKEN_CACHE_BYTES = 1024L * 1024;
    /** CA bundle ceiling. */
    public static final long DEFAULT_MAX_CA_BUNDLE_BYTES = 8L * 1024 * 1024;
    /** Nothing routine is recorded until a caller lowers it. */
    public static final String DEFAULT_LOG_LEVEL = "WARNING";
    public static final String PRINT_FORMAT_PRETTY = "pretty";
    public static final String PRINT_FORMAT_COMPACT = "compact";
    public static final String DEFAULT_PRINT_FORMAT = PRINT_FORMAT_PRETTY;

    /** {@code responseFormat}: {@code getData()} is the decoded map, list or bytes. */
    public static final String RESPONSE_FORMAT_MAP = "map";
    /** {@code responseFormat}: {@code getData()} is the body's own text. */
    public static final String RESPONSE_FORMAT_JSON = "json";
    /** {@code responseFormat}: {@code getData()} is the typed view. */
    public static final String RESPONSE_FORMAT_DTO = "dto";
    public static final String DEFAULT_RESPONSE_FORMAT = RESPONSE_FORMAT_MAP;
    /** The lowest-priority tier: anything set in code wins over it. */
    public static final String RESPONSE_FORMAT_ENVIRONMENT_VARIABLE = "WC_RESPONSE_FORMAT";

    /** A sentinel that never looks like a release. Do not change it to a version number. */
    private static final String FALLBACK_VERSION = "0.0.0+source";

    /** From the jar manifest when packaged; the source-tree literal otherwise. */
    public static final String SDK_VERSION = readImplementationVersion();
    public static final String DEFAULT_USER_AGENT = "webcommander/" + SDK_VERSION;

    /** In severity order, which is the order the message lists them in. */
    private static final List<String> VALID_LOG_LEVELS =
            List.of("DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL");
    private static final Set<String> VALID_PRINT_FORMATS =
            Set.of(PRINT_FORMAT_PRETTY, PRINT_FORMAT_COMPACT);
    /** The three response formats, in the order every refusal lists them. */
    public static final List<String> RESPONSE_FORMATS =
            List.of(RESPONSE_FORMAT_MAP, RESPONSE_FORMAT_JSON, RESPONSE_FORMAT_DTO);
    /** ASCII digits with no leading zero: {@code v04} is not a mounted version. */
    private static final Pattern API_VERSION_PATTERN =
            Pattern.compile("^v?[1-9][0-9]*$", Pattern.CASE_INSENSITIVE);
    /** 100-599 is every status there is; the ceiling bounds a runaway collection. */
    private static final int MAX_HTTP_CODES = 500;

    private final AuthConfig auth;
    private final String apiVersion;
    private final double timeoutSeconds;
    private final double connectTimeoutSeconds;
    private final int maxRetries;
    private final Set<Integer> retryableHttpCodes;
    private final Set<Integer> unsafeRetryableHttpCodes;
    private final double retryBackoffSeconds;
    private final double retryBackoffMaxSeconds;
    private final boolean verifySsl;
    private final String caBundle;
    private final String proxyUrl;
    private final int poolMaxsize;
    private final long maxResponseBytes;
    private final long maxRequestBytes;
    private final long maxUploadBytes;
    private final long maxMultipartOverheadBytes;
    private final long maxTokenCacheBytes;
    private final long maxCaBundleBytes;
    private final Map<String, String> defaultHeaders;
    private final boolean raiseOnError;
    private final String responseFormat;
    private final String userAgent;
    private final String logLevel;
    private final boolean logBodies;
    private final String logFile;
    private final boolean printApiInfo;
    private final boolean printRequestPayload;
    private final boolean printResponseBody;
    private final boolean printHeaders;
    private final boolean printCurl;
    private final boolean printOnlyOnError;
    private final String printFormat;
    private final boolean printColor;
    private final int printMaxChars;

    private SdkConfig(Builder builder) {
        this.auth = builder.auth;
        this.apiVersion = normaliseApiVersion(builder.apiVersion);
        this.timeoutSeconds = builder.timeoutSeconds;
        this.connectTimeoutSeconds = builder.connectTimeoutSeconds;
        this.maxRetries = builder.maxRetries;
        // Not Set.copyOf: it throws a bare NullPointerException before validate() can report it.
        this.retryableHttpCodes = copyCodes(builder.retryableHttpCodes);
        this.unsafeRetryableHttpCodes = copyCodes(builder.unsafeRetryableHttpCodes);
        this.retryBackoffSeconds = builder.retryBackoffSeconds;
        this.retryBackoffMaxSeconds = builder.retryBackoffMaxSeconds;
        this.verifySsl = builder.verifySsl;
        this.caBundle = builder.caBundle;
        this.proxyUrl = builder.proxyUrl;
        this.poolMaxsize = builder.poolMaxsize;
        this.maxResponseBytes = builder.maxResponseBytes;
        this.maxRequestBytes = builder.maxRequestBytes;
        this.maxUploadBytes = builder.maxUploadBytes;
        this.maxMultipartOverheadBytes = builder.maxMultipartOverheadBytes;
        this.maxTokenCacheBytes = builder.maxTokenCacheBytes;
        this.maxCaBundleBytes = builder.maxCaBundleBytes;
        this.defaultHeaders = builder.defaultHeaders == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(builder.defaultHeaders));
        this.raiseOnError = builder.raiseOnError;
        this.responseFormat = foldResponseFormat(builder.responseFormat);
        this.userAgent = builder.userAgent;
        this.logLevel = builder.logLevel == null
                ? null : builder.logLevel.strip().toUpperCase(Locale.ROOT);
        this.logBodies = builder.logBodies;
        this.logFile = builder.logFile;
        this.printApiInfo = builder.printApiInfo;
        this.printRequestPayload = builder.printRequestPayload;
        this.printResponseBody = builder.printResponseBody;
        this.printHeaders = builder.printHeaders;
        this.printCurl = builder.printCurl;
        this.printOnlyOnError = builder.printOnlyOnError;
        this.printFormat = builder.printFormat == null
                ? null : builder.printFormat.strip().toLowerCase(Locale.ROOT);
        this.printColor = builder.printColor;
        this.printMaxChars = builder.printMaxChars;
        validate();
    }

    /** The credentials are fixed here, the one thing a builder cannot be told later. */
    public static Builder builder(AuthConfig auth) {
        return new Builder(auth);
    }

    /** This config is untouched, and {@code responseFormat} comes across already resolved. */
    public Builder toBuilder() {
        Builder copy = new Builder(auth);
        copy.apiVersion = apiVersion;
        copy.timeoutSeconds = timeoutSeconds;
        copy.connectTimeoutSeconds = connectTimeoutSeconds;
        copy.maxRetries = maxRetries;
        copy.retryableHttpCodes = retryableHttpCodes;
        copy.unsafeRetryableHttpCodes = unsafeRetryableHttpCodes;
        copy.retryBackoffSeconds = retryBackoffSeconds;
        copy.retryBackoffMaxSeconds = retryBackoffMaxSeconds;
        copy.verifySsl = verifySsl;
        copy.caBundle = caBundle;
        copy.proxyUrl = proxyUrl;
        copy.poolMaxsize = poolMaxsize;
        copy.maxResponseBytes = maxResponseBytes;
        copy.maxRequestBytes = maxRequestBytes;
        copy.maxUploadBytes = maxUploadBytes;
        copy.maxMultipartOverheadBytes = maxMultipartOverheadBytes;
        copy.maxTokenCacheBytes = maxTokenCacheBytes;
        copy.maxCaBundleBytes = maxCaBundleBytes;
        copy.defaultHeaders = new LinkedHashMap<>(defaultHeaders);
        copy.raiseOnError = raiseOnError;
        copy.responseFormat = responseFormat;
        copy.userAgent = userAgent;
        copy.logLevel = logLevel;
        copy.logBodies = logBodies;
        copy.logFile = logFile;
        copy.printApiInfo = printApiInfo;
        copy.printRequestPayload = printRequestPayload;
        copy.printResponseBody = printResponseBody;
        copy.printHeaders = printHeaders;
        copy.printCurl = printCurl;
        copy.printOnlyOnError = printOnlyOnError;
        copy.printFormat = printFormat;
        copy.printColor = printColor;
        copy.printMaxChars = printMaxChars;
        return copy;
    }

    public SdkConfig withOverrides(UnaryOperator<Builder> overrides) {
        Builder builder = toBuilder();
        return (overrides == null ? builder : overrides.apply(builder)).build();
    }

    public static SdkConfig fromCredentials(List<?> credentials) {
        return fromCredentials(credentials, null, null);
    }

    public static SdkConfig fromCredentials(List<?> credentials, Object instance) {
        return fromCredentials(credentials, instance, null);
    }

    /** {@code instance}: {@code null}, an index, or one entry's {@code clientName}. */
    public static SdkConfig fromCredentials(List<?> credentials, Object instance,
            UnaryOperator<Builder> overrides) {
        Object chosen = selectCredentials(credentials, instance);
        if (chosen instanceof SdkConfig config) {
            return overrides == null ? config : overrides.apply(config.toBuilder()).build();
        }
        if (chosen instanceof AuthConfig auth) {
            Builder builder = builder(auth);
            return (overrides == null ? builder : overrides.apply(builder)).build();
        }
        if (chosen instanceof Map<?, ?> mapping) {
            Builder builder = fromMapping(mapping, instance);
            return (overrides == null ? builder : overrides.apply(builder)).build();
        }
        throw configTypeRefusal(chosen, instance == null ? "0" : instance);
    }

    /** Build a builder from a mapping of config fields, the way a loader hands them over. */
    private static Builder fromMapping(Map<?, ?> block, Object instance) {
        String where = "credentials[" + (instance == null ? "0"
                : instance instanceof String name ? "'" + name + "'" : instance) + "]";
        Map<String, Object> remaining = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : block.entrySet()) {
            if (!(entry.getKey() instanceof String key)) {
                throw new SdkConfigError(where + ": a config field name must be text, got "
                        + (entry.getKey() == null ? "null"
                                : entry.getKey().getClass().getSimpleName()));
            }
            remaining.put(key, entry.getValue());
        }

        boolean carriesAuth = remaining.containsKey("auth");
        Object suppliedAuth = remaining.remove("auth");

        Set<String> authNames = AuthConfig.fieldNames();
        Map<String, Object> authValues = new LinkedHashMap<>();
        for (String name : new ArrayList<>(remaining.keySet())) {
            if (authNames.contains(name)) {
                authValues.put(name, remaining.remove(name));
            }
        }

        if (carriesAuth && !authValues.isEmpty()) {
            throw new SdkConfigError("auth: an AuthConfig was supplied alongside the "
                    + "credential field(s) " + String.join(", ",
                            new TreeSet<>(authValues.keySet()))
                    + "; pass one or the other");
        }

        AuthConfig auth;
        if (carriesAuth) {
            if (!(suppliedAuth instanceof AuthConfig supplied)) {
                throw new SdkConfigError(where + ": auth must be an AuthConfig, got "
                        + (suppliedAuth == null ? "null"
                                : suppliedAuth.getClass().getSimpleName()));
            }
            auth = supplied;
        } else {
            List<String> absent = AuthConfig.absentRequiredFields(authValues.keySet());
            if (!absent.isEmpty()) {
                throw new SdkConfigError(
                        "credentials: missing required field(s): " + String.join(", ", absent));
            }
            AuthConfig.Builder authBuilder = new AuthConfig.Builder();
            for (Map.Entry<String, Object> entry : authValues.entrySet()) {
                assign(authBuilder, entry.getKey(), entry.getValue(), "credentials");
            }
            auth = authBuilder.build();
        }

        Builder builder = builder(auth);
        for (Map.Entry<String, Object> entry : remaining.entrySet()) {
            assign(builder, entry.getKey(), entry.getValue(), "credentials");
        }
        return builder;
    }

    /** A key with no matching setter is refused by name rather than ignored. */
    private static void assign(Object builder, String key, Object value, String where) {
        String setter = AuthConfig.camelCase(key);
        List<Method> named = new ArrayList<>();
        for (Method method : builder.getClass().getMethods()) {
            if (method.getName().equals(setter) && method.getParameterCount() == 1) {
                named.add(method);
            }
        }
        if (named.isEmpty()) {
            List<String> known = new ArrayList<>();
            for (Method method : builder.getClass().getDeclaredMethods()) {
                if (method.getParameterCount() == 1
                        && Modifier.isPublic(method.getModifiers())) {
                    known.add(AuthConfig.snakeCase(method.getName()));
                }
            }
            Collections.sort(known);
            throw new SdkConfigError(where + ": unknown config field: " + key
                    + "; known: " + String.join(", ", known));
        }
        for (Method method : named) {
            Object coerced = coerce(value, method.getParameterTypes()[0]);
            if (coerced == NOT_COERCIBLE) {
                continue;
            }
            try {
                method.invoke(builder, coerced);
                return;
            } catch (ReflectiveOperationException failure) {
                throw new SdkConfigError(where + ": " + key + " could not be applied: "
                        + failure.getCause());
            }
        }
        throw new SdkConfigError(where + ": " + key + ": expected "
                + named.get(0).getParameterTypes()[0].getSimpleName() + ", got "
                + (value == null ? "null" : value.getClass().getSimpleName()));
    }

    /** Returned by {@link #coerce} when a value cannot become the wanted type. */
    private static final Object NOT_COERCIBLE = new Object();

    /** Every arm is a shape a JSON, YAML or {@code .env} loader really produces. */
    private static Object coerce(Object value, Class<?> wanted) {
        if (value == null) {
            return wanted.isPrimitive() ? NOT_COERCIBLE : null;
        }
        if (Map.class.isAssignableFrom(wanted) && value instanceof Map<?, ?> entries) {
            // Before isInstance: erasure would let a raw Map through to a ClassCastException.
            Map<String, String> flattened = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : entries.entrySet()) {
                if (!(entry.getKey() instanceof String name)) {
                    return NOT_COERCIBLE;
                }
                Object item = entry.getValue();
                if (!(item instanceof String) && !(item instanceof Number)
                        && !(item instanceof Boolean)) {
                    return NOT_COERCIBLE;
                }
                flattened.put(name, String.valueOf(item));
            }
            return flattened;
        }
        if (wanted.isInstance(value)) {
            return value;
        }
        if ((wanted == double.class || wanted == Double.class) && value instanceof Number number) {
            return number.doubleValue();
        }
        if ((wanted == int.class || wanted == Integer.class) && value instanceof Number number
                && number.doubleValue() == Math.rint(number.doubleValue())) {
            return number.intValue();
        }
        if ((wanted == boolean.class || wanted == Boolean.class)
                && value instanceof Boolean flag) {
            return flag;
        }
        if (Set.class.isAssignableFrom(wanted) && value instanceof Collection<?> items) {
            Set<Integer> codes = new LinkedHashSet<>();
            for (Object item : items) {
                if (!(item instanceof Number number)) {
                    return NOT_COERCIBLE;
                }
                codes.add(number.intValue());
            }
            return codes;
        }
        return NOT_COERCIBLE;
    }

    private static void requireConfigType(Object block, Object where) {
        if (!(block instanceof SdkConfig || block instanceof AuthConfig || block instanceof Map)) {
            throw configTypeRefusal(block, where);
        }
    }

    private static SdkConfigError configTypeRefusal(Object block, Object where) {
        return new SdkConfigError("credentials[" + where + "]: expected an SdkConfig, an "
                + "AuthConfig or a mapping of config fields, got "
                + (block == null ? "null" : block.getClass().getSimpleName()));
    }

    /** The single selection rule, shared with {@code ClientRegistry.buildClient}. */
    public static Object selectCredentials(List<?> credentials, Object instance) {
        if (credentials == null || credentials.isEmpty()) {
            throw new SdkUsageError("no credentials were supplied");
        }
        List<Object> blocks = new ArrayList<>(credentials);
        if (instance == null) {
            return blocks.get(0);
        }
        if (instance instanceof Byte || instance instanceof Short
                || instance instanceof Integer || instance instanceof Long) {
            long position = ((Number) instance).longValue();
            int size = blocks.size();
            if (position >= -size && position < size) {
                return blocks.get((int) (((position % size) + size) % size));
            }
            throw new SdkUsageError("no credentials at index " + position + "; "
                    + size + " supplied (0-" + (size - 1) + ")");
        }
        if (instance instanceof String name) {
            List<Integer> matched = new ArrayList<>();
            for (int index = 0; index < blocks.size(); index++) {
                if (name.equals(readClientName(blocks.get(index)))) {
                    matched.add(index);
                }
            }
            if (matched.size() > 1) {
                List<String> positions = new ArrayList<>();
                for (Integer index : matched) {
                    positions.add(String.valueOf(index));
                }
                throw new SdkUsageError("instance '" + name + "' is defined more than "
                        + "once, at index " + String.join(" and ", positions)
                        + "; clientName has to identify one entry");
            }
            if (matched.size() == 1) {
                return blocks.get(matched.get(0));
            }
            for (int index = 0; index < blocks.size(); index++) {
                requireConfigType(blocks.get(index), index);
            }
            List<String> available = new ArrayList<>();
            for (int index = 0; index < blocks.size(); index++) {
                String label = readClientName(blocks.get(index));
                available.add(label != null ? label : "<index " + index + ">");
            }
            throw new SdkUsageError("unknown instance '" + name + "'; available: "
                    + String.join(", ", available));
        }
        throw new SdkUsageError("instance must be null, an index or a clientName");
    }

    /** An unsupported type is answered, not refused: this builds the message reporting it. */
    public static String readClientName(Object block) {
        if (block instanceof SdkConfig config) {
            return config.getClientName();
        }
        if (block instanceof AuthConfig auth) {
            return auth.getClientName();
        }
        if (block instanceof Map<?, ?> mapping) {
            Object name = mapping.containsKey("client_name")
                    ? mapping.get("client_name") : mapping.get("clientName");
            if (name instanceof String text) {
                return text;
            }
            Object auth = mapping.get("auth");
            if (auth instanceof AuthConfig supplied) {
                return supplied.getClientName();
            }
        }
        return null;
    }

    private void validate() {
        List<String> problems = new ArrayList<>();
        if (auth == null) {
            problems.add("auth: an AuthConfig is required");
        }
        if (apiVersion == null || !API_VERSION_PATTERN.matcher(apiVersion).matches()) {
            problems.add("apiVersion: expected something like 'v4' or '4', got "
                    + Transport.quoteForMessage(apiVersion));
        }
        if (maxRetries < 1) {
            problems.add("maxRetries: must be at least 1 (1 disables retry), got " + maxRetries);
        }
        if (!finitePositive(timeoutSeconds)) {
            problems.add("timeoutSeconds: must be a finite number > 0");
        }
        if (!finitePositive(connectTimeoutSeconds)) {
            problems.add("connectTimeoutSeconds: must be a finite number > 0");
        }
        if (Double.isNaN(retryBackoffSeconds) || Double.isInfinite(retryBackoffSeconds)
                || retryBackoffSeconds < 0) {
            problems.add("retryBackoffSeconds: must be a finite number >= 0");
        }
        if (Double.isNaN(retryBackoffMaxSeconds) || Double.isInfinite(retryBackoffMaxSeconds)
                || retryBackoffMaxSeconds < retryBackoffSeconds) {
            problems.add("retryBackoffMaxSeconds: must be finite and >= retryBackoffSeconds");
        }
        if (poolMaxsize < 1) {
            problems.add("poolMaxsize: must be at least 1");
        }
        if (maxResponseBytes < 1) {
            problems.add("maxResponseBytes: must be at least 1, got " + maxResponseBytes);
        }
        if (maxRequestBytes < 1) {
            problems.add("maxRequestBytes: must be at least 1, got " + maxRequestBytes);
        }
        if (maxUploadBytes < 1) {
            problems.add("maxUploadBytes: must be at least 1, got " + maxUploadBytes);
        }
        if (maxMultipartOverheadBytes < 1) {
            problems.add("maxMultipartOverheadBytes: must be at least 1, got "
                    + maxMultipartOverheadBytes);
        }
        if (maxTokenCacheBytes < 1) {
            problems.add("maxTokenCacheBytes: must be at least 1, got " + maxTokenCacheBytes);
        }
        if (maxCaBundleBytes < 1) {
            problems.add("maxCaBundleBytes: must be at least 1, got " + maxCaBundleBytes);
        }
        validateCodes(problems, "retryableHttpCodes", retryableHttpCodes);
        validateCodes(problems, "unsafeRetryableHttpCodes", unsafeRetryableHttpCodes);
        if (logLevel == null || !VALID_LOG_LEVELS.contains(logLevel)) {
            problems.add("logLevel: expected one of " + String.join(", ", VALID_LOG_LEVELS)
                    + ", got " + Transport.quoteForMessage(logLevel));
        }
        if (printFormat == null || !VALID_PRINT_FORMATS.contains(printFormat)) {
            problems.add("printFormat: expected one of " + PRINT_FORMAT_PRETTY + ", "
                    + PRINT_FORMAT_COMPACT + ", got " + Transport.quoteForMessage(printFormat));
        }
        if (printMaxChars < 0) {
            problems.add("printMaxChars: must be >= 0 (0 = unlimited)");
        }
        if (responseFormat == null || !RESPONSE_FORMATS.contains(responseFormat)) {
            problems.add("responseFormat: expected one of "
                    + String.join(", ", RESPONSE_FORMATS) + ", got "
                    + Transport.quoteForMessage(responseFormat)
                    + " (it may have come from " + RESPONSE_FORMAT_ENVIRONMENT_VARIABLE + ", "
                    + "which is read once here and is the lowest-priority default)");
        }
        if (userAgent == null || userAgent.strip().isEmpty()) {
            problems.add("userAgent: must not be blank");
        } else if (!Transport.isSendableHeaderValue(userAgent)
                || Transport.hasEdgeWhitespace(userAgent)) {
            problems.add("userAgent: " + Transport.quoteForMessage(userAgent)
                    + " cannot be sent as a header value");
        }
        if (caBundle != null && !caBundle.strip().isEmpty()) {
            if (!verifySsl) {
                problems.add("caBundle and verifySsl=false are contradictory: one supplies a "
                        + "trust store, the other disables verification");
            }
            if (!Files.isRegularFile(Path.of(caBundle))) {
                problems.add("caBundle: no such file: " + Transport.quoteForMessage(caBundle));
            } else {
                problems.addAll(caBundleSizeProblems());
            }
        }
        if (proxyUrl != null && !proxyUrl.isBlank()) {
            problems.addAll(proxyUrlProblems(proxyUrl));
        }
        problems.addAll(headerProblems(defaultHeaders));

        if (!problems.isEmpty()) {
            throw new SdkConfigError(String.join("; ", problems));
        }
    }

    /** Refused before anything opens it, so the refusal names the option at construction. */
    private List<String> caBundleSizeProblems() {
        try {
            long size = Files.size(Path.of(caBundle));
            if (size > maxCaBundleBytes) {
                return List.of("caBundle: "
                        + Redaction.redactPath(caBundle) + " is " + size
                        + " bytes, over the " + maxCaBundleBytes
                        + "-byte maxCaBundleBytes ceiling - raise it with "
                        + "SdkConfig.builder(...).maxCaBundleBytes(bytes) if the bundle is "
                        + "meant to be that large");
            }
        } catch (IOException unreadable) {
            return List.of("caBundle: " + Redaction.redactPath(caBundle)
                    + " could not be measured: "
                    + Redaction.describeFailure(unreadable));
        }
        return List.of();
    }

    /** A scheme-less {@code proxy.example:3128} otherwise fails at request time naming no field. */
    private static List<String> proxyUrlProblems(String proxyUrl) {
        UrlBuilder.Authority parsed = UrlBuilder.Authority.split(proxyUrl);
        if (parsed.isPortMalformed()) {
            return List.of("proxyUrl: is not a URL (unreadable port, or an unclosed "
                    + "IPv6 literal). The value is not shown: it may carry credentials");
        }
        if (parsed.getScheme().isEmpty() || parsed.getHost().isEmpty()) {
            return List.of("proxyUrl: must carry a scheme and a host, like "
                    + "http://proxy.example:3128 or socks5://proxy.example:1080. "
                    + "The value is not shown: it may carry credentials");
        }
        if (parsed.getScheme().startsWith("socks") && parsed.getUserInfo() != null
                && !parsed.getUserInfo().isEmpty()) {
            return List.of("proxyUrl: a socks proxy with a username and password is not "
                    + "supported; use an http:// proxy");
        }
        return List.of();
    }

    /** Ascending: {@code Set.of(...)} iterates differently per JVM run, and a null sorts last. */
    private static Set<Integer> copyCodes(Set<Integer> codes) {
        if (codes == null) {
            return null;
        }
        List<Integer> ordered = new ArrayList<>(codes);
        ordered.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        return Collections.unmodifiableSet(new LinkedHashSet<>(ordered));
    }

    private static void validateCodes(List<String> problems, String name, Set<Integer> codes) {
        if (codes == null) {
            problems.add(name + ": expected a set of HTTP status codes, got null");
            return;
        }
        if (codes.size() > MAX_HTTP_CODES) {
            problems.add(name + ": at most " + MAX_HTTP_CODES + " status codes (100-599 is "
                    + "every code there is), got more than that");
            return;
        }
        for (Integer code : codes) {
            if (code == null || code < 100 || code > 599) {
                problems.add(name + ": " + code + " is not an HTTP status code");
            }
        }
    }

    private static List<String> headerProblems(Map<String, String> headers) {
        List<String> problems = new ArrayList<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            boolean badName = !Transport.isHttpToken(name);
            boolean badValue = !Transport.isSendableHeaderValue(value)
                    || Transport.hasEdgeWhitespace(value);
            if (badName || badValue) {
                problems.add("defaultHeaders: " + Transport.quoteForMessage(name)
                        + " cannot be sent as a header");
                continue;
            }
            if (CONTENT_TYPE_HEADER.equalsIgnoreCase(name) && !Transport.isMediaType(value)) {
                problems.add(Transport.refuseContentType("defaultHeaders", value));
            }
        }
        return problems;
    }

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    public AuthConfig getAuth() {
        return auth;
    }

    /** Normalised at construction, so {@code " 4 "} reads back as {@code "v4"}. */
    public String getApiVersion() {
        return apiVersion;
    }

    /** The whole attempt: DNS, connecting, writing, thinking time and reading the body. */
    public double getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /** Inside {@link #getTimeoutSeconds()}, so a smaller ceiling leaves this unreachable. */
    public double getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /** Immutable and in ascending order, so two configs render alike on every run. */
    public Set<Integer> getRetryableHttpCodes() {
        return retryableHttpCodes;
    }

    /** The narrower set a state-changing method must also match before it is repeated. */
    public Set<Integer> getUnsafeRetryableHttpCodes() {
        return unsafeRetryableHttpCodes;
    }

    /** The base of the doubling window; 0 retries immediately. */
    public double getRetryBackoffSeconds() {
        return retryBackoffSeconds;
    }

    public double getRetryBackoffMaxSeconds() {
        return retryBackoffMaxSeconds;
    }

    /** False disables verification everywhere and cannot be combined with a {@code caBundle}. */
    public boolean isVerifySsl() {
        return verifySsl;
    }

    /** Checked to exist at construction; {@code null} uses the platform's own roots. */
    public String getCaBundle() {
        return caBundle;
    }

    /** May hold userinfo, so treat it as a credential. */
    public String getProxyUrl() {
        return proxyUrl;
    }

    public int getPoolMaxsize() {
        return poolMaxsize;
    }

    public long getMaxResponseBytes() {
        return maxResponseBytes;
    }

    public long getMaxRequestBytes() {
        return maxRequestBytes;
    }

    public long getMaxUploadBytes() {
        return maxUploadBytes;
    }

    public long getMaxMultipartOverheadBytes() {
        return maxMultipartOverheadBytes;
    }

    public long getMaxTokenCacheBytes() {
        return maxTokenCacheBytes;
    }

    public long getMaxCaBundleBytes() {
        return maxCaBundleBytes;
    }

    /** A fresh mutable copy, in the order given, so a caller's edits cannot reach the config. */
    public Map<String, String> getDefaultHeaders() {
        return new LinkedHashMap<>(defaultHeaders);
    }

    /** True throws an {@code SdkError}; false, the default, answers an {@code ApiResponse}. */
    public boolean isRaiseOnError() {
        return raiseOnError;
    }

    /** Already resolved: what was set in code, else the environment variable, else the default. */
    public String getResponseFormat() {
        return responseFormat;
    }

    /** {@code null} is refused, so a tier meaning "nothing was set" must not call this. */
    public static String normaliseResponseFormat(String responseFormat) {
        String folded = foldResponseFormat(responseFormat);
        if (folded == null || !RESPONSE_FORMATS.contains(folded)) {
            throw new SdkUsageError("responseFormat: expected one of "
                    + String.join(", ", RESPONSE_FORMATS) + ", got "
                    + Transport.quoteForMessage(responseFormat));
        }
        return folded;
    }

    /** The one spelling of "the same format": {@code " DTO "} and {@code "dto"} are it. */
    private static String foldResponseFormat(String responseFormat) {
        return responseFormat == null ? null : responseFormat.strip().toLowerCase(Locale.ROOT);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getLogLevel() {
        return logLevel;
    }

    /** True records them redacted; false, the default, records only method, URL and status. */
    public boolean isLogBodies() {
        return logBodies;
    }

    /** Appended in UTF-8 and released when the client closes. */
    public String getLogFile() {
        return logFile;
    }

    /** False by default, so a library using this SDK stays silent. */
    public boolean isPrintApiInfo() {
        return printApiInfo;
    }

    public boolean isPrintRequestPayload() {
        return printRequestPayload;
    }

    public boolean isPrintResponseBody() {
        return printResponseBody;
    }

    public boolean isPrintHeaders() {
        return printHeaders;
    }

    /** The line is not runnable as it stands: the secrets in it are redacted. */
    public boolean isPrintCurl() {
        return printCurl;
    }

    /** Prints nothing for a successful call, whatever the other {@code print*} flags say. */
    public boolean isPrintOnlyOnError() {
        return printOnlyOnError;
    }

    public String getPrintFormat() {
        return printFormat;
    }

    /** A redirected stream is left plain either way. */
    public boolean isPrintColor() {
        return printColor;
    }

    /** 0, the default, means unlimited. */
    public int getPrintMaxChars() {
        return printMaxChars;
    }

    public String getBaseUrl() {
        return auth.getBaseUrl();
    }

    public String getClientId() {
        return auth.getClientId();
    }

    /** The real secret, never masked: do not log this. */
    public String getClientSecret() {
        return auth.getClientSecret();
    }

    public String getRedirectUri() {
        return auth.getRedirectUri();
    }

    /** The real value, never masked: do not log this. */
    public String getAuthString() {
        return auth.getAuthString();
    }

    public String getClientName() {
        return auth.getClientName();
    }

    public String getGrantType() {
        return auth.getGrantType();
    }

    /** Compare against the {@code USE_DEFAULT_TOKEN_CACHE} sentinel by {@code ==}. */
    public String getTokenCachePath() {
        return auth.getTokenCachePath();
    }

    /** The fields {@link #toString()} renders, in declaration order. */
    private static final List<String> REPR_FIELDS = List.of(
            "auth", "apiVersion", "timeoutSeconds", "connectTimeoutSeconds", "maxRetries",
            "retryableHttpCodes", "unsafeRetryableHttpCodes", "retryBackoffSeconds",
            "retryBackoffMaxSeconds", "verifySsl", "caBundle", "proxyUrl", "poolMaxsize",
            "maxResponseBytes", "maxRequestBytes", "maxUploadBytes",
            "maxMultipartOverheadBytes", "maxTokenCacheBytes", "maxCaBundleBytes",
            "defaultHeaders", "raiseOnError", "responseFormat", "userAgent", "logLevel",
            "logBodies", "logFile",
            "printApiInfo", "printRequestPayload", "printResponseBody", "printHeaders",
            "printCurl", "printOnlyOnError", "printFormat", "printColor", "printMaxChars");

    /** Every field, never a subset, so diffing two configs shows every knob that differs. */
    @Override
    public String toString() {
        return "SdkConfig(" + String.join(", ", Redaction.redactFields(this, REPR_FIELDS)) + ")";
    }

    private static String normaliseApiVersion(String raw) {
        if (raw == null) {
            return null;
        }
        String value = raw.strip().toLowerCase(Locale.ROOT);
        if (!value.isEmpty() && !value.startsWith("v")) {
            value = "v" + value;
        }
        return value;
    }

    private static boolean finitePositive(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value) && value > 0;
    }

    /** Read at construction, once per {@link Builder}, and never per call. */
    private static String responseFormatFromEnvironment() {
        String fromEnvironment = System.getenv(RESPONSE_FORMAT_ENVIRONMENT_VARIABLE);
        return fromEnvironment == null || fromEnvironment.strip().isEmpty()
                ? DEFAULT_RESPONSE_FORMAT : fromEnvironment;
    }

    private static String readImplementationVersion() {
        String fromManifest = SdkConfig.class.getPackage() == null
                ? null : SdkConfig.class.getPackage().getImplementationVersion();
        return fromManifest == null ? FALLBACK_VERSION : fromManifest;
    }

    public static final class Builder {
        private final AuthConfig auth;
        private String apiVersion = DEFAULT_API_VERSION;
        private double timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
        private double connectTimeoutSeconds = DEFAULT_CONNECT_TIMEOUT_SECONDS;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private Set<Integer> retryableHttpCodes = RetryPolicy.DEFAULT_RETRYABLE_HTTP_CODES;
        private Set<Integer> unsafeRetryableHttpCodes = RetryPolicy.DEFAULT_UNSAFE_RETRYABLE_HTTP_CODES;
        private double retryBackoffSeconds = DEFAULT_RETRY_BACKOFF_SECONDS;
        private double retryBackoffMaxSeconds = DEFAULT_RETRY_BACKOFF_MAX_SECONDS;
        private boolean verifySsl = true;
        private String caBundle;
        private String proxyUrl;
        private int poolMaxsize = DEFAULT_POOL_MAXSIZE;
        private long maxResponseBytes = DEFAULT_MAX_RESPONSE_BYTES;
        private long maxRequestBytes = DEFAULT_MAX_REQUEST_BYTES;
        private long maxUploadBytes = DEFAULT_MAX_UPLOAD_BYTES;
        private long maxMultipartOverheadBytes = DEFAULT_MAX_MULTIPART_OVERHEAD_BYTES;
        private long maxTokenCacheBytes = DEFAULT_MAX_TOKEN_CACHE_BYTES;
        private long maxCaBundleBytes = DEFAULT_MAX_CA_BUNDLE_BYTES;
        private Map<String, String> defaultHeaders = new LinkedHashMap<>();
        private boolean raiseOnError = false;
        private String responseFormat = responseFormatFromEnvironment();
        private String userAgent = DEFAULT_USER_AGENT;
        private String logLevel = DEFAULT_LOG_LEVEL;
        private boolean logBodies = false;
        private String logFile;
        private boolean printApiInfo = false;
        private boolean printRequestPayload = false;
        private boolean printResponseBody = false;
        private boolean printHeaders = false;
        private boolean printCurl = false;
        private boolean printOnlyOnError = false;
        private String printFormat = DEFAULT_PRINT_FORMAT;
        private boolean printColor = false;
        private int printMaxChars = 0;

        private Builder(AuthConfig auth) {
            this.auth = auth;
        }

        /** {@code "v4"} or bare {@code "4"}, stripped and lowered; a leading zero is refused. */
        public Builder apiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public Builder timeoutSeconds(double timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        public Builder connectTimeoutSeconds(double connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
            return this;
        }

        /** Attempts including the first: 1 disables retrying, 3 sends once plus two repeats. */
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        /** Each status within 100-599, at most 500 of them. */
        public Builder retryableHttpCodes(Set<Integer> retryableHttpCodes) {
            this.retryableHttpCodes = retryableHttpCodes;
            return this;
        }

        /** Statuses a state-changing method may also be retried on; empty by default. */
        public Builder unsafeRetryableHttpCodes(Set<Integer> unsafeRetryableHttpCodes) {
            this.unsafeRetryableHttpCodes = unsafeRetryableHttpCodes;
            return this;
        }

        public Builder retryBackoffSeconds(double retryBackoffSeconds) {
            this.retryBackoffSeconds = retryBackoffSeconds;
            return this;
        }

        /** A value below {@code retryBackoffSeconds} is refused. */
        public Builder retryBackoffMaxSeconds(double retryBackoffMaxSeconds) {
            this.retryBackoffMaxSeconds = retryBackoffMaxSeconds;
            return this;
        }

        public Builder verifySsl(boolean verifySsl) {
            this.verifySsl = verifySsl;
            return this;
        }

        /** Must be an existing regular file; blank keeps the platform trust store. */
        public Builder caBundle(String caBundle) {
            this.caBundle = caBundle;
            return this;
        }

        /** At least a scheme and a host, like {@code http://proxy.example:3128}. */
        public Builder proxyUrl(String proxyUrl) {
            this.proxyUrl = proxyUrl;
            return this;
        }

        public Builder poolMaxsize(int poolMaxsize) {
            this.poolMaxsize = poolMaxsize;
            return this;
        }

        /** A larger body is refused with an {@code SdkError} naming this option. */
        public Builder maxResponseBytes(long maxResponseBytes) {
            this.maxResponseBytes = maxResponseBytes;
            return this;
        }

        public Builder maxRequestBytes(long maxRequestBytes) {
            this.maxRequestBytes = maxRequestBytes;
            return this;
        }

        public Builder maxUploadBytes(long maxUploadBytes) {
            this.maxUploadBytes = maxUploadBytes;
            return this;
        }

        public Builder maxMultipartOverheadBytes(long maxMultipartOverheadBytes) {
            this.maxMultipartOverheadBytes = maxMultipartOverheadBytes;
            return this;
        }

        public Builder maxTokenCacheBytes(long maxTokenCacheBytes) {
            this.maxTokenCacheBytes = maxTokenCacheBytes;
            return this;
        }

        public Builder maxCaBundleBytes(long maxCaBundleBytes) {
            this.maxCaBundleBytes = maxCaBundleBytes;
            return this;
        }

        /** Copied, so later edits are not seen; this replaces rather than merges. */
        public Builder defaultHeaders(Map<String, String> defaultHeaders) {
            this.defaultHeaders = defaultHeaders == null
                    ? new LinkedHashMap<>() : new LinkedHashMap<>(defaultHeaders);
            return this;
        }

        public Builder raiseOnError(boolean raiseOnError) {
            this.raiseOnError = raiseOnError;
            return this;
        }

        /** One of {@code map}, {@code json}, {@code dto}; anything else is refused. */
        public Builder responseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder logLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder logBodies(boolean logBodies) {
            this.logBodies = logBodies;
            return this;
        }

        /** Appended in UTF-8, SLF4J binding or not; an unwritable path fails the client build. */
        public Builder logFile(String logFile) {
            this.logFile = logFile;
            return this;
        }

        public Builder printApiInfo(boolean printApiInfo) {
            this.printApiInfo = printApiInfo;
            return this;
        }

        public Builder printRequestPayload(boolean printRequestPayload) {
            this.printRequestPayload = printRequestPayload;
            return this;
        }

        public Builder printResponseBody(boolean printResponseBody) {
            this.printResponseBody = printResponseBody;
            return this;
        }

        public Builder printHeaders(boolean printHeaders) {
            this.printHeaders = printHeaders;
            return this;
        }

        public Builder printCurl(boolean printCurl) {
            this.printCurl = printCurl;
            return this;
        }

        public Builder printOnlyOnError(boolean printOnlyOnError) {
            this.printOnlyOnError = printOnlyOnError;
            return this;
        }

        public Builder printFormat(String printFormat) {
            this.printFormat = printFormat;
            return this;
        }

        public Builder printColor(boolean printColor) {
            this.printColor = printColor;
            return this;
        }

        public Builder printMaxChars(int printMaxChars) {
            this.printMaxChars = printMaxChars;
            return this;
        }

        /** The message lists every problem at once rather than only the first. */
        public SdkConfig build() {
            return new SdkConfig(this);
        }
    }
}
