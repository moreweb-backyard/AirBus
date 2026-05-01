package cz.msebera.android.httpclient.impl.client.cache;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.cache.HttpCacheEntry;
import cz.msebera.android.httpclient.client.methods.HttpExecutionAware;
import cz.msebera.android.httpclient.client.methods.HttpRequestWrapper;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;

/* loaded from: classes.dex */
public class AsynchronousValidationRequest implements Runnable {
    private final HttpCacheEntry cacheEntry;
    private final CachingExec cachingExec;
    private final int consecutiveFailedAttempts;
    private final HttpClientContext context;
    private final HttpExecutionAware execAware;
    private final String identifier;
    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());
    private final AsynchronousValidator parent;
    private final HttpRequestWrapper request;
    private final HttpRoute route;

    private boolean isNotServerError(int i) {
        return i < 500;
    }

    AsynchronousValidationRequest(AsynchronousValidator asynchronousValidator, CachingExec cachingExec, HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper, HttpClientContext httpClientContext, HttpExecutionAware httpExecutionAware, HttpCacheEntry httpCacheEntry, String str, int i) {
        this.parent = asynchronousValidator;
        this.cachingExec = cachingExec;
        this.route = httpRoute;
        this.request = httpRequestWrapper;
        this.context = httpClientContext;
        this.execAware = httpExecutionAware;
        this.cacheEntry = httpCacheEntry;
        this.identifier = str;
        this.consecutiveFailedAttempts = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (revalidateCacheEntry()) {
                this.parent.jobSuccessful(this.identifier);
            } else {
                this.parent.jobFailed(this.identifier);
            }
        } finally {
            this.parent.markComplete(this.identifier);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean revalidateCacheEntry() {
        /*
            r7 = this;
            r0 = 0
            cz.msebera.android.httpclient.impl.client.cache.CachingExec r1 = r7.cachingExec     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.conn.routing.HttpRoute r2 = r7.route     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.client.methods.HttpRequestWrapper r3 = r7.request     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.client.protocol.HttpClientContext r4 = r7.context     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.client.methods.HttpExecutionAware r5 = r7.execAware     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.client.cache.HttpCacheEntry r6 = r7.cacheEntry     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.client.methods.CloseableHttpResponse r1 = r1.revalidateCacheEntry(r2, r3, r4, r5, r6)     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            cz.msebera.android.httpclient.StatusLine r2 = r1.getStatusLine()     // Catch: java.lang.Throwable -> L2c
            int r2 = r2.getStatusCode()     // Catch: java.lang.Throwable -> L2c
            boolean r2 = r7.isNotServerError(r2)     // Catch: java.lang.Throwable -> L2c
            if (r2 == 0) goto L27
            boolean r2 = r7.isNotStale(r1)     // Catch: java.lang.Throwable -> L2c
            if (r2 == 0) goto L27
            r2 = 1
            goto L28
        L27:
            r2 = 0
        L28:
            r1.close()     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            return r2
        L2c:
            r2 = move-exception
            r1.close()     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
            throw r2     // Catch: java.lang.RuntimeException -> L31 cz.msebera.android.httpclient.HttpException -> L49 java.io.IOException -> L52
        L31:
            r1 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r2 = r7.log
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "RuntimeException thrown during asynchronous revalidation: "
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r2.error(r1)
            return r0
        L49:
            r1 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r2 = r7.log
            java.lang.String r3 = "HTTP protocol exception during asynchronous revalidation"
            r2.error(r3, r1)
            return r0
        L52:
            r1 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r2 = r7.log
            java.lang.String r3 = "Asynchronous revalidation failed due to I/O error"
            r2.debug(r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.client.cache.AsynchronousValidationRequest.revalidateCacheEntry():boolean");
    }

    private boolean isNotStale(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders("Warning");
        if (headers == null) {
            return true;
        }
        for (Header header : headers) {
            String value = header.getValue();
            if (value.startsWith("110") || value.startsWith("111")) {
                return false;
            }
        }
        return true;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getConsecutiveFailedAttempts() {
        return this.consecutiveFailedAttempts;
    }
}
