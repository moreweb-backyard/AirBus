package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class AbstractListenableFuture<V> extends AbstractFuture<V> implements ListenableFuture<V> {
    private final ExecutionList executionList = new ExecutionList();

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable runnable, Executor executor) {
        this.executionList.add(runnable, executor);
    }

    @Override // com.google.common.util.concurrent.AbstractFuture
    protected void done() {
        this.executionList.run();
    }
}
