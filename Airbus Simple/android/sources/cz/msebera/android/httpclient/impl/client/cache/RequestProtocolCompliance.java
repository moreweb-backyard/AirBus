package cz.msebera.android.httpclient.impl.client.cache;

import com.litesuits.http.data.Consts;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.cache.HeaderConstants;
import cz.msebera.android.httpclient.client.methods.HttpRequestWrapper;
import cz.msebera.android.httpclient.entity.AbstractHttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicHttpResponse;
import cz.msebera.android.httpclient.message.BasicStatusLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Immutable
/* loaded from: classes.dex */
class RequestProtocolCompliance {
    private static final List<String> disallowedWithNoCache = Arrays.asList(HeaderConstants.CACHE_CONTROL_MIN_FRESH, HeaderConstants.CACHE_CONTROL_MAX_STALE, "max-age");
    private final boolean weakETagOnPutDeleteAllowed;

    public RequestProtocolCompliance() {
        this.weakETagOnPutDeleteAllowed = false;
    }

    public RequestProtocolCompliance(boolean z) {
        this.weakETagOnPutDeleteAllowed = z;
    }

    public List<RequestProtocolError> requestIsFatallyNonCompliant(HttpRequest httpRequest) throws ParseException {
        RequestProtocolError requestProtocolErrorRequestHasWeekETagForPUTOrDELETEIfMatch;
        ArrayList arrayList = new ArrayList();
        RequestProtocolError requestProtocolErrorRequestHasWeakETagAndRange = requestHasWeakETagAndRange(httpRequest);
        if (requestProtocolErrorRequestHasWeakETagAndRange != null) {
            arrayList.add(requestProtocolErrorRequestHasWeakETagAndRange);
        }
        if (!this.weakETagOnPutDeleteAllowed && (requestProtocolErrorRequestHasWeekETagForPUTOrDELETEIfMatch = requestHasWeekETagForPUTOrDELETEIfMatch(httpRequest)) != null) {
            arrayList.add(requestProtocolErrorRequestHasWeekETagForPUTOrDELETEIfMatch);
        }
        RequestProtocolError requestProtocolErrorRequestContainsNoCacheDirectiveWithFieldName = requestContainsNoCacheDirectiveWithFieldName(httpRequest);
        if (requestProtocolErrorRequestContainsNoCacheDirectiveWithFieldName != null) {
            arrayList.add(requestProtocolErrorRequestContainsNoCacheDirectiveWithFieldName);
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void makeRequestCompliant(HttpRequestWrapper httpRequestWrapper) throws ClientProtocolException {
        if (requestMustNotHaveEntity(httpRequestWrapper)) {
            ((HttpEntityEnclosingRequest) httpRequestWrapper).setEntity(null);
        }
        verifyRequestWithExpectContinueFlagHas100continueHeader(httpRequestWrapper);
        verifyOPTIONSRequestWithBodyHasContentType(httpRequestWrapper);
        decrementOPTIONSMaxForwardsIfGreaterThen0(httpRequestWrapper);
        stripOtherFreshnessDirectivesWithNoCache(httpRequestWrapper);
        if (requestVersionIsTooLow(httpRequestWrapper) || requestMinorVersionIsTooHighMajorVersionsMatch(httpRequestWrapper)) {
            httpRequestWrapper.setProtocolVersion(HttpVersion.HTTP_1_1);
        }
    }

    private void stripOtherFreshnessDirectivesWithNoCache(HttpRequest httpRequest) throws ParseException {
        ArrayList arrayList = new ArrayList();
        Header[] headers = httpRequest.getHeaders("Cache-Control");
        int length = headers.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            boolean z2 = z;
            for (HeaderElement headerElement : headers[i].getElements()) {
                if (!disallowedWithNoCache.contains(headerElement.getName())) {
                    arrayList.add(headerElement);
                }
                if (HeaderConstants.CACHE_CONTROL_NO_CACHE.equals(headerElement.getName())) {
                    z2 = true;
                }
            }
            i++;
            z = z2;
        }
        if (z) {
            httpRequest.removeHeaders("Cache-Control");
            httpRequest.setHeader("Cache-Control", buildHeaderFromElements(arrayList));
        }
    }

    private String buildHeaderFromElements(List<HeaderElement> list) {
        StringBuilder sb = new StringBuilder("");
        boolean z = true;
        for (HeaderElement headerElement : list) {
            if (z) {
                z = false;
            } else {
                sb.append(Consts.SECOND_LEVEL_SPLIT);
            }
            sb.append(headerElement.toString());
        }
        return sb.toString();
    }

    private boolean requestMustNotHaveEntity(HttpRequest httpRequest) {
        return "TRACE".equals(httpRequest.getRequestLine().getMethod()) && (httpRequest instanceof HttpEntityEnclosingRequest);
    }

    private void decrementOPTIONSMaxForwardsIfGreaterThen0(HttpRequest httpRequest) {
        Header firstHeader;
        if ("OPTIONS".equals(httpRequest.getRequestLine().getMethod()) && (firstHeader = httpRequest.getFirstHeader("Max-Forwards")) != null) {
            httpRequest.removeHeaders("Max-Forwards");
            httpRequest.setHeader("Max-Forwards", Integer.toString(Integer.parseInt(firstHeader.getValue()) - 1));
        }
    }

    private void verifyOPTIONSRequestWithBodyHasContentType(HttpRequest httpRequest) {
        if ("OPTIONS".equals(httpRequest.getRequestLine().getMethod()) && (httpRequest instanceof HttpEntityEnclosingRequest)) {
            addContentTypeHeaderIfMissing((HttpEntityEnclosingRequest) httpRequest);
        }
    }

    private void addContentTypeHeaderIfMissing(HttpEntityEnclosingRequest httpEntityEnclosingRequest) {
        if (httpEntityEnclosingRequest.getEntity().getContentType() == null) {
            ((AbstractHttpEntity) httpEntityEnclosingRequest.getEntity()).setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        }
    }

    private void verifyRequestWithExpectContinueFlagHas100continueHeader(HttpRequest httpRequest) throws ParseException {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest httpEntityEnclosingRequest = (HttpEntityEnclosingRequest) httpRequest;
            if (httpEntityEnclosingRequest.expectContinue() && httpEntityEnclosingRequest.getEntity() != null) {
                add100ContinueHeaderIfMissing(httpRequest);
                return;
            } else {
                remove100ContinueHeaderIfExists(httpRequest);
                return;
            }
        }
        remove100ContinueHeaderIfExists(httpRequest);
    }

