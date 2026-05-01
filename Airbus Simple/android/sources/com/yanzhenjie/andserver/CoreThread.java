package com.yanzhenjie.andserver;

import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.util.Executors;
import com.yanzhenjie.andserver.website.BasicWebsite;
import com.yanzhenjie.andserver.website.WebSite;
import cz.msebera.android.httpclient.params.CoreConnectionPNames;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpRequestHandlerResolver;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

/* loaded from: classes.dex */
class CoreThread extends Thread {
    private boolean isLoop;
    private final Map<String, RequestHandler> mHandlerMap;
    private Server.Listener mListener;
    private final int mPort;
    private ServerSocket mServerSocket;
    private final int mTimeout;
    private final WebSite mWebSite;

    CoreThread(int i, int i2, Map<String, RequestHandler> map, WebSite webSite, Server.Listener listener) {
        this.mPort = i;
        this.mTimeout = i2;
        this.mHandlerMap = map;
        this.mWebSite = webSite;
        this.mListener = listener;
    }

    private HttpParams createHttpParams() {
        return new BasicHttpParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, this.mTimeout).setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8192).setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false).setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true).setParameter("http.origin-server", "WebServer/1.1");
    }

    private HttpProcessor createHttpProcessor() {
        BasicHttpProcessor basicHttpProcessor = new BasicHttpProcessor();
        basicHttpProcessor.addInterceptor(new ResponseContent());
        basicHttpProcessor.addInterceptor(new ResponseConnControl());
        basicHttpProcessor.addInterceptor(new ResponseDate());
        basicHttpProcessor.addInterceptor(new ResponseServer());
        basicHttpProcessor.addInterceptor(new ResponseProcessCookies());
        return basicHttpProcessor;
    }

    private HttpRequestHandlerResolver registerRequestHandler() {
        HttpRequestHandlerRegistry httpRequestHandlerRegistry = new HttpRequestHandlerRegistry();
        for (Map.Entry<String, RequestHandler> entry : this.mHandlerMap.entrySet()) {
            httpRequestHandlerRegistry.register(BasicWebsite.getHttpPath(entry.getKey()), new DefaultHttpRequestHandler(entry.getValue()));
        }
        HashMap map = new HashMap(3);
        this.mWebSite.onRegister(map);
        if (map.size() > 0) {
            for (Map.Entry entry2 : map.entrySet()) {
                httpRequestHandlerRegistry.register(BasicWebsite.getHttpPath((String) entry2.getKey()), new DefaultHttpRequestHandler((RequestHandler) entry2.getValue()));
            }
        }
        return httpRequestHandlerRegistry;
    }

    private HttpService createHttpService(HttpParams httpParams, HttpProcessor httpProcessor, HttpRequestHandlerResolver httpRequestHandlerResolver) {
        HttpService httpService = new HttpService(httpProcessor, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());
        httpService.setParams(httpParams);
        httpService.setHandlerResolver(httpRequestHandlerResolver);
        return httpService;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        HttpParams httpParamsCreateHttpParams = createHttpParams();
        HttpService httpServiceCreateHttpService = createHttpService(httpParamsCreateHttpParams, createHttpProcessor(), registerRequestHandler());
        try {
            try {
                this.mServerSocket = new ServerSocket();
                this.mServerSocket.setReuseAddress(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.mServerSocket.bind(new InetSocketAddress(this.mPort));
                if (this.mListener != null) {
                    Executors.getInstance().handler(new Runnable() { // from class: com.yanzhenjie.andserver.CoreThread.2
                        @Override // java.lang.Runnable
                        public void run() {
                            CoreThread.this.mListener.onStarted();
                        }
                    });
                }
                this.isLoop = true;
                while (this.isLoop) {
                    if (!this.mServerSocket.isClosed()) {
                        Socket socketAccept = this.mServerSocket.accept();
                        DefaultHttpServerConnection defaultHttpServerConnection = new DefaultHttpServerConnection();
                        defaultHttpServerConnection.bind(socketAccept, httpParamsCreateHttpParams);
                        HandleRequestThread handleRequestThread = new HandleRequestThread(this, httpServiceCreateHttpService, defaultHttpServerConnection);
                        handleRequestThread.setDaemon(true);
                        Executors.getInstance().executorService(handleRequestThread);
                    }
                }
            } catch (IOException e2) {
                if (this.mListener != null) {
                    Executors.getInstance().handler(new Runnable() { // from class: com.yanzhenjie.andserver.CoreThread.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CoreThread.this.mListener.onError(e2);
                        }
                    });
                }
            }
        } finally {
            shutdown();
        }
    }

    void shutdown() throws IOException {
        this.isLoop = false;
        try {
            if (this.mServerSocket != null) {
                this.mServerSocket.close();
            }
        } catch (IOException unused) {
        }
        if (isInterrupted()) {
            interrupt();
        }
        if (this.mListener != null) {
            Executors.getInstance().handler(new Runnable() { // from class: com.yanzhenjie.andserver.CoreThread.3
                @Override // java.lang.Runnable
                public void run() {
                    CoreThread.this.mListener.onStopped();
                }
            });
        }
    }

    boolean isRunning() {
        return this.isLoop;
    }
}
