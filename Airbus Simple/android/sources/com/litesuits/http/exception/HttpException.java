package com.litesuits.http.exception;

/* loaded from: classes.dex */
public abstract class HttpException extends Exception {
    public static boolean printStackTrace = true;
    private static final long serialVersionUID = -8585446012573642784L;
    protected boolean handled;

    public boolean isHandled() {
        return this.handled;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends HttpException> T setHandled(boolean z) {
        this.handled = z;
        return this;
    }

    public HttpException() {
        this.handled = true;
    }

    public HttpException(String str) {
        super(str);
        this.handled = true;
    }

    public HttpException(String str, Throwable th) {
        super(str, th);
        this.handled = true;
    }

    public HttpException(Throwable th) {
        super(th);
        this.handled = true;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "HttpException{handled=" + this.handled + "} " + super.toString();
    }
}
