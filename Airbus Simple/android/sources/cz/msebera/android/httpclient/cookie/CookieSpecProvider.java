package cz.msebera.android.httpclient.cookie;

import cz.msebera.android.httpclient.protocol.HttpContext;

/* loaded from: classes.dex */
public interface CookieSpecProvider {
    CookieSpec create(HttpContext httpContext);
}
