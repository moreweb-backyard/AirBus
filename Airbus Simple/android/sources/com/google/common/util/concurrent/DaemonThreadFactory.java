package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes.dex */
public class DaemonThreadFactory implements ThreadFactory {
    private final ThreadFactory factory;

    public DaemonThreadFactory(ThreadFactory threadFactory) {
        Preconditions.checkNotNull(threadFactory);
        this.factory = threadFactory;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        Thread threadNewThread = this.factory.newThread(runnable);
        threadNewThread.setDaemon(true);
        return threadNewThread;
    }
}
