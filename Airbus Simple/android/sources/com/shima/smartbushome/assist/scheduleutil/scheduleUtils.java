package com.shima.smartbushome.assist.scheduleutil;

import android.app.ActivityManager;
import android.content.Context;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class scheduleUtils {
    public static boolean isServiceWork(Context context, String str) throws SecurityException {
        List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(100);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().toString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProessRunning(Context context, String str) {
        Iterator<ActivityManager.RunningAppProcessInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses().iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().processName.equals(str)) {
                z = true;
            }
        }
        return z;
    }
}
