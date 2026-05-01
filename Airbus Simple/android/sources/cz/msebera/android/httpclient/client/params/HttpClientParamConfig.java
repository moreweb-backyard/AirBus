package cz.msebera.android.httpclient.client.params;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.auth.params.AuthPNames;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.params.CoreConnectionPNames;
import cz.msebera.android.httpclient.params.HttpParams;
import java.net.InetAddress;
import java.util.Collection;

@Deprecated
/* loaded from: classes.dex */
public final class HttpClientParamConfig {
    private HttpClientParamConfig() {
    }

    public static RequestConfig getRequestConfig(HttpParams httpParams) {
        return RequestConfig.custom().setSocketTimeout(httpParams.getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0)).setStaleConnectionCheckEnabled(httpParams.getBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true)).setConnectTimeout(httpParams.getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0)).setExpectContinueEnabled(httpParams.getBooleanParameter("http.protocol.expect-continue", false)).setProxy((HttpHost) httpParams.getParameter("http.route.default-proxy")).setLocalAddress((InetAddress) httpParams.getParameter("http.route.local-address")).setProxyPreferredAuthSchemes((Collection) httpParams.getParameter(AuthPNames.PROXY_AUTH_PREF)).setTargetPreferredAuthSchemes((Collection) httpParams.getParameter(AuthPNames.TARGET_AUTH_PREF)).setAuthenticationEnabled(httpParams.getBooleanParameter("http.protocol.handle-authentication", true)).setCircularRedirectsAllowed(httpParams.getBooleanParameter("http.protocol.allow-circular-redirects", false)).setConnectionRequestTimeout((int) httpParams.getLongParameter("http.conn-manager.timeout", 0L)).setCookieSpec((String) httpParams.getParameter("http.protocol.cookie-policy")).setMaxRedirects(httpParams.getIntParameter("http.protocol.max-redirects", 50)).setRedirectsEnabled(httpParams.getBooleanParameter("http.protocol.handle-redirects", true)).setRelativeRedirectsAllowed(!httpParams.getBooleanParameter("http.protocol.reject-relative-redirect", false)).build();
    }
}
