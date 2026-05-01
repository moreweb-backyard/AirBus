package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class SaveTips {
    public int _id;
    public String date;
    public String detail;
    public String title;
    public String url;

    public SaveTips() {
    }

    public SaveTips(String str, String str2, String str3, String str4) {
        this.title = str;
        this.date = str2;
        this.detail = str3;
        this.url = str4;
    }
}
