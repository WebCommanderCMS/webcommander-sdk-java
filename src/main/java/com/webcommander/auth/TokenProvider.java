package com.webcommander.auth;

import com.webcommander.client.ApiResponse;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkAuthError;
import com.webcommander.errors.SdkConfigError;
import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.http.Transport;
import com.webcommander.Redaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/** Supplies the access token: read from the {@link TokenStore}, renewed when it is due. */
public final class TokenProvider {

    /** Where a tolerated cache-write refusal is reported. */
    private static final Logger CACHE_LOG = LoggerFactory.getLogger("webcommander.tokenCache");

    /** Carries the same {@code {api_version}} placeholder as every other path constant here. */
    public static final String TOKEN_PATH_TEMPLATE = "/api/{api_version}/oauth2/token";
    private static final String API_VERSION_PLACEHOLDER = "{api_version}";
    private static final String REFRESH_GRANT_TYPE = "refresh_token";

    /** A {@code null} version leaves the empty segment visible in the path. */
    public static String buildTokenPath(String apiVersion) {
        return TOKEN_PATH_TEMPLATE.replace(API_VERSION_PLACEHOLDER,
                apiVersion == null ? "" : apiVersion);
    }

    private final SdkConfig config;
    private final Transport transport;
    private final TokenStore tokenStore;
    private final String tokenPath;
    private final ReentrantLock threadLock = new ReentrantLock();

    /** The transport's TLS and retry settings govern authentication too. */
    public TokenProvider(SdkConfig config, Transport transport, TokenStore tokenStore) {
        this.config = config;
        this.transport = transport;
        this.tokenStore = tokenStore;
        this.tokenPath = buildTokenPath(config.getApiVersion());
    }

    public String getTokenUrl() {
        return config.getBaseUrl() + tokenPath;
    }

    /** Never {@code null}: a refused grant raises {@link SdkAuthError} instead. */
    public String getAccessToken() {
        TokenRecord record = TokenRecord.fromMap(tokenStore.readTokenRecord());
        if (record != null && !record.needsRefresh()) {
            return record.accessToken();
        }
        return renewAccessToken(null, true);
    }

    /** Answers a freshly minted token, or the replacement another worker already fetched. */
    public String refreshAccessToken(String staleAccessToken) {
        return renewAccessToken(staleAccessToken, false);
    }

    public void clearAccessToken() {
        tokenStore.clearTokenRecord();
    }

    private String renewAccessToken(String staleAccessToken, boolean anyCachedTokenWillDo) {
        // Taken outside the try: a refusal must not reach the finally and unlock nothing.
        TokenLock.acquire(threadLock, "the in-process token refresh lock");
        try (AutoCloseable ignored = tokenStore.acquireTokenRefreshLock()) {
            TokenRecord record = TokenRecord.fromMap(tokenStore.readTokenRecord());
            if (record != null && !record.needsRefresh()) {
                if (anyCachedTokenWillDo
                        || (staleAccessToken != null && !record.accessToken().equals(staleAccessToken))) {
                    return record.accessToken();
                }
            }

            TokenRecord renewed = null;
            if (record != null && record.refreshToken() != null) {
                renewed = requestTokenWithRefreshGrant(record.refreshToken());
            }
            if (renewed == null) {
                renewed = requestTokenWithCredentials();
            }
            try {
                tokenStore.writeTokenRecord(renewed.toMap());
            } catch (SdkUsageError programmingFailure) {
                throw programmingFailure;
            } catch (SdkError cacheRefusal) {
                if (Thread.currentThread().isInterrupted()) {
                    throw cacheRefusal;
                }
                CACHE_LOG.warn("token cache write refused by {}: {} - the token just "
                        + "obtained is returned, and the next call will authenticate "
                        + "again rather than read a cached token",
                        tokenStore.getClass().getSimpleName(),
                        cacheRefusal.getClass().getSimpleName());
            }
            return renewed.accessToken();
        } catch (RuntimeException error) {
            throw error;
        } catch (Exception error) {
            throw new SdkConfigError(
                    "tokenCachePath: the token refresh lock could not be released ("
                            + Redaction.describeFailure(error)
                            + "); pass a writable path, or use memoryOnlyTokenCache() to "
                            + "keep the token in memory", error);
        } finally {
            threadLock.unlock();
        }
    }

    private TokenRecord requestTokenWithCredentials() {
        ApiResponse response = transport.sendHttpRequest(
                Transport.TransportRequest.builder("POST", getTokenUrl())
                        .jsonBody(credentialsPayload())
                        .headers(tokenHeaders())
                        .build());
        if (!response.succeeded()) {
            boolean detail = SdkError.showsFullDetail();
            List<String> messages = detail ? response.getErrorMessages() : List.of();
            SdkAuthError error = new SdkAuthError(
                    "token request failed with " + response.getStatusCode()
                            + (messages.isEmpty() ? "" : ": " + messages.get(0)),
                    response.getStatusCode(),
                    response.getErrorType(),
                    detail ? Redaction.redactValue(response.getMap()) : null,
                    detail ? Redaction.redactMapping(response.getHeaders()) : null,
                    response.getRequestInfo());
            for (String message : messages) {
                error.addError(message);
            }
            throw error;
        }
        return buildTokenRecord(response, null);
    }

