package voice;

import java.io.IOException;
import voice.Buffer;
import voice.decoder.fft.AudioFloatConverter;
import voice.decoder.fft.AudioFormat;

/* loaded from: classes.dex */
public class WavFileDataSource implements DataSource {
    Buffer.BufferData bufferData;
    int byteOverlapSize;
    int byteStepSize;
    AudioFloatConverter converter;
    int floatOverlapSize;
    int floatStepSize;
    int waveDataPos;
    private WaveFile waveFile;

    @Override // voice.DataSource
    public void freeData(Buffer.BufferData bufferData) {
    }

    public WavFileDataSource(String str, int i, int i2, AudioFormat audioFormat) throws IOException {
        this.waveFile = new WaveFile(str);
        if (this.waveFile.getNumChannels() != 1 && this.waveFile.getBitPerSample() != 2 && this.waveFile.getSampleRate() != 44100) {
            throw new IOException("只支持wav格式为44100采样率，单声道，16位精度");
        }
        this.floatStepSize = i - i2;
        this.floatOverlapSize = i2;
        this.byteStepSize = this.floatStepSize * audioFormat.getFrameSize();
        this.byteOverlapSize = this.floatOverlapSize * audioFormat.getFrameSize();
        this.waveDataPos = 0;
        this.bufferData = new Buffer.BufferData(this.byteStepSize + this.byteOverlapSize);
        this.bufferData.floatData = new float[this.floatStepSize + this.floatOverlapSize];
        this.converter = AudioFloatConverter.getConverter(audioFormat);
    }

    @Override // voice.DataSource
    public Buffer.BufferData getData() {
        if (this.waveDataPos == 0) {
            System.arraycopy(this.waveFile.getData(), 0, this.bufferData.mData, this.waveDataPos, this.byteStepSize + this.byteOverlapSize);
            this.converter.toFloatArray(this.bufferData.mData, 0, this.bufferData.floatData, 0, this.floatStepSize + this.floatOverlapSize);
            this.waveDataPos = this.byteStepSize + this.byteOverlapSize;
        } else if (this.waveDataPos + this.byteStepSize <= this.waveFile.getDataLen()) {
            System.arraycopy(this.bufferData.mData, this.byteStepSize, this.bufferData.mData, 0, this.byteOverlapSize);
            System.arraycopy(this.waveFile.getData(), this.waveDataPos, this.bufferData.mData, this.byteOverlapSize, this.byteStepSize);
            System.arraycopy(this.bufferData.floatData, this.floatStepSize, this.bufferData.floatData, 0, this.floatOverlapSize);
            this.converter.toFloatArray(this.bufferData.mData, this.byteOverlapSize, this.bufferData.floatData, this.floatOverlapSize, this.floatStepSize);
            this.waveDataPos += this.byteStepSize;
        } else {
            return Buffer.BufferData.getEmptyBuffer();
        }
        return this.bufferData;
    }
}
