package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savesecurity {
    public int _id;
    public int deviceID;
    public int password;
    public int room_id;
    public int security_id;
    public int subnetID;

    public Savesecurity() {
    }

    public Savesecurity(int i, int i2, int i3, int i4, int i5) {
        this.room_id = i;
        this.security_id = i2;
        this.subnetID = i3;
        this.deviceID = i4;
        this.password = i5;
    }
}
