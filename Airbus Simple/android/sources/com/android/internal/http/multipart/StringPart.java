package com.android.internal.http.multipart;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class StringPart extends PartBase {
    public static final String DEFAULT_CHARSET = "US-ASCII";
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_TRANSFER_ENCODING = "8bit";

    /* JADX WARN: Illegal instructions before constructor call */
    public StringPart(String str, String str2, String str3) {
        String str4 = (String) null;
        super(str4, str4, str4, str4);
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public StringPart(String str, String str2) {
        String str3 = (String) null;
        super(str3, str3, str3, str3);
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.Part
    protected void sendData(OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.Part
    protected long lengthOfData() {
        throw new RuntimeException("Stub!");
    }

    @Override // com.android.internal.http.multipart.PartBase
    public void setCharSet(String str) {
        throw new RuntimeException("Stub!");
    }
}
