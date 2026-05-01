package com.litesuits.http.request.content;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class StringBody extends HttpBody {
    protected String charset;
    protected byte[] content;
    protected String mimeType;
    protected String string;

    public StringBody(String str) {
        this(str, null, null);
    }

    public StringBody(String str, String str2, String str3) {
        str2 = str2 == null ? "text/plain" : str2;
        str3 = str3 == null ? "UTF-8" : str3;
        this.charset = str3;
        this.string = str;
        this.mimeType = str2;
        try {
            this.content = str.getBytes(str3);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.contentType = str2 + "; charset=" + str3;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public long getContentLength() {
        return this.content.length;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.content);
        outputStream.flush();
    }

    public String getString() {
        return this.string;
    }

    public String getCharset() {
        return this.charset;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String toString() {
        return "StringBody{charset='" + this.charset + "', mimeType='" + this.mimeType + "', string='" + this.string + "'} ";
    }
}
