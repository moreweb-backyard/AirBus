package com.litesuits.http.exception;

/* loaded from: classes.dex */
public class HttpNetException extends HttpException {
    private static final long serialVersionUID = 4961807092977094093L;
    private NetException exceptionType;

    public HttpNetException(NetException netException) {
        super(netException.toString());
        this.exceptionType = netException;
    }

    public HttpNetException(Throwable th) {
        super(th.toString(), th);
        this.exceptionType = NetException.NetworkUnstable;
    }

    public NetException getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(NetException netException) {
        this.exceptionType = netException;
    }

    @Override // com.litesuits.http.exception.HttpException, java.lang.Throwable
    public String toString() {
        return this.exceptionType.toString();
    }
}
