package com.pzj.ipcdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/* loaded from: classes.dex */
public class BaseActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    public void showToast(int i) {
        Toast.makeText(this, getResources().getString(i), 1).show();
    }
}
