package com.litesuits.http.listener;

import android.os.SystemClock;
import com.litesuits.http.data.StatisticsInfo;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.InternalResponse;
import com.litesuits.http.response.Response;

/* loaded from: classes.dex */
public class StatisticsListener {
    private static final String TAG = "StatisticsListener";
    private long connect;
    private long connectStart;
    private long headLen;
    private InternalResponse internalResponse;
    private long read;
    private long readLen;
    private long readStart;
    private StatisticsInfo statisticsInfo;
    private long total;

    public void onRedirect(AbstractRequest abstractRequest) {
    }

    public void onRetry(AbstractRequest abstractRequest, int i, int i2) {
    }

    public StatisticsListener(InternalResponse internalResponse, StatisticsInfo statisticsInfo) {
        this.internalResponse = internalResponse;
        this.statisticsInfo = statisticsInfo;
    }

    public void onStart(AbstractRequest abstractRequest) {
        this.total = SystemClock.uptimeMillis();
    }

    public void onEnd(Response response) {
        if (this.total > 0) {
            this.total = SystemClock.uptimeMillis() - this.total;
            this.internalResponse.setUseTime(this.total);
            this.statisticsInfo.addConnectTime(this.total);
            this.headLen = this.internalResponse.getContentLength();
            this.readLen = this.internalResponse.getReadedLength();
            long j = this.readLen > 0 ? this.readLen : 0L;
            if (j == 0 && this.headLen > 0) {
                j = this.headLen;
            }
            this.statisticsInfo.addDataLength(j);
        }
    }

    public void onPreConnect(AbstractRequest abstractRequest) {
        this.connectStart = SystemClock.uptimeMillis();
    }

    public void onAfterConnect(AbstractRequest abstractRequest) {
        this.connect += SystemClock.uptimeMillis() - this.connectStart;
    }

    public void onPreRead(AbstractRequest abstractRequest) {
        this.readStart = SystemClock.uptimeMillis();
    }

    public void onAfterRead(AbstractRequest abstractRequest) {
        this.read += SystemClock.uptimeMillis() - this.readStart;
    }

    public String toString() {
        return resToString();
    }

    public String resToString() {
        return "\n[length]   headerLen: " + this.headLen + " B,    readedLen: " + this.readLen + " B,    global total len: " + this.statisticsInfo.getDataLength() + " B\n[time]   connect  : " + this.connect + " MS,    read: " + this.read + " MS,    total: " + this.total + " MS,    global total time: " + this.statisticsInfo.getConnectTime() + " MS";
    }
}
