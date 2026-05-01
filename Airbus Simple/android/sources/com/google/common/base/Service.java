package com.google.common.base;

import java.util.concurrent.Future;

/* loaded from: classes.dex */
public interface Service {

    public enum State {
        NEW,
        STARTING,
        RUNNING,
        STOPPING,
        TERMINATED,
        FAILED
    }

    boolean isRunning();

    Future<State> start();

    State startAndWait();

    State state();

    Future<State> stop();

    State stopAndWait();
}
