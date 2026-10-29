package com.webcommander.config;

import com.webcommander.errors.SdkConfigError;
import com.webcommander.http.UrlBuilder;
import com.webcommander.Redaction;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

/** The credentials, and where the resulting token is kept; behaviour is {@link SdkConfig}. */
public final class AuthConfig {

    /** The default, so nothing lands in the working directory. */
    public static final String USE_DEFAULT_TOKEN_CACHE = "\0__WC_USE_DEFAULT_TOKEN_CACHE__\0";

    /** The only flow the credentials on this class describe. */
    public static final String DEFAULT_GRANT_TYPE = "client_credentials";

    /** The fields {@link #toString()} delegates to {@link Redaction#redactFields}. */
    private static final List<String> REPR_FIELDS = List.of(
            "baseUrl", "clientId", "clientSecret", "redirectUri", "authString",
            "clientName", "grantType", "tokenCachePath");

    /** The fields with no default, in declaration order. */
    private static final List<String> REQUIRED_FIELDS = List.of(
            "baseUrl", "clientId", "clientSecret", "redirectUri", "authString", "clientName");

    private final String baseUrl;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String authString;
    private final String clientName;
    private final String grantType;
    private final String tokenCachePath;

    private AuthConfig(Builder builder) {
        this.baseUrl = normaliseBaseUrl(builder.baseUrl);
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.redirectUri = builder.redirectUri;
        this.authString = builder.authString;
        this.clientName = builder.clientName;
        this.grantType = builder.grantType;
        this.tokenCachePath = builder.tokenCachePath;
        validate();
    }

    /** The six credentials unset; the grant and the cache path at their defaults. */
    public static Builder builder() {
        return new Builder();
    }

    /** This config is left untouched whatever the builder is then told. */
    public Builder toBuilder() {
        Builder copy = new Builder();
        copy.baseUrl = baseUrl;
        copy.clientId = clientId;
        copy.clientSecret = clientSecret;
        copy.redirectUri = redirectUri;
        copy.authString = authString;
        copy.clientName = clientName;
        copy.grantType = grantType;
        copy.tokenCachePath = tokenCachePath;
        return copy;
    }

    public AuthConfig withOverrides(UnaryOperator<Builder> overrides) {
        Builder builder = toBuilder();
        return (overrides == null ? builder : overrides.apply(builder)).build();
    }

    /** Surrounding whitespace and trailing slashes removed, never blank. */
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    /** The real secret, never masked: do not log this. */
    public String getClientSecret() {
        return clientSecret;
    }

    /** Not validated as a URL and printed in clear, so keep credentials out of its query. */
    public String getRedirectUri() {
        return redirectUri;
    }

    /** The real value, never masked: do not log this. */
    public String getAuthString() {
        return authString;
    }

    /** As supplied, padding included; a registry matches it exactly. */
    public String getClientName() {
        return clientName;
    }

    public String getGrantType() {
        return grantType;
    }

    /** Compare against the {@link #USE_DEFAULT_TOKEN_CACHE} sentinel by {@code ==}. */
    public String getTokenCachePath() {
        return tokenCachePath;
    }

    /** Credentials redacted, so safe to log. */
    @Override
    public String toString() {
        List<String> shown = new ArrayList<>(Redaction.redactFields(this, REPR_FIELDS));
        if (tokenCachePath == USE_DEFAULT_TOKEN_CACHE) {
            shown.set(REPR_FIELDS.indexOf("tokenCachePath"),
                    "tokenCachePath=USE_DEFAULT_TOKEN_CACHE");
        }
        return "AuthConfig(" + String.join(", ", shown) + ")";
    }

    private void validate() {
        List<String> problems = new ArrayList<>();

        List<String> missing = new ArrayList<>();
        if (isBlank(baseUrl)) {
            missing.add("baseUrl");
        }
        if (isBlank(clientId)) {
            missing.add("clientId");
        }
        if (isBlank(clientSecret)) {
            missing.add("clientSecret");
        }
        if (isBlank(redirectUri)) {
            missing.add("redirectUri");
        }
        if (isBlank(authString)) {
            missing.add("authString");
        }
        if (!missing.isEmpty()) {
            problems.add("missing required values: " + String.join(", ", missing));
        }
        if (!isBlank(baseUrl)) {
            problems.addAll(problemsWithTheBaseUrl(baseUrl));
        }
        if (isBlank(clientName)) {
            problems.add("clientName: must not be blank");
        }
        if (isBlank(grantType)) {
            problems.add("grantType: must not be blank");
        }

        if (tokenCachePath != null && tokenCachePath != USE_DEFAULT_TOKEN_CACHE) {
            if (tokenCachePath.strip().isEmpty()) {
                problems.add("tokenCachePath: must not be blank — pass null to keep the "
                        + "token in memory, or name the file to write");
            } else if (isExistingDirectory(tokenCachePath)) {
                problems.add("tokenCachePath: " + Redaction.redactPath(tokenCachePath)
                        + " is an existing directory, so nothing could be written to it — "
                        + "name the file to write, or pass null");
            }
        }

        if (!problems.isEmpty()) {
            String label = isBlank(clientName) ? "<unnamed>" : clientName.strip();
            throw new SdkConfigError("instance " + label + ": " + String.join("; ", problems));
        }
    }

