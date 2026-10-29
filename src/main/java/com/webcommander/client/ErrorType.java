package com.webcommander.client;

/** A call's classification, from the HTTP status and a 2xx body's {@code isSuccess} flag. */
public enum ErrorType {
    /** The call succeeded: a 2xx whose envelope did not deny success. */
    NONE("none"),
    /** 3xx. Reaches the caller as a failure, since the transport does not follow it. */
    REDIRECT("redirect"),
    /** 400 or 422: the store read the request and refused its contents. */
    VALIDATION("validation"),
    /** 401: the credentials were missing or rejected, after one refresh-and-replay. */
    AUTHENTICATION("authentication"),
    /** 403: authenticated, but this instance may not perform the call. */
    AUTHORIZATION("authorization"),
    /** 404: no such resource. */
    NOT_FOUND("not_found"),
    /** 405: the path exists but not for this method. */
    METHOD_NOT_ALLOWED("method_not_allowed"),
    /** 409: the call contradicts the resource's current state. */
    CONFLICT("conflict"),
    /** 429: throttled; retried first only when the retry policy allows it. */
    RATE_LIMIT("rate_limit"),
    /** 5xx: the store failed to answer the call it received. */
    SERVER("server"),
    /** No answer arrived at all — connect failure, reset, TLS. The retryable class. */
    NETWORK("network"),
    /** The configured connect or read wait elapsed with no answer. */
    TIMEOUT("timeout"),
    /** Anything the names above do not cover: an odd status, a bare 2xx denial, a cancel. */
    UNKNOWN("unknown");

    private final String value;

    ErrorType(String value) {
        this.value = value;
    }

    /** The wire spelling, e.g. {@code not_found}; stable across releases. */
    public String value() {
        return value;
    }

    /** The wire spelling, as {@link #value()}. */
    @Override
    public String toString() {
        return value;
    }

    /** Map an HTTP status to a classification, taking any 2xx at its word. */
    public static ErrorType classify(int statusCode) {
        return classify(statusCode, false);
    }

    /** The same, told whether a 2xx body carried an {@code isSuccess} denial. */
    public static ErrorType classify(int statusCode, boolean bodyDeniesSuccess) {
        if (statusCode >= 200 && statusCode < 300) {
            return bodyDeniesSuccess ? UNKNOWN : NONE;
        }
        if (statusCode >= 300 && statusCode < 400) {
            return REDIRECT;
        }
        return switch (statusCode) {
            case 400, 422 -> VALIDATION;
            case 401 -> AUTHENTICATION;
            case 403 -> AUTHORIZATION;
            case 404 -> NOT_FOUND;
            case 405 -> METHOD_NOT_ALLOWED;
            case 409 -> CONFLICT;
            case 429 -> RATE_LIMIT;
            default -> statusCode >= 500 && statusCode < 600 ? SERVER : UNKNOWN;
        };
    }
}
