package com.pzj.ipcdemo.utils;

import android.app.Activity;

/* loaded from: classes.dex */
public class SystemUIUtil {
    public static void setSystemUIVisible(Activity activity, boolean z) {
        try {
            if (z) {
                activity.getWindow().getDecorView().setSystemUiVisibility(4352);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(5894);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
