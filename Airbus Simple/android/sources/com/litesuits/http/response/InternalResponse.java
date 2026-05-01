package com.litesuits.http.response;

import android.util.Log;
import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.StatisticsListener;
import com.litesuits.http.request.AbstractRequest;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class InternalResponse<T> implements Response<T> {
    private static final String TAG = "InternalResponse";
    protected String charSet = "UTF-8";
    protected String contentEncoding;
    protected long contentLength;
    protected String contentType;
    protected HttpException exception;
    protected ArrayList<NameValuePair> headers;
    protected HttpStatus httpStatus;
    protected boolean isCacheHit;
    protected long readedLength;
    protected int redirectTimes;
    protected AbstractRequest<T> request;
    protected int retryTimes;
    protected StatisticsListener statistics;
    protected Object tag;
    protected long useTime;

    public InternalResponse(AbstractRequest<T> abstractRequest) {
        this.request = abstractRequest;
    }

    @Override // com.litesuits.http.response.Response
    public T getResult() {
        return (T) this.request.getDataParser().getData();
    }

    @Override // com.litesuits.http.response.Response
    public <R extends AbstractRequest<T>> R getRequest() {
        return this.request;
    }

    public <R extends AbstractRequest<T>> void setRequest(R r) {
        this.request = r;
    }

    @Override // com.litesuits.http.response.Response
    public boolean isCacheHit() {
        return this.isCacheHit;
    }

    @Override // com.litesuits.http.response.Response
    public String getRawString() {
        return this.request.getDataParser().getRawString();
    }

    @Override // com.litesuits.http.response.Response
    public Response<T> setTag(Object obj) {
        this.tag = obj;
        return this;
    }

    @Override // com.litesuits.http.response.Response
    public Object getTag() {
        return this.tag;
    }

    public void setCacheHit(boolean z) {
        this.isCacheHit = z;
    }

    @Override // com.litesuits.http.response.Response
    public HttpException getException() {
        return this.exception;
    }

    public void setException(HttpException httpException) {
        this.exception = httpException;
    }

    @Override // com.litesuits.http.response.Response
    public String getCharSet() {
        return this.charSet;
    }

    public void setCharSet(String str) {
        if (str != null) {
            this.charSet = str;
        }
    }

    @Override // com.litesuits.http.response.Response
    public ArrayList<NameValuePair> getHeaders() {
        return this.headers;
    }

    public void setHeaders(ArrayList<NameValuePair> arrayList) {
        this.headers = arrayList;
    }

    @Override // com.litesuits.http.response.Response
    public long getContentLength() {
        return this.contentLength;
    }

    public long setContentLength(long j) {
        this.contentLength = j;
        return this.contentLength;
    }

    @Override // com.litesuits.http.response.Response
    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public InternalResponse setContentEncoding(String str) {
        this.contentEncoding = str;
        return this;
    }

    @Override // com.litesuits.http.response.Response
    public String getContentType() {
        return this.contentType;
    }

    public InternalResponse setContentType(String str) {
        this.contentType = str;
        return this;
    }

    @Override // com.litesuits.http.response.Response
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public StatisticsListener getStatistics() {
        return this.statistics;
    }

    public void setStatistics(StatisticsListener statisticsListener) {
        this.statistics = statisticsListener;
    }

    @Override // com.litesuits.http.response.Response
    public int getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(int i) {
        this.retryTimes = i;
    }

    @Override // com.litesuits.http.response.Response
    public int getRedirectTimes() {
        return this.redirectTimes;
    }

    public void setRedirectTimes(int i) {
        this.redirectTimes = i;
    }

    @Override // com.litesuits.http.response.Response
    public long getReadedLength() {
        return this.readedLength;
    }

    public void setReadedLength(long j) {
        this.readedLength = j;
    }

    @Override // com.litesuits.http.response.Response
    public long getUseTime() {
        return this.useTime;
    }

    public void setUseTime(long j) {
        this.useTime = j;
    }

    @Override // com.litesuits.http.response.Response
    public boolean isConnectSuccess() {
        return this.httpStatus != null && this.httpStatus.isSuccess();
    }

    public boolean isResultOk() {
        return getResult() != null;
    }

    public String toString() {
        return resToString();
    }

    @Override // com.litesuits.http.response.Response
    public String resToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("^_^\n");
        sb.append("____________________________ lite http response info start ____________________________");
        sb.append("\n url            : ");
        sb.append(this.request.getUri());
        sb.append("\n status         : ");
        sb.append(this.httpStatus);
        sb.append("\n charSet        : ");
        sb.append(this.charSet);
        sb.append("\n useTime        : ");
        sb.append(this.useTime);
        sb.append("\n retryTimes     : ");
        sb.append(this.retryTimes);
        sb.append("\n redirectTimes  : ");
        sb.append(this.redirectTimes);
        sb.append("\n readedLength   : ");
        sb.append(this.readedLength);
        sb.append("\n contentLength  : ");
        sb.append(this.contentLength);
        sb.append("\n contentEncoding: ");
        sb.append(this.contentEncoding);
        sb.append("\n contentType    : ");
        sb.append(this.contentType);
        sb.append("\n statistics     : ");
        sb.append(this.statistics);
        sb.append("\n tag            : ");
        sb.append(this.tag);
        sb.append("\n header         ");
        if (this.headers == null) {
            sb.append(": null");
        } else {
            Iterator<NameValuePair> it = this.headers.iterator();
            while (it.hasNext()) {
                NameValuePair next = it.next();
                sb.append("\n|    ");
                sb.append(next);
            }
        }
        sb.append("\n ");
        sb.append(this.request);
        sb.append("\n exception      : ");
        sb.append(this.exception);
        sb.append("\n.");
        sb.append("\n _________________ data-start _________________");
        sb.append("\n ");
        sb.append(getResult());
        sb.append("\n _________________ data-over _________________");
        sb.append("\n.");
        sb.append("\n model raw string     : ");
        sb.append(getRawString());
        sb.append("\n____________________________ lite http response info end ____________________________");
        return sb.toString();
    }

    @Override // com.litesuits.http.response.Response
    public void printInfo() {
        Log.i(TAG, resToString());
    }
}
