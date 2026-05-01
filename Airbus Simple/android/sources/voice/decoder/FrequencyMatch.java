package voice.decoder;

/* loaded from: classes.dex */
public class FrequencyMatch {
    FreqAmplitude frequency;
    long timeIdx;

    FrequencyMatch(long j, FreqAmplitude freqAmplitude) {
        this.timeIdx = j;
        this.frequency = freqAmplitude;
    }
}
