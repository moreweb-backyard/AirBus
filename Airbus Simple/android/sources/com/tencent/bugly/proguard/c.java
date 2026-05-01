package com.tencent.bugly.proguard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class c extends a {
    protected HashMap<String, byte[]> d = null;
    private HashMap<String, Object> e = new HashMap<>();
    private h f = new h();

    @Override // com.tencent.bugly.proguard.a
    public final /* bridge */ /* synthetic */ void a(String str) {
        super.a(str);
    }

    public void d() {
        this.d = new HashMap<>();
    }

    @Override // com.tencent.bugly.proguard.a
    public <T> void a(String str, T t) {
        if (this.d == null) {
            super.a(str, (String) t);
            return;
        }
        if (str == null) {
            throw new IllegalArgumentException("put key can not is null");
        }
        if (t == null) {
            throw new IllegalArgumentException("put value can not is null");
        }
        if (t instanceof Set) {
            throw new IllegalArgumentException("can not support Set");
        }
        i iVar = new i();
        iVar.a(this.b);
        iVar.a(t, 0);
        this.d.put(str, k.a(iVar.a()));
    }

    public final <T> T b(String str, T t) throws b {
        if (this.d != null) {
            if (!this.d.containsKey(str)) {
                return null;
            }
            if (this.e.containsKey(str)) {
                return (T) this.e.get(str);
            }
            try {
                this.f.a(this.d.get(str));
                this.f.a(this.b);
                T t2 = (T) this.f.a((h) t, 0, true);
                if (t2 != null) {
                    this.e.put(str, t2);
                }
                return t2;
            } catch (Exception e) {
                throw new b(e);
            }
        }
        if (!this.a.containsKey(str)) {
            return null;
        }
        if (this.e.containsKey(str)) {
            return (T) this.e.get(str);
        }
        byte[] value = new byte[0];
        Iterator<Map.Entry<String, byte[]>> it = this.a.get(str).entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<String, byte[]> next = it.next();
            next.getKey();
            value = next.getValue();
        }
        try {
            this.f.a(value);
            this.f.a(this.b);
            T t3 = (T) this.f.a((h) t, 0, true);
            this.e.put(str, t3);
            return t3;
        } catch (Exception e2) {
            throw new b(e2);
        }
    }

    @Override // com.tencent.bugly.proguard.a
    public byte[] a() {
        if (this.d != null) {
            i iVar = new i(0);
            iVar.a(this.b);
            iVar.a((Map) this.d, 0);
            return k.a(iVar.a());
        }
        return super.a();
    }

    @Override // com.tencent.bugly.proguard.a
    public void a(byte[] bArr) {
        try {
            super.a(bArr);
        } catch (Exception unused) {
            this.f.a(bArr);
            this.f.a(this.b);
            HashMap map = new HashMap(1);
            map.put("", new byte[0]);
            this.d = this.f.a((Map) map, 0, false);
        }
    }
}
