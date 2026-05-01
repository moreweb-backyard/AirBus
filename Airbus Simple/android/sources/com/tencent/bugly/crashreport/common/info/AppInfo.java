package com.tencent.bugly.crashreport.common.info;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Process;
import android.support.v4.os.EnvironmentCompat;
import com.litesuits.http.data.Consts;
import com.tencent.bugly.proguard.w;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class AppInfo {
    private static ActivityManager a;

    static {
        "@buglyAllChannel@".split(Consts.SECOND_LEVEL_SPLIT);
        "@buglyAllChannelPriority@".split(Consts.SECOND_LEVEL_SPLIT);
    }

    public static String a(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageName();
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static PackageInfo b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(a(context), 0);
        } catch (Throwable th) {
            if (w.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static String a(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return null;
        }
        try {
            return packageInfo.versionName;
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String b(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return null;
        }
        try {
            return Integer.toString(packageInfo.versionCode);
        } catch (Throwable th) {
            if (w.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static boolean a(Context context, String str) {
        if (context == null || str == null || str.trim().length() <= 0) {
            return false;
        }
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null) {
                for (String str2 : strArr) {
                    if (str.equals(str2)) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
        }
        return false;
    }

    public static String a(int i) throws Throwable {
        FileReader fileReader = null;
        try {
            try {
                FileReader fileReader2 = new FileReader("/proc/" + i + "/cmdline");
                try {
                    char[] cArr = new char[512];
                    fileReader2.read(cArr);
                    int i2 = 0;
                    while (i2 < cArr.length && cArr[i2] != 0) {
                        i2++;
                    }
                    String strSubstring = new String(cArr).substring(0, i2);
                    try {
                        fileReader2.close();
                    } catch (Throwable unused) {
                    }
                    return strSubstring;
                } catch (Throwable th) {
                    th = th;
                    fileReader = fileReader2;
                    if (!w.a(th)) {
                        th.printStackTrace();
                    }
                    String strValueOf = String.valueOf(i);
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable unused2) {
                        }
                    }
                    return strValueOf;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static String c(Context context) {
        try {
            return a(Process.myPid());
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> d(Context context) {
        if (context == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData == null) {
                return null;
            }
            HashMap map = new HashMap();
            Object obj = applicationInfo.metaData.get("BUGLY_DISABLE");
            if (obj != null) {
                map.put("BUGLY_DISABLE", obj.toString());
            }
            Object obj2 = applicationInfo.metaData.get("BUGLY_APPID");
            if (obj2 != null) {
                map.put("BUGLY_APPID", obj2.toString());
            }
            Object obj3 = applicationInfo.metaData.get("BUGLY_APP_CHANNEL");
            if (obj3 != null) {
                map.put("BUGLY_APP_CHANNEL", obj3.toString());
            }
            Object obj4 = applicationInfo.metaData.get("BUGLY_APP_VERSION");
            if (obj4 != null) {
                map.put("BUGLY_APP_VERSION", obj4.toString());
            }
            Object obj5 = applicationInfo.metaData.get("BUGLY_ENABLE_DEBUG");
            if (obj5 != null) {
                map.put("BUGLY_ENABLE_DEBUG", obj5.toString());
            }
            Object obj6 = applicationInfo.metaData.get("com.tencent.rdm.uuid");
            if (obj6 != null) {
                map.put("com.tencent.rdm.uuid", obj6.toString());
            }
            return map;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static List<String> a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        try {
            String str = map.get("BUGLY_DISABLE");
            if (str != null && str.length() != 0) {
                String[] strArrSplit = str.split(Consts.SECOND_LEVEL_SPLIT);
                for (int i = 0; i < strArrSplit.length; i++) {
                    strArrSplit[i] = strArrSplit[i].trim();
                }
                return Arrays.asList(strArrSplit);
            }
            return null;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static String a(byte[] bArr) {
        X509Certificate x509Certificate;
        StringBuilder sb = new StringBuilder();
        if (bArr != null && bArr.length > 0) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                if (certificateFactory == null || (x509Certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(bArr))) == null) {
                    return null;
                }
                sb.append("Issuer|");
                Principal issuerDN = x509Certificate.getIssuerDN();
                if (issuerDN != null) {
                    sb.append(issuerDN.toString());
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                sb.append("SerialNumber|");
                BigInteger serialNumber = x509Certificate.getSerialNumber();
                if (issuerDN != null) {
                    sb.append(serialNumber.toString(16));
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                sb.append("NotBefore|");
                Date notBefore = x509Certificate.getNotBefore();
                if (issuerDN != null) {
                    sb.append(notBefore.toString());
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                sb.append("NotAfter|");
                Date notAfter = x509Certificate.getNotAfter();
                if (issuerDN != null) {
                    sb.append(notAfter.toString());
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                sb.append("SHA1|");
                String strB = com.tencent.bugly.proguard.a.b(MessageDigest.getInstance("SHA1").digest(x509Certificate.getEncoded()));
                if (strB != null && strB.length() > 0) {
                    sb.append(strB.toString());
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                sb.append("MD5|");
                String strB2 = com.tencent.bugly.proguard.a.b(MessageDigest.getInstance("MD5").digest(x509Certificate.getEncoded()));
                if (strB2 != null && strB2.length() > 0) {
                    sb.append(strB2.toString());
                } else {
                    sb.append(EnvironmentCompat.MEDIA_UNKNOWN);
                }
            } catch (CertificateException e) {
                if (!w.a(e)) {
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        return sb.length() == 0 ? EnvironmentCompat.MEDIA_UNKNOWN : sb.toString();
    }

    public static String e(Context context) throws PackageManager.NameNotFoundException {
        Signature[] signatureArr;
        String strA = a(context);
        if (strA == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(strA, 64);
            if (packageInfo == null || (signatureArr = packageInfo.signatures) == null || signatureArr.length == 0) {
                return null;
            }
            return a(packageInfo.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static boolean f(Context context) {
        if (context == null) {
            return false;
        }
        if (a == null) {
            a = (ActivityManager) context.getSystemService("activity");
        }
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            a.getMemoryInfo(memoryInfo);
            if (!memoryInfo.lowMemory) {
                return false;
            }
            w.c("Memory is low.", new Object[0]);
            return true;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }
}
