package com.litesuits.http.request.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class FileBody extends HttpBody {
    private File file;

    public FileBody(File file) {
        this(file, "application/octet-stream");
    }

    public FileBody(File file, String str) {
        this.file = file;
        this.contentType = str;
    }

    public File getFile() {
        return this.file;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public long getContentLength() {
        return this.file.length();
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public void writeTo(OutputStream outputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        try {
            byte[] bArr = new byte[4096];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i != -1) {
                    outputStream.write(bArr, 0, i);
                } else {
                    outputStream.flush();
                    return;
                }
            }
        } finally {
            fileInputStream.close();
        }
    }
}
