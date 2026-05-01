package com.google.common.util.concurrent;

import com.google.common.base.Service;
import com.google.common.base.Throwables;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new AbstractService() { // from class: com.google.common.util.concurrent.AbstractIdleService.1
        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            AbstractIdleService.this.executor(Service.State.STARTING).execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractIdleService.1.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AbstractIdleService.this.startUp();
                        notifyStarted();
                    } catch (Throwable th) {
                        notifyFailed(th);
                        throw Throwables.propagate(th);
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStop() {
            AbstractIdleService.this.executor(Service.State.STOPPING).execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractIdleService.1.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AbstractIdleService.this.shutDown();
                        notifyStopped();
                    } catch (Throwable th) {
                        notifyFailed(th);
                        throw Throwables.propagate(th);
                    }
                }
            });
        }
    };

    protected abstract void shutDown() throws Exception;

    protected abstract void startUp() throws Exception;

    protected Executor executor(final Service.State state) {
        return new Executor() { // from class: com.google.common.util.concurrent.AbstractIdleService.2
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                new Thread(runnable, AbstractIdleService.this.toString() + " " + state).start();
            }
        };
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    @Override // com.google.common.base.Service
    public final Future<Service.State> start() {
        return this.delegate.start();
    }

    @Override // com.google.common.base.Service
    public final Service.State startAndWait() {
        return this.delegate.startAndWait();
    }

    @Override // com.google.common.base.Service
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    @Override // com.google.common.base.Service
    public final Service.State state() {
        return this.delegate.state();
    }

    @Override // com.google.common.base.Service
    public final Future<Service.State> stop() {
        return this.delegate.stop();
    }

    @Override // com.google.common.base.Service
    public final Service.State stopAndWait() {
        return this.delegate.stopAndWait();
    }
}
