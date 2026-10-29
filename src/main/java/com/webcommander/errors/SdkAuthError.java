package com.webcommander.errors;

import com.webcommander.client.ErrorType;
import com.webcommander.client.RequestInfo;

import java.util.Map;

/** Authentication failed: a rejected grant, a 401 surviving one refresh-and-replay, or a 403. */
public class SdkAuthError extends SdkError {

    /** A credential failure carrying the response that reported it, already redacted. */
    public SdkAuthError(
            String message,
            Integer statusCode,
            ErrorType errorType,
            Object rawResponse,
            Map<String, String> responseHeaders,
            RequestInfo requestInfo) {
        super(message, statusCode, errorType, rawResponse, responseHeaders, requestInfo);
    }
}
