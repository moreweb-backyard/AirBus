package com.android.internal.http.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class FilePart extends PartBase {
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    protected static final String FILE_NAME = "; filename=";

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, PartSource partSource, String str2, String str3) {
        String str4 = (String) null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, PartSource partSource) {
        String str2 = (String) null;
        super(str2, str2, str2, str2);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, File file) throws FileNotFoundException {
        String str2 = (String) null;
        super(str2, str2, str2, str2);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, File file, String str2, String str3) throws FileNotFoundException {
        String str4 = (String) null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, String str2, File file) throws FileNotFoundException {
        String str3 = (String) null;
        super(str3, str3, str3, str3);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public FilePart(String str, String str2, File file, String str3, String str4) throws FileNotFoundException {
        String str5 = (String) null;
        super(str5, str5, str5, str5);
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.Part
    protected void sendDispositionHeader(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.Part
    protected void sendData(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    protected PartSource getSource() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.Part
    protected long lengthOfData() {
        throw new RuntimeException("Stub!");
    }
}
