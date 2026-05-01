package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class aq extends j {
    private static ArrayList<ap> A;
    private static Map<String, String> B;
    private static Map<String, String> C;
    private static Map<String, String> v = new HashMap();
    private static ao w;
    private static an x;
    private static ArrayList<an> y;
    private static ArrayList<an> z;
    public String a = "";
    public long b = 0;
    public String c = "";
    public String d = "";
    public String e = "";
    public String f = "";
    public String g = "";
    public Map<String, String> h = null;
    public String i = "";
    public ao j = null;
    public int k = 0;
    public String l = "";
    public String m = "";
    public an n = null;
    public ArrayList<an> o = null;
    public ArrayList<an> p = null;
    public ArrayList<ap> q = null;
    public Map<String, String> r = null;
    public Map<String, String> s = null;
    public String t = "";
    private boolean u = true;

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 0);
        iVar.a(this.b, 1);
        iVar.a(this.c, 2);
        if (this.d != null) {
            iVar.a(this.d, 3);
        }
        if (this.e != null) {
            iVar.a(this.e, 4);
        }
        if (this.f != null) {
            iVar.a(this.f, 5);
        }
        if (this.g != null) {
            iVar.a(this.g, 6);
        }
        if (this.h != null) {
            iVar.a((Map) this.h, 7);
        }
        if (this.i != null) {
            iVar.a(this.i, 8);
        }
        if (this.j != null) {
            iVar.a((j) this.j, 9);
        }
        iVar.a(this.k, 10);
        if (this.l != null) {
            iVar.a(this.l, 11);
        }
        if (this.m != null) {
            iVar.a(this.m, 12);
        }
        if (this.n != null) {
            iVar.a((j) this.n, 13);
        }
        if (this.o != null) {
            iVar.a((Collection) this.o, 14);
        }
        if (this.p != null) {
            iVar.a((Collection) this.p, 15);
        }
        if (this.q != null) {
            iVar.a((Collection) this.q, 16);
        }
        if (this.r != null) {
            iVar.a((Map) this.r, 17);
        }
        if (this.s != null) {
            iVar.a((Map) this.s, 18);
        }
        if (this.t != null) {
            iVar.a(this.t, 19);
        }
        iVar.a(this.u, 20);
    }

    static {
        v.put("", "");
        w = new ao();
        x = new an();
        y = new ArrayList<>();
        y.add(new an());
        z = new ArrayList<>();
        z.add(new an());
        A = new ArrayList<>();
        A.add(new ap());
        B = new HashMap();
        B.put("", "");
        C = new HashMap();
        C.put("", "");
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        this.a = hVar.b(0, true);
        this.b = hVar.a(this.b, 1, true);
        this.c = hVar.b(2, true);
        this.d = hVar.b(3, false);
        this.e = hVar.b(4, false);
        this.f = hVar.b(5, false);
        this.g = hVar.b(6, false);
        this.h = (Map) hVar.a((h) v, 7, false);
        this.i = hVar.b(8, false);
        this.j = (ao) hVar.a((j) w, 9, false);
        this.k = hVar.a(this.k, 10, false);
        this.l = hVar.b(11, false);
        this.m = hVar.b(12, false);
        this.n = (an) hVar.a((j) x, 13, false);
        this.o = (ArrayList) hVar.a((h) y, 14, false);
        this.p = (ArrayList) hVar.a((h) z, 15, false);
        this.q = (ArrayList) hVar.a((h) A, 16, false);
        this.r = (Map) hVar.a((h) B, 17, false);
        this.s = (Map) hVar.a((h) C, 18, false);
        this.t = hVar.b(19, false);
        boolean z2 = this.u;
        this.u = hVar.a(20, false);
    }
}
