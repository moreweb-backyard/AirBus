package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public final class CharStreams {
    private static final int BUF_SIZE = 2048;

    private CharStreams() {
    }

    public static InputSupplier<StringReader> newReaderSupplier(final String str) {
        Preconditions.checkNotNull(str);
        return new InputSupplier<StringReader>() { // from class: com.google.common.io.CharStreams.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public StringReader getInput() {
                return new StringReader(str);
            }
        };
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(final InputSupplier<? extends InputStream> inputSupplier, final Charset charset) {
        Preconditions.checkNotNull(inputSupplier);
        Preconditions.checkNotNull(charset);
        return new InputSupplier<InputStreamReader>() { // from class: com.google.common.io.CharStreams.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public InputStreamReader getInput() throws IOException {
                return new InputStreamReader((InputStream) inputSupplier.getInput(), charset);
            }
        };
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(final OutputSupplier<? extends OutputStream> outputSupplier, final Charset charset) {
        Preconditions.checkNotNull(outputSupplier);
        Preconditions.checkNotNull(charset);
        return new OutputSupplier<OutputStreamWriter>() { // from class: com.google.common.io.CharStreams.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.OutputSupplier
            public OutputStreamWriter getOutput() throws IOException {
                return new OutputStreamWriter((OutputStream) outputSupplier.getOutput(), charset);
            }
        };
    }

    public static <W extends Appendable & Closeable> void write(CharSequence charSequence, OutputSupplier<W> outputSupplier) throws IOException {
        Preconditions.checkNotNull(charSequence);
        W output = outputSupplier.getOutput();
        try {
            output.append(charSequence);
            Closeables.close(output, false);
        } catch (Throwable th) {
            Closeables.close(output, true);
            throw th;
        }
    }

    public static <R extends Readable & Closeable, W extends Appendable & Closeable> long copy(InputSupplier<R> inputSupplier, OutputSupplier<W> outputSupplier) throws IOException {
        R input = inputSupplier.getInput();
        try {
            W output = outputSupplier.getOutput();
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

    public static <R extends Readable & Closeable> long copy(InputSupplier<R> inputSupplier, Appendable appendable) throws IOException {
        R input = inputSupplier.getInput();
        try {
            long jCopy = copy(input, appendable);
            Closeables.close(input, false);
            return jCopy;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static long copy(Readable readable, Appendable appendable) throws IOException {
        CharBuffer charBufferAllocate = CharBuffer.allocate(2048);
        long j = 0;
        while (true) {
            int i = readable.read(charBufferAllocate);
            if (i == -1) {
                return j;
            }
            charBufferAllocate.flip();
            appendable.append(charBufferAllocate, 0, i);
            j += i;
        }
    }

    public static String toString(Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }

    public static <R extends Readable & Closeable> String toString(InputSupplier<R> inputSupplier) throws IOException {
        return toStringBuilder(inputSupplier).toString();
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        StringBuilder sb = new StringBuilder();
        copy(readable, sb);
        return sb;
    }

    private static <R extends Readable & Closeable> StringBuilder toStringBuilder(InputSupplier<R> inputSupplier) throws IOException {
        R input = inputSupplier.getInput();
        try {
            StringBuilder stringBuilder = toStringBuilder(input);
            Closeables.close(input, false);
            return stringBuilder;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static <R extends Readable & Closeable> String readFirstLine(InputSupplier<R> inputSupplier) throws IOException {
        R input = inputSupplier.getInput();
        try {
            String line = new LineReader(input).readLine();
            Closeables.close(input, false);
            return line;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static <R extends Readable & Closeable> List<String> readLines(InputSupplier<R> inputSupplier) throws IOException {
        R input = inputSupplier.getInput();
        try {
            List<String> lines = readLines(input);
            Closeables.close(input, false);
            return lines;
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static List<String> readLines(Readable readable) throws IOException {
        ArrayList arrayList = new ArrayList();
        LineReader lineReader = new LineReader(readable);
        while (true) {
            String line = lineReader.readLine();
            if (line == null) {
                return arrayList;
            }
            arrayList.add(line);
        }
    }

    public static <R extends Readable & Closeable, T> T readLines(InputSupplier<R> inputSupplier, LineProcessor<T> lineProcessor) throws IOException {
        String line;
        R input = inputSupplier.getInput();
        try {
            LineReader lineReader = new LineReader(input);
            do {
                line = lineReader.readLine();
                if (line == null) {
                    break;
                }
            } while (lineProcessor.processLine(line));
            Closeables.close(input, false);
            return lineProcessor.getResult();
        } catch (Throwable th) {
            Closeables.close(input, true);
            throw th;
        }
    }

    public static InputSupplier<Reader> join(final Iterable<? extends InputSupplier<? extends Reader>> iterable) {
        return new InputSupplier<Reader>() { // from class: com.google.common.io.CharStreams.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.io.InputSupplier
            public Reader getInput() throws IOException {
                return new MultiReader(iterable.iterator());
            }
        };
    }

    public static InputSupplier<Reader> join(InputSupplier<? extends Reader>... inputSupplierArr) {
        return join(Arrays.asList(inputSupplierArr));
    }

    public static void skipFully(Reader reader, long j) throws IOException {
        while (j > 0) {
            long jSkip = reader.skip(j);
            if (jSkip != 0) {
                j -= jSkip;
            } else {
                if (reader.read() == -1) {
                    throw new EOFException();
                }
                j--;
            }
        }
    }

    public static Writer asWriter(Appendable appendable) {
        if (appendable instanceof Writer) {
            return (Writer) appendable;
        }
        return new AppendableWriter(appendable);
    }
}
