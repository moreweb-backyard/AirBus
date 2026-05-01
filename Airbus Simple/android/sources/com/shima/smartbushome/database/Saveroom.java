package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Saveroom {
    public int _id;
    public int area_id;
    public int curtain;
    public int fan;
    public int fh;
    public int hvac;
    public int light;
    public int media;
    public int mood;
    public int music;
    public int nio;
    public int other;
    public String room_icon;
    public String room_icon_bg;
    public int room_id;
    public String room_name;

    public Saveroom() {
    }

    public Saveroom(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7, String str2, String str3, int i8, int i9, int i10, int i11) {
        this.room_id = i;
        this.room_name = str;
        this.light = i2;
        this.hvac = i3;
        this.mood = i4;
        this.fan = i5;
        this.curtain = i6;
        this.music = i7;
        this.room_icon = str2;
        this.room_icon_bg = str3;
        this.other = i8;
        this.media = i9;
        this.nio = i10;
        this.fh = i11;
    }

    public Saveroom(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7, String str2, String str3, int i8, int i9, int i10, int i11, int i12) {
        this.room_id = i;
        this.room_name = str;
        this.light = i2;
        this.hvac = i3;
        this.mood = i4;
        this.fan = i5;
        this.curtain = i6;
        this.music = i7;
        this.room_icon = str2;
        this.room_icon_bg = str3;
        this.other = i8;
        this.media = i9;
        this.nio = i10;
        this.fh = i11;
        this.area_id = i12;
    }
}
