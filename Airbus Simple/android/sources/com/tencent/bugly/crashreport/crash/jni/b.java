package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import com.litesuits.http.data.Consts;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.j;
import com.tencent.bugly.proguard.w;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class b {
    private StringBuilder a;
    private int b;

    private void d(String str) {
        for (int i = 0; i < this.b; i++) {
            this.a.append('\t');
        }
        if (str != null) {
            StringBuilder sb = this.a;
            sb.append(str);
            sb.append(": ");
        }
    }

    public b(StringBuilder sb, int i) {
        this.b = 0;
        this.a = sb;
        this.b = i;
    }

    public b a(boolean z, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(z ? 'T' : 'F');
        sb.append('\n');
        return this;
    }

    private static Map<String, Integer> c(String str) {
        if (str == null) {
            return null;
        }
        try {
            HashMap map = new HashMap();
            for (String str2 : str.split(Consts.SECOND_LEVEL_SPLIT)) {
                String[] strArrSplit = str2.split(":");
                if (strArrSplit.length != 2) {
                    w.e("error format at %s", str2);
                    return null;
                }
                map.put(strArrSplit[0], Integer.valueOf(Integer.parseInt(strArrSplit[1])));
            }
            return map;
        } catch (Exception e) {
            w.e("error format intStateStr %s", str);
            e.printStackTrace();
            return null;
        }
    }

    public b a(byte b, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append((int) b);
        sb.append('\n');
        return this;
    }

    public b a(char c, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(c);
        sb.append('\n');
        return this;
    }

    public b a(short s, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append((int) s);
        sb.append('\n');
        return this;
    }

    public b a(int i, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(i);
        sb.append('\n');
        return this;
    }

    protected static String a(String str) {
        if (str == null) {
            return "";
        }
        String[] strArrSplit = str.split(IOUtils.LINE_SEPARATOR_UNIX);
        if (strArrSplit == null || strArrSplit.length == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : strArrSplit) {
            if (!str2.contains("java.lang.Thread.getStackTrace(")) {
                sb.append(str2);
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
        }
        return sb.toString();
    }

    public b a(long j, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(j);
        sb.append('\n');
        return this;
    }

    public b a(float f, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(f);
        sb.append('\n');
        return this;
    }

    public b a(double d, String str) {
        d(str);
        StringBuilder sb = this.a;
        sb.append(d);
        sb.append('\n');
        return this;
    }

    private static CrashDetailBean a(Context context, Map<String, String> map, NativeExceptionHandler nativeExceptionHandler) {
        String str;
        String str2;
        HashMap map2;
        if (map == null) {
            return null;
        }
        if (com.tencent.bugly.crashreport.common.info.a.a(context) == null) {
            w.e("abnormal com info not created", new Object[0]);
            return null;
        }
        String str3 = map.get("intStateStr");
        if (str3 == null || str3.trim().length() <= 0) {
            w.e("no intStateStr", new Object[0]);
            return null;
        }
        Map<String, Integer> mapC = c(str3);
        if (mapC == null) {
            w.e("parse intSateMap fail", Integer.valueOf(map.size()));
            return null;
        }
        try {
            int iIntValue = mapC.get("ep").intValue();
            int iIntValue2 = mapC.get("et").intValue();
            mapC.get("sino").intValue();
            int iIntValue3 = mapC.get("sico").intValue();
            int iIntValue4 = mapC.get("spd").intValue();
            mapC.get("sud").intValue();
            long jIntValue = mapC.get("ets").intValue();
            long jIntValue2 = mapC.get("etms").intValue();
            String str4 = map.get("soVersion");
            if (str4 == null) {
                w.e("error format at version", new Object[0]);
                return null;
            }
            String str5 = map.get("errorAddr");
            if (str5 == null) {
                str5 = "unknown2";
            }
            String str6 = str5;
            String str7 = map.get("codeMsg");
            if (str7 == null) {
                str7 = "unknown2";
            }
            String str8 = map.get("tombPath");
            if (str8 == null) {
                str8 = "unknown2";
            }
            String str9 = str8;
            String str10 = map.get("signalName");
            if (str10 == null) {
                str10 = "unknown2";
            }
            map.get("errnoMsg");
            String str11 = map.get("stack");
            if (str11 == null) {
                str11 = "unknown2";
            }
            String str12 = map.get("jstack");
            if (str12 != null) {
                str11 = str11 + "java:\n" + str12;
            }
            long j = (jIntValue * 1000) + (jIntValue2 / 1000);
            String strA = a(str11);
            String str13 = map.get("sendingProcess");
            if (str13 == null) {
                str13 = "UNKNOWN";
            }
            String str14 = str13 + "(" + iIntValue4 + ")";
            if (iIntValue3 > 0) {
                str2 = str10 + "(" + str7 + ")";
                str = "KERNEL";
            } else {
                str = str7;
                str2 = str10;
            }
            String str15 = map.get("nativeLog");
            byte[] bArrA = (str15 == null || str15.isEmpty()) ? null : com.tencent.bugly.proguard.a.a((File) null, str15);
            String str16 = map.get("processName");
            if (str16 == null) {
                str16 = "unknown(" + iIntValue + ")";
            }
            String str17 = str16;
            String str18 = map.get("threadName");
            if (str18 == null) {
                str18 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            String str19 = str18 + "(" + iIntValue2 + ")";
            String str20 = map.get("key-value");
            if (str20 != null) {
                HashMap map3 = new HashMap();
                for (String str21 : str20.split(IOUtils.LINE_SEPARATOR_UNIX)) {
                    String[] strArrSplit = str21.split(Consts.EQUALS);
                    if (strArrSplit.length == 2) {
                        map3.put(strArrSplit[0], strArrSplit[1]);
                    }
                }
                map2 = map3;
            } else {
                map2 = null;
            }
            CrashDetailBean crashDetailBeanPackageCrashDatas = nativeExceptionHandler.packageCrashDatas(str17, str19, j, str2, str6, strA, str, str14, str9, str4, bArrA, map2, false);
            if (crashDetailBeanPackageCrashDatas != null) {
                String str22 = map.get("userId");
                if (str22 != null) {
                    crashDetailBeanPackageCrashDatas.m = str22;
                }
                String str23 = map.get("sysLog");
                if (str23 != null) {
                    crashDetailBeanPackageCrashDatas.w = str23;
                }
                String str24 = map.get("appVersion");
                if (str24 != null) {
                    crashDetailBeanPackageCrashDatas.f = str24;
                }
                crashDetailBeanPackageCrashDatas.y = null;
                crashDetailBeanPackageCrashDatas.k = true;
            }
            return crashDetailBeanPackageCrashDatas;
        } catch (Throwable th) {
            w.e("error format", new Object[0]);
            th.printStackTrace();
            return null;
        }
    }

    public b b(String str, String str2) {
        d(str2);
        if (str == null) {
            this.a.append("null\n");
        } else {
            StringBuilder sb = this.a;
            sb.append(str);
            sb.append('\n');
        }
        return this;
    }

    public b a(byte[] bArr, String str) {
        d(str);
        if (bArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (bArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(bArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(bArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (byte b : bArr) {
            bVar.a(b, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    public b a(short[] sArr, String str) {
        d(str);
        if (sArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (sArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(sArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(sArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (short s : sArr) {
            bVar.a(s, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    public b a(int[] iArr, String str) {
        d(str);
        if (iArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (iArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(iArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(iArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (int i : iArr) {
            bVar.a(i, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    public b a(long[] jArr, String str) {
        d(str);
        if (jArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (jArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(jArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(jArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (long j : jArr) {
            bVar.a(j, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    public b a(float[] fArr, String str) {
        d(str);
        if (fArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (fArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(fArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(fArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (float f : fArr) {
            bVar.a(f, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    public b a(double[] dArr, String str) {
        d(str);
        if (dArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (dArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(dArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(dArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (double d : dArr) {
            bVar.a(d, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    private static String a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i = inputStream.read();
            if (i == -1) {
                return null;
            }
            if (i == 0) {
                return sb.toString();
            }
            sb.append((char) i);
        }
    }

    public <K, V> b a(Map<K, V> map, String str) {
        d(str);
        if (map == null) {
            this.a.append("null\n");
            return this;
        }
        if (map.isEmpty()) {
            StringBuilder sb = this.a;
            sb.append(map.size());
            sb.append(", {}\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(map.size());
        sb2.append(", {\n");
        b bVar = new b(this.a, this.b + 1);
        b bVar2 = new b(this.a, this.b + 2);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            bVar.a('(', (String) null);
            bVar2.a((b) entry.getKey(), (String) null);
            bVar2.a((b) entry.getValue(), (String) null);
            bVar.a(')', (String) null);
        }
        a('}', (String) null);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r7v6 */
    public static CrashDetailBean a(Context context, String str, NativeExceptionHandler nativeExceptionHandler) {
        FileInputStream fileInputStream;
        String str2;
        String strA;
        if (context == null || str == null || nativeExceptionHandler == null) {
            w.e("get eup record file args error", new Object[0]);
            return null;
        }
        File file = new File(str, "rqd_record.eup");
        if (file.exists()) {
            ?? CanRead = file.canRead();
            try {
                if (CanRead != 0) {
                    try {
                        fileInputStream = new FileInputStream(file);
                    } catch (IOException e) {
                        e = e;
                        fileInputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        CanRead = 0;
                        if (CanRead != 0) {
                            try {
                                CanRead.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                    try {
                        String strA2 = a(fileInputStream);
                        if (strA2 != null && strA2.equals("NATIVE_RQD_REPORT")) {
                            HashMap map = new HashMap();
                            loop0: while (true) {
                                str2 = null;
                                while (true) {
                                    strA = a(fileInputStream);
                                    if (strA == null) {
                                        break loop0;
                                    }
                                    if (str2 == null) {
                                        str2 = strA;
                                    }
                                }
                                map.put(str2, strA);
                            }
                            if (str2 != null) {
                                w.e("record not pair! drop! %s", str2);
                                try {
                                    fileInputStream.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                return null;
                            }
                            CrashDetailBean crashDetailBeanA = a(context, map, nativeExceptionHandler);
                            try {
                                fileInputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                            return crashDetailBeanA;
                        }
                        w.e("record read fail! %s", strA2);
                        try {
                            fileInputStream.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                        return null;
                    } catch (IOException e6) {
                        e = e6;
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        return null;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return null;
    }

    public <T> b a(T[] tArr, String str) {
        d(str);
        if (tArr == null) {
            this.a.append("null\n");
            return this;
        }
        if (tArr.length == 0) {
            StringBuilder sb = this.a;
            sb.append(tArr.length);
            sb.append(", []\n");
            return this;
        }
        StringBuilder sb2 = this.a;
        sb2.append(tArr.length);
        sb2.append(", [\n");
        b bVar = new b(this.a, this.b + 1);
        for (T t : tArr) {
            bVar.a((b) t, (String) null);
        }
        a(']', (String) null);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> b a(Collection<T> collection, String str) {
        if (collection == null) {
            d(str);
            this.a.append("null\t");
            return this;
        }
        return a(collection.toArray(), str);
    }

    private static String c(String str, String str2) throws IOException {
        BufferedReader bufferedReaderA = com.tencent.bugly.proguard.a.a(str, "reg_record.txt");
        try {
            if (bufferedReaderA == null) {
                return null;
            }
            try {
                StringBuilder sb = new StringBuilder();
                if (!bufferedReaderA.readLine().startsWith(str2)) {
                    if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e) {
                            w.a(e);
                        }
                    }
                    return null;
                }
                int i = 0;
                int i2 = 18;
                int length = 0;
                while (true) {
                    String line = bufferedReaderA.readLine();
                    if (line == null) {
                        break;
                    }
                    if (i % 4 == 0) {
                        if (i > 0) {
                            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                        }
                        sb.append("  ");
                    } else {
                        if (line.length() > 16) {
                            i2 = 28;
                        }
                        sb.append("                ".substring(0, i2 - length));
                    }
                    length = line.length();
                    sb.append(line);
                    i++;
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                String string = sb.toString();
                if (bufferedReaderA != null) {
                    try {
                        bufferedReaderA.close();
                    } catch (Exception e2) {
                        w.a(e2);
                    }
                }
                return string;
            } catch (Throwable th) {
                w.a(th);
                if (bufferedReaderA != null) {
                    try {
                        bufferedReaderA.close();
                    } catch (Exception e3) {
                        w.a(e3);
                    }
                }
                return null;
            }
        } catch (Throwable th2) {
            if (bufferedReaderA != null) {
                try {
                    bufferedReaderA.close();
                } catch (Exception e4) {
                    w.a(e4);
                }
            }
            throw th2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> b a(T t, String str) {
        if (t == 0) {
            this.a.append("null\n");
        } else if (t instanceof Byte) {
            a(((Byte) t).byteValue(), str);
        } else if (t instanceof Boolean) {
            a(((Boolean) t).booleanValue(), str);
        } else if (t instanceof Short) {
            a(((Short) t).shortValue(), str);
        } else if (t instanceof Integer) {
            a(((Integer) t).intValue(), str);
        } else if (t instanceof Long) {
            a(((Long) t).longValue(), str);
        } else if (t instanceof Float) {
            a(((Float) t).floatValue(), str);
        } else if (t instanceof Double) {
            a(((Double) t).doubleValue(), str);
        } else if (t instanceof String) {
            b((String) t, str);
        } else if (t instanceof Map) {
            a((Map) t, str);
        } else if (t instanceof List) {
            a((Collection) t, str);
        } else if (t instanceof j) {
            a((j) t, str);
        } else if (t instanceof byte[]) {
            a((byte[]) t, str);
        } else if (t instanceof boolean[]) {
            a((b) t, str);
        } else if (t instanceof short[]) {
            a((short[]) t, str);
        } else if (t instanceof int[]) {
            a((int[]) t, str);
        } else if (t instanceof long[]) {
            a((long[]) t, str);
        } else if (t instanceof float[]) {
            a((float[]) t, str);
        } else if (t instanceof double[]) {
            a((double[]) t, str);
        } else if (t.getClass().isArray()) {
            a((Object[]) t, str);
        } else {
            throw new com.tencent.bugly.proguard.b("write object error: unsupport type.");
        }
        return this;
    }

    private static String d(String str, String str2) throws IOException {
        BufferedReader bufferedReaderA = com.tencent.bugly.proguard.a.a(str, "map_record.txt");
        try {
            if (bufferedReaderA == null) {
                return null;
            }
            try {
                StringBuilder sb = new StringBuilder();
                if (!bufferedReaderA.readLine().startsWith(str2)) {
                    if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e) {
                            w.a(e);
                        }
                    }
                    return null;
                }
                while (true) {
                    String line = bufferedReaderA.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append("  ");
                    sb.append(line);
                    sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                }
                String string = sb.toString();
                if (bufferedReaderA != null) {
                    try {
                        bufferedReaderA.close();
                    } catch (Exception e2) {
                        w.a(e2);
                    }
                }
                return string;
            } catch (Throwable th) {
                w.a(th);
                if (bufferedReaderA != null) {
                    try {
                        bufferedReaderA.close();
                    } catch (Exception e3) {
                        w.a(e3);
                    }
                }
                return null;
            }
        } catch (Throwable th2) {
            if (bufferedReaderA != null) {
                try {
                    bufferedReaderA.close();
                } catch (Exception e4) {
                    w.a(e4);
                }
            }
            throw th2;
        }
    }

    public b a(j jVar, String str) {
        a('{', str);
        if (jVar == null) {
            StringBuilder sb = this.a;
            sb.append('\t');
            sb.append("null");
        } else {
            jVar.a(this.a, this.b + 1);
        }
        a('}', (String) null);
        return this;
    }

    public static String a(String str, String str2) throws IOException {
        if (str == null || str2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String strC = c(str, str2);
        if (strC != null && !strC.isEmpty()) {
            sb.append("Register infos:\n");
            sb.append(strC);
        }
        String strD = d(str, str2);
        if (strD != null && !strD.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
            sb.append("System SO infos:\n");
            sb.append(strD);
        }
        return sb.toString();
    }

    public static void b(String str) {
        File file = new File(str, "rqd_record.eup");
        if (file.exists() && file.canWrite()) {
            file.delete();
            w.c("delete record file %s", file.getAbsoluteFile());
        }
        File file2 = new File(str, "reg_record.txt");
        if (file2.exists() && file2.canWrite()) {
            file2.delete();
            w.c("delete record file %s", file2.getAbsoluteFile());
        }
        File file3 = new File(str, "map_record.txt");
        if (file3.exists() && file3.canWrite()) {
            file3.delete();
            w.c("delete record file %s", file3.getAbsoluteFile());
        }
        File file4 = new File(str, "backup_record.txt");
        if (file4.exists() && file4.canWrite()) {
            file4.delete();
            w.c("delete record file %s", file4.getAbsoluteFile());
        }
    }
}
