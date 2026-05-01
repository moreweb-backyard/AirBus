package org.apache.commons.fileupload.disk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ParameterParser;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DeferredFileOutputStream;

/* loaded from: classes.dex */
public class DiskFileItem implements FileItem {
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final long serialVersionUID = 2237570099615271025L;
    private byte[] cachedContent;
    private final String contentType;
    private transient DeferredFileOutputStream dfos;
    private File dfosFile;
    private String fieldName;
    private final String fileName;
    private FileItemHeaders headers;
    private boolean isFormField;
    private final File repository;
    private long size = -1;
    private final int sizeThreshold;
    private transient File tempFile;
    private static final String UID = UUID.randomUUID().toString().replace('-', '_');
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public DiskFileItem(String str, String str2, boolean z, String str3, int i, File file) {
        this.fieldName = str;
        this.contentType = str2;
        this.isFormField = z;
        this.fileName = str3;
        this.sizeThreshold = i;
        this.repository = file;
    }

    @Override // org.apache.commons.fileupload.FileItem
    public InputStream getInputStream() throws IOException {
        if (!isInMemory()) {
            return new FileInputStream(this.dfos.getFile());
        }
        if (this.cachedContent == null) {
            this.cachedContent = this.dfos.getData();
        }
        return new ByteArrayInputStream(this.cachedContent);
    }

    @Override // org.apache.commons.fileupload.FileItem
    public String getContentType() {
        return this.contentType;
    }

    public String getCharSet() {
        ParameterParser parameterParser = new ParameterParser();
        parameterParser.setLowerCaseNames(true);
        return parameterParser.parse(getContentType(), ';').get("charset");
    }

    @Override // org.apache.commons.fileupload.FileItem
    public String getName() {
        return Streams.checkFileName(this.fileName);
    }

    @Override // org.apache.commons.fileupload.FileItem
    public boolean isInMemory() {
        if (this.cachedContent != null) {
            return true;
        }
        return this.dfos.isInMemory();
    }

    @Override // org.apache.commons.fileupload.FileItem
    public long getSize() {
        if (this.size >= 0) {
            return this.size;
        }
        if (this.cachedContent != null) {
            return this.cachedContent.length;
        }
        if (this.dfos.isInMemory()) {
            return this.dfos.getData().length;
        }
        return this.dfos.getFile().length();
    }

