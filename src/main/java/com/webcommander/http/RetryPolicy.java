package com.webcommander.http;

import com.webcommander.errors.SdkConfigError;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/** Retry decisions and backoff timing. */
public final class RetryPolicy {

    /** Statuses worth another attempt at all (safe methods). */
    public static final Set<Integer> DEFAULT_RETRYABLE_HTTP_CODES =
            Collections.unmodifiableSet(new LinkedHashSet<>(List.of(429, 500, 502, 503, 504)));

    /** Statuses a <b>state-changing</b> method may be replayed on: <b>none by default</b>. */
    public static final Set<Integer> DEFAULT_UNSAFE_RETRYABLE_HTTP_CODES =
            Collections.unmodifiableSet(new LinkedHashSet<>());

    /** The methods RFC 9110 §9.2.1 calls safe: read-only, so a repeat duplicates nothing. */
    public static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");

    /** The safe methods plus RFC 9110's idempotent ones: a repeat cannot change the outcome. */
    public static final Set<String> IDEMPOTENT_METHODS = idempotentMethods();

    private static Set<String> idempotentMethods() {
        Set<String> methods = new LinkedHashSet<>(SAFE_METHODS);
        methods.add("PUT");
        methods.add("DELETE");
        return Collections.unmodifiableSet(methods);
    }

    /** How far an attempt got before it failed, which decides whether it may be sent again. */
    public enum TransportFailure {
        /** The connection was never established, so the store cannot have seen the request. */
        NOT_DELIVERED,
        /** Connected, so the request may have been acted on; retried only when idempotent. */
        MAYBE_DELIVERED,
        /** Not a transport failure at all. Never retried. */
        NOT_TRANSPORT
    }

    private static final int MIN_HTTP_CODE = 100;
    private static final int MAX_HTTP_CODE = 599;
    private static final int MAX_HTTP_CODES = 500;

    private final int maxAttempts;
    private final double backoffSeconds;
    private final double backoffMaxSeconds;
    private final Set<Integer> retryableStatusCodes;
    private final Set<Integer> unsafeRetryableStatusCodes;

    /** A policy with every value given explicitly. */
    public RetryPolicy(
            int maxAttempts,
            double backoffSeconds,
            double backoffMaxSeconds,
            Set<Integer> retryableStatusCodes,
            Set<Integer> unsafeRetryableStatusCodes) {
        if (maxAttempts < 1) {
            throw new SdkConfigError(
                    "maxAttempts: must be at least 1 (1 disables retry), got " + maxAttempts);
        }
        requireFinite("backoffSeconds", backoffSeconds);
        requireFinite("backoffMaxSeconds", backoffMaxSeconds);
        if (backoffSeconds < 0) {
            throw new SdkConfigError("backoffSeconds: must be >= 0, got " + backoffSeconds);
        }
        if (backoffMaxSeconds < backoffSeconds) {
            throw new SdkConfigError("backoffMaxSeconds: must be >= backoffSeconds, got "
                    + backoffMaxSeconds + " < " + backoffSeconds);
        }
        this.maxAttempts = maxAttempts;
        this.backoffSeconds = backoffSeconds;
        this.backoffMaxSeconds = backoffMaxSeconds;
        this.retryableStatusCodes = copyCodes("retryableStatusCodes", retryableStatusCodes);
        this.unsafeRetryableStatusCodes =
                copyCodes("unsafeRetryableStatusCodes", unsafeRetryableStatusCodes);
    }

    /** A sorted defensive copy, refusing nulls as an {@link SdkConfigError}, not a bare NPE. */
    private static Set<Integer> copyCodes(String name, Set<Integer> codes) {
        if (codes == null) {
            throw new SdkConfigError(name + ": expected a set of HTTP status codes, got null");
        }
        List<Integer> ordered = new ArrayList<>(codes);
        if (ordered.contains(null)) {
            throw new SdkConfigError(name + ": null is not an HTTP status code");
        }
        if (ordered.size() > MAX_HTTP_CODES) {
            throw new SdkConfigError(name + ": at most " + MAX_HTTP_CODES + " status codes "
                    + "(100-599 is every code there is), got " + ordered.size());
        }
        for (Integer code : ordered) {
            if (code < MIN_HTTP_CODE || code > MAX_HTTP_CODE) {
                throw new SdkConfigError(name + ": " + code + " is not an HTTP status code");
            }
        }
        ordered.sort(Comparator.naturalOrder());
        return Collections.unmodifiableSet(new LinkedHashSet<>(ordered));
    }

    /** Total attempts including the first; 1 means no repeat. */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /** Whether this status warrants another attempt; an unknown method counts as state-changing. */
    public boolean shouldRetryStatusCode(int statusCode, String method) {
        if (!retryableStatusCodes.contains(statusCode)) {
            return false;
        }
        if (isUnsafeMethod(method)) {
            return unsafeRetryableStatusCodes.contains(statusCode);
        }
        return true;
    }

