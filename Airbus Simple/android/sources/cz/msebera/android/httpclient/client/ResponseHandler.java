package cz.msebera.android.httpclient.client;

import cz.msebera.android.httpclient.HttpResponse;
import java.io.IOException;

/* loaded from: classes.dex */
public interface ResponseHandler<T> {
    T handleResponse(HttpResponse httpResponse) throws IOException;
}
