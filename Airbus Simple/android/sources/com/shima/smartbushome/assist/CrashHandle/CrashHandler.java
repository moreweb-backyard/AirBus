package com.shima.smartbushome.assist.CrashHandle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.litesuits.http.data.Consts;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

@SuppressLint({"SimpleDateFormat"})
/* loaded from: classes.dex */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static String TAG = "MyCrash";
    private static CrashHandler instance = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT_DATE);

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        autoClear(5);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (!handleException(th) && this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, th);
            return;
        }
        SystemClock.sleep(3000L);
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.shima.smartbushome.assist.CrashHandle.CrashHandler$1] */
    private boolean handleException(Throwable th) {
        if (th == null) {
            return false;
        }
        try {
            new Thread() { // from class: com.shima.smartbushome.assist.CrashHandle.CrashHandler.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    Toast.makeText(CrashHandler.this.mContext, "sorry,the app crash,it will open again", 1).show();
                    Looper.loop();
                }
            }.start();
            collectDeviceInfo(this.mContext);
            saveCrashInfoFile(th);
            SystemClock.sleep(3000L);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void collectDeviceInfo(Context context) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo != null) {
                String str = packageInfo.versionName + "";
                String str2 = packageInfo.versionCode + "";
                this.infos.put("versionName", str);
                this.infos.put("versionCode", str2);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCrashInfoFile(Throwable th) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append(IOUtils.LINE_SEPARATOR_WINDOWS + new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT).format(new Date()) + IOUtils.LINE_SEPARATOR_UNIX);
            for (Map.Entry<String, String> entry : this.infos.entrySet()) {
                stringBuffer.append(entry.getKey() + Consts.EQUALS + entry.getValue() + IOUtils.LINE_SEPARATOR_UNIX);
            }
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            printWriter.flush();
            printWriter.close();
            stringBuffer.append(stringWriter.toString());
            return writeFile(stringBuffer.toString());
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            stringBuffer.append("an error occured while writing file...\r\n");
            writeFile(stringBuffer.toString());
            return null;
        }
    }

    private String writeFile(String str) throws Exception {
        String str2 = "crash-" + this.formatter.format(new Date()) + ".log";
        if (FileUtil.hasSdcard()) {
            String globalpath = getGlobalpath();
            File file = new File(globalpath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(globalpath + str2, true);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        return str2;
    }

    public static String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash" + File.separator;
    }

    public static void setTag(String str) {
        TAG = str;
    }

    public void autoClear(final int i) {
        FileUtil.delete(getGlobalpath(), new FilenameFilter() { // from class: com.shima.smartbushome.assist.CrashHandle.CrashHandler.2
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                String fileNameWithoutExtension = FileUtil.getFileNameWithoutExtension(str);
                int i2 = i < 0 ? i : i * (-1);
                StringBuilder sb = new StringBuilder();
                sb.append("crash-");
                sb.append(DateUtil.getOtherDay(i2));
                return sb.toString().compareTo(fileNameWithoutExtension) >= 0;
            }
        });
    }
}
