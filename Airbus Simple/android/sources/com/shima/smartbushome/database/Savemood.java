package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemood {
    public int _id;
    public int control_type;
    public int deviceID;
    public int mood_id;
    public int room_id;
    public int subnetID;
    public int value_1;
    public int value_2;
    public int value_3;
    public int value_4;
    public int value_5;
    public int value_6;

    public Savemood() {
    }

    public Savemood(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        this.room_id = i;
        this.mood_id = i2;
        this.subnetID = i3;
        this.deviceID = i4;
        this.control_type = i5;
        this.value_1 = i6;
        this.value_2 = i7;
        this.value_3 = i8;
        this.value_4 = i9;
        this.value_5 = i10;
        this.value_6 = i11;
    }
}
