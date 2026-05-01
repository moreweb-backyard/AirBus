package com.pzj.ipcdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
public class Tools {
    private static float memorySizeTotal;

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:29:0x00d0 -> B:40:0x00d3). Please report as a decompilation issue!!! */
    public static void savePicToSDcard(Context context, String str, Bitmap bitmap) throws Throwable {
        FileOutputStream fileOutputStream;
        if (bitmap == null) {
            return;
        }
        DatabaseUtil databaseUtil = new DatabaseUtil(context);
        String strDate = getStrDate();
        databaseUtil.open();
        int count = databaseUtil.queryVideoOrPictureByDate(str, strDate, DatabaseUtil.TYPE_PICTURE).getCount() + 1;
        databaseUtil.close();
        FileOutputStream fileOutputStream2 = null;
        try {
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file, strDate + "_" + count + str + ".jpg");
                fileOutputStream = new FileOutputStream(file2);
                try {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream)) {
                        fileOutputStream.flush();
                        databaseUtil.open();
                        databaseUtil.createVideoOrPic(str, file2.getAbsolutePath(), DatabaseUtil.TYPE_PICTURE, strDate);
                        databaseUtil.close();
                        String absolutePath = file2.getAbsolutePath();
                        absolutePath.substring(absolutePath.lastIndexOf("/") + 1).substring(0, 10);
                        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + file2.getAbsolutePath())));
                        if (!bitmap.isRecycled() && bitmap != null) {
                            bitmap.recycle();
                        }
                    }
                    fileOutputStream.close();
                } catch (Exception e2) {
                    e = e2;
                    fileOutputStream2 = fileOutputStream;
                    e.printStackTrace();
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0080 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String savePresetPicToSDcard(android.content.Context r4, java.lang.String r5, int r6, android.graphics.Bitmap r7) throws java.lang.Throwable {
        /*
            r4 = 0
            if (r7 != 0) goto L4
            return r4
        L4:
            java.io.File r0 = new java.io.File     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.lang.String r2 = "/eye4/preset"
            r0.<init>(r1, r2)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            boolean r1 = r0.exists()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            if (r1 != 0) goto L18
            r0.mkdirs()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
        L18:
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            r2.<init>()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            r2.append(r5)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.lang.String r5 = "_"
            r2.append(r5)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            r2.append(r6)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.lang.String r5 = ".jpg"
            r2.append(r5)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.lang.String r5 = r2.toString()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            r1.<init>(r0, r5)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            r5.<init>(r1)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c
            android.graphics.Bitmap$CompressFormat r6 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
            r0 = 50
            boolean r6 = r7.compress(r6, r0, r5)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
            if (r6 == 0) goto L53
            r5.flush()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
            boolean r6 = r7.isRecycled()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
            if (r6 != 0) goto L53
            if (r7 == 0) goto L53
            r7.recycle()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
        L53:
            java.lang.String r6 = r1.getAbsolutePath()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L65
            r5.close()     // Catch: java.io.IOException -> L5b
            goto L5f
        L5b:
            r4 = move-exception
            r4.printStackTrace()
        L5f:
            return r6
        L60:
            r4 = move-exception
            r3 = r5
            r5 = r4
            r4 = r3
            goto L7e
        L65:
            r6 = move-exception
            r3 = r6
            r6 = r5
            r5 = r3
            goto L6e
        L6a:
            r5 = move-exception
            goto L7e
        L6c:
            r5 = move-exception
            r6 = r4
        L6e:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L7c
            if (r6 == 0) goto L7b
            r6.close()     // Catch: java.io.IOException -> L77
            goto L7b
        L77:
            r5 = move-exception
            r5.printStackTrace()
        L7b:
            return r4
        L7c:
            r5 = move-exception
            r4 = r6
        L7e:
            if (r4 == 0) goto L88
            r4.close()     // Catch: java.io.IOException -> L84
            goto L88
        L84:
            r4 = move-exception
            r4.printStackTrace()
        L88:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pzj.ipcdemo.utils.Tools.savePresetPicToSDcard(android.content.Context, java.lang.String, int, android.graphics.Bitmap):java.lang.String");
    }

    public static String getStrDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH_mm").format(new Date());
    }

    public static int getPhoneSDKIntForPlayBack() {
        if (Build.BRAND.toLowerCase().contains("xiaomi")) {
            return 24;
        }
        return Integer.parseInt(Build.VERSION.SDK);
    }

    public static int getPhoneMemoryForPlayBack() {
        return (((double) getPhoneTotalMemory()) < 2.8d || Integer.parseInt(Build.VERSION.SDK) < 23) ? 0 : 1;
    }

    public static float getPhoneTotalMemory() throws IOException {
        if (memorySizeTotal != 0.0f) {
            return memorySizeTotal;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String[] strArrSplit = bufferedReader.readLine().split("\\s+");
            for (String str : strArrSplit) {
            }
            double dIntValue = Integer.valueOf(strArrSplit[1]).intValue();
            Double.isNaN(dIntValue);
            memorySizeTotal = (float) ((dIntValue / 1000.0d) / 1000.0d);
            bufferedReader.close();
        } catch (IOException unused) {
        }
        return memorySizeTotal;
    }
}
