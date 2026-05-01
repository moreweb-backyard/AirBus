package per.goweii.burred;

import android.graphics.Bitmap;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

/* loaded from: classes.dex */
public interface IBlur {
    Bitmap process(@NonNull Bitmap bitmap, @FloatRange(from = 0.0d) float f, @FloatRange(from = 1.0d) float f2, boolean z, boolean z2);

    void recycle();
}
