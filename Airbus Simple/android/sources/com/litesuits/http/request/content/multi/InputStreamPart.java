package com.litesuits.http.request.content.multi;

import com.litesuits.http.log.HttpLog;
import com.litesuits.http.utils.StringCodingUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class InputStreamPart extends AbstractPart {
    protected static final String TAG = "InputStreamPart";
    protected String fileName;
    protected InputStream inputStream;

    public InputStreamPart(String str, InputStream inputStream) {
        this(str, inputStream, null, "application/octet-stream");
    }

    public InputStreamPart(String str, InputStream inputStream, String str2) {
        this(str, inputStream, null, str2);
    }

    public InputStreamPart(String str, InputStream inputStream, String str2, String str3) {
        super(str, str3);
        if (inputStream == null) {
            throw new NullPointerException("InputStream not be null !");
        }
        this.inputStream = inputStream;
        this.fileName = str2;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentType() {
        return StringCodingUtils.getBytes("Content-Type: " + this.mimeType + IOUtils.LINE_SEPARATOR_WINDOWS, infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentDisposition() {
        String str = "Content-Disposition: form-data; name=\"" + this.key;
        if (this.fileName == null) {
            return StringCodingUtils.getBytes(str + "\"\r\n", infoCharset);
        }
        return StringCodingUtils.getBytes(str + "\"; filename=\"" + this.fileName + "\"\r\n", infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public long getTotalLength() throws IOException {
        long jAvailable = this.inputStream.available();
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, TAG + "内容长度 header ： " + this.header.length + " ,body: " + jAvailable + " ,换行：" + CR_LF.length);
        }
        return this.header.length + jAvailable + CR_LF.length;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public byte[] getTransferEncoding() {
        return TRANSFER_ENCODING_BINARY;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public void writeTo(OutputStream outputStream) throws IOException {
        try {
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int i = this.inputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, i);
                    updateProgress(i);
                }
                outputStream.write(CR_LF);
                updateProgress(CR_LF.length);
                outputStream.flush();
                if (this.inputStream == null) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (this.inputStream == null) {
                    return;
                }
            }
            this.inputStream.close();
        } catch (Throwable th) {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            throw th;
        }
    }
}
