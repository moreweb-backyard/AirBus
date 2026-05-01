package com.litesuits.http.request.content.multi;

import com.litesuits.http.utils.StringCodingUtils;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class BoundaryCreater {
    private String boundary;
    private byte[] boundaryEnd;
    private byte[] boundaryLine;
    public static final Charset charset = Charset.forName("UTF-8");
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public BoundaryCreater() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = sb.toString();
        this.boundaryLine = StringCodingUtils.getBytes("--" + this.boundary + IOUtils.LINE_SEPARATOR_WINDOWS, charset);
        this.boundaryEnd = StringCodingUtils.getBytes("--" + this.boundary + "--\r\n", charset);
    }

    public String getBoundary() {
        return this.boundary;
    }

    public byte[] getBoundaryLine() {
        return this.boundaryLine;
    }

    public byte[] getBoundaryEnd() {
        return this.boundaryEnd;
    }
}
