package com.yanzhenjie.andserver.handler;

import com.yanzhenjie.andserver.util.AssetsWrapper;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class AssetsRequestHandler extends BasicRequestHandler {
    private AssetsWrapper mAssetsWrapper;
    private String mFilePath;

    public AssetsRequestHandler(AssetsWrapper assetsWrapper, String str) {
        this.mAssetsWrapper = assetsWrapper;
        this.mFilePath = str;
    }

    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IllegalStateException, IOException {
        InputStream inputStream = this.mAssetsWrapper.getInputStream(this.mFilePath);
        if (inputStream == null) {
            requestInvalid(httpResponse);
        } else {
            httpResponse.setStatusCode(200);
            httpResponse.setEntity(new InputStreamEntity(inputStream, inputStream.available()));
        }
    }
}
