package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemediabutton {
    public int _id;
    public int button_num;
    public int ifIRmarco;
    public int media_controltype;
    public int media_id;
    public int media_swno;
    public int media_type;
    public int room_id;

    public Savemediabutton() {
    }

    public Savemediabutton(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.room_id = i;
        this.media_id = i2;
        this.button_num = i3;
        this.media_swno = i4;
        this.media_controltype = i5;
        this.media_type = i6;
        this.ifIRmarco = i7;
    }
}
