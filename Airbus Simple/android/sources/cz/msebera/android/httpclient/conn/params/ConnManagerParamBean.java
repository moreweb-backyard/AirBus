package cz.msebera.android.httpclient.conn.params;

import cz.msebera.android.httpclient.annotation.NotThreadSafe;
import cz.msebera.android.httpclient.params.HttpAbstractParamBean;
import cz.msebera.android.httpclient.params.HttpParams;

@NotThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class ConnManagerParamBean extends HttpAbstractParamBean {
    public ConnManagerParamBean(HttpParams httpParams) {
        super(httpParams);
    }

    public void setTimeout(long j) {
        this.params.setLongParameter("http.conn-manager.timeout", j);
    }

    public void setMaxTotalConnections(int i) {
        this.params.setIntParameter("http.conn-manager.max-total", i);
    }

    public void setConnectionsPerRoute(ConnPerRouteBean connPerRouteBean) {
        this.params.setParameter("http.conn-manager.max-per-route", connPerRouteBean);
    }
}
