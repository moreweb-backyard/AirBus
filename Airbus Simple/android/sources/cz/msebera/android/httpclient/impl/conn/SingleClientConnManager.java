package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.annotation.GuardedBy;
import cz.msebera.android.httpclient.annotation.ThreadSafe;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ThreadSafe
@Deprecated
/* loaded from: classes.dex */
public class SingleClientConnManager implements ClientConnectionManager {
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final boolean alwaysShutDown;
    protected final ClientConnectionOperator connOperator;

    @GuardedBy("this")
    protected volatile long connectionExpiresTime;
    protected volatile boolean isShutDown;

    @GuardedBy("this")
    protected volatile long lastReleaseTime;
    public HttpClientAndroidLog log;

    @GuardedBy("this")
    protected volatile ConnAdapter managedConn;
    protected final SchemeRegistry schemeRegistry;

    @GuardedBy("this")
    protected volatile PoolEntry uniquePoolEntry;

    @Deprecated
    public SingleClientConnManager(HttpParams httpParams, SchemeRegistry schemeRegistry) {
        this(schemeRegistry);
    }

    public SingleClientConnManager(SchemeRegistry schemeRegistry) {
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(schemeRegistry, "Scheme registry");
        this.schemeRegistry = schemeRegistry;
        this.connOperator = createConnectionOperator(schemeRegistry);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1L;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }

