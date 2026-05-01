package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public interface UninterruptibleFuture<V> extends Future<V> {
    @Override // java.util.concurrent.Future
    V get() throws ExecutionException;

    @Override // java.util.concurrent.Future
    V get(long j, TimeUnit timeUnit) throws ExecutionException, TimeoutException;
}
