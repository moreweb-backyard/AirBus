package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class an extends j implements Cloneable {
    public String a = "";
    public String b = "";
    public String c = "";
    private String e = "";
    public String d = "";

    @Override // com.tencent.bugly.proguard.j
    public final void a(StringBuilder sb, int i) {
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 0);
        if (this.b != null) {
            iVar.a(this.b, 1);
        }
        if (this.c != null) {
            iVar.a(this.c, 2);
        }
        if (this.e != null) {
            iVar.a(this.e, 3);
        }
        if (this.d != null) {
            iVar.a(this.d, 4);
        }
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        this.a = hVar.b(0, true);
        this.b = hVar.b(1, false);
        this.c = hVar.b(2, false);
        this.e = hVar.b(3, false);
        this.d = hVar.b(4, false);
    }
}
