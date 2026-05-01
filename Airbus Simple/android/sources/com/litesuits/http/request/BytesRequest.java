package com.litesuits.http.request;

import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.BytesParser;
import com.litesuits.http.request.param.HttpParamModel;

/* loaded from: classes.dex */
public class BytesRequest extends AbstractRequest<byte[]> {
    public BytesRequest(String str) {
        super(str);
    }

    public BytesRequest(HttpParamModel httpParamModel) {
        super(httpParamModel);
    }

    @Override // com.litesuits.http.request.AbstractRequest
    public DataParser<byte[]> createDataParser() {
        return new BytesParser();
    }
}
