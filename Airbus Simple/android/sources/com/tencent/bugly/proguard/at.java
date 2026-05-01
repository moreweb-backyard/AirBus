package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class at extends j {
    private static byte[] i = {0};
    private static Map<String, String> j = new HashMap();
    public byte a = 0;
    public int b = 0;
    public byte[] c = null;
    public String d = "";
    public long e = 0;
    public String f = "";
    public String g = "";
    public Map<String, String> h = null;

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 0);
        iVar.a(this.b, 1);
        if (this.c != null) {
            iVar.a(this.c, 2);
        }
        if (this.d != null) {
            iVar.a(this.d, 3);
        }
        iVar.a(this.e, 4);
        if (this.f != null) {
            iVar.a(this.f, 5);
        }
        if (this.g != null) {
            iVar.a(this.g, 6);
        }
        if (this.h != null) {
            iVar.a((Map) this.h, 7);
        }
    }

    static {
        j.put("", "");
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        this.a = hVar.a(this.a, 0, true);
        this.b = hVar.a(this.b, 1, true);
        byte[] bArr = i;
        this.c = hVar.c(2, false);
        this.d = hVar.b(3, false);
        this.e = hVar.a(this.e, 4, false);
        this.f = hVar.b(5, false);
        this.g = hVar.b(6, false);
        this.h = (Map) hVar.a((h) j, 7, false);
    }
}
