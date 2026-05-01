package com.litesuits.http.data;

import com.google.gson.Gson;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class GsonImpl extends Json {
    private Gson gson = new Gson();

    @Override // com.litesuits.http.data.Json
    public String toJson(Object obj) {
        return this.gson.toJson(obj);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(String str, Class<T> cls) {
        return (T) this.gson.fromJson(str, (Class) cls);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(String str, Type type) {
        return (T) this.gson.fromJson(str, type);
    }

    @Override // com.litesuits.http.data.Json
    public <T> T toObject(byte[] bArr, Class<T> cls) {
        return (T) this.gson.fromJson(new String(bArr), (Class) cls);
    }
}
