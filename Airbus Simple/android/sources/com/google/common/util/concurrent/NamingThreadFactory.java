package com.google.common.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class NamingThreadFactory implements ThreadFactory {
    public static final ThreadFactory DEFAULT_FACTORY = java.util.concurrent.Executors.defaultThreadFactory();
    private final ThreadFactory backingFactory;
    private final AtomicInteger count;
    private final String format;

    public NamingThreadFactory(String str) {
        this(str, DEFAULT_FACTORY);
    }

    public NamingThreadFactory(String str, ThreadFactory threadFactory) {
        this.count = new AtomicInteger(0);
        this.format = str;
        this.backingFactory = threadFactory;
        makeName(0);
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        Thread threadNewThread = this.backingFactory.newThread(runnable);
        threadNewThread.setName(makeName(this.count.getAndIncrement()));
        return threadNewThread;
    }

    private String makeName(int i) {
        return String.format(this.format, Integer.valueOf(i));
    }
}
