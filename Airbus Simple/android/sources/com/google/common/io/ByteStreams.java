package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Checksum;

/* loaded from: classes.dex */
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;

    private ByteStreams() {
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] bArr) {
        return newInputStreamSupplier(bArr, 0, bArr.length);
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(final byte[] bArr, final int i, final int i2) {
        return new InputSupplier<ByteArrayInputStream>() { // from class: com.google.common.io.ByteStreams.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public ByteArrayInputStream getInput() {
                return new ByteArrayInputStream(bArr, i, i2);
            }
        };
    }

    public static void write(byte[] bArr, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        Preconditions.checkNotNull(bArr);
        OutputStream output = outputSupplier.getOutput();
        try {
            output.write(bArr);
            Closeables.close(output, false);
        } catch (Throwable th) {
            Closeables.close(output, true);
            throw th;
        }
    }

    public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        InputStream input = inputSupplier.getInput();
        try {
            OutputStream output = outputSupplier.getOutput();
            try {
                long jCopy = copy(input, output);
                Closeables.close(output, false);
                Closeables.close(input, false);
                return jCopy;
            } catch (Throwable th) {
                Closeables.close(output, true);
                throw th;
            }
        } catch (Throwable th2) {
            Closeables.close(input, true);
            throw th2;
        }
    }

    public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputStream outputStream) throws IOException {
        InputStream input = inputSupplier.getInput();
        try {
            long jCopy = copy(input, outputStream);
            Closeables.close(input, false);
            return jCopy;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                return j;
            }
            outputStream.write(bArr, 0, i);
            j += i;
        }
    }

    public static long copy(ReadableByteChannel readableByteChannel, WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4096);
        long jWrite = 0;
        while (readableByteChannel.read(byteBufferAllocate) != -1) {
            byteBufferAllocate.flip();
            while (byteBufferAllocate.hasRemaining()) {
                jWrite += writableByteChannel.write(byteBufferAllocate);
            }
            byteBufferAllocate.clear();
        }
        return jWrite;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] toByteArray(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
        InputStream input = inputSupplier.getInput();
        try {
            byte[] byteArray = toByteArray(input);
            Closeables.close(input, false);
            return byteArray;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr) {
        return new ByteArrayDataInputStream(bArr);
    }

    public static ByteArrayDataInput newDataInput(byte[] bArr, int i) {
        Preconditions.checkPositionIndex(i, bArr.length);
        return new ByteArrayDataInputStream(bArr, i);
    }

    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(byte[] bArr) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr));
        }

        ByteArrayDataInputStream(byte[] bArr, int i) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr, i, bArr.length - i));
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] bArr) throws IOException {
            try {
                this.input.readFully(bArr);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            try {
                this.input.readFully(bArr, i, i2);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int skipBytes(int i) {
            try {
                return this.input.skipBytes(i);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (EOFException e) {
                throw new IllegalStateException(e);
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public short readShort() {
            try {
                return this.input.readShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public char readChar() {
            try {
                return this.input.readChar();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readInt() {
            try {
                return this.input.readInt();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public long readLong() {
            try {
                return this.input.readLong();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public String readLine() {
            try {
                return this.input.readLine();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static ByteArrayDataOutput newDataOutput() {
        return new ByteArrayDataOutputStream();
    }

    public static ByteArrayDataOutput newDataOutput(int i) {
        Preconditions.checkArgument(i >= 0, "Invalid size: %s", Integer.valueOf(i));
        return new ByteArrayDataOutputStream(i);
    }

    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream() {
            this(new ByteArrayOutputStream());
        }

        ByteArrayDataOutputStream(int i) {
            this(new ByteArrayOutputStream(i));
        }

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputSteam = byteArrayOutputStream;
            this.output = new DataOutputStream(byteArrayOutputStream);
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(int i) throws IOException {
            try {
                this.output.write(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] bArr) throws IOException {
            try {
                this.output.write(bArr);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] bArr, int i, int i2) throws IOException {
            try {
                this.output.write(bArr, i, i2);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBoolean(boolean z) throws IOException {
            try {
                this.output.writeBoolean(z);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeByte(int i) throws IOException {
            try {
                this.output.writeByte(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBytes(String str) throws IOException {
            try {
                this.output.writeBytes(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChar(int i) throws IOException {
            try {
                this.output.writeChar(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChars(String str) throws IOException {
            try {
                this.output.writeChars(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeDouble(double d) throws IOException {
            try {
                this.output.writeDouble(d);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeFloat(float f) throws IOException {
            try {
                this.output.writeFloat(f);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeInt(int i) throws IOException {
            try {
                this.output.writeInt(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeLong(long j) throws IOException {
            try {
                this.output.writeLong(j);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeShort(int i) throws IOException {
            try {
                this.output.writeShort(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeUTF(String str) throws IOException {
            try {
                this.output.writeUTF(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput
        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    public static long length(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
        InputStream input = inputSupplier.getInput();
        long j = 0;
        while (true) {
            try {
                long jSkip = input.skip(2147483647L);
                if (jSkip != 0) {
                    j += jSkip;
                } else {
                    if (input.read() == -1) {
                        Closeables.close(input, false);
                        return j;
                    }
                    j++;
                }
            } catch (Throwable th) {
                Closeables.close(input, true);
                throw th;
            }
        }
    }

    public static boolean equal(InputSupplier<? extends InputStream> inputSupplier, InputSupplier<? extends InputStream> inputSupplier2) throws Throwable {
        int i;
        byte[] bArr = new byte[4096];
        byte[] bArr2 = new byte[4096];
        InputStream input = inputSupplier.getInput();
        boolean z = true;
        try {
            input = inputSupplier2.getInput();
            do {
                try {
                    i = read(input, bArr, 0, 4096);
                    try {
                        if (i != read(input, bArr2, 0, 4096) || !Arrays.equals(bArr, bArr2)) {
                            Closeables.close(input, false);
                            Closeables.close(input, false);
                            return false;
                        }
                    } catch (Throwable th) {
                        th = th;
                        z = false;
                        throw th;
                    }
                } finally {
                    Closeables.close(input, true);
                }
            } while (i == 4096);
            Closeables.close(input, false);
            Closeables.close(input, false);
            return true;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (read(inputStream, bArr, i, i2) != i2) {
            throw new EOFException();
        }
    }

    public static void skipFully(InputStream inputStream, long j) throws IOException {
        while (j > 0) {
            long jSkip = inputStream.skip(j);
            if (jSkip != 0) {
                j -= jSkip;
            } else {
                if (inputStream.read() == -1) {
                    throw new EOFException();
                }
                j--;
            }
        }
    }

    public static <T> T readBytes(InputSupplier<? extends InputStream> inputSupplier, ByteProcessor<T> byteProcessor) throws IOException {
        byte[] bArr = new byte[4096];
        InputStream input = inputSupplier.getInput();
        while (true) {
            boolean z = true;
            try {
                int i = input.read(bArr);
                if (i == -1) {
                    z = false;
                    break;
                }
                if (!byteProcessor.processBytes(bArr, 0, i)) {
                    break;
                }
            } finally {
                Closeables.close(input, z);
            }
        }
        return byteProcessor.getResult();
    }

    public static long getChecksum(InputSupplier<? extends InputStream> inputSupplier, final Checksum checksum) throws IOException {
        return ((Long) readBytes(inputSupplier, new ByteProcessor<Long>() { // from class: com.google.common.io.ByteStreams.2
            @Override // com.google.common.io.ByteProcessor
            public boolean processBytes(byte[] bArr, int i, int i2) {
                checksum.update(bArr, i, i2);
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.ByteProcessor
            public Long getResult() {
                long value = checksum.getValue();
                checksum.reset();
                return Long.valueOf(value);
            }
        })).longValue();
    }

    public static byte[] getDigest(InputSupplier<? extends InputStream> inputSupplier, final MessageDigest messageDigest) throws IOException {
        return (byte[]) readBytes(inputSupplier, new ByteProcessor<byte[]>() { // from class: com.google.common.io.ByteStreams.3
            @Override // com.google.common.io.ByteProcessor
            public boolean processBytes(byte[] bArr, int i, int i2) {
                messageDigest.update(bArr, i, i2);
                return true;
            }

            @Override // com.google.common.io.ByteProcessor
            public byte[] getResult() {
                return messageDigest.digest();
            }
        });
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int i3 = 0;
        while (i3 < i2) {
            int i4 = inputStream.read(bArr, i + i3, i2 - i3);
            if (i4 == -1) {
                break;
            }
            i3 += i4;
        }
        return i3;
    }

    public static InputSupplier<InputStream> slice(final InputSupplier<? extends InputStream> inputSupplier, final long j, final long j2) {
        Preconditions.checkNotNull(inputSupplier);
        Preconditions.checkArgument(j >= 0, "offset is negative");
        Preconditions.checkArgument(j2 >= 0, "length is negative");
        return new InputSupplier<InputStream>() { // from class: com.google.common.io.ByteStreams.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public InputStream getInput() throws IOException {
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                if (j > 0) {
                    try {
                        ByteStreams.skipFully(inputStream, j);
                    } catch (IOException e) {
                        Closeables.closeQuietly(inputStream);
                        throw e;
                    }
                }
                return new LimitInputStream(inputStream, j2);
            }
        };
    }

    public static InputSupplier<InputStream> join(final Iterable<? extends InputSupplier<? extends InputStream>> iterable) {
        return new InputSupplier<InputStream>() { // from class: com.google.common.io.ByteStreams.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public InputStream getInput() throws IOException {
                return new MultiInputStream(iterable.iterator());
            }
        };
    }

    public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>... inputSupplierArr) {
        return join(Arrays.asList(inputSupplierArr));
    }
}
