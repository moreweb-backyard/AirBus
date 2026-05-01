package com.litesuits.http.exception;

import com.litesuits.http.data.HttpStatus;

/* loaded from: classes.dex */
public class HttpServerException extends HttpException {
    private static final long serialVersionUID = 3695887939006497385L;
    private ServerException exceptionType;
    private HttpStatus status;

    public HttpServerException(ServerException serverException) {
        super(serverException.toString());
        this.exceptionType = serverException;
    }

    public HttpServerException(HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.status = httpStatus;
        if (httpStatus.getCode() >= 500) {
            this.exceptionType = ServerException.ServerInnerError;
        } else {
            this.exceptionType = ServerException.ServerRejectClient;
        }
    }

    public ServerException getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(ServerException serverException) {
        this.exceptionType = serverException;
    }

    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override // com.litesuits.http.exception.HttpException, java.lang.Throwable
    public String toString() {
        return this.exceptionType + ", " + this.status;
    }
}
