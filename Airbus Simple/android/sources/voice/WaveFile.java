package voice;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class WaveFile {
    private static int lenaudioformat = 2;
    private static int lenbitspersample = 2;
    private static int lenblockling = 2;
    private static int lenbyterate = 4;
    private static int lenchunkdescriptor = 4;
    private static int lenchunksize = 4;
    private static int lendatasubchunk = 4;
    private static int lenfmtubchunk = 4;
    private static int lennumchannels = 2;
    private static int lensamplerate = 2;
    private static int lensubchunk1size = 4;
    private static int lensubchunk2size = 4;
    private static int lenwaveflag = 4;
    private String filename = null;
    private byte[] data = null;
    private int len = 0;
    private String chunkdescriptor = null;
    private long chunksize = 0;
    private String waveflag = null;
    private String fmtubchunk = null;
    private long subchunk1size = 0;
    private int audioformat = 0;
    private int numchannels = 0;
    private long samplerate = 0;
    private long byterate = 0;
    private int blockalign = 0;
    private int bitspersample = 0;
    private String datasubchunk = null;
    private long subchunk2size = 0;
    private FileInputStream fis = null;
    private BufferedInputStream bis = null;
    private boolean issuccess = false;

    public WaveFile(String str) throws IOException {
        initReader(str);
    }

    public boolean isSuccess() {
        return this.issuccess;
    }

    public int getBitPerSample() {
        return this.bitspersample;
    }

    public long getSampleRate() {
        return this.samplerate;
    }

    public int getNumChannels() {
        return this.numchannels;
    }

    public int getDataLen() {
        return this.len;
    }

    public byte[] getData() {
        return this.data;
    }

    private void initReader(String str) throws IOException {
        this.filename = str;
        try {
            this.fis = new FileInputStream(this.filename);
            this.bis = new BufferedInputStream(this.fis);
            this.chunkdescriptor = readString(lenchunkdescriptor);
            if (!this.chunkdescriptor.endsWith("RIFF")) {
                throw new IllegalArgumentException("RIFF miss, " + str + " is not a wave file.");
            }
            this.chunksize = readLong();
            this.waveflag = readString(lenwaveflag);
            if (!this.waveflag.endsWith("WAVE")) {
                throw new IllegalArgumentException("WAVE miss, " + str + " is not a wave file.");
            }
            this.fmtubchunk = readString(lenfmtubchunk);
            if (!this.fmtubchunk.endsWith("fmt ")) {
                throw new IllegalArgumentException("fmt miss, " + str + " is not a wave file.");
            }
            this.subchunk1size = readLong();
            this.audioformat = readInt();
            this.numchannels = readInt();
            this.samplerate = readLong();
            this.byterate = readLong();
            this.blockalign = readInt();
            this.bitspersample = readInt();
            this.datasubchunk = readString(lendatasubchunk);
            if (!this.datasubchunk.endsWith("data")) {
                throw new IllegalArgumentException("data miss, " + str + " is not a wave file.");
            }
            this.subchunk2size = readLong();
            this.len = (int) this.subchunk2size;
            this.data = readBytes(this.len);
            this.issuccess = true;
        } finally {
            if (this.bis != null) {
                this.bis.close();
            }
            if (this.fis != null) {
                this.fis.close();
            }
        }
    }

    private String readString(int i) throws IOException {
        byte[] bArr = new byte[i];
        if (this.bis.read(bArr) != i) {
            throw new IOException("no more data!!!");
        }
        return new String(bArr);
    }

    private int readInt() throws IOException {
        byte[] bArr = new byte[2];
        if (this.bis.read(bArr) != 2) {
            throw new IOException("no more data!!!");
        }
        return (bArr[0] & 255) | (bArr[1] << 8);
    }

    private long readLong() throws IOException {
        long[] jArr = new long[4];
        for (int i = 0; i < 4; i++) {
            jArr[i] = this.bis.read();
            if (jArr[i] == -1) {
                throw new IOException("no more data!!!");
            }
        }
        return (jArr[3] << 24) | jArr[0] | (jArr[1] << 8) | (jArr[2] << 16);
    }

    private byte[] readBytes(int i) throws IOException {
        byte[] bArr = new byte[i];
        if (this.bis.read(bArr) == i) {
            return bArr;
        }
        throw new IOException("no more data!!!");
    }
}
