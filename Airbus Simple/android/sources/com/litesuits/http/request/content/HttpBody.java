package com.litesuits.http.request.content;

import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public abstract class HttpBody {
    public static final int OUTPUT_BUFFER_SIZE = 4096;
    protected String contentEncoding;
    protected String contentType;
    protected HttpListener httpListener;
    protected AbstractRequest request;

    public abstract long getContentLength();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public String getContentType() {
        return this.contentType;
    }

    public HttpBody setContentType(String str) {
        this.contentType = str;
        return this;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public HttpBody setContentEncoding(String str) {
        this.contentEncoding = str;
        return this;
    }

    public HttpListener getHttpListener() {
        return this.httpListener;
    }

    public void setHttpListener(HttpListener httpListener) {
        this.httpListener = httpListener;
    }

    public AbstractRequest getRequest() {
        return this.request;
    }

    public void setRequest(AbstractRequest abstractRequest) {
        this.request = abstractRequest;
        setHttpListener(abstractRequest.getHttpListener());
    }
}
