package com.shima.smartbushome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.shima.smartbushome.util.SystemUIUtil;

/* loaded from: classes.dex */
public class WelcomeActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new Handler().postDelayed(new Runnable() { // from class: com.shima.smartbushome.WelcomeActivity.1
            @Override // java.lang.Runnable
            public void run() {
                WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, (Class<?>) MainActivity.class));
                WelcomeActivity.this.finish();
            }
        }, 2000L);
        SystemUIUtil.setSystemUIVisible(this, false);
    }
}
