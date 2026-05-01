package com.yanzhenjie.andserver;

/* loaded from: classes.dex */
public interface Server {

    public interface Listener {
        void onError(Exception exc);

        void onStarted();

        void onStopped();
    }

    boolean isRunning();

    void start();

    void stop();
}