    private Map<String, Object> credentialsPayload() {
        return grantPayload(config.getAuth().getGrantType(),
                "auth_string", config.getAuth().getAuthString());
    }

    private Map<String, Object> grantPayload(String grantType, String grantKey, String grantValue) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("grant_type", grantType);
        payload.put(grantKey, grantValue);
        payload.put("redirect_uri", config.getAuth().getRedirectUri());
        payload.put("client_id", config.getAuth().getClientId());
        payload.put("client_secret", config.getAuth().getClientSecret());
        return payload;
    }

    private TokenRecord requestTokenWithRefreshGrant(String refreshToken) {
        Map<String, Object> payload = grantPayload(REFRESH_GRANT_TYPE, "refresh_token", refreshToken);
        try {
            ApiResponse response = transport.sendHttpRequest(
                    Transport.TransportRequest.builder("POST", getTokenUrl())
                            .jsonBody(payload)
                            .headers(tokenHeaders())
                            .build());
            if (!response.succeeded()) {
                return null;
            }
            return buildTokenRecord(response, refreshToken);
        } catch (SdkUsageError
                | SdkConfigError notARefusal) {
            throw notARefusal;
        } catch (SdkError refused) {
            if (Thread.currentThread().isInterrupted()) {
                throw refused;
            }
            return null;
        }
    }

    private Map<String, String> tokenHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("User-Agent", config.getUserAgent());
        return headers;
    }

    private TokenRecord buildTokenRecord(ApiResponse response, String previousRefreshToken) {
        Object body = response.getMap();
        Object granted = body instanceof Map<?, ?> envelope ? envelope.get("access_token") : null;
        if (!(granted instanceof String token) || !TokenRecord.isBearerCredential(token)) {
            throw grantRefusal(response, body, granted == null
                    ? "the token response did not contain an access_token"
                    : granted instanceof String
                            ? "the token response's access_token is not a sendable bearer "
                                    + "credential - RFC 6750 section 2.1 allows letters, "
                                    + "digits and - . _ ~ + / with optional trailing '='"
                            : "the token response's access_token is "
                                    + granted.getClass().getSimpleName()
                                    + " rather than text, and this SDK does not convert a "
                                    + "JSON value into a credential");
        }
        Map<?, ?> envelope = (Map<?, ?>) body;
        double ttlSeconds = readTtlSeconds(envelope.get("expires_in"));
        Object refresh = envelope.get("refresh_token");
        if (refresh != null && !(refresh instanceof String)) {
            throw grantRefusal(response, body,
                    "the token response's refresh_token is "
                    + refresh.getClass().getSimpleName() + " rather than text, and this "
                    + "SDK does not convert a JSON value into a credential");
        }
        String refreshed = (String) refresh;
        if (refreshed != null && !refreshed.isEmpty()
                && !TokenRecord.isTokenResponseText(refreshed)) {
            throw grantRefusal(response, body,
                    "the token response's refresh_token cannot be sent back - RFC 6749 "
                    + "section 5.1 limits a token-response value to visible ASCII and the "
                    + "space character");
        }
        return new TokenRecord(
                token,
                refreshed != null && !refreshed.isEmpty() ? refreshed : previousRefreshToken,
                System.currentTimeMillis() / 1000.0 + ttlSeconds,
                Math.min(TokenRecord.EXPIRY_SKEW_SECONDS, ttlSeconds / 2));
    }

    /** One refusal shape for every unusable grant answer. */
    private SdkAuthError grantRefusal(ApiResponse response, Object body, String reason) {
        return new SdkAuthError(reason,
                response.getStatusCode(), response.getErrorType(),
                SdkError.showsFullDetail()
                        ? Redaction.redactValue(body) : null,
                Map.of(), response.getRequestInfo());
    }

    /** NaN, infinity, non-positive and unparseable lifetimes all take the default. */
    private static double readTtlSeconds(Object expiresIn) {
        double ttlSeconds = TokenRecord.DEFAULT_TOKEN_TTL_SECONDS;
        if (expiresIn instanceof Number number) {
            ttlSeconds = number.doubleValue();
        } else if (expiresIn instanceof String text) {
            try {
                ttlSeconds = Double.parseDouble(text);
            } catch (NumberFormatException ignored) {
            }
        }
        if (Double.isNaN(ttlSeconds) || Double.isInfinite(ttlSeconds) || ttlSeconds <= 0) {
            ttlSeconds = TokenRecord.DEFAULT_TOKEN_TTL_SECONDS;
        }
        return Math.min(ttlSeconds, TokenRecord.MAX_TOKEN_TTL_SECONDS);
    }
}
