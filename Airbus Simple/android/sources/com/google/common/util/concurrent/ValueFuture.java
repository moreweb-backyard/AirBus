package com.google.common.util.concurrent;

/* loaded from: classes.dex */
public class ValueFuture<V> extends AbstractListenableFuture<V> {
    public static <T> ValueFuture<T> create() {
        return new ValueFuture<>();
    }

    private ValueFuture() {
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean set(V v) {
        return super.set(v);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    public boolean setException(Throwable th) {
        return super.setException(th);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
    public boolean cancel(boolean z) {
        return super.cancel();
    }
}
