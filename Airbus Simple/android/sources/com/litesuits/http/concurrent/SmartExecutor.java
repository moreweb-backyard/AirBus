package com.litesuits.http.concurrent;

import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.utils.HttpUtil;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class SmartExecutor implements Executor {
    private static final int CPU_CORE = HttpUtil.getCoresNumbers();
    private static final int DEFAULT_CACHE_SENCOND = 5;
    private static final String TAG = "SmartExecutor";
    private static ThreadPoolExecutor threadPool;
    private int coreSize;
    private final Object lock;
    private OverloadPolicy overloadPolicy;
    private int queueSize;
    private LinkedList<WrappedRunnable> runningList;
    private SchedulePolicy schedulePolicy;
    private LinkedList<WrappedRunnable> waitingList;

    interface WrappedRunnable extends Runnable {
        Runnable getRealRunnable();
    }

    public SmartExecutor() {
        this.coreSize = CPU_CORE;
        this.queueSize = this.coreSize * 32;
        this.lock = new Object();
        this.runningList = new LinkedList<>();
        this.waitingList = new LinkedList<>();
        this.schedulePolicy = SchedulePolicy.FirstInFistRun;
        this.overloadPolicy = OverloadPolicy.DiscardOldTaskInQueue;
        initThreadPool();
    }

    public SmartExecutor(int i, int i2) {
        this.coreSize = CPU_CORE;
        this.queueSize = this.coreSize * 32;
        this.lock = new Object();
        this.runningList = new LinkedList<>();
        this.waitingList = new LinkedList<>();
        this.schedulePolicy = SchedulePolicy.FirstInFistRun;
        this.overloadPolicy = OverloadPolicy.DiscardOldTaskInQueue;
        this.coreSize = i;
        this.queueSize = i2;
        initThreadPool();
    }

    protected synchronized void initThreadPool() {
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, "SmartExecutor core-queue size: " + this.coreSize + " - " + this.queueSize + "  running-wait task: " + this.runningList.size() + " - " + this.waitingList.size());
        }
        if (threadPool == null) {
            threadPool = createDefaultThreadPool();
        }
    }

    public static ThreadPoolExecutor createDefaultThreadPool() {
        return new ThreadPoolExecutor(CPU_CORE, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 5L, TimeUnit.SECONDS, new SynchronousQueue(), new ThreadFactory() { // from class: com.litesuits.http.concurrent.SmartExecutor.1
            static final String NAME = "lite-";
            AtomicInteger IDS = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, NAME + this.IDS.getAndIncrement());
            }
        }, new ThreadPoolExecutor.DiscardPolicy());
    }

    public static void setThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        threadPool = threadPoolExecutor;
    }

    public static ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public boolean cancelWaitingTask(Runnable runnable) {
        boolean z;
        synchronized (this.lock) {
            int size = this.waitingList.size();
            z = false;
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    if (this.waitingList.get(i).getRealRunnable() == runnable) {
                        this.waitingList.remove(i);
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return new FutureTask(runnable, t);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask(callable);
    }

    public Future<?> submit(Runnable runnable) {
        RunnableFuture runnableFutureNewTaskFor = newTaskFor(runnable, null);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        RunnableFuture<T> runnableFutureNewTaskFor = newTaskFor(runnable, t);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    public <T> Future<T> submit(Callable<T> callable) {
        RunnableFuture<T> runnableFutureNewTaskFor = newTaskFor(callable);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    public <T> void submit(RunnableFuture<T> runnableFuture) {
        execute(runnableFuture);
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        WrappedRunnable wrappedRunnable = new WrappedRunnable() { // from class: com.litesuits.http.concurrent.SmartExecutor.2
            public Runnable realRunnable;

            @Override // com.litesuits.http.concurrent.SmartExecutor.WrappedRunnable
            public Runnable getRealRunnable() {
                return runnable;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    runnable.run();
                } finally {
                    SmartExecutor.this.scheduleNext(this);
                }
            }
        };
        boolean z = false;
        synchronized (this.lock) {
            if (this.runningList.size() < this.coreSize) {
                this.runningList.add(wrappedRunnable);
                threadPool.execute(wrappedRunnable);
            } else if (this.waitingList.size() < this.queueSize) {
                this.waitingList.addLast(wrappedRunnable);
            } else {
                switch (this.overloadPolicy) {
                    case DiscardNewTaskInQueue:
                        this.waitingList.pollLast();
                        this.waitingList.addLast(wrappedRunnable);
                        break;
                    case DiscardOldTaskInQueue:
                        this.waitingList.pollFirst();
                        this.waitingList.addLast(wrappedRunnable);
                        break;
                    case CallerRuns:
                        z = true;
                        break;
                    case ThrowExecption:
                        throw new RuntimeException("Task rejected from lite smart executor. " + runnable.toString());
                }
            }
        }
        if (z) {
            if (HttpLog.isPrint) {
                HttpLog.i(TAG, "SmartExecutor task running in caller thread");
            }
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNext(WrappedRunnable wrappedRunnable) {
        WrappedRunnable wrappedRunnablePollLast;
        synchronized (this.lock) {
            if (!this.runningList.remove(wrappedRunnable)) {
                this.runningList.clear();
                HttpLog.e(TAG, "SmartExecutor scheduler remove failed, so clear all(running list) to avoid unpreditable error : " + wrappedRunnable);
            }
            if (this.waitingList.size() > 0) {
                switch (this.schedulePolicy) {
                    case LastInFirstRun:
                        wrappedRunnablePollLast = this.waitingList.pollLast();
                        break;
                    case FirstInFistRun:
                        wrappedRunnablePollLast = this.waitingList.pollFirst();
                        break;
                    default:
                        wrappedRunnablePollLast = this.waitingList.pollLast();
                        break;
                }
                if (wrappedRunnablePollLast != null) {
                    this.runningList.add(wrappedRunnablePollLast);
                    threadPool.execute(wrappedRunnablePollLast);
                    HttpLog.v(TAG, "Thread " + Thread.currentThread().getName() + " execute next task..");
                } else {
                    HttpLog.e(TAG, "SmartExecutor get a NULL task from waiting queue: " + Thread.currentThread().getName());
                }
            } else if (HttpLog.isPrint) {
                HttpLog.v(TAG, "SmartExecutor: all tasks is completed. current thread: " + Thread.currentThread().getName());
            }
        }
    }

    public void printThreadPoolInfo() {
        if (HttpLog.isPrint) {
            Log.i(TAG, "___________________________");
            Log.i(TAG, "state (shutdown - terminating - terminated): " + threadPool.isShutdown() + " - " + threadPool.isTerminating() + " - " + threadPool.isTerminated());
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("pool size (core - max): ");
            sb.append(threadPool.getCorePoolSize());
            sb.append(" - ");
            sb.append(threadPool.getMaximumPoolSize());
            Log.i(str, sb.toString());
            Log.i(TAG, "task (active - complete - total): " + threadPool.getActiveCount() + " - " + threadPool.getCompletedTaskCount() + " - " + threadPool.getTaskCount());
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("waitingList size : ");
            sb2.append(threadPool.getQueue().size());
            sb2.append(" , ");
            sb2.append(threadPool.getQueue());
            Log.i(str2, sb2.toString());
        }
    }

    public int getCoreSize() {
        return this.coreSize;
    }

    public int getRunningSize() {
        return this.runningList.size();
    }

    public int getWaitingSize() {
        return this.waitingList.size();
    }

    public SmartExecutor setCoreSize(int i) {
        if (i <= 0) {
            throw new NullPointerException("coreSize can not <= 0 !");
        }
        this.coreSize = i;
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, "SmartExecutor core-queue size: " + i + " - " + this.queueSize + "  running-wait task: " + this.runningList.size() + " - " + this.waitingList.size());
        }
        return this;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public SmartExecutor setQueueSize(int i) {
        if (i < 0) {
            throw new NullPointerException("queueSize can not < 0 !");
        }
        this.queueSize = i;
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, "SmartExecutor core-queue size: " + this.coreSize + " - " + i + "  running-wait task: " + this.runningList.size() + " - " + this.waitingList.size());
        }
        return this;
    }

    public OverloadPolicy getOverloadPolicy() {
        return this.overloadPolicy;
    }

    public void setOverloadPolicy(OverloadPolicy overloadPolicy) {
        if (overloadPolicy == null) {
            throw new NullPointerException("OverloadPolicy can not be null !");
        }
        this.overloadPolicy = overloadPolicy;
    }

    public SchedulePolicy getSchedulePolicy() {
        return this.schedulePolicy;
    }

    public void setSchedulePolicy(SchedulePolicy schedulePolicy) {
        if (schedulePolicy == null) {
            throw new NullPointerException("SchedulePolicy can not be null !");
        }
        this.schedulePolicy = schedulePolicy;
    }
}
