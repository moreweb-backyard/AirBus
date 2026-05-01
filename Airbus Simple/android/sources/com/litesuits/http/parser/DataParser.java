package com.litesuits.http.parser;

import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class DataParser<T> {
    protected static final String TAG = "DataParser";
    protected T data;
    protected long readLength;
    protected AbstractRequest<T> request;
    protected String charSet = "UTF-8";
    protected int buffSize = 4096;

    public String getRawString() {
        return null;
    }

    public abstract boolean isMemCacheSupport();

    protected abstract T parseNetStream(InputStream inputStream, long j, String str) throws IOException;

    public abstract T readFromDiskCache(File file) throws IOException;

    protected byte[] translateBytes(byte[] bArr) {
        return bArr;
    }

    public T readFromNetStream(InputStream inputStream, long j, String str) throws IOException {
        if (inputStream != null) {
            try {
                this.data = parseNetStream(inputStream, j, str);
            } finally {
                inputStream.close();
            }
        }
        return this.data;
    }

    public final T readFromMemoryCache(T t) {
        if (isMemCacheSupport()) {
            this.data = t;
        }
        return this.data;
    }

    public final T getData() {
        return this.data;
    }

    public final long getReadedLength() {
        return this.readLength;
    }

    public final void setRequest(AbstractRequest<T> abstractRequest) {
        this.request = abstractRequest;
        if (abstractRequest.getCharSet() != null) {
            this.charSet = abstractRequest.getCharSet();
        }
    }

    protected final void notifyReading(long j, long j2) {
        HttpListener<T> httpListener = this.request.getHttpListener();
        if (httpListener != null) {
            httpListener.notifyCallLoading(this.request, j, j2);
        }
    }

    public String toString() {
        return "DataParser{buffSize=" + this.buffSize + ", readLength=" + this.readLength + '}';
    }
}
