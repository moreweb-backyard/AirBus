package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savestatus {
    public int channel;
    public int deviceID;
    public String name;
    public String status_icon;
    public int status_id;
    public int subnetID;
    public int type;
    public int unit;

    public Savestatus() {
    }

    public Savestatus(int i, String str, int i2, int i3, int i4, int i5, String str2, int i6) {
        this.status_id = i;
        this.name = str;
        this.subnetID = i2;
        this.deviceID = i3;
        this.type = i4;
        this.channel = i5;
        this.status_icon = str2;
        this.unit = i6;
    }
}
