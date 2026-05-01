package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class seclogdata {
    public String Address;
    public String Channel;
    public String Date;
    public String Time;
    public String Type;
    public int lognum;

    public seclogdata() {
    }

    public seclogdata(int i, String str, String str2, String str3, String str4, String str5) {
        this.lognum = i;
        this.Date = str;
        this.Time = str2;
        this.Address = str3;
        this.Channel = str4;
        this.Type = str5;
    }
}
