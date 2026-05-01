package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a implements NativeExceptionHandler {
    private final Context a;
    private final com.tencent.bugly.crashreport.crash.b b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final com.tencent.bugly.crashreport.common.strategy.a d;
    private final String e;

    public a(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2, String str) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
        this.e = str;
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final CrashDetailBean packageCrashDatas(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, String str9, byte[] bArr, Map<String, String> map, boolean z) throws IOException {
        int iIndexOf;
        String str10;
        int iIndexOf2;
        boolean zN = c.a().n();
        String str11 = zN ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (zN) {
            w.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.b = 1;
        crashDetailBean.e = this.c.g();
        crashDetailBean.f = this.c.i;
        crashDetailBean.g = this.c.v();
        crashDetailBean.m = this.c.f();
        crashDetailBean.n = str3;
        crashDetailBean.o = str11;
        crashDetailBean.p = str4;
        crashDetailBean.q = str5;
        crashDetailBean.r = j;
        crashDetailBean.u = com.tencent.bugly.proguard.a.c(crashDetailBean.q.getBytes());
        crashDetailBean.z = str;
        crashDetailBean.A = str2;
        crashDetailBean.H = this.c.x();
        crashDetailBean.h = this.c.u();
        crashDetailBean.i = this.c.G();
        crashDetailBean.v = str8;
        String strA = b.a(this.e, str8);
        if (strA != null) {
            crashDetailBean.T = strA;
        }
        File file = new File(this.e, "backup_record.txt");
        crashDetailBean.U = file.exists() ? file.getAbsolutePath() : null;
        crashDetailBean.I = str7;
        crashDetailBean.J = str6;
        crashDetailBean.K = str9;
        crashDetailBean.E = this.c.o();
        crashDetailBean.F = this.c.n();
        crashDetailBean.G = this.c.p();
        if (z) {
            crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
            crashDetailBean.w = com.tencent.bugly.proguard.a.a(this.a, c.d, (String) null);
            crashDetailBean.x = x.a(true);
            crashDetailBean.L = this.c.a;
            crashDetailBean.M = this.c.n;
            crashDetailBean.O = this.c.D();
            crashDetailBean.P = this.c.E();
            crashDetailBean.Q = this.c.y();
            crashDetailBean.R = this.c.C();
            crashDetailBean.y = com.tencent.bugly.proguard.a.a(c.e, false);
            if (crashDetailBean.q != null && (iIndexOf = crashDetailBean.q.indexOf("java:\n")) > 0) {
                int length = iIndexOf + "java:\n".length();
                String strSubstring = crashDetailBean.q.substring(length, crashDetailBean.q.length() - 1);
                if (strSubstring.length() > 0 && crashDetailBean.y.containsKey(crashDetailBean.A) && (iIndexOf2 = (str10 = crashDetailBean.y.get(crashDetailBean.A)).indexOf(strSubstring)) > 0) {
                    String strSubstring2 = str10.substring(iIndexOf2);
                    crashDetailBean.y.put(crashDetailBean.A, strSubstring2);
                    crashDetailBean.q = crashDetailBean.q.substring(0, length);
                    crashDetailBean.q += strSubstring2;
                }
            }
            if (str == null) {
                crashDetailBean.z = this.c.d;
            }
            this.b.b(crashDetailBean);
        } else {
            crashDetailBean.B = -1L;
            crashDetailBean.C = -1L;
            crashDetailBean.D = -1L;
            crashDetailBean.w = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.";
            crashDetailBean.L = -1L;
            crashDetailBean.O = -1;
            crashDetailBean.P = -1;
            crashDetailBean.Q = map;
            crashDetailBean.R = null;
            crashDetailBean.y = null;
            if (str == null) {
                crashDetailBean.z = "unknown(record)";
            }
            if (bArr == null) {
                crashDetailBean.x = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.".getBytes();
            } else {
                crashDetailBean.x = bArr;
            }
        }
        return crashDetailBean;
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final void handleNativeException(int i, int i2, long j, long j2, String str, String str2, String str3, String str4, int i3, String str5, int i4, int i5, int i6, String str6, String str7) {
        w.a("Native Crash Happen v1", new Object[0]);
        handleNativeException2(i, i2, j, j2, str, str2, str3, str4, i3, str5, i4, i5, i6, str6, str7, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x0226 A[Catch: Throwable -> 0x0288, TryCatch #2 {Throwable -> 0x0288, blocks: (B:74:0x0220, B:76:0x0226, B:78:0x022f), top: B:98:0x0220 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x022f A[Catch: Throwable -> 0x0288, TRY_LEAVE, TryCatch #2 {Throwable -> 0x0288, blocks: (B:74:0x0220, B:76:0x0226, B:78:0x022f), top: B:98:0x0220 }] */
    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void handleNativeException2(int r22, int r23, long r24, long r26, java.lang.String r28, java.lang.String r29, java.lang.String r30, java.lang.String r31, int r32, java.lang.String r33, int r34, int r35, int r36, java.lang.String r37, java.lang.String r38, java.lang.String[] r39) {
        /*
            Method dump skipped, instructions count: 664
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.a.handleNativeException2(int, int, long, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int, java.lang.String, java.lang.String, java.lang.String[]):void");
    }
}
