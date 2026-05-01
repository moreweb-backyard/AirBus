package com.google.common.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/* loaded from: classes.dex */
public abstract class AbstractFuture<V> implements Future<V> {
    private final Sync<V> sync = new Sync<>();

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        return false;
    }

    protected void done() {
    }

    @Override // java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.sync.get(timeUnit.toNanos(j));
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException, InterruptedException {
        return this.sync.get();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.sync.isDone();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.sync.isCancelled();
    }

    protected boolean set(V v) {
        boolean z = this.sync.set(v);
        if (z) {
            done();
        }
        return z;
    }

    protected boolean setException(Throwable th) {
        boolean exception = this.sync.setException(th);
        if (exception) {
            done();
        }
        if (th instanceof Error) {
            throw ((Error) th);
        }
        return exception;
    }

    protected final boolean cancel() {
        boolean zCancel = this.sync.cancel();
        if (zCancel) {
            done();
        }
        return zCancel;
    }

    static final class Sync<V> extends AbstractQueuedSynchronizer {
        static final int CANCELLED = 4;
        static final int COMPLETED = 2;
        static final int COMPLETING = 1;
        static final int RUNNING = 0;
        private static final long serialVersionUID = 0;
        private ExecutionException exception;
        private V value;

        Sync() {
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected int tryAcquireShared(int i) {
            return isDone() ? 1 : -1;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean tryReleaseShared(int i) {
            setState(i);
            return true;
        }

        V get(long j) throws ExecutionException, CancellationException, InterruptedException, TimeoutException {
            if (!tryAcquireSharedNanos(-1, j)) {
                throw new TimeoutException("Timeout waiting for task.");
            }
            return getValue();
        }

        V get() throws ExecutionException, CancellationException, InterruptedException {
            acquireSharedInterruptibly(-1);
            return getValue();
        }

        private V getValue() throws ExecutionException, CancellationException {
            int state = getState();
            if (state == 2) {
                if (this.exception != null) {
                    throw this.exception;
                }
                return this.value;
            }
            if (state == 4) {
                throw new CancellationException("Task was cancelled.");
            }
            throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
        }

        boolean isDone() {
            return (getState() & 6) != 0;
        }

        boolean isCancelled() {
            return getState() == 4;
        }

        boolean set(V v) {
            return complete(v, null, 2);
        }

        boolean setException(Throwable th) {
            return complete(null, th, 2);
        }

        boolean cancel() {
            return complete(null, null, 4);
        }

        private boolean complete(V v, Throwable th, int i) {
            if (!compareAndSetState(0, 1)) {
                return false;
            }
            this.value = v;
            this.exception = th == null ? null : new ExecutionException(th);
            releaseShared(i);
            return true;
        }
    }
}
