package com.webcommander.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkAuthError;
import com.webcommander.errors.SdkError;
import com.webcommander.Redaction;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.http.Transport;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** What every SDK call returns: one HTTP exchange — what was sent, what came back. */
public final class ApiResponse {

    private static final int MAX_REPORTED_MESSAGES = 50;
    private static final int MAX_MESSAGE_DEPTH = 4;

    private static final ObjectMapper JSON = new ObjectMapper();

    private final int statusCode;
    private final Object data;
    private final Map<String, String> headers;
    private final String contentType;
    private final ErrorType errorType;
    private final RequestInfo requestInfo;
    /** The body's own bytes, so {@link #getJson()} returns the store's text, not a re-encoding. */
    private final byte[] rawBody;
    /** Which member {@link #getData()} follows; never null once resolved. */
    private final String responseFormat;
    /** How {@link #getDto()} builds its view, or null when the call names no shape. */
    private final DtoFactory dtoFactory;

    private final Object lazyViews = new Object();
    private volatile String jsonText;
    private volatile boolean jsonTextRead;
    private volatile Object dto;
    private volatile boolean dtoBuilt;

    /** One exchange with its body's own bytes and how it is presented; {@code data} stays live. */
    public ApiResponse(
            int statusCode,
            Object data,
            Map<String, String> headers,
            String contentType,
            RequestInfo requestInfo,
            ErrorType errorType,
            byte[] rawBody,
            String responseFormat,
            DtoFactory dtoFactory) {
        this.rawBody = rawBody;
        this.responseFormat = responseFormat == null
                ? SdkConfig.DEFAULT_RESPONSE_FORMAT
                : SdkConfig.normaliseResponseFormat(responseFormat);
        this.dtoFactory = dtoFactory;
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers == null ? Map.of() : Map.copyOf(headers);
        this.contentType = contentType;
        boolean denied = bodyDeniesSuccess(data);
        this.errorType = errorType == null
                || errorType == ErrorType.NONE && (statusCode >= 300 || denied)
                ? ErrorType.classify(statusCode, denied) : errorType;
        this.requestInfo = requestInfo;
    }

    /** The HTTP status; a 2xx alone does not mean the call worked, see {@link #succeeded()}. */
    public int getStatusCode() {
        return statusCode;
    }

    /** The body in the form this call resolved to: the only member that follows configuration. */
    public Object getData() {
        if (SdkConfig.RESPONSE_FORMAT_JSON.equals(responseFormat)) {
            return getJson();
        }
        if (SdkConfig.RESPONSE_FORMAT_DTO.equals(responseFormat)) {
            return getDto();
        }
        return data;
    }

    /** The decoded body itself, live and unredacted: mutating it changes this response. */
    public Object getMap() {
        return data;
    }

    /** The body's own text exactly as the store sent it, whatever {@code responseFormat} says. */
    public String getJson() {
        if (jsonTextRead) {
            return jsonText;
        }
        synchronized (lazyViews) {
            if (!jsonTextRead) {
                jsonText = materialiseJsonText();
                jsonTextRead = true;
            }
            return jsonText;
        }
    }

    /** The typed view of the body, whatever {@code responseFormat} says; {@code null} if none. */
    public Object getDto() {
        if (dtoBuilt) {
            return dto;
        }
        synchronized (lazyViews) {
            if (!dtoBuilt) {
                dto = dtoFactory == null || !(data instanceof Map) ? null
                        : dtoFactory.buildDto(data);
                dtoBuilt = true;
            }
            return dto;
        }
    }

    /** {@link #getDto()} as the type you expect, refused if the view is of another type. */
    public <T> T getDto(Class<T> dtoType) {
        Object view = getDto();
        if (view == null) {
            return null;
        }
        if (!dtoType.isInstance(view)) {
            throw new SdkUsageError("this response's DTO is a "
                    + view.getClass().getSimpleName() + ", not a " + dtoType.getSimpleName()
                    + " — the endpoint decides the shape, so ask for the one it produces");
        }
        return dtoType.cast(view);
    }

