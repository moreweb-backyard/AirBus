package com.tencent.bugly.proguard;

import java.util.ArrayList;
import java.util.Collection;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ar extends j implements Cloneable {
    private static ArrayList<aq> b;
    public ArrayList<aq> a = null;

    @Override // com.tencent.bugly.proguard.j
    public final void a(StringBuilder sb, int i) {
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) {
        iVar.a((Collection) this.a, 0);
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        if (b == null) {
            b = new ArrayList<>();
            b.add(new aq());
        }
        this.a = (ArrayList) hVar.a((h) b, 0, true);
    }
}