    private void remove100ContinueHeaderIfExists(HttpRequest httpRequest) throws ParseException {
        Header[] headers = httpRequest.getHeaders("Expect");
        ArrayList arrayList = new ArrayList();
        int length = headers.length;
        ArrayList arrayList2 = arrayList;
        int i = 0;
        boolean z = false;
        while (i < length) {
            Header header = headers[i];
            boolean z2 = z;
            for (HeaderElement headerElement : header.getElements()) {
                if ("100-continue".equalsIgnoreCase(headerElement.getName())) {
                    z2 = true;
                } else {
                    arrayList2.add(headerElement);
                }
            }
            if (z2) {
                httpRequest.removeHeader(header);
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    httpRequest.addHeader(new BasicHeader("Expect", ((HeaderElement) it.next()).getName()));
                }
                return;
            }
            arrayList2 = new ArrayList();
            i++;
            z = z2;
        }
    }

    private void add100ContinueHeaderIfMissing(HttpRequest httpRequest) throws ParseException {
        Header[] headers = httpRequest.getHeaders("Expect");
        int length = headers.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            boolean z2 = z;
            for (HeaderElement headerElement : headers[i].getElements()) {
                if ("100-continue".equalsIgnoreCase(headerElement.getName())) {
                    z2 = true;
                }
            }
            i++;
            z = z2;
        }
        if (z) {
            return;
        }
        httpRequest.addHeader("Expect", "100-continue");
    }

    protected boolean requestMinorVersionIsTooHighMajorVersionsMatch(HttpRequest httpRequest) {
        ProtocolVersion protocolVersion = httpRequest.getProtocolVersion();
        return protocolVersion.getMajor() == HttpVersion.HTTP_1_1.getMajor() && protocolVersion.getMinor() > HttpVersion.HTTP_1_1.getMinor();
    }

    protected boolean requestVersionIsTooLow(HttpRequest httpRequest) {
        return httpRequest.getProtocolVersion().compareToVersion(HttpVersion.HTTP_1_1) < 0;
    }

    public HttpResponse getErrorForRequest(RequestProtocolError requestProtocolError) {
        switch (requestProtocolError) {
            case BODY_BUT_NO_LENGTH_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 411, ""));
            case WEAK_ETAG_AND_RANGE_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Weak eTag not compatible with byte range"));
            case WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Weak eTag not compatible with PUT or DELETE requests"));
            case NO_CACHE_DIRECTIVE_WITH_FIELD_NAME:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "No-Cache directive MUST NOT include a field name"));
            default:
                throw new IllegalStateException("The request was compliant, therefore no error can be generated for it.");
        }
    }

    private RequestProtocolError requestHasWeakETagAndRange(HttpRequest httpRequest) {
        Header firstHeader;
        if ("GET".equals(httpRequest.getRequestLine().getMethod()) && httpRequest.getFirstHeader("Range") != null && (firstHeader = httpRequest.getFirstHeader("If-Range")) != null && firstHeader.getValue().startsWith("W/")) {
            return RequestProtocolError.WEAK_ETAG_AND_RANGE_ERROR;
        }
        return null;
    }

    private RequestProtocolError requestHasWeekETagForPUTOrDELETEIfMatch(HttpRequest httpRequest) {
        String method = httpRequest.getRequestLine().getMethod();
        if (!"PUT".equals(method) && !"DELETE".equals(method)) {
            return null;
        }
        Header firstHeader = httpRequest.getFirstHeader("If-Match");
        if (firstHeader != null) {
            if (firstHeader.getValue().startsWith("W/")) {
                return RequestProtocolError.WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR;
            }
        } else {
            Header firstHeader2 = httpRequest.getFirstHeader("If-None-Match");
            if (firstHeader2 != null && firstHeader2.getValue().startsWith("W/")) {
                return RequestProtocolError.WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR;
            }
        }
        return null;
    }

    private RequestProtocolError requestContainsNoCacheDirectiveWithFieldName(HttpRequest httpRequest) throws ParseException {
        for (Header header : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : header.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_NO_CACHE.equalsIgnoreCase(headerElement.getName()) && headerElement.getValue() != null) {
                    return RequestProtocolError.NO_CACHE_DIRECTIVE_WITH_FIELD_NAME;
                }
            }
        }
        return null;
    }
}
