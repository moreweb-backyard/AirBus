package com.google.common.util.concurrent;

import com.google.common.base.Service;
import com.google.common.base.Throwables;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public abstract class AbstractExecutionThreadService implements Service {
    private final Service delegate = new AbstractService() { // from class: com.google.common.util.concurrent.AbstractExecutionThreadService.1
        @Override // com.google.common.util.concurrent.AbstractService
        protected final void doStart() {
            AbstractExecutionThreadService.this.executor().execute(new Runnable() { // from class: com.google.common.util.concurrent.AbstractExecutionThreadService.1.1
                /* JADX WARN: Finally extract failed */
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AbstractExecutionThreadService.this.startUp();
                        notifyStarted();
                        if (isRunning()) {
                            try {
                                AbstractExecutionThreadService.this.run();
                            } catch (Throwable th) {
                                try {
                                    AbstractExecutionThreadService.this.shutDown();
                                } catch (Exception unused) {
                                }
                                throw th;
                            }
                        }
                        AbstractExecutionThreadService.this.shutDown();
                        notifyStopped();
                    } catch (Throwable th2) {
                        notifyFailed(th2);
                        throw Throwables.propagate(th2);
                    }
                }
            });
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    };

    protected abstract void run() throws Exception;

    protected void shutDown() throws Exception {
    }

    protected void startUp() throws Exception {
    }

    protected void triggerShutdown() {
    }

    protected Executor executor() {
        return new Executor() { // from class: com.google.common.util.concurrent.AbstractExecutionThreadService.2
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                new Thread(runnable, AbstractExecutionThreadService.this.toString()).start();
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
