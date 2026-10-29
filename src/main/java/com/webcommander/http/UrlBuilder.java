package com.webcommander.http;

import com.webcommander.errors.SdkUsageError;
import com.webcommander.Redaction;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** URL assembly for WebCommander paths. */
public final class UrlBuilder {

    private UrlBuilder() {}

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([a-z_][a-z0-9_]*)}");
    private static final Pattern QUERY_OR_FRAGMENT = Pattern.compile("[?#]");
    /** A {@code name=***} pair: a URL carrying the marker is a printed copy, not the real value. */
    private static final Pattern REDACTED_PARAMETER =
            Pattern.compile("[?#&]([^?#&=]*)=" + Pattern.quote(Redaction.REDACTED) + "(?=[&#]|$)");

    /** The {@code {name}} placeholders in a template, first-seen order, query part excluded. */
    public static List<String> findPathParamNames(String pathTemplate) {
        Set<String> names = new LinkedHashSet<>();
        Matcher matcher = PLACEHOLDER.matcher(splitTemplate(pathTemplate)[0]);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return new ArrayList<>(names);
    }

    /** Fill {@code {name}} placeholders, percent-encoding each value and refusing dot-only ones. */
    public static String substitutePathParams(String pathTemplate, Map<String, ?> pathParams) {
        String[] parts = splitTemplate(pathTemplate);
        List<String> expected = findPathParamNames(pathTemplate);
        Map<String, ?> supplied = pathParams == null ? Map.of() : pathParams;

        List<String> problems = new ArrayList<>();
        List<String> missing = expected.stream().filter(name -> !supplied.containsKey(name)).sorted().toList();
        if (!missing.isEmpty()) {
            problems.add("missing path params: " + String.join(", ", missing));
        }
        Set<String> unknown = new TreeSet<>(supplied.keySet());
        expected.forEach(unknown::remove);
        if (!unknown.isEmpty()) {
            problems.add("unknown path params: " + String.join(", ", unknown));
        }
        List<String> blank = new ArrayList<>();
        List<String> wrongTyped = new ArrayList<>();
        List<String> dotOnly = new ArrayList<>();
        for (String name : expected) {
            if (!supplied.containsKey(name)) {
                continue;
            }
            Object value = supplied.get(name);
            if (value == null || (value instanceof String text && text.strip().isEmpty())) {
                blank.add(name);
            } else if (!usablePathParam(value)) {
                wrongTyped.add(name + " (" + value.getClass().getSimpleName() + ")");
            } else if (isDotOnly(pathParamText(value))) {
                dotOnly.add(name);
            }
        }
        if (!blank.isEmpty()) {
            problems.add("blank path params: " + String.join(", ", blank.stream().sorted().toList()));
        }
        if (!wrongTyped.isEmpty()) {
            problems.add("wrong-typed path params: " + String.join(", ", wrongTyped.stream().sorted().toList()));
        }
        if (!dotOnly.isEmpty()) {
            problems.add("dot-only path params (a value of only dots is a relative"
                    + " path segment, which the URL layer removes, so the request would"
                    + " reach a different endpoint): "
                    + String.join(", ", dotOnly.stream().sorted().toList()));
        }
        if (!problems.isEmpty()) {
            throw new SdkUsageError(pathTemplate + " -> " + String.join("; ", problems));
        }

        Matcher matcher = PLACEHOLDER.matcher(parts[0]);
        StringBuilder out = new StringBuilder();
        while (matcher.find()) {
            Object value = supplied.get(matcher.group(1));
            matcher.appendReplacement(out,
                    Matcher.quoteReplacement(percentEncode(pathParamText(value))));
        }
        matcher.appendTail(out);
        String substituted = out.toString();
        if (substituted.indexOf('{') >= 0 || substituted.indexOf('}') >= 0) {
            throw new SdkUsageError(
                    pathTemplate + " -> malformed placeholder, unresolved: " + substituted);
        }
        return substituted + parts[1];
    }

    /** Join a base URL and a path; an absolute path must be on the configured origin. */
    public static String buildUrl(String baseUrl, String path) {
        rejectARedactedValue(path);
        rejectAFragment(path);
        String lowered = path.toLowerCase(Locale.ROOT);
        if (lowered.startsWith("http://") || lowered.startsWith("https://")) {
            requireSameOrigin(baseUrl, path);
            return path;
        }
        String trimmedBase = baseUrl.replaceAll("/+$", "");
        return trimmedBase + (path.startsWith("/") ? "" : "/") + path;
    }

    static void rejectAFragment(String path) {
        int hash = path.indexOf('#');
        if (hash >= 0) {
            throw new SdkUsageError("path: a '#' at character " + hash + " begins a URL "
                    + "fragment, which is never sent; percent-encode a literal '#'");
        }
    }

    /** RFC 3986 percent-encoding with an empty safe set: unreserved characters only. */
    private static String percentEncode(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder out = new StringBuilder(bytes.length);
        for (byte encoded : bytes) {
            int character = encoded & 0xff;
            boolean unreserved = (character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z')
                    || (character >= '0' && character <= '9')
                    || character == '-' || character == '.'
                    || character == '_' || character == '~';
            if (unreserved) {
                out.append((char) character);
            } else {
                out.append('%').append(String.format("%02X", character));
            }
        }
        return out.toString();
    }

    private static void rejectARedactedValue(String path) {
        Matcher found = REDACTED_PARAMETER.matcher(path);
        if (found.find()) {
            throw new SdkUsageError(
                    "path: parameter '" + found.group(1) + "' holds only the redaction marker "
                    + Redaction.REDACTED + ", so this URL is a redacted copy");
        }
    }

    private static void requireSameOrigin(String baseUrl, String url) {
        requireParseable(url);
        // Authority, never java.net.URI: its null host and userinfo would compare equal.
        Authority parsed = Authority.split(url);
        if (carriesCredentials(parsed.getUserInfo())) {
            throw new SdkUsageError(
                    "absolute URL for host " + parsed.getHost() + " carries embedded "
                    + "credentials, which would replace the bearer token; strip the "
                    + "userinfo before following it");
        }
        Authority base = Authority.split(baseUrl);
        requireAUsablePort("absolute URL", parsed);
        requireAUsablePort("baseUrl", base);
        if (!origin(base).equals(origin(parsed))) {
            throw new SdkUsageError(
                    "absolute URL: this is not the configured origin (differing: "
                    + originMismatch(base, parsed) + "); an absolute URL is requested "
                    + "with the access token attached, so only the store's own links "
                    + "may be passed back");
        }
    }

    /** Which origin parts disagree, so the refusal can give a reason without either URL. */
    private static String originMismatch(Authority base, Authority parsed) {
        List<String> differing = new ArrayList<>();
        if (!base.getScheme().equals(parsed.getScheme())) {
            differing.add("scheme");
        }
        if (!base.getHost().equals(parsed.getHost())) {
            differing.add("host");
        }
        if (effectivePort(base) != effectivePort(parsed)) {
            differing.add("port");
        }
        return differing.isEmpty() ? "origin" : String.join(", ", differing);
    }

    /** Whether a {@code userinfo} run actually carries a credential. */
    private static boolean carriesCredentials(String userInfo) {
        if (userInfo == null) {
            return false;
        }
        int colon = userInfo.indexOf(':');
        String user = colon < 0 ? userInfo : userInfo.substring(0, colon);
        String password = colon < 0 ? "" : userInfo.substring(colon + 1);
        return !user.isEmpty() || !password.isEmpty();
    }

    /** Refuse a port no TCP stack can dial; {@code which} names the side being judged. */
    private static void requireAUsablePort(String which, Authority parsed) {
        if (parsed.isPortMalformed()) {
            throw new SdkUsageError(which + ": unusable port: "
                    + parsed.getAuthorityProblem() + "; the URL itself is not shown");
        }
    }

    /** Refuse a URL that {@link URI} cannot parse. */
    private static void requireParseable(String url) {
        try {
            new URI(url);
        } catch (URISyntaxException error) {
            throw new SdkUsageError("absolute URL: not a parseable URL: " + error.getReason()
                    + (error.getIndex() >= 0 ? " at index " + error.getIndex() : "")
                    + "; the URL itself is not shown");
        }
    }

    /** scheme://host:port with the scheme's implied port made explicit. */
    private static String origin(Authority authority) {
        return authority.getScheme() + "://" + authority.getHost() + ":"
                + effectivePort(authority);
    }

    /** The written port, or the scheme's implied one when none was written. */
    private static int effectivePort(Authority authority) {
        int port = authority.getPort();
        if (port >= 0) {
            return port;
        }
        String scheme = authority.getScheme();
        return "https".equals(scheme) ? 443 : "http".equals(scheme) ? 80 : -1;
    }

    private static String[] splitTemplate(String pathTemplate) {
        Matcher boundary = QUERY_OR_FRAGMENT.matcher(pathTemplate);
        if (boundary.find()) {
            return new String[] {
                    pathTemplate.substring(0, boundary.start()),
                    pathTemplate.substring(boundary.start()),
            };
        }
        return new String[] {pathTemplate, ""};
    }

    private static String pathParamText(Object value) {
        return value instanceof String raw ? raw.strip() : String.valueOf(value);
    }

    /** Whether a value is nothing but dots — {@code "."}, {@code ".."} and longer runs. */
    private static boolean isDotOnly(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    private static boolean usablePathParam(Object value) {
        return value instanceof String || value instanceof Integer || value instanceof Long;
    }

    /** The URL authority grammar, hand-rolled: {@code URI.getHost()} is null for a '_' host. */
    public static final class Authority {

        private final String scheme;
        private final String netloc;
        private final String userInfo;
        private final String host;
        private final int port;
        private final String authorityProblem;

        private Authority(String scheme, String netloc, String userInfo, String host,
                int port, String authorityProblem) {
            this.scheme = scheme;
            this.netloc = netloc;
            this.userInfo = userInfo;
            this.host = host;
            this.port = port;
            this.authorityProblem = authorityProblem;
        }

        /** Split a URL into its parts; {@code null} splits to an all-absent result. */
        public static Authority split(String url) {
            if (url == null) {
                return new Authority("", "", null, "", -1, null);
            }
            String rest = url;
            String scheme = "";
            int colon = rest.indexOf(':');
            if (colon > 0 && isSchemeText(rest.substring(0, colon))) {
                scheme = rest.substring(0, colon).toLowerCase(Locale.ROOT);
                rest = rest.substring(colon + 1);
            }
            if (!rest.startsWith("//")) {
                return new Authority(scheme, "", null, "", -1, null);
            }
            rest = rest.substring(2);
            int end = rest.length();
            for (int i = 0; i < rest.length(); i++) {
                char character = rest.charAt(i);
                if (character == '/' || character == '?' || character == '#') {
                    end = i;
                    break;
                }
            }
            String netloc = rest.substring(0, end);

            String userInfo = null;
            String hostinfo = netloc;
            int at = netloc.lastIndexOf('@');
            if (at >= 0) {
                userInfo = netloc.substring(0, at);
                hostinfo = netloc.substring(at + 1);
            }

            String host;
            String portText;
            if (hostinfo.startsWith("[")) {
                int close = hostinfo.indexOf(']');
                if (close < 0) {
                    return new Authority(scheme, netloc, userInfo, "", -1, INVALID_IPV6);
                }
                host = hostinfo.substring(0, close + 1);
                String tail = hostinfo.substring(close + 1);
                portText = tail.startsWith(":") ? tail.substring(1) : "";
            } else {
                int portColon = hostinfo.indexOf(':');
                host = portColon < 0 ? hostinfo : hostinfo.substring(0, portColon);
                portText = portColon < 0 ? "" : hostinfo.substring(portColon + 1);
            }

            int port = -1;
            String problem = null;
            if (!portText.isEmpty()) {
                try {
                    port = Integer.parseInt(portText);
                    if (port < 0 || port > 65535) {
                        port = -1;
                        problem = PORT_OUT_OF_RANGE;
                    }
                } catch (NumberFormatException notANumber) {
                    problem = "Port could not be cast to integer value as '" + portText + "'";
                }
            }
            return new Authority(scheme, netloc, userInfo,
                    host.toLowerCase(Locale.ROOT), port, problem);
        }

        /** An authority whose IPv6 literal never closes its bracket. */
        private static final String INVALID_IPV6 = "Invalid IPv6 URL";
        /** A port that reads as a number no TCP stack can dial. */
        private static final String PORT_OUT_OF_RANGE = "Port out of range 0-65535";

        /** RFC 3986 {@code scheme}: a letter, then letters, digits, {@code +-.} */
        private static boolean isSchemeText(String candidate) {
            if (candidate.isEmpty() || !Character.isLetter(candidate.charAt(0))) {
                return false;
            }
            for (int i = 1; i < candidate.length(); i++) {
                char character = candidate.charAt(i);
                if (!Character.isLetterOrDigit(character)
                        && character != '+' && character != '-' && character != '.') {
                    return false;
                }
            }
            return true;
        }

        /** The scheme in lower case, or {@code ""} when the URL carries none. */
        public String getScheme() {
            return scheme;
        }

        /** The {@code userinfo} before the last {@code @}, or {@code null}. */
        public String getUserInfo() {
            return userInfo;
        }

        /** The host in lower case, {@code ""} when there is none. */
        public String getHost() {
            return host;
        }

        /** The host with an IPv6 literal's brackets stripped, for dialling. */
        public String getHostForConnect() {
            if (host.length() > 1 && host.startsWith("[") && host.endsWith("]")) {
                return host.substring(1, host.length() - 1);
            }
            return host;
        }

        /** The explicit port, or {@code -1} when none was written. */
        public int getPort() {
            return port;
        }

        /** Whether a written port or IPv6 literal could not be read. */
        public boolean isPortMalformed() {
            return authorityProblem != null;
        }

        /** Why it could not be read: an unreadable port or an unclosed IPv6 literal. */
        public String getAuthorityProblem() {
            return authorityProblem;
        }

        /** Whether the URL carries an authority at all. */
        public boolean hasNetloc() {
            return !netloc.isEmpty();
        }
    }
}
