package com.webcommander.auth;

import java.util.Map;

/** Where an access token lives between calls: persistence plus refresh coordination. */
public interface TokenStore {

    /** The stored token in the shared cache format, copied out so a caller cannot mutate it. */
    Map<String, Object> readTokenRecord();

    /** Replaces whatever was there, copying the record rather than keeping the caller's map. */
    void writeTokenRecord(Map<String, Object> record);

    /** Discard the stored token; a no-op when there is none. */
    void clearTokenRecord();

    /** Held for the life of the returned handle, so one worker fetches while the others wait. */
    AutoCloseable acquireTokenRefreshLock();
}
