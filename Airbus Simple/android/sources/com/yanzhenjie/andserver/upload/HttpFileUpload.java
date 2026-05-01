package com.yanzhenjie.andserver.upload;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;

/* loaded from: classes.dex */
public class HttpFileUpload extends FileUpload {
    public static final boolean isMultipartContentWithPost(HttpRequest httpRequest) {
        return "POST".equalsIgnoreCase(httpRequest.getRequestLine().getMethod()) && (httpRequest instanceof HttpEntityEnclosingRequest) && FileUploadBase.isMultipartContent(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest));
    }

    public static final boolean isMultipartContent(HttpRequest httpRequest) {
        return (httpRequest instanceof HttpEntityEnclosingRequest) && FileUploadBase.isMultipartContent(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest));
    }

    public HttpFileUpload() {
    }

    public HttpFileUpload(FileItemFactory fileItemFactory) {
        super(fileItemFactory);
    }

    public List<FileItem> parseRequest(HttpRequest httpRequest) throws FileUploadException {
        return parseRequest(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest));
    }

    public Map<String, List<FileItem>> parseParameterMap(HttpRequest httpRequest) throws FileUploadException {
        return parseParameterMap(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest));
    }

    public FileItemIterator getItemIterator(HttpRequest httpRequest) throws FileUploadException, IOException {
        return getItemIterator(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest));
    }
}
