package com.yanzhenjie.andserver;

import java.io.IOException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpService;

/* loaded from: classes.dex */
public class HandleRequestThread extends Thread {
    private final HttpServerConnection mConnection;
    private CoreThread mCore;
    private final HttpService mHttpService;

    public HandleRequestThread(CoreThread coreThread, HttpService httpService, HttpServerConnection httpServerConnection) {
        this.mCore = coreThread;
        this.mHttpService = httpService;
        this.mConnection = httpServerConnection;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.mCore.isRunning() && this.mConnection.isOpen()) {
            try {
                this.mHttpService.handleRequest(this.mConnection, new BasicHttpContext());
            } catch (Exception unused) {
            } catch (Throwable th) {
                try {
                    this.mConnection.shutdown();
                } catch (IOException unused2) {
                }
                throw th;
            }
        }
        try {
            this.mConnection.shutdown();
        } catch (IOException unused3) {
        }
    }
}
