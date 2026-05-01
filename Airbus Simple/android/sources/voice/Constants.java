package voice;

import voice.encoder.SignalGenerator;

/* loaded from: classes.dex */
public class Constants {
    public static final int DEFAULT_BUFFER_COUNT = 3;
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final String DEFAULT_CODE_BOOK = "0123456789abcdef";
    public static final int DEFAULT_OVERLAP_SIZE = 512;
    public static final int DEFAULT_SAMPLE_RATE = 44100;
    public static final int MAX_FREQUENCY = 18800;
    public static final int MAX_FREQ_COUNT = 19;
    public static final int MIN_FREQUENCY = 14800;
    public static final int OVERLAP_TOKEN = 17;
    public static final int SIGNAL_SIZE = 10;
    public static final int START_TOKEN = 0;
    public static final int STOP_TOKEN = 18;
    public static final String TestSignal = "0ce0a6a088";
    public static final int[] CODE_FREQUENCY = {15000, 15200, 15400, 15600, 15800, SignalGenerator.SAMPLE_RATE_16, 16200, 16400, 16600, 16800, 17000, 17200, 17400, 17600, 17800, 18000, 18200, 18400, 18600};
    public static String TestFullSignal = "1f0fee123456782f5bb";
}
