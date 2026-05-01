package org.apache.commons.fileupload.util;

import java.io.IOException;

/* loaded from: classes.dex */
public interface Closeable {
    void close() throws IOException;

    boolean isClosed() throws IOException;
}
