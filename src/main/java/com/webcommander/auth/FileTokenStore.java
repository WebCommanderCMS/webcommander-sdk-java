package com.webcommander.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcommander.Redaction;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkConfigError;
import com.webcommander.errors.SdkError;
import com.webcommander.http.Transport;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/** JSON-file token storage, safe to share across processes: one cache, one authentication. */
public final class FileTokenStore implements TokenStore {

    public static final String DEFAULT_TOKEN_CACHE_FILENAME = "wc_java_token.json";

    public static final double DEFAULT_LOCK_WAIT_SECONDS = 300.0;

    private static final String LOCK_SUFFIX = ".lock";
    private static final long LOCK_POLL_MILLIS = 50;
    private static final String DEFAULT_NAMESPACE = "default";
    private static final int REPLACE_RETRY_ATTEMPTS = 5;
    /** Sidecar replacements tolerated before the environment is called uncoordinatable. */
    private static final int MAX_SIDECAR_REPLACEMENTS = 8;
    private static final long REPLACE_RETRY_INITIAL_MILLIS = 10;

    private static final ObjectMapper JSON = new ObjectMapper();

    /** One gate and one channel per sidecar path, for every store in this JVM. */
    private static final ConcurrentMap<String, SharedFileLock> LOCKS_BY_PATH =
            new ConcurrentHashMap<>();

    private final Path path;
    private final Path lockPath;
    private final String namespace;
    private final long lockWaitMillis;
    private final long maxCacheBytes;

    /** The default size ceiling of the cache file, in bytes. */
    public static final long DEFAULT_MAX_CACHE_BYTES =
            SdkConfig.DEFAULT_MAX_TOKEN_CACHE_BYTES;

    public FileTokenStore(String filePath) {
        this(filePath, null);
    }

    public FileTokenStore(String filePath, String namespace) {
        this(filePath, namespace, DEFAULT_LOCK_WAIT_SECONDS);
    }

    public FileTokenStore(String filePath, String namespace, double lockWaitSeconds) {
        this(filePath, namespace, lockWaitSeconds, DEFAULT_MAX_CACHE_BYTES);
    }

