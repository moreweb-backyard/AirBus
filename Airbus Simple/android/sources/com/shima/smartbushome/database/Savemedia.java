package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemedia {
    public int _id;
    public int deviceID;
    public String media_icon;
    public int media_id;
    public String media_statement;
    public int room_id;
    public int subnetID;

    public Savemedia() {
    }

    public Savemedia(int i, int i2, int i3, int i4, String str, String str2) {
        this.room_id = i;
        this.subnetID = i2;
        this.deviceID = i3;
        this.media_id = i4;
        this.media_statement = str;
        this.media_icon = str2;
    }
}
