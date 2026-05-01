package com.litesuits.http.concurrent;

/* loaded from: classes.dex */
public abstract class PriorityRunnable implements Runnable {
    int priority;

    protected PriorityRunnable(int i) {
        this.priority = i;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int i) {
        this.priority = i;
    }
}
