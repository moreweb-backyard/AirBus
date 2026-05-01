package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class LimitInputStream extends FilterInputStream {
    private long left;
    private long mark;

    public LimitInputStream(InputStream inputStream, long j) {
        super(inputStream);
        this.mark = -1L;
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkArgument(j >= 0, "limit must be non-negative");
        this.left = j;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return (int) Math.min(this.in.available(), this.left);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i) {
        this.in.mark(i);
        this.mark = this.left;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.left == 0) {
            return -1;
        }
        int i = this.in.read();
        if (i != -1) {
            this.left--;
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.left == 0) {
            return -1;
        }
        int i3 = this.in.read(bArr, i, (int) Math.min(i2, this.left));
        if (i3 != -1) {
            this.left -= i3;
        }
        return i3;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        if (!this.in.markSupported()) {
            throw new IOException("Mark not supported");
        }
        if (this.mark == -1) {
            throw new IOException("Mark not set");
        }
        this.in.reset();
        this.left = this.mark;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) throws IOException {
        long jSkip = this.in.skip(Math.min(j, this.left));
        this.left -= jSkip;
        return jSkip;
    }
}
