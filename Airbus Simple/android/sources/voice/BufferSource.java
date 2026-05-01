package voice;

import voice.Buffer;

/* loaded from: classes.dex */
public interface BufferSource {
    void freeBuffer(Buffer.BufferData bufferData);

    Buffer.BufferData getBuffer();
}
