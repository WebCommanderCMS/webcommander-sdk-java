package com.webcommander.auth;

import com.webcommander.errors.SdkError;

import java.util.concurrent.locks.ReentrantLock;

/** Taking a token lock so that cancellation is honoured while waiting for one. */
final class TokenLock {

    private TokenLock() {}

    /** Take {@code lock}, or refuse because this thread was cancelled. */
    static void acquire(ReentrantLock lock, String waitingFor) {
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException interruption) {
            Thread.currentThread().interrupt();
            throw new SdkError("cancelled while waiting for " + waitingFor, interruption);
        }
    }
}