    /** Which member {@link #getData()} follows: {@code "map"}, {@code "json"} or {@code "dto"}. */
    public String getResponseFormat() {
        return responseFormat;
    }

    /** The same exchange presented another way, sharing its status, body and record. */
    public ApiResponse withResponseView(String responseFormat, DtoFactory dtoFactory) {
        String format = responseFormat == null ? this.responseFormat : responseFormat;
        DtoFactory factory = dtoFactory == null ? this.dtoFactory : dtoFactory;
        if (format.equals(this.responseFormat) && factory == this.dtoFactory) {
            return this;
        }
        return new ApiResponse(statusCode, data, headers, contentType, requestInfo, errorType,
                rawBody, format, factory);
    }

    private String materialiseJsonText() {
        if (data instanceof byte[] || rawBody == null || rawBody.length == 0) {
            return null;
        }
        return new String(rawBody, textCharset());
    }

    /** The declared charset, or UTF-8, which RFC 8259 says a JSON body is. */
    private Charset textCharset() {
        String lowered = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        Charset declared = Transport.declaredCharset(lowered);
        return declared != null ? declared : StandardCharsets.UTF_8;
    }

    /** The response headers, unmodifiable and not redacted: redact before printing or storing. */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /** The response {@code Content-Type} with its parameters, or {@code null} if none. */
    public String getContentType() {
        return contentType;
    }

    /** How the failure classifies; {@link ErrorType#NONE} when status and body agree. */
    public ErrorType getErrorType() {
        return errorType;
    }

    /** What was sent, redacted and detached at construction; {@code null} if built without one. */
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    /** Whether the call succeeded: a 2xx status and a body carrying no {@code isSuccess} denial. */
    public boolean succeeded() {
        return statusCode >= 200 && statusCode < 300 && !bodyDeniesSuccess(data);
    }

    /** Whether a body carries an explicit {@code isSuccess} denial. */
    private static boolean bodyDeniesSuccess(Object data) {
        return data instanceof Map<?, ?> body
                && body.containsKey("isSuccess")
                && !isTruthy(body.get("isSuccess"));
    }

