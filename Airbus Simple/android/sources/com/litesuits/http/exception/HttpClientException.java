package com.litesuits.http.exception;

/* loaded from: classes.dex */
public class HttpClientException extends HttpException {
    private static final long serialVersionUID = -1710690396078830713L;
    private ClientException exceptionType;

    public HttpClientException(ClientException clientException) {
        super(clientException.toString());
        this.exceptionType = clientException;
    }

    public HttpClientException(Throwable th) {
        super(th.toString(), th);
        this.exceptionType = ClientException.SomeOtherException;
    }

    public HttpClientException(Throwable th, ClientException clientException) {
        super(th.toString(), th);
        this.exceptionType = clientException;
    }

    public ClientException getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(ClientException clientException) {
        this.exceptionType = clientException;
    }

    @Override // com.litesuits.http.exception.HttpException, java.lang.Throwable
    public String toString() {
        return this.exceptionType.toString();
    }
}
