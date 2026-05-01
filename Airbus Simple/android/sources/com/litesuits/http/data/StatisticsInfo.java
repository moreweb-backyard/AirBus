package com.litesuits.http.data;

import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public class StatisticsInfo {
    private AtomicLong connectTime = new AtomicLong();
    private AtomicLong dataLength = new AtomicLong();

    public void addConnectTime(long j) {
        this.connectTime.addAndGet(j);
    }

    public void addDataLength(long j) {
        this.dataLength.addAndGet(j);
    }

    public long getConnectTime() {
        return this.connectTime.longValue();
    }

    public long getDataLength() {
        return this.dataLength.longValue();
    }

    public String toString() {
        return "StatisticsInfo{connectTime=" + this.connectTime + ", dataLength=" + this.dataLength + '}';
    }
}
