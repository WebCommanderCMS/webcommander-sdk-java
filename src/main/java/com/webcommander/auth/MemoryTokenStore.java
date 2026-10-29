package com.webcommander.auth;

import com.webcommander.Redaction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/** In-process token storage, isolated per instance: two stores never share a token. */
public final class MemoryTokenStore implements TokenStore {

    private final ReentrantLock lock = new ReentrantLock();
    private Map<String, Object> stored;

    @Override
    public Map<String, Object> readTokenRecord() {
        TokenLock.acquire(lock, "the token cache lock");
        try {
            return stored == null ? null : new LinkedHashMap<>(Redaction.snapshotMapping(stored));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void writeTokenRecord(Map<String, Object> record) {
        TokenLock.acquire(lock, "the token cache lock");
        try {
            stored = Redaction.snapshotMapping(record);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clearTokenRecord() {
        TokenLock.acquire(lock, "the token cache lock");
        try {
            stored = null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AutoCloseable acquireTokenRefreshLock() {
        TokenLock.acquire(lock, "the token refresh lock");
        return lock::unlock;
    }
}
