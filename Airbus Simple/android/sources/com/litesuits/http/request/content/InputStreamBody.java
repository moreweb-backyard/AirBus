package com.litesuits.http.request.content;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class InputStreamBody extends HttpBody {
    protected InputStream instream;
    protected long length;

    public InputStreamBody(InputStream inputStream) {
        this(inputStream, null);
    }

    public InputStreamBody(InputStream inputStream, String str) {
        this(inputStream, str, -1L);
    }

    public InputStreamBody(InputStream inputStream, String str, long j) {
        this.instream = inputStream;
        this.contentType = str == null ? "application/octet-stream" : str;
        this.length = j;
    }

    public InputStream getInstream() {
        return this.instream;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public long getContentLength() {
        return this.length;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public void writeTo(OutputStream outputStream) throws IOException {
        int i;
        if (this.instream == null) {
            return;
        }
        try {
            byte[] bArr = new byte[4096];
            if (this.length < 0) {
                while (true) {
                    int i2 = this.instream.read(bArr);
                    if (i2 == -1) {
                        break;
                    } else {
                        outputStream.write(bArr, 0, i2);
                    }
                }
            } else {
                long j = this.length;
                while (j > 0 && (i = this.instream.read(bArr, 0, (int) Math.min(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, j))) != -1) {
                    outputStream.write(bArr, 0, i);
                    j -= i;
                }
            }
            outputStream.flush();
        } finally {
            this.instream.close();
        }
    }

    public String toString() {
        return "InputStreamBody{instream=" + this.instream + ", length=" + this.length + "} " + super.toString();
    }
}
