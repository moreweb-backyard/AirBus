package voice;

import voice.Buffer;

/* loaded from: classes.dex */
public interface DataSource {
    void freeData(Buffer.BufferData bufferData);

    Buffer.BufferData getData();
}
