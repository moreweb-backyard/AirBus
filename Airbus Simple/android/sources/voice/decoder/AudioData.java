package voice.decoder;

/* loaded from: classes.dex */
public interface AudioData {
    int getBufferSize();

    byte[] getByteBuffer();

    int getOverlap();
}
