package org.apache.commons.fileupload.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class LimitedInputStream extends FilterInputStream implements Closeable {
    private boolean closed;
    private long count;
    private final long sizeMax;

    protected abstract void raiseError(long j, long j2) throws IOException;

    public LimitedInputStream(InputStream inputStream, long j) {
        super(inputStream);
        this.sizeMax = j;
    }

    private void checkLimit() throws IOException {
        if (this.count > this.sizeMax) {
            raiseError(this.sizeMax, this.count);
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i = super.read();
        if (i != -1) {
            this.count++;
            checkLimit();
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = super.read(bArr, i, i2);
        if (i3 > 0) {
            this.count += i3;
            checkLimit();
        }
        return i3;
    }

    @Override // org.apache.commons.fileupload.util.Closeable
    public boolean isClosed() throws IOException {
        return this.closed;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable, org.apache.commons.fileupload.util.Closeable
    public void close() throws IOException {
        this.closed = true;
        super.close();
    }
}
