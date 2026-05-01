package com.litesuits.http.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.log.HttpLog;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;

/* loaded from: classes.dex */
public abstract class HttpListener<Data> {
    private static final int M_CANCEL = 4;
    private static final int M_END = 9;
    private static final int M_FAILURE = 3;
    private static final int M_READING = 5;
    private static final int M_REDIRECT = 8;
    private static final int M_RETRY = 7;
    private static final int M_START = 1;
    private static final int M_SUCCESS = 2;
    private static final int M_UPLOADING = 6;
    private static final String TAG = "HttpListener";
    private long delayMillis;
    private HttpListener<Data>.HttpHandler handler;
    private HttpListener<Data> linkedListener;
    private boolean readingNotify;
    private boolean runOnUiThread;
    private boolean uploadingNotify;

    public boolean disableListener() {
        return false;
    }

    public void onCancel(Data data, Response<Data> response) {
    }

    public void onEnd(Response<Data> response) {
    }

    public void onFailure(HttpException httpException, Response<Data> response) {
    }

    public void onLoading(AbstractRequest<Data> abstractRequest, long j, long j2) {
    }

    public void onRedirect(AbstractRequest<Data> abstractRequest, int i, int i2) {
    }

    public void onRetry(AbstractRequest<Data> abstractRequest, int i, int i2) {
    }

    public void onStart(AbstractRequest<Data> abstractRequest) {
    }

    public void onSuccess(Data data, Response<Data> response) {
    }

    public void onUploading(AbstractRequest<Data> abstractRequest, long j, long j2) {
    }

    public HttpListener() {
        this(true);
    }

    public HttpListener(long j) {
        this.runOnUiThread = true;
        this.readingNotify = false;
        this.uploadingNotify = false;
        this.delayMillis = j;
    }

    public HttpListener(boolean z) {
        this.runOnUiThread = true;
        this.readingNotify = false;
        this.uploadingNotify = false;
        setRunOnUiThread(z);
    }

    public HttpListener(boolean z, boolean z2, boolean z3) {
        this(z);
        this.readingNotify = z2;
        this.uploadingNotify = z3;
    }

    public final HttpListener<Data> getLinkedListener() {
        return this.linkedListener;
    }

    public final HttpListener<Data> setLinkedListener(HttpListener<Data> httpListener) {
        if (this.linkedListener != null) {
            HttpListener<Data> linkedListener = this.linkedListener;
            while (httpListener != linkedListener) {
                linkedListener = linkedListener.getLinkedListener();
                if (linkedListener == null) {
                }
            }
            throw new RuntimeException("Circular refrence:  " + httpListener);
        }
        this.linkedListener = httpListener;
        return this;
    }

    public final boolean isRunOnUiThread() {
        return this.runOnUiThread;
    }

    public final HttpListener<Data> setRunOnUiThread(boolean z) {
        this.runOnUiThread = z;
        if (z) {
            this.handler = new HttpHandler(Looper.getMainLooper());
        } else {
            this.handler = null;
        }
        return this;
    }

    public final boolean isReadingNotify() {
        return this.readingNotify;
    }

    public final HttpListener<Data> setReadingNotify(boolean z) {
        this.readingNotify = z;
        return this;
    }

    public final boolean isUploadingNotify() {
        return this.uploadingNotify;
    }

    public final HttpListener<Data> setUploadingNotify(boolean z) {
        this.uploadingNotify = z;
        return this;
    }

    public long getDelayMillis() {
        return this.delayMillis;
    }

    public HttpListener<Data> setDelayMillis(long j) {
        this.delayMillis = j;
        return this;
    }

