package lecho.lib.hellocharts.util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

/* loaded from: classes.dex */
public abstract class ChartUtils {
    private static final float DARKEN_INTENSITY = 0.9f;
    private static final float DARKEN_SATURATION = 1.1f;
    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int COLOR_BLUE = Color.parseColor("#33B5E5");
    public static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
    public static final int COLOR_GREEN = Color.parseColor("#99CC00");
    public static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
    public static final int COLOR_RED = Color.parseColor("#FF4444");
    public static final int[] COLORS = {COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED};
    private static int COLOR_INDEX = 0;

    public static int dp2px(float f, int i) {
        if (i == 0) {
            return 0;
        }
        return (int) ((i * f) + 0.5f);
    }

    public static int sp2px(float f, int i) {
        if (i == 0) {
            return 0;
        }
        return (int) ((i * f) + 0.5f);
    }

    public static final int pickColor() {
        int[] iArr = COLORS;
        double dRandom = Math.random();
        double length = COLORS.length - 1;
        Double.isNaN(length);
        return iArr[(int) Math.round(dRandom * length)];
    }

    public static final int nextColor() {
        if (COLOR_INDEX >= COLORS.length) {
            COLOR_INDEX = 0;
        }
        int[] iArr = COLORS;
        int i = COLOR_INDEX;
        COLOR_INDEX = i + 1;
        return iArr[i];
    }

    public static int px2dp(float f, int i) {
        return (int) Math.ceil(i / f);
    }

    public static int px2sp(float f, int i) {
        return (int) Math.ceil(i / f);
    }

    public static int mm2px(Context context, int i) {
        return (int) (TypedValue.applyDimension(5, i, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int darkenColor(int i) {
        float[] fArr = new float[3];
        int iAlpha = Color.alpha(i);
        Color.colorToHSV(i, fArr);
        fArr[1] = Math.min(fArr[1] * DARKEN_SATURATION, 1.0f);
        fArr[2] = fArr[2] * DARKEN_INTENSITY;
        int iHSVToColor = Color.HSVToColor(fArr);
        return Color.argb(iAlpha, Color.red(iHSVToColor), Color.green(iHSVToColor), Color.blue(iHSVToColor));
    }
}
