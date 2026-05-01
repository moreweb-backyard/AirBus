package com.litesuits.http.exception.handler;

import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.exception.ClientException;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.HttpServerException;
import com.litesuits.http.exception.NetException;
import com.litesuits.http.exception.ServerException;

/* loaded from: classes.dex */
public abstract class HttpExceptionHandler {
    protected abstract void onClientException(HttpClientException httpClientException, ClientException clientException);

    protected abstract void onNetException(HttpNetException httpNetException, NetException netException);

    protected abstract void onServerException(HttpServerException httpServerException, ServerException serverException, HttpStatus httpStatus);

    public HttpExceptionHandler handleException(HttpException httpException) {
        if (httpException != null) {
            if (httpException instanceof HttpClientException) {
                HttpClientException httpClientException = (HttpClientException) httpException;
                onClientException(httpClientException, httpClientException.getExceptionType());
            } else if (httpException instanceof HttpNetException) {
                HttpNetException httpNetException = (HttpNetException) httpException;
                onNetException(httpNetException, httpNetException.getExceptionType());
            } else if (httpException instanceof HttpServerException) {
                HttpServerException httpServerException = (HttpServerException) httpException;
                onServerException(httpServerException, httpServerException.getExceptionType(), httpServerException.getHttpStatus());
            } else {
                HttpClientException httpClientException2 = new HttpClientException(httpException);
                onClientException(httpClientException2, httpClientException2.getExceptionType());
            }
            httpException.setHandled(true);
        }
        return this;
    }
}
