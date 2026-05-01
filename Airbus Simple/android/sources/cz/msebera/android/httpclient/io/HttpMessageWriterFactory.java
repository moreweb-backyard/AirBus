package cz.msebera.android.httpclient.io;

import cz.msebera.android.httpclient.HttpMessage;

/* loaded from: classes.dex */
public interface HttpMessageWriterFactory<T extends HttpMessage> {
    HttpMessageWriter<T> create(SessionOutputBuffer sessionOutputBuffer);
}
