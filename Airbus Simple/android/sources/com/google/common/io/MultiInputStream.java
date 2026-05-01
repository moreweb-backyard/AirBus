package com.google.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/* loaded from: classes.dex */
final class MultiInputStream extends InputStream {
    private InputStream in;
    private Iterator<? extends InputSupplier<? extends InputStream>> it;

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    public MultiInputStream(Iterator<? extends InputSupplier<? extends InputStream>> it) throws IOException {
        this.it = it;
        advance();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = this.it.next().getInput();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int i = this.in.read();
        if (i != -1) {
            return i;
        }
        advance();
        return read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int i3 = this.in.read(bArr, i, i2);
        if (i3 != -1) {
            return i3;
        }
        advance();
        return read(bArr, i, i2);
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        if (this.in == null || j <= 0) {
            return 0L;
        }
        long jSkip = this.in.skip(j);
        if (jSkip != 0) {
            return jSkip;
        }
        if (read() == -1) {
            return 0L;
        }
        return this.in.skip(j - 1) + 1;
    }
}
