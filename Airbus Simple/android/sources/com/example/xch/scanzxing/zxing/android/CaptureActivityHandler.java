package com.example.xch.scanzxing.zxing.android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.xch.scanzxing.R;
import com.example.xch.scanzxing.zxing.camera.CameraManager;
import com.example.xch.scanzxing.zxing.decode.DecodeThread;
import com.example.xch.scanzxing.zxing.view.ViewfinderResultPointCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public final class CaptureActivityHandler extends Handler {
    private static final String TAG = "CaptureActivityHandler";
    private final CaptureActivity activity;
    private final CameraManager cameraManager;
    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(CaptureActivity captureActivity, Collection<BarcodeFormat> collection, Map<DecodeHintType, ?> map, String str, CameraManager cameraManager) {
        this.activity = captureActivity;
        this.decodeThread = new DecodeThread(captureActivity, collection, map, str, new ViewfinderResultPointCallback(captureActivity.getViewfinderView()));
        this.decodeThread.start();
        this.state = State.SUCCESS;
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
            return;
        }
        String str = null;
        bitmapCopy = null;
        bitmapCopy = null;
        Bitmap bitmapCopy = null;
        str = null;
        if (message.what == R.id.decode_succeeded) {
            this.state = State.SUCCESS;
            Bundle data = message.getData();
            float f = 1.0f;
            if (data != null) {
                try {
                    byte[] byteArray = data.getByteArray(DecodeThread.BARCODE_BITMAP);
                    if (byteArray != null) {
                        Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, null);
                        try {
                            bitmapCopy = bitmapDecodeByteArray.copy(Bitmap.Config.ARGB_8888, true);
                        } catch (Exception e) {
                            e = e;
                            bitmapCopy = bitmapDecodeByteArray;
                            e.printStackTrace();
                            this.activity.handleDecode((Result) message.obj, bitmapCopy, f);
                            return;
                        }
                    }
                    f = data.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
                } catch (Exception e2) {
                    e = e2;
                }
            }
            this.activity.handleDecode((Result) message.obj, bitmapCopy, f);
            return;
        }
        if (message.what == R.id.decode_failed) {
            this.state = State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
            return;
        }
        if (message.what == R.id.return_scan_result) {
            this.activity.setResult(-1, (Intent) message.obj);
            this.activity.finish();
            return;
        }
        if (message.what == R.id.launch_product_query) {
            String str2 = (String) message.obj;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(524288);
            intent.setData(Uri.parse(str2));
            ResolveInfo resolveInfoResolveActivity = this.activity.getPackageManager().resolveActivity(intent, 65536);
            if (resolveInfoResolveActivity != null && resolveInfoResolveActivity.activityInfo != null) {
                str = resolveInfoResolveActivity.activityInfo.packageName;
                Log.d(TAG, "Using browser in package " + str);
            }
            if ("com.android.browser".equals(str) || "com.android.chrome".equals(str)) {
                intent.setPackage(str);
                intent.addFlags(268435456);
                intent.putExtra("com.android.browser.application_id", str);
            }
            try {
                this.activity.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Log.w(TAG, "Can't find anything to handle VIEW of URI " + str2);
            }
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        this.cameraManager.stopPreview();
        Message.obtain(this.decodeThread.getHandler(), R.id.quit).sendToTarget();
        try {
            this.decodeThread.join(500L);
        } catch (InterruptedException unused) {
        }
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
            this.activity.drawViewfinder();
        }
    }
}
