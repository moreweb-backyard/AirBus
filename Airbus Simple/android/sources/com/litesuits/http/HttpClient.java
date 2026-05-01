package com.litesuits.http;

import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.InternalResponse;

/* loaded from: classes.dex */
public interface HttpClient {
    void config(HttpConfig httpConfig);

    <T> void connect(AbstractRequest<T> abstractRequest, InternalResponse internalResponse) throws Exception;
}