    /** A cache over {@code maxCacheBytes} reads as unreadable; a write refuses that state. */
    public FileTokenStore(String filePath, String namespace, double lockWaitSeconds,
            long maxCacheBytes) {
        if (!Double.isFinite(lockWaitSeconds) || lockWaitSeconds < 0) {
            throw new SdkConfigError("lockWaitSeconds: expected a finite number of seconds "
                    + ">= 0, got " + lockWaitSeconds);
        }
        if (maxCacheBytes < 1) {
            throw new SdkConfigError("maxCacheBytes: must be at least 1, got " + maxCacheBytes);
        }
        this.maxCacheBytes = maxCacheBytes;
        this.path = Path.of(filePath);
        this.lockPath = Path.of(filePath + LOCK_SUFFIX);
        this.namespace = namespace == null || namespace.isEmpty() ? DEFAULT_NAMESPACE : namespace;
        this.lockWaitMillis = Math.round(lockWaitSeconds * 1000.0);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException error) {
                throw new SdkConfigError("tokenCachePath: the directory for "
                        + Redaction.redactPath(path)
                        + " could not be created (" + Redaction.describeFailure(error) + "); pass a path whose "
                        + "parent exists, or use memoryOnlyTokenCache() to keep the token in "
                        + "memory", error);
            }
        }
    }

    /** The secret and the auth string go into the key, never into the file. */
    public static String buildCredentialsNamespace(
            String baseUrl, String clientId, String clientSecret,
            String authString, String grantType) {
        StringBuilder material = new StringBuilder();
        for (String value : new String[] {"v3", baseUrl, clientId, clientSecret, authString, grantType}) {
            String text = value == null ? "" : value;
            material.append(text.codePointCount(0, text.length())).append(':').append(text);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(material.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte octet : hash) {
                hex.append(String.format("%02x", octet));
            }
            return hex.substring(0, 16);
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 unavailable", error);   // JDK-mandated algorithm
        }
    }

    public static String buildDefaultTokenCachePath() {
        return buildDefaultTokenCachePath(DEFAULT_TOKEN_CACHE_FILENAME);
    }

    /** Per-user cache path under the platform cache directory, created owner-only where POSIX. */
    public static String buildDefaultTokenCachePath(String filename) {
        String osName = osName();
        String root;
        if (osName.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            root = localAppData != null && !localAppData.isEmpty()
                    ? localAppData : System.getProperty("user.home");
        } else if (osName.contains("mac")) {
            root = System.getProperty("user.home") + "/Library/Caches";
        } else {
            String xdgCacheHome = System.getenv("XDG_CACHE_HOME");
            root = xdgCacheHome != null && !xdgCacheHome.isEmpty()
                    ? xdgCacheHome : System.getProperty("user.home") + "/.cache";
        }
        if (!isAbsolute(root)) {
            root = System.getProperty("java.io.tmpdir");
        }
        Path directory = Path.of(root, "webcommander");
        try {
            Files.createDirectories(directory);
            restrictToOwner(directory);
        } catch (IOException error) {
            throw new SdkConfigError("tokenCachePath: the default cache directory could not "
                    + "be created (" + Redaction.describeFailure(error) + "); pass a path, or use "
                    + "memoryOnlyTokenCache() to keep the token in memory", error);
        }
        return directory.resolve(filename).toString();
    }

    private static String osName() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
    }

    private static boolean isAbsolute(String root) {
        if (root == null || root.isEmpty()) {
            return false;
        }
        try {
            return Path.of(root).isAbsolute();
        } catch (InvalidPathException notAPath) {
            return false;
        }
    }

    /** Best-effort owner-only permissions; a no-op where POSIX bits do not apply. */
    private static void restrictToOwner(Path target) {
        try {
            Files.setPosixFilePermissions(target,
                    Files.isDirectory(target)
                            ? PosixFilePermissions.fromString("rwx------")
                            : PosixFilePermissions.fromString("rw-------"));
        } catch (UnsupportedOperationException | IOException ignored) {
            // Windows ACLs govern here; the cache keeps what its directory granted.
        }
    }

    @Override
    public Map<String, Object> readTokenRecord() {
        SharedFileLock shared = shared();
        TokenLock.acquire(shared.gate, "the token cache lock");
        try {
            return readNamespaceRecord();
        } finally {
            shared.gate.unlock();
        }
    }

    @Override
    public void writeTokenRecord(Map<String, Object> record) {
        try (AutoCloseable ignored = acquireTokenRefreshLock()) {
            requireTheLockStillGoverns();
            writeNamespaceRecord(record);
        } catch (Exception error) {
            throw writeFailure(error);
        }
    }

    private void requireTheLockStillGoverns() {
        Object locked = shared().identity;
        if (locked == null) {
            return;
        }
        Object current = fileIdentity(lockPath);
        if (current != null && !locked.equals(current)) {
            throw new SdkConfigError("tokenCachePath: the token cache lock file "
                    + Redaction.redactPath(lockPath) + " was replaced while this process "
                    + "held the lock on it, so this entry was not written");
        }
    }

    private RuntimeException writeFailure(Exception error) {
        if (error instanceof RuntimeException runtime) {
            return runtime;
        }
        return new SdkConfigError("tokenCachePath: the token cache " + Redaction.redactPath(path)
                + " could not be written (" + Redaction.describeFailure(error)
                + "); pass a writable path, or use memoryOnlyTokenCache() to keep the "
                + "token in memory", error);
    }

    /** Drop this store's entry, leaving every other instance's token alone. */
    @Override
    public void clearTokenRecord() {
        try (AutoCloseable ignored = acquireTokenRefreshLock()) {
            clearNamespaceRecord();
        } catch (Exception error) {
            throw writeFailure(error);
        }
    }

    /** Absence is the outcome asked for; every other failure is a failed logout. */
    void deleteTheTokenFile() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException failure) {
            throw new SdkError("the token cache file " + Redaction.redactPath(path)
                    + " could not be deleted (" + Redaction.describeFailure(failure)
                    + "), so a reusable token may still be on disk", failure);
        }
    }

    /** Serialise refreshes across threads <b>and</b> processes, or refuse the call. */
    @Override
    public AutoCloseable acquireTokenRefreshLock() {
        SharedFileLock shared = shared();
        TokenLock.acquire(shared.gate, "the token refresh lock");
        if (shared.depth > 0) {
            shared.depth++;
            return handle(shared);
        }
        try {
            takeTheFileLock(shared);
        } catch (RuntimeException refusal) {
            shared.gate.unlock();
            throw refusal;
        }
        shared.depth = 1;
        return handle(shared);
    }

    /** One outstanding level of the reentrant lock, given back at most once. */
    private AutoCloseable handle(SharedFileLock shared) {
        return new AutoCloseable() {
            private boolean released;

            @Override
            public void close() {
                if (released) {
                    return;
                }
                released = true;
                leave(shared);
            }
        };
    }

    /** Give back one level, dropping the operating-system lock only on the transition to zero. */
    private void leave(SharedFileLock shared) {
        try {
            if (shared.depth > 1) {
                shared.depth--;
                return;
            }
            shared.depth = 0;
            FileLock lock = shared.lock;
            FileChannel channel = shared.channel;
            shared.lock = null;
            shared.channel = null;
            shared.identity = null;
            if (lock != null) {
                try {
                    lock.release();
                } catch (IOException ignored) {
                    // the channel close below frees it regardless
                }
            }
            if (channel != null) {
                closeQuietly(channel);
            }
        } finally {
            shared.gate.unlock();
        }
    }

    private void takeTheFileLock(SharedFileLock shared) {
        long startedAt = System.nanoTime();
        boolean recreatedTheParent = false;
        int replacements = 0;
        while (true) {
            FileChannel channel;
            try {
                // No TRUNCATE_EXISTING: it would rewrite a file another process holds a lock on.
                channel = FileChannel.open(lockPath,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (NoSuchFileException gone) {
                if (recreatedTheParent) {
                    throw unopenableSidecar(gone);
                }
                recreatedTheParent = true;
                String reason = remakeTheParentDirectory();
                if (reason == null) {
                    continue;
                }
                throw new SdkConfigError("tokenCachePath: the directory holding the token "
                        + "lock file " + Redaction.redactPath(lockPath)
                        + " is gone and could not be recreated ("
                        + reason + ")", gone);
            } catch (IOException error) {
                throw unopenableSidecar(error);
            }
            Object opened = fileIdentity(lockPath);
            FileLock lock;
            try {
                // One fixed byte, so the lock does not depend on the sidecar's length.
                lock = channel.tryLock(0L, 1L, false);
            } catch (OverlappingFileLockException alreadyHeldHere) {
                closeQuietly(channel);
                throw new SdkConfigError("tokenCachePath: this JVM already holds a file lock on "
                        + Redaction.redactPath(lockPath) + "; give this client its own "
                        + "tokenCachePath", alreadyHeldHere);
            } catch (IOException refused) {
                closeQuietly(channel);
                throw new SdkConfigError("tokenCachePath: the operating system refused to "
                        + "lock " + Redaction.redactPath(lockPath)
                        + " (" + Redaction.describeFailure(refused) + ")", refused);
            }
            if (lock != null) {
                Object locked = fileIdentity(lockPath);
                if (opened != null && !opened.equals(locked)) {
                    releaseQuietly(lock);
                    closeQuietly(channel);
                    if (++replacements > MAX_SIDECAR_REPLACEMENTS) {
                        throw new SdkConfigError("tokenCachePath: the token cache lock file "
                                + Redaction.redactPath(lockPath) + " was replaced "
                                + MAX_SIDECAR_REPLACEMENTS + " times while it was being locked");
                    }
                    continue;
                }
                shared.channel = channel;
                shared.lock = lock;
                shared.identity = locked != null ? locked : opened;
                return;
            }
            closeQuietly(channel);
            if ((System.nanoTime() - startedAt) / 1_000_000L >= lockWaitMillis) {
                throw new SdkError("the token refresh lock on "
                        + Redaction.redactPath(lockPath) + " was held "
                        + "elsewhere for longer than " + configuredWait());
            }
            try {
                Thread.sleep(LOCK_POLL_MILLIS);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
                throw new SdkError("cancelled while waiting for the lock file "
                        + Redaction.redactPath(lockPath), error);
            }
        }
    }

    private String configuredWait() {
        return lockWaitMillis >= 1000 ? lockWaitMillis / 1000 + "s" : lockWaitMillis + "ms";
    }

    private SdkConfigError unopenableSidecar(IOException error) {
        return new SdkConfigError("tokenCachePath: the token cache lock file "
                + Redaction.redactPath(lockPath)
                + " could not be opened (" + Redaction.describeFailure(error) + ")", error);
    }

    /** {@code null} when the directory is there afterwards, else the reason it is not. */
    private String remakeTheParentDirectory() {
        Path parent = lockPath.getParent();
        if (parent == null) {
            return null;
        }
        try {
            Files.createDirectories(parent);
            return null;
        } catch (IOException error) {
            return Redaction.describeFailure(error);
        }
    }

    private SharedFileLock shared() {
        return LOCKS_BY_PATH.computeIfAbsent(
                canonicalLockKey(lockPath), key -> new SharedFileLock());
    }

    /** The absolute, normalised path, lower-cased on Windows and macOS. */
    private static String canonicalLockKey(Path lockPath) {
        Path resolved = realTarget(lockPath);
        String absolute = resolved.toAbsolutePath().normalize().toString();
        String platform = osName();
        boolean foldsCase = platform.contains("win") || platform.contains("mac");
        return foldsCase ? absolute.toLowerCase(Locale.ROOT) : absolute;
    }

    /** Symbolic links followed, so two spellings of one file resolve to one key. */
    private static Path realTarget(Path lockPath) {
        Path absolute = lockPath.toAbsolutePath().normalize();
        Path parent = absolute.getParent();
        Path name = absolute.getFileName();
        if (parent == null || name == null) {
            return absolute;
        }
        try {
            return parent.toRealPath().resolve(name);
        } catch (IOException | RuntimeException unresolvable) {
            return absolute;
        }
    }

    /** The identity of the file at a pathname, or {@code null} when the platform supplies none. */
    private static Object fileIdentity(Path file) {
        try {
            return Files.readAttributes(file, BasicFileAttributes.class)
                    .fileKey();
        } catch (IOException | RuntimeException unknown) {
            return null;
        }
    }

    /** A cache present but unreadable is refused rather than read as absent. */
    private Map<String, Object> readNamespaceRecord() {
        CacheReading reading = readTheCache();
        if (reading.isUnusable()) {
            throw new SdkError("the token cache at " + Redaction.redactPath(path) + " is "
                    + reading.description() + "; clearTokenRecord() removes it",
                    reading.cause());
        }
        Map<String, Object> records = reading.records();
        if (!records.containsKey(namespace)) {
            return null;
        }
        Object entry = records.get(namespace);
        if (entry instanceof Map<?, ?> record) {
            Map<String, Object> copy = new LinkedHashMap<>();
            record.forEach((key, value) -> copy.put(String.valueOf(key), value));
            return copy;
        }
        throw new SdkError("the token cache at " + Redaction.redactPath(path)
                + " holds an entry for these credentials that is not a token record ("
                + (entry == null ? "null" : entry.getClass().getSimpleName())
                + "); clearTokenRecord() removes it");
    }

    /** Store this namespace's entry, keeping every sibling namespace intact. */
    private void writeNamespaceRecord(Map<String, Object> record) {
        CacheReading reading = readTheCache();
        if (reading.isUnusable()) {
            throw new SdkError("the token cache at " + Redaction.redactPath(path) + " is "
                    + reading.description() + ", so this entry was not written; "
                    + "clearTokenRecord() removes it", reading.cause());
        }
        Map<String, Object> stored = reading.records();
        stored.put(namespace, new LinkedHashMap<>(record));
        writeAllNamespaces(stored);
    }

    /** Discard this namespace's entry, and the file itself once it holds no other. */
    private void clearNamespaceRecord() {
        CacheReading reading = readTheCache();
        if (reading.isUnusable()) {
            deleteTheTokenFile();
            return;
        }
        Map<String, Object> stored = reading.records();
        if (stored.remove(namespace) == null) {
            return;
        }
        if (!stored.isEmpty()) {
            writeAllNamespaces(stored);
        } else {
            deleteTheTokenFile();
        }
    }

    private enum CacheState { ABSENT, EMPTY, RECORDS, CORRUPT, UNREADABLE }

    private record CacheReading(CacheState state, Map<String, Object> entries, Throwable cause) {

        /** Whether the file is there and its contents are unknown. */
        boolean isUnusable() {
            return state == CacheState.CORRUPT || state == CacheState.UNREADABLE;
        }

        String description() {
            return state == CacheState.CORRUPT
                    ? "present but not a JSON object" : "present but could not be read";
        }

        Map<String, Object> records() {
            return entries == null ? new LinkedHashMap<>() : new LinkedHashMap<>(entries);
        }
    }

    private CacheReading readTheCache() {
        String content;
        try {
            // Opened directly: Files.exists would read an unreadable cache as absent.
            byte[] bytes;
            try (InputStream stream = Files.newInputStream(path)) {
                bytes = Transport.readAtMost(stream, maxCacheBytes);
            }
            if (bytes.length > maxCacheBytes) {
                return new CacheReading(CacheState.UNREADABLE, null,
                        new IOException(Redaction.redactPath(path) + " is over the "
                                + maxCacheBytes
                                + "-byte token cache ceiling, so it was not buffered"));
            }
            content = decodeUtf8Strictly(bytes);
        } catch (NoSuchFileException absent) {
            return new CacheReading(CacheState.ABSENT, null, null);
        } catch (MalformedInputException | UnmappableCharacterException notText) {
            return new CacheReading(CacheState.CORRUPT, null, notText);
        } catch (IOException error) {
            return new CacheReading(CacheState.UNREADABLE, null, error);
        }
        if (content.strip().isEmpty()) {
            return new CacheReading(CacheState.EMPTY, null, null);
        }
        Object parsed;
        try {
            parsed = JSON.readValue(content, Object.class);
        } catch (IOException notJson) {
            return new CacheReading(CacheState.CORRUPT, null, notJson);
        }
        if (!(parsed instanceof Map<?, ?> mapping)) {
            return new CacheReading(CacheState.CORRUPT, null, null);
        }
        Map<String, Object> records = new LinkedHashMap<>();
        mapping.forEach((key, value) -> records.put(String.valueOf(key), value));
        return new CacheReading(CacheState.RECORDS, records, null);
    }

    /** Bytes that are not UTF-8 are refused rather than becoming a string of U+FFFD. */
    private static String decodeUtf8Strictly(byte[] bytes)
            throws CharacterCodingException {
        return StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes)).toString();
    }

    private void writeAllNamespaces(Map<String, Object> stored) {
        try {
            Path directory = path.getParent() == null ? Path.of(".") : path.getParent();
            Files.createDirectories(directory);
            Path temporary = Files.createTempFile(directory, path.getFileName().toString(), ".tmp");
            try {
                Files.writeString(temporary, JSON.writeValueAsString(stored), StandardCharsets.UTF_8);
                replaceTheTokenFile(temporary);
                restrictToOwner(path);
            } catch (Exception error) {
                try {
                    Files.deleteIfExists(temporary);
                } catch (IOException ignored) {
                }
                throw error;
            }
        } catch (Exception error) {
            throw writeFailure(error);
        }
    }

    /** Briefly retried on Windows: a reader makes the replace fail with a sharing violation. */
    private void replaceTheTokenFile(Path temporary) throws IOException {
        long wait = REPLACE_RETRY_INITIAL_MILLIS;
        for (int attempt = 0; ; attempt++) {
            try {
                try {
                    Files.move(temporary, path,
                            StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException error) {
                    Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
                }
                return;
            } catch (IOException error) {
                boolean windows = osName().contains("win");
                if (!windows || attempt == REPLACE_RETRY_ATTEMPTS - 1) {
                    throw error;
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw error;
                }
                wait *= 2;
            }
        }
    }

    private static void closeQuietly(FileChannel channel) {
        try {
            channel.close();
        } catch (IOException ignored) {
        }
    }

    private static void releaseQuietly(FileLock lock) {
        try {
            lock.release();
        } catch (IOException ignored) {
        }
    }

    /** One sidecar path's gate, channel and depth, shared by every store in this JVM. */
    private static final class SharedFileLock {

        /** Serialises acquisitions and plain reads within this JVM. */
        final ReentrantLock gate = new ReentrantLock();

        FileChannel channel;

        FileLock lock;

        Object identity;

        int depth;
    }
}
