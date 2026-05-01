package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.WebSite;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes.dex */
class DefaultServer implements Server {
    private CoreThread mCore;
    private final Map<String, RequestHandler> mHandlerMap;
    private Server.Listener mListener;
    private final int mPort;
    private final int mTimeout;
    private final WebSite mWebSite;

    DefaultServer(int i, int i2, Map<String, RequestHandler> map, WebSite webSite, Server.Listener listener) {
        this.mPort = i;
        this.mTimeout = i2;
        this.mHandlerMap = map;
        this.mWebSite = webSite;
        this.mListener = listener;
    }

    @Override // com.yanzhenjie.andserver.Server
    public void start() {
        if (isRunning()) {
            return;
        }
        this.mCore = new CoreThread(this.mPort, this.mTimeout, this.mHandlerMap, this.mWebSite, this.mListener);
        this.mCore.start();
    }

    @Override // com.yanzhenjie.andserver.Server
    public void stop() throws IOException {
        if (isRunning()) {
            this.mCore.shutdown();
        }
    }

    @Override // com.yanzhenjie.andserver.Server
    public boolean isRunning() {
        return this.mCore != null && this.mCore.isRunning();
    }
}
