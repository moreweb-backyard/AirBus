package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.WebSite;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class AndServer {
    private final Map<String, RequestHandler> mHandlerMap;
    private final Server.Listener mListener;
    private final int mPort;
    private final int mTimeout;
    private final WebSite mWebSite;

    private AndServer(Build build) {
        this.mPort = build.mPort;
        this.mTimeout = build.mTimeout;
        this.mHandlerMap = build.mHandlerMap;
        this.mWebSite = build.mWebSite;
        this.mListener = build.mListener;
    }

    public Server createServer() {
        return new DefaultServer(this.mPort, this.mTimeout, this.mHandlerMap, this.mWebSite, this.mListener);
    }

    public static final class Build {
        private Server.Listener mListener;
        private WebSite mWebSite;
        private int mPort = 8080;
        private int mTimeout = 10000;
        private Map<String, RequestHandler> mHandlerMap = new HashMap(2);

        public Build port(int i) {
            this.mPort = i;
            return this;
        }

        public Build timeout(int i) {
            this.mTimeout = i;
            return this;
        }

        public Build registerHandler(String str, RequestHandler requestHandler) {
            this.mHandlerMap.put(str, requestHandler);
            return this;
        }

        public Build website(WebSite webSite) {
            this.mWebSite = webSite;
            return this;
        }

        public Build listener(Server.Listener listener) {
            this.mListener = listener;
            return this;
        }

        public AndServer build() {
            return new AndServer(this);
        }
    }
}
