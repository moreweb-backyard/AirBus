package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savehvac {
    public int _id;
    public int deviceID;
    public int hvac_id;
    public String hvac_remark;
    public int room_id;
    public int subnetID;

    public Savehvac() {
    }

    public Savehvac(int i, int i2, int i3, int i4, String str) {
        this.room_id = i;
        this.hvac_id = i2;
        this.subnetID = i3;
        this.deviceID = i4;
        this.hvac_remark = str;
    }
}
