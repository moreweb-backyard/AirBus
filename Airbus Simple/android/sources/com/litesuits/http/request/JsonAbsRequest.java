package com.litesuits.http.request;

import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.JsonParser;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.request.param.NonHttpParam;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public abstract class JsonAbsRequest<T> extends AbstractRequest<T> {

    @NonHttpParam
    protected Type resultType;

    public JsonAbsRequest(String str) {
        super(str);
    }

    protected JsonAbsRequest(HttpParamModel httpParamModel) {
        super(httpParamModel);
    }

    protected JsonAbsRequest(String str, HttpParamModel httpParamModel) {
        super(str, httpParamModel);
    }

    @Override // com.litesuits.http.request.AbstractRequest
    public DataParser<T> createDataParser() {
        return new JsonParser(getResultType());
    }

    public Type getResultType() {
        if (this.resultType == null) {
            this.resultType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.resultType;
    }

    public <R extends JsonAbsRequest> R setResultType(Type type) {
        this.resultType = type;
        return this;
    }
}
