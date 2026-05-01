package cz.msebera.android.httpclient.impl;

import cz.msebera.android.httpclient.HttpConnectionMetrics;
import cz.msebera.android.httpclient.annotation.NotThreadSafe;
import cz.msebera.android.httpclient.io.HttpTransportMetrics;
import java.util.HashMap;
import java.util.Map;

@NotThreadSafe
/* loaded from: classes.dex */
public class HttpConnectionMetricsImpl implements HttpConnectionMetrics {
    public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
    public static final String REQUEST_COUNT = "http.request-count";
    public static final String RESPONSE_COUNT = "http.response-count";
    public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
    private final HttpTransportMetrics inTransportMetric;
    private Map<String, Object> metricsCache;
    private final HttpTransportMetrics outTransportMetric;
    private long requestCount = 0;
    private long responseCount = 0;

    public HttpConnectionMetricsImpl(HttpTransportMetrics httpTransportMetrics, HttpTransportMetrics httpTransportMetrics2) {
        this.inTransportMetric = httpTransportMetrics;
        this.outTransportMetric = httpTransportMetrics2;
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public long getReceivedBytesCount() {
        if (this.inTransportMetric != null) {
            return this.inTransportMetric.getBytesTransferred();
        }
        return -1L;
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public long getSentBytesCount() {
        if (this.outTransportMetric != null) {
            return this.outTransportMetric.getBytesTransferred();
        }
        return -1L;
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public long getRequestCount() {
        return this.requestCount;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public long getResponseCount() {
        return this.responseCount;
    }

    public void incrementResponseCount() {
        this.responseCount++;
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public Object getMetric(String str) {
        Object obj = this.metricsCache != null ? this.metricsCache.get(str) : null;
        if (obj != null) {
            return obj;
        }
        if ("http.request-count".equals(str)) {
            return Long.valueOf(this.requestCount);
        }
        if ("http.response-count".equals(str)) {
            return Long.valueOf(this.responseCount);
        }
        if ("http.received-bytes-count".equals(str)) {
            if (this.inTransportMetric != null) {
                return Long.valueOf(this.inTransportMetric.getBytesTransferred());
            }
            return null;
        }
        if (!"http.sent-bytes-count".equals(str)) {
            return obj;
        }
        if (this.outTransportMetric != null) {
            return Long.valueOf(this.outTransportMetric.getBytesTransferred());
        }
        return null;
    }

    public void setMetric(String str, Object obj) {
        if (this.metricsCache == null) {
            this.metricsCache = new HashMap();
        }
        this.metricsCache.put(str, obj);
    }

    @Override // cz.msebera.android.httpclient.HttpConnectionMetrics
    public void reset() {
        if (this.outTransportMetric != null) {
            this.outTransportMetric.reset();
        }
        if (this.inTransportMetric != null) {
            this.inTransportMetric.reset();
        }
        this.requestCount = 0L;
        this.responseCount = 0L;
        this.metricsCache = null;
    }
}
