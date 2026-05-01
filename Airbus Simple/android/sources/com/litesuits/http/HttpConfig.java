package com.litesuits.http;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import com.litesuits.http.concurrent.OverloadPolicy;
import com.litesuits.http.concurrent.SchedulePolicy;
import com.litesuits.http.concurrent.SmartExecutor;
import com.litesuits.http.data.Json;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.impl.huc.HttpUrlClient;
import com.litesuits.http.impl.huc.RetryHandler;
import com.litesuits.http.listener.GlobalHttpListener;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.network.Network;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.request.query.JsonQueryBuilder;
import com.litesuits.http.request.query.ModelQueryBuilder;
import com.litesuits.http.utils.HttpUtil;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class HttpConfig {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final int DEFAULT_HTTPS_PORT = 443;
    public static final int DEFAULT_HTTP_PORT = 80;
    public static final int DEFAULT_MAX_REDIRECT_TIMES = 5;
    public static final int DEFAULT_MAX_RETRY_TIMES = 3;
    public static final int DEFAULT_TIMEOUT = 20000;
    public static final int DEFAULT_TRY_WAIT_TIME = 3000;
    public static final int FLAG_NET_DISABLE_NONE = 0;
    protected static final String TAG = "HttpConfig";
    protected String baseUrl;
    protected Map<String, String> commonHeaders;
    protected Context context;
    protected boolean debugged;
    protected CacheMode defaultCacheMode;
    protected boolean detectNetwork;
    protected int disableNetworkFlags;
    protected boolean doStatistics;
    protected GlobalHttpListener globalHttpListener;
    protected HttpClient httpClient;
    protected OverloadPolicy overloadPolicy;
    protected RetryHandler retryHandler;
    protected SchedulePolicy schedulePolicy;
    protected SmartExecutor smartExecutor;
    public static final int FLAG_NET_DISABLE_ALL = Network.NetType.None.value;
    public static final int FLAG_NET_DISABLE_MOBILE = Network.NetType.Mobile.value;
    public static final int FLAG_NET_DISABLE_WIFI = Network.NetType.Wifi.value;
    public static final int FLAG_NET_DISABLE_OTHER = Network.NetType.Other.value;
    protected static final String VERSION = "v3";
    public static String userAgent = String.format("litehttp-%s (android-%s; api-%s; %s; %s)", VERSION, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT), Build.BRAND, Build.MODEL);
    protected int connectTimeout = DEFAULT_TIMEOUT;
    protected int socketTimeout = DEFAULT_TIMEOUT;
    protected int retrySleepMillis = 3000;
    protected int concurrentSize = HttpUtil.getCoresNumbers();
    protected int waitingQueueSize = this.concurrentSize * 20;
    protected long maxMemCacheBytesSize = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED;
    protected String defaultCacheDir = Environment.getExternalStorageDirectory() + "/lite/http-cache";
    protected String defaultCharSet = "UTF-8";
    protected HttpMethods defaultHttpMethod = HttpMethods.Get;
    protected long defaultCacheExpireMillis = -1;
    protected int defaultMaxRetryTimes = 3;
    protected int defaultMaxRedirectTimes = 5;
    protected ModelQueryBuilder defaultModelQueryBuilder = new JsonQueryBuilder();

    protected HttpConfig(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
        initDefaultComponent();
        setDefaultCacheDir(getDefaultCacheDir(context));
    }

    private void initDefaultComponent() {
        this.httpClient = new HttpUrlClient();
        this.smartExecutor = new SmartExecutor(this.concurrentSize, this.waitingQueueSize);
        this.retryHandler = new RetryHandler(this.retrySleepMillis);
    }

    protected String getDefaultCacheDir(Context context) {
        if (context != null) {
            return context.getFilesDir() + "/lite/http-cache";
        }
        return Environment.getExternalStorageDirectory() + "/lite/http-cache";
    }

    public Context getContext() {
        return this.context;
    }

    public HttpConfig setContext(Context context) {
        this.context = context.getApplicationContext();
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public HttpConfig setUserAgent(String str) {
        userAgent = str;
        return this;
    }

    public int getDisableNetworkFlags() {
        return this.disableNetworkFlags;
    }

    public HttpConfig setDisableNetworkFlags(int i) {
        this.disableNetworkFlags = i;
        return this;
    }

    public boolean isDoStatistics() {
        return this.doStatistics;
    }

    public HttpConfig setDoStatistics(boolean z) {
        this.doStatistics = z;
        return this;
    }

    public boolean isDetectNetwork() {
        return this.detectNetwork;
    }

    public HttpConfig setDetectNetwork(boolean z) {
        this.detectNetwork = z;
        return this;
    }

    public int getRetrySleepMillis() {
        return this.retrySleepMillis;
    }

    public HttpConfig setRetrySleepMillis(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("retrySleepMillis can not < 1 ! ");
        }
        this.retrySleepMillis = i;
        this.retryHandler.setRetrySleepTimeMS(i);
        return this;
    }

    public int getConcurrentSize() {
        return this.concurrentSize;
    }

    public HttpConfig setConcurrentSize(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("waitingQueueSize can not < 1 ! ");
        }
        this.concurrentSize = i;
        this.smartExecutor.setCoreSize(i);
        return this;
    }

    public int getWaitingQueueSize() {
        return this.waitingQueueSize;
    }

    public HttpConfig setWaitingQueueSize(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("waitingQueueSize can not < 0 ! ");
        }
        this.waitingQueueSize = i;
        this.smartExecutor.setQueueSize(i);
        return this;
    }

    public SchedulePolicy getSchedulePolicy() {
        return this.schedulePolicy;
    }

    public HttpConfig setSchedulePolicy(SchedulePolicy schedulePolicy) {
        if (schedulePolicy == null) {
            throw new IllegalArgumentException("SchedulePolicy can not be null! ");
        }
        this.schedulePolicy = schedulePolicy;
        this.smartExecutor.setSchedulePolicy(schedulePolicy);
        return this;
    }

    public OverloadPolicy getOverloadPolicy() {
        return this.overloadPolicy;
    }

    public HttpConfig setOverloadPolicy(OverloadPolicy overloadPolicy) {
        if (overloadPolicy == null) {
            throw new IllegalArgumentException("OverloadPolicy can not be null! ");
        }
        this.overloadPolicy = overloadPolicy;
        this.smartExecutor.setOverloadPolicy(overloadPolicy);
        return this;
    }

    public long getMaxMemCacheBytesSize() {
        return this.maxMemCacheBytesSize;
    }

    public HttpConfig setMaxMemCacheBytesSize(long j) {
        this.maxMemCacheBytesSize = j;
        return this;
    }

    public String getDefaultCacheDir() {
        return this.defaultCacheDir;
    }

    public HttpConfig setDefaultCacheDir(String str) {
        this.defaultCacheDir = str;
        File file = new File(str);
        if (!file.exists()) {
            boolean zMkdirs = file.mkdirs();
            HttpLog.i(TAG, file.getAbsolutePath() + "  mkdirs: " + zMkdirs);
        }
        HttpLog.i(TAG, "lite http cache file dir: " + str);
        return this;
    }

    public Map<String, String> getCommonHeaders() {
        return this.commonHeaders;
    }

    public HttpConfig setCommonHeaders(List<NameValuePair> list) {
        if (list != null) {
            this.commonHeaders = new LinkedHashMap();
            for (NameValuePair nameValuePair : list) {
                this.commonHeaders.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        return this;
    }

    public HttpConfig setCommonHeaders(Map<String, String> map) {
        this.commonHeaders = map;
        return this;
    }

    public String getDefaultCharSet() {
        return this.defaultCharSet;
    }

    public HttpConfig setDefaultCharSet(String str) {
        this.defaultCharSet = str;
        return this;
    }

    public HttpMethods getDefaultHttpMethod() {
        return this.defaultHttpMethod;
    }

    public HttpConfig setDefaultHttpMethod(HttpMethods httpMethods) {
        this.defaultHttpMethod = httpMethods;
        return this;
    }

    public CacheMode getDefaultCacheMode() {
        return this.defaultCacheMode;
    }

    public HttpConfig setDefaultCacheMode(CacheMode cacheMode) {
        this.defaultCacheMode = cacheMode;
        return this;
    }

    public long getDefaultCacheExpireMillis() {
        return this.defaultCacheExpireMillis;
    }

    public HttpConfig setDefaultCacheExpireMillis(long j) {
        this.defaultCacheExpireMillis = j;
        return this;
    }

    public int getDefaultMaxRetryTimes() {
        return this.defaultMaxRetryTimes;
    }

    public HttpConfig setDefaultMaxRetryTimes(int i) {
        this.defaultMaxRetryTimes = i;
        return this;
    }

    public int getDefaultMaxRedirectTimes() {
        return this.defaultMaxRedirectTimes;
    }

    public HttpConfig setDefaultMaxRedirectTimes(int i) {
        this.defaultMaxRedirectTimes = i;
        return this;
    }

    public ModelQueryBuilder getDefaultModelQueryBuilder() {
        return this.defaultModelQueryBuilder;
    }

    public HttpConfig setDefaultModelQueryBuilder(ModelQueryBuilder modelQueryBuilder) {
        this.defaultModelQueryBuilder = modelQueryBuilder;
        return this;
    }

    public GlobalHttpListener getGlobalHttpListener() {
        return this.globalHttpListener;
    }

    public HttpConfig setGlobalHttpListener(GlobalHttpListener globalHttpListener) {
        this.globalHttpListener = globalHttpListener;
        return this;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public HttpConfig setBaseUrl(String str) {
        this.baseUrl = str;
        return this;
    }

    public boolean isDebugged() {
        return this.debugged;
    }

    public HttpConfig setDebugged(boolean z) {
        this.debugged = z;
        HttpLog.isPrint = z;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpConfig setConnectTimeout(int i) {
        this.connectTimeout = i;
        return this;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public HttpConfig setSocketTimeout(int i) {
        this.socketTimeout = i;
        return this;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpConfig setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public RetryHandler getRetryHandler() {
        return this.retryHandler;
    }

    public HttpConfig setRetryHandler(RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this;
    }

    public SmartExecutor getSmartExecutor() {
        return this.smartExecutor;
    }

    public boolean isDisableAllNetwork() {
        return (this.disableNetworkFlags & FLAG_NET_DISABLE_ALL) == FLAG_NET_DISABLE_ALL;
    }

    public boolean detectNetworkNeeded() {
        return this.detectNetwork && this.context != null;
    }

    public boolean isDisableNetwork(int i) {
        return (this.disableNetworkFlags & i) == i;
    }

    public HttpConfig setJsonConvertor(Json json) {
        Json.set(json);
        return this;
    }

    private void applyToHttpClient() {
        if (this.httpClient != null) {
            this.httpClient.config(this);
        }
    }

    public LiteHttp create() {
        return new LiteHttp(this);
    }

    public HttpConfig restoreToDefault() {
        Json.setDefault();
        userAgent = String.format("litehttp%s (android-%s; api-%s; %s; %s)", VERSION, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT), Build.BRAND, Build.MODEL);
        this.connectTimeout = DEFAULT_TIMEOUT;
        this.socketTimeout = DEFAULT_TIMEOUT;
        this.disableNetworkFlags = 0;
        this.doStatistics = false;
        this.detectNetwork = false;
        this.retrySleepMillis = 3000;
        this.concurrentSize = HttpUtil.getCoresNumbers();
        this.waitingQueueSize = this.concurrentSize * 20;
        this.schedulePolicy = SchedulePolicy.FirstInFistRun;
        this.overloadPolicy = OverloadPolicy.DiscardOldTaskInQueue;
        this.maxMemCacheBytesSize = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED;
        this.defaultCacheDir = getDefaultCacheDir(this.context);
        this.commonHeaders = null;
        this.defaultCharSet = "UTF-8";
        this.defaultHttpMethod = HttpMethods.Get;
        this.defaultCacheMode = null;
        this.defaultCacheExpireMillis = 0L;
        this.defaultMaxRetryTimes = 3;
        this.defaultMaxRedirectTimes = 5;
        this.defaultModelQueryBuilder = new JsonQueryBuilder();
        this.globalHttpListener = null;
        this.baseUrl = null;
        initDefaultComponent();
        applyToHttpClient();
        return this;
    }

    public String toString() {
        return "HttpConfig{context=" + this.context + ", userAgent='" + userAgent + "', connectTimeout=" + this.connectTimeout + ", socketTimeout=" + this.socketTimeout + ", disableNetworkFlags=" + this.disableNetworkFlags + ", doStatistics=" + this.doStatistics + ", detectNetwork=" + this.detectNetwork + ", retrySleepMillis=" + this.retrySleepMillis + ", concurrentSize=" + this.concurrentSize + ", waitingQueueSize=" + this.waitingQueueSize + ", schedulePolicy=" + this.schedulePolicy + ", overloadPolicy=" + this.overloadPolicy + ", maxMemCacheBytesSize=" + this.maxMemCacheBytesSize + ", defaultCacheDir='" + this.defaultCacheDir + "', commonHeaders=" + this.commonHeaders + ", defaultCharSet='" + this.defaultCharSet + "', defaultHttpMethod=" + this.defaultHttpMethod + ", defaultCacheMode=" + this.defaultCacheMode + ", defaultCacheExpireMillis=" + this.defaultCacheExpireMillis + ", defaultMaxRetryTimes=" + this.defaultMaxRetryTimes + ", defaultMaxRedirectTimes=" + this.defaultMaxRedirectTimes + ", defaultModelQueryBuilder=" + this.defaultModelQueryBuilder + ", globalHttpListener=" + this.globalHttpListener + ", baseUrl='" + this.baseUrl + "', debugged=" + this.debugged + ", smartExecutor=" + this.smartExecutor + ", httpClient=" + this.httpClient + ", retryHandler=" + this.retryHandler + '}';
    }
}
