package cz.msebera.android.httpclient.conn.params;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.annotation.NotThreadSafe;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.params.HttpAbstractParamBean;
import cz.msebera.android.httpclient.params.HttpParams;
import java.net.InetAddress;

@NotThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class ConnRouteParamBean extends HttpAbstractParamBean {
    public ConnRouteParamBean(HttpParams httpParams) {
        super(httpParams);
    }

    public void setDefaultProxy(HttpHost httpHost) {
        this.params.setParameter("http.route.default-proxy", httpHost);
    }

    public void setLocalAddress(InetAddress inetAddress) {
        this.params.setParameter("http.route.local-address", inetAddress);
    }

    public void setForcedRoute(HttpRoute httpRoute) {
        this.params.setParameter("http.route.forced-route", httpRoute);
    }
}
