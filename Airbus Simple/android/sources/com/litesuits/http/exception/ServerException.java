package com.litesuits.http.exception;

/* loaded from: classes.dex */
public enum ServerException {
    ServerInnerError("Server Inner Exception", "服务器内部异常"),
    ServerRejectClient("Server Reject Client Exception", "服务器拒绝或无法提供服务"),
    RedirectTooMuch("Server RedirectTooMuch", "重定向次数过多");

    private static final String TAG = ServerException.class.getName();
    public String chiReason;
    public String reason;

    ServerException(String str, String str2) {
        this.reason = str;
        this.chiReason = str2;
    }

    @Override // java.lang.Enum
    public String toString() {
        return TAG + ": " + this.reason + " (" + this.chiReason + ")";
    }
}