    public SingleClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
        return new DefaultClientConnectionOperator(schemeRegistry);
    }

    protected final void assertStillUp() throws IllegalStateException {
        Asserts.check(!this.isShutDown, "Manager is shut down");
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public final ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object obj) {
        return new ClientConnectionRequest() { // from class: cz.msebera.android.httpclient.impl.conn.SingleClientConnManager.1
            @Override // cz.msebera.android.httpclient.conn.ClientConnectionRequest
            public void abortRequest() {
            }

            @Override // cz.msebera.android.httpclient.conn.ClientConnectionRequest
            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) {
                return SingleClientConnManager.this.getConnection(httpRoute, obj);
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x006c A[Catch: all -> 0x0080, TryCatch #0 {, blocks: (B:6:0x0027, B:10:0x0030, B:12:0x0042, B:14:0x0048, B:21:0x005b, B:27:0x006c, B:28:0x0073, B:29:0x007e, B:24:0x0062), top: B:34:0x0027, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cz.msebera.android.httpclient.conn.ManagedClientConnection getConnection(cz.msebera.android.httpclient.conn.routing.HttpRoute r4, java.lang.Object r5) throws java.lang.IllegalStateException {
        /*
            r3 = this;
            java.lang.String r5 = "Route"
            cz.msebera.android.httpclient.util.Args.notNull(r4, r5)
            r3.assertStillUp()
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r5 = r3.log
            boolean r5 = r5.isDebugEnabled()
            if (r5 == 0) goto L26
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r5 = r3.log
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Get connection for route "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            r5.debug(r0)
        L26:
            monitor-enter(r3)
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$ConnAdapter r5 = r3.managedConn     // Catch: java.lang.Throwable -> L80
            r0 = 0
            r1 = 1
            if (r5 != 0) goto L2f
            r5 = 1
            goto L30
        L2f:
            r5 = 0
        L30:
            java.lang.String r2 = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one."
            cz.msebera.android.httpclient.util.Asserts.check(r5, r2)     // Catch: java.lang.Throwable -> L80
            r3.closeExpiredConnections()     // Catch: java.lang.Throwable -> L80
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r5 = r3.uniquePoolEntry     // Catch: java.lang.Throwable -> L80
            cz.msebera.android.httpclient.conn.OperatedClientConnection r5 = r5.connection     // Catch: java.lang.Throwable -> L80
            boolean r5 = r5.isOpen()     // Catch: java.lang.Throwable -> L80
            if (r5 == 0) goto L57
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r5 = r3.uniquePoolEntry     // Catch: java.lang.Throwable -> L80
            cz.msebera.android.httpclient.conn.routing.RouteTracker r5 = r5.tracker     // Catch: java.lang.Throwable -> L80
            if (r5 == 0) goto L55
            cz.msebera.android.httpclient.conn.routing.HttpRoute r5 = r5.toRoute()     // Catch: java.lang.Throwable -> L80
            boolean r5 = r5.equals(r4)     // Catch: java.lang.Throwable -> L80
            if (r5 != 0) goto L53
            goto L55
        L53:
            r5 = 0
            goto L59
        L55:
            r5 = 1
            goto L59
        L57:
            r5 = 0
            r0 = 1
        L59:
            if (r5 == 0) goto L6a
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r5 = r3.uniquePoolEntry     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L80
            r5.shutdown()     // Catch: java.io.IOException -> L61 java.lang.Throwable -> L80
            goto L69
        L61:
            r5 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r0 = r3.log     // Catch: java.lang.Throwable -> L80
            java.lang.String r2 = "Problem shutting down connection."
            r0.debug(r2, r5)     // Catch: java.lang.Throwable -> L80
        L69:
            r0 = 1
        L6a:
            if (r0 == 0) goto L73
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r5 = new cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry     // Catch: java.lang.Throwable -> L80
            r5.<init>()     // Catch: java.lang.Throwable -> L80
            r3.uniquePoolEntry = r5     // Catch: java.lang.Throwable -> L80
        L73:
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$ConnAdapter r5 = new cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$ConnAdapter     // Catch: java.lang.Throwable -> L80
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r0 = r3.uniquePoolEntry     // Catch: java.lang.Throwable -> L80
            r5.<init>(r0, r4)     // Catch: java.lang.Throwable -> L80
            r3.managedConn = r5     // Catch: java.lang.Throwable -> L80
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$ConnAdapter r4 = r3.managedConn     // Catch: java.lang.Throwable -> L80
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L80
            return r4
        L80:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L80
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.SingleClientConnManager.getConnection(cz.msebera.android.httpclient.conn.routing.HttpRoute, java.lang.Object):cz.msebera.android.httpclient.conn.ManagedClientConnection");
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void releaseConnection(ManagedClientConnection managedClientConnection, long j, TimeUnit timeUnit) throws IllegalStateException {
        Args.check(managedClientConnection instanceof ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
        assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Releasing connection " + managedClientConnection);
        }
        ConnAdapter connAdapter = (ConnAdapter) managedClientConnection;
        synchronized (connAdapter) {
            if (connAdapter.poolEntry == null) {
                return;
            }
            Asserts.check(connAdapter.getManager() == this, "Connection not obtained from this manager");
            try {
                try {
                    if (connAdapter.isOpen() && (this.alwaysShutDown || !connAdapter.isMarkedReusable())) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Released connection open but not reusable.");
                        }
                        connAdapter.shutdown();
                    }
                    connAdapter.detach();
                    synchronized (this) {
                        this.managedConn = null;
                        this.lastReleaseTime = System.currentTimeMillis();
                        if (j > 0) {
                            this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                        } else {
                            this.connectionExpiresTime = Long.MAX_VALUE;
                        }
                    }
                } catch (IOException e) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Exception shutting down released connection.", e);
                    }
                    connAdapter.detach();
                    synchronized (this) {
                        this.managedConn = null;
                        this.lastReleaseTime = System.currentTimeMillis();
                        if (j > 0) {
                            this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                        } else {
                            this.connectionExpiresTime = Long.MAX_VALUE;
                        }
                    }
                }
            } catch (Throwable th) {
                connAdapter.detach();
                synchronized (this) {
                    this.managedConn = null;
                    this.lastReleaseTime = System.currentTimeMillis();
                    if (j > 0) {
                        this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                    } else {
                        this.connectionExpiresTime = Long.MAX_VALUE;
                    }
                    throw th;
                }
            }
        }
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void closeExpiredConnections() throws IllegalStateException {
        if (System.currentTimeMillis() >= this.connectionExpiresTime) {
            closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0035 A[Catch: all -> 0x0037, DONT_GENERATE, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000d, B:8:0x0017, B:11:0x0027, B:14:0x002e, B:15:0x0035), top: B:20:0x0009, inners: #1 }] */
    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void closeIdleConnections(long r3, java.util.concurrent.TimeUnit r5) throws java.lang.IllegalStateException {
        /*
            r2 = this;
            r2.assertStillUp()
            java.lang.String r0 = "Time unit"
            cz.msebera.android.httpclient.util.Args.notNull(r5, r0)
            monitor-enter(r2)
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$ConnAdapter r0 = r2.managedConn     // Catch: java.lang.Throwable -> L37
            if (r0 != 0) goto L35
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r0 = r2.uniquePoolEntry     // Catch: java.lang.Throwable -> L37
            cz.msebera.android.httpclient.conn.OperatedClientConnection r0 = r0.connection     // Catch: java.lang.Throwable -> L37
            boolean r0 = r0.isOpen()     // Catch: java.lang.Throwable -> L37
            if (r0 == 0) goto L35
            long r0 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L37
            long r3 = r5.toMillis(r3)     // Catch: java.lang.Throwable -> L37
            r5 = 0
            long r0 = r0 - r3
            long r3 = r2.lastReleaseTime     // Catch: java.lang.Throwable -> L37
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 > 0) goto L35
            cz.msebera.android.httpclient.impl.conn.SingleClientConnManager$PoolEntry r3 = r2.uniquePoolEntry     // Catch: java.io.IOException -> L2d java.lang.Throwable -> L37
            r3.close()     // Catch: java.io.IOException -> L2d java.lang.Throwable -> L37
            goto L35
        L2d:
            r3 = move-exception
            cz.msebera.android.httpclient.extras.HttpClientAndroidLog r4 = r2.log     // Catch: java.lang.Throwable -> L37
            java.lang.String r5 = "Problem closing idle connection."
            r4.debug(r5, r3)     // Catch: java.lang.Throwable -> L37
        L35:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L37
            return
        L37:
            r3 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L37
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.SingleClientConnManager.closeIdleConnections(long, java.util.concurrent.TimeUnit):void");
    }

    @Override // cz.msebera.android.httpclient.conn.ClientConnectionManager
    public void shutdown() {
        this.isShutDown = true;
        synchronized (this) {
            try {
                try {
                    if (this.uniquePoolEntry != null) {
                        this.uniquePoolEntry.shutdown();
                    }
                    this.uniquePoolEntry = null;
                } catch (IOException e) {
                    this.log.debug("Problem while shutting down manager.", e);
                    this.uniquePoolEntry = null;
                }
                this.managedConn = null;
            } catch (Throwable th) {
                this.uniquePoolEntry = null;
                this.managedConn = null;
                throw th;
            }
        }
    }

    protected void revokeConnection() {
        ConnAdapter connAdapter = this.managedConn;
        if (connAdapter == null) {
            return;
        }
        connAdapter.detach();
        synchronized (this) {
            try {
                this.uniquePoolEntry.shutdown();
            } catch (IOException e) {
                this.log.debug("Problem while shutting down connection.", e);
            }
        }
    }

    protected class PoolEntry extends AbstractPoolEntry {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, null);
        }

        protected void close() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }

        protected void shutdown() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }

    protected class ConnAdapter extends AbstractPooledConnAdapter {
        protected ConnAdapter(PoolEntry poolEntry, HttpRoute httpRoute) {
            super(SingleClientConnManager.this, poolEntry);
            markReusable();
            poolEntry.route = httpRoute;
        }
    }
}
