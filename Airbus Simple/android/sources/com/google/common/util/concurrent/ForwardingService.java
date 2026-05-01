package com.google.common.util.concurrent;

import com.google.common.base.Service;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public abstract class ForwardingService extends ForwardingObject implements Service {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.ForwardingObject
    public abstract Service delegate();

    @Override // com.google.common.base.Service
    public Future<Service.State> start() {
        return delegate().start();
    }

    @Override // com.google.common.base.Service
    public Service.State state() {
        return delegate().state();
    }

    @Override // com.google.common.base.Service
    public Future<Service.State> stop() {
        return delegate().stop();
    }

    @Override // com.google.common.base.Service
    public Service.State startAndWait() {
        return delegate().startAndWait();
    }

    @Override // com.google.common.base.Service
    public Service.State stopAndWait() {
        return delegate().stopAndWait();
    }

    @Override // com.google.common.base.Service
    public boolean isRunning() {
        return delegate().isRunning();
    }
}
