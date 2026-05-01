package cz.msebera.android.httpclient.impl.client.cache;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpMessage;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.client.cache.HeaderConstants;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Immutable
/* loaded from: classes.dex */
class ResponseCachingPolicy {
    private static final String[] AUTH_CACHEABLE_PARAMS = {"s-maxage", HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE, HeaderConstants.PUBLIC};
    private static final Set<Integer> cacheableStatuses = new HashSet(Arrays.asList(200, 203, 300, 301, 410));
    public HttpClientAndroidLog log = new HttpClientAndroidLog(getClass());
    private final long maxObjectSizeBytes;
    private final boolean neverCache1_0ResponsesWithQueryString;
    private final boolean sharedCache;
    private final Set<Integer> uncacheableStatuses;

    private boolean unknownStatusCode(int i) {
        if (i >= 100 && i <= 101) {
            return false;
        }
        if (i >= 200 && i <= 206) {
            return false;
        }
        if (i >= 300 && i <= 307) {
            return false;
        }
        if (i < 400 || i > 417) {
            return i < 500 || i > 505;
        }
        return false;
    }

    public ResponseCachingPolicy(long j, boolean z, boolean z2, boolean z3) {
        this.maxObjectSizeBytes = j;
        this.sharedCache = z;
        this.neverCache1_0ResponsesWithQueryString = z2;
        if (z3) {
            this.uncacheableStatuses = new HashSet(Arrays.asList(206));
        } else {
            this.uncacheableStatuses = new HashSet(Arrays.asList(206, 303));
        }
    }

    public boolean isResponseCacheable(String str, HttpResponse httpResponse) throws ParseException {
        boolean z;
        if (!"GET".equals(str) && !"HEAD".equals(str)) {
            this.log.debug("Response was not cacheable.");
            return false;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (cacheableStatuses.contains(Integer.valueOf(statusCode))) {
            z = true;
        } else {
            if (this.uncacheableStatuses.contains(Integer.valueOf(statusCode)) || unknownStatusCode(statusCode)) {
                return false;
            }
            z = false;
        }
        if ((httpResponse.getFirstHeader("Content-Length") != null && Integer.parseInt(r0.getValue()) > this.maxObjectSizeBytes) || httpResponse.getHeaders("Age").length > 1 || httpResponse.getHeaders("Expires").length > 1) {
            return false;
        }
        Header[] headers = httpResponse.getHeaders("Date");
        if (headers.length != 1 || DateUtils.parseDate(headers[0].getValue()) == null) {
            return false;
        }
        for (Header header : httpResponse.getHeaders("Vary")) {
            for (HeaderElement headerElement : header.getElements()) {
                if ("*".equals(headerElement.getName())) {
                    return false;
                }
            }
        }
        if (isExplicitlyNonCacheable(httpResponse)) {
            return false;
        }
        return z || isExplicitlyCacheable(httpResponse);
    }

    protected boolean isExplicitlyNonCacheable(HttpResponse httpResponse) throws ParseException {
        for (Header header : httpResponse.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : header.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_NO_STORE.equals(headerElement.getName()) || HeaderConstants.CACHE_CONTROL_NO_CACHE.equals(headerElement.getName())) {
                    return true;
                }
                if (this.sharedCache && HeaderConstants.PRIVATE.equals(headerElement.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean hasCacheControlParameterFrom(HttpMessage httpMessage, String[] strArr) throws ParseException {
        for (Header header : httpMessage.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : header.getElements()) {
                for (String str : strArr) {
                    if (str.equalsIgnoreCase(headerElement.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isExplicitlyCacheable(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Expires") != null) {
            return true;
        }
        return hasCacheControlParameterFrom(httpResponse, new String[]{"max-age", "s-maxage", HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE, HeaderConstants.CACHE_CONTROL_PROXY_REVALIDATE, HeaderConstants.PUBLIC});
    }

    public boolean isResponseCacheable(HttpRequest httpRequest, HttpResponse httpResponse) {
        Header[] headers;
        if (requestProtocolGreaterThanAccepted(httpRequest)) {
            this.log.debug("Response was not cacheable.");
            return false;
        }
        if (hasCacheControlParameterFrom(httpRequest, new String[]{HeaderConstants.CACHE_CONTROL_NO_STORE})) {
            return false;
        }
        if (httpRequest.getRequestLine().getUri().contains("?")) {
            if (this.neverCache1_0ResponsesWithQueryString && from1_0Origin(httpResponse)) {
                this.log.debug("Response was not cacheable as it had a query string.");
                return false;
            }
            if (!isExplicitlyCacheable(httpResponse)) {
                this.log.debug("Response was not cacheable as it is missing explicit caching headers.");
                return false;
            }
        }
        if (expiresHeaderLessOrEqualToDateHeaderAndNoCacheControl(httpResponse)) {
            return false;
        }
        if (!this.sharedCache || (headers = httpRequest.getHeaders("Authorization")) == null || headers.length <= 0 || hasCacheControlParameterFrom(httpResponse, AUTH_CACHEABLE_PARAMS)) {
            return isResponseCacheable(httpRequest.getRequestLine().getMethod(), httpResponse);
        }
        return false;
    }

    private boolean expiresHeaderLessOrEqualToDateHeaderAndNoCacheControl(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Cache-Control") != null) {
            return false;
        }
        Header firstHeader = httpResponse.getFirstHeader("Expires");
        Header firstHeader2 = httpResponse.getFirstHeader("Date");
        if (firstHeader == null || firstHeader2 == null) {
            return false;
        }
        Date date = DateUtils.parseDate(firstHeader.getValue());
        Date date2 = DateUtils.parseDate(firstHeader2.getValue());
        if (date == null || date2 == null) {
            return false;
        }
        return date.equals(date2) || date.before(date2);
    }

    private boolean from1_0Origin(HttpResponse httpResponse) throws ParseException {
        Header firstHeader = httpResponse.getFirstHeader("Via");
        if (firstHeader != null) {
            HeaderElement[] elements = firstHeader.getElements();
            if (elements.length > 0) {
                String str = elements[0].toString().split("\\s")[0];
                if (str.contains("/")) {
                    return str.equals("HTTP/1.0");
                }
                return str.equals("1.0");
            }
        }
        return HttpVersion.HTTP_1_0.equals(httpResponse.getProtocolVersion());
    }

    private boolean requestProtocolGreaterThanAccepted(HttpRequest httpRequest) {
        return httpRequest.getProtocolVersion().compareToVersion(HttpVersion.HTTP_1_1) > 0;
    }
}
