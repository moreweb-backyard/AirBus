package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class r {
    private static r b;
    public Map<String, String> a = null;
    private Context c;

    private r(Context context) {
        this.c = context;
    }

    public static r a(Context context) {
        if (b == null) {
            b = new r(context);
        }
        return b;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(8:(2:121|22)|(7:24|(1:26)(2:27|28)|115|29|113|30|36)(10:39|(1:49)(1:48)|(3:117|51|(5:123|53|133|54|60)(7:63|127|64|65|129|66|67))(1:75)|135|76|(1:78)|79|119|80|102)|92|93|(1:95)|125|96|102) */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x0188, code lost:
    
        if (com.tencent.bugly.proguard.w.a(r7) == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01b7, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0182, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0183, code lost:
    
        r7 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x018a A[PHI: r7 r10 r11 r12 r20
  0x018a: PHI (r7v1 java.lang.Throwable) = (r7v2 java.lang.Throwable), (r7v3 java.lang.Throwable) binds: [B:100:0x0188, B:84:0x0169] A[DONT_GENERATE, DONT_INLINE]
  0x018a: PHI (r10v4 int) = (r10v6 int), (r10v8 int) binds: [B:100:0x0188, B:84:0x0169] A[DONT_GENERATE, DONT_INLINE]
  0x018a: PHI (r11v6 boolean) = (r11v8 boolean), (r11v18 boolean) binds: [B:100:0x0188, B:84:0x0169] A[DONT_GENERATE, DONT_INLINE]
  0x018a: PHI (r12v7 java.lang.String) = (r12v9 java.lang.String), (r12v13 java.lang.String) binds: [B:100:0x0188, B:84:0x0169] A[DONT_GENERATE, DONT_INLINE]
  0x018a: PHI (r20v1 int) = (r20v3 int), (r20v8 int) binds: [B:100:0x0188, B:84:0x0169] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x017b A[Catch: all -> 0x016e, TRY_LEAVE, TryCatch #11 {all -> 0x016e, blocks: (B:22:0x0097, B:24:0x009f, B:29:0x00b4, B:93:0x0175, B:95:0x017b, B:27:0x00b0, B:51:0x00e2, B:53:0x00ea, B:64:0x0117, B:66:0x0123, B:76:0x013c, B:79:0x015c), top: B:121:0x0097 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final byte[] a(java.lang.String r22, byte[] r23, com.tencent.bugly.proguard.u r24, java.util.Map<java.lang.String, java.lang.String> r25) {
        /*
            Method dump skipped, instructions count: 440
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.r.a(java.lang.String, byte[], com.tencent.bugly.proguard.u, java.util.Map):byte[]");
    }

    private static Map<String, String> a(HttpURLConnection httpURLConnection) {
        HashMap map = new HashMap();
        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
        if (headerFields == null || headerFields.size() == 0) {
            return null;
        }
        for (String str : headerFields.keySet()) {
            List<String> list = headerFields.get(str);
            if (list.size() > 0) {
                map.put(str, list.get(0));
            }
        }
        return map;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x004f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] b(java.net.HttpURLConnection r5) throws java.lang.Throwable {
        /*
            r0 = 0
            if (r5 != 0) goto L4
            return r0
        L4:
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            java.io.InputStream r5 = r5.getInputStream()     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r5.<init>()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
        L16:
            int r3 = r1.read(r2)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            if (r3 <= 0) goto L21
            r4 = 0
            r5.write(r2, r4, r3)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            goto L16
        L21:
            r5.flush()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            byte[] r5 = r5.toByteArray()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r1.close()     // Catch: java.lang.Throwable -> L2c
            goto L30
        L2c:
            r0 = move-exception
            r0.printStackTrace()
        L30:
            return r5
        L31:
            r5 = move-exception
            goto L38
        L33:
            r5 = move-exception
            r1 = r0
            goto L4d
        L36:
            r5 = move-exception
            r1 = r0
        L38:
            boolean r2 = com.tencent.bugly.proguard.w.a(r5)     // Catch: java.lang.Throwable -> L4c
            if (r2 != 0) goto L41
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L4c
        L41:
            if (r1 == 0) goto L4b
            r1.close()     // Catch: java.lang.Throwable -> L47
            goto L4b
        L47:
            r5 = move-exception
            r5.printStackTrace()
        L4b:
            return r0
        L4c:
            r5 = move-exception
        L4d:
            if (r1 == 0) goto L57
            r1.close()     // Catch: java.lang.Throwable -> L53
            goto L57
        L53:
            r0 = move-exception
            r0.printStackTrace()
        L57:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.r.b(java.net.HttpURLConnection):byte[]");
    }

    private HttpURLConnection a(String str, byte[] bArr, String str2, Map<String, String> map) {
        if (str == null) {
            w.e("destUrl is null.", new Object[0]);
            return null;
        }
        HttpURLConnection httpURLConnectionA = a(str2, str);
        if (httpURLConnectionA == null) {
            w.e("Failed to get HttpURLConnection object.", new Object[0]);
            return null;
        }
        try {
            httpURLConnectionA.setRequestProperty("wup_version", "3.0");
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    httpURLConnectionA.setRequestProperty(entry.getKey(), URLEncoder.encode(entry.getValue(), "utf-8"));
                }
            }
            httpURLConnectionA.setRequestProperty("A37", URLEncoder.encode(str2, "utf-8"));
            httpURLConnectionA.setRequestProperty("A38", URLEncoder.encode(com.tencent.bugly.crashreport.common.info.b.e(this.c), "utf-8"));
            httpURLConnectionA.connect();
            OutputStream outputStream = httpURLConnectionA.getOutputStream();
            if (bArr == null) {
                outputStream.write(0);
            } else {
                outputStream.write(bArr);
            }
            return httpURLConnectionA;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            w.e("Failed to upload crash, please check your network.", new Object[0]);
            return null;
        }
    }

    private static HttpURLConnection a(String str, String str2) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(str2);
            if (str != null && str.toLowerCase(Locale.US).contains("wap")) {
                httpURLConnection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")))));
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpURLConnection.setConnectTimeout(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            return httpURLConnection;
        } catch (Throwable th) {
            if (w.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }
}
