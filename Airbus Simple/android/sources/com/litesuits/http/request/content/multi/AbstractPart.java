package com.litesuits.http.request.content.multi;

import com.litesuits.http.utils.StringCodingUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public abstract class AbstractPart {
    public byte[] header;
    protected String key;
    protected String mimeType;
    protected MultipartBody multipartBody;
    protected static final Charset infoCharset = BoundaryCreater.charset;
    public static final byte[] CR_LF = StringCodingUtils.getBytes(IOUtils.LINE_SEPARATOR_WINDOWS, infoCharset);
    public static final byte[] TRANSFER_ENCODING_BINARY = StringCodingUtils.getBytes("Content-Transfer-Encoding: binary\r\n", infoCharset);
    public static final byte[] TRANSFER_ENCODING_8BIT = StringCodingUtils.getBytes("Content-Transfer-Encoding: 8bit\r\n", infoCharset);

    protected abstract byte[] createContentDisposition();

    protected abstract byte[] createContentType();

    public abstract long getTotalLength() throws IOException;

    public abstract byte[] getTransferEncoding();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    protected AbstractPart(String str, String str2) {
        this.mimeType = "application/octet-stream";
        this.key = str;
        if (str2 != null) {
            this.mimeType = str2;
        }
    }

    public byte[] createHeader(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(bArr);
            byteArrayOutputStream.write(createContentDisposition());
            byteArrayOutputStream.write(createContentType());
            byteArrayOutputStream.write(getTransferEncoding());
            byteArrayOutputStream.write(CR_LF);
            this.header = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.header;
    }

    public void writeToServer(OutputStream outputStream) throws IOException {
        if (this.header == null) {
            throw new RuntimeException("Not call createHeader()，未调用createHeader方法");
        }
        outputStream.write(this.header);
        updateProgress(this.header.length);
        writeTo(outputStream);
    }

    public MultipartBody getMultipartBody() {
        return this.multipartBody;
    }

    public void setMultipartBody(MultipartBody multipartBody) {
        this.multipartBody = multipartBody;
    }

    protected void updateProgress(int i) {
        if (this.multipartBody != null) {
            this.multipartBody.updateProgress(i);
        }
    }
}
