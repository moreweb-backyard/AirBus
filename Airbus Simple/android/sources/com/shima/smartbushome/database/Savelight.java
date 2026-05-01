package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savelight {
    public int _id;
    public int brightvalue;
    public int channel;
    public int deviceID;
    public int lightType;
    public String light_icon;
    public int light_id;
    public String light_statement;
    public int room_id;
    public int subnetID;

    public Savelight() {
    }

    public Savelight(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str, String str2) {
        this.room_id = i;
        this.subnetID = i2;
        this.deviceID = i3;
        this.light_id = i4;
        this.channel = i5;
        this.brightvalue = i6;
        this.lightType = i7;
        this.light_statement = str;
        this.light_icon = str2;
    }
}
