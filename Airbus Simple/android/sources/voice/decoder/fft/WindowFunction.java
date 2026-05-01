package voice.decoder.fft;

/* loaded from: classes.dex */
public abstract class WindowFunction {
    protected static final float TWO_PI = 6.2831855f;
    protected int length;

    protected abstract float value(int i, int i2);

    public void apply(float[] fArr) {
        this.length = fArr.length;
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = fArr[i] * value(fArr.length, i);
        }
    }

    public float[] generateCurve(int i) {
        float[] fArr = new float[i];
        for (int i2 = 0; i2 < i; i2++) {
            fArr[i2] = value(i, i2) * 1.0f;
        }
        return fArr;
    }
}
