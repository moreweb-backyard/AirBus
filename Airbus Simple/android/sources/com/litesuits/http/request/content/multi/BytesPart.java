package com.litesuits.http.request.content.multi;

import com.litesuits.http.log.HttpLog;
import com.litesuits.http.utils.StringCodingUtils;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class BytesPart extends AbstractPart {
    public static final String TAG = "BytesPart";
    public byte[] bytes;

    public BytesPart(String str, byte[] bArr) {
        this(str, bArr, null);
        this.bytes = bArr;
    }

    public BytesPart(String str, byte[] bArr, String str2) {
        super(str, str2);
        this.bytes = bArr;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentType() {
        return StringCodingUtils.getBytes("Content-Type: " + this.mimeType + IOUtils.LINE_SEPARATOR_WINDOWS, infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentDisposition() {
        return StringCodingUtils.getBytes("Content-Disposition: form-data; name=\"" + this.key + "\"\r\n", infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public long getTotalLength() {
        if (HttpLog.isPrint && HttpLog.isPrint) {
            HttpLog.v(TAG, TAG + "内容长度 header ： " + this.header.length + " ,body: " + this.bytes.length + " ,换行：" + CR_LF.length);
        }
        return this.header.length + this.bytes.length + CR_LF.length;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public byte[] getTransferEncoding() {
        return TRANSFER_ENCODING_BINARY;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.bytes);
        outputStream.write(CR_LF);
        updateProgress(this.bytes.length + CR_LF.length);
    }
}
