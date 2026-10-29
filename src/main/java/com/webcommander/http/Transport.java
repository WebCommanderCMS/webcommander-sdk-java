package com.webcommander.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.ErrorType;
import com.webcommander.client.RequestInfo;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.Redaction;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** HTTP execution: one request in, one decoded {@link ApiResponse} out, whatever the status. */
public final class Transport implements AutoCloseable {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
    /** Methods safe to resend. */
    private static final Set<String> RESENDABLE_METHODS = Set.of("GET", "HEAD", "OPTIONS");
    private static final Set<String> BODY_REQUIRED_METHODS =
            Set.of("POST", "PUT", "PATCH", "PROPPATCH", "REPORT");
    private static final String UNKNOWN_PARAMETER_ERROR = "unknown_parameter";
    private static final Pattern PARAMETER_NAME = Pattern.compile("[A-Za-z0-9_-]+");

    private final SdkConfig config;
    private final RetryPolicy retryPolicy;
    private final Consumer<String> onProgress;
    private final OkHttpClient client;
    private final boolean ownsClient;

    public Transport(SdkConfig config) {
        this(config, null, null, null);
    }

    /** {@code null} derives the policy from the config; progress lines arrive already redacted. */
    public Transport(SdkConfig config, RetryPolicy retryPolicy, Consumer<String> onProgress) {
        this(config, retryPolicy, onProgress, null);
    }

    /** The one substitution seam: a supplied {@code httpClient} replaces the configured one. */
    public Transport(SdkConfig config, RetryPolicy retryPolicy, Consumer<String> onProgress,
            OkHttpClient httpClient) {
        this.config = config;
        this.retryPolicy = retryPolicy != null ? retryPolicy : new RetryPolicy(
                config.getMaxRetries(),
                config.getRetryBackoffSeconds(),
                config.getRetryBackoffMaxSeconds(),
                config.getRetryableHttpCodes(),
                config.getUnsafeRetryableHttpCodes());
        this.onProgress = onProgress != null ? onProgress : message -> { };
        this.ownsClient = httpClient == null;
        this.client = httpClient != null
                ? OkHttpClientFactory.withSdkInvariants(httpClient, config)
                : OkHttpClientFactory.create(config);
    }

    /** A 3xx is returned rather than followed; a 401 is returned for the client to replay. */
    public ApiResponse sendHttpRequest(TransportRequest request) {
        String method = normaliseMethod(request.httpMethod());
        checkRequestHeaders(request.headers());
        checkMultipartLabels(request.files, request.formData);
        HttpUrl url = buildRequestUrl(request.url(), request.queryParams);
        checkSourceBudget(method, url, request);
        return sendWithRetries(method, url, request);
    }

    private void checkSourceBudget(String method, HttpUrl url, TransportRequest request) {
        long sources = sourceBytesOf(request);
        if (sources > config.getMaxUploadBytes()) {
            throw new SdkUsageError(method + " " + endpoint(request, url)
                    + ": the request's sources are " + sources + " bytes, over the "
                    + config.getMaxUploadBytes() + "-byte maxUploadBytes ceiling");
        }
    }

    /** Part content, form field names and values, and a JSON body already rendered to bytes. */
    private static long sourceBytesOf(TransportRequest request) {
        long total = partContentBytesOf(request);
        if (request.formData != null) {
            for (String name : request.formData.keySet()) {
                total += utf8Length(name);
            }
        }
        return total;
    }

    private static long partContentBytesOf(TransportRequest request) {
        long total = 0;
        if (request.jsonBody instanceof byte[] bytes) {
            total += bytes.length;
        }
        if (request.files != null) {
            for (Object value : request.files.values()) {
                total += partBytes(value).length;
            }
        }
        if (request.formData != null) {
            for (Object value : request.formData.values()) {
                total += utf8Length(String.valueOf(value));
            }
        }
        return total;
    }

    /** The exact bytes the connection would carry, framing included. */
    private void checkWireBudget(String method, HttpUrl url, TransportRequest request,
            RequestBody body) {
        long wire;
        try {
            wire = body == null ? 0 : body.contentLength();
        } catch (IOException unmeasurable) {
            // A body that cannot state a length is not one this transport can bound, or will send.
            throw new SdkUsageError(method + " " + endpoint(request, url)
                    + ": the encoded body would not state its length", unmeasurable);
        }
        if (wire > config.getMaxRequestBytes()) {
            throw new SdkUsageError(method + " " + endpoint(request, url)
                    + ": the encoded body is " + wire + " bytes, over the "
                    + config.getMaxRequestBytes() + "-byte maxRequestBytes ceiling");
        }
        if (body instanceof MultipartBody) {
            long framing = wire - partContentBytesOf(request);
            if (framing > config.getMaxMultipartOverheadBytes()) {
                throw new SdkUsageError(method + " " + endpoint(request, url)
                        + ": the multipart framing is " + framing + " bytes, over the "
                        + config.getMaxMultipartOverheadBytes()
                        + "-byte maxMultipartOverheadBytes ceiling");
            }
        }
    }

