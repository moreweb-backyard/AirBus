package voice.encoder;

import com.litesuits.http.HttpConfig;
import java.util.Iterator;
import java.util.List;
import voice.Buffer;
import voice.Constants;
import voice.LogHelper;
import voice.encoder.SignalGenerator;

/* loaded from: classes.dex */
public class Encoder implements SignalGenerator.Listener, SignalGenerator.Callback {
    private static final int[] DIRECTIVE_FREQUENCY = {19000, 19200, 19400, 19600, 19800, HttpConfig.DEFAULT_TIMEOUT};
    private static final int STATE_ENCODING = 1;
    private static final int STATE_STOPED = 2;
    private static final String TAG = "Encoder";
    private BufferSource mCallback;
    private Listener mListener;
    private SignalGenerator mSignalGenerator;
    private int[] CODE_FREQUENCY = Constants.CODE_FREQUENCY;
    private int mState = 2;

    public interface BufferSource {
        void freeEncodeBuffer(Buffer.BufferData bufferData);

        Buffer.BufferData getEncodeBuffer();
    }

    public interface Listener {
        void onEndEncode();

        void onStartEncode();
    }

    public final int getMaxCodeCount() {
        return 19;
    }

    public Encoder(BufferSource bufferSource, int i, int i2, int i3) {
        this.mCallback = bufferSource;
        this.mSignalGenerator = new SignalGenerator(this, i, i2, i3);
        this.mSignalGenerator.setListener(this);
    }

    public void setFreqs(int[] iArr) {
        this.CODE_FREQUENCY = iArr;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public final boolean isStoped() {
        return 2 == this.mState;
    }

    public void setVolume(double d) {
        this.mSignalGenerator.setVolume(d);
    }

    public void encode(List<Integer> list, int i) {
        encode(list, this.CODE_FREQUENCY, i, 0);
    }

    public void encode(List<Integer> list, int[] iArr, int i, int i2) {
        if (2 == this.mState) {
            this.mState = 1;
            if (this.mListener != null) {
                this.mListener.onStartEncode();
            }
            this.mSignalGenerator.start();
            Iterator<Integer> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int iIntValue = it.next().intValue();
                if (1 == this.mState) {
                    LogHelper.d(TAG, "encode:" + iIntValue);
                    if (iIntValue >= 0 && iIntValue < iArr.length) {
                        this.mSignalGenerator.gen(iArr[iIntValue], i);
                    } else {
                        LogHelper.e(TAG, "code index error");
                    }
                } else {
                    LogHelper.d(TAG, "encode force stop");
                    break;
                }
            }
            if (1 != this.mState) {
                LogHelper.d(TAG, "encode force stop");
            } else if (i2 > 0) {
                this.mSignalGenerator.gen(0, i2);
            }
            stop();
            if (this.mListener != null) {
                this.mListener.onEndEncode();
            }
        }
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 2;
            this.mSignalGenerator.stop();
        }
    }

    @Override // voice.encoder.SignalGenerator.Listener
    public void onStartGen() {
        LogHelper.d(TAG, "start gen codes");
    }

    @Override // voice.encoder.SignalGenerator.Listener
    public void onStopGen() {
        LogHelper.d(TAG, "end gen codes");
    }

    @Override // voice.encoder.SignalGenerator.Callback
    public Buffer.BufferData getGenBuffer() {
        if (this.mCallback != null) {
            return this.mCallback.getEncodeBuffer();
        }
        return null;
    }

    @Override // voice.encoder.SignalGenerator.Callback
    public void freeGenBuffer(Buffer.BufferData bufferData) {
        if (this.mCallback != null) {
            this.mCallback.freeEncodeBuffer(bufferData);
        }
    }

    public int[] getCodeFrequency() {
        return this.CODE_FREQUENCY;
    }

    public static int[] getDirectiveFrequency() {
        return DIRECTIVE_FREQUENCY;
    }
}
