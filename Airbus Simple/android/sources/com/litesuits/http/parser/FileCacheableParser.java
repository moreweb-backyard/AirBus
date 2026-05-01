package com.litesuits.http.parser;

import com.litesuits.http.log.HttpLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class FileCacheableParser<T> extends DataParser<T> {
    protected File file;

    @Override // com.litesuits.http.parser.DataParser
    public boolean isMemCacheSupport() {
        return false;
    }

    protected abstract T parseDiskCache(File file) throws IOException;

    public FileCacheableParser() {
    }

    public FileCacheableParser(File file) {
        this.file = file;
    }

    @Override // com.litesuits.http.parser.DataParser
    public final T readFromDiskCache(File file) throws IOException {
        this.data = parseDiskCache(file);
        return this.data;
    }

    protected final File streamToFile(InputStream inputStream, long j) throws Throwable {
        FileOutputStream fileOutputStream;
        int i;
        File cachedFile = this.request.getCachedFile();
        try {
            File parentFile = cachedFile.getParentFile();
            if (!parentFile.exists()) {
                boolean zMkdirs = parentFile.mkdirs();
                if (HttpLog.isPrint) {
                    HttpLog.i(TAG, "keep cache mkdirs result: " + zMkdirs + "  path:" + parentFile.getAbsolutePath());
                }
            }
            fileOutputStream = new FileOutputStream(cachedFile);
        } catch (Throwable th) {
            th = th;
            fileOutputStream = null;
        }
        try {
            byte[] bArrTranslateBytes = new byte[this.buffSize];
            while (!this.request.isCancelledOrInterrupted() && (i = inputStream.read(bArrTranslateBytes)) != -1) {
                bArrTranslateBytes = translateBytes(bArrTranslateBytes);
                fileOutputStream.write(bArrTranslateBytes, 0, i);
                this.readLength += i;
                notifyReading(j, this.readLength);
            }
            if (HttpLog.isPrint) {
                HttpLog.i("FileParser", "file len: " + cachedFile.length());
            }
            fileOutputStream.close();
            return cachedFile;
        } catch (Throwable th2) {
            th = th2;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }
}
