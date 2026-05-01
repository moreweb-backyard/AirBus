package com.example.xch.scanzxing.zxing.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import com.example.xch.scanzxing.R;
import com.example.xch.scanzxing.zxing.camera.CameraManager;
import com.example.xch.scanzxing.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "CaptureActivity";
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private String characterSet;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private ImageButton imageButton_back;
    private InactivityTimer inactivityTimer;
    private IntentSource source;
    private ViewfinderView viewfinderView;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(128);
        setContentView(R.layout.capture);
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(this);
        this.beepManager = new BeepManager(this);
        this.imageButton_back = (ImageButton) findViewById(R.id.capture_imageview_back);
        this.imageButton_back.setOnClickListener(new View.OnClickListener() { // from class: com.example.xch.scanzxing.zxing.android.CaptureActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CaptureActivity.this.finish();
            }
        });
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.cameraManager = new CameraManager(getApplication());
        this.viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        this.viewfinderView.setCameraManager(this.cameraManager);
        this.handler = null;
        SurfaceHolder holder = ((SurfaceView) findViewById(R.id.preview_view)).getHolder();
        if (this.hasSurface) {
            initCamera(holder);
        } else {
            holder.addCallback(this);
        }
        this.beepManager.updatePrefs();
        this.inactivityTimer.onResume();
        this.source = IntentSource.NONE;
        this.decodeFormats = null;
        this.characterSet = null;
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }
        this.inactivityTimer.onPause();
        this.beepManager.close();
        this.cameraManager.closeDriver();
        if (!this.hasSurface) {
            ((SurfaceView) findViewById(R.id.preview_view)).getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (this.hasSurface) {
            return;
        }
        this.hasSurface = true;
        initCamera(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.hasSurface = false;
    }

    public void handleDecode(Result result, Bitmap bitmap, float f) {
        this.inactivityTimer.onActivity();
        if (bitmap != null) {
            this.beepManager.playBeepSoundAndVibrate();
            Intent intent = getIntent();
            intent.putExtra("codedContent", result.getText());
            intent.putExtra("codedBitmap", bitmap);
            setResult(-1, intent);
            finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (this.cameraManager.isOpen()) {
            return;
        }
        try {
            this.cameraManager.openDriver(surfaceHolder);
            if (this.handler == null) {
                this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.decodeHints, this.characterSet, this.cameraManager);
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e2) {
            Log.w(TAG, "Unexpected error initializing camera", e2);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }
}
