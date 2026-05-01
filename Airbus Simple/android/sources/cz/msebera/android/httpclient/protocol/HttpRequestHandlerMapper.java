package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.HttpRequest;

/* loaded from: classes.dex */
public interface HttpRequestHandlerMapper {
    HttpRequestHandler lookup(HttpRequest httpRequest);
}
