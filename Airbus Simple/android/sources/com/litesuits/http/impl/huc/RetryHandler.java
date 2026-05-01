package com.litesuits.http.impl.huc;

import android.content.Context;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.NetException;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.network.Network;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import javax.net.ssl.SSLException;

/* loaded from: classes.dex */
public class RetryHandler {
    public static final String TAG = "RetryHandler";
    private int retrySleepTimeMS;
    private HashSet<Class<?>> whitelist = new HashSet<>();
    private HashSet<Class<?>> blacklist = new HashSet<>();

    public RetryHandler(int i) {
        this.retrySleepTimeMS = i;
        this.whitelist.add(SocketException.class);
        this.whitelist.add(SocketTimeoutException.class);
        this.blacklist.add(MalformedURLException.class);
        this.blacklist.add(UnknownHostException.class);
        this.blacklist.add(FileNotFoundException.class);
        this.blacklist.add(SSLException.class);
    }

    public RetryHandler setRetrySleepTimeMS(int i) {
        this.retrySleepTimeMS = i;
        return this;
    }

    public boolean retryRequest(IOException iOException, int i, int i2, Context context) throws HttpNetException, InterruptedException {
        if (i > i2) {
            if (HttpLog.isPrint) {
                HttpLog.w(TAG, "retry count > max retry times..");
            }
            throw new HttpNetException(iOException);
        }
        boolean z = true;
        if (isInList(this.blacklist, iOException)) {
            if (HttpLog.isPrint) {
                HttpLog.w(TAG, "exception in blacklist..");
            }
            z = false;
        } else if (isInList(this.whitelist, iOException) && HttpLog.isPrint) {
            HttpLog.w(TAG, "exception in whitelist..");
        }
        if (z) {
            if (context != null) {
                if (Network.isConnected(context)) {
                    HttpLog.d(TAG, "Network isConnected, retry now");
                } else if (Network.isConnectedOrConnecting(context)) {
                    if (HttpLog.isPrint) {
                        HttpLog.v(TAG, "Network is Connected Or Connecting, wait for retey : " + this.retrySleepTimeMS + " ms");
                    }
                    Thread.sleep(this.retrySleepTimeMS);
                } else {
                    HttpLog.d(TAG, "Without any Network , immediately cancel retry");
                    throw new HttpNetException(NetException.NetworkNotAvilable);
                }
            } else {
                if (HttpLog.isPrint) {
                    HttpLog.v(TAG, "app context is null..");
                    HttpLog.v(TAG, "wait for retry : " + this.retrySleepTimeMS + " ms");
                }
                Thread.sleep(this.retrySleepTimeMS);
            }
        }
        if (HttpLog.isPrint) {
            HttpLog.i(TAG, "retry: " + z + " , retryCount: " + i + " , exception: " + iOException);
        }
        return z;
    }

    protected boolean isInList(HashSet<Class<?>> hashSet, Throwable th) {
        Iterator<Class<?>> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().isInstance(th)) {
                return true;
            }
        }
        return false;
    }
}
