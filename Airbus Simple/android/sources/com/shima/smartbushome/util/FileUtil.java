package com.shima.smartbushome.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.litesuits.http.data.Consts;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileUtil {
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean copyFile(String str, String str2) throws IOException {
        try {
            if (!new File(str).exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            int i = 0;
            while (true) {
                int i2 = fileInputStream.read(bArr);
                if (i2 != -1) {
                    i += i2;
                    System.out.println(i);
                    fileOutputStream.write(bArr, 0, i2);
                } else {
                    fileInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("copyFile error");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(File file, String str) throws IOException {
        if (file == null) {
            return false;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            byte[] bArr = new byte[1024];
            int i = 0;
            while (true) {
                int i2 = fileInputStream.read(bArr);
                if (i2 != -1) {
                    i += i2;
                    System.out.println(i);
                    fileOutputStream.write(bArr, 0, i2);
                } else {
                    fileInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("copyFile error");
            e.printStackTrace();
            return false;
        }
    }

    public static void copyFolder(String str, String str2) throws IOException {
        File file;
        try {
            new File(str2).mkdirs();
            String[] list = new File(str).list();
            for (int i = 0; i < list.length; i++) {
                if (str.endsWith(File.separator)) {
                    file = new File(str + list[i]);
                } else {
                    file = new File(str + File.separator + list[i]);
                }
                if (file.isFile()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(str2 + "/" + file.getName().toString());
                    byte[] bArr = new byte[5120];
                    while (true) {
                        int i2 = fileInputStream.read(bArr);
                        if (i2 == -1) {
                            break;
                        } else {
                            fileOutputStream.write(bArr, 0, i2);
                        }
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileInputStream.close();
                }
                if (file.isDirectory()) {
                    copyFolder(str + "/" + list[i], str2 + "/" + list[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("copyFolder error");
            e.printStackTrace();
        }
    }

    public static boolean deleteSDFile(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    deleteSDFile(file2.getAbsolutePath());
                }
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String GetFileSize(File file) {
        float length = file.length();
        System.out.println("-------changdu:" + length);
        float f = (float) 1024;
        if (length < f) {
            return String.format("%d byte", Integer.valueOf((int) length));
        }
        float f2 = 1048576;
        return length < f2 ? String.format("%.2f kb", Float.valueOf(length / f)) : length < ((float) 1073741824) ? String.format("%.2f mb", Float.valueOf(length / f2)) : "";
    }

    public static String GetFileType(String str) {
        int iLastIndexOf;
        return (str == null || str.length() <= 0 || (iLastIndexOf = str.lastIndexOf(46)) <= -1 || iLastIndexOf >= str.length() + (-1)) ? str : str.substring(iLastIndexOf + 1);
    }

    public static String UriToFilePath(Activity activity, Uri uri) throws IllegalArgumentException {
        Cursor cursorManagedQuery = activity.managedQuery(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = cursorManagedQuery.getColumnIndexOrThrow("_data");
        cursorManagedQuery.moveToFirst();
        return cursorManagedQuery.getString(columnIndexOrThrow);
    }

    public static Uri getPictureUri(Context context, Intent intent) {
        String encodedPath;
        Uri data = intent.getData();
        String type = intent.getType();
        if (data.getScheme().equals("file") && type.contains("image/") && (encodedPath = data.getEncodedPath()) != null) {
            String strDecode = Uri.decode(encodedPath);
            ContentResolver contentResolver = context.getContentResolver();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("(");
            stringBuffer.append("_data");
            stringBuffer.append(Consts.EQUALS);
            stringBuffer.append("'" + strDecode + "'");
            stringBuffer.append(")");
            Cursor cursorQuery = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, stringBuffer.toString(), null, null);
            int i = 0;
            cursorQuery.moveToFirst();
            while (!cursorQuery.isAfterLast()) {
                i = cursorQuery.getInt(cursorQuery.getColumnIndex("_id"));
                cursorQuery.moveToNext();
            }
            if (i != 0) {
                Uri uri = Uri.parse("content://media/external/images/media/" + i);
                if (uri != null) {
                    return uri;
                }
            }
        }
        return data;
    }
}
