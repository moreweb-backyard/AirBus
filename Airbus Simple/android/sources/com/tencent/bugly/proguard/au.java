package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class au extends j implements Cloneable {
    public String a = "";
    private String b = "";

    @Override // com.tencent.bugly.proguard.j
    public final void a(StringBuilder sb, int i) {
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 0);
        iVar.a(this.b, 1);
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        this.a = hVar.b(0, true);
        this.b = hVar.b(1, true);
    }
}
