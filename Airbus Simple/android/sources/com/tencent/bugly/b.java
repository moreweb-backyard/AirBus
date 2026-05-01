package com.tencent.bugly;

import android.content.Context;
import android.util.Log;
import com.tencent.bugly.proguard.m;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.w;
import java.util.ArrayList;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {
    public static boolean a = true;
    public static boolean b;
    private static List<a> c = new ArrayList();
    private static o d;
    private static m e;
    private static boolean f;

    private static boolean a(com.tencent.bugly.crashreport.common.info.a aVar) {
        List<String> list = aVar.m;
        aVar.getClass();
        return list != null && list.contains("bugly");
    }

    public static synchronized void a(Context context) {
        a(context, null);
    }

    public static synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (f) {
            w.d("[init] initial Multi-times, ignore this.", new Object[0]);
            return;
        }
        if (context == null) {
            Log.w(w.a, "[init] context of init() is null, check it.");
            return;
        }
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
        if (a(aVarA)) {
            a = false;
            return;
        }
        String strE = aVarA.e();
        if (strE == null) {
            Log.e(w.a, "[init] meta data of BUGLY_APPID in AndroidManifest.xml should be set.");
        } else {
            a(context, strE, aVarA.t, buglyStrategy);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:89:0x01ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void a(android.content.Context r18, java.lang.String r19, boolean r20, com.tencent.bugly.BuglyStrategy r21) {
        /*
            Method dump skipped, instructions count: 556
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.b.a(android.content.Context, java.lang.String, boolean, com.tencent.bugly.BuglyStrategy):void");
    }

    public static void a(a aVar) {
        if (c.contains(aVar)) {
            return;
        }
        c.add(aVar);
    }
}
