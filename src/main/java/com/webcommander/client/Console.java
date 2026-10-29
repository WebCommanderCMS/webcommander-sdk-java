package com.webcommander.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.webcommander.config.SdkConfig;
import com.webcommander.Redaction;
import com.webcommander.http.Transport;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/** Renders one exchange for a terminal; everything shown passes through the redactor first. */
public final class Console {

    private static final ObjectMapper PRETTY = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setDefaultPrettyPrinter(new SdkPrettyPrinter());
    private static final ObjectMapper COMPACT = new ObjectMapper();

    /** Stands in where a status code would go for a request that never reached one. */
    static final String NO_RESPONSE_STATUS = "no response";

    private static final String MULTIPART_PREFIX = "<multipart: ";
    private static final String PAYLOAD_FILES_KEY = "files";
    private static final String PAYLOAD_FORM_KEY = "form";

    /** Built from the code point, so no literal control byte sits in this source. */
    private static final char ESC = 0x1b;
    private static final String ANSI_ESCAPE = String.valueOf(ESC);
    private static final String ANSI_ESCAPE_START = ESC + "[";
    private static final String ANSI_RESET = ESC + "[0m";
    private static final String ANSI_DIM = ESC + "[2m";

    private Console() {}

    /** The text to print for one exchange, told whether it carried files or went form-encoded. */
    public static String renderExchange(ApiResponse response, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, SdkConfig config) {
        return render(response.getRequestInfo(), String.valueOf(response.getStatusCode()),
                response.succeeded(), response.getMap(), response.getHeaders(),
                reportedBody, files, formEncoded, config);
    }

    /** The text to print for a send that produced no response: a connection failure or timeout. */
    static String renderFailedExchange(RequestInfo info, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, SdkConfig config) {
        return render(info, NO_RESPONSE_STATUS, false, null, Map.of(),
                reportedBody, files, formEncoded, config);
    }

    private static String render(RequestInfo info, String statusCode, boolean succeeded,
            Object data, Map<String, String> responseHeaders, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, SdkConfig config) {
        Object payload = summarisePayload(reportedBody, files);

        if (SdkConfig.PRINT_FORMAT_COMPACT.equals(config.getPrintFormat())) {
            StringBuilder line = new StringBuilder("wc | ")
                    .append(info.getClientName()).append(" | ")
                    .append(info.getHttpMethod()).append(" | ")
                    .append(info.getUrl()).append(" | ")
                    .append(statusCode).append(" | ")
                    .append(info.getAttemptCount()).append('/').append(info.getMaxAttempts())
                    .append(" | ").append(info.getElapsedSeconds()).append('s');
            if (config.isPrintRequestPayload()) {
                line.append(" | payload=")
                        .append(renderJsonValue(Redaction.redactValue(payload), true));
            }
            if (config.isPrintResponseBody()) {
                line.append(" | response=")
                        .append(renderJsonValue(Redaction.redactValue(data), true));
            }
            return truncate(neutralise(line.toString()), config.getPrintMaxChars());
        }

        Colour colour = new Colour(config.isPrintColor() && stdoutSupportsColour());

        StringBuilder out = new StringBuilder(colour.dim("WebCommander  "
                + info.getHttpMethod() + "  " + statusCode + "  " + info.getElapsedSeconds() + "s"));
        if (config.isPrintApiInfo()) {
            row(out, "client", info.getClientName());
            row(out, "api version", info.getApiVersion());
            row(out, "method", info.getHttpMethod());
            row(out, "endpoint", info.getUrl());
            row(out, "params", renderJsonValue(info.getQueryParams(), true));
            out.append('\n').append(String.format("%-12s: ", "status"))
                    .append(colour.status(statusCode, succeeded));
            row(out, "attempt", info.getAttemptCount() + " / " + info.getMaxAttempts());
            row(out, "elapsed", info.getElapsedSeconds() + "s");
        }
        if (config.isPrintHeaders()) {
            section(out, "request headers", colour);
            headerRows(out, info.getHeaders());
            section(out, "response headers", colour);
            headerRows(out, Redaction.redactMapping(responseHeaders));
        }
        if (config.isPrintRequestPayload() && payload != null) {
            section(out, "payload", colour);
            block(out, renderJsonValue(Redaction.redactValue(payload), false));
        }
        if (config.isPrintResponseBody()) {
            section(out, "response", colour);
            block(out, renderJsonValue(Redaction.redactValue(data), false));
        }
        if (config.isPrintCurl()) {
            section(out, "curl (POSIX shell)", colour);
            block(out, buildCurlCommand(info, reportedBody, files, formEncoded, config));
        }
        return truncate(out.toString(), config.getPrintMaxChars());
    }