    /** The clock starts here, so {@code elapsed} measures the exchange and nothing before. */
    private ApiResponse sendWithRetries(String method, HttpUrl url, TransportRequest request) {
        long started = System.nanoTime();
        int attempt = 0;
        int requestsSent = 0;
        boolean staleEchoResent = false;
        boolean ceilingReached = false;
        Exception lastException = null;
        long prepareBy = started + (long) (config.getTimeoutSeconds() * 1e9);
        RequestBody body = encodeBody(method, request, prepareBy);
        refuseIfPreparationOverran(method, url, request, prepareBy, started);
        checkWireBudget(method, url, request, body);

        while (attempt < retryPolicy.getMaxAttempts()) {
            if (Thread.currentThread().isInterrupted()) {
                throw interruptedSend(method, url, request, requestsSent,
                        staleEchoResent, started, null);
            }
            attempt++;
            requestsSent++;
            // The endpoint, not the resolved URL, which would hand a customer id to this callback.
            onProgress.accept(method + " " + endpoint(request, url)
                    + " sending (attempt " + attempt + "/" + retryPolicy.getMaxAttempts() + ")");
            // Kept for isCanceled(): a callTimeout and an interrupt raise the same type.
            okhttp3.Call call = client.newCall(buildRequest(method, url, request, body));
            try (Response raw = call.execute()) {
                int status = raw.code();
                byte[] bodyBytes = readBodyWithinTheCap(raw, method, url, request,
                        requestsSent, grantedAttempts(staleEchoResent), started);
                String contentType = raw.header("Content-Type");

                if (attempt < retryPolicy.getMaxAttempts()
                        && retryPolicy.shouldRetryStatusCode(status, method)) {
                    if (!sleepSeconds(retryPolicy.computeBackoffSeconds(
                            attempt, raw.header("Retry-After")))) {
                        throw interruptedSend(method, url, request, requestsSent,
                                staleEchoResent, started, null);
                    }
                    continue;
                }

                Object decoded = decodeResponseBody(status, bodyBytes, contentType);
                ApiResponse response = buildResponse(raw, decoded, bodyBytes, contentType, method,
                        request, requestsSent, grantedAttempts(staleEchoResent), started);

                if (!staleEchoResent
                        && RESENDABLE_METHODS.contains(method)
                        && detectStaleParameterEcho(response, request.queryParams())) {
                    staleEchoResent = true;
                    attempt--;
                    client.connectionPool().evictAll();
                    continue;
                }
                return response;
            } catch (IOException exception) {
                lastException = exception;
                boolean ceiling = call.isCanceled();
                // Assigned, not accumulated, so an earlier ceiling cannot mislabel a later failure.
                ceilingReached = ceiling;
                if (!ceiling && meansInterrupted(exception)) {
                    throw interruptedSend(method, url, request, requestsSent,
                            staleEchoResent, started, exception);
                }
                if (attempt >= retryPolicy.getMaxAttempts()
                        || !retryPolicy.shouldRetryAfterFailure(
                                RetryPolicy.classifyTransportFailure(exception), method)) {
                    break;
                }
                if (!sleepSeconds(retryPolicy.computeBackoffSeconds(attempt, null))) {
                    throw interruptedSend(method, url, request, requestsSent,
                            staleEchoResent, started, exception);
                }
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw interruptedSend(method, url, request, requestsSent,
                    staleEchoResent, started, lastException);
        }
        boolean timedOut = ceilingReached || isTimeoutFailure(lastException);
        throw new SdkError(
                method + " " + endpoint(request, url)
                        + (timedOut ? " timed out" : " failed") + " after " + requestsSent
                        + " attempt(s)" + detailOf(lastException),
                null,
                timedOut ? ErrorType.TIMEOUT : ErrorType.NETWORK,
                null,
                null,
                requestInfo(method, request, requestsSent,
                        grantedAttempts(staleEchoResent), started),
                lastException);
    }

    /** Refuse a call whose preparation used the whole budget, before the first byte goes out. */
    private void refuseIfPreparationOverran(String method, HttpUrl url,
            TransportRequest request, long prepareBy, long started) {
        if (System.nanoTime() - prepareBy <= 0) {
            return;
        }
        throw new SdkError(
                method + " " + endpoint(request, url) + " was not sent: preparing the body "
                        + "took longer than the " + config.getTimeoutSeconds()
                        + "-second timeoutSeconds budget, which bounds the preparation and "
                        + "the first attempt together",
                null,
                ErrorType.TIMEOUT,
                null,
                null,
                requestInfo(method, request, 0, retryPolicy.getMaxAttempts(), started));
    }

    private byte[] readBodyWithinTheCap(Response raw, String method, HttpUrl url,
            TransportRequest request, int requestsSent, int maxAttempts, long started)
            throws IOException {
        okhttp3.ResponseBody body = raw.body();
        if (body == null) {
            return new byte[0];
        }
        long cap = config.getMaxResponseBytes();
        long declared = body.contentLength();
        if (declared > cap) {
            throw refuseOversizedBody(method, url, cap, declared,
                    request, requestsSent, maxAttempts, started);
        }
        byte[] collected;
        try (InputStream stream = body.byteStream()) {
            collected = readAtMost(stream, cap);
        }
        if (collected.length > cap) {
            throw refuseOversizedBody(method, url, cap, declared,
                    request, requestsSent, maxAttempts, started);
        }
        return collected;
    }

    /** At most {@code limit + 1} bytes, so at the ceiling and over it differ. */
    public static byte[] readAtMost(InputStream stream, long limit) throws IOException {
        long ceiling = limit == Long.MAX_VALUE ? Long.MAX_VALUE : limit + 1;
        ByteArrayOutputStream collected = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        while (collected.size() < ceiling) {
            int wanted = (int) Math.min(chunk.length, ceiling - collected.size());
            int read = stream.read(chunk, 0, wanted);
            if (read < 0) {
                break;
            }
            collected.write(chunk, 0, read);
        }
        return collected.toByteArray();
    }

    /** {@code UNKNOWN}, not {@code NETWORK}: a re-send would read the same oversized body. */
    private SdkError refuseOversizedBody(String method, HttpUrl url, long cap, long declared,
            TransportRequest request, int requestsSent, int maxAttempts, long started) {
        return new SdkError(
                method + " " + endpoint(request, url)
                        + " answered with a body over the " + cap
                        + "-byte maxResponseBytes cap"
                        + (declared >= 0 ? " (Content-Length " + declared + ")" : "")
                        + ", so it was not buffered - raise the cap with "
                        + "SdkConfig.builder(...).maxResponseBytes(bytes) if this response is "
                        + "meant to be that large",
                null,
                ErrorType.UNKNOWN,
                null,
                null,
                requestInfo(method, request, requestsSent, maxAttempts, started));
    }

    public void closeTransport() {
        if (!ownsClient) {
            return;
        }
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void close() {
        closeTransport();
    }

    public static String normaliseMethod(String httpMethod) {
        if (httpMethod == null || httpMethod.strip().isEmpty()) {
            throw new SdkUsageError("httpMethod: expected a non-blank string like 'GET', "
                    + "got '" + httpMethod + "'");
        }
        String method = httpMethod.strip().toUpperCase(Locale.ROOT);
        if (!isHttpToken(method)) {
            throw new SdkUsageError("httpMethod: expected a single verb like 'GET', got '"
                    + shown(method) + "' - a method name is an RFC 9110 token, so it "
                    + "carries no spaces, no control characters and no separators");
        }
        return method;
    }

    /** A {@code token}, not a list of known verbs: {@code PURGE} and {@code MKCOL} are real. */
    public static boolean isHttpToken(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            boolean allowed = (character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z')
                    || (character >= '0' && character <= '9')
                    || "!#$%&'*+-.^_`|~".indexOf(character) >= 0;
            if (!allowed) {
                return false;
            }
        }
        return true;
    }

    private static final int MAX_SHOWN = 120;

    private static final String MEDIA_TYPE_ADVICE =
            "type/subtype, optionally followed by '; parameter=value'";

    /** A value as a refusal message shows it: escaped, bounded and single-quoted. */
    public static String quoteForMessage(String value) {
        return value == null ? "null" : "'" + escapeForMessage(value) + "'";
    }

    /** A value with its control characters escaped and its length bounded, unquoted. */
    public static String escapeForMessage(String value) {
        StringBuilder escaped = new StringBuilder(value.length() + 2);
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '\\' -> escaped.append("\\\\");
                case '\'' -> escaped.append("\\'");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (character < 0x20 || character == 0x7f) {
                        escaped.append(String.format("\\x%02x", (int) character));
                    } else {
                        escaped.append(character);
                    }
                }
            }
        }
        String shown = escaped.toString();
        if (shown.length() > MAX_SHOWN) {
            int head = (MAX_SHOWN - 3) / 2;
            int tail = MAX_SHOWN - 3 - head;
            shown = shown.substring(0, head) + "..." + shown.substring(shown.length() - tail);
        }
        return shown;
    }

    /** The refusal for a {@code Content-Type} value that is not a media type. */
    public static String refuseContentType(String where, String value) {
        return where + ": Content-Type must be a media type like 'application/json', got "
                + quoteForMessage(value) + " — " + MEDIA_TYPE_ADVICE;
    }

    /** A refused name may carry the line break it was refused for, and this text reaches a log. */
    private static String shown(String label) {
        StringBuilder escaped = new StringBuilder(label.length());
        for (int i = 0; i < label.length() && i < 60; i++) {
            char character = label.charAt(i);
            if (character < 0x20 || character == 0x7f) {
                escaped.append(String.format("\\x%02x", (int) character));
            } else {
                escaped.append(character);
            }
        }
        return label.length() > 60 ? escaped + "..." : escaped.toString();
    }

    /** Checked here as well as in the client, because the token grant builds its own headers. */
    private static void checkRequestHeaders(Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        String refused = null;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String value = entry.getValue();
            if (!isHttpToken(entry.getKey()) || !isSendableHeaderValue(value)
                    || hasEdgeWhitespace(value)) {
                refused = entry.getKey();
                break;
            }
        }
        if (refused != null) {
            throw new SdkUsageError("header '" + shown(refused) + "': not something the "
                    + "transport can send — a name that is not an RFC 9110 token, or a value "
                    + "carrying a carriage return, a newline, a leading or trailing space or "
                    + "tab, or a character outside printable ASCII. The value is not shown: "
                    + "it may be a credential");
        }
    }

    /** Every character a visible ASCII one, a space or a tab. */
    public static boolean isSendableHeaderValue(String value) {
        if (value == null) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            if (character != '\t' && (character < 0x20 || character > 0x7e)) {
                return false;
            }
        }
        return true;
    }

    /** Refused rather than trimmed: {@code okhttp3.Headers.of} trims both ends of a value. */
    public static boolean hasEdgeWhitespace(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        return first == ' ' || first == '\t' || last == ' ' || last == '\t';
    }

    /** {@code tchar} from RFC 9110 §5.6.2, written out rather than built from {@code \w}. */
    private static final String TCHAR = "[!#$%&'*+\\-.^_`|~0-9A-Za-z]";

    private static final String TOKEN = TCHAR + "+";

    /** {@code quoted-string} from RFC 9110 §5.6.4, capped at latin-1's U+00FF. */
    private static final String QUOTED_STRING =
            "\"(?:[\\t \\x21\\x23-\\x5B\\x5D-\\x7E\\x80-\\xFF]"
                    + "|\\\\[\\t \\x21-\\x7E\\x80-\\xFF])*\"";

    private static final String PARAMETER = TOKEN + "=(?:" + TOKEN + "|" + QUOTED_STRING + ")";

    /** Use {@link java.util.regex.Matcher#matches()}; {@code $} matches before a last newline. */
    private static final Pattern MEDIA_TYPE = Pattern.compile(
            TOKEN + "/" + TOKEN + "(?:[ \\t]*;[ \\t]*" + PARAMETER + ")*");

    /** Longer values are refused unread: the regex recurses per parameter and can overflow. */
    private static final int MAX_JUDGEABLE = 1024;

    /** RFC 9110 §8.3 {@code type/subtype} plus zero or more {@code ; parameter=value} pairs. */
    public static boolean isMediaType(String value) {
        if (value == null || value.length() > MAX_JUDGEABLE) {
            return false;
        }
        return MEDIA_TYPE.matcher(value).matches();
    }

    public static HttpUrl buildRequestUrl(String url, Map<String, Object> queryParams) {
        HttpUrl parsed = HttpUrl.parse(url);
        if (parsed == null) {
            throw new SdkUsageError("not a valid http(s) URL: " + Redaction.redactText(url));
        }
        UrlBuilder.rejectAFragment(url);
        if (queryParams == null || queryParams.isEmpty()) {
            return parsed;
        }
        HttpUrl.Builder builder = parsed.newBuilder();
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            if (entry.getValue() instanceof List<?> values) {
                for (Object value : values) {
                    builder.addQueryParameter(entry.getKey(), String.valueOf(value));
                }
            } else {
                builder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return builder.build();
    }

    /** Built once, so every attempt writes the same bytes. {@code null} for a bodiless verb. */
    private static RequestBody encodeBody(String method, TransportRequest request,
            long prepareBy) {
        RequestBody body =
                honourCallerContentType(buildBody(request, prepareBy), request.headers());
        if (body == null && BODY_REQUIRED_METHODS.contains(method)) {
            // Null content type, so OkHttp leaves the caller's own header standing.
            return RequestBody.create(new byte[0], null);
        }
        return body;
    }

    private Request buildRequest(String method, HttpUrl url, TransportRequest request,
            RequestBody body) {
        Request.Builder builder = new Request.Builder().url(url);
        if (request.headers() != null) {
            builder.headers(Headers.of(request.headers()));
        }
        if (body instanceof MultipartBody multipart) {
            builder.header(CONTENT_TYPE_HEADER, String.valueOf(multipart.contentType()));
        }
        return builder.method(method, body).build();
    }

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    /** OkHttp's {@code BridgeInterceptor} otherwise overwrites it from {@code contentType()}. */
    private static RequestBody honourCallerContentType(
            RequestBody body, Map<String, String> headers) {
        if (body == null || body instanceof MultipartBody) {
            return body;
        }
        String declared = findContentType(headers);
        if (declared == null) {
            return body;
        }
        MediaType parsed = MediaType.parse(declared);
        RequestBody encoded = body;
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return parsed;
            }

            @Override
            public long contentLength() throws IOException {
                return encoded.contentLength();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                encoded.writeTo(sink);
            }
        };
    }

    /** The caller's {@code Content-Type}, matched case-insensitively; the last spelling wins. */
    public static String findContentType(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        String found = null;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (CONTENT_TYPE_HEADER.equalsIgnoreCase(entry.getKey())
                    && entry.getValue() != null) {
                found = entry.getValue();
            }
        }
        return found;
    }

    /** A mapping, a list, a scalar, or JSON text. */
    public static String encodeJsonBody(Object jsonBody) {
        if (jsonBody instanceof CharSequence text) {
            return text.toString();
        }
        try {
            return JSON.writeValueAsString(jsonBody);
        } catch (IOException error) {
            throw new SdkUsageError(
                    "jsonBody is not JSON-serialisable: " + error.getMessage(), error);
        }
    }

    /** Every {@link Path} read into its bytes, so the content is settled before the call. */
    public static Map<String, Object> materialiseFileParts(Map<String, Object> files) {
        return materialiseFileParts(files, MAX_UPLOAD_PART_BYTES);
    }

    public static Map<String, Object> materialiseFileParts(
            Map<String, Object> files, long maxPartBytes) {
        return materialiseFileParts(files, maxPartBytes, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    /** Under the config's ceilings: per part, in aggregate, and the preparation deadline. */
    public static Map<String, Object> materialiseFileParts(
            Map<String, Object> files, SdkConfig config) {
        return materialiseFileParts(files, MAX_UPLOAD_PART_BYTES, config.getMaxUploadBytes(),
                System.nanoTime() + (long) (config.getTimeoutSeconds() * 1e9));
    }

    /** Reads at most {@code min(perPart, remaining) + 1}, one past the first ceiling hit. */
    private static Map<String, Object> materialiseFileParts(Map<String, Object> files,
            long maxPartBytes, long maxAggregateBytes, long prepareBy) {
        if (files == null || files.isEmpty()) {
            return files;
        }
        Map<String, Object> materialised = new LinkedHashMap<>();
        long total = 0;
        for (Map.Entry<String, Object> entry : files.entrySet()) {
            refuseIfPastPreparationBudget(prepareBy);
            Object value = entry.getValue();
            if (value instanceof Path path) {
                byte[] content = readFilePart(path, maxPartBytes,
                        maxAggregateBytes - total, prepareBy);
                total += content.length;
                materialised.put(entry.getKey(),
                        List.of(String.valueOf(path.getFileName()), content));
            } else {
                total += partBytes(value).length;
                materialised.put(entry.getKey(), value);
            }
            if (total > maxAggregateBytes) {
                throw refuseOversizedUploadTotal(entry.getKey(), maxAggregateBytes, total);
            }
        }
        return materialised;
    }

    private static SdkUsageError refuseOversizedUploadTotal(
            String field, long cap, long total) {
        return new SdkUsageError("files: the uploads add up to more than the " + cap
                + "-byte maxUploadBytes ceiling by the time '" + field + "' is counted"
                + (total > cap + 1 ? " (" + total + " bytes)" : ""));
    }

    private static void refuseIfPastPreparationBudget(long prepareBy) {
        if (prepareBy != Long.MAX_VALUE && System.nanoTime() - prepareBy > 0) {
            throw new SdkUsageError("files: reading the named uploads ran past the "
                    + "timeoutSeconds preparation budget, so the request was abandoned "
                    + "before anything was sent");
        }
    }

    /** Per-part upload ceiling. */
    public static final long MAX_UPLOAD_PART_BYTES = 64L * 1024 * 1024;

    /** Once per logical call, so a leaf whose toString changes cannot alter the 401 replay. */
    public static Map<String, Object> materialiseFormFields(Map<String, Object> formData) {
        if (formData == null || formData.isEmpty()) {
            return formData;
        }
        Map<String, Object> materialised = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            Object value = entry.getValue();
            materialised.put(entry.getKey(),
                    value instanceof String text ? text : String.valueOf(value));
        }
        return materialised;
    }

    /** Chunked against a running total; {@code Files.readAllBytes} allocates before any check. */
    private static byte[] readFilePart(Path path, long maxPartBytes,
            long remainingAggregateBytes, long prepareBy) {
        long bound = Math.max(0, Math.min(maxPartBytes, remainingAggregateBytes));
        try {
            long declared = Files.isRegularFile(path) ? Files.size(path) : -1;
            if (declared > maxPartBytes) {
                throw refuseOversizedUpload(path, maxPartBytes, declared);
            }
            long ceiling = bound == Long.MAX_VALUE ? Long.MAX_VALUE : bound + 1;
            ByteArrayOutputStream collected = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            try (InputStream stream = Files.newInputStream(path)) {
                while (collected.size() < ceiling) {
                    refuseIfPastPreparationBudget(prepareBy);
                    int wanted = (int) Math.min(chunk.length, ceiling - collected.size());
                    int read = stream.read(chunk, 0, wanted);
                    if (read < 0) {
                        break;
                    }
                    collected.write(chunk, 0, read);
                }
            }
            if (collected.size() > maxPartBytes) {
                throw refuseOversizedUpload(path, maxPartBytes, declared);
            }
            return collected.toByteArray();
        } catch (IOException error) {
            throw new SdkUsageError("files: " + path + " could not be read: "
                    + error.getMessage(), error);
        }
    }

    private static SdkUsageError refuseOversizedUpload(Path path, long cap, long declared) {
        return new SdkUsageError("files: " + path + " is over the " + cap
                + "-byte per-part upload bound"
                + (declared >= 0 ? " (" + declared + " bytes)" : ""));
    }

    /** All replayable, so a retry or a 401 replay re-sends the same content. */
    private static RequestBody buildBody(TransportRequest request, long prepareBy) {
        if (request.jsonBody != null) {
            return RequestBody.create(jsonBodyBytes(request), JSON_TYPE);
        }
        if (request.files != null && !request.files.isEmpty()) {
            MultipartBody.Builder multipart =
                    new MultipartBody.Builder(multipartBoundary(request, prepareBy))
                            .setType(MultipartBody.FORM);
            for (Map.Entry<String, Object> entry : request.files.entrySet()) {
                addFilePart(multipart, entry.getKey(), entry.getValue());
            }
            if (request.formData != null) {
                request.formData.forEach((name, value) ->
                        multipart.addFormDataPart(
                                escapeMultipartLabel(name), String.valueOf(value)));
            }
            return multipart.build();
        }
        if (request.formData != null) {
            FormBody.Builder form = new FormBody.Builder();
            request.formData.forEach((name, value) -> form.add(name, String.valueOf(value)));
            return form.build();
        }
        return null;
    }

    /** A fresh array, since {@code RequestBody.create} holds its array by reference until send. */
    private static byte[] jsonBodyBytes(TransportRequest request) {
        if (request.jsonBody instanceof byte[] bytes) {
            return bytes.clone();
        }
        return encodeJsonBody(request.jsonBody).getBytes(StandardCharsets.UTF_8);
    }

    /** What every generated multipart boundary starts with, so one is recognisable. */
    private static final String BOUNDARY_PREFIX = "WebCommanderSdkBoundary";

    /** Derived from the captured content and never drawn at random. */
    private static String multipartBoundary(TransportRequest request, long prepareBy) {
        for (int salt = 0; salt < 1000; salt++) {
            if (System.nanoTime() - prepareBy > 0) {
                throw new SdkUsageError("files: deriving a multipart boundary for this body "
                        + "ran past the timeoutSeconds preparation budget, so it was "
                        + "abandoned before anything was sent");
            }
            String candidate = BOUNDARY_PREFIX + digestOfParts(request, salt);
            if (!partsContain(request, candidate)) {
                return candidate;
            }
        }
        // A random boundary would reintroduce the per-attempt difference.
        throw new SdkUsageError("files: no multipart boundary could be derived that this "
                + "body does not already contain, so the parts could not be framed");
    }

    /** The salted digest of every part label and every byte the parts carry. */
    private static String digestOfParts(TransportRequest request, int salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 is unavailable", impossible);
        }
        digest.update(new byte[] {(byte) salt, (byte) (salt >>> 8)});
        request.files.forEach((field, value) -> {
            digestLabel(digest, field);
            digestLabel(digest, multipartFilename(field, value));
            digestLabel(digest, declaredPartContentType(value));
            digest.update(partBytes(value));
            digest.update((byte) 0);
        });
        if (request.formData != null) {
            request.formData.forEach((name, value) -> {
                digestLabel(digest, name);
                digestLabel(digest, String.valueOf(value));
            });
        }
        byte[] hashed = digest.digest();
        StringBuilder hex = new StringBuilder(32);
        for (int index = 0; index < 16; index++) {
            hex.append(String.format("%02x", hashed[index]));
        }
        return hex.toString();
    }

    private static void digestLabel(MessageDigest digest, String label) {
        if (label != null) {
            digest.update(label.getBytes(StandardCharsets.UTF_8));
        }
        digest.update((byte) 0);
    }

    /** The captured array, deliberately: callers only read it, and a copy would copy the upload. */
    private static byte[] partBytes(Object value) {
        if (value instanceof byte[] bytes) {
            return bytes;
        }
        if (value instanceof List<?> tuple && tuple.size() >= 2
                && tuple.get(1) instanceof byte[] bytes) {
            return bytes;
        }
        return new byte[0];
    }

    private static boolean partsContain(TransportRequest request, String candidate) {
        byte[] needle = candidate.getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<String, Object> entry : request.files.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (labelContains(field, candidate)
                    || labelContains(multipartFilename(field, value), candidate)
                    || labelContains(declaredPartContentType(value), candidate)
                    || indexOfBytes(partBytes(value), needle) >= 0) {
                return true;
            }
        }
        if (request.formData != null) {
            for (Map.Entry<String, Object> entry : request.formData.entrySet()) {
                if (labelContains(entry.getKey(), candidate)
                        || labelContains(String.valueOf(entry.getValue()), candidate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean labelContains(String label, String candidate) {
        return label != null && label.contains(candidate);
    }

    private static int indexOfBytes(byte[] haystack, byte[] needle) {
        outer:
        for (int start = 0; start + needle.length <= haystack.length; start++) {
            for (int index = 0; index < needle.length; index++) {
                if (haystack[start + index] != needle[index]) {
                    continue outer;
                }
            }
            return start;
        }
        return -1;
    }

    private static void addFilePart(MultipartBody.Builder multipart, String field, Object value) {
        if (value instanceof byte[] bytes) {
            addBytesPart(multipart, field, multipartFilename(field, value), bytes, null);
        } else if (value instanceof List<?> tuple && tuple.size() >= 2
                && tuple.get(0) instanceof String filename && tuple.get(1) instanceof byte[] bytes) {
            String declared = tuple.size() > 2 && tuple.get(2) instanceof String given ? given : null;
            addBytesPart(multipart, field, filename, bytes, declared);
        } else {
            throw refuseFilePart(field);
        }
    }

    public static boolean isSendableFilePart(Object value) {
        if (value instanceof byte[] || value instanceof Path) {
            return true;
        }
        return value instanceof List<?> tuple && tuple.size() >= 2
                && tuple.get(0) instanceof String && tuple.get(1) instanceof byte[];
    }

    /** The value itself is never shown, because a file part is caller data. */
    public static SdkUsageError refuseFilePart(String field) {
        return new SdkUsageError("files: '" + field + "' must be byte[], a Path, or "
                + "[filename, byte[], contentType?] - a raw stream cannot be replayed on a "
                + "retry, so buffer it first");
    }

    private static void addBytesPart(MultipartBody.Builder multipart, String field,
            String filename, byte[] bytes, String declaredContentType) {
        multipart.addFormDataPart(escapeMultipartLabel(field), escapeMultipartLabel(filename),
                RequestBody.create(bytes.clone(),
                        partContentType(field, filename, bytes, declaredContentType)));
    }

    public static void checkMultipartLabels(
            Map<String, Object> files, Map<String, Object> formData) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (String field : sortedLabels(files)) {
            refuseMultipartLabelBytes(
                    "a files part name of " + utf8Length(field) + " byte(s)", field);
            Object value = files.get(field);
            refuseMultipartLabelBytes("the filename on part '" + field + "'",
                    multipartFilename(field, value));
            refuseMultipartLabelBytes("the content type on part '" + field + "'",
                    declaredPartContentType(value));
        }
        if (formData != null) {
            for (String field : sortedLabels(formData)) {
                refuseMultipartLabelBytes(
                        "a formData name of " + utf8Length(field) + " byte(s)", field);
            }
        }
    }

    /** A CR, an LF or a NUL in a label would become part of a part header. */
    private static void refuseMultipartLabelBytes(String what, String value) {
        if (value == null) {
            return;
        }
        for (int index = 0; index < value.length(); index++) {
            String named = switch (value.charAt(index)) {
                case '\r' -> "a carriage return";
                case '\n' -> "a newline";
                case '\0' -> "a NUL";
                default -> null;
            };
            if (named != null) {
                throw new SdkUsageError(what + " carries " + named + " at byte "
                        + utf8Length(value.substring(0, index)) + "; strip CR, LF and NUL");
            }
        }
    }

    private static String escapeMultipartLabel(String label) {
        return label == null ? null : label.replace("\\", "%5C").replace("\"", "%22");
    }

    private static String declaredPartContentType(Object value) {
        return value instanceof List<?> tuple && tuple.size() > 2
                && tuple.get(2) instanceof String declared ? declared : null;
    }

    /** A stable order, so a body with two bad labels names the same one every run. */
    private static List<String> sortedLabels(Map<String, Object> values) {
        List<String> labels = new ArrayList<>(values.keySet());
        labels.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        return labels;
    }

    private static int utf8Length(String label) {
        return label == null ? 0 : label.getBytes(StandardCharsets.UTF_8).length;
    }

    /** Public because the console's curl line renders the same answer. */
    public static String multipartFilename(String field, Object value) {
        if (value instanceof Path path) {
            return String.valueOf(path.getFileName());
        }
        if (value instanceof List<?> tuple && !tuple.isEmpty()
                && tuple.get(0) instanceof String filename) {
            return filename;
        }
        if (value instanceof byte[] bytes) {
            return extensionOf(field) == null ? field + sniffContent(bytes).extension() : field;
        }
        return field;
    }

    /** The caller's own content type, then the filename's extension, then the content. */
    private static MediaType partContentType(String field, String filename, byte[] bytes,
            String declaredContentType) {
        if (declaredContentType != null) {
            MediaType declared = MediaType.parse(declaredContentType);
            if (declared == null) {
                throw new SdkUsageError("files: '" + field + "' declared a content type that is "
                        + "not a media type - the third tuple slot takes a form like 'text/csv', "
                        + "or leave it out and the SDK derives one from the content");
            }
            return declared;
        }
        String extension = extensionOf(filename);
        String byExtension = extension == null ? null : CONTENT_TYPES_BY_EXTENSION.get(extension);
        return MediaType.parse(byExtension != null ? byExtension : sniffContent(bytes).contentType());
    }

    private record SniffedContent(String extension, String contentType) {}

    private static final SniffedContent XLSX_CONTENT = new SniffedContent(".xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    private static final SniffedContent XLS_CONTENT =
            new SniffedContent(".xls", "application/vnd.ms-excel");
    private static final SniffedContent CSV_CONTENT = new SniffedContent(".csv", "text/csv");
    private static final SniffedContent BINARY_CONTENT =
            new SniffedContent(".bin", "application/octet-stream");

    /** The ZIP header every XLSX starts with — an XLSX is a zip of XML parts. */
    private static final byte[] ZIP_MAGIC = {'P', 'K', 0x03, 0x04};
    private static final byte[] OLE2_MAGIC = {(byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0};
    private static final int SNIFF_LIMIT = 512;

    private static final Map<String, String> CONTENT_TYPES_BY_EXTENSION = Map.of(
            ".xlsx", XLSX_CONTENT.contentType(),
            ".xls", XLS_CONTENT.contentType(),
            ".csv", CSV_CONTENT.contentType(),
            ".txt", "text/plain",
            ".json", "application/json",
            ".xml", "application/xml",
            ".zip", "application/zip",
            ".pdf", "application/pdf");

    private static SniffedContent sniffContent(byte[] bytes) {
        if (startsWith(bytes, ZIP_MAGIC)) {
            return XLSX_CONTENT;
        }
        if (startsWith(bytes, OLE2_MAGIC)) {
            return XLS_CONTENT;
        }
        return looksLikeText(bytes) ? CSV_CONTENT : BINARY_CONTENT;
    }

    private static boolean startsWith(byte[] bytes, byte[] magic) {
        if (bytes.length < magic.length) {
            return false;
        }
        for (int index = 0; index < magic.length; index++) {
            if (bytes[index] != magic[index]) {
                return false;
            }
        }
        return true;
    }

    /** No NUL and no C0 control other than tab, newline and carriage return. */
    private static boolean looksLikeText(byte[] bytes) {
        if (bytes.length == 0) {
            return false;
        }
        int limit = Math.min(bytes.length, SNIFF_LIMIT);
        for (int index = 0; index < limit; index++) {
            int value = bytes[index] & 0xFF;
            if (value < 0x20 && value != '\t' && value != '\n' && value != '\r') {
                return false;
            }
            if (value == 0x7F) {
                return false;
            }
        }
        return true;
    }

    private static String extensionOf(String filename) {
        if (filename == null) {
            return null;
        }
        int nameStart = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1;
        String name = filename.substring(nameStart);
        int dot = name.lastIndexOf('.');
        if (dot <= 0 || dot == name.length() - 1) {
            return null;
        }
        return name.substring(dot).toLowerCase(Locale.ROOT);
    }

    /** Keeps {@code bodyBytes} so {@code getJson()} returns the store's own text. */
    private ApiResponse buildResponse(
            Response raw, Object decoded, byte[] bodyBytes, String contentType, String method,
            TransportRequest request, int attempt, int maxAttempts, long started) {
        Map<String, String> headers = new LinkedHashMap<>();
        for (String name : raw.headers().names()) {
            headers.put(name, raw.header(name));
        }
        return new ApiResponse(raw.code(), decoded, headers, contentType,
                requestInfo(method, request, attempt, maxAttempts, started),
                ErrorType.classify(raw.code()), bodyBytes, null, null);
    }

    /** The resolved URL, redacted, only when {@link SdkError#ERROR_DETAIL_PROPERTY} opts in. */
    private static String endpoint(TransportRequest request, HttpUrl url) {
        if (SdkError.showsFullDetail()) {
            return Redaction.redactText(url.toString());
        }
        if (request.pathTemplate() != null) {
            return request.pathTemplate();
        }
        return Redaction.reduceToEndpoint(url.encodedPath());
    }

    /** The lower layer's own text, appended only when detail is opted into. */
    private static String detailOf(Throwable failure) {
        if (failure == null || !SdkError.showsFullDetail()) {
            return "";
        }
        return ": " + Redaction.describeFailure(failure);
    }

    private RequestInfo requestInfo(
            String method, TransportRequest request,
            int attempt, int maxAttempts, long started) {
        return new RequestInfo(
                config.getClientName(),
                config.getApiVersion(),
                method,
                request.url(),
                request.queryParams,
                request.requestInfoBody,
                request.requestInfoHeaders == null ? Map.of() : request.requestInfoHeaders,
                attempt,
                maxAttempts,
                Math.round((System.nanoTime() - started) / 1e5) / 1e4,
                request.pathTemplate());
    }

    private int grantedAttempts(boolean staleEchoResent) {
        return retryPolicy.getMaxAttempts() + (staleEchoResent ? 1 : 0);
    }

    /** JSON to Maps/Lists, {@code text/*} to text, else raw bytes; empty or 204 is {@code null}. */
    public static Object decodeResponseBody(int status, byte[] body, String contentType) {
        if (status == 204 || body == null || body.length == 0) {
            return null;
        }
        String lowered = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        if (isJsonMediaType(lowered)) {
            try {
                return JSON.readValue(body, Object.class);
            } catch (IOException error) {
                return new String(body, StandardCharsets.UTF_8);
            }
        }
        if (lowered.startsWith("text/")) {
            Charset declared = declaredCharset(lowered);
            if (declared != null) {
                return new String(body, declared);
            }
            try {
                return StandardCharsets.UTF_8.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT)
                        .decode(ByteBuffer.wrap(body)).toString();
            } catch (CharacterCodingException notUtf8) {
                return new String(body, TEXT_FALLBACK_CHARSET);
            }
        }
        return body;
    }

    /** Includes RFC 6839 {@code +json} suffixes such as {@code application/problem+json}. */
    static boolean isJsonMediaType(String loweredContentType) {
        int parameters = loweredContentType.indexOf(';');
        String mediaType = (parameters < 0
                ? loweredContentType : loweredContentType.substring(0, parameters)).strip();
        return mediaType.equals("application/json") || mediaType.endsWith("+json");
    }

    /** The RFC 2616 default for a charset-less {@code text/*} body; this codec cannot fail. */
    private static final Charset TEXT_FALLBACK_CHARSET = StandardCharsets.ISO_8859_1;

    /** The charset a lower-cased {@code Content-Type} names, or {@code null} if unusable. */
    public static Charset declaredCharset(String loweredContentType) {
        int marker = loweredContentType.indexOf("charset=");
        if (marker < 0) {
            return null;
        }
        String name = loweredContentType.substring(marker + "charset=".length()).split("[;\\s]")[0];
        try {
            return Charset.forName(name.replace("\"", ""));
        } catch (IllegalArgumentException error) {
            return null;
        }
    }

    /** A 400 naming only parameters this request never sent. */
    @SuppressWarnings("unchecked")
    static boolean detectStaleParameterEcho(ApiResponse response, Map<String, Object> sentParams) {
        if (response.getStatusCode() != 400 || !(response.getMap() instanceof Map)) {
            return false;
        }
        Map<String, Object> body = (Map<String, Object>) response.getMap();
        Set<String> namedFields = extractErrorFields(body);
        if (namedFields.isEmpty() && UNKNOWN_PARAMETER_ERROR.equals(body.get("error"))) {
            namedFields = parseUnknownParameterMessage(body);
        }
        if (namedFields.isEmpty()) {
            return false;
        }
        Set<String> sent = new HashSet<>();
        if (sentParams != null) {
            sentParams.keySet().forEach(name -> sent.add(name.toLowerCase(Locale.ROOT)));
        }
        if (sent.isEmpty()) {
            return false;
        }
        return Collections.disjoint(namedFields, sent);
    }

    private static Set<String> extractErrorFields(Map<String, Object> body) {
        Set<String> fields = new HashSet<>();
        Object entries = body.get("errors");
        if (entries instanceof Map<?, ?> byField) {
            for (Object fieldName : byField.keySet()) {
                String name = String.valueOf(fieldName).strip().toLowerCase(Locale.ROOT);
                if (!name.isEmpty()) {
                    fields.add(name);
                }
            }
        } else if (entries instanceof List<?> list) {
            for (Object entry : list) {
                if (entry instanceof Map<?, ?> item && item.get("field") != null) {
                    fields.add(String.valueOf(item.get("field")).strip().toLowerCase(Locale.ROOT));
                }
            }
        }
        return fields;
    }

    private static Set<String> parseUnknownParameterMessage(Map<String, Object> body) {
        Set<String> fields = new HashSet<>();
        String message = String.valueOf(body.getOrDefault("message", ""));
        String marker = "unknown query parameter(s):";
        int at = message.indexOf(marker);
        if (at >= 0) {
            for (String listed : message.substring(at + marker.length()).split(",")) {
                Matcher named = PARAMETER_NAME.matcher(listed);
                if (named.find()) {
                    fields.add(named.group().toLowerCase(Locale.ROOT));
                }
            }
        }
        return fields;
    }

    /** {@code false} means interrupted: the flag is restored, and the call must be abandoned. */
    private static boolean sleepSeconds(double seconds) {
        if (seconds <= 0) {
            return true;
        }
        try {
            Thread.sleep((long) (seconds * 1000));
            return true;
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** The whole chain, since OkHttp wraps a stalled read's {@link SocketTimeoutException}. */
    static boolean isTimeoutFailure(Throwable failure) {
        Set<Throwable> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Throwable current = failure; current != null && seen.add(current);
                current = current.getCause()) {
            if (current instanceof SocketTimeoutException) {
                return true;
            }
        }
        return false;
    }

    private static boolean meansInterrupted(IOException exception) {
        return !(exception instanceof SocketTimeoutException)
                && exception instanceof InterruptedIOException;
    }

    /** Not {@link ErrorType#NETWORK}: that is the retryable class, and the caller cancelled. */
    private SdkError interruptedSend(String method, HttpUrl url, TransportRequest request,
            int requestsSent, boolean staleEchoResent, long started, Exception cause) {
        Thread.currentThread().interrupt();
        return new SdkError(
                method + " " + endpoint(request, url)
                        + " was interrupted after " + requestsSent + " attempt(s)"
                        + detailOf(cause),
                null,
                ErrorType.UNKNOWN,
                null,
                null,
                requestInfo(method, request, requestsSent,
                        grantedAttempts(staleEchoResent), started),
                cause);
    }

    /** The captured request: a list query value is sent as one pair per element. */
    public record TransportRequest(
            String httpMethod,
            String url,
            Map<String, Object> queryParams,
            Object jsonBody,
            Map<String, Object> formData,
            Map<String, Object> files,
            Map<String, String> headers,
            Map<String, String> requestInfoHeaders,
            Object requestInfoBody,
            String pathTemplate) {

        /** Capture the caller's collections, so every attempt sends one content. */
        public TransportRequest {
            queryParams = detach(queryParams);
            jsonBody = Redaction.detachValue(jsonBody);
            formData = formData instanceof Owned
                    ? formData : detach(materialiseFormFields(formData));
            files = files instanceof Owned ? files : detach(materialiseFileParts(files));
            headers = detach(headers);
            requestInfoHeaders = detach(requestInfoHeaders);
            requestInfoBody = Redaction.detachValue(requestInfoBody);
        }

        /** Keeps {@code null} distinct from empty: an absent body and an empty one differ. */
        private static <V> Map<String, V> detach(Map<String, V> values) {
            if (values == null) {
                return null;
            }
            return values instanceof Owned ? values : new Owned<>(Redaction.detachMapping(values));
        }

        /** A capture this type already made, so re-capturing it can be skipped. */
        private static final class Owned<V> extends AbstractMap<String, V> {
            private final Map<String, V> entries;

            Owned(Map<String, V> entries) {
                this.entries = entries;
            }

            @Override
            public Set<Map.Entry<String, V>> entrySet() {
                return entries.entrySet();
            }

            @Override
            public V get(Object key) {
                return entries.get(key);
            }

            @Override
            public boolean containsKey(Object key) {
                return entries.containsKey(key);
            }

            @Override
            public int size() {
                return entries.size();
            }

            @Override
            public boolean isEmpty() {
                return entries.isEmpty();
            }

            @Override
            public Set<String> keySet() {
                return entries.keySet();
            }

            @Override
            public Collection<V> values() {
                return entries.values();
            }

            @Override
            public void forEach(BiConsumer<? super String, ? super V> action) {
                entries.forEach(action);
            }
        }

        /** A copy: Java has no immutable array, so returning its own would leak the capture. */
        public Map<String, Object> files() {
            return files == null ? null : Redaction.detachMapping(files);
        }

        /** A fresh {@code byte[]} for a rendered document, else the unmodifiable capture. */
        public Object jsonBody() {
            return jsonBody instanceof byte[] bytes ? bytes.clone() : jsonBody;
        }

        /** A copy, because a value may be a {@code byte[]} and Java has no immutable array. */
        public Map<String, Object> queryParams() {
            return queryParams == null ? null : Redaction.detachMapping(queryParams);
        }

        public Object requestInfoBody() {
            return Redaction.detachValue(requestInfoBody);
        }

        /** How the 401 replay puts a fresh bearer token on an otherwise byte-identical request. */
        public TransportRequest withHeaders(
                Map<String, String> headers, Map<String, String> requestInfoHeaders) {
            return new TransportRequest(httpMethod, url, queryParams, jsonBody, formData,
                    files, headers, requestInfoHeaders, requestInfoBody, pathTemplate);
        }

        public static Builder builder(String httpMethod, String url) {
            return new Builder(httpMethod, url);
        }

        /** Not thread-safe; build then share. */
        public static final class Builder {
            private final String httpMethod;
            private final String url;
            private Map<String, Object> queryParams;
            private Object jsonBody;
            private Map<String, Object> formData;
            private Map<String, Object> files;
            private Map<String, String> headers;
            private Map<String, String> requestInfoHeaders;
            private Object requestInfoBody;
            private String pathTemplate;

            private Builder(String httpMethod, String url) {
                this.httpMethod = httpMethod;
                this.url = url;
            }

            public Builder queryParams(Map<String, Object> queryParams) {
                this.queryParams = queryParams;
                return this;
            }

            /** Rendered UTF-8 wire bytes are cloned and sent as they are; text is sent verbatim. */
            public Builder jsonBody(Object jsonBody) {
                this.jsonBody = jsonBody;
                return this;
            }

            public Builder formData(Map<String, Object> formData) {
                this.formData = formData;
                return this;
            }

            /** Each a {@link Path}, a {@code byte[]} or a {@code [filename, bytes, type?]} list. */
            public Builder files(Map<String, Object> files) {
                this.files = files;
                return this;
            }

            public Builder headers(Map<String, String> headers) {
                this.headers = headers;
                return this;
            }

            /** Already passed through redaction; the live headers must not be passed here. */
            public Builder requestInfoHeaders(Map<String, String> requestInfoHeaders) {
                this.requestInfoHeaders = requestInfoHeaders;
                return this;
            }

            /** Already passed through redaction; the live body must not be passed here. */
            public Builder requestInfoBody(Object requestInfoBody) {
                this.requestInfoBody = requestInfoBody;
                return this;
            }

            /** What a default diagnostic names: it identifies the endpoint and nobody on it. */
            public Builder pathTemplate(String pathTemplate) {
                this.pathTemplate = pathTemplate;
                return this;
            }

            public TransportRequest build() {
                return new TransportRequest(httpMethod, url, queryParams, jsonBody,
                        formData, files, headers, requestInfoHeaders, requestInfoBody,
                        pathTemplate);
            }
        }
    }
}
