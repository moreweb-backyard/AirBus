package com.litesuits.http.data;

/* loaded from: classes.dex */
public class HttpStatus {
    private String chiDes;
    private int code;
    private String des;

    public HttpStatus(int i, String str) {
        this.code = i;
        this.des = str;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return "code: " + this.code + ", " + this.des;
    }

    public boolean isSuccess() {
        return this.code < 300 || this.code == 600;
    }

    public String toString() {
        return getDescription();
    }

    public String getDescriptionInChinese() {
        return this.code + ":" + this.des + " (" + getChinese() + ")";
    }

    private String getChinese() throws IllegalAccessException, IllegalArgumentException {
        Object obj;
        if (this.chiDes != null) {
            return this.chiDes;
        }
        try {
            obj = HttpStatus.class.getDeclaredField("STATUS_" + this.code).get(this);
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        String str = "" + obj;
        this.chiDes = str;
        return str;
    }
}
