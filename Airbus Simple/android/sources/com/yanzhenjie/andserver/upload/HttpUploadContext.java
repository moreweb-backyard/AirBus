package com.yanzhenjie.andserver.upload;

import android.support.v7.widget.ActivityChooserView;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.fileupload.UploadContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;

/* loaded from: classes.dex */
public class HttpUploadContext implements UploadContext {
    private final HttpEntity mEntity;

    public HttpUploadContext(HttpEntityEnclosingRequest httpEntityEnclosingRequest) {
        this.mEntity = httpEntityEnclosingRequest.getEntity();
    }

    @Override // org.apache.commons.fileupload.RequestContext
    public String getCharacterEncoding() {
        Header contentEncoding = this.mEntity.getContentEncoding();
        if (contentEncoding == null) {
            return null;
        }
        return contentEncoding.getValue();
    }

    @Override // org.apache.commons.fileupload.RequestContext
    public String getContentType() {
        Header contentType = this.mEntity.getContentType();
        if (contentType == null) {
            return null;
        }
        return contentType.getValue();
    }

    @Override // org.apache.commons.fileupload.RequestContext
    public int getContentLength() {
        long jContentLength = contentLength();
        return jContentLength > 2147483647L ? ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED : (int) jContentLength;
    }

    @Override // org.apache.commons.fileupload.UploadContext
    public long contentLength() {
        return this.mEntity.getContentLength();
    }

    @Override // org.apache.commons.fileupload.RequestContext
    public InputStream getInputStream() throws IOException {
        return this.mEntity.getContent();
    }

    public String toString() {
        return String.format("ContentLength=%s, ContentType=%s", Long.valueOf(contentLength()), getContentType());
    }
}
