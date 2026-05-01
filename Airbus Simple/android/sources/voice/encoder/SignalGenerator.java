package voice.encoder;

import voice.Buffer;

/* loaded from: classes.dex */
public class SignalGenerator {
    public static final int BITS_16 = 32768;
    public static final int BITS_8 = 128;
    private static final int DEFAULT_BITS = 128;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final int DEFAULT_SAMPLE_RATE = 8000;
    public static final int SAMPLE_RATE_11 = 11250;
    public static final int SAMPLE_RATE_16 = 16000;
    public static final int SAMPLE_RATE_8 = 8000;
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    private static final String TAG = "Generator";
    public static final int UNIT_ACCURACY_1 = 4;
    public static final int UNIT_ACCURACY_2 = 8;
    private int mBits;
    private int mBufferSize;
    private Callback mCallback;
    private int mDuration;
    private int mFilledSize;
    private int mGenRate;
    private Listener mListener;
    private int mSampleRate;
    private int mState;
    private double volume;

    public interface Callback {
        void freeGenBuffer(Buffer.BufferData bufferData);

        Buffer.BufferData getGenBuffer();
    }

    public interface Listener {
        void onStartGen();

        void onStopGen();
    }

    public SignalGenerator(Callback callback) {
        this(callback, 8000, 128, 1024);
    }

    public SignalGenerator(Callback callback, int i, int i2, int i3) {
        this.volume = 1.0d;
        this.mCallback = callback;
        this.mBufferSize = i3;
        this.mSampleRate = i;
        this.mBits = i2;
        this.mDuration = 0;
        this.mFilledSize = 0;
        this.mState = 2;
    }

    public void setVolume(double d) {
        this.volume = d;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 2;
        }
    }

    public void start() {
        if (2 == this.mState) {
            this.mState = 1;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00fb, code lost:
    
        r11 = r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void gen(int r20, int r21) {
        /*
            Method dump skipped, instructions count: 285
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: voice.encoder.SignalGenerator.gen(int, int):void");
    }
}
