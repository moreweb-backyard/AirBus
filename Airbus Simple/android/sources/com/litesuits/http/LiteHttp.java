package com.litesuits.http;

import android.content.Context;
import android.util.Log;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.data.StatisticsInfo;
import com.litesuits.http.exception.ClientException;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.HttpServerException;
import com.litesuits.http.exception.NetException;
import com.litesuits.http.listener.StatisticsListener;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.network.Network;
import com.litesuits.http.parser.DataParser;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.JsonRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.litesuits.http.response.InternalResponse;
import com.litesuits.http.response.Response;
import com.litesuits.http.utils.HttpUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public class LiteHttp {
    private static final String TAG = "LiteHttp";
    protected HttpConfig config;
    protected final Object lock = new Object();
    protected StatisticsInfo statisticsInfo = new StatisticsInfo();
    protected AtomicLong memCachedSize = new AtomicLong();
    protected ConcurrentHashMap<String, HttpCache> memCache = new ConcurrentHashMap<>();

    public static HttpConfig build(Context context) {
        return new HttpConfig(context);
    }

    protected LiteHttp(HttpConfig httpConfig) {
        initConfig(httpConfig);
    }

    protected void initConfig(HttpConfig httpConfig) {
        this.config = httpConfig;
        Log.d(TAG, httpConfig.toString());
    }

    public final HttpConfig getConfig() {
        return this.config;
    }

    public final Context getAppContext() {
        return this.config.context;
    }

    public final StatisticsInfo getStatisticsInfo() {
        return this.statisticsInfo;
    }

    public <T> void executeAsync(final AbstractRequest<T> abstractRequest) {
        this.config.smartExecutor.execute(new Runnable() { // from class: com.litesuits.http.LiteHttp.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                LiteHttp.this.execute(abstractRequest);
            }
        });
    }

    public <T> Response<T> execute(HttpRichParamModel<T> httpRichParamModel) {
        return execute(httpRichParamModel.buildRequest());
    }

    public <T> JsonRequest<T> executeAsync(HttpRichParamModel<T> httpRichParamModel) {
        JsonRequest<T> jsonRequestBuildRequest = httpRichParamModel.buildRequest();
        executeAsync(jsonRequestBuildRequest);
        return jsonRequestBuildRequest;
    }

    public <T> T perform(AbstractRequest<T> abstractRequest) {
        return execute(abstractRequest).getResult();
    }

    public <T> T perform(HttpRichParamModel<T> httpRichParamModel) {
        return execute(httpRichParamModel.buildRequest()).getResult();
    }

    public <T> FutureTask<T> performAsync(final AbstractRequest<T> abstractRequest) {
        FutureTask<T> futureTask = new FutureTask<>(new Callable<T>() { // from class: com.litesuits.http.LiteHttp.2
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                return LiteHttp.this.execute(abstractRequest).getResult();
            }
        });
        this.config.smartExecutor.execute(futureTask);
        return futureTask;
    }

    public <T> Response<T> executeOrThrow(AbstractRequest<T> abstractRequest) throws Throwable {
        Response<T> responseExecute = execute(abstractRequest);
        HttpException exception = responseExecute.getException();
        if (exception == null) {
            return responseExecute;
        }
        throw exception;
    }

    public <T> T performOrThrow(AbstractRequest<T> abstractRequest) throws HttpException {
        return executeOrThrow(abstractRequest).getResult();
    }

    public String get(String str) {
        return (String) perform(new StringRequest(str).setMethod(HttpMethods.Get));
    }

    public <T> T get(String str, Class<T> cls) {
        return (T) perform(new JsonRequest(str, cls).setMethod(HttpMethods.Get));
    }

    public <T> T get(AbstractRequest<T> abstractRequest) {
        return (T) perform(abstractRequest.setMethod(HttpMethods.Get));
    }

    public <T> T put(AbstractRequest<T> abstractRequest) {
        return (T) perform(abstractRequest.setMethod(HttpMethods.Put));
    }

    public <T> T post(AbstractRequest<T> abstractRequest) {
        return (T) perform(abstractRequest.setMethod(HttpMethods.Post));
    }

    public <T> T delete(AbstractRequest<T> abstractRequest) {
        return (T) perform(abstractRequest.setMethod(HttpMethods.Delete));
    }

    public <T> ArrayList<NameValuePair> head(AbstractRequest<T> abstractRequest) {
        return execute(abstractRequest.setMethod(HttpMethods.Head)).getHeaders();
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x0386 A[Catch: Throwable -> 0x041e, TryCatch #0 {Throwable -> 0x041e, blocks: (B:114:0x0382, B:116:0x0386, B:118:0x03e2, B:120:0x03e8, B:124:0x03fd, B:122:0x03f2, B:123:0x03f6, B:126:0x0402, B:129:0x040a, B:130:0x040e, B:131:0x0416), top: B:136:0x0382 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x03e2 A[Catch: Throwable -> 0x041e, TryCatch #0 {Throwable -> 0x041e, blocks: (B:114:0x0382, B:116:0x0386, B:118:0x03e2, B:120:0x03e8, B:124:0x03fd, B:122:0x03f2, B:123:0x03f6, B:126:0x0402, B:129:0x040a, B:130:0x040e, B:131:0x0416), top: B:136:0x0382 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0402 A[Catch: Throwable -> 0x041e, TryCatch #0 {Throwable -> 0x041e, blocks: (B:114:0x0382, B:116:0x0386, B:118:0x03e2, B:120:0x03e8, B:124:0x03fd, B:122:0x03f2, B:123:0x03f6, B:126:0x0402, B:129:0x040a, B:130:0x040e, B:131:0x0416), top: B:136:0x0382 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T> com.litesuits.http.response.Response<T> execute(com.litesuits.http.request.AbstractRequest<T> r12) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1059
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.litesuits.http.LiteHttp.execute(com.litesuits.http.request.AbstractRequest):com.litesuits.http.response.Response");
    }

    protected <T> InternalResponse<T> handleRequest(AbstractRequest<T> abstractRequest) {
        if (this.config.commonHeaders != null) {
            abstractRequest.addHeader(this.config.commonHeaders);
        }
        if (abstractRequest.getCacheMode() == null) {
            abstractRequest.setCacheMode(this.config.defaultCacheMode);
        }
        if (abstractRequest.getCacheDir() == null) {
            abstractRequest.setCacheDir(this.config.defaultCacheDir);
        }
        if (abstractRequest.getCacheExpireMillis() < 0) {
            abstractRequest.setCacheExpireMillis(this.config.defaultCacheExpireMillis);
        }
        if (abstractRequest.getCharSet() == null) {
            abstractRequest.setCharSet(this.config.defaultCharSet);
        }
        if (abstractRequest.getMethod() == null) {
            abstractRequest.setMethod(this.config.defaultHttpMethod);
        }
        if (abstractRequest.getMaxRedirectTimes() < 0) {
            abstractRequest.setMaxRedirectTimes(this.config.defaultMaxRedirectTimes);
        }
        if (abstractRequest.getMaxRetryTimes() < 0) {
            abstractRequest.setMaxRetryTimes(this.config.defaultMaxRetryTimes);
        }
        if (abstractRequest.getSocketTimeout() < 0) {
            abstractRequest.setSocketTimeout(this.config.socketTimeout);
        }
        if (abstractRequest.getConnectTimeout() < 0) {
            abstractRequest.setConnectTimeout(this.config.connectTimeout);
        }
        if (abstractRequest.getQueryBuilder() == null) {
            abstractRequest.setQueryBuilder(this.config.defaultModelQueryBuilder);
        }
        if (this.config.globalHttpListener != null) {
            abstractRequest.setGlobalHttpListener(this.config.globalHttpListener);
        }
        if (abstractRequest.getBaseUrl() == null) {
            abstractRequest.setBaseUrl(this.config.baseUrl);
        }
        return new InternalResponse<>(abstractRequest);
    }

    protected void tryToDetectNetwork() throws HttpNetException, HttpClientException {
        if (this.config.detectNetwork || this.config.disableNetworkFlags > 0) {
            if (this.config.context == null) {
                throw new HttpClientException(ClientException.ContextNeeded);
            }
            Network.NetType connectedType = null;
            try {
                if (this.config.detectNetwork && (connectedType = Network.getConnectedType(this.config.context)) == Network.NetType.None) {
                    throw new HttpNetException(NetException.NetworkNotAvilable);
                }
                if (this.config.disableNetworkFlags > 0) {
                    if (connectedType == null) {
                        connectedType = Network.getConnectedType(this.config.context);
                    }
                    if (connectedType != null) {
                        if (this.config.isDisableAllNetwork() || this.config.isDisableNetwork(connectedType.value)) {
                            throw new HttpNetException(NetException.NetworkDisabled);
                        }
                        return;
                    }
                    HttpLog.e(TAG, "DisableNetwork but cant get network type !!!");
                }
            } catch (SecurityException e) {
                throw new HttpClientException(e, ClientException.PermissionDenied);
            }
        }
    }

    protected <T> boolean tryToConnectNetwork(AbstractRequest<T> abstractRequest, InternalResponse<T> internalResponse) throws HttpNetException, InterruptedException, HttpClientException, IOException, HttpServerException {
        StatisticsListener statisticsListener;
        if (this.config.doStatistics) {
            statisticsListener = new StatisticsListener(internalResponse, this.statisticsInfo);
            internalResponse.setStatistics(statisticsListener);
        } else {
            statisticsListener = null;
        }
        if (statisticsListener != null) {
            statisticsListener.onStart(abstractRequest);
        }
        try {
            if (!abstractRequest.isCancelledOrInterrupted()) {
                tryToDetectNetwork();
                if (HttpLog.isPrint) {
                    HttpLog.v(TAG, "lite http connect network...  url: " + abstractRequest.getUri() + "  tag: " + abstractRequest.getTag());
                }
                connectWithRetries(abstractRequest, internalResponse);
                tryToKeepCacheInMemory(internalResponse);
                return true;
            }
            if (statisticsListener != null) {
                statisticsListener.onEnd(internalResponse);
            }
            if (abstractRequest.getCacheMode() != CacheMode.NetFirst || internalResponse.isResultOk() || abstractRequest.isCancelledOrInterrupted()) {
                return false;
            }
            tryHitCache(internalResponse);
            return false;
        } finally {
            if (statisticsListener != null) {
                statisticsListener.onEnd(internalResponse);
            }
            if (abstractRequest.getCacheMode() == CacheMode.NetFirst && !internalResponse.isResultOk() && !abstractRequest.isCancelledOrInterrupted()) {
                tryHitCache(internalResponse);
            }
        }
    }

    public <T> void connectWithRetries(AbstractRequest<T> abstractRequest, InternalResponse internalResponse) throws HttpNetException, InterruptedException, HttpClientException, HttpServerException {
        int maxRetryTimes = abstractRequest.getMaxRetryTimes();
        boolean zRetryRequest = true;
        IOException iOException = null;
        int i = 0;
        while (zRetryRequest) {
            try {
            } catch (HttpNetException | HttpServerException e) {
                throw e;
            } catch (IOException e2) {
                iOException = e2;
            } catch (SecurityException e3) {
                throw new HttpClientException(e3, ClientException.PermissionDenied);
            } catch (Exception e4) {
                throw new HttpClientException(e4);
            }
            if (abstractRequest.isCancelledOrInterrupted()) {
                return;
            }
            this.config.httpClient.connect(abstractRequest, internalResponse);
            iOException = null;
            if (iOException == null) {
                zRetryRequest = false;
            } else {
                if (abstractRequest.isCancelledOrInterrupted()) {
                    return;
                }
                i++;
                zRetryRequest = this.config.retryHandler.retryRequest(iOException, i, maxRetryTimes, this.config.getContext());
                if (zRetryRequest) {
                    internalResponse.setRetryTimes(i);
                    if (HttpLog.isPrint) {
                        HttpLog.i(TAG, "LiteHttp retry request: " + abstractRequest.getUri());
                    }
                    if (abstractRequest.getHttpListener() != null) {
                        abstractRequest.getHttpListener().notifyCallRetry(abstractRequest, maxRetryTimes, i);
                    }
                }
            }
        }
        if (iOException != null) {
            throw new HttpNetException(iOException);
        }
    }

    protected <T> boolean tryHitCache(InternalResponse<T> internalResponse) throws IOException {
        HttpCache httpCache;
        AbstractRequest request = internalResponse.getRequest();
        String cacheKey = request.getCacheKey();
        long cacheExpireMillis = request.getCacheExpireMillis();
        if (request.getDataParser().isMemCacheSupport() && (httpCache = this.memCache.get(cacheKey)) != null && (cacheExpireMillis < 0 || cacheExpireMillis > getCurrentTimeMillis() - httpCache.time)) {
            request.getDataParser().readFromMemoryCache(httpCache.data);
            internalResponse.setCacheHit(true);
            if (HttpLog.isPrint) {
                HttpLog.i(TAG, "lite-http mem cache hit!    url:" + request.getUri() + "  tag:" + request.getTag() + "  key:" + cacheKey + "  cache time:" + HttpUtil.formatDate(httpCache.time) + "  expire: " + cacheExpireMillis);
            }
            return true;
        }
        if (request.isCancelledOrInterrupted()) {
            return false;
        }
        File cachedFile = request.getCachedFile();
        if (!cachedFile.exists() || (cacheExpireMillis >= 0 && cacheExpireMillis <= getCurrentTimeMillis() - cachedFile.lastModified())) {
            return false;
        }
        request.getDataParser().readFromDiskCache(cachedFile);
        internalResponse.setCacheHit(true);
        if (HttpLog.isPrint) {
            HttpLog.i(TAG, "lite-http disk cache hit!    url:" + request.getUri() + "  tag:" + request.getTag() + "  key:" + cacheKey + "  cache time:" + HttpUtil.formatDate(cachedFile.lastModified()) + "  expire: " + cacheExpireMillis);
        }
        return true;
    }

    protected <T> boolean tryToKeepCacheInMemory(InternalResponse<T> internalResponse) {
        AbstractRequest request = internalResponse.getRequest();
        if (!request.isCachedModel()) {
            return false;
        }
        DataParser dataParser = request.getDataParser();
        if (HttpLog.isPrint) {
            HttpLog.v(TAG, "lite http try to keep cache.. maximum cache len: " + this.config.maxMemCacheBytesSize + "   now cache len: " + this.memCachedSize.get() + "   wanna put len: " + dataParser.getReadedLength() + "   url: " + request.getUri() + "   tag: " + request.getTag());
        }
        if (!dataParser.isMemCacheSupport()) {
            return false;
        }
        if (this.memCachedSize.get() + dataParser.getReadedLength() > this.config.maxMemCacheBytesSize) {
            clearMemCache();
            if (HttpLog.isPrint) {
                HttpLog.i(TAG, "lite http cache full ______________ and clear all mem cache success");
            }
        }
        if (dataParser.getReadedLength() >= this.config.maxMemCacheBytesSize) {
            return true;
        }
        HttpCache httpCache = new HttpCache();
        httpCache.time = getCurrentTimeMillis();
        httpCache.key = request.getCacheKey();
        httpCache.charSet = internalResponse.getCharSet();
        httpCache.data = (T) dataParser.getData();
        httpCache.length = dataParser.getReadedLength();
        synchronized (this.lock) {
            this.memCache.put(httpCache.key, httpCache);
            this.memCachedSize.addAndGet(httpCache.length);
            if (HttpLog.isPrint) {
                HttpLog.v(TAG, "lite http keep mem cache success,    url: " + request.getUri() + "   tag: " + request.getTag() + "   key: " + httpCache.key + "   len: " + httpCache.length);
            }
        }
        return true;
    }

    public final long cleanCacheForRequest(AbstractRequest abstractRequest) {
        long j;
        if (abstractRequest.getCacheDir() == null) {
            abstractRequest.setCacheDir(this.config.defaultCacheDir);
        }
        synchronized (this.lock) {
            if (this.memCache.get(abstractRequest.getCacheKey()) != null) {
                j = this.memCache.remove(abstractRequest.getCacheKey()).length;
                this.memCachedSize.addAndGet(-j);
            } else {
                j = 0;
            }
        }
        File cachedFile = abstractRequest.getCachedFile();
        if (cachedFile == null) {
            return j;
        }
        long length = cachedFile.length();
        cachedFile.delete();
        return length;
    }

    public final long clearMemCache() {
        long j;
        synchronized (this.lock) {
            j = this.memCachedSize.get();
            this.memCache.clear();
            this.memCachedSize.set(0L);
        }
        return j;
    }

    public final boolean deleteCachedFile(String str) {
        return new File(this.config.defaultCacheDir, str).delete();
    }

    public final long deleteCachedFiles() {
        File[] fileArrListFiles;
        File file = new File(this.config.defaultCacheDir);
        long length = 0;
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
            for (File file2 : fileArrListFiles) {
                length += file2.length();
                file2.delete();
            }
        }
        return length;
    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
