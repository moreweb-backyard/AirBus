package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import com.litesuits.http.data.Charsets;
import com.litesuits.http.data.Consts;
import com.litesuits.http.log.HttpLog;
import com.shima.smartbushome.assist.CrashHandle.DateUtil;
import com.tencent.bugly.crashreport.biz.UserInfoBean;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class a {
    protected HashMap<String, HashMap<String, byte[]>> a = new HashMap<>();
    protected String b;
    h c;
    private HashMap<String, Object> d;

    a() {
        new HashMap();
        this.d = new HashMap<>();
        this.b = Charsets.GBK;
        this.c = new h();
    }

    public static aw a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        aw awVar = new aw();
        awVar.a = userInfoBean.e;
        awVar.e = userInfoBean.j;
        awVar.d = userInfoBean.c;
        awVar.c = userInfoBean.d;
        awVar.g = com.tencent.bugly.crashreport.common.info.a.a().h();
        awVar.h = userInfoBean.o == 1;
        switch (userInfoBean.b) {
            case 1:
                awVar.b = (byte) 1;
                break;
            case 2:
                awVar.b = (byte) 4;
                break;
            case 3:
                awVar.b = (byte) 2;
                break;
            case 4:
                awVar.b = (byte) 3;
                break;
            default:
                if (userInfoBean.b >= 10 && userInfoBean.b < 20) {
                    awVar.b = (byte) userInfoBean.b;
                    break;
                } else {
                    w.e("unknown uinfo type %d ", Integer.valueOf(userInfoBean.b));
                    return null;
                }
                break;
        }
        awVar.f = new HashMap();
        if (userInfoBean.p >= 0) {
            Map<String, String> map = awVar.f;
            StringBuilder sb = new StringBuilder();
            sb.append(userInfoBean.p);
            map.put("C01", sb.toString());
        }
        if (userInfoBean.q >= 0) {
            Map<String, String> map2 = awVar.f;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(userInfoBean.q);
            map2.put("C02", sb2.toString());
        }
        if (userInfoBean.r != null && userInfoBean.r.size() > 0) {
            for (Map.Entry<String, String> entry : userInfoBean.r.entrySet()) {
                awVar.f.put("C03_" + entry.getKey(), entry.getValue());
            }
        }
        if (userInfoBean.s != null && userInfoBean.s.size() > 0) {
            for (Map.Entry<String, String> entry2 : userInfoBean.s.entrySet()) {
                awVar.f.put("C04_" + entry2.getKey(), entry2.getValue());
            }
        }
        Map<String, String> map3 = awVar.f;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(!userInfoBean.l);
        map3.put("A36", sb3.toString());
        Map<String, String> map4 = awVar.f;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(userInfoBean.g);
        map4.put("F02", sb4.toString());
        Map<String, String> map5 = awVar.f;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(userInfoBean.h);
        map5.put("F03", sb5.toString());
        awVar.f.put("F04", userInfoBean.j);
        Map<String, String> map6 = awVar.f;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(userInfoBean.i);
        map6.put("F05", sb6.toString());
        awVar.f.put("F06", userInfoBean.m);
        Map<String, String> map7 = awVar.f;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(userInfoBean.k);
        map7.put("F10", sb7.toString());
        w.c("summary type %d vm:%d", Byte.valueOf(awVar.b), Integer.valueOf(awVar.f.size()));
        return awVar;
    }

    public void a(String str) {
        this.b = str;
    }

    public static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        try {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.getBuffer().toString();
        } catch (Throwable th2) {
            if (w.a(th2)) {
                return "fail";
            }
            th2.printStackTrace();
            return "fail";
        }
    }

    public static String b() {
        try {
            return new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT, Locale.US).format(new Date());
        } catch (Exception unused) {
            return new Date().toString();
        }
    }

    public static String a(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayList.size(); i++) {
            String str = arrayList.get(i);
            if (str.equals("java.lang.Integer") || str.equals("int")) {
                str = "int32";
            } else if (str.equals("java.lang.Boolean") || str.equals("boolean")) {
                str = "bool";
            } else if (str.equals("java.lang.Byte") || str.equals("byte")) {
                str = "char";
            } else if (str.equals("java.lang.Double") || str.equals("double")) {
                str = "double";
            } else if (str.equals("java.lang.Float") || str.equals("float")) {
                str = "float";
            } else if (str.equals("java.lang.Long") || str.equals("long")) {
                str = "int64";
            } else if (str.equals("java.lang.Short") || str.equals("short")) {
                str = "short";
            } else {
                if (str.equals("java.lang.Character")) {
                    throw new IllegalArgumentException("can not support java.lang.Character");
                }
                if (str.equals("java.lang.String")) {
                    str = "string";
                } else if (str.equals("java.util.List")) {
                    str = "list";
                } else if (str.equals("java.util.Map")) {
                    str = "map";
                }
            }
            arrayList.set(i, str);
        }
        Collections.reverse(arrayList);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            String str2 = arrayList.get(i2);
            if (str2.equals("list")) {
                int i3 = i2 - 1;
                arrayList.set(i3, "<" + arrayList.get(i3));
                arrayList.set(0, arrayList.get(0) + ">");
            } else if (str2.equals("map")) {
                int i4 = i2 - 1;
                arrayList.set(i4, "<" + arrayList.get(i4) + Consts.SECOND_LEVEL_SPLIT);
                arrayList.set(0, arrayList.get(0) + ">");
            } else if (str2.equals("Array")) {
                int i5 = i2 - 1;
                arrayList.set(i5, "<" + arrayList.get(i5));
                arrayList.set(0, arrayList.get(0) + ">");
            }
        }
        Collections.reverse(arrayList);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next());
        }
        return stringBuffer.toString();
    }

    public <T> void a(String str, T t) {
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
        byte[] bArrA = k.a(iVar.a());
        HashMap<String, byte[]> map = new HashMap<>(1);
        ArrayList<String> arrayList = new ArrayList<>(1);
        a(arrayList, t);
        map.put(a(arrayList), bArrA);
        this.d.remove(str);
        this.a.put(str, map);
    }

    public static String a(Date date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT, Locale.US).format(date);
        } catch (Exception unused) {
            return new Date().toString();
        }
    }

    public static ax a(List<UserInfoBean> list, int i) {
        com.tencent.bugly.crashreport.common.info.a aVarA;
        if (list == null || list.size() == 0 || (aVarA = com.tencent.bugly.crashreport.common.info.a.a()) == null) {
            return null;
        }
        aVarA.s();
        ax axVar = new ax();
        axVar.b = aVarA.d;
        axVar.c = aVarA.g();
        ArrayList<aw> arrayList = new ArrayList<>();
        Iterator<UserInfoBean> it = list.iterator();
        while (it.hasNext()) {
            aw awVarA = a(it.next());
            if (awVarA != null) {
                arrayList.add(awVarA);
            }
        }
        axVar.d = arrayList;
        axVar.e = new HashMap();
        axVar.e.put("A7", aVarA.e);
        axVar.e.put("A6", aVarA.r());
        axVar.e.put("A5", aVarA.q());
        Map<String, String> map = axVar.e;
        StringBuilder sb = new StringBuilder();
        sb.append(aVarA.o());
        map.put("A2", sb.toString());
        Map<String, String> map2 = axVar.e;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(aVarA.o());
        map2.put("A1", sb2.toString());
        axVar.e.put("A24", aVarA.g);
        Map<String, String> map3 = axVar.e;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(aVarA.p());
        map3.put("A17", sb3.toString());
        axVar.e.put("A15", aVarA.v());
        Map<String, String> map4 = axVar.e;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(aVarA.w());
        map4.put("A13", sb4.toString());
        axVar.e.put("F08", aVarA.u);
        axVar.e.put("F09", aVarA.v);
        Map<String, String> mapC = aVarA.C();
        if (mapC != null && mapC.size() > 0) {
            for (Map.Entry<String, String> entry : mapC.entrySet()) {
                axVar.e.put("C04_" + entry.getKey(), entry.getValue());
            }
        }
        switch (i) {
            case 1:
                axVar.a = (byte) 1;
                return axVar;
            case 2:
                axVar.a = (byte) 2;
                return axVar;
            default:
                w.e("unknown up type %d ", Integer.valueOf(i));
                return null;
        }
    }

    public static byte[] a(byte[] bArr, int i, String str) {
        am akVar;
        if (bArr == null || i == -1) {
            return bArr;
        }
        try {
            if (i == 1) {
                akVar = new al();
            } else {
                akVar = i == 3 ? new ak() : null;
            }
            if (akVar == null) {
                return null;
            }
            akVar.a(str);
            return akVar.a(bArr);
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            w.d("encrytype %d %s", Integer.valueOf(i), str);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] a(java.io.File r6, java.lang.String r7) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 219
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.a.a(java.io.File, java.lang.String):byte[]");
    }

    public static <T extends j> T a(byte[] bArr, Class<T> cls) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        try {
            T tNewInstance = cls.newInstance();
            h hVar = new h(bArr);
            hVar.a("utf-8");
            tNewInstance.a(hVar);
            return tNewInstance;
        } catch (Throwable th) {
            if (!w.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static as a(Context context, int i, byte[] bArr) {
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a();
        StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (aVarA == null || strategyBeanC == null) {
            w.e("illigle access to create req pkg!", new Object[0]);
            return null;
        }
        try {
            as asVar = new as();
            synchronized (aVarA) {
                asVar.a = 1;
                asVar.b = aVarA.e();
                asVar.c = aVarA.c;
                asVar.d = aVarA.i;
                asVar.e = aVarA.j;
                aVarA.getClass();
                asVar.f = "2.2.0";
                asVar.g = i;
                if (bArr == null) {
                    bArr = "".getBytes();
                }
                asVar.h = bArr;
                asVar.i = aVarA.f;
                asVar.j = aVarA.g;
                asVar.k = new HashMap();
                asVar.l = aVarA.d();
                asVar.m = strategyBeanC.m;
                asVar.o = aVarA.g();
                asVar.p = com.tencent.bugly.crashreport.common.info.b.e(context);
                asVar.q = System.currentTimeMillis();
                asVar.r = aVarA.j();
                asVar.s = aVarA.i();
                asVar.t = aVarA.l();
                asVar.u = aVarA.k();
                asVar.v = aVarA.m();
                asVar.w = asVar.p;
                aVarA.getClass();
                asVar.n = "com.tencent.bugly";
            }
            Map<String, String> map = asVar.k;
            StringBuilder sb = new StringBuilder();
            sb.append(aVarA.y);
            map.put("F11", sb.toString());
            Map<String, String> map2 = asVar.k;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(aVarA.x);
            map2.put("F12", sb2.toString());
            asVar.k.put("G1", aVarA.t());
            asVar.k.put("G2", aVarA.I());
            asVar.k.put("G3", aVarA.J());
            asVar.k.put("G4", aVarA.K());
            asVar.k.put("G5", aVarA.L());
            asVar.k.put("G6", aVarA.M());
            asVar.k.put("G7", Long.toString(aVarA.N()));
            return asVar;
        } catch (Throwable th) {
            if (!w.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] a(byte[] bArr, int i) {
        if (bArr == null) {
            return bArr;
        }
        w.c("rqdp{  zp:} %s rqdp{  len:} %s", 2, Integer.valueOf(bArr.length));
        try {
            ah ahVarA = ag.a(2);
            if (ahVarA == null) {
                return null;
            }
            return ahVarA.a(bArr);
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] b(byte[] bArr, int i) {
        if (bArr == null || i == -1) {
            return bArr;
        }
        w.c("rqdp{  unzp:} %s rqdp{  len:} %s", Integer.valueOf(i), Integer.valueOf(bArr.length));
        try {
            ah ahVarA = ag.a(i);
            if (ahVarA == null) {
                return null;
            }
            return ahVarA.b(bArr);
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private void a(ArrayList<String> arrayList, Object obj) {
        if (obj.getClass().isArray()) {
            if (!obj.getClass().getComponentType().toString().equals("byte")) {
                throw new IllegalArgumentException("only byte[] is supported");
            }
            if (Array.getLength(obj) > 0) {
                arrayList.add("java.util.List");
                a(arrayList, Array.get(obj, 0));
                return;
            } else {
                arrayList.add("Array");
                arrayList.add("?");
                return;
            }
        }
        if (obj instanceof Array) {
            throw new IllegalArgumentException("can not support Array, please use List");
        }
        if (obj instanceof List) {
            arrayList.add("java.util.List");
            List list = (List) obj;
            if (list.size() > 0) {
                a(arrayList, list.get(0));
                return;
            } else {
                arrayList.add("?");
                return;
            }
        }
        if (obj instanceof Map) {
            arrayList.add("java.util.Map");
            Map map = (Map) obj;
            if (map.size() > 0) {
                Object next = map.keySet().iterator().next();
                Object obj2 = map.get(next);
                arrayList.add(next.getClass().getName());
                a(arrayList, obj2);
                return;
            }
            arrayList.add("?");
            arrayList.add("?");
            return;
        }
        arrayList.add(obj.getClass().getName());
    }

    public static byte[] a(byte[] bArr, int i, int i2, String str) {
        try {
            return b(a(bArr, 1, str), 2);
        } catch (Exception e) {
            if (w.a(e)) {
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] a(as asVar) {
        try {
            d dVar = new d();
            dVar.d();
            dVar.a("utf-8");
            dVar.b(1);
            dVar.b("RqdServer");
            dVar.c("sync");
            dVar.a("detail", (String) asVar);
            return dVar.a();
        } catch (Throwable th) {
            if (w.b(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static long c() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT_DATE, Locale.US);
            return simpleDateFormat.parse(simpleDateFormat.format(new Date())).getTime();
        } catch (Throwable th) {
            if (w.a(th)) {
                return -1L;
            }
            th.printStackTrace();
            return -1L;
        }
    }

    public byte[] a() {
        i iVar = new i(0);
        iVar.a(this.b);
        iVar.a((Map) this.a, 0);
        return k.a(iVar.a());
    }

    public static at a(byte[] bArr, boolean z) {
        if (bArr != null) {
            try {
                d dVar = new d();
                dVar.d();
                dVar.a("utf-8");
                dVar.a(bArr);
                Object objB = dVar.b("detail", (String) new at());
                at atVar = at.class.isInstance(objB) ? (at) at.class.cast(objB) : null;
                if (!z && atVar != null && atVar.c != null && atVar.c.length > 0) {
                    w.c("resp buf %d", Integer.valueOf(atVar.c.length));
                    atVar.c = a(atVar.c, 2, 1, StrategyBean.a);
                    if (atVar.c == null) {
                        w.e("resp sbuffer error!", new Object[0]);
                        return null;
                    }
                }
                return atVar;
            } catch (Throwable th) {
                if (!w.b(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String b(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public void a(byte[] bArr) {
        this.c.a(bArr);
        this.c.a(this.b);
        HashMap map = new HashMap(1);
        HashMap map2 = new HashMap(1);
        map2.put("", new byte[0]);
        map.put("", map2);
        this.a = this.c.a((Map) map, 0, false);
    }

    public static String c(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return HttpLog.NULL;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(bArr);
            return b(messageDigest.digest());
        } catch (Throwable th) {
            if (w.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static byte[] a(j jVar) {
        try {
            i iVar = new i();
            iVar.a("utf-8");
            jVar.a(iVar);
            return iVar.b();
        } catch (Throwable th) {
            if (w.b(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x00ed A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x00e3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(java.io.File r4, java.io.File r5, int r6) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 270
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.a.a(java.io.File, java.io.File, int):boolean");
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0095: MOVE (r4 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:50:0x0095 */
    public static ArrayList<String> a(Context context, String[] strArr) throws Throwable {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        BufferedReader bufferedReader3;
        ArrayList<String> arrayList = new ArrayList<>();
        if (com.tencent.bugly.crashreport.common.info.a.a(context).F()) {
            ArrayList<String> arrayList2 = new ArrayList<>();
            arrayList2.add(new String("unknown(low memory)"));
            return arrayList2;
        }
        BufferedReader bufferedReader4 = null;
        try {
            try {
                Process processExec = Runtime.getRuntime().exec(strArr);
                bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        arrayList.add(line);
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader2 = null;
                    }
                }
                bufferedReader2 = new BufferedReader(new InputStreamReader(processExec.getErrorStream()));
                while (true) {
                    try {
                        String line2 = bufferedReader2.readLine();
                        if (line2 != null) {
                            arrayList.add(line2);
                        } else {
                            try {
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (!w.a(th)) {
                            th.printStackTrace();
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        return null;
                    }
                }
                bufferedReader.close();
                try {
                    bufferedReader2.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedReader4 = bufferedReader3;
        }
    }

    public static String a(Context context, String str) throws Throwable {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        ArrayList<String> arrayListA = a(context, new String[]{"/system/bin/sh", "-c", "getprop " + str});
        return (arrayListA == null || arrayListA.size() <= 0) ? "fail" : arrayListA.get(0);
    }

    public static byte[] a(long j) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(j);
            return sb.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long d(byte[] bArr) {
        if (bArr == null) {
            return -1L;
        }
        try {
            return Long.parseLong(new String(bArr, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static Object a(String str, String str2, Object obj, Class<?>[] clsArr, Object[] objArr) throws NoSuchMethodException, SecurityException {
        try {
            Method declaredMethod = Class.forName(str).getDeclaredMethod(str2, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(null, objArr);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void a(Parcel parcel, Map<String, PlugInBean> map) {
        if (map == null || map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList arrayList = new ArrayList(size);
        ArrayList arrayList2 = new ArrayList(size);
        for (Map.Entry<String, PlugInBean> entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putInt("pluginNum", arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            bundle.putString("pluginKey" + i, (String) arrayList.get(i));
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            bundle.putString("pluginVal" + i2 + "plugInId", ((PlugInBean) arrayList2.get(i2)).a);
            bundle.putString("pluginVal" + i2 + "plugInUUID", ((PlugInBean) arrayList2.get(i2)).c);
            bundle.putString("pluginVal" + i2 + "plugInVersion", ((PlugInBean) arrayList2.get(i2)).b);
        }
        parcel.writeBundle(bundle);
    }

    public static Map<String, PlugInBean> a(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        HashMap map = null;
        if (bundle == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int iIntValue = ((Integer) bundle.get("pluginNum")).intValue();
        for (int i = 0; i < iIntValue; i++) {
            arrayList.add(bundle.getString("pluginKey" + i));
        }
        for (int i2 = 0; i2 < iIntValue; i2++) {
            arrayList2.add(new PlugInBean(bundle.getString("pluginVal" + i2 + "plugInId"), bundle.getString("pluginVal" + i2 + "plugInVersion"), bundle.getString("pluginVal" + i2 + "plugInUUID")));
        }
        if (arrayList.size() == arrayList2.size()) {
            map = new HashMap(arrayList.size());
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                map.put(arrayList.get(i3), PlugInBean.class.cast(arrayList2.get(i3)));
            }
        } else {
            w.e("map plugin parcel error!", new Object[0]);
        }
        return map;
    }

    public static void b(Parcel parcel, Map<String, String> map) {
        if (map == null || map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList<String> arrayList = new ArrayList<>(size);
        ArrayList<String> arrayList2 = new ArrayList<>(size);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("keys", arrayList);
        bundle.putStringArrayList("values", arrayList2);
        parcel.writeBundle(bundle);
    }

    public static Map<String, String> b(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        HashMap map = null;
        if (bundle == null) {
            return null;
        }
        ArrayList<String> stringArrayList = bundle.getStringArrayList("keys");
        ArrayList<String> stringArrayList2 = bundle.getStringArrayList("values");
        if (stringArrayList != null && stringArrayList2 != null && stringArrayList.size() == stringArrayList2.size()) {
            map = new HashMap(stringArrayList.size());
            for (int i = 0; i < stringArrayList.size(); i++) {
                map.put(stringArrayList.get(i), stringArrayList2.get(i));
            }
        } else {
            w.e("map parcel error!", new Object[0]);
        }
        return map;
    }

    public static <T> T a(byte[] bArr, Parcelable.Creator<T> creator) {
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.unmarshall(bArr, 0, bArr.length);
        parcelObtain.setDataPosition(0);
        try {
            try {
                T tCreateFromParcel = creator.createFromParcel(parcelObtain);
                if (parcelObtain != null) {
                    parcelObtain.recycle();
                }
                return tCreateFromParcel;
            } catch (Throwable th) {
                th.printStackTrace();
                if (parcelObtain == null) {
                    return null;
                }
                parcelObtain.recycle();
                return null;
            }
        } catch (Throwable th2) {
            if (parcelObtain != null) {
                parcelObtain.recycle();
            }
            throw th2;
        }
    }

    public static String a(Context context, int i, String str) throws Throwable {
        String[] strArr;
        Process process = null;
        if (!AppInfo.a(context, "android.permission.READ_LOGS")) {
            w.d("no read_log permission!", new Object[0]);
            return null;
        }
        if (str == null) {
            strArr = new String[]{"logcat", "-d", "-v", "threadtime"};
        } else {
            strArr = new String[]{"logcat", "-d", "-v", "threadtime", "-s", str};
        }
        StringBuilder sb = new StringBuilder();
        try {
            try {
                Process processExec = Runtime.getRuntime().exec(strArr);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                        if (i > 0 && sb.length() > i) {
                            sb.delete(0, sb.length() - i);
                        }
                    }
                    String string = sb.toString();
                    if (processExec != null) {
                        try {
                            processExec.getOutputStream().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            processExec.getInputStream().close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        try {
                            processExec.getErrorStream().close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return string;
                } catch (Throwable th) {
                    th = th;
                    process = processExec;
                    if (process != null) {
                        try {
                            process.getOutputStream().close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        try {
                            process.getInputStream().close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                        try {
                            process.getErrorStream().close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static Map<String, String> a(int i, boolean z) {
        HashMap map = new HashMap(12);
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces == null) {
            return null;
        }
        Thread.currentThread().getId();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            int i2 = 0;
            sb.setLength(0);
            if (entry.getValue() != null && entry.getValue().length != 0) {
                StackTraceElement[] value = entry.getValue();
                int length = value.length;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    StackTraceElement stackTraceElement = value[i2];
                    if (i > 0 && sb.length() >= i) {
                        sb.append("\n[Stack over limit size :" + i + " , has been cutted !]");
                        break;
                    }
                    sb.append(stackTraceElement.toString());
                    sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                    i2++;
                }
                map.put(entry.getKey().getName() + "(" + entry.getKey().getId() + ")", sb.toString());
            }
        }
        return map;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0055 A[Catch: all -> 0x0059, Exception -> 0x005b, TRY_ENTER, TryCatch #2 {Exception -> 0x005b, blocks: (B:7:0x001c, B:18:0x0035, B:19:0x0038, B:24:0x0055, B:29:0x005d), top: B:38:0x0006, outer: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized byte[] a(int r6) {
        /*
            java.lang.Class<com.tencent.bugly.proguard.a> r6 = com.tencent.bugly.proguard.a.class
            monitor-enter(r6)
            r0 = 16
            r1 = 0
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            java.io.DataInputStream r2 = new java.io.DataInputStream     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            java.lang.String r5 = "/dev/urandom"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L26
            r2.readFully(r0)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L52
            r2.close()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            monitor-exit(r6)
            return r0
        L21:
            r0 = move-exception
            goto L28
        L23:
            r0 = move-exception
            r2 = r1
            goto L53
        L26:
            r0 = move-exception
            r2 = r1
        L28:
            java.lang.String r3 = "Failed to read from /dev/urandom : %s"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L52
            r5 = 0
            r4[r5] = r0     // Catch: java.lang.Throwable -> L52
            com.tencent.bugly.proguard.w.e(r3, r4)     // Catch: java.lang.Throwable -> L52
            if (r2 == 0) goto L38
            r2.close()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
        L38:
            java.lang.String r0 = "AES"
            javax.crypto.KeyGenerator r0 = javax.crypto.KeyGenerator.getInstance(r0)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            r2 = 128(0x80, float:1.8E-43)
            java.security.SecureRandom r3 = new java.security.SecureRandom     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            r3.<init>()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            r0.init(r2, r3)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            javax.crypto.SecretKey r0 = r0.generateKey()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            byte[] r0 = r0.getEncoded()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            monitor-exit(r6)
            return r0
        L52:
            r0 = move-exception
        L53:
            if (r2 == 0) goto L5d
            r2.close()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
            goto L5d
        L59:
            r0 = move-exception
            goto L69
        L5b:
            r0 = move-exception
            goto L5e
        L5d:
            throw r0     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5b
        L5e:
            boolean r2 = com.tencent.bugly.proguard.w.b(r0)     // Catch: java.lang.Throwable -> L59
            if (r2 != 0) goto L67
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L59
        L67:
            monitor-exit(r6)
            return r1
        L69:
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.a.a(int):byte[]");
    }

    public static byte[] a(int i, byte[] bArr, byte[] bArr2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(i, secretKeySpec, new IvParameterSpec(bArr2));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            if (w.b(e)) {
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] b(int i, byte[] bArr, byte[] bArr2) throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            PublicKey publicKeyGeneratePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bArr2));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, publicKeyGeneratePublic);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            if (w.b(e)) {
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    public static boolean a(Context context, String str, long j) {
        w.c("[Util] try to lock file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            File file = new File(context.getFilesDir() + File.separator + str);
            if (file.exists()) {
                if (System.currentTimeMillis() - file.lastModified() < j) {
                    return false;
                }
                w.c("[Util] lock file(%s) is expired, unlock it", str);
                b(context, str);
            }
            if (!file.createNewFile()) {
                return false;
            }
            w.c("[Util] successfully locked file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return true;
        } catch (Throwable th) {
            w.a(th);
            return false;
        }
    }

    public static boolean b(Context context, String str) {
        w.c("[Util] try to unlock file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            File file = new File(context.getFilesDir() + File.separator + str);
            if (!file.exists()) {
                return true;
            }
            if (!file.delete()) {
                return false;
            }
            w.c("[Util] successfully unlocked file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return true;
        } catch (Throwable th) {
            w.a(th);
            return false;
        }
    }

    public static BufferedReader a(File file) {
        if (file == null || !file.exists() || !file.canRead()) {
            return null;
        }
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        } catch (Throwable th) {
            w.a(th);
            return null;
        }
    }

    public static BufferedReader a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            File file = new File(str, str2);
            if (file.exists() && file.canRead()) {
                return a(file);
            }
            return null;
        } catch (NullPointerException e) {
            w.a(e);
            return null;
        }
    }
}
