package com.google.common.util.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class Executors {
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
        threadPoolExecutor.setThreadFactory(daemonThreadFactory(threadPoolExecutor.getThreadFactory()));
        ExecutorService executorServiceUnconfigurableExecutorService = java.util.concurrent.Executors.unconfigurableExecutorService(threadPoolExecutor);
        addDelayedShutdownHook(executorServiceUnconfigurableExecutorService, j, timeUnit);
        return executorServiceUnconfigurableExecutorService;
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
        scheduledThreadPoolExecutor.setThreadFactory(daemonThreadFactory(scheduledThreadPoolExecutor.getThreadFactory()));
        ScheduledExecutorService scheduledExecutorServiceUnconfigurableScheduledExecutorService = java.util.concurrent.Executors.unconfigurableScheduledExecutorService(scheduledThreadPoolExecutor);
        addDelayedShutdownHook(scheduledExecutorServiceUnconfigurableScheduledExecutorService, j, timeUnit);
        return scheduledExecutorServiceUnconfigurableScheduledExecutorService;
    }

    public static void addDelayedShutdownHook(final ExecutorService executorService, final long j, final TimeUnit timeUnit) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() { // from class: com.google.common.util.concurrent.Executors.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(j, timeUnit);
                } catch (InterruptedException unused) {
                }
            }
        }));
    }

    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        return getExitingExecutorService(threadPoolExecutor, 120L, TimeUnit.SECONDS);
    }

    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return getExitingScheduledExecutorService(scheduledThreadPoolExecutor, 120L, TimeUnit.SECONDS);
    }

    public static ThreadFactory daemonThreadFactory() {
        return daemonThreadFactory(java.util.concurrent.Executors.defaultThreadFactory());
    }

    public static ThreadFactory daemonThreadFactory(ThreadFactory threadFactory) {
        return new DaemonThreadFactory(threadFactory);
    }

    public static ExecutorService sameThreadExecutor() {
        return new SameThreadExecutorService();
    }

    private static class SameThreadExecutorService extends AbstractExecutorService {
        private final Lock lock;
        private int runningTasks;
        private boolean shutdown;
        private final Condition termination;

        private SameThreadExecutorService() {
            this.lock = new ReentrantLock();
            this.termination = this.lock.newCondition();
            this.runningTasks = 0;
            this.shutdown = false;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            startTask();
            try {
                runnable.run();
            } finally {
                endTask();
            }
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isShutdown() {
            this.lock.lock();
            try {
                return this.shutdown;
            } finally {
                this.lock.unlock();
            }
        }

        @Override // java.util.concurrent.ExecutorService
        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            } finally {
                this.lock.unlock();
            }
        }

        @Override // java.util.concurrent.ExecutorService
        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
        @Override // java.util.concurrent.ExecutorService
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean isTerminated() {
            /*
                r2 = this;
                java.util.concurrent.locks.Lock r0 = r2.lock
                r0.lock()
                boolean r0 = r2.shutdown     // Catch: java.lang.Throwable -> L16
                if (r0 == 0) goto Lf
                int r0 = r2.runningTasks     // Catch: java.lang.Throwable -> L16
                if (r0 != 0) goto Lf
                r0 = 1
                goto L10
            Lf:
                r0 = 0
            L10:
                java.util.concurrent.locks.Lock r1 = r2.lock
                r1.unlock()
                return r0
            L16:
                r0 = move-exception
                java.util.concurrent.locks.Lock r1 = r2.lock
                r1.unlock()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Executors.SameThreadExecutorService.isTerminated():boolean");
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            boolean z;
            long nanos = timeUnit.toNanos(j);
            this.lock.lock();
            while (true) {
                try {
                    if (isTerminated()) {
                        z = true;
                        break;
                    }
                    if (nanos <= 0) {
                        z = false;
                        break;
                    }
                    nanos = this.termination.awaitNanos(nanos);
                } finally {
                    this.lock.unlock();
                }
            }
            return z;
        }

        private void startTask() {
            this.lock.lock();
            try {
                if (isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                this.runningTasks++;
            } finally {
                this.lock.unlock();
            }
        }

        private void endTask() {
            this.lock.lock();
            try {
                this.runningTasks--;
                if (isTerminated()) {
                    this.termination.signalAll();
                }
            } finally {
                this.lock.unlock();
            }
        }
    }
}
