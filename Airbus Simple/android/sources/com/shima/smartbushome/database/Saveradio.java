package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Saveradio {
    public String channel_name;
    public int channel_num;
    public String channel_value;
    public int room_id;

    public Saveradio() {
    }

    public Saveradio(int i, int i2, String str, String str2) {
        this.room_id = i;
        this.channel_num = i2;
        this.channel_value = str2;
        this.channel_name = str;
    }
}