    private static String normaliseBaseUrl(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.strip();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    private static List<String> problemsWithTheBaseUrl(String baseUrl) {
        List<String> problems = new ArrayList<>();
        UrlBuilder.Authority parsed = UrlBuilder.Authority.split(baseUrl);
        if (parsed.isPortMalformed()) {
            problems.add("baseUrl: is not a URL (" + parsed.getAuthorityProblem() + ")");
            return problems;
        }
        String scheme = parsed.getScheme();
        if (!scheme.equals("http") && !scheme.equals("https") || !parsed.hasNetloc()) {
            String shown = Redaction.redactText(baseUrl);
            boolean unrewrittenUserinfo =
                    shown.indexOf('@') >= 0 && !shown.contains(Redaction.REDACTED + "@");
            problems.add("baseUrl: must start with http:// or https:// and include a host"
                    + (unrewrittenUserinfo
                            ? ". The value is not shown: it may carry credentials"
                            : ", got '" + shown + "'"));
            return problems;
        }
        if (parsed.getUserInfo() != null) {
            problems.add("baseUrl: host " + parsed.getHost() + " carries embedded credentials, "
                    + "which would replace the bearer token; strip the userinfo");
        }
        int mark = indexOfAny(baseUrl, '?', '#');
        if (mark >= 0) {
            problems.add("baseUrl: must be the instance origin only, with no query string "
                    + "or fragment, got '" + Redaction.redactText(baseUrl) + "'");
        }
        return problems;
    }

    /** Never throws: a value no filesystem could name is not a directory. */
    private static boolean isExistingDirectory(String path) {
        try {
            return Files.isDirectory(Path.of(path));
        } catch (InvalidPathException notAPath) {
            return false;
        }
    }

    private static int indexOfAny(String text, char... targets) {
        for (int i = 0; i < text.length(); i++) {
            for (char target : targets) {
                if (text.charAt(i) == target) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isBlank(String value) {
        return value == null || value.strip().isEmpty();
    }

    /** {@code baseUrl} through {@code clientName} are the six {@link #build()} will not guess. */
    public static final class Builder {
        private String baseUrl;
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authString;
        private String clientName;
        private String grantType = DEFAULT_GRANT_TYPE;
        private String tokenCachePath = USE_DEFAULT_TOKEN_CACHE;

        public Builder() {
        }

        /** An {@code http://} or {@code https://} origin with a host and nothing after it. */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /** Sent as given and never parsed. */
        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder authString(String authString) {
            this.authString = authString;
            return this;
        }

        /** Matched exactly, and shown in logs. */
        public Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        /** A shareable file, the default-cache sentinel, or {@code null} for memory only. */
        public Builder tokenCachePath(String tokenCachePath) {
            this.tokenCachePath = tokenCachePath;
            return this;
        }

        /** The readable spelling of {@code tokenCachePath(null)}: nothing on disk. */
        public Builder memoryOnlyTokenCache() {
            return tokenCachePath(null);
        }

        /** The message names the instance and lists every problem at once. */
        public AuthConfig build() {
            return new AuthConfig(this);
        }
    }

    static List<String> absentRequiredFields(Set<String> suppliedNames) {
        List<String> absent = new ArrayList<>();
        for (String field : REQUIRED_FIELDS) {
            String name = snakeCase(field);
            if (!suppliedNames.contains(name)) {
                absent.add(name);
            }
        }
        return absent;
    }

    /** Every field name of this class, snake_cased as a loader mapping spells it. */
    static Set<String> fieldNames() {
        Set<String> names = new LinkedHashSet<>();
        for (Field field : AuthConfig.class.getDeclaredFields()) {
            if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                names.add(snakeCase(field.getName()));
            }
        }
        return names;
    }

    static String snakeCase(String name) {
        StringBuilder out = new StringBuilder(name.length() + 4);
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isUpperCase(character) && i > 0) {
                boolean previousLower = Character.isLowerCase(name.charAt(i - 1));
                boolean nextLower =
                        i + 1 < name.length() && Character.isLowerCase(name.charAt(i + 1));
                if (previousLower || nextLower) {
                    out.append('_');
                }
            }
            out.append(Character.toLowerCase(character));
        }
        return out.toString();
    }

    /** The inverse of {@link #snakeCase}: the builder method a mapping key names. */
    static String camelCase(String snakeCaseName) {
        StringBuilder out = new StringBuilder(snakeCaseName.length());
        boolean raise = false;
        for (int i = 0; i < snakeCaseName.length(); i++) {
            char character = snakeCaseName.charAt(i);
            if (character == '_') {
                raise = true;
                continue;
            }
            out.append(raise ? Character.toUpperCase(character) : character);
            raise = false;
        }
        return out.toString();
    }
}
