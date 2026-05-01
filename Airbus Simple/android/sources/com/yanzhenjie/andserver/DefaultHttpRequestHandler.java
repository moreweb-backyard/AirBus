package com.yanzhenjie.andserver;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

/* loaded from: classes.dex */
public class DefaultHttpRequestHandler implements HttpRequestHandler {
    private RequestHandler mRequestHandler;

    public DefaultHttpRequestHandler(RequestHandler requestHandler) {
        this.mRequestHandler = requestHandler;
    }

    @Override // org.apache.http.protocol.HttpRequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Server", "AndServer");
        this.mRequestHandler.handle(httpRequest, httpResponse, httpContext);
    }
}
