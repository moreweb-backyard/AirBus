package cz.msebera.android.httpclient.client.params;

import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;

@Immutable
@Deprecated
/* loaded from: classes.dex */
public class HttpClientParams {
    private HttpClientParams() {
    }

    public static boolean isRedirecting(HttpParams httpParams) {
        Args.notNull(httpParams, "HTTP parameters");
        return httpParams.getBooleanParameter("http.protocol.handle-redirects", true);
    }

    public static void setRedirecting(HttpParams httpParams, boolean z) {
        Args.notNull(httpParams, "HTTP parameters");
        httpParams.setBooleanParameter("http.protocol.handle-redirects", z);
    }

    public static boolean isAuthenticating(HttpParams httpParams) {
        Args.notNull(httpParams, "HTTP parameters");
        return httpParams.getBooleanParameter("http.protocol.handle-authentication", true);
    }

    public static void setAuthenticating(HttpParams httpParams, boolean z) {
        Args.notNull(httpParams, "HTTP parameters");
        httpParams.setBooleanParameter("http.protocol.handle-authentication", z);
    }

    public static String getCookiePolicy(HttpParams httpParams) {
        Args.notNull(httpParams, "HTTP parameters");
        String str = (String) httpParams.getParameter("http.protocol.cookie-policy");
        return str == null ? "best-match" : str;
    }

    public static void setCookiePolicy(HttpParams httpParams, String str) {
        Args.notNull(httpParams, "HTTP parameters");
        httpParams.setParameter("http.protocol.cookie-policy", str);
    }

    public static void setConnectionManagerTimeout(HttpParams httpParams, long j) {
        Args.notNull(httpParams, "HTTP parameters");
        httpParams.setLongParameter("http.conn-manager.timeout", j);
    }

    public static long getConnectionManagerTimeout(HttpParams httpParams) {
        Args.notNull(httpParams, "HTTP parameters");
        Long l = (Long) httpParams.getParameter("http.conn-manager.timeout");
        if (l != null) {
            return l.longValue();
        }
        return HttpConnectionParams.getConnectionTimeout(httpParams);
    }
}
