package com.webcommander.errors;

/** A name that does not exist on a client or on a bound resource. */
public class SdkAttributeError extends SdkUsageError {

    /** A lookup by name that matched nothing registered. */
    public SdkAttributeError(String message) {
        super(message);
    }
}
