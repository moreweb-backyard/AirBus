package com.shima.smartbushome.andserver;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class RequestFileHandler implements RequestHandler {
    private String mFilePath;

    public RequestFileHandler(String str) {
        this.mFilePath = str;
    }

    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IllegalStateException, IOException {
        File file = new File(this.mFilePath);
        if (file.exists()) {
            httpResponse.setStatusCode(200);
            httpResponse.setHeader("ContentLength", Long.toString(file.length()));
            httpResponse.setEntity(new FileEntity(file, HttpRequestParser.getMimeType(file.getName())));
        } else {
            httpResponse.setStatusCode(404);
            httpResponse.setEntity(new StringEntity(""));
        }
    }
}
