package voice.decoder;

import android.text.TextUtils;
import voice.Buffer;
import voice.BufferSource;
import voice.CRC;
import voice.Constants;
import voice.DataSource;
import voice.LogHelper;
import voice.decoder.FFTVoiceRecognition;
import voice.decoder.Record;
import voice.reedsolomon.GenericGF;
import voice.reedsolomon.ReedSolomonDecoder;

/* loaded from: classes.dex */
public class VoiceRecognition implements Record.Listener, DataSource, BufferSource, FFTVoiceRecognition.RecognitionListener {
    private static final int STATE_PENDING = 3;
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    public static final int Status_ECCError = -2;
    public static final int Status_NotEnoughSignal = 100;
    public static final int Status_NotHeaderOrTail = 101;
    public static final int Status_RecogCountZero = 105;
    public static final int Status_Success = 0;
    private static final String TAG = "VoiceRecognition";
    final int ECCodeLen;
    ReedSolomonDecoder decoder;
    private Buffer mBuffer;
    private String mCodeBook;
    private VoiceRecognitionListener mListener;
    private int mMaxCodeIndex;
    private FFTVoiceRecognition mRecognition;
    private Thread mRecognitionThread;
    private Record mRecord;
    private Thread mRecordThread;
    private int mState;

    public enum AnalyseType {
        Signal,
        Directive,
        Both
    }

    public VoiceRecognition() {
        this(Constants.DEFAULT_CODE_BOOK);
    }

    private VoiceRecognition(String str) {
        this(str, Constants.DEFAULT_SAMPLE_RATE, 1024, 512, 3);
    }

    private VoiceRecognition(String str, int i, int i2, int i3, int i4) {
        this.ECCodeLen = 5;
        this.mState = 2;
        this.mRecord = new Record(this, i, 1, 2, i2, i3);
        this.mRecord.setListener(this);
        this.mBuffer = new Buffer(i4, this.mRecord.getByteBufferSize());
        this.mMaxCodeIndex = 17;
        setCodeBook(str);
        this.mRecognition = new FFTVoiceRecognition(this, this.mRecord.getFormat(), i2, i2, i3);
        this.mRecognition.setListener(this);
        this.decoder = new ReedSolomonDecoder(GenericGF.AZTEC_PARAM);
    }

    public void setFreqs(int[] iArr) {
        if (iArr == null || iArr.length != 19) {
            throw new RuntimeException("无效的频率设置");
        }
        this.mRecognition.setFreqs(iArr);
    }

    public void setListener(VoiceRecognitionListener voiceRecognitionListener) {
        this.mListener = voiceRecognitionListener;
    }

    public void setCodeBook(String str) {
        if (TextUtils.isEmpty(str) || str.length() > this.mMaxCodeIndex) {
            return;
        }
        this.mCodeBook = str;
    }

