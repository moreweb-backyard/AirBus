package com.tencent.bugly.crashreport.crash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.an;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.aq;
import com.tencent.bugly.proguard.ar;
import com.tencent.bugly.proguard.as;
import com.tencent.bugly.proguard.n;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.s;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.w;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {
    private static int a;
    private Context b;
    private o c;
    private com.tencent.bugly.crashreport.common.strategy.a d;
    private n e;
    private BuglyStrategy.a f;

    public b(int i, Context context, t tVar, o oVar, com.tencent.bugly.crashreport.common.strategy.a aVar, BuglyStrategy.a aVar2, n nVar) {
        a = i;
        this.b = context;
        this.c = oVar;
        this.d = aVar;
        this.f = aVar2;
        this.e = nVar;
    }

    private static List<a> a(List<a> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        long jC = com.tencent.bugly.proguard.a.c();
        ArrayList arrayList = new ArrayList();
        for (a aVar : list) {
            if (aVar.d && aVar.b < jC - 86400000) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    private CrashDetailBean a(List<a> list, CrashDetailBean crashDetailBean) {
        List<CrashDetailBean> listB;
        String[] strArrSplit;
        if (list == null || list.size() == 0) {
            return crashDetailBean;
        }
        CrashDetailBean crashDetailBean2 = null;
        ArrayList arrayList = new ArrayList(10);
        for (a aVar : list) {
            if (aVar.e) {
                arrayList.add(aVar);
            }
        }
        if (arrayList.size() > 0 && (listB = b(arrayList)) != null && listB.size() > 0) {
            Collections.sort(listB);
            CrashDetailBean crashDetailBean3 = null;
            for (int i = 0; i < listB.size(); i++) {
                CrashDetailBean crashDetailBean4 = listB.get(i);
                if (i == 0) {
                    crashDetailBean3 = crashDetailBean4;
                } else if (crashDetailBean4.s != null && (strArrSplit = crashDetailBean4.s.split(IOUtils.LINE_SEPARATOR_UNIX)) != null) {
                    for (String str : strArrSplit) {
                        if (!crashDetailBean3.s.contains(str)) {
                            crashDetailBean3.t++;
                            crashDetailBean3.s += str + IOUtils.LINE_SEPARATOR_UNIX;
                        }
                    }
                }
            }
            crashDetailBean2 = crashDetailBean3;
        }
        if (crashDetailBean2 == null) {
            crashDetailBean.j = true;
            crashDetailBean.t = 0;
            crashDetailBean.s = "";
            crashDetailBean2 = crashDetailBean;
        }
        for (a aVar2 : list) {
            if (!aVar2.e && !aVar2.d) {
                String str2 = crashDetailBean2.s;
                StringBuilder sb = new StringBuilder();
                sb.append(aVar2.b);
                if (!str2.contains(sb.toString())) {
                    crashDetailBean2.t++;
                    crashDetailBean2.s += aVar2.b + IOUtils.LINE_SEPARATOR_UNIX;
                }
            }
        }
        if (crashDetailBean2.r != crashDetailBean.r) {
            String str3 = crashDetailBean2.s;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(crashDetailBean.r);
            if (!str3.contains(sb2.toString())) {
                crashDetailBean2.t++;
                crashDetailBean2.s += crashDetailBean.r + IOUtils.LINE_SEPARATOR_UNIX;
            }
        }
        return crashDetailBean2;
    }

    public final boolean a(CrashDetailBean crashDetailBean) {
        return a(crashDetailBean, -123456789);
    }

    public final boolean a(CrashDetailBean crashDetailBean, int i) throws Throwable {
        int i2 = crashDetailBean.b;
        String str = crashDetailBean.n;
        String str2 = crashDetailBean.p;
        String str3 = crashDetailBean.q;
        long j = crashDetailBean.r;
        String str4 = crashDetailBean.m;
        String str5 = crashDetailBean.e;
        String str6 = crashDetailBean.c;
        if (this.e != null && !this.e.c()) {
            return true;
        }
        if (crashDetailBean.b != 2) {
            q qVar = new q();
            qVar.b = 1;
            qVar.c = crashDetailBean.z;
            qVar.d = crashDetailBean.A;
            qVar.e = crashDetailBean.r;
            o oVar = this.c;
            o.b(1);
            this.c.a(qVar);
            w.b("[crash] a crash occur, handling...", new Object[0]);
        } else {
            w.b("[crash] a caught exception occur, handling...", new Object[0]);
        }
        List<a> listB = b();
        ArrayList arrayList = null;
        if (listB != null && listB.size() > 0) {
            arrayList = new ArrayList(10);
            ArrayList arrayList2 = new ArrayList(10);
            arrayList.addAll(a(listB));
            listB.removeAll(arrayList);
            if (!com.tencent.bugly.b.b && c.c) {
                boolean z = false;
                for (a aVar : listB) {
                    if (crashDetailBean.u.equals(aVar.c)) {
                        if (aVar.e) {
                            z = true;
                        }
                        arrayList2.add(aVar);
                    }
                }
                if (z || arrayList2.size() + 1 >= 5) {
                    w.a("same crash occur too much do merged!", new Object[0]);
                    CrashDetailBean crashDetailBeanA = a(arrayList2, crashDetailBean);
                    crashDetailBeanA.a = -1L;
                    c(crashDetailBeanA);
                    arrayList.addAll(arrayList2);
                    c(arrayList);
                    w.b("[crash] save crash success. this device crash many times, won't upload crashes immediately", new Object[0]);
                    return true;
                }
            }
        }
        c(crashDetailBean);
        c(arrayList);
        w.b("[crash] save crash success", new Object[0]);
        return false;
    }

    public final List<CrashDetailBean> a() {
        StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (strategyBeanC == null) {
            w.d("have not synced remote!", new Object[0]);
            return null;
        }
        if (!strategyBeanC.d) {
            w.d("Crashreport remote closed, please check your APPID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            w.b("[init] WARNING! Crashreport closed by server, please check your APPID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            return null;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jC = com.tencent.bugly.proguard.a.c();
        List<a> listB = b();
        if (listB == null || listB.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<a> it = listB.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.b < jC - c.f) {
                it.remove();
                arrayList.add(next);
            } else if (next.d) {
                if (next.b >= jCurrentTimeMillis - 86400000) {
                    it.remove();
                } else if (!next.e) {
                    it.remove();
                    arrayList.add(next);
                }
            } else if (next.f >= 3 && next.b < jCurrentTimeMillis - 86400000) {
                it.remove();
                arrayList.add(next);
            }
        }
        if (arrayList.size() > 0) {
            c(arrayList);
        }
        ArrayList arrayList2 = new ArrayList();
        List<CrashDetailBean> listB2 = b(listB);
        if (listB2 != null && listB2.size() > 0) {
            String str = com.tencent.bugly.crashreport.common.info.a.a().i;
            Iterator<CrashDetailBean> it2 = listB2.iterator();
            while (it2.hasNext()) {
                CrashDetailBean next2 = it2.next();
                if (!str.equals(next2.f)) {
                    it2.remove();
                    arrayList2.add(next2);
                }
            }
        }
        if (arrayList2.size() > 0) {
            d(arrayList2);
        }
        return listB2;
    }

    public final void a(CrashDetailBean crashDetailBean, long j, boolean z) {
        w.a("try to upload right now", new Object[0]);
        ArrayList arrayList = new ArrayList();
        arrayList.add(crashDetailBean);
        if (crashDetailBean.b == 7) {
            a(arrayList, 3000L, z, true);
        } else {
            a(arrayList, 3000L, z, false);
        }
        int i = crashDetailBean.b;
        if (this.e != null) {
            n nVar = this.e;
        }
    }

    public final void a(final List<CrashDetailBean> list, long j, boolean z, boolean z2) {
        ar arVar;
        if (!this.d.c().d) {
            w.d("remote report is disable!", new Object[0]);
            w.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
            return;
        }
        if (list == null || list.size() == 0) {
            return;
        }
        try {
            Context context = this.b;
            com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a();
            if (context == null || list == null || list.size() == 0 || aVarA == null) {
                w.d("enEXPPkg args == null!", new Object[0]);
                arVar = null;
            } else {
                aVarA.s();
                arVar = new ar();
                arVar.a = new ArrayList<>();
                Iterator<CrashDetailBean> it = list.iterator();
                while (it.hasNext()) {
                    arVar.a.add(a(context, it.next(), aVarA));
                }
            }
            if (arVar == null) {
                w.d("create eupPkg fail!", new Object[0]);
                return;
            }
            byte[] bArrA = com.tencent.bugly.proguard.a.a(arVar);
            if (bArrA == null) {
                w.d("send encode fail!", new Object[0]);
                return;
            }
            as asVarA = com.tencent.bugly.proguard.a.a(this.b, 630, bArrA);
            if (asVarA == null) {
                w.d("request package is null.", new Object[0]);
                return;
            }
            s sVar = new s() { // from class: com.tencent.bugly.crashreport.crash.b.1
                @Override // com.tencent.bugly.proguard.s
                public final void a(boolean z3) {
                    b bVar = b.this;
                    b.a(z3, (List<CrashDetailBean>) list);
                }
            };
            if (z) {
                if (!z2) {
                    t.a().a(a, asVarA, null, sVar, true, j, false);
                } else {
                    t.a().a(a, asVarA, null, sVar, true, j, true);
                }
                w.a("wake up!", new Object[0]);
                return;
            }
            if (!z2) {
                t.a().a(a, asVarA, (String) null, sVar, false);
            } else {
                t.a().a(a, asVarA, (String) null, sVar, true);
            }
        } catch (Throwable th) {
            w.e("req cr error %s", th.toString());
            if (w.b(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public static void a(boolean z, List<CrashDetailBean> list) {
        if (list != null && list.size() > 0) {
            w.c("up finish update state %b", Boolean.valueOf(z));
            for (CrashDetailBean crashDetailBean : list) {
                w.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                crashDetailBean.l++;
                crashDetailBean.d = z;
                w.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
            }
            Iterator<CrashDetailBean> it = list.iterator();
            while (it.hasNext()) {
                c.a().a(it.next());
            }
            w.c("update state size %d", Integer.valueOf(list.size()));
        }
        if (z) {
            return;
        }
        w.b("[crash] upload fail.", new Object[0]);
    }

    public final void b(CrashDetailBean crashDetailBean) {
        int i;
        Map<String, String> mapOnCrashHandleStart;
        String strSubstring;
        HashMap map;
        if (crashDetailBean == null) {
            return;
        }
        if (this.f == null && this.e == null) {
            return;
        }
        try {
            w.a("[crash callback] start user's callback:onCrashHandleStart()", new Object[0]);
            switch (crashDetailBean.b) {
                case 0:
                    i = 0;
                    break;
                case 1:
                    i = 2;
                    break;
                case 2:
                    i = 1;
                    break;
                case 3:
                    i = 4;
                    break;
                case 4:
                    i = 3;
                    break;
                case 5:
                    i = 5;
                    break;
                case 6:
                    i = 6;
                    break;
                case 7:
                    i = 7;
                    break;
                default:
                    return;
            }
            int i2 = crashDetailBean.b;
            String str = crashDetailBean.n;
            String str2 = crashDetailBean.p;
            String str3 = crashDetailBean.q;
            long j = crashDetailBean.r;
            byte[] bArrOnCrashHandleStart2GetExtraDatas = null;
            if (this.e != null) {
                n nVar = this.e;
                String strB = this.e.b();
                if (strB != null) {
                    map = new HashMap(1);
                    map.put("userData", strB);
                } else {
                    map = null;
                }
                mapOnCrashHandleStart = map;
            } else {
                mapOnCrashHandleStart = this.f != null ? this.f.onCrashHandleStart(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q) : null;
            }
            if (mapOnCrashHandleStart != null && mapOnCrashHandleStart.size() > 0) {
                crashDetailBean.N = new LinkedHashMap(mapOnCrashHandleStart.size());
                for (Map.Entry<String, String> entry : mapOnCrashHandleStart.entrySet()) {
                    String key = entry.getKey();
                    if (!(key == null || key.trim().length() <= 0)) {
                        String key2 = entry.getKey();
                        if (key2.length() > 100) {
                            key2 = key2.substring(0, 100);
                            w.d("setted key length is over limit %d substring to %s", 100, key2);
                        }
                        String value = entry.getValue();
                        if (!(value == null || value.trim().length() <= 0) && entry.getValue().length() > 30000) {
                            strSubstring = entry.getValue().substring(entry.getValue().length() - BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
                            w.d("setted %s value length is over limit %d substring", key2, Integer.valueOf(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH));
                        } else {
                            strSubstring = entry.getValue();
                        }
                        crashDetailBean.N.put(key2, strSubstring);
                        w.a("add setted key %s value size:%d", key2, Integer.valueOf(strSubstring.length()));
                    }
                }
            }
            w.a("[crash callback] start user's callback:onCrashHandleStart2GetExtraDatas()", new Object[0]);
            if (this.e != null) {
                bArrOnCrashHandleStart2GetExtraDatas = this.e.a();
            } else if (this.f != null) {
                bArrOnCrashHandleStart2GetExtraDatas = this.f.onCrashHandleStart2GetExtraDatas(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q);
            }
            crashDetailBean.S = bArrOnCrashHandleStart2GetExtraDatas;
            if (crashDetailBean.S != null) {
                if (crashDetailBean.S.length > 30000) {
                    w.d("extra bytes size %d is over limit %d will drop over part", Integer.valueOf(crashDetailBean.S.length), Integer.valueOf(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH));
                }
                w.a("add extra bytes %d ", Integer.valueOf(crashDetailBean.S.length));
            }
        } catch (Throwable th) {
            w.d("crash handle callback somthing wrong! %s", th.getClass().getName());
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static ContentValues d(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (crashDetailBean.a > 0) {
                contentValues.put("_id", Long.valueOf(crashDetailBean.a));
            }
            contentValues.put("_tm", Long.valueOf(crashDetailBean.r));
            contentValues.put("_s1", crashDetailBean.u);
            contentValues.put("_up", Integer.valueOf(crashDetailBean.d ? 1 : 0));
            contentValues.put("_me", Integer.valueOf(crashDetailBean.j ? 1 : 0));
            contentValues.put("_uc", Integer.valueOf(crashDetailBean.l));
            Parcel parcelObtain = Parcel.obtain();
            crashDetailBean.writeToParcel(parcelObtain, 0);
            byte[] bArrMarshall = parcelObtain.marshall();
            parcelObtain.recycle();
            contentValues.put("_dt", bArrMarshall);
            return contentValues;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static CrashDetailBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            CrashDetailBean crashDetailBean = (CrashDetailBean) com.tencent.bugly.proguard.a.a(blob, CrashDetailBean.CREATOR);
            if (crashDetailBean != null) {
                crashDetailBean.a = j;
            }
            return crashDetailBean;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public final void c(CrashDetailBean crashDetailBean) {
        ContentValues contentValuesD;
        if (crashDetailBean == null || (contentValuesD = d(crashDetailBean)) == null) {
            return;
        }
        long jA = o.a().a("t_cr", contentValuesD, (n) null, true);
        if (jA >= 0) {
            w.c("insert %s success!", "t_cr");
            crashDetailBean.a = jA;
        }
    }

    private List<CrashDetailBean> b(List<a> list) throws Throwable {
        Cursor cursorA;
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (a aVar : list) {
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(aVar.a);
        }
        String string = sb.toString();
        if (string.length() > 0) {
            string = string.substring(4);
        }
        String str = string;
        sb.setLength(0);
        try {
            cursorA = o.a().a("t_cr", null, str, null, null, true);
            if (cursorA == null) {
                if (cursorA != null && !cursorA.isClosed()) {
                    cursorA.close();
                }
                return null;
            }
            try {
                try {
                    ArrayList arrayList = new ArrayList();
                    while (cursorA.moveToNext()) {
                        CrashDetailBean crashDetailBeanA = a(cursorA);
                        if (crashDetailBeanA != null) {
                            arrayList.add(crashDetailBeanA);
                        } else {
                            try {
                                long j = cursorA.getLong(cursorA.getColumnIndex("_id"));
                                sb.append(" or _id");
                                sb.append(" = ");
                                sb.append(j);
                            } catch (Throwable unused) {
                                w.d("unknown id!", new Object[0]);
                            }
                        }
                    }
                    String string2 = sb.toString();
                    if (string2.length() > 0) {
                        w.d("deleted %s illegle data %d", "t_cr", Integer.valueOf(o.a().a("t_cr", string2.substring(4), (String[]) null, (n) null, true)));
                    }
                    if (cursorA != null && !cursorA.isClosed()) {
                        cursorA.close();
                    }
                    return arrayList;
                } catch (Throwable th) {
                    th = th;
                    if (cursorA != null && !cursorA.isClosed()) {
                        cursorA.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (!w.a(th)) {
                    th.printStackTrace();
                }
                if (cursorA != null && !cursorA.isClosed()) {
                    cursorA.close();
                }
                return null;
            }
        } catch (Throwable th3) {
            th = th3;
            cursorA = null;
            if (cursorA != null) {
                cursorA.close();
            }
            throw th;
        }
    }

    private static a b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            a aVar = new a();
            aVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            aVar.b = cursor.getLong(cursor.getColumnIndex("_tm"));
            aVar.c = cursor.getString(cursor.getColumnIndex("_s1"));
            aVar.d = cursor.getInt(cursor.getColumnIndex("_up")) == 1;
            aVar.e = cursor.getInt(cursor.getColumnIndex("_me")) == 1;
            aVar.f = cursor.getInt(cursor.getColumnIndex("_uc"));
            return aVar;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private List<a> b() throws Throwable {
        Cursor cursorA;
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                cursorA = o.a().a("t_cr", new String[]{"_id", "_tm", "_s1", "_up", "_me", "_uc"}, null, null, null, true);
                if (cursorA == null) {
                    if (cursorA != null && !cursorA.isClosed()) {
                        cursorA.close();
                    }
                    return null;
                }
                try {
                    try {
                        StringBuilder sb = new StringBuilder();
                        while (cursorA.moveToNext()) {
                            a aVarB = b(cursorA);
                            if (aVarB != null) {
                                arrayList.add(aVarB);
                            } else {
                                try {
                                    long j = cursorA.getLong(cursorA.getColumnIndex("_id"));
                                    sb.append(" or _id");
                                    sb.append(" = ");
                                    sb.append(j);
                                } catch (Throwable unused) {
                                    w.d("unknown id!", new Object[0]);
                                }
                            }
                        }
                        String string = sb.toString();
                        if (string.length() > 0) {
                            w.d("deleted %s illegle data %d", "t_cr", Integer.valueOf(o.a().a("t_cr", string.substring(4), (String[]) null, (n) null, true)));
                        }
                        if (cursorA != null && !cursorA.isClosed()) {
                            cursorA.close();
                        }
                        return arrayList;
                    } catch (Throwable th) {
                        th = th;
                        cursor = cursorA;
                        if (!w.a(th)) {
                            th.printStackTrace();
                        }
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        return arrayList;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursorA != null && !cursorA.isClosed()) {
                        cursorA.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                cursorA = cursor;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    private static void c(List<a> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (a aVar : list) {
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(aVar.a);
        }
        String string = sb.toString();
        if (string.length() > 0) {
            string = string.substring(4);
        }
        String str = string;
        sb.setLength(0);
        try {
            w.c("deleted %s data %d", "t_cr", Integer.valueOf(o.a().a("t_cr", str, (String[]) null, (n) null, true)));
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static void d(List<CrashDetailBean> list) {
        if (list != null) {
            try {
                if (list.size() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (CrashDetailBean crashDetailBean : list) {
                    sb.append(" or _id");
                    sb.append(" = ");
                    sb.append(crashDetailBean.a);
                }
                String string = sb.toString();
                if (string.length() > 0) {
                    string = string.substring(4);
                }
                sb.setLength(0);
                w.c("deleted %s data %d", "t_cr", Integer.valueOf(o.a().a("t_cr", string, (String[]) null, (n) null, true)));
            } catch (Throwable th) {
                if (w.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        }
    }

    private static aq a(Context context, CrashDetailBean crashDetailBean, com.tencent.bugly.crashreport.common.info.a aVar) throws Throwable {
        ap apVar;
        ap apVarA;
        ap apVarA2;
        if (context == null || crashDetailBean == null || aVar == null) {
            w.d("enExp args == null", new Object[0]);
            return null;
        }
        aq aqVar = new aq();
        switch (crashDetailBean.b) {
            case 0:
                aqVar.a = crashDetailBean.j ? "200" : "100";
                break;
            case 1:
                aqVar.a = crashDetailBean.j ? "201" : "101";
                break;
            case 2:
                aqVar.a = crashDetailBean.j ? "202" : "102";
                break;
            case 3:
                aqVar.a = crashDetailBean.j ? "203" : "103";
                break;
            case 4:
                aqVar.a = crashDetailBean.j ? "204" : "104";
                break;
            case 5:
                aqVar.a = crashDetailBean.j ? "207" : "107";
                break;
            case 6:
                aqVar.a = crashDetailBean.j ? "206" : "106";
                break;
            case 7:
                aqVar.a = crashDetailBean.j ? "208" : "108";
                break;
            default:
                w.e("crash type error! %d", Integer.valueOf(crashDetailBean.b));
                break;
        }
        aqVar.b = crashDetailBean.r;
        aqVar.c = crashDetailBean.n;
        aqVar.d = crashDetailBean.o;
        aqVar.e = crashDetailBean.p;
        aqVar.g = crashDetailBean.q;
        aqVar.h = crashDetailBean.y;
        aqVar.i = crashDetailBean.c;
        aqVar.j = null;
        aqVar.l = crashDetailBean.m;
        aqVar.m = crashDetailBean.e;
        aqVar.f = crashDetailBean.A;
        aqVar.t = com.tencent.bugly.crashreport.common.info.a.a().h();
        aqVar.n = null;
        if (crashDetailBean.i != null && crashDetailBean.i.size() > 0) {
            aqVar.o = new ArrayList<>();
            for (Map.Entry<String, PlugInBean> entry : crashDetailBean.i.entrySet()) {
                an anVar = new an();
                anVar.a = entry.getValue().a;
                anVar.c = entry.getValue().c;
                anVar.d = entry.getValue().b;
                anVar.b = aVar.q();
                aqVar.o.add(anVar);
            }
        }
        if (crashDetailBean.h != null && crashDetailBean.h.size() > 0) {
            aqVar.p = new ArrayList<>();
            for (Map.Entry<String, PlugInBean> entry2 : crashDetailBean.h.entrySet()) {
                an anVar2 = new an();
                anVar2.a = entry2.getValue().a;
                anVar2.c = entry2.getValue().c;
                anVar2.d = entry2.getValue().b;
                aqVar.p.add(anVar2);
            }
        }
        if (crashDetailBean.j) {
            aqVar.k = crashDetailBean.t;
            if (crashDetailBean.s != null && crashDetailBean.s.length() > 0) {
                if (aqVar.q == null) {
                    aqVar.q = new ArrayList<>();
                }
                try {
                    aqVar.q.add(new ap((byte) 1, "alltimes.txt", crashDetailBean.s.getBytes("utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    aqVar.q = null;
                }
            }
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(aqVar.k);
            objArr[1] = Integer.valueOf(aqVar.q != null ? aqVar.q.size() : 0);
            w.c("crashcount:%d sz:%d", objArr);
        }
        if (crashDetailBean.w != null) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            try {
                aqVar.q.add(new ap((byte) 1, "log.txt", crashDetailBean.w.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                aqVar.q = null;
            }
        }
        if (crashDetailBean.T != null || crashDetailBean.T.length() > 0) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            try {
                apVar = new ap((byte) 1, "crashInfos.txt", crashDetailBean.T.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
                apVar = null;
            }
            if (apVar != null) {
                w.c("attach crash infos", new Object[0]);
                aqVar.q.add(apVar);
            }
        }
        if (crashDetailBean.U != null) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            ap apVarA3 = a("backupRecord.zip", context, crashDetailBean.U);
            if (apVarA3 != null) {
                w.c("attach backup record", new Object[0]);
                aqVar.q.add(apVarA3);
            }
        }
        if (crashDetailBean.x != null && crashDetailBean.x.length > 0) {
            ap apVar2 = new ap((byte) 2, "buglylog.zip", crashDetailBean.x);
            w.c("attach user log", new Object[0]);
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            aqVar.q.add(apVar2);
        }
        if (crashDetailBean.b == 3) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            if (crashDetailBean.N != null && crashDetailBean.N.containsKey("BUGLY_CR_01")) {
                try {
                    aqVar.q.add(new ap((byte) 1, "anrMessage.txt", crashDetailBean.N.get("BUGLY_CR_01").getBytes("utf-8")));
                    w.c("attach anr message", new Object[0]);
                } catch (UnsupportedEncodingException e4) {
                    e4.printStackTrace();
                    aqVar.q = null;
                }
                crashDetailBean.N.remove("BUGLY_CR_01");
            }
            if (crashDetailBean.v != null && (apVarA2 = a("trace.zip", context, crashDetailBean.v)) != null) {
                w.c("attach traces", new Object[0]);
                aqVar.q.add(apVarA2);
            }
        }
        if (crashDetailBean.b == 1) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            if (crashDetailBean.v != null && (apVarA = a("tomb.zip", context, crashDetailBean.v)) != null) {
                w.c("attach tombs", new Object[0]);
                aqVar.q.add(apVarA);
            }
        }
        if (crashDetailBean.S != null && crashDetailBean.S.length > 0) {
            if (aqVar.q == null) {
                aqVar.q = new ArrayList<>();
            }
            aqVar.q.add(new ap((byte) 1, "userExtraByteData", crashDetailBean.S));
            w.c("attach extraData", new Object[0]);
        }
        aqVar.r = new HashMap();
        Map<String, String> map = aqVar.r;
        StringBuilder sb = new StringBuilder();
        sb.append(crashDetailBean.B);
        map.put("A9", sb.toString());
        Map<String, String> map2 = aqVar.r;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(crashDetailBean.C);
        map2.put("A11", sb2.toString());
        Map<String, String> map3 = aqVar.r;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(crashDetailBean.D);
        map3.put("A10", sb3.toString());
        aqVar.r.put("A23", crashDetailBean.f);
        aqVar.r.put("A7", aVar.e);
        aqVar.r.put("A6", aVar.r());
        aqVar.r.put("A5", aVar.q());
        aqVar.r.put("A22", aVar.g());
        Map<String, String> map4 = aqVar.r;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(crashDetailBean.F);
        map4.put("A2", sb4.toString());
        Map<String, String> map5 = aqVar.r;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(crashDetailBean.E);
        map5.put("A1", sb5.toString());
        aqVar.r.put("A24", aVar.g);
        Map<String, String> map6 = aqVar.r;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(crashDetailBean.G);
        map6.put("A17", sb6.toString());
        aqVar.r.put("A3", aVar.j());
        aqVar.r.put("A16", aVar.l());
        aqVar.r.put("A25", aVar.m());
        aqVar.r.put("A14", aVar.k());
        aqVar.r.put("A15", aVar.v());
        Map<String, String> map7 = aqVar.r;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(aVar.w());
        map7.put("A13", sb7.toString());
        aqVar.r.put("A34", crashDetailBean.z);
        if (aVar.w != null) {
            aqVar.r.put("productIdentify", aVar.w);
        }
        try {
            aqVar.r.put("A26", URLEncoder.encode(crashDetailBean.H, "utf-8"));
        } catch (UnsupportedEncodingException e5) {
            e5.printStackTrace();
        }
        if (crashDetailBean.b == 1) {
            aqVar.r.put("A27", crashDetailBean.J);
            aqVar.r.put("A28", crashDetailBean.I);
            Map<String, String> map8 = aqVar.r;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(crashDetailBean.k);
            map8.put("A29", sb8.toString());
        }
        aqVar.r.put("A30", crashDetailBean.K);
        Map<String, String> map9 = aqVar.r;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(crashDetailBean.L);
        map9.put("A18", sb9.toString());
        Map<String, String> map10 = aqVar.r;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(!crashDetailBean.M);
        map10.put("A36", sb10.toString());
        Map<String, String> map11 = aqVar.r;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(aVar.p);
        map11.put("F02", sb11.toString());
        Map<String, String> map12 = aqVar.r;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(aVar.q);
        map12.put("F03", sb12.toString());
        aqVar.r.put("F04", aVar.d());
        Map<String, String> map13 = aqVar.r;
        StringBuilder sb13 = new StringBuilder();
        sb13.append(aVar.r);
        map13.put("F05", sb13.toString());
        aqVar.r.put("F06", aVar.o);
        aqVar.r.put("F08", aVar.u);
        aqVar.r.put("F09", aVar.v);
        Map<String, String> map14 = aqVar.r;
        StringBuilder sb14 = new StringBuilder();
        sb14.append(aVar.s);
        map14.put("F10", sb14.toString());
        if (crashDetailBean.O >= 0) {
            Map<String, String> map15 = aqVar.r;
            StringBuilder sb15 = new StringBuilder();
            sb15.append(crashDetailBean.O);
            map15.put("C01", sb15.toString());
        }
        if (crashDetailBean.P >= 0) {
            Map<String, String> map16 = aqVar.r;
            StringBuilder sb16 = new StringBuilder();
            sb16.append(crashDetailBean.P);
            map16.put("C02", sb16.toString());
        }
        if (crashDetailBean.Q != null && crashDetailBean.Q.size() > 0) {
            for (Map.Entry<String, String> entry3 : crashDetailBean.Q.entrySet()) {
                aqVar.r.put("C03_" + entry3.getKey(), entry3.getValue());
            }
        }
        if (crashDetailBean.R != null && crashDetailBean.R.size() > 0) {
            for (Map.Entry<String, String> entry4 : crashDetailBean.R.entrySet()) {
                aqVar.r.put("C04_" + entry4.getKey(), entry4.getValue());
            }
        }
        aqVar.s = null;
        if (crashDetailBean.N != null && crashDetailBean.N.size() > 0) {
            aqVar.s = crashDetailBean.N;
            w.a("setted message size %d", Integer.valueOf(aqVar.s.size()));
        }
        Object[] objArr2 = new Object[12];
        objArr2[0] = crashDetailBean.n;
        objArr2[1] = crashDetailBean.c;
        objArr2[2] = aVar.d();
        objArr2[3] = Long.valueOf((crashDetailBean.r - crashDetailBean.L) / 1000);
        objArr2[4] = Boolean.valueOf(crashDetailBean.k);
        objArr2[5] = Boolean.valueOf(crashDetailBean.M);
        objArr2[6] = Boolean.valueOf(crashDetailBean.j);
        objArr2[7] = Boolean.valueOf(crashDetailBean.b == 1);
        objArr2[8] = Integer.valueOf(crashDetailBean.t);
        objArr2[9] = crashDetailBean.s;
        objArr2[10] = Boolean.valueOf(crashDetailBean.d);
        objArr2[11] = Integer.valueOf(aqVar.r.size());
        w.c("%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d", objArr2);
        return aqVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00ba A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r6v2, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.io.FileInputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.tencent.bugly.proguard.ap a(java.lang.String r5, android.content.Context r6, java.lang.String r7) throws java.lang.Throwable {
        /*
            r0 = 0
            r1 = 0
            if (r7 == 0) goto Ld9
            if (r6 != 0) goto L8
            goto Ld9
        L8:
            java.lang.String r2 = "zip %s"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]
            r4[r1] = r7
            com.tencent.bugly.proguard.w.c(r2, r4)
            java.io.File r2 = new java.io.File
            r2.<init>(r7)
            java.io.File r7 = new java.io.File
            java.io.File r6 = r6.getCacheDir()
            r7.<init>(r6, r5)
            r5 = 5000(0x1388, float:7.006E-42)
            boolean r5 = com.tencent.bugly.proguard.a.a(r2, r7, r5)
            if (r5 != 0) goto L30
            java.lang.String r5 = "zip fail!"
            java.lang.Object[] r6 = new java.lang.Object[r1]
            com.tencent.bugly.proguard.w.d(r5, r6)
            return r0
        L30:
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream
            r5.<init>()
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8b
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8b
            r2 = 1000(0x3e8, float:1.401E-42)
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
        L3e:
            int r4 = r6.read(r2)     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            if (r4 <= 0) goto L4b
            r5.write(r2, r1, r4)     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            r5.flush()     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            goto L3e
        L4b:
            byte[] r5 = r5.toByteArray()     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            java.lang.String r2 = "read bytes :%d"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            int r4 = r5.length     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            r3[r1] = r4     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            com.tencent.bugly.proguard.w.c(r2, r3)     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            com.tencent.bugly.proguard.ap r2 = new com.tencent.bugly.proguard.ap     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            r3 = 2
            java.lang.String r4 = r7.getName()     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            r2.<init>(r3, r4, r5)     // Catch: java.lang.Throwable -> L86 java.lang.Throwable -> Lb7
            r6.close()     // Catch: java.io.IOException -> L6b
            goto L75
        L6b:
            r5 = move-exception
            boolean r6 = com.tencent.bugly.proguard.w.a(r5)
            if (r6 != 0) goto L75
            r5.printStackTrace()
        L75:
            boolean r5 = r7.exists()
            if (r5 == 0) goto L85
            java.lang.String r5 = "del tmp"
            java.lang.Object[] r6 = new java.lang.Object[r1]
            com.tencent.bugly.proguard.w.c(r5, r6)
            r7.delete()
        L85:
            return r2
        L86:
            r5 = move-exception
            goto L8d
        L88:
            r5 = move-exception
            r6 = r0
            goto Lb8
        L8b:
            r5 = move-exception
            r6 = r0
        L8d:
            boolean r2 = com.tencent.bugly.proguard.w.a(r5)     // Catch: java.lang.Throwable -> Lb7
            if (r2 != 0) goto L96
            r5.printStackTrace()     // Catch: java.lang.Throwable -> Lb7
        L96:
            if (r6 == 0) goto La6
            r6.close()     // Catch: java.io.IOException -> L9c
            goto La6
        L9c:
            r5 = move-exception
            boolean r6 = com.tencent.bugly.proguard.w.a(r5)
            if (r6 != 0) goto La6
            r5.printStackTrace()
        La6:
            boolean r5 = r7.exists()
            if (r5 == 0) goto Lb6
            java.lang.String r5 = "del tmp"
            java.lang.Object[] r6 = new java.lang.Object[r1]
            com.tencent.bugly.proguard.w.c(r5, r6)
            r7.delete()
        Lb6:
            return r0
        Lb7:
            r5 = move-exception
        Lb8:
            if (r6 == 0) goto Lc8
            r6.close()     // Catch: java.io.IOException -> Lbe
            goto Lc8
        Lbe:
            r6 = move-exception
            boolean r0 = com.tencent.bugly.proguard.w.a(r6)
            if (r0 != 0) goto Lc8
            r6.printStackTrace()
        Lc8:
            boolean r6 = r7.exists()
            if (r6 == 0) goto Ld8
            java.lang.Object[] r6 = new java.lang.Object[r1]
            java.lang.String r0 = "del tmp"
            com.tencent.bugly.proguard.w.c(r0, r6)
            r7.delete()
        Ld8:
            throw r5
        Ld9:
            java.lang.String r5 = "rqdp{  createZipAttachment sourcePath == null || context == null ,pls check}"
            java.lang.Object[] r6 = new java.lang.Object[r1]
            com.tencent.bugly.proguard.w.d(r5, r6)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.a(java.lang.String, android.content.Context, java.lang.String):com.tencent.bugly.proguard.ap");
    }

    public static void a(String str, String str2, String str3, Thread thread, String str4, CrashDetailBean crashDetailBean) {
        String str5;
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a();
        if (aVarA == null) {
            return;
        }
        w.e("#++++++++++Record By Bugly++++++++++#", new Object[0]);
        w.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        boolean z = true;
        w.e("# PKG NAME: %s", aVarA.c);
        w.e("# APP VER: %s", aVarA.i);
        w.e("# LAUNCH TIME: %s", com.tencent.bugly.proguard.a.a(new Date(com.tencent.bugly.crashreport.common.info.a.a().a)));
        w.e("# CRASH TYPE: %s", str);
        w.e("# CRASH TIME: %s", str2);
        w.e("# CRASH PROCESS: %s", str3);
        if (thread != null) {
            w.e("# CRASH THREAD: %s", thread.getName());
        }
        if (crashDetailBean != null) {
            w.e("# REPORT ID: %s", crashDetailBean.c);
            Object[] objArr = new Object[2];
            objArr[0] = aVarA.f;
            objArr[1] = aVarA.w().booleanValue() ? "ROOTED" : "UNROOT";
            w.e("# CRASH DEVICE: %s %s", objArr);
            w.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
            w.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
            String str6 = crashDetailBean.J;
            if (!(str6 == null || str6.trim().length() <= 0)) {
                w.e("# EXCEPTION FIRED BY %s %s", crashDetailBean.J, crashDetailBean.I);
            } else if (crashDetailBean.b == 3) {
                Object[] objArr2 = new Object[1];
                if (crashDetailBean.N == null) {
                    str5 = "null";
                } else {
                    str5 = crashDetailBean.N.get("BUGLY_CR_01");
                }
                objArr2[0] = str5;
                w.e("# EXCEPTION ANR MESSAGE:\n %s", objArr2);
            }
        }
        if (str4 != null && str4.trim().length() > 0) {
            z = false;
        }
        if (!z) {
            w.e("# CRASH STACK: ", new Object[0]);
            w.e(str4, new Object[0]);
        }
        w.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }
}