    /** Describe what was sent without reading a byte of any upload. */
    public static Object summarisePayload(Object body, Map<String, Object> files) {
        List<String[]> entries = multipartEntries(files);
        if (entries.isEmpty()) {
            return body;
        }
        List<String> names = new ArrayList<>();
        for (String[] entry : entries) {
            names.add(entry[0] + "=" + entry[1]);
        }
        String summary = MULTIPART_PREFIX + String.join(", ", names) + ">";
        if (body == null) {
            return summary;
        }
        Map<String, Object> both = new LinkedHashMap<>();
        both.put(PAYLOAD_FILES_KEY, summary);
        both.put(PAYLOAD_FORM_KEY, body);
        return both;
    }

    /** A copy-pasteable, shell-quoted curl from the request record, every secret redacted. */
    public static String buildCurlCommand(RequestInfo info, Object reportedBody,
            Map<String, Object> files, boolean formEncoded, SdkConfig config) {
        StringBuilder curl = new StringBuilder("curl -X ")
                .append(shellQuote(neutralise(info.getHttpMethod())))
                .append(' ').append(shellQuote(neutralise(withQuery(info))));
        if (!config.isVerifySsl()) {
            curl.append(" \\\n  -k");
        }
        info.getHeaders().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> curl.append(" \\\n  -H ")
                        .append(shellQuote(neutralise(entry.getKey() + ": " + entry.getValue()))));

