package com.litesuits.http.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class FastJson extends Json {
    private static final String TAG = "FastJson";

    @Override // com.litesuits.http.data.Json
    public String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(String str, Class<T> cls) {
        return (T) JSON.parseObject(str, cls);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(String str, Type type) {
        return (T) JSON.parseObject(str, type, new Feature[0]);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(byte[] bArr, Class<T> cls) {
        return (T) JSON.parseObject(bArr, cls, new Feature[0]);
    }
}
