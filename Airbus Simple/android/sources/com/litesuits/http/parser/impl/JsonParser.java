package com.litesuits.http.parser.impl;

import com.litesuits.http.data.Json;
import com.litesuits.http.parser.MemCacheableParser;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class JsonParser<T> extends MemCacheableParser<T> {
    protected Type claxx;
    protected String json;

    public JsonParser(Type type) {
        this.claxx = type;
    }

    @Override // com.litesuits.http.parser.DataParser
    protected T parseNetStream(InputStream inputStream, long j, String str) throws IOException {
        this.json = streamToString(inputStream, j, str);
        return (T) Json.get().toObject(this.json, this.claxx);
    }

    @Override // com.litesuits.http.parser.MemCacheableParser
    protected T parseDiskCache(InputStream inputStream, long j) throws IOException {
        this.json = streamToString(inputStream, j, this.charSet);
        return (T) Json.get().toObject(this.json, this.claxx);
    }

    @Override // com.litesuits.http.parser.MemCacheableParser
    protected boolean tryKeepToCache(T t) throws IOException {
        return keepToCache(this.json);
    }

    @Override // com.litesuits.http.parser.DataParser
    public String getRawString() {
        return this.json;
    }

    public <C> C getJsonModel(Class<C> cls) {
        return (C) Json.get().toObject(this.json, (Class) cls);
    }

    @Override // com.litesuits.http.parser.DataParser
    public String toString() {
        return "JsonParser{claxx=" + this.claxx + "} " + super.toString();
    }
}
