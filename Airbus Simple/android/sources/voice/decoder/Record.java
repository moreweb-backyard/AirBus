package voice.decoder;

import android.media.AudioRecord;
import voice.Buffer;
import voice.BufferSource;
import voice.LogHelper;
import voice.decoder.fft.AudioFloatConverter;
import voice.decoder.fft.AudioFormat;
import voice.encoder.SignalGenerator;

/* loaded from: classes.dex */
public class Record {
    public static final int BITS_16 = 2;
    public static final int BITS_8 = 1;
    public static final int CHANNEL_1 = 1;
    public static final int CHANNEL_2 = 2;
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    private static final String TAG = "Record";
    private byte[] buffer;
    private int byteOverlap;
    private int byteStepSize;
    AudioFloatConverter converter;
    private float[] floatBuffer;
    private int floatOverlap;
    private int floatStepSize;
    AudioFormat format;
    private int mBits;
    private int mBufferSize;
    private BufferSource mCallback;
    private int mChannel;
    private int mFrequence;
    private Listener mListener;
    private int overlap;
    private int mChannelConfig = 16;
    private int mAudioEncoding = 2;
    private int mState = 2;

    public interface Listener {
        void onStartRecord();

        void onStopRecord();
    }

    public Record(BufferSource bufferSource, int i, int i2, int i3, int i4, int i5) {
        this.mFrequence = SignalGenerator.SAMPLE_RATE_8;
        this.mChannel = 1;
        this.mBits = 1;
        this.mCallback = bufferSource;
        this.mFrequence = i;
        this.mChannel = i2;
        this.mBits = i3;
        this.overlap = i5;
        this.format = new AudioFormat(i, i3 * 8, i2, true, false);
        this.converter = AudioFloatConverter.getConverter(this.format);
        this.floatOverlap = i5;
        this.floatStepSize = i4 - this.floatOverlap;
        this.floatBuffer = new float[i4];
        this.byteOverlap = this.floatOverlap * this.format.getFrameSize();
        this.byteStepSize = this.floatStepSize * this.format.getFrameSize();
        this.mBufferSize = this.byteOverlap + this.byteStepSize;
        this.buffer = new byte[this.mBufferSize];
    }

    public int getByteBufferSize() {
        return this.mBufferSize;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void start() throws IllegalStateException {
        int i;
        int i2;
        int i3;
        if (2 == this.mState) {
            switch (this.mChannel) {
                case 1:
                    this.mChannelConfig = 16;
                    break;
                case 2:
                    this.mChannelConfig = 12;
                    break;
            }
            switch (this.mBits) {
                case 1:
                    this.mAudioEncoding = 3;
                    break;
                case 2:
                    this.mAudioEncoding = 2;
                    break;
            }
            int minBufferSize = AudioRecord.getMinBufferSize(this.mFrequence, this.mChannelConfig, this.mAudioEncoding);
            LogHelper.d(TAG, "minBufferSize:" + minBufferSize);
            if (minBufferSize >= minBufferSize) {
                AudioRecord audioRecord = new AudioRecord(1, this.mFrequence, this.mChannelConfig, this.mAudioEncoding, minBufferSize);
                try {
                    this.mState = 1;
                    audioRecord.startRecording();
                    LogHelper.d(TAG, "record start");
                    if (this.mCallback != null) {
                        if (this.mListener != null) {
                            this.mListener.onStartRecord();
                        }
                        boolean z = true;
                        while (true) {
                            if (1 == this.mState) {
                                Buffer.BufferData buffer = this.mCallback.getBuffer();
                                if (buffer != null) {
                                    if (buffer.mData != null) {
                                        int i4 = this.floatOverlap + this.floatStepSize;
                                        int i5 = this.mBufferSize;
                                        if (z) {
                                            i = i4;
                                            i2 = 0;
                                            i3 = 0;
                                        } else {
                                            int i6 = this.floatOverlap;
                                            int i7 = this.floatStepSize;
                                            int i8 = this.byteOverlap;
                                            int i9 = this.byteStepSize;
                                            System.arraycopy(this.floatBuffer, this.floatStepSize, this.floatBuffer, 0, this.floatOverlap);
                                            i3 = i6;
                                            i = i7;
                                            i2 = i8;
                                            i5 = i9;
                                        }
                                        int i10 = 0;
                                        int i11 = 0;
                                        while (i10 != -1 && i11 < i5) {
                                            i10 = audioRecord.read(this.buffer, i2 + i11, i5 - i11);
                                            i11 += i10;
                                        }
                                        System.arraycopy(this.buffer, 0, buffer.mData, 0, buffer.mData.length);
                                        buffer.setFilledSize(i11 + i2);
                                        if (buffer.floatData == null) {
                                            buffer.floatData = new float[this.floatOverlap + this.floatStepSize];
                                            buffer.setFloatBufferSize(this.floatOverlap + this.floatStepSize);
                                            buffer.setFloatOverlapSize(this.floatOverlap);
                                        }
                                        this.converter.toFloatArray(buffer.mData, i2, this.floatBuffer, i3, i);
                                        System.arraycopy(this.floatBuffer, 0, buffer.floatData, 0, buffer.floatData.length);
                                        this.mCallback.freeBuffer(buffer);
                                        z = false;
                                    } else {
                                        LogHelper.d(TAG, "get end input data, so stop");
                                    }
                                } else {
                                    LogHelper.e(TAG, "get null data");
                                }
                            }
                        }
                        if (this.mListener != null) {
                            this.mListener.onStopRecord();
                        }
                    }
                    audioRecord.stop();
                    audioRecord.release();
                    LogHelper.d(TAG, "record stop");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    LogHelper.e(TAG, "start record error");
                }
                this.mState = 2;
                return;
            }
            LogHelper.e(TAG, "bufferSize is too small");
        }
    }

    public int getState() {
        return this.mState;
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 2;
        }
    }
}
