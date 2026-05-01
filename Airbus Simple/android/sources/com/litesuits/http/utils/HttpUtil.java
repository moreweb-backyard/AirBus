package com.litesuits.http.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.shima.smartbushome.assist.CrashHandle.DateUtil;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class HttpUtil {
    private static int CPU_CORES = 0;
    private static final String CPU_FILTER = "cpu[0-9]+";
    private static final String PATH_CPU = "/sys/devices/system/cpu/";
    private static final String TAG = "HttpUtil";

    public static String formatDate(long j) {
        return new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT).format(new Date(j));
    }

    public static int getProcessorsCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getCoresNumbers() {
        if (CPU_CORES > 0) {
            return CPU_CORES;
        }
        try {
            CPU_CORES = new File(PATH_CPU).listFiles(new FileFilter() { // from class: com.litesuits.http.utils.HttpUtil.1CpuFilter
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return Pattern.matches(HttpUtil.CPU_FILTER, file.getName());
                }
            }).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = Runtime.getRuntime().availableProcessors();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = 1;
        }
        HttpLog.i(TAG, "CPU cores: " + CPU_CORES);
        return CPU_CORES;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (str2 != null) {
            builder.setMessage(str2);
        }
        if (str != null) {
            builder.setTitle(str);
        }
        return builder;
    }

    public static Dialog showTips(Context context, String str, String str2) {
        return showTips(context, str, str2, null, null);
    }

    public static Dialog showTips(Context context, String str, String str2, String str3, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder builderDialogBuilder = dialogBuilder(context, str, str2);
        builderDialogBuilder.setCancelable(true);
        builderDialogBuilder.setPositiveButton(str3, (DialogInterface.OnClickListener) null);
        AlertDialog alertDialogShow = builderDialogBuilder.show();
        alertDialogShow.setCanceledOnTouchOutside(true);
        alertDialogShow.setOnDismissListener(onDismissListener);
        return alertDialogShow;
    }

    public static ArrayList<Field> getAllParamModelFields(Class<?> cls) {
        ArrayList<Field> arrayList = new ArrayList<>();
        while (cls != null && cls != HttpRichParamModel.class && cls != Object.class) {
            for (Field field : cls.getDeclaredFields()) {
                if (!field.isSynthetic()) {
                    arrayList.add(field);
                }
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }
}
