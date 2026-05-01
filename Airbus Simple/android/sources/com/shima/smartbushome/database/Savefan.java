package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savefan {
    public int _id;
    public int channel;
    public int deviceID;
    public int fan_Type;
    public String fan_icon;
    public int fan_id;
    public String fan_statement;
    public int room_id;
    public int subnetID;

    public Savefan() {
    }

    public Savefan(int i, int i2, int i3, int i4, String str, int i5, int i6, String str2) {
        this.room_id = i;
        this.subnetID = i2;
        this.deviceID = i3;
        this.fan_id = i4;
        this.channel = i5;
        this.fan_Type = i6;
        this.fan_statement = str;
        this.fan_icon = str2;
    }
}
