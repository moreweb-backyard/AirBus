package com.litesuits.http.request.content;

import com.litesuits.http.data.Consts;
import com.litesuits.http.data.Json;

/* loaded from: classes.dex */
public class JsonBody extends StringBody {
    public JsonBody(Object obj) {
        this(obj, "UTF-8");
    }

    public JsonBody(String str) {
        this(str, "UTF-8");
    }

    public JsonBody(String str, String str2) {
        super(str, Consts.MIME_TYPE_JSON, str2);
    }

    public JsonBody(Object obj, String str) {
        super(Json.get().toJson(obj), Consts.MIME_TYPE_JSON, str);
    }

    @Override // com.litesuits.http.request.content.StringBody
    public String toString() {
        return "JsonBody{} " + super.toString();
    }
}
