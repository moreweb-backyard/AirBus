package com.tencent.bugly.crashreport.common.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class StrategyBean implements Parcelable {
    public static final Parcelable.Creator<StrategyBean> CREATOR = new Parcelable.Creator<StrategyBean>() { // from class: com.tencent.bugly.crashreport.common.strategy.StrategyBean.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ StrategyBean createFromParcel(Parcel parcel) {
            return new StrategyBean(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ StrategyBean[] newArray(int i) {
            return new StrategyBean[i];
        }
    };
    public static String a = null;
    private static String w = "http://android.bugly.qq.com/rqd/async";
    private static String x = "http://android.bugly.qq.com/rqd/async";
    private static String y = "http://rqd.uu.qq.com/rqd/sync";
    public long b;
    public long c;
    public boolean d;
    public boolean e;
    public boolean f;
    public boolean g;
    public boolean h;
    public boolean i;
    public boolean j;
    public boolean k;
    public boolean l;
    public long m;
    public long n;
    public String o;
    public String p;
    public String q;
    public String r;
    public Map<String, String> s;
    public int t;
    public long u;
    public long v;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public StrategyBean() {
        this.b = -1L;
        this.c = -1L;
        this.d = true;
        this.e = true;
        this.f = true;
        this.g = true;
        this.h = false;
        this.i = true;
        this.j = true;
        this.k = true;
        this.l = true;
        this.n = 30000L;
        this.o = w;
        this.p = x;
        this.q = y;
        this.t = 10;
        this.u = 300000L;
        this.v = -1L;
        this.c = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("S(@L@L");
        sb.append("@)");
        a = sb.toString();
        sb.setLength(0);
        sb.append("*^@K#K");
        sb.append("@!");
        this.r = sb.toString();
    }

    public StrategyBean(Parcel parcel) {
        this.b = -1L;
        this.c = -1L;
        boolean z = true;
        this.d = true;
        this.e = true;
        this.f = true;
        this.g = true;
        this.h = false;
        this.i = true;
        this.j = true;
        this.k = true;
        this.l = true;
        this.n = 30000L;
        this.o = w;
        this.p = x;
        this.q = y;
        this.t = 10;
        this.u = 300000L;
        this.v = -1L;
        try {
            a = "S(@L@L@)";
            this.c = parcel.readLong();
            this.d = parcel.readByte() == 1;
            this.e = parcel.readByte() == 1;
            this.f = parcel.readByte() == 1;
            this.o = parcel.readString();
            this.p = parcel.readString();
            this.r = parcel.readString();
            this.s = com.tencent.bugly.proguard.a.b(parcel);
            this.g = parcel.readByte() == 1;
            this.h = parcel.readByte() == 1;
            this.k = parcel.readByte() == 1;
            this.l = parcel.readByte() == 1;
            this.n = parcel.readLong();
            this.i = parcel.readByte() == 1;
            if (parcel.readByte() != 1) {
                z = false;
            }
            this.j = z;
            this.m = parcel.readLong();
            this.t = parcel.readInt();
            this.u = parcel.readLong();
            this.v = parcel.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.c);
        parcel.writeByte(this.d ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.e ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.f ? (byte) 1 : (byte) 0);
        parcel.writeString(this.o);
        parcel.writeString(this.p);
        parcel.writeString(this.r);
        com.tencent.bugly.proguard.a.b(parcel, this.s);
        parcel.writeByte(this.g ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.h ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.k ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.l ? (byte) 1 : (byte) 0);
        parcel.writeLong(this.n);
        parcel.writeByte(this.i ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.j ? (byte) 1 : (byte) 0);
        parcel.writeLong(this.m);
        parcel.writeInt(this.t);
        parcel.writeLong(this.u);
        parcel.writeLong(this.v);
    }
}
