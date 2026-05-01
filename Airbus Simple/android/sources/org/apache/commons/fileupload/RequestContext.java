package org.apache.commons.fileupload;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface RequestContext {
    String getCharacterEncoding();

    @Deprecated
    int getContentLength();

    String getContentType();

    InputStream getInputStream() throws IOException;
}
