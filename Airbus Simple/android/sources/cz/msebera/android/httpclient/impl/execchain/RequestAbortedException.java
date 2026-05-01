package cz.msebera.android.httpclient.impl.execchain;

import cz.msebera.android.httpclient.annotation.Immutable;
import java.io.InterruptedIOException;

@Immutable
/* loaded from: classes.dex */
public class RequestAbortedException extends InterruptedIOException {
    private static final long serialVersionUID = 4973849966012490112L;

    public RequestAbortedException(String str) {
        super(str);
    }

    public RequestAbortedException(String str, Throwable th) {
        super(str);
        if (th != null) {
            initCause(th);
        }
    }
}
