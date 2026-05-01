package voice.decoder;

import cz.msebera.android.httpclient.message.TokenParser;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import voice.Constants;

/* loaded from: classes.dex */
public class PCM2Wav {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static void save2Wave(String str, byte[] bArr, int i, int i2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        WaveHeader waveHeader = new WaveHeader();
        waveHeader.fileLength = i2 + 36;
        waveHeader.FmtHdrLeth = 16;
        waveHeader.BitsPerSample = (short) 16;
        waveHeader.Channels = (short) 1;
        waveHeader.FormatTag = (short) 1;
        waveHeader.SamplesPerSec = Constants.DEFAULT_SAMPLE_RATE;
        waveHeader.BlockAlign = (short) ((waveHeader.Channels * waveHeader.BitsPerSample) / 8);
        waveHeader.AvgBytesPerSec = waveHeader.BlockAlign * waveHeader.SamplesPerSec;
        waveHeader.DataHdrLeth = i2;
        byte[] header = waveHeader.getHeader();
        fileOutputStream.write(header, 0, header.length);
        fileOutputStream.write(bArr, i, i2);
        fileOutputStream.flush();
        fileOutputStream.close();
        System.out.println("wav saved!");
    }

    public static void save2Wave(String str, byte[] bArr) throws IOException {
        save2Wave(str, bArr, 0, bArr.length);
    }

    public static class WaveHeader {
        public int AvgBytesPerSec;
        public short BitsPerSample;
        public short BlockAlign;
        public short Channels;
        public int DataHdrLeth;
        public int FmtHdrLeth;
        public short FormatTag;
        public int SamplesPerSec;
        public int fileLength;
        public final char[] fileID = {'R', 'I', 'F', 'F'};
        public char[] wavTag = {'W', 'A', 'V', 'E'};
        public char[] FmtHdrID = {'f', 'm', 't', TokenParser.SP};
        public char[] DataHdrID = {'d', 'a', 't', 'a'};

        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            WriteChar(byteArrayOutputStream, this.fileID);
            WriteInt(byteArrayOutputStream, this.fileLength);
            WriteChar(byteArrayOutputStream, this.wavTag);
            WriteChar(byteArrayOutputStream, this.FmtHdrID);
            WriteInt(byteArrayOutputStream, this.FmtHdrLeth);
            WriteShort(byteArrayOutputStream, this.FormatTag);
            WriteShort(byteArrayOutputStream, this.Channels);
            WriteInt(byteArrayOutputStream, this.SamplesPerSec);
            WriteInt(byteArrayOutputStream, this.AvgBytesPerSec);
            WriteShort(byteArrayOutputStream, this.BlockAlign);
            WriteShort(byteArrayOutputStream, this.BitsPerSample);
            WriteChar(byteArrayOutputStream, this.DataHdrID);
            WriteInt(byteArrayOutputStream, this.DataHdrLeth);
            byteArrayOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        }

        private void WriteShort(ByteArrayOutputStream byteArrayOutputStream, int i) throws IOException {
            byteArrayOutputStream.write(new byte[]{(byte) ((i << 24) >> 24), (byte) ((i << 16) >> 24)});
        }

        private void WriteInt(ByteArrayOutputStream byteArrayOutputStream, int i) throws IOException {
            byteArrayOutputStream.write(new byte[]{(byte) ((i << 24) >> 24), (byte) ((i << 16) >> 24), (byte) ((i << 8) >> 24), (byte) (i >> 24)});
        }

        private void WriteChar(ByteArrayOutputStream byteArrayOutputStream, char[] cArr) {
            for (char c : cArr) {
                byteArrayOutputStream.write(c);
            }
        }
    }
}
