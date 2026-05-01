package com.yanzhenjie.andserver.handler;

import com.yanzhenjie.andserver.RequestHandler;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

/* loaded from: classes.dex */
public abstract class BasicRequestHandler implements RequestHandler {
    protected void requestInvalid(HttpResponse httpResponse) throws HttpException, IllegalStateException, IOException {
        requestInvalid(httpResponse, "The requested resource does not exist.");
    }

    protected void requestInvalid(HttpResponse httpResponse, String str) throws HttpException, IllegalStateException, IOException {
        httpResponse.setStatusCode(404);
        httpResponse.setEntity(new StringEntity(str));
    }
}
