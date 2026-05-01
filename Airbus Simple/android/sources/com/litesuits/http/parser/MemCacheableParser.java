package com.litesuits.http.parser;

import com.litesuits.http.log.HttpLog;
import com.litesuits.http.utils.StringCodingUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public abstract class MemCacheableParser<T> extends DataParser<T> {
    @Override // com.litesuits.http.parser.DataParser
    public boolean isMemCacheSupport() {
        return true;
    }

    protected abstract T parseDiskCache(InputStream inputStream, long j) throws IOException;

    protected String translateString(String str) {
        return str;
    }

    protected abstract boolean tryKeepToCache(T t) throws IOException;

    @Override // com.litesuits.http.parser.DataParser
    public final T readFromNetStream(InputStream inputStream, long j, String str) throws IOException {
        this.data = (T) super.readFromNetStream(inputStream, j, str);
        if (this.data != null && this.request.isCachedModel()) {
            tryKeepToCache(this.data);
        }
        return this.data;
    }

    @Override // com.litesuits.http.parser.DataParser
    public final T readFromDiskCache(File file) throws Throwable {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (Throwable th) {
            th = th;
            fileInputStream = null;
        }
        try {
            this.data = parseDiskCache(fileInputStream, file.length());
            fileInputStream.close();
            return this.data;
        } catch (Throwable th2) {
            th = th2;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    protected final byte[] streamToByteArray(InputStream inputStream, long j) throws IOException {
        int i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[this.buffSize];
            while (!this.request.isCancelledOrInterrupted() && (i = inputStream.read(bArr)) > 0) {
                byteArrayOutputStream.write(bArr, 0, i);
                this.readLength += i;
            }
            return translateBytes(byteArrayOutputStream.toByteArray());
        } finally {
            byteArrayOutputStream.close();
        }
    }

    protected final String streamToString(InputStream inputStream, long j, String str) throws IOException {
        int i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[this.buffSize];
            while (!this.request.isCancelledOrInterrupted() && (i = inputStream.read(bArr)) > 0) {
                byteArrayOutputStream.write(bArr, 0, i);
                this.readLength += i;
                notifyReading(j, this.readLength);
            }
            return translateString(byteArrayOutputStream.toString(str));
        } finally {
            byteArrayOutputStream.close();
        }
    }

    protected final boolean keepToCache(String str) throws IOException {
        if (str != null) {
            return keepToCache(StringCodingUtils.getBytes(str, Charset.forName(this.charSet)));
        }
        return false;
    }

    protected final boolean keepToCache(byte[] bArr) throws Throwable {
        FileOutputStream fileOutputStream;
        File cachedFile;
        if (bArr == null) {
            return false;
        }
        try {
            cachedFile = this.request.getCachedFile();
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
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            if (HttpLog.isPrint) {
                HttpLog.v(TAG, "lite http keep disk cache success,    tag: " + this.request.getTag() + "   url: " + this.request.getUri() + "   key: " + this.request.getCacheKey() + "   path: " + cachedFile.getAbsolutePath());
            }
            fileOutputStream.close();
            return true;
        } catch (Throwable th2) {
            th = th2;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }
}
