package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class ReversedLinesFileReader implements Closeable {
    private final int avoidNewlineSplitBufferSize;
    private final int blockSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private final Charset encoding;
    private final byte[][] newLineSequences;
    private final RandomAccessFile randomAccessFile;
    private final long totalBlockCount;
    private final long totalByteLength;
    private boolean trailingNewlineOfFileSkipped;

    @Deprecated
    public ReversedLinesFileReader(File file) throws IOException {
        this(file, 4096, Charset.defaultCharset());
    }

    public ReversedLinesFileReader(File file, Charset charset) throws IOException {
        this(file, 4096, charset);
    }

    public ReversedLinesFileReader(File file, int i, Charset charset) throws IOException {
        int i2;
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = i;
        this.encoding = charset;
        Charset charset2 = Charsets.toCharset(charset);
        if (charset2.newEncoder().maxBytesPerChar() == 1.0f || charset2 == Charsets.UTF_8 || charset2 == Charset.forName("Shift_JIS") || charset2 == Charset.forName("windows-31j") || charset2 == Charset.forName("x-windows-949") || charset2 == Charset.forName("gbk") || charset2 == Charset.forName("x-windows-950")) {
            this.byteDecrement = 1;
        } else if (charset2 == Charsets.UTF_16BE || charset2 == Charsets.UTF_16LE) {
            this.byteDecrement = 2;
        } else {
            if (charset2 == Charsets.UTF_16) {
                throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
            }
            throw new UnsupportedEncodingException("Encoding " + charset + " is not supported yet (feel free to submit a patch)");
        }
        this.newLineSequences = new byte[][]{IOUtils.LINE_SEPARATOR_WINDOWS.getBytes(charset), IOUtils.LINE_SEPARATOR_UNIX.getBytes(charset), "\r".getBytes(charset)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        long j = i;
        int i3 = (int) (this.totalByteLength % j);
        if (i3 > 0) {
            this.totalBlockCount = (this.totalByteLength / j) + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / j;
            i2 = this.totalByteLength > 0 ? i : i2;
            this.currentFilePart = new FilePart(this.totalBlockCount, i2, null);
        }
        i2 = i3;
        this.currentFilePart = new FilePart(this.totalBlockCount, i2, null);
    }

    public ReversedLinesFileReader(File file, int i, String str) throws IOException {
        this(file, i, Charsets.toCharset(str));
    }

    public String readLine() throws IOException {
        String line = this.currentFilePart.readLine();
        while (line == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
            line = this.currentFilePart.readLine();
        }
        if (!"".equals(line) || this.trailingNewlineOfFileSkipped) {
            return line;
        }
        this.trailingNewlineOfFileSkipped = true;
        return readLine();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    private class FilePart {
        private int currentLastBytePos;
        private final byte[] data;
        private byte[] leftOver;
        private final long no;

        private FilePart(long j, int i, byte[] bArr) throws IOException {
            this.no = j;
            this.data = new byte[(bArr != null ? bArr.length : 0) + i];
            long j2 = (j - 1) * ReversedLinesFileReader.this.blockSize;
            if (j > 0) {
                ReversedLinesFileReader.this.randomAccessFile.seek(j2);
                if (ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, i) != i) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (bArr != null) {
                System.arraycopy(bArr, 0, this.data, i, bArr.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FilePart rollOver() throws IOException {
            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            }
            if (this.no > 1) {
                return ReversedLinesFileReader.this.new FilePart(this.no - 1, ReversedLinesFileReader.this.blockSize, this.leftOver);
            }
            if (this.leftOver == null) {
                return null;
            }
            throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x006a, code lost:
        
            r2 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x006b, code lost:
        
            if (r0 == false) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x006f, code lost:
        
            if (r9.leftOver == null) goto L35;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0071, code lost:
        
            r2 = new java.lang.String(r9.leftOver, r9.this$0.encoding);
            r9.leftOver = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x0080, code lost:
        
            return r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:?, code lost:
        
            return r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
        
            return r2;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String readLine() throws java.io.IOException {
            /*
                r9 = this;
                long r0 = r9.no
                r2 = 0
                r3 = 1
                r4 = 1
                int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
                if (r6 != 0) goto Lc
                r0 = 1
                goto Ld
            Lc:
                r0 = 0
            Ld:
                int r1 = r9.currentLastBytePos
            Lf:
                r4 = -1
                r5 = 0
                if (r1 <= r4) goto L6a
                if (r0 != 0) goto L21
                org.apache.commons.io.input.ReversedLinesFileReader r4 = org.apache.commons.io.input.ReversedLinesFileReader.this
                int r4 = org.apache.commons.io.input.ReversedLinesFileReader.access$600(r4)
                if (r1 >= r4) goto L21
                r9.createLeftOver()
                goto L6a
            L21:
                byte[] r4 = r9.data
                int r4 = r9.getNewLineMatchByteCount(r4, r1)
                if (r4 <= 0) goto L5e
                int r6 = r1 + 1
                int r7 = r9.currentLastBytePos
                int r7 = r7 - r6
                int r7 = r7 + r3
                if (r7 < 0) goto L47
                byte[] r3 = new byte[r7]
                byte[] r8 = r9.data
                java.lang.System.arraycopy(r8, r6, r3, r2, r7)
                java.lang.String r2 = new java.lang.String
                org.apache.commons.io.input.ReversedLinesFileReader r6 = org.apache.commons.io.input.ReversedLinesFileReader.this
                java.nio.charset.Charset r6 = org.apache.commons.io.input.ReversedLinesFileReader.access$500(r6)
                r2.<init>(r3, r6)
                int r1 = r1 - r4
                r9.currentLastBytePos = r1
                goto L6b
            L47:
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Unexpected negative line length="
                r1.append(r2)
                r1.append(r7)
                java.lang.String r1 = r1.toString()
                r0.<init>(r1)
                throw r0
            L5e:
                org.apache.commons.io.input.ReversedLinesFileReader r4 = org.apache.commons.io.input.ReversedLinesFileReader.this
                int r4 = org.apache.commons.io.input.ReversedLinesFileReader.access$700(r4)
                int r1 = r1 - r4
                if (r1 >= 0) goto Lf
                r9.createLeftOver()
            L6a:
                r2 = r5
            L6b:
                if (r0 == 0) goto L80
                byte[] r0 = r9.leftOver
                if (r0 == 0) goto L80
                java.lang.String r2 = new java.lang.String
                byte[] r0 = r9.leftOver
                org.apache.commons.io.input.ReversedLinesFileReader r1 = org.apache.commons.io.input.ReversedLinesFileReader.this
                java.nio.charset.Charset r1 = org.apache.commons.io.input.ReversedLinesFileReader.access$500(r1)
                r2.<init>(r0, r1)
                r9.leftOver = r5
            L80:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.ReversedLinesFileReader.FilePart.readLine():java.lang.String");
        }

        private void createLeftOver() {
            int i = this.currentLastBytePos + 1;
            if (i > 0) {
                this.leftOver = new byte[i];
                System.arraycopy(this.data, 0, this.leftOver, 0, i);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] bArr, int i) {
            for (byte[] bArr2 : ReversedLinesFileReader.this.newLineSequences) {
                boolean z = true;
                for (int length = bArr2.length - 1; length >= 0; length--) {
                    int length2 = (i + length) - (bArr2.length - 1);
                    z &= length2 >= 0 && bArr[length2] == bArr2[length];
                }
                if (z) {
                    return bArr2.length;
                }
            }
            return 0;
        }
    }
}
