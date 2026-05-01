package com.litesuits.http.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;

/* loaded from: classes.dex */
public abstract class GlobalHttpListener {
    private static final int M_CANCEL = 4;
    private static final int M_FAILURE = 3;
    private static final int M_START = 1;
    private static final int M_SUCCESS = 2;
    private static final String TAG = "GlobalHttpListener";
    private HttpHandler handler;
    private boolean runOnUiThread;

    public void onCancel(Object obj, Response<?> response) {
    }

    public abstract void onFailure(HttpException httpException, Response<?> response);

    public void onStart(AbstractRequest<?> abstractRequest) {
    }

    public abstract void onSuccess(Object obj, Response<?> response);

    public GlobalHttpListener() {
        this(true);
    }

    public GlobalHttpListener(boolean z) {
        this.runOnUiThread = true;
        setRunOnUiThread(z);
    }

    public boolean isRunOnUiThread() {
        return this.runOnUiThread;
    }

    public GlobalHttpListener setRunOnUiThread(boolean z) {
        this.runOnUiThread = z;
        if (z) {
            this.handler = new HttpHandler(Looper.getMainLooper());
        } else {
            this.handler = null;
        }
        return this;
    }

    private class HttpHandler extends Handler {
        private HttpHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    GlobalHttpListener.this.onStart((AbstractRequest) message.obj);
                    break;
                case 2:
                    Object[] objArr = (Object[]) message.obj;
                    GlobalHttpListener.this.onSuccess(objArr[0], (Response) objArr[1]);
                    break;
                case 3:
                    Object[] objArr2 = (Object[]) message.obj;
                    GlobalHttpListener.this.onFailure((HttpException) objArr2[0], (Response) objArr2[1]);
                    break;
                case 4:
                    Object[] objArr3 = (Object[]) message.obj;
                    GlobalHttpListener.this.onCancel(objArr3[0], (Response) objArr3[1]);
                    break;
            }
        }
    }

    public final void notifyCallStart(AbstractRequest<?> abstractRequest) {
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(1);
            messageObtainMessage.obj = abstractRequest;
            this.handler.sendMessage(messageObtainMessage);
            return;
        }
        onStart(abstractRequest);
    }

    public final void notifyCallSuccess(Object obj, Response<?> response) {
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(2);
            messageObtainMessage.obj = new Object[]{obj, response};
            this.handler.sendMessage(messageObtainMessage);
            return;
        }
        onSuccess(obj, response);
    }

    public final void notifyCallFailure(HttpException httpException, Response<?> response) {
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(3);
            messageObtainMessage.obj = new Object[]{httpException, response};
            this.handler.sendMessage(messageObtainMessage);
            return;
        }
        onFailure(httpException, response);
    }

    public final void notifyCallCancel(Object obj, Response<?> response) {
        if (HttpLog.isPrint) {
            HttpLog.w(TAG, "Request be Cancelled!  isCancelled: " + response.getRequest().isCancelled() + "  Thread isInterrupted: " + Thread.currentThread().isInterrupted());
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(4);
            messageObtainMessage.obj = new Object[]{obj, response};
            this.handler.sendMessage(messageObtainMessage);
            return;
        }
        onCancel(obj, response);
    }
}
