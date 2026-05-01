package org.apache.commons.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.util.Closeable;
import org.apache.commons.fileupload.util.Streams;

/* loaded from: classes.dex */
public class MultipartStream {
    protected static final int DEFAULT_BUFSIZE = 4096;
    public static final int HEADER_PART_SIZE_MAX = 10240;
    public static final byte LF = 10;
    private byte[] boundary;
    private int boundaryLength;
    private final int bufSize;
    private final byte[] buffer;
    private int head;
    private String headerEncoding;
    private final InputStream input;
    private int keepRegion;
    private final ProgressNotifier notifier;
    private int tail;
    public static final byte CR = 13;
    protected static final byte[] HEADER_SEPARATOR = {CR, 10, CR, 10};
    protected static final byte[] FIELD_SEPARATOR = {CR, 10};
    public static final byte DASH = 45;
    protected static final byte[] STREAM_TERMINATOR = {DASH, DASH};
    protected static final byte[] BOUNDARY_PREFIX = {CR, 10, DASH, DASH};

    public static class ProgressNotifier {
        private long bytesRead;
        private final long contentLength;
        private int items;
        private final ProgressListener listener;

        ProgressNotifier(ProgressListener progressListener, long j) {
            this.listener = progressListener;
            this.contentLength = j;
        }

        void noteBytesRead(int i) {
            this.bytesRead += i;
            notifyListener();
        }

        void noteItem() {
            this.items++;
            notifyListener();
        }

        private void notifyListener() {
            if (this.listener != null) {
                this.listener.update(this.bytesRead, this.contentLength, this.items);
            }
        }
    }

    @Deprecated
    public MultipartStream() {
        this((InputStream) null, (byte[]) null, (ProgressNotifier) null);
    }

    @Deprecated
    public MultipartStream(InputStream inputStream, byte[] bArr, int i) {
        this(inputStream, bArr, i, null);
    }

    public MultipartStream(InputStream inputStream, byte[] bArr, int i, ProgressNotifier progressNotifier) {
        if (bArr == null) {
            throw new IllegalArgumentException("boundary may not be null");
        }
        this.boundaryLength = bArr.length + BOUNDARY_PREFIX.length;
        if (i < this.boundaryLength + 1) {
            throw new IllegalArgumentException("The buffer size specified for the MultipartStream is too small");
        }
        this.input = inputStream;
        this.bufSize = Math.max(i, this.boundaryLength * 2);
        this.buffer = new byte[this.bufSize];
        this.notifier = progressNotifier;
        this.boundary = new byte[this.boundaryLength];
        this.keepRegion = this.boundary.length;
        System.arraycopy(BOUNDARY_PREFIX, 0, this.boundary, 0, BOUNDARY_PREFIX.length);
        System.arraycopy(bArr, 0, this.boundary, BOUNDARY_PREFIX.length, bArr.length);
        this.head = 0;
        this.tail = 0;
    }

    MultipartStream(InputStream inputStream, byte[] bArr, ProgressNotifier progressNotifier) {
        this(inputStream, bArr, 4096, progressNotifier);
    }

    @Deprecated
    public MultipartStream(InputStream inputStream, byte[] bArr) {
        this(inputStream, bArr, 4096, null);
    }

    public String getHeaderEncoding() {
        return this.headerEncoding;
    }

    public void setHeaderEncoding(String str) {
        this.headerEncoding = str;
    }

    public byte readByte() throws IOException {
        if (this.head == this.tail) {
            this.head = 0;
            this.tail = this.input.read(this.buffer, this.head, this.bufSize);
            if (this.tail == -1) {
                throw new IOException("No more data is available");
            }
            if (this.notifier != null) {
                this.notifier.noteBytesRead(this.tail);
            }
        }
        byte[] bArr = this.buffer;
        int i = this.head;
        this.head = i + 1;
        return bArr[i];
    }

