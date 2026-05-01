package com.litesuits.http.response;

import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.AbstractRequest;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface Response<T> {
    String getCharSet();

    String getContentEncoding();

    long getContentLength();

    String getContentType();

    HttpException getException();

    ArrayList<NameValuePair> getHeaders();

    HttpStatus getHttpStatus();

    String getRawString();

    long getReadedLength();

    int getRedirectTimes();

    <R extends AbstractRequest<T>> R getRequest();

    T getResult();

    int getRetryTimes();

    Object getTag();

    long getUseTime();

    boolean isCacheHit();

    boolean isConnectSuccess();

    void printInfo();

    String resToString();

    Response<T> setTag(Object obj);
}
