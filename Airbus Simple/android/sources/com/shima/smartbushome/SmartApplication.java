package com.shima.smartbushome;

import android.app.Application;
import com.tencent.bugly.crashreport.CrashReport;

/* loaded from: classes.dex */
public class SmartApplication extends Application {
    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "ac724b7810", false);
    }
}
