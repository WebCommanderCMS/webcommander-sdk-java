package com.webcommander.errors;

/** A caller mistake the SDK detects before any network I/O. */
public class SdkUsageError extends SdkError {

    /** A caller mistake caught before anything left the process. */
    public SdkUsageError(String message) {
        super(message);
    }

    /** As above, keeping the failure that exposed the mistake. */
    public SdkUsageError(String message, Throwable cause) {
        super(message, null, null, null, null, null, cause);
    }
}