        List<String[]> entries = multipartEntries(files);
        for (String[] entry : entries) {
            curl.append(" \\\n  -F ")
                    .append(shellQuote(neutralise(entry[0]) + "=@" + neutralise(entry[1])));
        }
        if (!entries.isEmpty()) {
            for (Map.Entry<String, Object> field : formFields(reportedBody)) {
                curl.append(" \\\n  -F ").append(shellQuote(
                        neutralise(field.getKey()) + "=" + neutralise(String.valueOf(field.getValue()))));
            }
        } else if (reportedBody != null) {
            List<Map.Entry<String, Object>> fields =
                    formEncoded ? formFields(reportedBody) : List.of();
            if (!fields.isEmpty()) {
                for (Map.Entry<String, Object> field : fields) {
                    curl.append(" \\\n  --data-urlencode ").append(shellQuote(
                            neutralise(field.getKey()) + "="
                                    + neutralise(String.valueOf(field.getValue()))));
                }
            } else {
                if (!formEncoded && Transport.findContentType(info.getHeaders()) == null) {
                    curl.append(" \\\n  -H ").append(shellQuote("Content-Type: application/json"));
                }
                curl.append(" \\\n  --data-raw ")
                        .append(shellQuote(curlBody(Redaction.redactValue(reportedBody))));
            }
        }
        return curl.toString();
    }

    private static String curlBody(Object shown) {
        if (shown instanceof CharSequence text && isJsonDocument(text.toString())) {
            return neutralise(collapseJsonDocument(text.toString()));
        }
        return renderJsonValue(shown, true);
    }

    /** The same JSON on one line, every token copied as written; see {@link CurlOneLinePrinter}. */
    private static String collapseJsonDocument(String document) {
        StringWriter collapsed = new StringWriter(document.length());
        try (JsonParser parser = COMPACT.getFactory().createParser(document);
                JsonGenerator generator =
                        COMPACT.getFactory().createGenerator(collapsed)) {
            generator.setPrettyPrinter(new CurlOneLinePrinter());
            while (parser.nextToken() != null) {
                if (parser.currentToken().isNumeric()) {
                    // As written: copyCurrentEvent would print 1.50 as 1.5.
                    generator.writeNumber(parser.getText());
                } else {
                    generator.copyCurrentEvent(parser);
                }
            }
        } catch (IOException notCollapsible) {
            return singleLine(document);
        }
        return collapsed.toString();
    }

    /** Asked of the transport, so the curl line reproduces the field and file names really sent. */
    private static List<String[]> multipartEntries(Map<String, Object> files) {
        List<String[]> entries = new ArrayList<>();
        if (files == null) {
            return entries;
        }
        for (Map.Entry<String, Object> entry : files.entrySet()) {
            entries.add(new String[] {entry.getKey(),
                    Transport.multipartFilename(entry.getKey(), entry.getValue())});
        }
        return entries;
    }

    /** Redacted name/value pairs for a form body; anything else has no fields. */
    private static List<Map.Entry<String, Object>> formFields(Object body) {
        List<Map.Entry<String, Object>> fields = new ArrayList<>();
        if (!(body instanceof Map<?, ?>)) {
            return fields;
        }
        if (Redaction.redactValue(body) instanceof Map<?, ?> redacted) {
            for (Map.Entry<?, ?> entry : redacted.entrySet()) {
                fields.add(Map.entry(String.valueOf(entry.getKey()),
                        entry.getValue() == null ? "" : entry.getValue()));
            }
        }
        return fields;
    }

    /** The URL for the curl line, with the query parameters appended. */
    private static String withQuery(RequestInfo info) {
        if (info.getQueryParams().isEmpty()) {
            return info.getUrl();
        }
        StringBuilder query = new StringBuilder(info.getUrl());
        char separator = info.getUrl().contains("?") ? '&' : '?';
        for (Map.Entry<String, Object> entry : info.getQueryParams().entrySet()) {
            for (Object value : entry.getValue() instanceof List<?> list
                    ? list : List.of(entry.getValue())) {
                query.append(separator)
                        .append(formEncode(entry.getKey()))
                        .append('=')
                        .append(formEncode(
                                Redaction.redactText(String.valueOf(value))));
                separator = '&';
            }
        }
        return query.toString();
    }

    private static String formEncode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8)
                .replace("%7E", "~");
    }

    /** One {@code label:value} row, its value neutralised so it cannot forge a second row. */
    private static void row(StringBuilder out, String label, Object value) {
        out.append('\n').append(String.format("%-12s: ", label))
                .append(neutralise(String.valueOf(value)));
    }

    /** Header rows sorted by name, so two runs of one call print the same block. */
    private static void headerRows(StringBuilder out, Map<String, String> headers) {
        headers.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> headerRow(out, entry.getKey(), entry.getValue()));
    }

    private static void headerRow(StringBuilder out, String name, Object value) {
        out.append('\n').append(String.format("%-14s: ", neutralise(name)))
                .append(neutralise(String.valueOf(value)));
    }

    /** A plain {@code -- label --} rule, not a box tee, so the blocks below stay copy-pasteable. */
    private static void section(StringBuilder out, String label, Colour colour) {
        String rule = boxGlyphs().rule;
        String prefix = rule + rule + " " + label + " ";
        out.append('\n').append(colour.dim(
                prefix + rule.repeat(Math.max(4, 62 - prefix.length()))));
    }

    private record BoxGlyphs(String rule, String ellipsis) { }

    private static final BoxGlyphs UNICODE_GLYPHS = new BoxGlyphs("─", "…");
    private static final BoxGlyphs ASCII_GLYPHS = new BoxGlyphs("-", "...");

    /** Every glyph the probe below must be able to encode. */
    private static final String PROBE_GLYPHS = "─…";

    /** Box glyphs this console can print; a cp1252 console turns {@code ─} into {@code ?}. */
    private static BoxGlyphs boxGlyphs() {
        return stdoutCharset().newEncoder().canEncode(PROBE_GLYPHS)
                ? UNICODE_GLYPHS : ASCII_GLYPHS;
    }

    private static Charset stdoutCharset() {
        if (OUT_CHARSET != null) {
            try {
                return (Charset) OUT_CHARSET.invoke(System.out);
            } catch (ReflectiveOperationException useThePropertiesBelow) {
            }
        }
        for (String property : new String[] {"sun.stdout.encoding", "stdout.encoding"}) {
            Charset named = charsetNamed(System.getProperty(property));
            if (named != null) {
                return named;
            }
        }
        return Charset.defaultCharset();
    }

    /** {@code java.io.PrintStream.charset()}, where the runtime has it (JDK 18+). */
    private static final Method OUT_CHARSET = findOutCharset();

    private static Method findOutCharset() {
        try {
            return PrintStream.class.getMethod("charset");
        } catch (NoSuchMethodException jdk17) {
            return null;
        }
    }

    private static Charset charsetNamed(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return Charset.forName(name);
        } catch (IllegalArgumentException unknownOrUnusable) {
            return null;
        }
    }

    /** A raw payload, response or curl block, with no gutter so it copies cleanly. */
    private static void block(StringBuilder out, String text) {
        for (String line : text.split("\n", -1)) {
            out.append('\n').append(line);
        }
    }

    /** Render a value as JSON, then neutralise it: Jackson escapes C0 controls but not C1. */
    public static String renderJsonValue(Object value, boolean compact) {
        if (value == null) {
            return "<empty>";
        }
        if (value instanceof byte[] bytes) {
            return "<" + bytes.length + " bytes>";
        }
        if (value instanceof CharSequence text) {
            String raw = text.toString();
            if (!compact) {
                return neutralise(raw);
            }
            if (isJsonDocument(raw)) {
                return neutralise(singleLine(raw));
            }
            try {
                String quoted = COMPACT.writeValueAsString(raw);
                return neutralise(quoted.substring(1, quoted.length() - 1));
            } catch (IOException error) {
                return neutralise(raw);
            }
        }
        try {
            return neutralise((compact ? COMPACT : PRETTY).writeValueAsString(value));
        } catch (IOException error) {
            return neutralise(String.valueOf(value));
        }
    }

    /** Whether the whole text is one JSON object or array. */
    private static boolean isJsonDocument(String text) {
        String leading = text.stripLeading();
        if (!leading.startsWith("{") && !leading.startsWith("[")) {
            return false;
        }
        try (JsonParser parser = COMPACT.createParser(text)) {
            JsonToken opening = parser.nextToken();
            if (opening != JsonToken.START_OBJECT
                    && opening != JsonToken.START_ARRAY) {
                return false;
            }
            parser.skipChildren();
            return parser.nextToken() == null;
        } catch (IOException notOneDocument) {
            return false;
        }
    }

    /** The same text on one line: CR, LF and CRLF each become a single space. */
    private static String singleLine(String text) {
        return text.replace("\r\n", " ").replace('\r', ' ').replace('\n', ' ');
    }

    /** Characters a POSIX shell needs no quoting for. */
    private static final Pattern SHELL_UNSAFE = Pattern.compile("[^A-Za-z0-9_@%+=:,./-]");

    /** POSIX shell quoting for one curl fragment, applied only when the fragment needs it. */
    static String shellQuote(String fragment) {
        if (fragment.isEmpty()) {
            return "''";
        }
        if (!SHELL_UNSAFE.matcher(fragment).find()) {
            return fragment;
        }
        return "'" + fragment.replace("'", "'\"'\"'") + "'";
    }

    /** C0 (newline excepted), DEL, C1, U+2028 and U+2029 never reach a terminal as themselves. */
    static String neutralise(String text) {
        StringBuilder out = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if ((character <= 0x1f && character != '\n') || (character >= 0x7f && character <= 0x9f)
                    || character == '\u2028' || character == '\u2029') {
                out.append(String.format("\\u%04x", (int) character));
            } else {
                out.append(character);
            }
        }
        return out.toString();
    }

    private static boolean stdoutSupportsColour() {
        String noColour = System.getenv("NO_COLOR");
        if (noColour != null && !noColour.isEmpty()) {
            return false;
        }
        if ("dumb".equals(System.getenv("TERM"))) {
            return false;
        }
        java.io.Console console = System.console();
        if (console == null) {
            return false;
        }
        if (IS_TERMINAL != null) {
            try {
                return (Boolean) IS_TERMINAL.invoke(console);
            } catch (ReflectiveOperationException useTheNullCheck) {
                return true;
            }
        }
        return true;
    }

    /** {@code java.io.Console.isTerminal()}, where the runtime has it (JDK 22+). */
    private static final Method IS_TERMINAL = findIsTerminal();

    private static Method findIsTerminal() {
        try {
            return java.io.Console.class.getMethod("isTerminal");
        } catch (NoSuchMethodException jdk21OrOlder) {
            return null;
        }
    }

    /** The SDK's indented-JSON shape: LF breaks, arrays indented, empty containers tight. */
    private static final class SdkPrettyPrinter
            extends DefaultPrettyPrinter {

        private static final DefaultIndenter TWO_SPACES_LF = new DefaultIndenter("  ", "\n");

        SdkPrettyPrinter() {
            _objectIndenter = TWO_SPACES_LF;
            _arrayIndenter = TWO_SPACES_LF;
        }

        // Without this the base class hands back a DefaultPrettyPrinter and the shape is lost.
        @Override
        public DefaultPrettyPrinter createInstance() {
            return new SdkPrettyPrinter();
        }

        @Override
        public void writeObjectFieldValueSeparator(
                JsonGenerator generator) throws IOException {
            generator.writeRaw(": ");
        }

        @Override
        public void writeEndObject(JsonGenerator generator,
                int entryCount) throws IOException {
            if (entryCount > 0) {
                super.writeEndObject(generator, entryCount);
                return;
            }
            // The nesting counter must still unwind, or an empty object's siblings indent too deep.
            if (!_objectIndenter.isInline()) {
                --_nesting;
            }
            generator.writeRaw('}');
        }

        @Override
        public void writeEndArray(JsonGenerator generator,
                int valueCount) throws IOException {
            if (valueCount > 0) {
                super.writeEndArray(generator, valueCount);
                return;
            }
            if (!_arrayIndenter.isInline()) {
                --_nesting;
            }
            generator.writeRaw(']');
        }
    }

    /** The block shape without line breaks or indentation, for the {@code --data-raw} argument. */
    private static final class CurlOneLinePrinter
            extends DefaultPrettyPrinter {

        CurlOneLinePrinter() {
            _objectIndenter = NopIndenter.instance;
            _arrayIndenter = NopIndenter.instance;
        }

        @Override
        public DefaultPrettyPrinter createInstance() {
            return new CurlOneLinePrinter();
        }

        @Override
        public void writeObjectFieldValueSeparator(
                JsonGenerator generator) throws IOException {
            generator.writeRaw(": ");
        }

        @Override
        public void writeObjectEntrySeparator(
                JsonGenerator generator) throws IOException {
            generator.writeRaw(", ");
        }

        @Override
        public void writeArrayValueSeparator(
                JsonGenerator generator) throws IOException {
            generator.writeRaw(", ");
        }

        @Override
        public void writeEndObject(JsonGenerator generator,
                int entryCount) throws IOException {
            generator.writeRaw('}');
        }

        @Override
        public void writeEndArray(JsonGenerator generator,
                int valueCount) throws IOException {
            generator.writeRaw(']');
        }
    }

    /** ANSI helpers that no-op when colour is off or stdout is not a terminal. */
    private static final class Colour {
        private final boolean enabled;

        Colour(boolean enabled) {
            this.enabled = enabled;
        }

        String dim(String text) {
            return enabled ? ANSI_DIM + text + ANSI_RESET : text;
        }

        String status(String text, boolean succeeded) {
            return enabled ? ANSI_ESCAPE_START + (succeeded ? "32" : "31") + "m" + text + ANSI_RESET
                    : text;
        }
    }

    /** Cut to {@code maxChars} with the notice included: its room comes out of the budget. */
    private static String truncate(String text, int maxChars) {
        if (maxChars <= 0 || text.length() <= maxChars) {
            return text;
        }
        String ellipsis = boxGlyphs().ellipsis;
        int reserved = (ellipsis + " +" + text.length() + " chars").length();
        if (text.indexOf(ANSI_ESCAPE) >= 0) {
            reserved += ANSI_RESET.length();
        }
        int cut = Math.max(0, maxChars - reserved);
        if (cut >= text.length()) {
            return text;
        }
        return closeColourRun(text.substring(0, cut))
                + ellipsis + " +" + (text.length() - cut) + " chars";
    }

    /** Repair an ANSI sequence a cut landed inside, so the terminal is never left dim. */
    private static String closeColourRun(String text) {
        int marker = text.lastIndexOf(ANSI_ESCAPE);
        if (marker < 0) {
            return text;
        }
        String cut = text.indexOf('m', marker) < 0 ? text.substring(0, marker) : text;
        int last = cut.lastIndexOf(ANSI_ESCAPE_START);
        if (last >= 0 && !cut.startsWith(ANSI_RESET, last)) {
            return cut + ANSI_RESET;
        }
        return cut;
    }
}
