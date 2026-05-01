package voice.decoder.fft;

import java.util.concurrent.ExecutionException;

/* loaded from: classes.dex */
public final class FFT {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final FloatFFT fft;
    private final int fftSize;
    private final WindowFunction windowFunction;

    public FFT(int i) {
        this(i, null);
    }

    public FFT(int i, WindowFunction windowFunction) {
        this.fft = new FloatFFT(i);
        this.fftSize = i;
        this.windowFunction = windowFunction;
    }

    public int getFFTSize() {
        return this.fftSize;
    }

    public void forwardTransform(float[] fArr) throws ExecutionException, InterruptedException {
        if (this.windowFunction != null) {
            this.windowFunction.apply(fArr);
        }
        this.fft.realForward(fArr);
    }

    public void complexForwardTransform(float[] fArr) throws ExecutionException, InterruptedException {
        if (this.windowFunction != null) {
            this.windowFunction.apply(fArr);
        }
        this.fft.complexForward(fArr);
    }

    public void backwardsTransform(float[] fArr) throws ExecutionException, InterruptedException {
        this.fft.realInverse(fArr, true);
    }

    public double binToHz(int i, float f) {
        double d = i * f;
        double d2 = this.fftSize;
        Double.isNaN(d);
        Double.isNaN(d2);
        return d / d2;
    }

    public float modulus(float[] fArr, int i) {
        int i2 = (i * 2) + 1;
        return (float) Math.sqrt((fArr[r4] * fArr[r4]) + (fArr[i2] * fArr[i2]));
    }

    public void modulus(float[] fArr, float[] fArr2) {
        for (int i = 0; i < fArr2.length; i++) {
            fArr2[i] = modulus(fArr, i);
        }
    }

    public void powerPhaseFFT(float[] fArr, float[] fArr2, float[] fArr3) throws ExecutionException, InterruptedException {
        if (this.windowFunction != null) {
            this.windowFunction.apply(fArr);
        }
        this.fft.realForward(fArr);
        fArr3[0] = 3.1415927f;
        fArr2[0] = -fArr[0];
        for (int i = 1; i < fArr2.length; i++) {
            int i2 = (i * 2) + 1;
            fArr2[i] = (float) Math.sqrt((fArr[r1] * fArr[r1]) + (fArr[i2] * fArr[i2]));
            fArr3[i] = (float) Math.atan2(fArr[i2], fArr[r1]);
        }
    }

    public void powerPhaseFFTBeatRootOnset(float[] fArr, float[] fArr2, float[] fArr3) throws ExecutionException, InterruptedException {
        powerPhaseFFT(fArr, fArr2, fArr3);
        fArr2[0] = (float) Math.sqrt((fArr[0] * fArr[0]) + (fArr[1] * fArr[1]));
    }
}
