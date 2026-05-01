package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemarco {
    public int _id;
    public int control_type;
    public String device;
    public int deviceID;
    public int marco_id;
    public String room;
    public int room_id;
    public int sentorder;
    public int subnetID;
    public int value1;
    public int value2;
    public int value3;

    public Savemarco() {
    }

    public Savemarco(int i, int i2, String str, String str2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.marco_id = i;
        this.room_id = i2;
        this.room = str;
        this.device = str2;
        this.subnetID = i3;
        this.deviceID = i4;
        this.control_type = i5;
        this.value1 = i6;
        this.value2 = i7;
        this.value3 = i8;
        this.sentorder = i9;
    }
}
