package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.auth.AuthOption;
import cz.msebera.android.httpclient.auth.AuthProtocolState;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.auth.AuthenticationException;
import cz.msebera.android.httpclient.auth.ContextAwareAuthScheme;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.client.AuthenticationStrategy;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.util.Queue;

/* loaded from: classes.dex */
public class HttpAuthenticator {
    public HttpClientAndroidLog log;

    public HttpAuthenticator(HttpClientAndroidLog httpClientAndroidLog) {
        this.log = httpClientAndroidLog == null ? new HttpClientAndroidLog(getClass()) : httpClientAndroidLog;
    }

    public HttpAuthenticator() {
        this(null);
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, AuthenticationStrategy authenticationStrategy, AuthState authState, HttpContext httpContext) {
        if (authenticationStrategy.isAuthenticationRequested(httpHost, httpResponse, httpContext)) {
            this.log.debug("Authentication required");
            if (authState.getState() != AuthProtocolState.SUCCESS) {
                return true;
            }
            authenticationStrategy.authFailed(httpHost, authState.getAuthScheme(), httpContext);
            return true;
        }
        switch (authState.getState()) {
            case CHALLENGED:
            case HANDSHAKE:
                this.log.debug("Authentication succeeded");
                authState.setState(AuthProtocolState.SUCCESS);
                authenticationStrategy.authSucceeded(httpHost, authState.getAuthScheme(), httpContext);
                return false;
            case SUCCESS:
                return false;
            default:
                authState.setState(AuthProtocolState.UNCHALLENGED);
                return false;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0067 A[Catch: MalformedChallengeException -> 0x00dd, TryCatch #0 {MalformedChallengeException -> 0x00dd, blocks: (B:3:0x0001, B:5:0x0009, B:6:0x0023, B:8:0x002d, B:10:0x0035, B:11:0x0046, B:28:0x00a9, B:30:0x00af, B:32:0x00b5, B:34:0x00bd, B:35:0x00d3, B:14:0x004b, B:16:0x0051, B:19:0x0067, B:21:0x0079, B:23:0x0089, B:25:0x00a0, B:27:0x00a6), top: B:44:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00af A[Catch: MalformedChallengeException -> 0x00dd, TryCatch #0 {MalformedChallengeException -> 0x00dd, blocks: (B:3:0x0001, B:5:0x0009, B:6:0x0023, B:8:0x002d, B:10:0x0035, B:11:0x0046, B:28:0x00a9, B:30:0x00af, B:32:0x00b5, B:34:0x00bd, B:35:0x00d3, B:14:0x004b, B:16:0x0051, B:19:0x0067, B:21:0x0079, B:23:0x0089, B:25:0x00a0, B:27:0x00a6), top: B:44:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean handleAuthChallenge(cz.msebera.android.httpclient.HttpHost r7, cz.msebera.android.httpclient.HttpResponse r8, cz.msebera.android.httpclient.client.AuthenticationStrategy r9, cz.msebera.android.httpclient.auth.AuthState r10, cz.msebera.android.httpclient.protocol.HttpContext r11) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.auth.HttpAuthenticator.handleAuthChallenge(cz.msebera.android.httpclient.HttpHost, cz.msebera.android.httpclient.HttpResponse, cz.msebera.android.httpclient.client.AuthenticationStrategy, cz.msebera.android.httpclient.auth.AuthState, cz.msebera.android.httpclient.protocol.HttpContext):boolean");
    }

    public void generateAuthResponse(HttpRequest httpRequest, AuthState authState, HttpContext httpContext) throws HttpException, IOException {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials credentials = authState.getCredentials();
        int i = AnonymousClass1.$SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[authState.getState().ordinal()];
        if (i != 1) {
            switch (i) {
                case 3:
                    ensureAuthScheme(authScheme);
                    if (authScheme.isConnectionBased()) {
                        return;
                    }
                    break;
                case 4:
                    return;
            }
        } else {
            Queue<AuthOption> authOptions = authState.getAuthOptions();
            if (authOptions != null) {
                while (!authOptions.isEmpty()) {
                    AuthOption authOptionRemove = authOptions.remove();
                    AuthScheme authScheme2 = authOptionRemove.getAuthScheme();
                    Credentials credentials2 = authOptionRemove.getCredentials();
                    authState.update(authScheme2, credentials2);
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Generating response to an authentication challenge using " + authScheme2.getSchemeName() + " scheme");
                    }
                    try {
                        httpRequest.addHeader(doAuth(authScheme2, credentials2, httpRequest, httpContext));
                        return;
                    } catch (AuthenticationException e) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn(authScheme2 + " authentication error: " + e.getMessage());
                        }
                    }
                }
                return;
            }
            ensureAuthScheme(authScheme);
        }
        if (authScheme != null) {
            try {
                httpRequest.addHeader(doAuth(authScheme, credentials, httpRequest, httpContext));
            } catch (AuthenticationException e2) {
                if (this.log.isErrorEnabled()) {
                    this.log.error(authScheme + " authentication error: " + e2.getMessage());
                }
            }
        }
    }

    private void ensureAuthScheme(AuthScheme authScheme) {
        Asserts.notNull(authScheme, "Auth scheme");
    }

    private Header doAuth(AuthScheme authScheme, Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate(credentials, httpRequest, httpContext);
        }
        return authScheme.authenticate(credentials, httpRequest);
    }
}
