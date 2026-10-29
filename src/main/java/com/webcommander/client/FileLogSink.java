package com.webcommander.client;

import com.webcommander.Redaction;
import com.webcommander.errors.SdkConfigError;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** The file behind {@code SdkConfig.logFile}. */
final class FileLogSink {

    static final Map<String, Integer> LEVEL_RANKS = Map.of(
            "DEBUG", 10, "INFO", 20, "WARNING", 30, "ERROR", 40, "CRITICAL", 50);

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    /** Every open sink, keyed by logger name. */
    private static final Map<String, Shared> OPEN_SINKS = new HashMap<>();
    private static final Object LOCK = new Object();

    private static final class Shared {
        final String absolutePath;
        final Writer writer;
        final int levelRank;
        int users = 1;

        Shared(String absolutePath, Writer writer, int levelRank) {
            this.absolutePath = absolutePath;
            this.writer = writer;
            this.levelRank = levelRank;
        }
    }

    private final String loggerName;
    private Shared shared;

    private FileLogSink(String loggerName, Shared shared) {
        this.loggerName = loggerName;
        this.shared = shared;
    }

    /** Open or share the sink for one client; {@code null} when the config names no file. */
    static FileLogSink attach(String loggerName, String logFile, String logLevel) {
        if (logFile == null || logFile.isBlank()) {
            return null;
        }
        // Absolute, so one file spelled two ways cannot open two sinks and log every record twice.
        String target;
        try {
            target = Path.of(logFile).toAbsolutePath().normalize().toString();
        } catch (InvalidPathException error) {
            throw new SdkConfigError("logFile " + Redaction
                    .redactPath(logFile) + " cannot be opened: "
                    + Redaction.describeFailure(error));
        }
        synchronized (LOCK) {
            Shared existing = OPEN_SINKS.get(loggerName);
            if (existing != null) {
                if (!existing.absolutePath.equals(target)) {
                    throw new SdkConfigError("logFile: client '" + loggerName + "' already "
                            + "logs to " + Redaction
                                    .redactPath(existing.absolutePath) + ", so adding "
                            + Redaction.redactPath(target)
                            + " would write every record from both clients into both files — "
                            + "give this client its own clientName, or close the first one first");
                }
                existing.users++;
                return new FileLogSink(loggerName, existing);
            }
            Writer writer;
            try {
                writer = Files.newBufferedWriter(Path.of(target), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND,
                        StandardOpenOption.WRITE);
            } catch (IOException | UnsupportedOperationException error) {
                throw new SdkConfigError("logFile " + Redaction
                        .redactPath(logFile) + " cannot be opened: "
                        + Redaction.describeFailure(error), error);
            }
            Shared opened = new Shared(target, writer,
                    LEVEL_RANKS.getOrDefault(logLevel, 30));
            OPEN_SINKS.put(loggerName, opened);
            return new FileLogSink(loggerName, opened);
        }
    }

    /** Append one record when it clears the opener's level; never throws. */
    void write(String level, String message) {
        Shared sink = shared;
        if (sink == null
                || LEVEL_RANKS.getOrDefault(level.toUpperCase(Locale.ROOT), 0) < sink.levelRank) {
            return;
        }
        String line = TIMESTAMP.format(LocalDateTime.now()) + " "
                + level.toUpperCase(Locale.ROOT) + " " + loggerName + " " + message
                + System.lineSeparator();
        synchronized (sink) {
            try {
                sink.writer.write(line);
                sink.writer.flush();
            } catch (IOException ignored) {
                // A full disk must not fail the API call the record describes.
            }
        }
    }

    /** Release this client's claim; the last user out closes the file. */
    void detach() {
        synchronized (LOCK) {
            Shared sink = shared;
            if (sink == null) {
                return;     // already detached; a double close() must stay safe
            }
            shared = null;
            sink.users--;
            if (sink.users > 0) {
                return;
            }
            OPEN_SINKS.remove(loggerName, sink);
            try {
                sink.writer.close();
            } catch (IOException ignored) {
                // Nothing left to do with a writer that will not close.
            }
        }
    }
}
