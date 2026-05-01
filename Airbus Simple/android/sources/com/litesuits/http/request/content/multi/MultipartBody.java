package com.litesuits.http.request.content.multi;

import com.litesuits.http.request.content.HttpBody;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class MultipartBody extends HttpBody {
    private long bytesWritten;
    private long totalSize;
    private LinkedList<AbstractPart> httpParts = new LinkedList<>();
    BoundaryCreater boundaryCreater = new BoundaryCreater();

    public MultipartBody() {
        this.contentType = "multipart/form-data; boundary=" + this.boundaryCreater.getBoundary();
    }

    public LinkedList<AbstractPart> getHttpParts() {
        return this.httpParts;
    }

    public MultipartBody setHttpParts(LinkedList<AbstractPart> linkedList) {
        this.httpParts = linkedList;
        return this;
    }

    public MultipartBody addPart(String str, String str2, String str3, String str4) throws UnsupportedEncodingException {
        return addPart(new StringPart(str, str2, str3, str4));
    }

    public MultipartBody addPart(String str, byte[] bArr, String str2) {
        return addPart(new BytesPart(str, bArr, str2));
    }

    public MultipartBody addPart(String str, File file, String str2) throws FileNotFoundException {
        return addPart(new FilePart(str, file, str2));
    }

    public MultipartBody addPart(String str, InputStream inputStream, String str2, String str3) {
        return addPart(new InputStreamPart(str, inputStream, str2, str3));
    }

    public MultipartBody addPart(AbstractPart abstractPart) {
        if (abstractPart == null) {
            return this;
        }
        abstractPart.setMultipartBody(this);
        abstractPart.createHeader(this.boundaryCreater.getBoundaryLine());
        this.httpParts.add(abstractPart);
        return this;
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public long getContentLength() {
        long j;
        IOException e;
        try {
            Iterator<AbstractPart> it = this.httpParts.iterator();
            j = -1;
            while (it.hasNext()) {
                try {
                    long totalLength = it.next().getTotalLength();
                    if (totalLength < 0) {
                        return -1L;
                    }
                    j += totalLength;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    return j;
                }
            }
            return j + this.boundaryCreater.getBoundaryEnd().length;
        } catch (IOException e3) {
            j = -1;
            e = e3;
        }
    }

    @Override // com.litesuits.http.request.content.HttpBody
    public void writeTo(OutputStream outputStream) throws IOException {
        this.bytesWritten = 0L;
        this.totalSize = (int) getContentLength();
        Iterator<AbstractPart> it = this.httpParts.iterator();
        while (it.hasNext()) {
            it.next().writeToServer(outputStream);
        }
        outputStream.write(this.boundaryCreater.getBoundaryEnd());
        updateProgress(this.boundaryCreater.getBoundaryEnd().length);
    }

    protected void updateProgress(long j) {
        this.bytesWritten += j;
        if (this.httpListener != null) {
            this.httpListener.notifyCallUploading(this.request, this.totalSize, this.bytesWritten);
        }
    }

    public BoundaryCreater getBoundary() {
        return this.boundaryCreater;
    }
}
