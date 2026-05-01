package com.xw.repo;

import android.content.res.Resources;
import android.os.Environment;
import android.util.TypedValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* loaded from: classes.dex */
class BubbleUtils {
    private static Properties sBuildProperties;
    private static final File BUILD_PROP_FILE = new File(Environment.getRootDirectory(), "build.prop");
    private static final Object sBuildPropertiesLock = new Object();

    BubbleUtils() {
    }

    private static Properties getBuildProperties() {
        synchronized (sBuildPropertiesLock) {
            if (sBuildProperties == null) {
                sBuildProperties = new Properties();
                try {
                    sBuildProperties.load(new FileInputStream(BUILD_PROP_FILE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sBuildProperties;
    }

    static boolean isMIUI() {
        return getBuildProperties().containsKey("ro.miui.ui.version.name");
    }

    static int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, i, Resources.getSystem().getDisplayMetrics());
    }

    static int sp2px(int i) {
        return (int) TypedValue.applyDimension(2, i, Resources.getSystem().getDisplayMetrics());
    }
}
