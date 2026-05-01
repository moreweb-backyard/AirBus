package com.litesuits.http.request;

import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.StringParser;
import com.litesuits.http.request.param.HttpParamModel;

/* loaded from: classes.dex */
public class StringRequest extends AbstractRequest<String> {
    public StringRequest(String str) {
        super(str);
    }

    public StringRequest(HttpParamModel httpParamModel) {
        super(httpParamModel);
    }

    public StringRequest(String str, HttpParamModel httpParamModel) {
        super(str, httpParamModel);
    }

    @Override // com.litesuits.http.request.AbstractRequest
    public DataParser<String> createDataParser() {
        return new StringParser();
    }
}
