package voice.decoder;

/* loaded from: classes.dex */
class EventInfo {
    public FreqAmplitude p1;
    public FreqAmplitude p2;
    public long timeIdx;

    EventInfo(long j, FreqAmplitude freqAmplitude, FreqAmplitude freqAmplitude2) {
        this.timeIdx = j;
        this.p1 = freqAmplitude;
        this.p2 = freqAmplitude2;
    }
}
