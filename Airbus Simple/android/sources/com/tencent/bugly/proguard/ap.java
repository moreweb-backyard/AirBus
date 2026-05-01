package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ap extends j implements Cloneable {
    private static byte[] d;
    private byte a;
    private String b;
    private byte[] c;

    @Override // com.tencent.bugly.proguard.j
    public final void a(StringBuilder sb, int i) {
    }

    public ap() {
        this.a = (byte) 0;
        this.b = "";
        this.c = null;
    }

    public ap(byte b, String str, byte[] bArr) {
        this.a = (byte) 0;
        this.b = "";
        this.c = null;
        this.a = b;
        this.b = str;
        this.c = bArr;
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 0);
        iVar.a(this.b, 1);
        if (this.c != null) {
            iVar.a(this.c, 2);
        }
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        this.a = hVar.a(this.a, 0, true);
        this.b = hVar.b(1, true);
        if (d == null) {
            d = new byte[]{0};
        }
        byte[] bArr = d;
        this.c = hVar.c(2, false);
    }
}
