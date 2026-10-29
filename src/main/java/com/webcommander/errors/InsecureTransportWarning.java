package com.webcommander.errors;

/** The transport cannot be trusted: TLS verification is off, or the base URL is plain HTTP. */
public final class InsecureTransportWarning {

    private final String message;

    /** A warning to hand to a logger. */
    public InsecureTransportWarning(String message) {
        this.message = message;
    }

    /** Why the transport cannot be trusted, without the category prefix. */
    public String getMessage() {
        return message;
    }

    /** {@code InsecureTransportWarning: <message>} — what the log record carries. */
    @Override
    public String toString() {
        return "InsecureTransportWarning: " + message;
    }
}
