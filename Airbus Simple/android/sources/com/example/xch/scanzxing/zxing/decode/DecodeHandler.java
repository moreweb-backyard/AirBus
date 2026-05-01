package com.example.xch.scanzxing.zxing.decode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.example.xch.scanzxing.R;
import com.example.xch.scanzxing.zxing.android.CaptureActivity;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/* loaded from: classes.dex */
public final class DecodeHandler extends Handler {
    private static final String TAG = "DecodeHandler";
    private final CaptureActivity activity;
    private boolean running = true;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();

    DecodeHandler(CaptureActivity captureActivity, Map<DecodeHintType, Object> map) {
        this.multiFormatReader.setHints(map);
        this.activity = captureActivity;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.running) {
            if (message.what == R.id.decode) {
                decode((byte[]) message.obj, message.arg1, message.arg2);
            } else if (message.what == R.id.quit) {
                this.running = false;
                Looper.myLooper().quit();
            }
        }
    }

    private void decode(byte[] bArr, int i, int i2) {
        Result resultDecodeWithState;
        long jCurrentTimeMillis = System.currentTimeMillis();
        byte[] bArr2 = new byte[bArr.length];
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                bArr2[(((i4 * i2) + i2) - i3) - 1] = bArr[(i3 * i) + i4];
            }
        }
        PlanarYUVLuminanceSource planarYUVLuminanceSourceBuildLuminanceSource = this.activity.getCameraManager().buildLuminanceSource(bArr2, i2, i);
        if (planarYUVLuminanceSourceBuildLuminanceSource != null) {
            try {
                resultDecodeWithState = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(planarYUVLuminanceSourceBuildLuminanceSource)));
            } catch (ReaderException unused) {
            } finally {
                this.multiFormatReader.reset();
            }
        } else {
            resultDecodeWithState = null;
        }
        Handler handler = this.activity.getHandler();
        if (resultDecodeWithState == null) {
            if (handler != null) {
                Message.obtain(handler, R.id.decode_failed).sendToTarget();
                return;
            }
            return;
        }
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        Log.d(TAG, "Found barcode in " + (jCurrentTimeMillis2 - jCurrentTimeMillis) + " ms");
        if (handler != null) {
            Message messageObtain = Message.obtain(handler, R.id.decode_succeeded, resultDecodeWithState);
            Bundle bundle = new Bundle();
            bundleThumbnail(planarYUVLuminanceSourceBuildLuminanceSource, bundle);
            messageObtain.setData(bundle);
            messageObtain.sendToTarget();
        }
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource planarYUVLuminanceSource, Bundle bundle) {
        int[] iArrRenderThumbnail = planarYUVLuminanceSource.renderThumbnail();
        int thumbnailWidth = planarYUVLuminanceSource.getThumbnailWidth();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iArrRenderThumbnail, 0, thumbnailWidth, thumbnailWidth, planarYUVLuminanceSource.getThumbnailHeight(), Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapCreateBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, byteArrayOutputStream.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, thumbnailWidth / planarYUVLuminanceSource.getWidth());
    }
}
