package com.google.common.util.concurrent;

import java.lang.Exception;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public abstract class AbstractCheckedFuture<V, E extends Exception> implements CheckedFuture<V, E> {
    protected final ListenableFuture<V> delegate;

    protected abstract E mapException(Exception exc);

    protected AbstractCheckedFuture(ListenableFuture<V> listenableFuture) {
        this.delegate = listenableFuture;
    }

    @Override // com.google.common.util.concurrent.CheckedFuture
    public V checkedGet() throws Exception {
        try {
            return get();
        } catch (InterruptedException e) {
            cancel(true);
            throw mapException(e);
        } catch (CancellationException e2) {
            throw mapException(e2);
        } catch (ExecutionException e3) {
            throw mapException(e3);
        }
    }

    @Override // com.google.common.util.concurrent.CheckedFuture
    public V checkedGet(long j, TimeUnit timeUnit) throws Exception {
        try {
            return get(j, timeUnit);
        } catch (InterruptedException e) {
            cancel(true);
            throw mapException(e);
        } catch (CancellationException e2) {
            throw mapException(e2);
        } catch (ExecutionException e3) {
            throw mapException(e3);
        }
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        return this.delegate.cancel(z);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.delegate.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.delegate.isDone();
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException, InterruptedException {
        return this.delegate.get();
    }

    @Override // java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.delegate.get(j, timeUnit);
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable runnable, Executor executor) {
        this.delegate.addListener(runnable, executor);
    }
}
