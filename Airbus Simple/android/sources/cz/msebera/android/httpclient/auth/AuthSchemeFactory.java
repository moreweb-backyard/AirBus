package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.params.HttpParams;

@Deprecated
/* loaded from: classes.dex */
public interface AuthSchemeFactory {
    AuthScheme newInstance(HttpParams httpParams);
}
