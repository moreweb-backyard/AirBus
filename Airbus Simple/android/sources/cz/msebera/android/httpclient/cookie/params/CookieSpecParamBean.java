package cz.msebera.android.httpclient.cookie.params;

import cz.msebera.android.httpclient.annotation.NotThreadSafe;
import cz.msebera.android.httpclient.params.HttpAbstractParamBean;
import cz.msebera.android.httpclient.params.HttpParams;
import java.util.Collection;

@NotThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class CookieSpecParamBean extends HttpAbstractParamBean {
    public CookieSpecParamBean(HttpParams httpParams) {
        super(httpParams);
    }

    public void setDatePatterns(Collection<String> collection) {
        this.params.setParameter("http.protocol.cookie-datepatterns", collection);
    }

    public void setSingleHeader(boolean z) {
        this.params.setBooleanParameter("http.protocol.single-cookie-header", z);
    }
}
