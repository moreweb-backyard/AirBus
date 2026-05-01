package cz.msebera.android.httpclient.impl.conn;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.data.Consts;
import cz.msebera.android.httpclient.annotation.ThreadSafe;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

@ThreadSafe
@Deprecated
/* loaded from: classes.dex */
public final class SchemeRegistryFactory {
    public static SchemeRegistry createDefault() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme(Consts.SCHEME_HTTPS, HttpConfig.DEFAULT_HTTPS_PORT, SSLSocketFactory.getSocketFactory()));
        return schemeRegistry;
    }

    public static SchemeRegistry createSystemDefault() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme(Consts.SCHEME_HTTPS, HttpConfig.DEFAULT_HTTPS_PORT, SSLSocketFactory.getSystemSocketFactory()));
        return schemeRegistry;
    }
}
