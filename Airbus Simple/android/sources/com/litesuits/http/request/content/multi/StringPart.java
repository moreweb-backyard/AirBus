package com.litesuits.http.request.content.multi;

import com.litesuits.http.utils.StringCodingUtils;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class StringPart extends BytesPart {
    protected String charset;
    protected String mimeType;

    public StringPart(String str, String str2) {
        this(str, str2, "UTF-8", "text/plain");
    }

    public StringPart(String str, String str2, String str3, String str4) {
        super(str, getBytes(str2, str3), str4);
        this.mimeType = str4 == null ? "text/plain" : str4;
        this.charset = str3;
    }

    public static byte[] getBytes(String str, String str2) {
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.litesuits.http.request.content.multi.BytesPart, com.litesuits.http.request.content.multi.AbstractPart
    protected byte[] createContentType() {
        return StringCodingUtils.getBytes("Content-Type: " + this.mimeType + " ; charset=" + this.charset + IOUtils.LINE_SEPARATOR_WINDOWS, infoCharset);
    }

    @Override // com.litesuits.http.request.content.multi.BytesPart, com.litesuits.http.request.content.multi.AbstractPart
    public byte[] getTransferEncoding() {
        return TRANSFER_ENCODING_8BIT;
    }
}
