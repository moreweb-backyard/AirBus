package voice.decoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import voice.Constants;
import voice.LogHelper;
import voice.decoder.FFTVoiceRecognition;

/* loaded from: classes.dex */
public class SignalAnalyser {
    private static final String CODEBOOK = "0123456789abcdef";
    private static final double REF_FREQ = 8.17579892d;
    public static final int Recog_NotEnoughSignal = 100;
    public static final int Recog_NotHeaderOrTail = 101;
    public static final int Recog_RecogCountZero = 105;
    public static final int Recog_Success = 0;
    protected static final int maxFreqYDistance = 5;
    protected static final int maxMainMatchCount = 5;
    protected static final int maxPruneSignalEventCount = 5;
    protected static final int maxShortFreqCount = 2;
    protected static final int maxSignalEventCount = 7;
    protected static final int minBigMatchCount = 2;
    protected static final int minPruneSignalEventCount = 4;
    protected static final int normalFreqYDistance = 4;
    int[] CODE_FREQUENCY;
    int bufferSize;
    FFTVoiceRecognition.RecognitionListener listener;
    int maxFreqIdx;
    int maxFrequency;
    int minFreqIdx;
    int minFrequency;
    int overlap;
    int pitchDistance;
    private static final double LOG_TWO = Math.log(2.0d);
    private static final char[] CODE_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    int minFreqPixY = 0;
    int maxFreqPixY = 0;
    int[] CODE_Y = null;
    int totalSignalCount = 0;
    int okSignalCount = 0;
    List<FrequencyInfo> signals = new ArrayList();
    TreeSet<FrequencyInfo> checkingFreq = new TreeSet<>();
    List<FrequencyInfo> checkedFreqs = new ArrayList(20);
    long timeIdx = 0;
    long time = 0;
    int getHeight = 1250;
    FreqAmplitude[] pixeledAmplitudes = new FreqAmplitude[this.getHeight];
    protected final int maxValidFreqCount = 1;
    protected final int freqCountLong = 3;
    float eventTotalAmplitude = 0.0f;
    int protocolLength = 10;
    final int duration = 65;
    float sampleRate = 44100.0f;
    FileOutputStream fos = null;
    int maxIdx2TimeCount = 200;
    long preDelTimeIdx = -1;
    Map<Long, EventInfo> idx2Times = new HashMap(350);
    StringBuffer sb = new StringBuffer(20480);
    boolean normalAmplitudeTop2AvgInited = false;
    int minNormalAmplitudeCount = 50;
    List<Double> findNormalAmplitudeTop2Avg = new ArrayList(100);
    double normalAmplitudeTop2Avg = 20.0d;
    double minPeakAmplitudeTop2Avg = this.normalAmplitudeTop2Avg * 1.4d;
    long preSignalTime = 0;
    long preValidFreqAmplitudeTime = 0;
    int AudioBufferSize = 512000;
    ByteArrayOutputStream bos = null;
    boolean logErrorSignals = false;
    boolean logEvents = true;
    int minRegcoSignalCount = this.protocolLength;
    StringBuffer logBuffer = new StringBuffer();
    boolean recoging = false;
    Comparator freqAmplitudeComparator = new FreqAmplitudeComparator();
    Comparator freqYComparator = new Comparator<FreqAmplitude>() { // from class: voice.decoder.SignalAnalyser.1
        @Override // java.util.Comparator
        public int compare(FreqAmplitude freqAmplitude, FreqAmplitude freqAmplitude2) {
            return freqAmplitude.frequencyY - freqAmplitude2.frequencyY;
        }
    };
    TreeSet<FreqAmplitude> validFreqs = new TreeSet<>(this.freqAmplitudeComparator);
    TreeSet<FreqAmplitude> top2Freqs = new TreeSet<>(this.freqAmplitudeComparator);

    private static class FreqAmplitudeComparator implements Comparator<FreqAmplitude> {
        private FreqAmplitudeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(FreqAmplitude freqAmplitude, FreqAmplitude freqAmplitude2) {
            double d = freqAmplitude.amplitude - freqAmplitude2.amplitude;
            if (d > 0.0d) {
                return 1;
            }
            if (d != 0.0d) {
                return -1;
            }
            if (freqAmplitude == freqAmplitude2) {
                return 0;
            }
            return freqAmplitude.hashCode() - freqAmplitude2.hashCode();
        }
    }

