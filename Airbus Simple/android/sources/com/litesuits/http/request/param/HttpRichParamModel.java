package com.litesuits.http.request.param;

import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.JsonRequest;
import com.litesuits.http.request.content.HttpBody;
import com.litesuits.http.request.query.ModelQueryBuilder;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;

/* loaded from: classes.dex */
public abstract class HttpRichParamModel<T> implements HttpParamModel {
    private HttpListener<T> httpListener;

    protected LinkedHashMap<String, String> createHeaders() {
        return null;
    }

    protected HttpBody createHttpBody() {
        return null;
    }

    protected HttpListener<T> createHttpListener() {
        return null;
    }

    protected ModelQueryBuilder createQueryBuilder() {
        return null;
    }

    public boolean isFieldsAttachToUrl() {
        return true;
    }

    public final LinkedHashMap<String, String> getHeaders() {
        return createHeaders();
    }

    public ModelQueryBuilder getModelQueryBuilder() {
        return createQueryBuilder();
    }

    public final HttpBody getHttpBody() {
        return createHttpBody();
    }

    public final HttpListener<T> getHttpListener() {
        if (this.httpListener == null) {
            this.httpListener = createHttpListener();
        }
        return this.httpListener;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <M extends HttpRichParamModel<T>> M setHttpListener(HttpListener<T> httpListener) {
        this.httpListener = httpListener;
        return this;
    }

    public JsonRequest<T> buildRequest() {
        return new JsonRequest<>(this, ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}
