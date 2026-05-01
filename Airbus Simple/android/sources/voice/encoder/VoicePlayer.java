package voice.encoder;

import android.text.TextUtils;
import com.litesuits.http.data.Consts;
import java.util.ArrayList;
import java.util.List;
import voice.Buffer;
import voice.CRC;
import voice.Constants;
import voice.LogHelper;
import voice.encoder.Encoder;
import voice.encoder.PcmPlayer;
import voice.reedsolomon.GenericGF;
import voice.reedsolomon.ReedSolomonEncoder;

/* loaded from: classes.dex */
public class VoicePlayer implements Encoder.Listener, Encoder.BufferSource, PcmPlayer.Listener, PcmPlayer.DataSource {
    private static final int DEFAULT_GEN_DURATION = 65;
    public static final int PLAY_PRIORITY_HIGH = 2;
    public static final int PLAY_PRIORITY_NORMAL = 1;
    private static final int STATE_PENDING = 3;
    private static final int STATE_START = 1;
    private static final int STATE_STOP = 2;
    private static final String TAG = "VoicePlayer";
    final int ECCodeLen;
    ReedSolomonEncoder encoder;
    private ArrayList<PlayJob> jobs;
    private Buffer mBuffer;
    private String mCodeBook;
    private Thread mEncodeThread;
    private Encoder mEncoder;
    private Listener mListener;
    private Thread mPlayThread;
    private PcmPlayer mPlayer;
    private int mState;

    public interface Listener {
        void onPlayEnd();

        void onPlayStart();
    }

    @Override // voice.encoder.Encoder.Listener
    public void onEndEncode() {
    }

    public static class PlayJob {
        public List<Integer> codes;
        public int[] frequencies;
        public int muteInterval;
        public long playCount;
        public int priority;

        private PlayJob(List<Integer> list, int[] iArr, long j, int i, int i2) {
            this.codes = null;
            this.frequencies = null;
            this.muteInterval = 0;
            this.priority = 1;
            this.playCount = Long.MAX_VALUE;
            this.codes = list;
            this.frequencies = iArr;
            this.playCount = j;
            this.muteInterval = i;
            this.priority = i2;
        }
    }

    public VoicePlayer() {
        this(Constants.DEFAULT_CODE_BOOK);
    }

    private VoicePlayer(String str) {
        this(str, Constants.DEFAULT_SAMPLE_RATE, 1024, 3);
    }

