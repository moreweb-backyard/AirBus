package cz.msebera.android.httpclient.client.params;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.annotation.NotThreadSafe;
import cz.msebera.android.httpclient.params.HttpAbstractParamBean;
import cz.msebera.android.httpclient.params.HttpParams;
import java.util.Collection;

@NotThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class ClientParamBean extends HttpAbstractParamBean {
    public ClientParamBean(HttpParams httpParams) {
        super(httpParams);
    }

    @Deprecated
    public void setConnectionManagerFactoryClassName(String str) {
        this.params.setParameter("http.connection-manager.factory-class-name", str);
    }

    public void setHandleRedirects(boolean z) {
        this.params.setBooleanParameter("http.protocol.handle-redirects", z);
    }

    public void setRejectRelativeRedirect(boolean z) {
        this.params.setBooleanParameter("http.protocol.reject-relative-redirect", z);
    }

    public void setMaxRedirects(int i) {
        this.params.setIntParameter("http.protocol.max-redirects", i);
    }

    public void setAllowCircularRedirects(boolean z) {
        this.params.setBooleanParameter("http.protocol.allow-circular-redirects", z);
    }

    public void setHandleAuthentication(boolean z) {
        this.params.setBooleanParameter("http.protocol.handle-authentication", z);
    }

    public void setCookiePolicy(String str) {
        this.params.setParameter("http.protocol.cookie-policy", str);
    }

    public void setVirtualHost(HttpHost httpHost) {
        this.params.setParameter("http.virtual-host", httpHost);
    }

    public void setDefaultHeaders(Collection<Header> collection) {
        this.params.setParameter("http.default-headers", collection);
    }

    public void setDefaultHost(HttpHost httpHost) {
        this.params.setParameter("http.default-host", httpHost);
    }

    public void setConnectionManagerTimeout(long j) {
        this.params.setLongParameter("http.conn-manager.timeout", j);
    }
}
