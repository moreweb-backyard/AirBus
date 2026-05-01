package voice.decoder;

import java.util.concurrent.ExecutionException;
import voice.Buffer;
import voice.Constants;
import voice.DataSource;
import voice.LogHelper;
import voice.decoder.VoiceRecognition;
import voice.decoder.fft.AudioFormat;
import voice.decoder.fft.FFT;

/* loaded from: classes.dex */
public class FFTVoiceRecognition {
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    private static final String TAG = "FFTVoiceRecognition";
    float[] amplitudes;
    BufferDataData audioData;
    DataSource dataSource;
    FFT fft;
    SignalAnalyser signalAnalyser;
    float[] transformbuffer;
    long signalContinueTime = 0;
    long directiveContinueTime = 0;
    private VoiceRecognition.AnalyseType analyseType = VoiceRecognition.AnalyseType.Both;
    private int mState = 2;

    public interface RecognitionListener {
        void onStartRecognition();

        void onStopRecognition(int i, int[] iArr, int i2);
    }

    public static class BufferDataData implements AudioData {
        Buffer.BufferData data;

        public Buffer.BufferData getData() {
            return this.data;
        }

        public void setData(Buffer.BufferData bufferData) {
            this.data = bufferData;
        }

        @Override // voice.decoder.AudioData
        public byte[] getByteBuffer() {
            return this.data.mData;
        }

        @Override // voice.decoder.AudioData
        public int getOverlap() {
            return this.data.getFloatOverlapSize();
        }

        @Override // voice.decoder.AudioData
        public int getBufferSize() {
            return this.data.getFloatBufferSize();
        }
    }

    public FFTVoiceRecognition(DataSource dataSource, AudioFormat audioFormat, int i, int i2, int i3) {
        this.fft = new FFT(i);
        this.amplitudes = new float[i / 2];
        this.dataSource = dataSource;
        this.signalAnalyser = new SignalAnalyser(Constants.CODE_FREQUENCY, Constants.MIN_FREQUENCY, Constants.MAX_FREQUENCY, i2, i3) { // from class: voice.decoder.FFTVoiceRecognition.1
            @Override // voice.decoder.SignalAnalyser
            protected void info(String str) {
                LogHelper.d("signalAnalyser", str);
            }
        };
        this.signalAnalyser.setLogErrorSignals(true);
        this.audioData = new BufferDataData();
        this.transformbuffer = new float[i * 2];
    }

    public void setFreqs(int[] iArr) {
        this.signalAnalyser.setFreqs(iArr);
    }

    public VoiceRecognition.AnalyseType getAnalyseType() {
        return this.analyseType;
    }

    public void setAnalyseType(VoiceRecognition.AnalyseType analyseType) {
        this.analyseType = analyseType;
    }

    public void start() {
        if (2 != this.mState || this.dataSource == null) {
            return;
        }
        this.mState = 1;
        while (true) {
            if (1 == this.mState) {
                Buffer.BufferData data = this.dataSource.getData();
                if (data != null) {
                    process(data);
                    if (data.mData != null) {
                        this.dataSource.freeData(data);
                    } else {
                        LogHelper.d(TAG, "end input buffer, so stop");
                        break;
                    }
                } else {
                    LogHelper.e(TAG, "get null recognition buffer");
                    break;
                }
            } else {
                break;
            }
        }
        this.mState = 2;
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 2;
        }
    }

    private void process(Buffer.BufferData bufferData) throws ExecutionException, InterruptedException {
        float[] fArr;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (bufferData.mData == null) {
            fArr = null;
        } else {
            if (((this.analyseType != VoiceRecognition.AnalyseType.Both && this.analyseType != VoiceRecognition.AnalyseType.Signal) || jCurrentTimeMillis <= this.signalContinueTime) && ((this.analyseType != VoiceRecognition.AnalyseType.Both && this.analyseType != VoiceRecognition.AnalyseType.Directive) || jCurrentTimeMillis <= this.directiveContinueTime)) {
                return;
            }
            System.arraycopy(bufferData.floatData, 0, this.transformbuffer, 0, bufferData.floatData.length);
            this.fft.forwardTransform(this.transformbuffer);
            int maxFreqIdx = this.signalAnalyser.getMaxFreqIdx();
            for (int minFreqIdx = this.signalAnalyser.getMinFreqIdx(); minFreqIdx < maxFreqIdx; minFreqIdx++) {
                this.amplitudes[minFreqIdx] = this.fft.modulus(this.transformbuffer, minFreqIdx);
            }
            fArr = this.amplitudes;
        }
        this.audioData.setData(bufferData);
        if ((this.analyseType == VoiceRecognition.AnalyseType.Both || this.analyseType == VoiceRecognition.AnalyseType.Signal) && jCurrentTimeMillis > this.signalContinueTime) {
            this.signalAnalyser.analyFFT(this.audioData, fArr, this.fft.getFFTSize());
        }
    }

    public RecognitionListener getListener() {
        return this.signalAnalyser.getListener();
    }

    public void setListener(RecognitionListener recognitionListener) {
        this.signalAnalyser.setListener(recognitionListener);
    }

    public void pauseDirectiveRecognition(long j) {
        this.directiveContinueTime = System.currentTimeMillis() + j;
    }

    public void pauseSignalRecognition(long j) {
        this.signalContinueTime = System.currentTimeMillis() + j;
    }
}