    private VoicePlayer(String str, int i, int i2, int i3) {
        this.jobs = new ArrayList<>(5);
        this.encoder = new ReedSolomonEncoder(GenericGF.AZTEC_PARAM);
        this.ECCodeLen = 5;
        this.mState = 2;
        this.mBuffer = new Buffer(i3, i2);
        this.mEncoder = new Encoder(this, i, 32768, i2);
        this.mEncoder.setListener(this);
        this.mPlayer = new PcmPlayer(this, i, 4, 2, i2);
        this.mPlayer.setListener(this);
        setCodeBook(str);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    private void setCodeBook(String str) {
        if (TextUtils.isEmpty(str) || str.length() >= this.mEncoder.getMaxCodeCount() - 1) {
            return;
        }
        this.mCodeBook = str;
    }

    private List<Integer> convertTextToCodes(String str) {
        int i;
        ArrayList arrayList = null;
        if (!TextUtils.isEmpty(str)) {
            ArrayList arrayList2 = new ArrayList(20);
            arrayList2.add(9);
            arrayList2.add(0);
            int length = str.length();
            int[] iArr = new int[length];
            int i2 = 0;
            while (true) {
                i = -1;
                if (i2 >= length) {
                    arrayList = arrayList2;
                    break;
                }
                char cCharAt = str.charAt(i2);
                int iIndexOf = this.mCodeBook.indexOf(cCharAt);
                if (iIndexOf <= -1) {
                    LogHelper.d(TAG, "invalidate char:" + cCharAt);
                    break;
                }
                iArr[i2] = iIndexOf;
                i2++;
            }
            if (arrayList != null) {
                int[] iArrRsEncode = rsEncode(this.encoder, iArr);
                StringBuffer stringBuffer = new StringBuffer(15);
                for (int i3 : iArrRsEncode) {
                    if (i3 != i) {
                        arrayList.add(Integer.valueOf(i3 + 1));
                        i = i3;
                    } else {
                        arrayList.add(17);
                        i = 17;
                    }
                    stringBuffer.append(this.mCodeBook.charAt(i3));
                }
                Constants.TestFullSignal = stringBuffer.toString();
                System.out.println(stringBuffer.toString());
                arrayList.add(18);
                arrayList.add(9);
            }
        }
        return arrayList;
    }

    private static int[] rsEncode(ReedSolomonEncoder reedSolomonEncoder, int[] iArr) {
        int[] iArr2 = new int[iArr.length + 4];
        System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
        int iCalc_crc16 = CRC.calc_crc16(iArr, iArr.length);
        iArr2[iArr.length] = (iCalc_crc16 >> 12) & 15;
        iArr2[iArr.length + 1] = (iCalc_crc16 >> 8) & 15;
        iArr2[iArr.length + 2] = (iCalc_crc16 >> 4) & 15;
        iArr2[iArr.length + 3] = iCalc_crc16 & 15;
        return iArr2;
    }

    private void play(String str) {
        play(str, 1L, 0);
    }

    public void setVolume(double d) {
        this.mEncoder.setVolume(d);
    }

    public void setFreqs(int[] iArr) {
        if (iArr == null || iArr.length != 19) {
            throw new RuntimeException("无效的频率设置");
        }
        this.mEncoder.setFreqs(iArr);
    }

    public void play(String str, long j, int i) {
        play(str, j, i, 1);
    }

    private void play(String str, long j, int i, int i2) {
        List<Integer> listConvertTextToCodes;
        if (this.mCodeBook != null && (listConvertTextToCodes = convertTextToCodes(str)) != null) {
            this.jobs.add(new PlayJob(listConvertTextToCodes, this.mEncoder.getCodeFrequency(), j, i, i2));
        }
        doPlay();
    }

    private void doPlay() {
        if (2 == this.mState) {
            this.mState = 3;
            this.mPlayThread = new Thread() { // from class: voice.encoder.VoicePlayer.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws IllegalStateException {
                    VoicePlayer.this.mPlayer.start();
                }
            };
            if (this.mPlayThread != null) {
                this.mPlayThread.start();
            }
            this.mEncodeThread = new Thread() { // from class: voice.encoder.VoicePlayer.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    int iSearchPlayingJob = VoicePlayer.this.searchPlayingJob();
                    do {
                        PlayJob playJob = (PlayJob) VoicePlayer.this.jobs.get(iSearchPlayingJob);
                        LogHelper.d(VoicePlayer.TAG, "encode start");
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("play:");
                        for (int i = 0; i < playJob.codes.size(); i++) {
                            stringBuffer.append(playJob.codes.get(i));
                            stringBuffer.append(Consts.SECOND_LEVEL_SPLIT);
                        }
                        System.out.println(stringBuffer);
                        VoicePlayer.this.mEncoder.encode(playJob.codes, playJob.frequencies, 65, playJob.playCount - 1 > 0 ? playJob.muteInterval : 0);
                        LogHelper.d(VoicePlayer.TAG, "encode end");
                        VoicePlayer.this.mEncoder.stop();
                        long j = playJob.playCount - 1;
                        playJob.playCount = j;
                        if (j <= 0) {
                            VoicePlayer.this.jobs.remove(iSearchPlayingJob);
                        }
                        iSearchPlayingJob = VoicePlayer.this.searchPlayingJob();
                        if (iSearchPlayingJob < 0) {
                            break;
                        }
                    } while (3 != VoicePlayer.this.mState);
                    VoicePlayer.this.stopPlayer();
                }
            };
            if (this.mEncodeThread != null) {
                this.mEncodeThread.start();
            }
            LogHelper.d(TAG, "play");
            this.mState = 1;
            return;
        }
        this.mEncoder.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int searchPlayingJob() {
        int i = 0;
        int i2 = -1;
        for (int i3 = 0; i3 < this.jobs.size(); i3++) {
            PlayJob playJob = this.jobs.get(i3);
            if (playJob.priority > i) {
                i = playJob.priority;
                i2 = i3;
            }
        }
        return i2;
    }

    public void stop() {
        if (1 == this.mState) {
            this.mState = 3;
            LogHelper.d(TAG, "force stop start");
            this.mEncoder.stop();
            if (this.mEncodeThread != null) {
                try {
                    try {
                        this.mEncodeThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    this.mEncodeThread = null;
                }
            }
            this.jobs.clear();
            LogHelper.d(TAG, "force stop end");
        }
    }

    public boolean isStopped() {
        return this.mState == 2 || (this.mEncodeThread == null && this.mPlayThread == null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPlayer() {
        if (this.mEncoder.isStoped()) {
            this.mPlayer.stop();
        }
        this.mBuffer.putFull(Buffer.BufferData.getEmptyBuffer());
        if (this.mPlayThread != null) {
            try {
                try {
                    this.mPlayThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                this.mPlayThread = null;
            }
        }
        this.mBuffer.reset();
        this.mState = 2;
    }

    @Override // voice.encoder.Encoder.Listener
    public void onStartEncode() {
        LogHelper.d(TAG, "onStartGen");
    }

    @Override // voice.encoder.Encoder.BufferSource
    public void freeEncodeBuffer(Buffer.BufferData bufferData) {
        if (bufferData != null) {
            this.mBuffer.putFull(bufferData);
        }
    }

    @Override // voice.encoder.Encoder.BufferSource
    public Buffer.BufferData getEncodeBuffer() {
        return this.mBuffer.getEmpty();
    }

    @Override // voice.encoder.PcmPlayer.DataSource
    public Buffer.BufferData getPlayBuffer() {
        return this.mBuffer.getFull();
    }

    @Override // voice.encoder.PcmPlayer.DataSource
    public void freePlayData(Buffer.BufferData bufferData) {
        this.mBuffer.putEmpty(bufferData);
    }

    @Override // voice.encoder.PcmPlayer.Listener
    public void onPlayStart() {
        if (this.mListener != null) {
            this.mListener.onPlayStart();
        }
    }

    @Override // voice.encoder.PcmPlayer.Listener
    public void onPlayStop() {
        if (this.mListener != null) {
            this.mListener.onPlayEnd();
        }
    }

    public PcmPlayer getmPlayer() {
        return this.mPlayer;
    }
}
