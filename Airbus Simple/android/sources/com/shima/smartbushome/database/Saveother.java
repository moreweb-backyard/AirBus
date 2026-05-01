package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Saveother {
    public int _id;
    public int channel_1;
    public int channel_2;
    public int deviceID;
    public String other_icon;
    public int other_id;
    public String other_statement;
    public int other_type;
    public int room_id;
    public int subnetID;

    public Saveother() {
    }

    public Saveother(int i, int i2, int i3, int i4, int i5, int i6, String str, String str2, int i7) {
        this.room_id = i;
        this.subnetID = i2;
        this.deviceID = i3;
        this.other_id = i4;
        this.channel_1 = i5;
        this.channel_2 = i6;
        this.other_statement = str;
        this.other_icon = str2;
        this.other_type = i7;
    }
}
