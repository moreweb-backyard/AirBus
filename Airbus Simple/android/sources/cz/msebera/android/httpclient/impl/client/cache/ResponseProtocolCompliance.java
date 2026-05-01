package cz.msebera.android.httpclient.impl.client.cache;

import com.litesuits.http.data.Consts;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpRequestWrapper;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import cz.msebera.android.httpclient.message.BasicHeader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Immutable
/* loaded from: classes.dex */
class ResponseProtocolCompliance {
    private static final String UNEXPECTED_100_CONTINUE = "The incoming request did not contain a 100-continue header, but the response was a Status 100, continue.";
    private static final String UNEXPECTED_PARTIAL_CONTENT = "partial content was returned for a request that did not ask for it";

    ResponseProtocolCompliance() {
    }

    public void ensureProtocolCompliance(HttpRequestWrapper httpRequestWrapper, HttpResponse httpResponse) throws IOException {
        if (backendResponseMustNotHaveBody(httpRequestWrapper, httpResponse)) {
            consumeBody(httpResponse);
            httpResponse.setEntity(null);
        }
        requestDidNotExpect100ContinueButResponseIsOne(httpRequestWrapper, httpResponse);
        transferEncodingIsNotReturnedTo1_0Client(httpRequestWrapper, httpResponse);
        ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(httpRequestWrapper, httpResponse);
        ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(httpRequestWrapper, httpResponse);
        ensure206ContainsDateHeader(httpResponse);
        ensure304DoesNotContainExtraEntityHeaders(httpResponse);
        identityIsNotUsedInContentEncoding(httpResponse);
        warningsWithNonMatchingWarnDatesAreRemoved(httpResponse);
    }

    private void consumeBody(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            IOUtils.consume(entity);
        }
    }

    private void warningsWithNonMatchingWarnDatesAreRemoved(HttpResponse httpResponse) {
        Header[] headers;
        Date date = DateUtils.parseDate(httpResponse.getFirstHeader("Date").getValue());
        if (date == null || (headers = httpResponse.getHeaders("Warning")) == null || headers.length == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int length = headers.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            boolean z2 = z;
            for (WarningValue warningValue : WarningValue.getWarningValues(headers[i])) {
                Date warnDate = warningValue.getWarnDate();
                if (warnDate == null || warnDate.equals(date)) {
                    arrayList.add(new BasicHeader("Warning", warningValue.toString()));
                } else {
                    z2 = true;
                }
            }
            i++;
            z = z2;
        }
        if (z) {
            httpResponse.removeHeaders("Warning");
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                httpResponse.addHeader((Header) it.next());
            }
        }
    }

    private void identityIsNotUsedInContentEncoding(HttpResponse httpResponse) throws ParseException {
        Header[] headers = httpResponse.getHeaders("Content-Encoding");
        if (headers == null || headers.length == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int length = headers.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            Header header = headers[i];
            StringBuilder sb = new StringBuilder();
            boolean z2 = z;
            boolean z3 = true;
            for (HeaderElement headerElement : header.getElements()) {
                if ("identity".equalsIgnoreCase(headerElement.getName())) {
                    z2 = true;
                } else {
                    if (!z3) {
                        sb.append(Consts.SECOND_LEVEL_SPLIT);
                    }
                    sb.append(headerElement.toString());
                    z3 = false;
                }
            }
            String string = sb.toString();
            if (!"".equals(string)) {
                arrayList.add(new BasicHeader("Content-Encoding", string));
            }
            i++;
            z = z2;
        }
        if (z) {
            httpResponse.removeHeaders("Content-Encoding");
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                httpResponse.addHeader((Header) it.next());
            }
        }
    }

    private void ensure206ContainsDateHeader(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Date") == null) {
            httpResponse.addHeader("Date", DateUtils.formatDate(new Date()));
        }
    }

    private void ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getFirstHeader("Range") == null && httpResponse.getStatusLine().getStatusCode() == 206) {
            consumeBody(httpResponse);
            throw new ClientProtocolException(UNEXPECTED_PARTIAL_CONTENT);
        }
    }

    private void ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getRequestLine().getMethod().equalsIgnoreCase("OPTIONS") && httpResponse.getStatusLine().getStatusCode() == 200 && httpResponse.getFirstHeader("Content-Length") == null) {
            httpResponse.addHeader("Content-Length", "0");
        }
    }

    private void ensure304DoesNotContainExtraEntityHeaders(HttpResponse httpResponse) {
        String[] strArr = {"Allow", "Content-Encoding", HttpHeaders.CONTENT_LANGUAGE, "Content-Length", HttpHeaders.CONTENT_MD5, "Content-Range", "Content-Type", "Last-Modified"};
        if (httpResponse.getStatusLine().getStatusCode() == 304) {
            for (String str : strArr) {
                httpResponse.removeHeaders(str);
            }
        }
    }

    private boolean backendResponseMustNotHaveBody(HttpRequest httpRequest, HttpResponse httpResponse) {
        return "HEAD".equals(httpRequest.getRequestLine().getMethod()) || httpResponse.getStatusLine().getStatusCode() == 204 || httpResponse.getStatusLine().getStatusCode() == 205 || httpResponse.getStatusLine().getStatusCode() == 304;
    }

    private void requestDidNotExpect100ContinueButResponseIsOne(HttpRequestWrapper httpRequestWrapper, HttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusLine().getStatusCode() != 100) {
            return;
        }
        HttpRequest original = httpRequestWrapper.getOriginal();
        if ((original instanceof HttpEntityEnclosingRequest) && ((HttpEntityEnclosingRequest) original).expectContinue()) {
            return;
        }
        consumeBody(httpResponse);
        throw new ClientProtocolException(UNEXPECTED_100_CONTINUE);
    }

    private void transferEncodingIsNotReturnedTo1_0Client(HttpRequestWrapper httpRequestWrapper, HttpResponse httpResponse) {
        if (httpRequestWrapper.getOriginal().getProtocolVersion().compareToVersion(HttpVersion.HTTP_1_1) >= 0) {
            return;
        }
        removeResponseTransferEncoding(httpResponse);
    }

    private void removeResponseTransferEncoding(HttpResponse httpResponse) {
        httpResponse.removeHeaders(HttpHeaders.TE);
        httpResponse.removeHeaders("Transfer-Encoding");
    }
}
