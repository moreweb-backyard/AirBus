package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savefloorheat {
    public int _id;
    public int channel;
    public int deviceID;
    public int floorheat_id;
    public String floorheat_remark;
    public int room_id;
    public int subnetID;

    public Savefloorheat() {
    }

    public Savefloorheat(int i, int i2, int i3, int i4, int i5, String str) {
        this.room_id = i;
        this.floorheat_id = i2;
        this.subnetID = i3;
        this.deviceID = i4;
        this.channel = i5;
        this.floorheat_remark = str;
    }
}
