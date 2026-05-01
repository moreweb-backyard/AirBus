package com.litesuits.http.request;

import android.net.Uri;
import com.litesuits.http.annotation.HttpBaseUrl;
import com.litesuits.http.annotation.HttpCacheExpire;
import com.litesuits.http.annotation.HttpCacheKey;
import com.litesuits.http.annotation.HttpCacheMode;
import com.litesuits.http.annotation.HttpCharSet;
import com.litesuits.http.annotation.HttpID;
import com.litesuits.http.annotation.HttpMaxRedirect;
import com.litesuits.http.annotation.HttpMaxRetry;
import com.litesuits.http.annotation.HttpMethod;
import com.litesuits.http.annotation.HttpTag;
import com.litesuits.http.annotation.HttpUri;
import com.litesuits.http.data.Consts;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.exception.ClientException;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.listener.GlobalHttpListener;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.parser.DataParser;
import com.litesuits.http.request.content.HttpBody;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.request.param.HttpReplace;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.litesuits.http.request.query.ModelQueryBuilder;
import com.litesuits.http.utils.HexUtil;
import com.litesuits.http.utils.HttpUtil;
import com.litesuits.http.utils.MD5Util;
import com.litesuits.http.utils.UriUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public abstract class AbstractRequest<T> {
    private static final String ENCODE_PATTERN_URL = "^.+\\?(%[0-9a-fA-F]+|[=&A-Za-z0-9_#\\-\\.\\*])*$";
    private static final String TAG = "AbstractRequest";
    private String baseUrl;
    private String cacheDir;
    private long cacheExpireMillis;
    private String cacheKey;
    private CacheMode cacheMode;
    private AtomicBoolean cancel;
    private String charSet;
    private int connectTimeout;
    protected DataParser<T> dataParser;
    private String fullUri;
    private GlobalHttpListener globalHttpListener;
    private Map<String, String> headers;
    private HttpBody httpBody;
    private HttpListener<T> httpListener;
    private long id;
    private int maxRedirectTimes;
    private int maxRetryTimes;
    private HttpMethods method;
    private Map<String, Field> paramFieldMap;
    private Map<String, String> paramMap;
    private HttpParamModel paramModel;
    private ModelQueryBuilder queryBuilder;
    private int socketTimeout;
    private Object tag;
    private String uri;

    public abstract DataParser<T> createDataParser();

    public AbstractRequest(String str) {
        this.maxRetryTimes = -1;
        this.maxRedirectTimes = -1;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
        this.cancel = new AtomicBoolean();
        this.cacheExpireMillis = -1L;
        this.paramFieldMap = null;
        this.uri = str;
    }

    public AbstractRequest(HttpParamModel httpParamModel) {
        this.maxRetryTimes = -1;
        this.maxRedirectTimes = -1;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
        this.cancel = new AtomicBoolean();
        this.cacheExpireMillis = -1L;
        this.paramFieldMap = null;
        setParamModel(httpParamModel);
    }

    public AbstractRequest(HttpParamModel httpParamModel, HttpListener<T> httpListener) {
        this(httpParamModel);
        setHttpListener(httpListener);
    }

    public AbstractRequest(String str, HttpParamModel httpParamModel) {
        this(str);
        setParamModel(httpParamModel);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setDataParser(DataParser<T> dataParser) {
        this.dataParser = dataParser;
        this.dataParser.setRequest(this);
        return this;
    }

    public <D extends DataParser<T>> D getDataParser() {
        if (this.dataParser == null) {
            setDataParser(createDataParser());
        }
        return this.dataParser;
    }

    public long getId() {
        return this.id;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setId(long j) {
        this.id = j;
        return this;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setBaseUrl(String str) {
        this.baseUrl = str;
        return this;
    }

    public String getUri() {
        return this.uri;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setUri(String str) {
        this.uri = str;
        return this;
    }

    public HttpMethods getMethod() {
        return this.method;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setMethod(HttpMethods httpMethods) {
        this.method = httpMethods;
        return this;
    }

    public Object getTag() {
        return this.tag;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setTag(Object obj) {
        this.tag = obj;
        return this;
    }

    public String getCharSet() {
        return this.charSet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCharSet(String str) {
        this.charSet = str;
        return this;
    }

    public int getMaxRetryTimes() {
        return this.maxRetryTimes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setMaxRetryTimes(int i) {
        this.maxRetryTimes = i;
        return this;
    }

    public int getMaxRedirectTimes() {
        return this.maxRedirectTimes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setMaxRedirectTimes(int i) {
        this.maxRedirectTimes = i;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setConnectTimeout(int i) {
        this.connectTimeout = i;
        return this;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setSocketTimeout(int i) {
        this.socketTimeout = i;
        return this;
    }

    public HttpListener<T> getHttpListener() {
        return this.httpListener;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setHttpListener(HttpListener<T> httpListener) {
        this.httpListener = httpListener;
        return this;
    }

    public boolean isCancelled() {
        return this.cancel.get();
    }

    public CacheMode getCacheMode() {
        return this.cacheMode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }

    public String getCacheKey() {
        if (this.cacheKey == null) {
            this.cacheKey = HexUtil.encodeHexStr(MD5Util.md5(getUri()));
            if (HttpLog.isPrint) {
                HttpLog.v(TAG, "generate cache key: " + this.cacheKey);
            }
        }
        return this.cacheKey;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheKey(String str) {
        this.cacheKey = str;
        return this;
    }

    public String getCacheDir() {
        return this.cacheDir;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheDir(String str) {
        this.cacheDir = str;
        return this;
    }

    public long getCacheExpireMillis() {
        return this.cacheExpireMillis;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheExpireMillis(long j) {
        this.cacheExpireMillis = j;
        return this;
    }

    public HttpParamModel getParamModel() {
        return this.paramModel;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setParamModel(HttpParamModel httpParamModel) {
        if (httpParamModel != null) {
            try {
                this.paramModel = httpParamModel;
                readParamFromAnnotations(httpParamModel);
                if (httpParamModel instanceof HttpRichParamModel) {
                    HttpRichParamModel httpRichParamModel = (HttpRichParamModel) httpParamModel;
                    HttpBody httpBody = httpRichParamModel.getHttpBody();
                    HttpListener<T> httpListener = httpRichParamModel.getHttpListener();
                    LinkedHashMap<String, String> headers = httpRichParamModel.getHeaders();
                    ModelQueryBuilder modelQueryBuilder = httpRichParamModel.getModelQueryBuilder();
                    if (httpBody != null) {
                        setHttpBody(httpBody);
                    }
                    if (httpListener != null) {
                        setHttpListener(httpListener);
                    }
                    if (headers != null) {
                        setHeaders(headers);
                    }
                    if (modelQueryBuilder != null) {
                        setQueryBuilder(modelQueryBuilder);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public ModelQueryBuilder getQueryBuilder() {
        return this.queryBuilder;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setQueryBuilder(ModelQueryBuilder modelQueryBuilder) {
        this.queryBuilder = modelQueryBuilder;
        return this;
    }

    public HttpBody getHttpBody() {
        return this.httpBody;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setHttpBody(HttpBody httpBody) {
        if (httpBody != null) {
            httpBody.setRequest(this);
        }
        this.httpBody = httpBody;
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setHeaders(Map<String, String> map) {
        this.headers = map;
        return this;
    }

    public Map<String, String> getParamMap() {
        return this.paramMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setParamMap(Map<String, String> map) {
        this.paramMap = map;
        return this;
    }

    public GlobalHttpListener getGlobalHttpListener() {
        return this.globalHttpListener;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setGlobalHttpListener(GlobalHttpListener globalHttpListener) {
        this.globalHttpListener = globalHttpListener;
        return this;
    }

    public LinkedHashMap<String, String> getBasicParams() throws IllegalAccessException, IllegalArgumentException, UnsupportedEncodingException, InvocationTargetException {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        if (this.paramMap != null) {
            linkedHashMap.putAll(this.paramMap);
        }
        if (this.paramModel == null || ((this.paramModel instanceof HttpRichParamModel) && !((HttpRichParamModel) this.paramModel).isFieldsAttachToUrl())) {
            return linkedHashMap;
        }
        linkedHashMap.putAll(getQueryBuilder().buildPrimaryMap(this.paramModel));
        return linkedHashMap;
    }

    public File getCachedFile() {
        if (this.cacheDir == null) {
            throw new RuntimeException("lite-http cache dir for request is null !");
        }
        return new File(this.cacheDir, getCacheKey());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheMode(CacheMode cacheMode, String str) {
        this.cacheMode = cacheMode;
        this.cacheKey = str;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheExpire(long j, TimeUnit timeUnit) {
        this.cacheExpireMillis = timeUnit.toMillis(j);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setCacheMode(CacheMode cacheMode, long j, TimeUnit timeUnit) {
        this.cacheMode = cacheMode;
        this.cacheExpireMillis = timeUnit.toMillis(j);
        return this;
    }

    public boolean isCancelledOrInterrupted() {
        return this.cancel.get() || Thread.currentThread().isInterrupted();
    }

    public void cancel() {
        this.cancel.set(true);
    }

    public String createFullUri() throws HttpClientException {
        if (this.uri == null || !this.uri.startsWith("http")) {
            if (this.baseUrl == null) {
                throw new HttpClientException(ClientException.UrlIsNull);
            }
            if (!this.baseUrl.startsWith("http")) {
                throw new HttpClientException(ClientException.IllegalScheme);
            }
            this.uri = this.uri == null ? this.baseUrl : this.baseUrl + this.uri;
        }
        try {
            StringBuilder sb = new StringBuilder();
            boolean zContains = this.uri.contains("?");
            if (zContains && !this.uri.matches(ENCODE_PATTERN_URL)) {
                Uri uri = Uri.parse(this.uri);
                Uri.Builder builderBuildUpon = uri.buildUpon();
                builderBuildUpon.query(null);
                for (String str : UriUtil.getQueryParameterNames(uri)) {
                    Iterator<String> it = UriUtil.getQueryParameters(uri, str).iterator();
                    while (it.hasNext()) {
                        builderBuildUpon.appendQueryParameter(str, it.next());
                    }
                }
                if (HttpLog.isPrint) {
                    HttpLog.d(TAG, "param uri origin: " + uri);
                }
                Uri uriBuild = builderBuildUpon.build();
                if (HttpLog.isPrint) {
                    HttpLog.d(TAG, "param uri encode: " + uriBuild);
                }
                sb.append(uriBuild);
            } else {
                sb.append(this.uri);
            }
            if (this.paramMap == null && this.paramModel == null) {
                return sb.toString();
            }
            LinkedHashMap<String, String> basicParams = getBasicParams();
            int size = basicParams.size();
            if (size > 0) {
                if (!zContains) {
                    sb.append("?");
                } else if (this.uri.contains(Consts.EQUALS)) {
                    sb.append("&");
                }
                int i = 0;
                for (Map.Entry<String, String> entry : basicParams.entrySet()) {
                    sb.append(URLEncoder.encode(entry.getKey(), this.charSet));
                    sb.append(Consts.EQUALS);
                    sb.append(URLEncoder.encode(entry.getValue(), this.charSet));
                    i++;
                    if (i != size) {
                        sb.append("&");
                    }
                }
            }
            this.fullUri = sb.toString();
            return this.fullUri;
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addUrlParam(String str, String str2) {
        if (str2 != null) {
            if (this.paramMap == null) {
                this.paramMap = new LinkedHashMap();
            }
            this.paramMap.put(str, str2);
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addUrlParam(List<NameValuePair> list) {
        if (list != null) {
            if (this.paramMap == null) {
                this.paramMap = new LinkedHashMap();
            }
            for (NameValuePair nameValuePair : list) {
                this.paramMap.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addUrlPrifix(String str) {
        setUri(str + this.uri);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addUrlSuffix(String str) {
        setUri(this.uri + str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S setHttpBody(HttpBody httpBody, HttpMethods httpMethods) {
        setMethod(httpMethods);
        setHttpBody(httpBody);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addHeader(List<NameValuePair> list) {
        if (list != null) {
            if (this.headers == null) {
                this.headers = new LinkedHashMap();
            }
            for (NameValuePair nameValuePair : list) {
                this.headers.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addHeader(String str, String str2) {
        if (str2 != null) {
            if (this.headers == null) {
                this.headers = new LinkedHashMap();
            }
            this.headers.put(str, str2);
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <S extends AbstractRequest<T>> S addHeader(Map<String, String> map) {
        if (map != null) {
            if (this.headers == null) {
                this.headers = new LinkedHashMap();
            }
            this.headers.putAll(map);
        }
        return this;
    }

    public boolean isCachedModel() {
        return (this.cacheMode == null || this.cacheMode == CacheMode.NetOnly) ? false : true;
    }

    public boolean needCached() {
        return isCachedModel();
    }

    private void readParamFromAnnotations(HttpParamModel httpParamModel) throws IllegalAccessException {
        Annotation[] annotations = httpParamModel.getClass().getAnnotations();
        if (annotations == null || annotations.length <= 0) {
            return;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof HttpID) {
                setId(((HttpID) annotation).value());
            } else if (annotation instanceof HttpTag) {
                setTag(handleAnnotation(httpParamModel, ((HttpTag) annotation).value()));
            } else if (annotation instanceof HttpBaseUrl) {
                this.baseUrl = handleAnnotation(httpParamModel, ((HttpUri) annotation).value());
            } else if (annotation instanceof HttpUri) {
                this.uri = handleAnnotation(httpParamModel, ((HttpUri) annotation).value());
            } else if (annotation instanceof HttpMethod) {
                this.method = ((HttpMethod) annotation).value();
            } else if (annotation instanceof HttpCacheMode) {
                this.cacheMode = ((HttpCacheMode) annotation).value();
            } else if (annotation instanceof HttpCacheExpire) {
                HttpCacheExpire httpCacheExpire = (HttpCacheExpire) annotation;
                this.cacheExpireMillis = httpCacheExpire.unit().toMillis(httpCacheExpire.value());
            } else if (annotation instanceof HttpCacheKey) {
                this.cacheKey = handleAnnotation(httpParamModel, ((HttpCacheKey) annotation).value());
            } else if (annotation instanceof HttpCharSet) {
                this.charSet = ((HttpCharSet) annotation).value();
            } else if (annotation instanceof HttpMaxRedirect) {
                this.maxRetryTimes = ((HttpMaxRedirect) annotation).value();
            } else if (annotation instanceof HttpMaxRetry) {
                this.maxRetryTimes = ((HttpMaxRetry) annotation).value();
            }
        }
    }

    private String handleAnnotation(HttpParamModel httpParamModel, String str) throws IllegalAccessException, IllegalArgumentException {
        if (str.indexOf(123) >= 0) {
            if (this.paramFieldMap == null) {
                this.paramFieldMap = new HashMap();
                ArrayList<Field> allParamModelFields = HttpUtil.getAllParamModelFields(httpParamModel.getClass());
                if (allParamModelFields != null) {
                    HttpLog.i(TAG, "handleAnnotation fields: " + allParamModelFields.size());
                    for (Field field : allParamModelFields) {
                        HttpReplace httpReplace = (HttpReplace) field.getAnnotation(HttpReplace.class);
                        if (httpReplace != null) {
                            this.paramFieldMap.put(httpReplace.value(), field);
                        }
                    }
                }
            }
            if (!this.paramFieldMap.isEmpty()) {
                for (Map.Entry<String, Field> entry : this.paramFieldMap.entrySet()) {
                    entry.getValue().setAccessible(true);
                    Object obj = entry.getValue().get(httpParamModel);
                    if (obj != null) {
                        str = str.replace(Consts.KV_ECLOSING_LEFT + entry.getKey() + Consts.KV_ECLOSING_RIGHT, obj.toString());
                    }
                }
            }
            HttpLog.i(TAG, "handleAnnotation value: " + str);
        }
        return str;
    }

    public String toString() {
        return reqToString();
    }

    public String reqToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n________________ request-start ________________");
        sb.append("\n full uri         : ");
        sb.append(this.fullUri);
        sb.append("\n id               : ");
        sb.append(this.id);
        sb.append("\n method           : ");
        sb.append(this.method);
        sb.append("\n tag              : ");
        sb.append(this.tag);
        sb.append("\n class            : ");
        sb.append(getClass().getSimpleName());
        sb.append("\n charSet          : ");
        sb.append(this.charSet);
        sb.append("\n maxRetryTimes    : ");
        sb.append(this.maxRetryTimes);
        sb.append("\n maxRedirectTimes : ");
        sb.append(this.maxRedirectTimes);
        sb.append("\n httpListener     : ");
        sb.append(this.httpListener);
        sb.append("\n cancelled        : ");
        sb.append(this.cancel.get());
        sb.append("\n cacheMode        : ");
        sb.append(this.cacheMode);
        sb.append("\n cacheKey         : ");
        sb.append(this.cacheKey);
        sb.append("\n cacheExpireMillis: ");
        sb.append(this.cacheExpireMillis);
        sb.append("\n model            : ");
        sb.append(this.paramModel);
        sb.append("\n queryBuilder     : ");
        sb.append(this.queryBuilder);
        sb.append("\n httpBody         : ");
        sb.append(this.httpBody);
        sb.append("\n dataParser       : ");
        sb.append(getDataParser());
        sb.append("\n header           ");
        if (this.headers == null) {
            sb.append(": null");
        } else {
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                sb.append("\n|    ");
                sb.append(String.format("%-20s", entry.getKey()));
                sb.append(" = ");
                sb.append(entry.getValue());
            }
        }
        sb.append("\n paramMap         ");
        if (this.paramMap == null) {
            sb.append(": null");
        } else {
            for (Map.Entry<String, String> entry2 : this.paramMap.entrySet()) {
                sb.append("\n|    ");
                sb.append(String.format("%-20s", entry2.getKey()));
                sb.append(" = ");
                sb.append(entry2.getValue());
            }
        }
        sb.append("\n________________ request-end ________________");
        return sb.toString();
    }
}
