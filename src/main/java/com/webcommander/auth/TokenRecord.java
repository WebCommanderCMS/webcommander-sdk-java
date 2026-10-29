package com.webcommander.auth;

import com.webcommander.Redaction;
import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** An empty {@code refreshToken} normalises to {@code null}; the skew is per record. */
public record TokenRecord(
        String accessToken, String refreshToken, double expiresAt, double skewSeconds) {

    /** Each credential is judged by where it goes: two destinations, two grammars. */
    public TokenRecord {
        refreshToken = refreshToken == null || refreshToken.isEmpty() ? null : refreshToken;
        if (!isBearerCredential(accessToken)) {
            throw new SdkUsageError(
                    "accessToken: not a sendable bearer credential (RFC 6750 section 2.1)");
        }
        if (refreshToken != null && !isTokenResponseText(refreshToken)) {
            throw new SdkUsageError(
                    "refreshToken: not a sendable token-response value (RFC 6749 section 5.1)");
        }
    }

    /** RFC 6750 2.1 {@code b64token}: alphanumerics, {@code -._~+/}, then {@code =} padding. */
    static boolean isBearerCredential(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        int index = 0;
        while (index < value.length() && isB64TokenCharacter(value.charAt(index))) {
            index++;
        }
        if (index == 0) {
            return false;   // padding alone is not a credential
        }
        while (index < value.length() && value.charAt(index) == '=') {
            index++;
        }
        return index == value.length();
    }

    /** One {@code b64token} character, padding excluded. */
    private static boolean isB64TokenCharacter(char character) {
        return (character >= 'A' && character <= 'Z')
                || (character >= 'a' && character <= 'z')
                || (character >= '0' && character <= '9')
                || character == '-' || character == '.' || character == '_'
                || character == '~' || character == '+' || character == '/';
    }

    /** RFC 6749 5.1's {@code VSCHAR}, {@code %x20-7E}. */
    static boolean isTokenResponseText(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character < 0x20 || character > 0x7E) {
                return false;
            }
        }
        return true;
    }

    /** Assumed lifetime when the server states none. */
    public static final double DEFAULT_TOKEN_TTL_SECONDS = 3600;
    public static final double EXPIRY_SKEW_SECONDS = 60;
    public static final double MAX_TOKEN_TTL_SECONDS = 24 * 60 * 60;

    /** True inside the skew the token was minted with, and for an expiry past the max TTL. */
    public boolean needsRefresh() {
        double now = System.currentTimeMillis() / 1000.0;
        if (expiresAt - now > MAX_TOKEN_TTL_SECONDS) {
            return true;
        }
        return now >= expiresAt - skewSeconds;
    }

    /** Carries both tokens in the clear: meant for a store, not for a log. */
    public Map<String, Object> toMap() {
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("access_token", accessToken);
        record.put("refresh_token", refreshToken);
        record.put("expires_at", expiresAt);
        record.put("skew_seconds", skewSeconds);
        return record;
    }

    /** Refused without a usable {@code access_token} or a finite {@code expires_at}. */
    public static TokenRecord fromMap(Map<String, Object> record) {
        if (record == null) {
            return null;
        }
        Object accessToken = record.get("access_token");
        Double expiresAt = finiteNumber(record.get("expires_at"));
        if (!(accessToken instanceof String token) || !isBearerCredential(token)) {
            throw new SdkError(
                    "the cached token record carries no usable access_token ("
                    + describeType(accessToken) + "); clearTokenRecord() removes it");
        }
        if (expiresAt == null) {
            throw new SdkError(
                    "the cached token record carries no finite expires_at ("
                    + describeType(record.get("expires_at")) + "); clearTokenRecord() removes it");
        }
        Double skewSeconds = finiteNumber(record.get("skew_seconds"));
        if (skewSeconds == null || skewSeconds < 0) {
            skewSeconds = EXPIRY_SKEW_SECONDS;
        }
        skewSeconds = Math.min(skewSeconds, MAX_TOKEN_TTL_SECONDS);
        Object refresh = record.get("refresh_token");
        if (refresh != null && !(refresh instanceof String)) {
            throw new SdkError(
                    "the cached token record carries a refresh_token that is "
                    + describeType(refresh) + " rather than text; clearTokenRecord() removes it");
        }
        String refreshed = (String) refresh;
        if (refreshed != null && !refreshed.isEmpty() && !isTokenResponseText(refreshed)) {
            throw new SdkError(
                    "the cached token record carries a refresh_token that cannot be sent "
                    + "(RFC 6749 section 5.1); clearTokenRecord() removes it");
        }
        return new TokenRecord(token, refreshed, expiresAt, skewSeconds);
    }

    /** A rejected value named by its type, never by its content: it is a credential. */
    private static String describeType(Object value) {
        return value == null ? "absent" : "a " + value.getClass().getSimpleName();
    }

    private static Double finiteNumber(Object value) {
        if (!(value instanceof Number number)) {
            return null;
        }
        double seconds = number.doubleValue();
        return Double.isNaN(seconds) || Double.isInfinite(seconds) ? null : seconds;
    }

    /** Both tokens redacted, and both numerics reduced to a state. */
    @Override
    public String toString() {
        String numerics = SdkError.showsFullDetail()
                ? "expiresAt=" + expiresAt + ", skewSeconds=" + skewSeconds
                : "expiresAt=<" + (needsRefresh() ? "expired" : "live")
                        + ">, skewSeconds=<seconds>";
        return "TokenRecord(" + String.join(", ", Redaction.redactFields(this, TOKEN_FIELDS))
                + ", " + numerics + ")";
    }

    /** The token fields {@link #toString()} renders, in record-component order. */
    private static final List<String> TOKEN_FIELDS = List.of("accessToken", "refreshToken");
}
