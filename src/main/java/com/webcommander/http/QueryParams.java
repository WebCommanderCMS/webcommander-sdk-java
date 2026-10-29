package com.webcommander.http;

import com.webcommander.errors.SdkUsageError;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Query parameter normalisation. */
public final class QueryParams {

    private QueryParams() {}

    /** Sort direction for the {@code dir} query parameter. */
    public enum SortDirection {
        /** Lowest value first, sent as {@code asc}. */
        ASC("asc"),
        /** Highest value first, sent as {@code desc}. */
        DESC("desc");

        private final String value;

        SortDirection(String value) {
            this.value = value;
        }

        /** The spelling the server accepts: {@code "asc"} or {@code "desc"}. */
        public String value() {
            return value;
        }

        /** The wire spelling, so a direction anywhere renders {@code desc}, not {@code DESC}. */
        @Override
        public String toString() {
            return value;
        }
    }

    /** Normalise query values for the wire; a {@code null} value drops that parameter. */
    public static Map<String, Object> buildQueryParams(Map<String, ?> params) {
        Map<String, Object> normalised = new LinkedHashMap<>();
        if (params == null) {
            return normalised;
        }
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            normalised.put(entry.getKey(), normalise(value, entry.getKey(), 0));
        }
        return normalised;
    }

    /** Date and time to whole seconds, with any sub-second part appended. */
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .toFormatter(Locale.ROOT);

    /** The offset always {@code +HH:MM}, UTC included, so one store pattern reads every value. */
    private static final DateTimeFormatter ISO_OFFSET_DATE_TIME = new DateTimeFormatterBuilder()
            .append(ISO_LOCAL_DATE_TIME)
            .appendOffset("+HH:MM", "+00:00")
            .toFormatter(Locale.ROOT);

    private static Object normalise(Object value, String key, int depth) {
        if (value instanceof Map<?, ?>) {
            throw new SdkUsageError("query param '" + key + "': nested objects cannot "
                    + "be encoded in a query string; flatten it or send it in the body");
        }
        if (value instanceof Boolean flag) {
            return flag ? "true" : "false";
        }
        if (value instanceof Enum<?> constant) {
            return constant.toString();
        }
        if (value instanceof LocalDate date) {
            return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        }
        if (value instanceof LocalDateTime dateTime) {
            return ISO_LOCAL_DATE_TIME.format(dateTime);
        }
        if (value instanceof ZonedDateTime zoned) {
            return ISO_OFFSET_DATE_TIME.format(zoned.toOffsetDateTime());
        }
        if (value instanceof OffsetDateTime offsetDateTime) {
            return ISO_OFFSET_DATE_TIME.format(offsetDateTime);
        }
        if (value instanceof Instant instant) {
            return ISO_OFFSET_DATE_TIME.format(instant.atOffset(ZoneOffset.UTC));
        }
        if (value instanceof BigDecimal decimal) {
            return decimal.toPlainString();
        }
        if (value instanceof CharSequence || value instanceof byte[]) {
            return value;
        }
        if (value instanceof Iterable<?> iterable) {
            if (depth >= 1) {
                throw new SdkUsageError("query param '" + key + "': a sequence inside a "
                        + "sequence has no query-string encoding; flatten it to one list of scalars");
            }
            List<Object> entries = new ArrayList<>();
            for (Object entry : iterable) {
                if (entry != null) {
                    entries.add(normalise(entry, key, depth + 1));
                }
            }
            return entries;
        }
        return value;
    }
}
