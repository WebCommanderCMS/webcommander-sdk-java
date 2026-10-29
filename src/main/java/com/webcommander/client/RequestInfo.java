package com.webcommander.client;

import com.webcommander.Redaction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Everything about the request behind a response, safe to log or attach to an exception. */
public final class RequestInfo {

    private final String clientName;
    private final String apiVersion;
    private final String httpMethod;
    private final String url;
    private final Map<String, Object> queryParams;
    private final Object body;
    private final Map<String, String> headers;
    private final int attemptCount;
    private final int maxAttempts;
    private final double elapsedSeconds;
    private final String pathTemplate;

    /** Capture one exchange; {@code url} carries no query string. */
    public RequestInfo(
            String clientName,
            String apiVersion,
            String httpMethod,
            String url,
            Map<String, Object> queryParams,
            Object body,
            Map<String, String> headers,
            int attemptCount,
            int maxAttempts,
            double elapsedSeconds,
            String pathTemplate) {
        this.pathTemplate = pathTemplate;
        this.clientName = clientName;
        this.apiVersion = apiVersion;
        this.httpMethod = httpMethod;
        this.url = Redaction.redactText(url);
        this.queryParams = redactQueryParams(queryParams);
        this.body = Redaction.redactedSnapshot(body);
        this.headers = Collections.unmodifiableMap(
                new LinkedHashMap<>(Redaction.redactMapping(headers)));
        this.attemptCount = attemptCount;
        this.maxAttempts = maxAttempts;
        this.elapsedSeconds = elapsedSeconds;
    }

    /** The query parameters, redacted by key name and by value, then frozen. */
    private static Map<String, Object> redactQueryParams(Map<String, Object> params) {
        Map<String, Object> shown = new LinkedHashMap<>();
        if (params == null) {
            return Collections.unmodifiableMap(shown);
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            shown.put(entry.getKey(),
                    Redaction.shouldRedact(entry.getKey())
                            ? Redaction.REDACTED
                            : Redaction.redactedSnapshot(entry.getValue()));
        }
        return Collections.unmodifiableMap(shown);
    }

    /** The name of the client that sent it. */
    public String getClientName() {
        return clientName;
    }

    /** The API version in force for the call. */
    public String getApiVersion() {
        return apiVersion;
    }

    /** The HTTP verb, upper-cased. */
    public String getHttpMethod() {
        return httpMethod;
    }

    /** The URL without the query string, redacted. */
    public String getUrl() {
        return url;
    }

    /** The endpoint as the call site named it, placeholders unresolved, or {@code null}. */
    public String getPathTemplate() {
        return pathTemplate;
    }

    /** The redacted query parameters, as a fresh copy per call; never {@code null}. */
    public Map<String, Object> getQueryParams() {
        return Redaction.snapshotMapping(queryParams);
    }

    /** A redacted summary of what was sent, as a fresh copy; uploads are described, never read. */
    public Object getBody() {
        return Redaction.snapshotValue(body);
    }

    /** The request headers, redacted and unmodifiable; never {@code null}. */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /** Which attempt produced this, counting from 1. */
    public int getAttemptCount() {
        return attemptCount;
    }

    /** How many attempts the retry policy allowed. */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /** Wall-clock time for the exchange, in seconds. */
    public double getElapsedSeconds() {
        return elapsedSeconds;
    }
}
