package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Service;
import com.google.common.base.Throwables;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public abstract class AbstractService implements Service {
    private final Transition shutdown;
    private final Transition startup;
    private final ReentrantLock lock = new ReentrantLock();
    private Service.State state = Service.State.NEW;
    private boolean shutdownWhenStartupFinishes = false;

    protected abstract void doStart();

    protected abstract void doStop();

    public AbstractService() {
        this.startup = new Transition();
        this.shutdown = new Transition();
    }

    @Override // com.google.common.base.Service
    public final Future<Service.State> start() {
        this.lock.lock();
        try {
            try {
                if (this.state == Service.State.NEW) {
                    this.state = Service.State.STARTING;
                    doStart();
                }
            } catch (Throwable th) {
                notifyFailed(th);
            }
            this.lock.unlock();
            return this.startup;
        } catch (Throwable th2) {
            this.lock.unlock();
            throw th2;
        }
    }

    @Override // com.google.common.base.Service
    public final Future<Service.State> stop() {
        this.lock.lock();
        try {
            try {
                if (this.state == Service.State.NEW) {
                    this.state = Service.State.TERMINATED;
                    this.startup.transitionSucceeded(Service.State.TERMINATED);
                    this.shutdown.transitionSucceeded(Service.State.TERMINATED);
                } else if (this.state == Service.State.STARTING) {
                    this.shutdownWhenStartupFinishes = true;
                    this.startup.transitionSucceeded(Service.State.STOPPING);
                } else if (this.state == Service.State.RUNNING) {
                    this.state = Service.State.STOPPING;
                    doStop();
                }
            } catch (Throwable th) {
                notifyFailed(th);
            }
            this.lock.unlock();
            return this.shutdown;
        } catch (Throwable th2) {
            this.lock.unlock();
            throw th2;
        }
    }

    @Override // com.google.common.base.Service
    public Service.State startAndWait() {
        try {
            return start().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e2) {
            throw Throwables.propagate(e2.getCause());
        }
    }

    @Override // com.google.common.base.Service
    public Service.State stopAndWait() {
        try {
            return stop().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e2) {
            throw Throwables.propagate(e2.getCause());
        }
    }

    protected final void notifyStarted() {
        this.lock.lock();
        try {
            if (this.state != Service.State.STARTING) {
                IllegalStateException illegalStateException = new IllegalStateException("Cannot notifyStarted() when the service is " + this.state);
                notifyFailed(illegalStateException);
                throw illegalStateException;
            }
            this.state = Service.State.RUNNING;
            if (this.shutdownWhenStartupFinishes) {
                stop();
            } else {
                this.startup.transitionSucceeded(Service.State.RUNNING);
            }
        } finally {
            this.lock.unlock();
        }
    }

    protected final void notifyStopped() {
        this.lock.lock();
        try {
            if (this.state != Service.State.STOPPING && this.state != Service.State.RUNNING) {
                IllegalStateException illegalStateException = new IllegalStateException("Cannot notifyStopped() when the service is " + this.state);
                notifyFailed(illegalStateException);
                throw illegalStateException;
            }
            this.state = Service.State.TERMINATED;
            this.shutdown.transitionSucceeded(Service.State.TERMINATED);
        } finally {
            this.lock.unlock();
        }
    }

    protected final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.lock.lock();
        try {
            if (this.state == Service.State.STARTING) {
                this.startup.transitionFailed(th);
                this.shutdown.transitionFailed(new Exception("Service failed to start.", th));
            } else if (this.state == Service.State.STOPPING) {
                this.shutdown.transitionFailed(th);
            }
            this.state = Service.State.FAILED;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // com.google.common.base.Service
    public final boolean isRunning() {
        return state() == Service.State.RUNNING;
    }

    @Override // com.google.common.base.Service
    public final Service.State state() {
        Service.State state;
        this.lock.lock();
        try {
            if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
                state = Service.State.STOPPING;
            } else {
                state = this.state;
            }
            return state;
        } finally {
            this.lock.unlock();
        }
    }

    private static class Transition implements Future<Service.State> {
        private final CountDownLatch done;
        private Throwable failureCause;
        private Service.State result;

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean z) {
            return false;
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return false;
        }

        private Transition() {
            this.done = new CountDownLatch(1);
        }

        void transitionSucceeded(Service.State state) {
            Preconditions.checkState(this.result == null);
            this.result = state;
            this.done.countDown();
        }

        void transitionFailed(Throwable th) {
            Preconditions.checkState(this.result == null);
            this.result = Service.State.FAILED;
            this.failureCause = th;
            this.done.countDown();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.done.getCount() == 0;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Service.State get() throws ExecutionException, InterruptedException {
            this.done.await();
            return getImmediately();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Service.State get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            if (this.done.await(j, timeUnit)) {
                return getImmediately();
            }
            throw new TimeoutException();
        }

        private Service.State getImmediately() throws ExecutionException {
            if (this.result == Service.State.FAILED) {
                throw new ExecutionException(this.failureCause);
            }
            return this.result;
        }
    }
}
