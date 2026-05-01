package voice.encoder;

import android.media.AudioTrack;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import voice.Buffer;
import voice.LogHelper;
import voice.decoder.PCM2Wav;

/* loaded from: classes.dex */
public class PcmPlayer {
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    private static final String TAG = "PcmPlayer";
    private static ByteOutputStream bos;
    private AudioTrack mAudio;
    private DataSource mCallback;
    private Listener mListener;
    private String outputFileName;
    private byte[] songBuffer;
    private int songBufferSize = 0;
    private int songPlayPos = 0;
    private int mState = 2;
    private long mPlayedLen = 0;

    public interface DataSource {
        void freePlayData(Buffer.BufferData bufferData);

        Buffer.BufferData getPlayBuffer();
    }

    public interface Listener {
        void onPlayStart();

        void onPlayStop();
    }

    public PcmPlayer(DataSource dataSource, int i, int i2, int i3, int i4) {
        this.mCallback = dataSource;
        this.mAudio = new AudioTrack(3, i, i2, i3, i4, 1);
    }

    public void setSaveData(String str) {
        this.outputFileName = str;
        if (bos == null) {
            bos = new ByteOutputStream();
        }
    }

    public void setMixPlaySound(String str) {
        File file;
        if (str != null) {
            try {
                file = new File(str);
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        } else {
            file = null;
        }
        if (file != null && file.exists()) {
            this.songBufferSize = (int) (file.length() - 44);
            this.songBufferSize -= this.songBufferSize % 4;
            FileInputStream fileInputStream = new FileInputStream(file);
            read(fileInputStream, new byte[44], 44);
            this.songBuffer = new byte[this.songBufferSize];
            this.songBufferSize = read(fileInputStream, this.songBuffer, this.songBufferSize);
            fileInputStream.close();
            return;
        }
        this.songBuffer = null;
        this.songBufferSize = 0;
        this.songPlayPos = 0;
    }

    public void setMixPlaySound(InputStream inputStream) {
        try {
            if (inputStream != null) {
                this.songBufferSize = inputStream.available() - 44;
                this.songBufferSize -= this.songBufferSize % 4;
                read(inputStream, new byte[44], 44);
                this.songBuffer = new byte[this.songBufferSize];
                this.songBufferSize = read(inputStream, this.songBuffer, this.songBufferSize);
            } else {
                this.songBuffer = null;
                this.songBufferSize = 0;
                this.songPlayPos = 0;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void resetMixPlayPos() {
        this.songPlayPos = 0;
    }

    private static int read(InputStream inputStream, byte[] bArr, int i) throws IOException {
        int i2 = 0;
        int i3 = 0;
        while (i2 >= 0 && i3 < i) {
            i2 = inputStream.read(bArr, i3, i - i3);
            i3 += i2;
        }
        return i3;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void start() throws IllegalStateException {
        LogHelper.d(TAG, "start");
        if (2 != this.mState || this.mAudio == null) {
            return;
        }
        this.mPlayedLen = 0L;
        if (this.mCallback != null) {
            this.mState = 1;
            LogHelper.d(TAG, "start");
            if (this.mListener != null) {
                this.mListener.onPlayStart();
            }
            while (true) {
                if (1 != this.mState) {
                    break;
                }
                LogHelper.d(TAG, "start getbuffer");
                Buffer.BufferData playBuffer = this.mCallback.getPlayBuffer();
                if (playBuffer != null) {
                    if (playBuffer.mData != null) {
                        int filledSize = playBuffer.getFilledSize();
                        if (this.songBuffer != null) {
                            for (int i = 0; i < filledSize; i++) {
                                if (this.songPlayPos >= this.songBufferSize) {
                                    this.songPlayPos = 0;
                                }
                                byte[] bArr = playBuffer.mData;
                                byte b = playBuffer.mData[i];
                                byte[] bArr2 = this.songBuffer;
                                int i2 = this.songPlayPos;
                                this.songPlayPos = i2 + 1;
                                bArr[i] = (byte) ((b + bArr2[i2]) / 2);
                            }
                        }
                        int iWrite = this.mAudio.write(playBuffer.mData, 0, filledSize);
                        if (bos != null && bos.size() < 10485760) {
                            bos.write(playBuffer.mData, 0, filledSize);
                        }
                        if (0 == this.mPlayedLen) {
                            this.mAudio.play();
                        }
                        this.mPlayedLen += iWrite;
                        this.mCallback.freePlayData(playBuffer);
                    } else {
                        LogHelper.d(TAG, "it is the end of input, so need stop");
                        break;
                    }
                } else {
                    LogHelper.e(TAG, "get null data");
                    break;
                }
            }
            if (this.mAudio != null) {
                this.mAudio.flush();
                this.mAudio.pause();
                this.mAudio.stop();
            }
            this.mState = 2;
            if (this.mListener != null) {
                this.mListener.onPlayStop();
            }
            LogHelper.d(TAG, "end");
            if (bos == null || this.outputFileName == null) {
                return;
            }
            try {
                PCM2Wav.save2Wave(this.outputFileName, bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.reset();
        }
    }

    public void stop() {
        if (1 != this.mState || this.mAudio == null) {
            return;
        }
        this.mState = 2;
    }
}
