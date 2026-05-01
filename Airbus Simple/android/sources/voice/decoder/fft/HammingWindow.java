package voice.decoder.fft;

/* loaded from: classes.dex */
public class HammingWindow extends WindowFunction {
    @Override // voice.decoder.fft.WindowFunction
    protected float value(int i, int i2) {
        return 0.54f - (((float) Math.cos((i2 * 6.2831855f) / (i - 1))) * 0.46f);
    }
}
