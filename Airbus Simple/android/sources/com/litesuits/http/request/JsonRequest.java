package com.litesuits.http.request;

import com.litesuits.http.data.TypeToken;
import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.JsonParser;
import com.litesuits.http.request.param.HttpParamModel;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class JsonRequest<T> extends JsonAbsRequest<T> {
    public JsonRequest(String str, Type type) {
        super(str);
        setResultType(type);
    }

    public JsonRequest(HttpParamModel httpParamModel, Type type) {
        super(httpParamModel);
        setResultType(type);
    }

    public JsonRequest(String str, TypeToken<T> typeToken) {
        super(str);
        setResultType(typeToken.getType());
    }

    public JsonRequest(HttpParamModel httpParamModel, TypeToken<T> typeToken) {
        super(httpParamModel);
        setResultType(typeToken.getType());
    }

    @Override // com.litesuits.http.request.JsonAbsRequest, com.litesuits.http.request.AbstractRequest
    public DataParser<T> createDataParser() {
        return new JsonParser(this.resultType);
    }
}
