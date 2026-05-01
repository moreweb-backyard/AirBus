package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class ExecutionList implements Runnable {
    private static final Logger LOG = Logger.getLogger(ExecutionList.class.getName());
    private final List<RunnableExecutorPair> runnables = Lists.newArrayList();
    private boolean executed = false;

    public void add(Runnable runnable, Executor executor) {
        boolean z;
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        synchronized (this.runnables) {
            if (this.executed) {
                z = true;
            } else {
                this.runnables.add(new RunnableExecutorPair(runnable, executor));
                z = false;
            }
        }
        if (z) {
            executor.execute(runnable);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.runnables) {
            this.executed = true;
        }
        Iterator<RunnableExecutorPair> it = this.runnables.iterator();
        while (it.hasNext()) {
            it.next().execute();
        }
    }

    private static class RunnableExecutorPair {
        final Executor executor;
        final Runnable runnable;

        RunnableExecutorPair(Runnable runnable, Executor executor) {
            this.runnable = runnable;
            this.executor = executor;
        }

        void execute() {
            try {
                this.executor.execute(this.runnable);
            } catch (RuntimeException e) {
                ExecutionList.LOG.log(Level.SEVERE, "RuntimeException while executing runnable " + this.runnable + " with executor " + this.executor, (Throwable) e);
            }
        }
    }
}