    public SignalAnalyser(int[] iArr, int i, int i2, int i3, int i4) {
        this.CODE_FREQUENCY = null;
        this.minFrequency = Constants.MIN_FREQUENCY;
        this.maxFrequency = Constants.MAX_FREQUENCY;
        this.bufferSize = 0;
        this.overlap = 0;
        this.minFreqIdx = 0;
        this.maxFreqIdx = 0;
        this.CODE_FREQUENCY = iArr;
        this.minFrequency = i;
        this.maxFrequency = i2;
        this.bufferSize = i3;
        this.overlap = i4;
        this.minFreqIdx = (this.minFrequency * this.bufferSize) / Constants.DEFAULT_SAMPLE_RATE;
        this.maxFreqIdx = (this.maxFrequency * this.bufferSize) / Constants.DEFAULT_SAMPLE_RATE;
    }

    public void setFreqs(int[] iArr) {
        this.CODE_FREQUENCY = iArr;
        for (int i = 0; i < iArr.length; i++) {
            if (iArr[i] < this.minFrequency) {
                this.minFrequency = iArr[i];
            }
            if (iArr[i] > this.maxFrequency) {
                this.maxFrequency = iArr[i];
            }
        }
        this.minFrequency -= 200;
        this.maxFrequency += 200;
        this.minFreqIdx = (this.minFrequency * this.bufferSize) / Constants.DEFAULT_SAMPLE_RATE;
        this.maxFreqIdx = (this.maxFrequency * this.bufferSize) / Constants.DEFAULT_SAMPLE_RATE;
    }

    public void setMinRegcoSignalCount(int i) {
        this.minRegcoSignalCount = i;
    }

    public void setProtocolLength(int i) {
        this.protocolLength = i;
        this.minRegcoSignalCount = i;
    }

    public double binToHz(int i, float f, int i2) {
        double d = i * f;
        double d2 = i2;
        Double.isNaN(d);
        Double.isNaN(d2);
        return d / d2;
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x02d1  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x07ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void analyFFT(voice.decoder.AudioData r53, float[] r54, int r55) {
        /*
            Method dump skipped, instructions count: 3833
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: voice.decoder.SignalAnalyser.analyFFT(voice.decoder.AudioData, float[], int):void");
    }

    private void initCodeY() {
        if (this.CODE_Y == null) {
            this.CODE_Y = new int[this.CODE_FREQUENCY.length];
            for (int i = 0; i < this.CODE_FREQUENCY.length; i++) {
                this.CODE_Y[i] = frequencyToBin(this.CODE_FREQUENCY[i]);
            }
            this.pitchDistance = this.CODE_Y[2] - this.CODE_Y[1];
        }
    }

    private int getIdxFromFrequency(int i) {
        int iBinarySearch = Arrays.binarySearch(this.CODE_Y, 1, this.CODE_Y.length - 1, i);
        if (iBinarySearch >= 0) {
            return iBinarySearch;
        }
        int i2 = -(iBinarySearch + 1);
        int i3 = i2 == 0 ? 1000 : i - this.CODE_Y[i2 - 1];
        int i4 = i2 < this.CODE_Y.length ? this.CODE_Y[i2] - i : 1000;
        if (i3 <= i4) {
            if (i3 <= 5) {
                return i2 - 1;
            }
            return -1;
        }
        if (i4 <= 5) {
            return i2;
        }
        return -1;
    }

    protected void info(String str) {
        LogHelper.i("signalAnalyser", str);
    }

    private float sumFreqRangeAmplitude(FreqAmplitude[] freqAmplitudeArr, int i, float f) {
        FreqAmplitude freqAmplitude;
        float f2 = f * 4.0f;
        float f3 = 0.0f;
        for (int i2 = i - 5; i2 <= i + 5; i2++) {
            if (i2 >= 0 && i2 <= freqAmplitudeArr.length && (freqAmplitude = freqAmplitudeArr[i2]) != null && freqAmplitude.amplitude > f2) {
                f3 += freqAmplitude.amplitude;
            }
        }
        return f3;
    }

    public static double hertzToAbsoluteCent(double d) {
        if (d > 0.0d) {
            return (Math.log(d / REF_FREQ) * 1200.0d) / LOG_TWO;
        }
        throw new IllegalArgumentException("Pitch in Hz schould be greater than zero, is " + d);
    }

    private int frequencyToBin(double d) {
        if (d == 0.0d || d <= 50.0d || d >= 22000.0d) {
            return 0;
        }
        double d2 = this.getHeight;
        Double.isNaN(d2);
        return ((int) (((d - 50.0d) / 22000.0d) * d2)) + 1;
    }

    public int getMinFreqIdx() {
        return this.minFreqIdx;
    }

    public int getMaxFreqIdx() {
        return this.maxFreqIdx;
    }

    public FFTVoiceRecognition.RecognitionListener getListener() {
        return this.listener;
    }

    public void setListener(FFTVoiceRecognition.RecognitionListener recognitionListener) {
        this.listener = recognitionListener;
    }

    public boolean isLogErrorSignals() {
        return this.logErrorSignals;
    }

    public void setLogErrorSignals(boolean z) {
        this.logErrorSignals = z;
    }
}
