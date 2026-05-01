package com.yanzhenjie.andserver.handler;

import com.yanzhenjie.andserver.util.HttpRequestParser;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.FileEntity;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class StorageRequestHandler extends BasicRequestHandler {
    private String mFilePath;

    public StorageRequestHandler(String str) {
        this.mFilePath = str;
    }

    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IllegalStateException, IOException {
        File file = new File(this.mFilePath);
        if (!file.exists()) {
            requestInvalid(httpResponse);
        } else {
            httpResponse.setStatusCode(200);
            httpResponse.setEntity(new FileEntity(file, HttpRequestParser.getMimeType(file.getName())));
        }
    }
}
