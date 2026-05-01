package cz.msebera.android.httpclient.conn;

import cz.msebera.android.httpclient.annotation.Immutable;
import java.io.IOException;

@Immutable
/* loaded from: classes.dex */
public class UnsupportedSchemeException extends IOException {
    private static final long serialVersionUID = 3597127619218687636L;

    public UnsupportedSchemeException(String str) {
        super(str);
    }
}
