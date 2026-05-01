package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savecurtain {
    public int _id;
    public int channel_1;
    public int channel_2;
    public String current_state;
    public int curtain_id;
    public String curtain_remark;
    public int curtain_type;
    public int deviceID;
    public int room_id;
    public int subnetID;

    public Savecurtain() {
    }

    public Savecurtain(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str, String str2) {
        this.room_id = i;
        this.subnetID = i2;
        this.deviceID = i3;
        this.curtain_id = i4;
        this.curtain_type = i5;
        this.channel_1 = i6;
        this.channel_2 = i7;
        this.curtain_remark = str;
        this.current_state = str2;
    }
}
