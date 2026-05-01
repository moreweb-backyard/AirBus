package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class f extends j {
    public byte[] e;
    private Map<String, String> i;
    private Map<String, String> j;
    private static /* synthetic */ boolean m = !f.class.desiredAssertionStatus();
    private static byte[] k = null;
    private static Map<String, String> l = null;
    public short a = 0;
    private byte f = 0;
    private int g = 0;
    public int b = 0;
    public String c = null;
    public String d = null;
    private int h = 0;

    public final boolean equals(Object obj) {
        f fVar = (f) obj;
        return k.a(1, (int) fVar.a) && k.a(1, (int) fVar.f) && k.a(1, fVar.g) && k.a(1, fVar.b) && k.a((Object) 1, (Object) fVar.c) && k.a((Object) 1, (Object) fVar.d) && k.a((Object) 1, (Object) fVar.e) && k.a(1, fVar.h) && k.a((Object) 1, (Object) fVar.i) && k.a((Object) 1, (Object) fVar.j);
    }

    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            if (m) {
                return null;
            }
            throw new AssertionError();
        }
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(i iVar) throws UnsupportedEncodingException {
        iVar.a(this.a, 1);
        iVar.a(this.f, 2);
        iVar.a(this.g, 3);
        iVar.a(this.b, 4);
        iVar.a(this.c, 5);
        iVar.a(this.d, 6);
        iVar.a(this.e, 7);
        iVar.a(this.h, 8);
        iVar.a((Map) this.i, 9);
        iVar.a((Map) this.j, 10);
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(h hVar) {
        try {
            this.a = hVar.a(this.a, 1, true);
            this.f = hVar.a(this.f, 2, true);
            this.g = hVar.a(this.g, 3, true);
            this.b = hVar.a(this.b, 4, true);
            this.c = hVar.b(5, true);
            this.d = hVar.b(6, true);
            if (k == null) {
                k = new byte[]{0};
            }
            byte[] bArr = k;
            this.e = hVar.c(7, true);
            this.h = hVar.a(this.h, 8, true);
            if (l == null) {
                HashMap map = new HashMap();
                l = map;
                map.put("", "");
            }
            this.i = (Map) hVar.a((h) l, 9, true);
            if (l == null) {
                HashMap map2 = new HashMap();
                l = map2;
                map2.put("", "");
            }
            this.j = (Map) hVar.a((h) l, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestPacket decode error " + e.a(this.e));
            throw new RuntimeException(e);
        }
    }

    @Override // com.tencent.bugly.proguard.j
    public final void a(StringBuilder sb, int i) {
        com.tencent.bugly.crashreport.crash.jni.b bVar = new com.tencent.bugly.crashreport.crash.jni.b(sb, i);
        bVar.a(this.a, "iVersion");
        bVar.a(this.f, "cPacketType");
        bVar.a(this.g, "iMessageType");
        bVar.a(this.b, "iRequestId");
        bVar.b(this.c, "sServantName");
        bVar.b(this.d, "sFuncName");
        bVar.a(this.e, "sBuffer");
        bVar.a(this.h, "iTimeout");
        bVar.a((Map) this.i, "context");
        bVar.a((Map) this.j, "status");
    }
}
