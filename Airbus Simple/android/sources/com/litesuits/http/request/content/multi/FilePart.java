package com.litesuits.http.request.content.multi;

import com.litesuits.http.log.HttpLog;
import com.litesuits.http.utils.StringCodingUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class FilePart extends AbstractPart {
    public static final String TAG = "FilePart";
    public File file;

    public FilePart(String str, File file) {
        this(str, file, "application/octet-stream");
    }

    public FilePart(String str, File file, String str2) {
        super(str, str2);
        this.file = file;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentType() {
        return StringCodingUtils.getBytes("Content-Type: " + this.mimeType + IOUtils.LINE_SEPARATOR_WINDOWS, infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentDisposition() {
        return StringCodingUtils.getBytes(("Content-Disposition: form-data; name=\"" + this.key) + "\"; filename=\"" + this.file.getName() + "\"\r\n", infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public long getTotalLength() {
        long length = this.file.length();
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, TAG + " 内容长度header ： " + this.header.length + " ,body: " + length + " ,换行：" + CR_LF.length);
        }
        return this.header.length + length + CR_LF.length;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public byte[] getTransferEncoding() {
        return TRANSFER_ENCODING_BINARY;
    }

    @Override // com.litesuits.http.request.content.multi.AbstractPart
    public void writeTo(OutputStream outputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        try {
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, i);
                    updateProgress(i);
                }
                outputStream.write(CR_LF);
                updateProgress(CR_LF.length);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            fileInputStream.close();
        }
    }
}