    public void start() {
        if (2 == this.mState) {
            this.mState = 3;
            this.mRecognitionThread = new Thread() { // from class: voice.decoder.VoiceRecognition.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    VoiceRecognition.this.mRecognition.start();
                }
            };
            if (this.mRecognitionThread != null) {
                this.mRecognitionThread.start();
            }
            this.mRecordThread = new Thread() { // from class: voice.decoder.VoiceRecognition.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws IllegalStateException {
                    VoiceRecognition.this.mRecord.start();
                    LogHelper.d(VoiceRecognition.TAG, "record thread end");
                    LogHelper.d(VoiceRecognition.TAG, "stop recognition start");
                    VoiceRecognition.this.stopRecognition();
                    LogHelper.d(VoiceRecognition.TAG, "stop recognition end");
                }
            };
            if (this.mRecordThread != null) {
                this.mRecordThread.start();
            }
            this.mState = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecognition() {
        this.mRecognition.stop();
        this.mBuffer.putFull(new Buffer.BufferData(0));
        if (this.mRecognitionThread != null) {
            try {
                try {
                    this.mRecognitionThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                this.mRecognitionThread = null;
            }
        }
        this.mBuffer.reset();
    }

    public void pauseSignal(long j) {
        this.mRecognition.pauseSignalRecognition(j);
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 3;
            LogHelper.d(TAG, "force stop start");
            this.mRecord.stop();
            if (this.mRecordThread != null) {
                try {
                    try {
                        this.mRecordThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    this.mRecordThread = null;
                }
            }
            this.mState = 2;
            LogHelper.d(TAG, "force stop end");
        }
    }

    @Override // voice.decoder.Record.Listener
    public void onStartRecord() {
        LogHelper.d(TAG, "start record");
    }

    @Override // voice.decoder.Record.Listener
    public void onStopRecord() {
        LogHelper.d(TAG, "stop record");
    }

    @Override // voice.BufferSource
    public Buffer.BufferData getBuffer() {
        Buffer.BufferData empty = this.mBuffer.getEmpty();
        if (empty == null) {
            LogHelper.e(TAG, "get null empty buffer");
        }
        return empty;
    }

    @Override // voice.BufferSource
    public void freeBuffer(Buffer.BufferData bufferData) {
        if (bufferData == null || this.mBuffer.putFull(bufferData)) {
            return;
        }
        LogHelper.e(TAG, "put full buffer failed");
    }

    @Override // voice.DataSource
    public void freeData(Buffer.BufferData bufferData) {
        if (bufferData == null || this.mBuffer.putEmpty(bufferData)) {
            return;
        }
        LogHelper.e(TAG, "put empty buffer failed");
    }

    @Override // voice.DataSource
    public Buffer.BufferData getData() {
        Buffer.BufferData full = this.mBuffer.getFull();
        if (full == null) {
            LogHelper.e(TAG, "get null full buffer");
        }
        return full;
    }

    @Override // voice.decoder.FFTVoiceRecognition.RecognitionListener
    public void onStartRecognition() {
        if (this.mListener != null) {
            this.mListener.onRecognitionStart();
        }
        LogHelper.d(TAG, "start recognition");
    }

    @Override // voice.decoder.FFTVoiceRecognition.RecognitionListener
    public void onStopRecognition(int i, int[] iArr, int i2) {
        int i3;
        String strDecode2String;
        if (i == 0) {
            strDecode2String = decode2String(iArr, i2, this.mCodeBook);
            i3 = strDecode2String == null ? -2 : 0;
        } else {
            i3 = i;
            strDecode2String = null;
        }
        if (this.mListener != null) {
            this.mListener.onRecognitionEnd(i3, strDecode2String);
        }
    }

    public static String decode2String(int[] iArr, int i, String str) {
        try {
            int[] iArr2 = new int[i];
            int i2 = 0;
            char c = 0;
            for (int i3 = 0; i3 < i; i3++) {
                char c2 = (char) iArr[i3];
                if ((i3 != 0 || c2 != 0) && (i3 != i - 1 || c2 != 18)) {
                    if (c2 != 17) {
                        c = (char) (((char) iArr[i3]) - 1);
                    }
                    iArr2[i2] = c;
                    i2++;
                }
            }
            if (i2 <= 4 || !decode(iArr2, i2)) {
                return null;
            }
            int i4 = i2 - 4;
            StringBuffer stringBuffer = new StringBuffer(10);
            for (int i5 = 0; i5 < i4; i5++) {
                stringBuffer.append(str.charAt(iArr2[i5]));
            }
            return stringBuffer.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private static boolean decode(int[] iArr, int i) {
        int i2 = i - 4;
        return CRC.calc_crc16(iArr, i2) == ((iArr[i - 1] & 15) | ((((iArr[i2] & 15) << 12) | ((iArr[i + (-3)] & 15) << 8)) | ((iArr[i + (-2)] & 15) << 4)));
    }
}