    @Override // org.apache.commons.fileupload.FileItem
    public byte[] get() throws Throwable {
        BufferedInputStream bufferedInputStream;
        if (isInMemory()) {
            if (this.cachedContent == null) {
                this.cachedContent = this.dfos.getData();
            }
            return this.cachedContent;
        }
        byte[] bArr = new byte[(int) getSize()];
        BufferedInputStream bufferedInputStream2 = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(this.dfos.getFile()));
        } catch (IOException unused) {
            bufferedInputStream = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            bufferedInputStream.read(bArr);
            try {
                bufferedInputStream.close();
                return bArr;
            } catch (IOException unused2) {
                return bArr;
            }
        } catch (IOException unused3) {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException unused4) {
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            bufferedInputStream2 = bufferedInputStream;
            if (bufferedInputStream2 != null) {
                try {
                    bufferedInputStream2.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    @Override // org.apache.commons.fileupload.FileItem
    public String getString(String str) throws UnsupportedEncodingException {
        return new String(get(), str);
    }

    @Override // org.apache.commons.fileupload.FileItem
    public String getString() throws Throwable {
        byte[] bArr = get();
        String charSet = getCharSet();
        if (charSet == null) {
            charSet = "ISO-8859-1";
        }
        try {
            return new String(bArr, charSet);
        } catch (UnsupportedEncodingException unused) {
            return new String(bArr);
        }
    }

    @Override // org.apache.commons.fileupload.FileItem
    public void write(File file) throws Exception {
        FileOutputStream fileOutputStream;
        BufferedInputStream bufferedInputStream;
        BufferedOutputStream bufferedOutputStream;
        BufferedOutputStream bufferedOutputStream2 = null;
        if (isInMemory()) {
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (Throwable th) {
                th = th;
                fileOutputStream = null;
            }
            try {
                fileOutputStream.write(get());
                fileOutputStream.close();
                return;
            } catch (Throwable th2) {
                th = th2;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        }
        File storeLocation = getStoreLocation();
        if (storeLocation != null) {
            this.size = storeLocation.length();
            if (storeLocation.renameTo(file)) {
                return;
            }
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(storeLocation));
                try {
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedInputStream = null;
            }
            try {
                IOUtils.copy(bufferedInputStream, bufferedOutputStream);
                try {
                    bufferedInputStream.close();
                } catch (IOException unused) {
                }
                try {
                    bufferedOutputStream.close();
                    return;
                } catch (IOException unused2) {
                    return;
                }
            } catch (Throwable th5) {
                th = th5;
                bufferedOutputStream2 = bufferedOutputStream;
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException unused3) {
                    }
                }
                if (bufferedOutputStream2 != null) {
                    try {
                        bufferedOutputStream2.close();
                        throw th;
                    } catch (IOException unused4) {
                        throw th;
                    }
                }
                throw th;
            }
        }
        throw new FileUploadException("Cannot write uploaded file to disk!");
    }

    @Override // org.apache.commons.fileupload.FileItem
    public void delete() {
        this.cachedContent = null;
        File storeLocation = getStoreLocation();
        if (storeLocation == null || !storeLocation.exists()) {
            return;
        }
        storeLocation.delete();
    }

    @Override // org.apache.commons.fileupload.FileItem
    public String getFieldName() {
        return this.fieldName;
    }

    @Override // org.apache.commons.fileupload.FileItem
    public void setFieldName(String str) {
        this.fieldName = str;
    }

    @Override // org.apache.commons.fileupload.FileItem
    public boolean isFormField() {
        return this.isFormField;
    }

    @Override // org.apache.commons.fileupload.FileItem
    public void setFormField(boolean z) {
        this.isFormField = z;
    }

    @Override // org.apache.commons.fileupload.FileItem
    public OutputStream getOutputStream() throws IOException {
        if (this.dfos == null) {
            this.dfos = new DeferredFileOutputStream(this.sizeThreshold, getTempFile());
        }
        return this.dfos;
    }

    public File getStoreLocation() {
        if (this.dfos == null) {
            return null;
        }
        return this.dfos.getFile();
    }

    protected void finalize() {
        File file = this.dfos.getFile();
        if (file == null || !file.exists()) {
            return;
        }
        file.delete();
    }

    protected File getTempFile() {
        if (this.tempFile == null) {
            File file = this.repository;
            if (file == null) {
                file = new File(System.getProperty("java.io.tmpdir"));
            }
            this.tempFile = new File(file, String.format("upload_%s_%s.tmp", UID, getUniqueId()));
        }
        return this.tempFile;
    }

    private static String getUniqueId() {
        int andIncrement = COUNTER.getAndIncrement();
        String string = Integer.toString(andIncrement);
        if (andIncrement >= 100000000) {
            return string;
        }
        return ("00000000" + string).substring(string.length());
    }

    public String toString() {
        return String.format("name=%s, StoreLocation=%s, size=%s bytes, isFormField=%s, FieldName=%s", getName(), getStoreLocation(), Long.valueOf(getSize()), Boolean.valueOf(isFormField()), getFieldName());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.dfos.isInMemory()) {
            this.cachedContent = get();
        } else {
            this.cachedContent = null;
            this.dfosFile = this.dfos.getFile();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        if (this.repository != null) {
            if (this.repository.isDirectory()) {
                if (this.repository.getPath().contains("\u0000")) {
                    throw new IOException(String.format("The repository [%s] contains a null character", this.repository.getPath()));
                }
            } else {
                throw new IOException(String.format("The repository [%s] is not a directory", this.repository.getAbsolutePath()));
            }
        }
        OutputStream outputStream = getOutputStream();
        if (this.cachedContent != null) {
            outputStream.write(this.cachedContent);
        } else {
            IOUtils.copy(new FileInputStream(this.dfosFile), outputStream);
            this.dfosFile.delete();
            this.dfosFile = null;
        }
        outputStream.close();
        this.cachedContent = null;
    }

    @Override // org.apache.commons.fileupload.FileItemHeadersSupport
    public FileItemHeaders getHeaders() {
        return this.headers;
    }

    @Override // org.apache.commons.fileupload.FileItemHeadersSupport
    public void setHeaders(FileItemHeaders fileItemHeaders) {
        this.headers = fileItemHeaders;
    }
}
