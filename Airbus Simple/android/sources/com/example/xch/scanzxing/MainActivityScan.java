package com.example.xch.scanzxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.xch.scanzxing.zxing.android.CaptureActivity;

/* loaded from: classes.dex */
public class MainActivityScan extends AppCompatActivity implements View.OnClickListener {
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final int REQUEST_CODE_SCAN = 0;
    private Button btn_scan;
    private TextView tv_scanResult;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main_scan);
        this.tv_scanResult = (TextView) findViewById(R.id.tv_scanResult);
        this.btn_scan = (Button) findViewById(R.id.btn_scan);
        this.btn_scan.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.btn_scan) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 1);
            } else {
                goScan();
            }
        }
    }

    private void goScan() {
        startActivityForResult(new Intent(this, (Class<?>) CaptureActivity.class), 0);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 1) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            goScan();
        } else {
            Toast.makeText(this, "你拒绝了权限申请，可能无法打开相机扫码哟！", 0).show();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1 && intent != null) {
            String stringExtra = intent.getStringExtra(DECODED_CONTENT_KEY);
            this.tv_scanResult.setText("你扫描到的内容是：" + stringExtra);
        }
    }
}
