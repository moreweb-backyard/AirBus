package voice.decoder;

/* loaded from: classes.dex */
public class FreqAmplitude {
    public float amplitude;
    public int amplitudeColor;
    public int frequencyY;
    public int order;
    public int peakType;
    public float relative;

    FreqAmplitude(int i, float f, int i2) {
        this.frequencyY = i;
        this.amplitude = f;
        this.amplitudeColor = i2;
    }

    FreqAmplitude(FreqAmplitude freqAmplitude) {
        this.frequencyY = freqAmplitude.frequencyY;
        this.amplitude = freqAmplitude.amplitude;
        this.amplitudeColor = freqAmplitude.amplitudeColor;
        this.peakType = freqAmplitude.peakType;
        this.order = freqAmplitude.order;
    }
}
