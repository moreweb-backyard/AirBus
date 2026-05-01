package per.goweii.anylayer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

/* loaded from: classes.dex */
final class BitmapUtils {
    private BitmapUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    static Bitmap scale(@NonNull Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (width != i || height != i2) {
            matrix.setScale(i / width, i2 / height);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
