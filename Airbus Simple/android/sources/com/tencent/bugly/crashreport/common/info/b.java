package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.tencent.bugly.proguard.w;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {
    private static String a;
    private static String b;

    public static String a() {
        try {
            return Build.MODEL;
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String b() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static int c() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Throwable th) {
            if (w.a(th)) {
                return -1;
            }
            th.printStackTrace();
            return -1;
        }
    }

    public static String a(Context context) {
        String deviceId;
        if (!AppInfo.a(context, "android.permission.READ_PHONE_STATE")) {
            w.d("no READ_PHONE_STATE permission to get IMEI", new Object[0]);
            return "null";
        }
        if (context == null) {
            return null;
        }
        try {
            deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            if (deviceId == null) {
                return deviceId;
            }
            try {
                return deviceId.toLowerCase();
            } catch (Throwable unused) {
                Log.i(w.a, "failed to get IMEI");
                return deviceId;
            }
        } catch (Throwable unused2) {
            deviceId = null;
        }
    }

    public static String b(Context context) {
        String subscriberId;
        if (!AppInfo.a(context, "android.permission.READ_PHONE_STATE")) {
            w.d("no READ_PHONE_STATE permission to get IMSI", new Object[0]);
            return "null";
        }
        if (context == null) {
            return null;
        }
        try {
            subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
            if (subscriberId == null) {
                return subscriberId;
            }
            try {
                return subscriberId.toLowerCase();
            } catch (Throwable unused) {
                Log.i(w.a, "failed to get IMSI");
                return subscriberId;
            }
        } catch (Throwable unused2) {
            subscriberId = null;
        }
    }

    public static String c(Context context) {
        String str = "fail";
        if (context == null) {
            return "fail";
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (string == null) {
                return "null";
            }
            try {
                return string.toLowerCase();
            } catch (Throwable th) {
                str = string;
                th = th;
                if (w.a(th)) {
                    return str;
                }
                Log.i(w.a, "failed to get Android ID");
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String d(Context context) {
        String str = "fail";
        if (context == null) {
            return "fail";
        }
        try {
            String macAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (macAddress == null) {
                return "null";
            }
            try {
                return macAddress.toLowerCase();
            } catch (Throwable th) {
                str = macAddress;
                th = th;
                if (w.a(th)) {
                    return str;
                }
                th.printStackTrace();
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String p() throws java.lang.Throwable {
        /*
            java.lang.String r0 = "/system/build.prop"
            r1 = 0
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L72
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L72
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L6a
            r3 = 2048(0x800, float:2.87E-42)
            r0.<init>(r2, r3)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L6a
        Lf:
            java.lang.String r3 = r0.readLine()     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            r4 = 0
            if (r3 == 0) goto L3b
            java.lang.String r5 = "="
            r6 = 2
            java.lang.String[] r3 = r3.split(r5, r6)     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            int r5 = r3.length     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            if (r5 != r6) goto Lf
            r5 = r3[r4]     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            java.lang.String r6 = "ro.product.cpu.abilist"
            boolean r5 = r5.equals(r6)     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            r6 = 1
            if (r5 == 0) goto L2e
            r3 = r3[r6]     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            goto L3c
        L2e:
            r5 = r3[r4]     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            java.lang.String r7 = "ro.product.cpu.abi"
            boolean r5 = r5.equals(r7)     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            if (r5 == 0) goto Lf
            r3 = r3[r6]     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            goto L3c
        L3b:
            r3 = r1
        L3c:
            if (r3 == 0) goto L46
            java.lang.String r5 = ","
            java.lang.String[] r3 = r3.split(r5)     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
            r3 = r3[r4]     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L9f
        L46:
            r0.close()     // Catch: java.io.IOException -> L4a
            goto L54
        L4a:
            r0 = move-exception
            boolean r1 = com.tencent.bugly.proguard.w.a(r0)
            if (r1 != 0) goto L54
            r0.printStackTrace()
        L54:
            r2.close()     // Catch: java.io.IOException -> L58
            goto L62
        L58:
            r0 = move-exception
            boolean r1 = com.tencent.bugly.proguard.w.a(r0)
            if (r1 != 0) goto L62
            r0.printStackTrace()
        L62:
            return r3
        L63:
            r3 = move-exception
            goto L75
        L65:
            r0 = move-exception
            r8 = r1
            r1 = r0
            r0 = r8
            goto La0
        L6a:
            r3 = move-exception
            r0 = r1
            goto L75
        L6d:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r2
            goto La0
        L72:
            r3 = move-exception
            r0 = r1
            r2 = r0
        L75:
            boolean r4 = com.tencent.bugly.proguard.w.a(r3)     // Catch: java.lang.Throwable -> L9f
            if (r4 != 0) goto L7e
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L9f
        L7e:
            if (r0 == 0) goto L8e
            r0.close()     // Catch: java.io.IOException -> L84
            goto L8e
        L84:
            r0 = move-exception
            boolean r3 = com.tencent.bugly.proguard.w.a(r0)
            if (r3 != 0) goto L8e
            r0.printStackTrace()
        L8e:
            if (r2 == 0) goto L9e
            r2.close()     // Catch: java.io.IOException -> L94
            goto L9e
        L94:
            r0 = move-exception
            boolean r2 = com.tencent.bugly.proguard.w.a(r0)
            if (r2 != 0) goto L9e
            r0.printStackTrace()
        L9e:
            return r1
        L9f:
            r1 = move-exception
        La0:
            if (r0 == 0) goto Lb0
            r0.close()     // Catch: java.io.IOException -> La6
            goto Lb0
        La6:
            r0 = move-exception
            boolean r3 = com.tencent.bugly.proguard.w.a(r0)
            if (r3 != 0) goto Lb0
            r0.printStackTrace()
        Lb0:
            if (r2 == 0) goto Lc0
            r2.close()     // Catch: java.io.IOException -> Lb6
            goto Lc0
        Lb6:
            r0 = move-exception
            boolean r2 = com.tencent.bugly.proguard.w.a(r0)
            if (r2 != 0) goto Lc0
            r0.printStackTrace()
        Lc0:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.p():java.lang.String");
    }

    public static String d() {
        try {
            String strP = p();
            if (strP == null) {
                strP = System.getProperty("os.arch");
            }
            return strP;
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static long e() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return -1L;
        }
    }

    public static long f() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return -1L;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.io.BufferedReader] */
    public static long g() throws Throwable {
        FileReader fileReader;
        Throwable th;
        BufferedReader bufferedReader;
        Throwable th2;
        ?? r0 = "/proc/meminfo";
        try {
            try {
                fileReader = new FileReader("/proc/meminfo");
            } catch (Throwable th3) {
                fileReader = null;
                th = th3;
                bufferedReader = null;
            }
            try {
                bufferedReader = new BufferedReader(fileReader, 2048);
            } catch (Throwable th4) {
                th2 = th4;
                r0 = 0;
                if (r0 != 0) {
                    try {
                        r0.close();
                    } catch (IOException e) {
                        if (!w.a(e)) {
                            e.printStackTrace();
                        }
                    }
                }
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e2) {
                        if (!w.a(e2)) {
                            e2.printStackTrace();
                        }
                    }
                }
                throw th2;
            }
            try {
                long j = Long.parseLong(bufferedReader.readLine().split(":\\s+", 2)[1].toLowerCase().replace("kb", "").trim()) << 10;
                try {
                    bufferedReader.close();
                } catch (IOException e3) {
                    if (!w.a(e3)) {
                        e3.printStackTrace();
                    }
                }
                try {
                    fileReader.close();
                } catch (IOException e4) {
                    if (!w.a(e4)) {
                        e4.printStackTrace();
                    }
                }
                return j;
            } catch (Throwable th5) {
                th = th5;
                if (!w.a(th)) {
                    th.printStackTrace();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                        if (!w.a(e5)) {
                            e5.printStackTrace();
                        }
                    }
                }
                if (fileReader == null) {
                    return -2L;
                }
                try {
                    fileReader.close();
                    return -2L;
                } catch (IOException e6) {
                    if (w.a(e6)) {
                        return -2L;
                    }
                    e6.printStackTrace();
                    return -2L;
                }
            }
        } catch (Throwable th6) {
            th2 = th6;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.io.BufferedReader] */
    public static long h() throws Throwable {
        FileReader fileReader;
        Throwable th;
        Throwable th2;
        BufferedReader bufferedReader;
        ?? r0 = "/proc/meminfo";
        try {
            try {
                fileReader = new FileReader("/proc/meminfo");
            } catch (Throwable th3) {
                fileReader = null;
                th = th3;
                r0 = 0;
            }
            try {
                bufferedReader = new BufferedReader(fileReader, 2048);
            } catch (Throwable th4) {
                th2 = th4;
                bufferedReader = null;
            }
            try {
                bufferedReader.readLine();
                long j = (Long.parseLong(bufferedReader.readLine().split(":\\s+", 2)[1].toLowerCase().replace("kb", "").trim()) << 10) + (Long.parseLong(bufferedReader.readLine().split(":\\s+", 2)[1].toLowerCase().replace("kb", "").trim()) << 10) + (Long.parseLong(bufferedReader.readLine().split(":\\s+", 2)[1].toLowerCase().replace("kb", "").trim()) << 10);
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    if (!w.a(e)) {
                        e.printStackTrace();
                    }
                }
                try {
                    fileReader.close();
                } catch (IOException e2) {
                    if (!w.a(e2)) {
                        e2.printStackTrace();
                    }
                }
                return j;
            } catch (Throwable th5) {
                th2 = th5;
                if (!w.a(th2)) {
                    th2.printStackTrace();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e3) {
                        if (!w.a(e3)) {
                            e3.printStackTrace();
                        }
                    }
                }
                if (fileReader == null) {
                    return -2L;
                }
                try {
                    fileReader.close();
                    return -2L;
                } catch (IOException e4) {
                    if (w.a(e4)) {
                        return -2L;
                    }
                    e4.printStackTrace();
                    return -2L;
                }
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    public static long i() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return 0L;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (w.a(th)) {
                return -2L;
            }
            th.printStackTrace();
            return -2L;
        }
    }

    public static long j() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return 0L;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (w.a(th)) {
                return -2L;
            }
            th.printStackTrace();
            return -2L;
        }
    }

    public static String k() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String l() {
        try {
            return Build.BRAND;
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String e(Context context) {
        NetworkInfo activeNetworkInfo;
        TelephonyManager telephonyManager;
        String str = EnvironmentCompat.MEDIA_UNKNOWN;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            if (!w.a(e)) {
                e.printStackTrace();
            }
        }
        if (activeNetworkInfo == null) {
            return null;
        }
        if (activeNetworkInfo.getType() == 1) {
            return "WIFI";
        }
        if (activeNetworkInfo.getType() == 0 && (telephonyManager = (TelephonyManager) context.getSystemService("phone")) != null) {
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case 1:
                    return "GPRS";
                case 2:
                    return "EDGE";
                case 3:
                    return "UMTS";
                case 4:
                    return "CDMA";
                case 5:
                    return "EVDO_0";
                case 6:
                    return "EVDO_A";
                case 7:
                    return "1xRTT";
                case 8:
                    return "HSDPA";
                case 9:
                    return "HSUPA";
                case 10:
                    return "HSPA";
                case 11:
                    return "iDen";
                case 12:
                    return "EVDO_B";
                case 13:
                    return "LTE";
                case 14:
                    return "eHRPD";
                case 15:
                    return "HSPA+";
                default:
                    str = "MOBILE(" + networkType + ")";
                    break;
            }
        }
        return str;
    }

    public static String f(Context context) throws Throwable {
        String strA = com.tencent.bugly.proguard.a.a(context, "ro.miui.ui.version.name");
        if (!(strA == null || strA.trim().length() <= 0) && !strA.equals("fail")) {
            return "XiaoMi/MIUI/" + strA;
        }
        String strA2 = com.tencent.bugly.proguard.a.a(context, "ro.build.version.emui");
        if (!(strA2 == null || strA2.trim().length() <= 0) && !strA2.equals("fail")) {
            return "HuaWei/EMOTION/" + strA2;
        }
        String strA3 = com.tencent.bugly.proguard.a.a(context, "ro.lenovo.series");
        if (!(strA3 == null || strA3.trim().length() <= 0) && !strA3.equals("fail")) {
            return "Lenovo/VIBE/" + com.tencent.bugly.proguard.a.a(context, "ro.build.version.incremental");
        }
        String strA4 = com.tencent.bugly.proguard.a.a(context, "ro.build.nubia.rom.name");
        if (!(strA4 == null || strA4.trim().length() <= 0) && !strA4.equals("fail")) {
            return "Zte/NUBIA/" + strA4 + "_" + com.tencent.bugly.proguard.a.a(context, "ro.build.nubia.rom.code");
        }
        String strA5 = com.tencent.bugly.proguard.a.a(context, "ro.meizu.product.model");
        if (!(strA5 == null || strA5.trim().length() <= 0) && !strA5.equals("fail")) {
            return "Meizu/FLYME/" + com.tencent.bugly.proguard.a.a(context, "ro.build.display.id");
        }
        String strA6 = com.tencent.bugly.proguard.a.a(context, "ro.build.version.opporom");
        if (!(strA6 == null || strA6.trim().length() <= 0) && !strA6.equals("fail")) {
            return "Oppo/COLOROS/" + strA6;
        }
        String strA7 = com.tencent.bugly.proguard.a.a(context, "ro.vivo.os.build.display.id");
        if (!(strA7 == null || strA7.trim().length() <= 0) && !strA7.equals("fail")) {
            return "vivo/FUNTOUCH/" + strA7;
        }
        String strA8 = com.tencent.bugly.proguard.a.a(context, "ro.aa.romver");
        if (!(strA8 == null || strA8.trim().length() <= 0) && !strA8.equals("fail")) {
            return "htc/" + strA8 + "/" + com.tencent.bugly.proguard.a.a(context, "ro.build.description");
        }
        String strA9 = com.tencent.bugly.proguard.a.a(context, "ro.lewa.version");
        if (!(strA9 == null || strA9.trim().length() <= 0) && !strA9.equals("fail")) {
            return "tcl/" + strA9 + "/" + com.tencent.bugly.proguard.a.a(context, "ro.build.display.id");
        }
        String strA10 = com.tencent.bugly.proguard.a.a(context, "ro.gn.gnromvernumber");
        if (!(strA10 == null || strA10.trim().length() <= 0) && !strA10.equals("fail")) {
            return "amigo/" + strA10 + "/" + com.tencent.bugly.proguard.a.a(context, "ro.build.display.id");
        }
        String strA11 = com.tencent.bugly.proguard.a.a(context, "ro.build.tyd.kbstyle_version");
        if (!(strA11 == null || strA11.trim().length() <= 0) && !strA11.equals("fail")) {
            return "dido/" + strA11;
        }
        return com.tencent.bugly.proguard.a.a(context, "ro.build.fingerprint") + "/" + com.tencent.bugly.proguard.a.a(context, "ro.build.rom.id");
    }

    public static String g(Context context) {
        return com.tencent.bugly.proguard.a.a(context, "ro.board.platform");
    }

    public static boolean h(Context context) throws Throwable {
        boolean zExists;
        boolean z = Build.TAGS != null && Build.TAGS.contains("test-keys");
        try {
            zExists = new File("/system/app/Superuser.apk").exists();
        } catch (Throwable unused) {
            zExists = false;
        }
        Boolean bool = null;
        ArrayList<String> arrayListA = com.tencent.bugly.proguard.a.a(context, new String[]{"/system/bin/sh", "-c", "type su"});
        if (arrayListA != null && arrayListA.size() > 0) {
            Iterator<String> it = arrayListA.iterator();
            while (it.hasNext()) {
                String next = it.next();
                w.c(next, new Object[0]);
                if (next.contains("not found")) {
                    bool = false;
                }
            }
            if (bool == null) {
                bool = true;
            }
        }
        return z || zExists || Boolean.valueOf(bool == null ? false : bool.booleanValue()).booleanValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x00a3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String m() throws java.lang.Throwable {
        /*
            r0 = 0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            r1.<init>()     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            java.io.File r2 = new java.io.File     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            java.lang.String r3 = "/sys/block/mmcblk0/device/type"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            boolean r2 = r2.exists()     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            if (r2 == 0) goto L2c
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            java.io.FileReader r3 = new java.io.FileReader     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            java.lang.String r4 = "/sys/block/mmcblk0/device/type"
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9c
            java.lang.String r3 = r2.readLine()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            if (r3 == 0) goto L28
            r1.append(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
        L28:
            r2.close()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            goto L2d
        L2c:
            r2 = r0
        L2d:
            java.lang.String r3 = ","
            r1.append(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r4 = "/sys/block/mmcblk0/device/name"
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            boolean r3 = r3.exists()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            if (r3 == 0) goto L5f
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.io.FileReader r4 = new java.io.FileReader     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r5 = "/sys/block/mmcblk0/device/name"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> L59 java.lang.Throwable -> L5c
            if (r2 == 0) goto L54
            r1.append(r2)     // Catch: java.lang.Throwable -> L59 java.lang.Throwable -> L5c
        L54:
            r3.close()     // Catch: java.lang.Throwable -> L59 java.lang.Throwable -> L5c
            r2 = r3
            goto L5f
        L59:
            r0 = move-exception
            r2 = r3
            goto Lad
        L5c:
            r1 = move-exception
            r2 = r3
            goto L9e
        L5f:
            java.lang.String r3 = ","
            r1.append(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r4 = "/sys/block/mmcblk0/device/cid"
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            boolean r3 = r3.exists()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            if (r3 == 0) goto L87
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.io.FileReader r4 = new java.io.FileReader     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r5 = "/sys/block/mmcblk0/device/cid"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> L59 java.lang.Throwable -> L5c
            if (r2 == 0) goto L86
            r1.append(r2)     // Catch: java.lang.Throwable -> L59 java.lang.Throwable -> L5c
        L86:
            r2 = r3
        L87:
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> Lac
            if (r2 == 0) goto L95
            r2.close()     // Catch: java.io.IOException -> L91
            goto L95
        L91:
            r0 = move-exception
            com.tencent.bugly.proguard.w.a(r0)
        L95:
            return r1
        L96:
            r1 = move-exception
            goto L9e
        L98:
            r1 = move-exception
            r2 = r0
            r0 = r1
            goto Lad
        L9c:
            r1 = move-exception
            r2 = r0
        L9e:
            com.tencent.bugly.proguard.w.a(r1)     // Catch: java.lang.Throwable -> Lac
            if (r2 == 0) goto Lab
            r2.close()     // Catch: java.io.IOException -> La7
            goto Lab
        La7:
            r1 = move-exception
            com.tencent.bugly.proguard.w.a(r1)
        Lab:
            return r0
        Lac:
            r0 = move-exception
        Lad:
            if (r2 == 0) goto Lb7
            r2.close()     // Catch: java.io.IOException -> Lb3
            goto Lb7
        Lb3:
            r1 = move-exception
            com.tencent.bugly.proguard.w.a(r1)
        Lb7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.m():java.lang.String");
    }

    public static String i(Context context) throws Throwable {
        StringBuilder sb = new StringBuilder();
        String strA = com.tencent.bugly.proguard.a.a(context, "ro.genymotion.version");
        if (strA != null) {
            sb.append("ro.genymotion.version");
            sb.append("|");
            sb.append(strA);
            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        String strA2 = com.tencent.bugly.proguard.a.a(context, "androVM.vbox_dpi");
        if (strA2 != null) {
            sb.append("androVM.vbox_dpi");
            sb.append("|");
            sb.append(strA2);
            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        String strA3 = com.tencent.bugly.proguard.a.a(context, "qemu.sf.fake_camera");
        if (strA3 != null) {
            sb.append("qemu.sf.fake_camera");
            sb.append("|");
            sb.append(strA3);
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00ad A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String j(android.content.Context r5) throws java.lang.Throwable {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = com.tencent.bugly.crashreport.common.info.b.a
            if (r1 != 0) goto L11
            java.lang.String r1 = "ro.secure"
            java.lang.String r1 = com.tencent.bugly.proguard.a.a(r5, r1)
            com.tencent.bugly.crashreport.common.info.b.a = r1
        L11:
            java.lang.String r1 = com.tencent.bugly.crashreport.common.info.b.a
            if (r1 == 0) goto L29
            java.lang.String r1 = "ro.secure"
            r0.append(r1)
            java.lang.String r1 = "|"
            r0.append(r1)
            java.lang.String r1 = com.tencent.bugly.crashreport.common.info.b.a
            r0.append(r1)
            java.lang.String r1 = "\n"
            r0.append(r1)
        L29:
            java.lang.String r1 = com.tencent.bugly.crashreport.common.info.b.b
            if (r1 != 0) goto L35
            java.lang.String r1 = "ro.debuggable"
            java.lang.String r5 = com.tencent.bugly.proguard.a.a(r5, r1)
            com.tencent.bugly.crashreport.common.info.b.b = r5
        L35:
            java.lang.String r5 = com.tencent.bugly.crashreport.common.info.b.b
            if (r5 == 0) goto L4d
            java.lang.String r5 = "ro.debuggable"
            r0.append(r5)
            java.lang.String r5 = "|"
            r0.append(r5)
            java.lang.String r5 = com.tencent.bugly.crashreport.common.info.b.b
            r0.append(r5)
            java.lang.String r5 = "\n"
            r0.append(r5)
        L4d:
            r5 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L94
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L94
            java.lang.String r3 = "/proc/self/status"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L94
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L94
        L5a:
            java.lang.String r5 = r1.readLine()     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            if (r5 == 0) goto L68
            java.lang.String r2 = "TracerPid:"
            boolean r2 = r5.startsWith(r2)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            if (r2 == 0) goto L5a
        L68:
            if (r5 == 0) goto L81
            r2 = 10
            java.lang.String r5 = r5.substring(r2)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            java.lang.String r5 = r5.trim()     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            java.lang.String r2 = "tracer_pid"
            r0.append(r2)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            java.lang.String r2 = "|"
            r0.append(r2)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            r0.append(r5)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
        L81:
            java.lang.String r5 = r0.toString()     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> Laa
            r1.close()     // Catch: java.io.IOException -> L89
            goto L8d
        L89:
            r0 = move-exception
            com.tencent.bugly.proguard.w.a(r0)
        L8d:
            return r5
        L8e:
            r5 = move-exception
            goto L98
        L90:
            r0 = move-exception
            r1 = r5
            r5 = r0
            goto Lab
        L94:
            r1 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
        L98:
            com.tencent.bugly.proguard.w.a(r5)     // Catch: java.lang.Throwable -> Laa
            if (r1 == 0) goto La5
            r1.close()     // Catch: java.io.IOException -> La1
            goto La5
        La1:
            r5 = move-exception
            com.tencent.bugly.proguard.w.a(r5)
        La5:
            java.lang.String r5 = r0.toString()
            return r5
        Laa:
            r5 = move-exception
        Lab:
            if (r1 == 0) goto Lb5
            r1.close()     // Catch: java.io.IOException -> Lb1
            goto Lb5
        Lb1:
            r0 = move-exception
            com.tencent.bugly.proguard.w.a(r0)
        Lb5:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.j(android.content.Context):java.lang.String");
    }

    public static String n() throws Throwable {
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
        } catch (IOException e) {
            w.a(e);
        }
        try {
            try {
                if (new File("/sys/class/power_supply/ac/online").exists()) {
                    bufferedReader = new BufferedReader(new FileReader("/sys/class/power_supply/ac/online"));
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            sb.append("ac_online");
                            sb.append("|");
                            sb.append(line);
                        }
                        bufferedReader.close();
                        bufferedReader2 = bufferedReader;
                    } catch (Throwable th) {
                        BufferedReader bufferedReader3 = bufferedReader;
                        th = th;
                        bufferedReader2 = bufferedReader3;
                        w.a(th);
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        return sb.toString();
                    }
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                if (new File("/sys/class/power_supply/usb/online").exists()) {
                    BufferedReader bufferedReader4 = new BufferedReader(new FileReader("/sys/class/power_supply/usb/online"));
                    String line2 = bufferedReader4.readLine();
                    if (line2 != null) {
                        sb.append("usb_online");
                        sb.append("|");
                        sb.append(line2);
                    }
                    bufferedReader4.close();
                    bufferedReader2 = bufferedReader4;
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                if (new File("/sys/class/power_supply/battery/capacity").exists()) {
                    bufferedReader = new BufferedReader(new FileReader("/sys/class/power_supply/battery/capacity"));
                    String line3 = bufferedReader.readLine();
                    if (line3 != null) {
                        sb.append("battery_capacity");
                        sb.append("|");
                        sb.append(line3);
                    }
                    bufferedReader.close();
                    bufferedReader2 = bufferedReader;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            return sb.toString();
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static String k(Context context) throws Throwable {
        StringBuilder sb = new StringBuilder();
        String strA = com.tencent.bugly.proguard.a.a(context, "gsm.sim.state");
        if (strA != null) {
            sb.append("gsm.sim.state");
            sb.append("|");
            sb.append(strA);
        }
        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
        String strA2 = com.tencent.bugly.proguard.a.a(context, "gsm.sim.state2");
        if (strA2 != null) {
            sb.append("gsm.sim.state2");
            sb.append("|");
            sb.append(strA2);
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x004a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x0030 -> B:31:0x0045). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long o() throws java.lang.Throwable {
        /*
            r0 = 0
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L36 java.lang.Throwable -> L39
            java.io.FileReader r3 = new java.io.FileReader     // Catch: java.lang.Throwable -> L36 java.lang.Throwable -> L39
            java.lang.String r4 = "/proc/uptime"
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L36 java.lang.Throwable -> L39
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L36 java.lang.Throwable -> L39
            java.lang.String r1 = r2.readLine()     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            if (r1 == 0) goto L2b
            java.lang.String r3 = " "
            java.lang.String[] r1 = r1.split(r3)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            r3 = 0
            r1 = r1[r3]     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            r5 = 1000(0x3e8, double:4.94E-321)
            long r3 = r3 / r5
            float r3 = (float) r3     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            float r1 = java.lang.Float.parseFloat(r1)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L47
            float r3 = r3 - r1
            r0 = r3
        L2b:
            r2.close()     // Catch: java.io.IOException -> L2f
            goto L45
        L2f:
            r1 = move-exception
            com.tencent.bugly.proguard.w.a(r1)
            goto L45
        L34:
            r1 = move-exception
            goto L3d
        L36:
            r0 = move-exception
            r2 = r1
            goto L48
        L39:
            r2 = move-exception
            r7 = r2
            r2 = r1
            r1 = r7
        L3d:
            com.tencent.bugly.proguard.w.a(r1)     // Catch: java.lang.Throwable -> L47
            if (r2 == 0) goto L45
            r2.close()     // Catch: java.io.IOException -> L2f
        L45:
            long r0 = (long) r0
            return r0
        L47:
            r0 = move-exception
        L48:
            if (r2 == 0) goto L52
            r2.close()     // Catch: java.io.IOException -> L4e
            goto L52
        L4e:
            r1 = move-exception
            com.tencent.bugly.proguard.w.a(r1)
        L52:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.o():long");
    }
}
