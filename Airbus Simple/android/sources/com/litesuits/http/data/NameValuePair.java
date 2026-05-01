package com.litesuits.http.data;

import java.io.Serializable;

/* loaded from: classes.dex */
public class NameValuePair implements Serializable {
    private static final long serialVersionUID = -1339856642868580559L;
    private final String name;
    private final String value;

    public NameValuePair(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return String.format("%-20s", this.name) + "=  " + this.value;
    }
}
