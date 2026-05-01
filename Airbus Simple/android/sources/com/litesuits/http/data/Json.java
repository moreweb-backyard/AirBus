package com.litesuits.http.data;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public abstract class Json {
    private static Json json;

    public abstract String toJson(Object obj);

    public abstract <T> T toObject(String str, Class<T> cls);

    public abstract <T> T toObject(String str, Type type);

    public abstract <T> T toObject(byte[] bArr, Class<T> cls);

    public static Json set(Json json2) {
        json = json2;
        return json;
    }

    public static Json setDefault() {
        json = new GsonImpl();
        return json;
    }

    public static Json get() {
        if (json == null) {
            json = new GsonImpl();
        }
        return json;
    }
}