    public boolean readBoundary() throws MalformedStreamException, FileUploadBase.FileUploadIOException {
        byte[] bArr = new byte[2];
        this.head += this.boundaryLength;
        try {
            bArr[0] = readByte();
            if (bArr[0] == 10) {
                return true;
            }
            bArr[1] = readByte();
            if (arrayequals(bArr, STREAM_TERMINATOR, 2)) {
                return false;
            }
            if (arrayequals(bArr, FIELD_SEPARATOR, 2)) {
                return true;
            }
            throw new MalformedStreamException("Unexpected characters follow a boundary");
        } catch (FileUploadBase.FileUploadIOException e) {
            throw e;
        } catch (IOException unused) {
            throw new MalformedStreamException("Stream ended unexpectedly");
        }
    }

    public void setBoundary(byte[] bArr) throws IllegalBoundaryException {
        if (bArr.length != this.boundaryLength - BOUNDARY_PREFIX.length) {
            throw new IllegalBoundaryException("The length of a boundary token can not be changed");
        }
        System.arraycopy(bArr, 0, this.boundary, BOUNDARY_PREFIX.length, bArr.length);
    }

    public String readHeaders() throws MalformedStreamException, FileUploadBase.FileUploadIOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        int i2 = 0;
        while (i < HEADER_SEPARATOR.length) {
            try {
                byte b = readByte();
                i2++;
                if (i2 > 10240) {
                    throw new MalformedStreamException(String.format("Header section has more than %s bytes (maybe it is not properly terminated)", 10240));
                }
                i = b == HEADER_SEPARATOR[i] ? i + 1 : 0;
                byteArrayOutputStream.write(b);
            } catch (FileUploadBase.FileUploadIOException e) {
                throw e;
            } catch (IOException unused) {
                throw new MalformedStreamException("Stream ended unexpectedly");
            }
        }
        if (this.headerEncoding != null) {
            try {
                return byteArrayOutputStream.toString(this.headerEncoding);
            } catch (UnsupportedEncodingException unused2) {
                return byteArrayOutputStream.toString();
            }
        }
        return byteArrayOutputStream.toString();
    }

    public int readBodyData(OutputStream outputStream) throws IOException {
        return (int) Streams.copy(newInputStream(), outputStream, false);
    }

    ItemInputStream newInputStream() {
        return new ItemInputStream();
    }

    public int discardBodyData() throws IOException {
        return readBodyData(null);
    }

    public boolean skipPreamble() throws IOException {
        System.arraycopy(this.boundary, 2, this.boundary, 0, this.boundary.length - 2);
        this.boundaryLength = this.boundary.length - 2;
        try {
            discardBodyData();
            return readBoundary();
        } catch (MalformedStreamException unused) {
            return false;
        } finally {
            System.arraycopy(this.boundary, 0, this.boundary, 2, this.boundary.length - 2);
            this.boundaryLength = this.boundary.length;
            this.boundary[0] = CR;
            this.boundary[1] = 10;
        }
    }

    public static boolean arrayequals(byte[] bArr, byte[] bArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    protected int findByte(byte b, int i) {
        while (i < this.tail) {
            if (this.buffer[i] == b) {
                return i;
            }
            i++;
        }
        return -1;
    }

    protected int findSeparator() {
        int i = this.tail - this.boundaryLength;
        int i2 = this.head;
        int i3 = 0;
        while (i2 <= i && i3 != this.boundaryLength) {
            int iFindByte = findByte(this.boundary[0], i2);
            if (iFindByte == -1 || iFindByte > i) {
                return -1;
            }
            i3 = 1;
            while (i3 < this.boundaryLength && this.buffer[iFindByte + i3] == this.boundary[i3]) {
                i3++;
            }
            i2 = iFindByte + 1;
        }
        if (i3 == this.boundaryLength) {
            return i2 - 1;
        }
        return -1;
    }

    public static class MalformedStreamException extends IOException {
        private static final long serialVersionUID = 6466926458059796677L;

        public MalformedStreamException() {
        }

        public MalformedStreamException(String str) {
            super(str);
        }
    }

    public static class IllegalBoundaryException extends IOException {
        private static final long serialVersionUID = -161533165102632918L;

        public IllegalBoundaryException() {
        }

        public IllegalBoundaryException(String str) {
            super(str);
        }
    }

    public class ItemInputStream extends InputStream implements Closeable {
        private static final int BYTE_POSITIVE_OFFSET = 256;
        private boolean closed;
        private int pad;
        private int pos;
        private long total;

        ItemInputStream() {
            findSeparator();
        }

        private void findSeparator() {
            this.pos = MultipartStream.this.findSeparator();
            if (this.pos == -1) {
                if (MultipartStream.this.tail - MultipartStream.this.head > MultipartStream.this.keepRegion) {
                    this.pad = MultipartStream.this.keepRegion;
                } else {
                    this.pad = MultipartStream.this.tail - MultipartStream.this.head;
                }
            }
        }

        public long getBytesRead() {
            return this.total;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.pos == -1 ? (MultipartStream.this.tail - MultipartStream.this.head) - this.pad : this.pos - MultipartStream.this.head;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.closed) {
                throw new FileItemStream.ItemSkippedException();
            }
            if (available() == 0 && makeAvailable() == 0) {
                return -1;
            }
            this.total++;
            byte[] bArr = MultipartStream.this.buffer;
            MultipartStream multipartStream = MultipartStream.this;
            int i = multipartStream.head;
            multipartStream.head = i + 1;
            byte b = bArr[i];
            return b >= 0 ? b : b + 256;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (this.closed) {
                throw new FileItemStream.ItemSkippedException();
            }
            if (i2 == 0) {
                return 0;
            }
            int iAvailable = available();
            if (iAvailable == 0 && (iAvailable = makeAvailable()) == 0) {
                return -1;
            }
            int iMin = Math.min(iAvailable, i2);
            System.arraycopy(MultipartStream.this.buffer, MultipartStream.this.head, bArr, i, iMin);
            MultipartStream.this.head += iMin;
            this.total += iMin;
            return iMin;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable, org.apache.commons.fileupload.util.Closeable
        public void close() throws IOException {
            close(false);
        }

        public void close(boolean z) throws IOException {
            if (this.closed) {
                return;
            }
            if (!z) {
                while (true) {
                    int iAvailable = available();
                    if (iAvailable == 0 && (iAvailable = makeAvailable()) == 0) {
                        break;
                    } else {
                        skip(iAvailable);
                    }
                }
            } else {
                this.closed = true;
                MultipartStream.this.input.close();
            }
            this.closed = true;
        }

        @Override // java.io.InputStream
        public long skip(long j) throws IOException {
            if (this.closed) {
                throw new FileItemStream.ItemSkippedException();
            }
            int iAvailable = available();
            if (iAvailable == 0 && (iAvailable = makeAvailable()) == 0) {
                return 0L;
            }
            long jMin = Math.min(iAvailable, j);
            MultipartStream.this.head = (int) (r0.head + jMin);
            return jMin;
        }

        private int makeAvailable() throws IOException {
            int iAvailable;
            if (this.pos != -1) {
                return 0;
            }
            this.total += (MultipartStream.this.tail - MultipartStream.this.head) - this.pad;
            System.arraycopy(MultipartStream.this.buffer, MultipartStream.this.tail - this.pad, MultipartStream.this.buffer, 0, this.pad);
            MultipartStream.this.head = 0;
            MultipartStream.this.tail = this.pad;
            do {
                int i = MultipartStream.this.input.read(MultipartStream.this.buffer, MultipartStream.this.tail, MultipartStream.this.bufSize - MultipartStream.this.tail);
                if (i != -1) {
                    if (MultipartStream.this.notifier != null) {
                        MultipartStream.this.notifier.noteBytesRead(i);
                    }
                    MultipartStream.this.tail += i;
                    findSeparator();
                    iAvailable = available();
                    if (iAvailable > 0) {
                        break;
                    }
                } else {
                    throw new MalformedStreamException("Stream ended unexpectedly");
                }
            } while (this.pos == -1);
            return iAvailable;
        }

        @Override // org.apache.commons.fileupload.util.Closeable
        public boolean isClosed() {
            return this.closed;
        }
    }
}