    /** The same decision from a classification the transport has already made. */
    public boolean shouldRetryAfterFailure(TransportFailure failure, String method) {
        if (failure == null) {
            return false;
        }
        return switch (failure) {
            case NOT_DELIVERED -> true;
            case MAYBE_DELIVERED -> isIdempotentMethod(method);
            case NOT_TRANSPORT -> false;
        };
    }

    /** How far an attempt got: the one place never-delivered is told from may-have-arrived. */
    public static TransportFailure classifyTransportFailure(Throwable failure) {
        if (!(failure instanceof IOException)) {
            return TransportFailure.NOT_TRANSPORT;
        }
        Set<Throwable> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Throwable current = failure; current != null && seen.add(current);
                current = current.getCause()) {
            if (neverLeftThisProcess(current)) {
                return TransportFailure.NOT_DELIVERED;
            }
        }
        return TransportFailure.MAYBE_DELIVERED;
    }

    /** Whether one link in a failure's chain names a connection that was never established. */
    private static boolean neverLeftThisProcess(Throwable failure) {
        if (failure instanceof UnknownHostException
                || failure instanceof ConnectException
                || failure instanceof NoRouteToHostException
                || failure instanceof PortUnreachableException
                || failure instanceof SSLHandshakeException
                || failure instanceof SSLPeerUnverifiedException) {
            return true;
        }
        if (failure instanceof SocketTimeoutException) {
            String message = failure.getMessage() == null
                    ? "" : failure.getMessage().toLowerCase(Locale.ROOT);
            return message.contains("connect");
        }
        return false;
    }

    /** Whether a repeat leaves the same state; {@code null} or an unknown method answers false. */
    public static boolean isIdempotentMethod(String method) {
        if (method == null) {
            return false;
        }
        return IDEMPOTENT_METHODS.contains(method.strip().toUpperCase(Locale.ROOT));
    }

    /** The wait before the next attempt: full jitter or {@code Retry-After}, both capped. */
    public double computeBackoffSeconds(int attempt, String retryAfter) {
        Double serverDelay = parseRetryAfter(retryAfter);
        if (serverDelay != null) {
            return Math.min(serverDelay, backoffMaxSeconds);
        }
        double window = Math.min(
                backoffSeconds * Math.pow(2, backoffDoublings(attempt)), backoffMaxSeconds);
        return window <= 0 ? 0.0 : ThreadLocalRandom.current().nextDouble(0.0, window);
    }

    /** How many doublings the exponential window may take before it saturates. */
    public static final int MAX_BACKOFF_DOUBLINGS = 1023;

    private static int backoffDoublings(int attempt) {
        return Math.min(Math.max(0, attempt - 1), MAX_BACKOFF_DOUBLINGS);
    }

    /** Whether a replay could duplicate an effect: anything outside {@link #SAFE_METHODS} can. */
    public static boolean isUnsafeMethod(String method) {
        if (method == null) {
            return true;
        }
        return !SAFE_METHODS.contains(method.strip().toUpperCase(Locale.ROOT));
    }

    /** The numeric shapes a {@code Retry-After} may take. */
    private static final Pattern DECIMAL_SECONDS =
            Pattern.compile("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?");
    private static final Pattern NAMED_NON_FINITE = Pattern.compile("(?i)[+-]?(nan|inf|infinity)");

    private static Double parseRetryAfter(String retryAfter) {
        if (retryAfter == null || retryAfter.strip().isEmpty()) {
            return null;
        }
        String value = retryAfter.strip();
        if (DECIMAL_SECONDS.matcher(value).matches()) {
            double seconds = Double.parseDouble(value);
            return seconds > 0.0 ? seconds : 0.0;
        }
        if (NAMED_NON_FINITE.matcher(value).matches()) {
            if (value.toLowerCase(Locale.ROOT).contains("nan")) {
                return 0.0;
            }
            return value.startsWith("-") ? 0.0 : Double.POSITIVE_INFINITY;
        }
        OffsetDateTime when = parseHttpDate(value);
        if (when == null) {
            return null;
        }
        double seconds = (when.toInstant().toEpochMilli() - System.currentTimeMillis()) / 1000.0;
        return Math.max(0.0, seconds);
    }

    /** Month names as an {@code HTTP-date} may spell them, abbreviated then in full. */
    private static final List<String> MONTH_NAMES = List.of(
            "jan", "feb", "mar", "apr", "may", "jun",
            "jul", "aug", "sep", "oct", "nov", "dec",
            "january", "february", "march", "april", "may", "june",
            "july", "august", "september", "october", "november", "december");

    /** Day names, only ever skipped — never checked against the date they precede. */
    private static final Set<String> DAY_NAMES =
            Set.of("mon", "tue", "wed", "thu", "fri", "sat", "sun");

    /** The obsolete zone abbreviations RFC 5322 §4.3 still requires a reader to know. */
    private static final Map<String, Integer> OBSOLETE_ZONE_SECONDS =
            Map.ofEntries(
                    Map.entry("UT", 0),
                    Map.entry("UTC", 0),
                    Map.entry("GMT", 0),
                    Map.entry("Z", 0),
                    Map.entry("AST", -4 * 3600),
                    Map.entry("ADT", -3 * 3600),
                    Map.entry("EST", -5 * 3600),
                    Map.entry("EDT", -4 * 3600),
                    Map.entry("CST", -6 * 3600),
                    Map.entry("CDT", -5 * 3600),
                    Map.entry("MST", -7 * 3600),
                    Map.entry("MDT", -6 * 3600),
                    Map.entry("PST", -8 * 3600),
                    Map.entry("PDT", -7 * 3600));

    /** Hand-rolled: RFC_1123_DATE_TIME reads one of the three forms RFC 9110 §5.6.7 requires. */
    private static OffsetDateTime parseHttpDate(String value) {
        List<String> parts = new ArrayList<>();
        for (String token : value.strip().split("\\s+")) {
            if (!token.isEmpty()) {
                parts.add(token);
            }
        }
        if (parts.isEmpty()) {
            return null;
        }
        String first = parts.get(0);
        if (first.endsWith(",") || DAY_NAMES.contains(first.toLowerCase(Locale.ROOT))) {
            parts.remove(0);
        } else {
            int comma = first.lastIndexOf(',');
            if (comma >= 0) {
                parts.set(0, first.substring(comma + 1));
            }
        }
        if (parts.size() == 3) {
            String[] hyphenated = parts.get(0).split("-");
            if (hyphenated.length == 3) {
                List<String> spread = new ArrayList<>(Arrays.asList(hyphenated));
                spread.addAll(parts.subList(1, parts.size()));
                parts = spread;
            }
        }
        if (parts.size() == 4) {
            String tail = parts.get(3);
            int sign = tail.indexOf('+') >= 0 ? tail.indexOf('+') : tail.indexOf('-');
            if (sign > 0) {
                parts.set(3, tail.substring(0, sign));
                parts.add(tail.substring(sign));
            } else {
                parts.add("");
            }
        }
        if (parts.size() < 5) {
            return null;
        }
        String dayText = parts.get(0);
        String monthText = parts.get(1).toLowerCase(Locale.ROOT);
        String yearText = parts.get(2);
        String timeText = parts.get(3);
        String zoneText = parts.get(4);
        if (!MONTH_NAMES.contains(monthText)) {
            String dayWasSecond = parts.get(1);
            monthText = dayText.toLowerCase(Locale.ROOT);
            dayText = dayWasSecond;
            if (!MONTH_NAMES.contains(monthText)) {
                return null;
            }
        }
        int month = MONTH_NAMES.indexOf(monthText) + 1;
        if (month > 12) {
            month -= 12;
        }
        dayText = withoutTrailingComma(dayText);
        if (yearText.indexOf(':') > 0) {
            String clockWasThird = yearText;
            yearText = timeText;
            timeText = clockWasThird;
        }
        if (yearText.endsWith(",")) {
            yearText = withoutTrailingComma(yearText);
            if (!yearText.isEmpty() && !Character.isDigit(yearText.charAt(0))) {
                String yearWasZone = zoneText;
                zoneText = yearText;
                yearText = yearWasZone;
            }
        }
        timeText = withoutTrailingComma(timeText);
        String[] clock = timeText.split(":", -1);
        if (clock.length == 1 && timeText.indexOf('.') >= 0) {
            clock = timeText.split("\\.", -1);
        }
        if (clock.length < 2 || clock.length > 3) {
            return null;
        }
        try {
            int year = Integer.parseInt(yearText);
            int day = Integer.parseInt(dayText);
            int hour = Integer.parseInt(clock[0]);
            int minute = Integer.parseInt(clock[1]);
            int second = clock.length == 3 ? Integer.parseInt(clock[2]) : 0;
            if (year < 100) {
                year += year > 68 ? 1900 : 2000;
            }
            return OffsetDateTime.of(year, month, day, hour, minute, second, 0,
                    readZone(zoneText));
        } catch (NumberFormatException | DateTimeException notADate) {
            return null;
        }
    }

    private static String withoutTrailingComma(String text) {
        return text.endsWith(",") ? text.substring(0, text.length() - 1) : text;
    }

    /** The zone a date was written in; UTC when it names none, or names one nobody reads. */
    private static ZoneOffset readZone(String zoneText) {
        String named = zoneText.toUpperCase(Locale.ROOT);
        Integer known = OBSOLETE_ZONE_SECONDS.get(named);
        if (known != null) {
            return ZoneOffset.ofTotalSeconds(known);
        }
        int written;
        try {
            written = Integer.parseInt(named);
        } catch (NumberFormatException notAnOffset) {
            return ZoneOffset.UTC;
        }
        int magnitude = Math.abs(written);
        int seconds = magnitude / 100 * 3600 + magnitude % 100 * 60;
        return ZoneOffset.ofTotalSeconds(written < 0 ? -seconds : seconds);
    }

    private static void requireFinite(String name, double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new SdkConfigError(name + ": expected a finite number of seconds, got " + value);
        }
    }
}