    private class HttpHandler extends Handler {
        private HttpHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (HttpListener.this.disableListener()) {
            }
            switch (message.what) {
                case 1:
                    HttpListener.this.onStart((AbstractRequest) message.obj);
                    break;
                case 2:
                    Object[] objArr = (Object[]) message.obj;
                    HttpListener.this.onSuccess(objArr[0], (Response) objArr[1]);
                    break;
                case 3:
                    Object[] objArr2 = (Object[]) message.obj;
                    HttpListener.this.onFailure((HttpException) objArr2[0], (Response) objArr2[1]);
                    break;
                case 4:
                    Object[] objArr3 = (Object[]) message.obj;
                    HttpListener.this.onCancel(objArr3[0], (Response) objArr3[1]);
                    break;
                case 5:
                    Object[] objArr4 = (Object[]) message.obj;
                    HttpListener.this.onLoading((AbstractRequest) objArr4[0], ((Long) objArr4[1]).longValue(), ((Long) objArr4[2]).longValue());
                    break;
                case 6:
                    Object[] objArr5 = (Object[]) message.obj;
                    HttpListener.this.onUploading((AbstractRequest) objArr5[0], ((Long) objArr5[1]).longValue(), ((Long) objArr5[2]).longValue());
                    break;
                case 7:
                    Object[] objArr6 = (Object[]) message.obj;
                    HttpListener.this.onRetry((AbstractRequest) objArr6[0], ((Integer) objArr6[1]).intValue(), ((Integer) objArr6[2]).intValue());
                    break;
                case 8:
                    Object[] objArr7 = (Object[]) message.obj;
                    HttpListener.this.onRedirect((AbstractRequest) objArr7[0], ((Integer) objArr7[1]).intValue(), ((Integer) objArr7[2]).intValue());
                    break;
                case 9:
                    HttpListener.this.onEnd((Response) message.obj);
                    break;
            }
        }
    }

    public final void notifyCallStart(AbstractRequest<Data> abstractRequest) {
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(1);
            messageObtainMessage.obj = abstractRequest;
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onStart(abstractRequest);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallStart(abstractRequest);
        }
    }

    public final void notifyCallSuccess(Data data, Response<Data> response) throws InterruptedException {
        delayOrNot();
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(2);
            messageObtainMessage.obj = new Object[]{data, response};
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onSuccess(data, response);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallSuccess(data, response);
        }
    }

    public final void notifyCallFailure(HttpException httpException, Response<Data> response) throws InterruptedException {
        delayOrNot();
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(3);
            messageObtainMessage.obj = new Object[]{httpException, response};
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onFailure(httpException, response);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallFailure(httpException, response);
        }
    }

    public final void notifyCallCancel(Data data, Response<Data> response) throws InterruptedException {
        if (HttpLog.isPrint) {
            HttpLog.w(TAG, "Request be Cancelled!  isCancelled: " + response.getRequest().isCancelled() + "  Thread isInterrupted: " + Thread.currentThread().isInterrupted());
        }
        delayOrNot();
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(4);
            messageObtainMessage.obj = new Object[]{data, response};
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onCancel(data, response);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallCancel(data, response);
        }
    }

    public final void notifyCallLoading(AbstractRequest<Data> abstractRequest, long j, long j2) {
        if (disableListener()) {
            return;
        }
        if (this.readingNotify) {
            if (this.runOnUiThread) {
                Message messageObtainMessage = this.handler.obtainMessage(5);
                messageObtainMessage.obj = new Object[]{abstractRequest, Long.valueOf(j), Long.valueOf(j2)};
                this.handler.sendMessage(messageObtainMessage);
            } else {
                onLoading(abstractRequest, j, j2);
            }
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallLoading(abstractRequest, j, j2);
        }
    }

    public final void notifyCallUploading(AbstractRequest<Data> abstractRequest, long j, long j2) {
        if (disableListener()) {
            return;
        }
        if (this.uploadingNotify) {
            if (this.runOnUiThread) {
                Message messageObtainMessage = this.handler.obtainMessage(6);
                messageObtainMessage.obj = new Object[]{abstractRequest, Long.valueOf(j), Long.valueOf(j2)};
                this.handler.sendMessage(messageObtainMessage);
            } else {
                onUploading(abstractRequest, j, j2);
            }
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallUploading(abstractRequest, j, j2);
        }
    }

    public final void notifyCallRetry(AbstractRequest<Data> abstractRequest, int i, int i2) {
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(7);
            messageObtainMessage.obj = new Object[]{abstractRequest, Integer.valueOf(i), Integer.valueOf(i2)};
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onRetry(abstractRequest, i, i2);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallRetry(abstractRequest, i, i2);
        }
    }

    public final void notifyCallRedirect(AbstractRequest<Data> abstractRequest, int i, int i2) {
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(8);
            messageObtainMessage.obj = new Object[]{abstractRequest, Integer.valueOf(i), Integer.valueOf(i2)};
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onRedirect(abstractRequest, i, i2);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallRedirect(abstractRequest, i, i2);
        }
    }

    public final void notifyCallEnd(Response<Data> response) {
        if (disableListener()) {
            return;
        }
        if (this.runOnUiThread) {
            Message messageObtainMessage = this.handler.obtainMessage(9);
            messageObtainMessage.obj = response;
            this.handler.sendMessage(messageObtainMessage);
        } else {
            onEnd(response);
        }
        if (this.linkedListener != null) {
            this.linkedListener.notifyCallEnd(response);
        }
    }

    private boolean delayOrNot() throws InterruptedException {
        if (this.delayMillis <= 0) {
            return false;
        }
        try {
            Thread.sleep(this.delayMillis);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
