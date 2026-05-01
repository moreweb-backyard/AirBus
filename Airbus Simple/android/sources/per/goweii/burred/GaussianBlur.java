package per.goweii.burred;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

/* loaded from: classes.dex */
public final class GaussianBlur implements IBlur {
    private static GaussianBlur INSTANCE;
    private final ScriptIntrinsicBlur gaussianBlur;
    private final RenderScript renderScript;

    @RequiresApi(api = 17)
    private GaussianBlur(Context context) {
        this.renderScript = RenderScript.create(context.getApplicationContext());
        this.gaussianBlur = ScriptIntrinsicBlur.create(this.renderScript, Element.U8_4(this.renderScript));
    }

    @RequiresApi(api = 17)
    public static GaussianBlur get(Context context) {
        if (INSTANCE == null) {
            synchronized (GaussianBlur.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GaussianBlur(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override // per.goweii.burred.IBlur
    @RequiresApi(api = 17)
    public Bitmap process(@NonNull Bitmap bitmap, @FloatRange(from = 0.0d) float f, @FloatRange(from = 1.0d) float f2, boolean z, boolean z2) {
        if (f <= 0.0f) {
            return bitmap;
        }
        if (f > 25.0f) {
            f2 *= f / 25.0f;
            f = 25.0f;
        }
        if (f2 == 1.0f) {
            Bitmap bitmapBlurIn25 = blurIn25(bitmap, f);
            if (z2) {
                bitmap.recycle();
            }
            return bitmapBlurIn25;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / f2), (int) (height / f2), true);
        if (z2) {
            bitmap.recycle();
        }
        Bitmap bitmapBlurIn252 = blurIn25(bitmapCreateScaledBitmap, f);
        bitmapCreateScaledBitmap.recycle();
        if (!z) {
            return bitmapBlurIn252;
        }
        Bitmap bitmapCreateScaledBitmap2 = Bitmap.createScaledBitmap(bitmapBlurIn252, width, height, true);
        bitmapBlurIn252.recycle();
        return bitmapCreateScaledBitmap2;
    }

    @Override // per.goweii.burred.IBlur
    public void recycle() {
        if (INSTANCE != null) {
            INSTANCE.gaussianBlur.destroy();
            INSTANCE.renderScript.destroy();
            INSTANCE = null;
        }
    }

    @RequiresApi(api = 17)
    private Bitmap blurIn25(@NonNull Bitmap bitmap, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation allocationCreateFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
        Allocation allocationCreateTyped = Allocation.createTyped(this.renderScript, allocationCreateFromBitmap.getType());
        this.gaussianBlur.setRadius(f);
        this.gaussianBlur.setInput(allocationCreateFromBitmap);
        this.gaussianBlur.forEach(allocationCreateTyped);
        allocationCreateTyped.copyTo(bitmapCreateBitmap);
        allocationCreateFromBitmap.destroy();
        allocationCreateTyped.destroy();
        return bitmapCreateBitmap;
    }
}
