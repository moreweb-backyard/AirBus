package cz.msebera.android.httpclient.impl.conn;

import com.litesuits.http.data.Consts;
import cz.msebera.android.httpclient.annotation.ThreadSafe;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;
import cz.msebera.android.httpclient.conn.DnsResolver;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.pool.ConnPoolControl;
import cz.msebera.android.httpclient.pool.PoolStats;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class PoolingClientConnectionManager implements ClientConnectionManager, ConnPoolControl<HttpRoute> {
    private final DnsResolver dnsResolver;
    public HttpClientAndroidLog log;
    private final ClientConnectionOperator operator;
    private final HttpConnPool pool;
    private final SchemeRegistry schemeRegistry;

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry) {
        this(schemeRegistry, -1L, TimeUnit.MILLISECONDS);
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, DnsResolver dnsResolver) {
        this(schemeRegistry, -1L, TimeUnit.MILLISECONDS, dnsResolver);
    }

    public PoolingClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long j, TimeUnit timeUnit) {
        this(schemeRegistry, j, timeUnit, new SystemDefaultDnsResolver());
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long j, TimeUnit timeUnit, DnsResolver dnsResolver) {
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(schemeRegistry, "Scheme registry");
        Args.notNull(dnsResolver, "DNS resolver");
        this.schemeRegistry = schemeRegistry;
        this.dnsResolver = dnsResolver;
        this.operator = createConnectionOperator(schemeRegistry);
        this.pool = new HttpConnPool(this.log, this.operator, 2, 20, j, timeUnit);
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
        return new DefaultClientConnectionOperator(schemeRegistry, this.dnsResolver);
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    private String format(HttpRoute httpRoute, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("[route: ");
        sb.append(httpRoute);
        sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        if (obj != null) {
            sb.append("[state: ");
            sb.append(obj);
            sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        }
        return sb.toString();
    }

    private String formatStats(HttpRoute httpRoute) {
        StringBuilder sb = new StringBuilder();
        PoolStats totalStats = this.pool.getTotalStats();
        PoolStats stats = this.pool.getStats(httpRoute);
        sb.append("[total kept alive: ");
        sb.append(totalStats.getAvailable());
        sb.append("; ");
        sb.append("route allocated: ");
        sb.append(stats.getLeased() + stats.getAvailable());
        sb.append(" of ");
        sb.append(stats.getMax());
        sb.append("; ");
        sb.append("total allocated: ");
        sb.append(totalStats.getLeased() + totalStats.getAvailable());
        sb.append(" of ");
        sb.append(totalStats.getMax());
        sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        return sb.toString();
    }

    private String format(HttpPoolEntry httpPoolEntry) {
        StringBuilder sb = new StringBuilder();
        sb.append("[id: ");
        sb.append(httpPoolEntry.getId());
        sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        sb.append("[route: ");
        sb.append(httpPoolEntry.getRoute());
        sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        Object state = httpPoolEntry.getState();
        if (state != null) {
            sb.append("[state: ");
            sb.append(state);
            sb.append(Consts.ARRAY_ECLOSING_RIGHT);
        }
        return sb.toString();
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public ClientConnectionRequest requestConnection(HttpRoute httpRoute, Object obj) {
        Args.notNull(httpRoute, "HTTP route");
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + format(httpRoute, obj) + formatStats(httpRoute));
        }
        final Future<HttpPoolEntry> futureLease = this.pool.lease(httpRoute, obj);
        return new ClientConnectionRequest() { // from class: cz.msebera.android.httpclient.impl.conn.PoolingClientConnectionManager.1
            @Override // cz.msebera.android.httpclient.conn.ClientConnectionRequest
            public void abortRequest() {
                futureLease.cancel(true);
            }

            @Override // cz.msebera.android.httpclient.conn.ClientConnectionRequest
            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException {
                return PoolingClientConnectionManager.this.leaseConnection(futureLease, j, timeUnit);
            }
        };
    }

    ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, ConnectionPoolTimeoutException, TimeoutException {
        try {
            HttpPoolEntry httpPoolEntry = future.get(j, timeUnit);
            if (httpPoolEntry == null || future.isCancelled()) {
                throw new InterruptedException();
            }
            Asserts.check(httpPoolEntry.getConnection() != null, "Pool entry with no connection");
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection leased: " + format(httpPoolEntry) + formatStats(httpPoolEntry.getRoute()));
            }
            return new ManagedClientConnectionImpl(this, this.operator, httpPoolEntry);
        } catch (ExecutionException e) {
            e = e;
            Throwable cause = e.getCause();
            if (cause != null) {
                e = cause;
            }
            this.log.error("Unexpected exception leasing connection from pool", e);
            throw new InterruptedException();
        } catch (TimeoutException unused) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00ac A[Catch: all -> 0x00e0, TryCatch #0 {, blocks: (B:8:0x0018, B:10:0x001e, B:34:0x009b, B:36:0x00ac, B:37:0x00d3, B:40:0x00d6, B:41:0x00df, B:12:0x0020, B:14:0x0026, B:16:0x002c, B:19:0x0031, B:21:0x0039, B:22:0x0040, B:27:0x004c, B:31:0x005d, B:33:0x0079, B:26:0x004a), top: B:45:0x0018, inners: #1 }] */
    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void releaseConnection(cz.msebera.android.httpclient.conn.ManagedClientConnection r5, long r6, java.util.concurrent.TimeUnit r8) {
        /*
            r4 = this;
            boolean r0 = r5 instanceof cz.msebera.android.httpclient.impl.conn.ManagedClientConnectionImpl
            java.lang.String r1 = "Connection class mismatch, connection not obtained from this manager"
            cz.msebera.android.httpclient.util.Args.check(r0, r1)
            cz.msebera.android.httpclient.impl.conn.ManagedClientConnectionImpl r5 = (cz.msebera.android.httpclient.impl.conn.ManagedClientConnectionImpl) r5
            cz.msebera.android.httpclient.conn.ClientConnectionManager r0 = r5.getManager()
            if (r0 != r4) goto L11
            r0 = 1
            goto L12
        L11:
            r0 = 0
        L12:
            java.lang.String r1 = "Connection not obtained from this manager"
            cz.msebera.android.httpclient.util.Asserts.check(r0, r1)
            monitor-enter(r5)
            cz.msebera.android.httpclient.impl.conn.HttpPoolEntry r0 = r5.detach()     // Catch: java.lang.Throwable -> Le0
            if (r0 != 0) goto L20
            monitor-exit(r5)     // Catch: java.lang.Throwable -> Le0
            return
        L20:
            boolean r1 = r5.isOpen()     // Catch: java.lang.Throwable -> Ld5
            if (r1 == 0) goto L40
            boolean r1 = r5.isMarkedReusable()     // Catch: java.lang.Throwable -> Ld5
            if (r1 != 0) goto L40
            r5.shutdown()     // Catch: java.io.IOException -> L30 java.lang.Throwable -> Ld5
            goto L40
        L30:
            r1 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r2 = r4.log     // Catch: java.lang.Throwable -> Ld5
            boolean r2 = r2.isDebugEnabled()     // Catch: java.lang.Throwable -> Ld5
            if (r2 == 0) goto L40
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r2 = r4.log     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r3 = "I/O exception shutting down released connection"
            r2.debug(r3, r1)     // Catch: java.lang.Throwable -> Ld5
        L40:
            boolean r1 = r5.isMarkedReusable()     // Catch: java.lang.Throwable -> Ld5
            if (r1 == 0) goto L9b
            if (r8 == 0) goto L4a
            r1 = r8
            goto L4c
        L4a:
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch: java.lang.Throwable -> Ld5
        L4c:
            r0.updateExpiry(r6, r1)     // Catch: java.lang.Throwable -> Ld5
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r1 = r4.log     // Catch: java.lang.Throwable -> Ld5
            boolean r1 = r1.isDebugEnabled()     // Catch: java.lang.Throwable -> Ld5
            if (r1 == 0) goto L9b
            r1 = 0
            int r3 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r3 <= 0) goto L77
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld5
            r1.<init>()     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r2 = "for "
            r1.append(r2)     // Catch: java.lang.Throwable -> Ld5
            r1.append(r6)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r6 = " "
            r1.append(r6)     // Catch: java.lang.Throwable -> Ld5
            r1.append(r8)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r6 = r1.toString()     // Catch: java.lang.Throwable -> Ld5
            goto L79
        L77:
            java.lang.String r6 = "indefinitely"
        L79:
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r7 = r4.log     // Catch: java.lang.Throwable -> Ld5
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld5
            r8.<init>()     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r1 = "Connection "
            r8.append(r1)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r1 = r4.format(r0)     // Catch: java.lang.Throwable -> Ld5
            r8.append(r1)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r1 = " can be kept alive "
            r8.append(r1)     // Catch: java.lang.Throwable -> Ld5
            r8.append(r6)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r6 = r8.toString()     // Catch: java.lang.Throwable -> Ld5
            r7.debug(r6)     // Catch: java.lang.Throwable -> Ld5
        L9b:
            cz.msebera.android.httpclient.impl.conn.HttpConnPool r6 = r4.pool     // Catch: java.lang.Throwable -> Le0
            boolean r7 = r5.isMarkedReusable()     // Catch: java.lang.Throwable -> Le0
            r6.release(r0, r7)     // Catch: java.lang.Throwable -> Le0
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r6 = r4.log     // Catch: java.lang.Throwable -> Le0
            boolean r6 = r6.isDebugEnabled()     // Catch: java.lang.Throwable -> Le0
            if (r6 == 0) goto Ld3
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r6 = r4.log     // Catch: java.lang.Throwable -> Le0
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Le0
            r7.<init>()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = "Connection released: "
            r7.append(r8)     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = r4.format(r0)     // Catch: java.lang.Throwable -> Le0
            r7.append(r8)     // Catch: java.lang.Throwable -> Le0
            java.lang.Object r8 = r0.getRoute()     // Catch: java.lang.Throwable -> Le0
            cz.msebera.android.httpclient.conn.routing.HttpRoute r8 = (cz.msebera.android.httpclient.conn.routing.HttpRoute) r8     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = r4.formatStats(r8)     // Catch: java.lang.Throwable -> Le0
            r7.append(r8)     // Catch: java.lang.Throwable -> Le0
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Le0
            r6.debug(r7)     // Catch: java.lang.Throwable -> Le0
        Ld3:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> Le0
            return
        Ld5:
            r6 = move-exception
            cz.msebera.android.httpclient.impl.conn.HttpConnPool r7 = r4.pool     // Catch: java.lang.Throwable -> Le0
            boolean r8 = r5.isMarkedReusable()     // Catch: java.lang.Throwable -> Le0
            r7.release(r0, r8)     // Catch: java.lang.Throwable -> Le0
            throw r6     // Catch: java.lang.Throwable -> Le0
        Le0:
            r6 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> Le0
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.PoolingClientConnectionManager.releaseConnection(cz.msebera.android.httpclient.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void shutdown() {
        this.log.debug("Connection manager is shutting down");
        try {
            this.pool.shutdown();
        } catch (IOException e) {
            this.log.debug("I/O exception shutting down connection manager", e);
        }
        this.log.debug("Connection manager shut down");
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + j + " " + timeUnit);
        }
        this.pool.closeIdle(j, timeUnit);
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public void setMaxTotal(int i) {
        this.pool.setMaxTotal(i);
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public void setDefaultMaxPerRoute(int i) {
        this.pool.setDefaultMaxPerRoute(i);
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public int getMaxPerRoute(HttpRoute httpRoute) {
        return this.pool.getMaxPerRoute(httpRoute);
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public void setMaxPerRoute(HttpRoute httpRoute, int i) {
        this.pool.setMaxPerRoute(httpRoute, i);
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }

    @Override // cz.msebera.android.httpclient.pool.ConnPoolControl
    public PoolStats getStats(HttpRoute httpRoute) {
        return this.pool.getStats(httpRoute);
    }
}
