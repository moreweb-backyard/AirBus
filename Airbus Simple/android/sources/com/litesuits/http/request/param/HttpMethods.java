package com.litesuits.http.request.param;

import cz.msebera.android.httpclient.client.methods.HttpPatch;

/* loaded from: classes.dex */
public enum HttpMethods {
    Get("GET"),
    Head("HEAD"),
    Trace("TRACE"),
    Options("OPTIONS"),
    Delete("DELETE"),
    Put("PUT"),
    Post("POST"),
    Patch(HttpPatch.METHOD_NAME);

    private String methodName;

    HttpMethods(String str) {
        this.methodName = str;
    }

    public String getMethodName() {
        return this.methodName;
    }
}