    /** JSON truthiness: what a store's {@code isSuccess} flag counts as true. */
    private static boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean flag) {
            return flag;
        }
        if (value instanceof Number number) {
            return number.doubleValue() != 0.0;
        }
        if (value instanceof CharSequence text) {
            return text.length() > 0;
        }
        if (value instanceof Collection<?> items) {
            return !items.isEmpty();
        }
        if (value instanceof Map<?, ?> entries) {
            return !entries.isEmpty();
        }
        return true;
    }

    /** Human-readable problems from the most specific tier present: errors, message, error. */
    public List<String> getErrorMessages() {
        if (succeeded()) {
            return List.of();
        }
        Object body = data;
        if (!(body instanceof Map)) {
            body = parseJsonBody(body);
        }
        if (!(body instanceof Map<?, ?> envelope)) {
            return List.of();
        }
        List<String> messages = readErrorEntries(envelope.get("errors"));
        if (messages.isEmpty()) {
            messages = readMessageValue(envelope.get("message"), 0);
        }
        if (messages.isEmpty()) {
            messages = readMessageValue(envelope.get("error"), 0);
            if (messages.isEmpty()) {
                messages = readMessageValue(envelope.get("errorCode"), 0);
            }
        }
        if (messages.isEmpty()) {
            messages = readMessageValue(envelope.get("detail"), 0);
            if (messages.isEmpty()) {
                messages = readMessageValue(envelope.get("title"), 0);
            }
        }
        return capMessages(messages);
    }

    /** Raise when the call failed, otherwise return this response for chaining. */
    public ApiResponse raiseForStatus() {
        if (succeeded()) {
            return this;
        }
        boolean detail = SdkError.showsFullDetail();
        List<String> messages = detail ? getErrorMessages() : List.of();
        String summary = messages.isEmpty() ? "HTTP " + statusCode : messages.get(0);
        Object body = detail ? Redaction.redactValue(data) : null;
        Map<String, String> shownHeaders = detail ? Redaction.redactMapping(headers) : null;
        SdkError error = (statusCode == 401 || statusCode == 403)
                ? new SdkAuthError(summary, statusCode, errorType, body, shownHeaders, requestInfo)
                : new SdkError(summary, statusCode, errorType, body, shownHeaders, requestInfo);
        for (String message : messages) {
            error.addError(message);
        }
        throw error;
    }

    private static Object parseJsonBody(Object body) {
        String text;
        if (body instanceof byte[] bytes) {
            text = new String(bytes, StandardCharsets.UTF_8);
        } else if (body instanceof String given) {
            text = given;
        } else {
            return null;
        }
        text = text.strip().replace("\uFEFF", "").strip();
        if (!text.startsWith("{") && !text.startsWith("[")) {
            return null;
        }
        try {
            Object parsed = JSON.readValue(text, Object.class);
            return parsed instanceof Map ? parsed : null;
        } catch (Exception error) {
            return null;
        }
    }

    /** Read the {@code errors} tier, whichever of its three wild shapes arrived. */
    private static List<String> readErrorEntries(Object entries) {
        List<String> messages = new ArrayList<>();
        if (entries instanceof Map<?, ?> byField) {
            for (Map.Entry<?, ?> entry : byField.entrySet()) {
                prefixField(entry.getKey(), readMessageValue(entry.getValue(), 0), messages);
            }
            return messages;
        }
        if (entries instanceof List<?> list) {
            for (Object entry : list) {
                if (entry instanceof Map<?, ?> item) {
                    Object text = item.get("message");
                    if (text == null) {
                        text = item.get("code");
                    }
                    prefixField(item.get("field"), readMessageValue(text, 0), messages);
                } else {
                    messages.addAll(readMessageValue(entry, 0));
                }
            }
            return messages;
        }
        if (entries instanceof String only) {
            return readMessageValue(only, 0);
        }
        return messages;
    }

    /** Turn any value from an error body into messages; blanks dropped, depth-guarded. */
    private static List<String> readMessageValue(Object value, int depth) {
        if (value == null || depth > MAX_MESSAGE_DEPTH) {
            return List.of();
        }
        if (value instanceof String message) {
            return message.strip().isEmpty() ? List.of() : List.of(message);
        }
        if (value instanceof Map<?, ?> nested) {
            Object text = nested.get("message");
            if (text == null) {
                text = nested.get("code");
            }
            return readMessageValue(text, depth + 1);
        }
        if (value instanceof List<?> list) {
            List<String> messages = new ArrayList<>();
            for (Object item : list) {
                messages.addAll(readMessageValue(item, depth + 1));
            }
            return messages;
        }
        String text = String.valueOf(value);
        return text.strip().isEmpty() ? List.of() : List.of(text);
    }

    /** Tested for presence, not truthiness: a bulk import's row 0 is a real field. */
    private static void prefixField(Object fieldName, List<String> fieldMessages, List<String> out) {
        String name = fieldName == null ? "" : String.valueOf(fieldName).strip();
        for (String message : fieldMessages) {
            out.add(name.isEmpty() ? message : name + ": " + message);
        }
    }

    private static List<String> capMessages(List<String> messages) {
        if (messages.size() <= MAX_REPORTED_MESSAGES) {
            return messages;
        }
        List<String> capped = new ArrayList<>(messages.subList(0, MAX_REPORTED_MESSAGES));
        capped.add("+" + (messages.size() - MAX_REPORTED_MESSAGES) + " more problems");
        return capped;
    }
}
