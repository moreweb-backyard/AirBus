package com.litesuits.http.request.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class ByteArrayBody extends HttpBody {
    private byte[] bytes;

    public ByteArrayBody(byte[] bArr) {
        this(bArr, "application/octet-stream");
    }

    public ByteArrayBody(byte[] bArr, String str) {
        this.bytes = bArr;
        this.contentType = str;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public long getContentLength() {
        return this.bytes.length;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.bytes);
        outputStream.flush();
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String toString() {
        return "ByteArrayBody{bytes=" + Arrays.toString(this.bytes) + "} " + super.toString();
    }
}
