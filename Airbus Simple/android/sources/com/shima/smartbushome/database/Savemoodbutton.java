package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemoodbutton {
    public int _id;
    public String mood_icon;
    public int mood_id;
    public String mood_name;
    public int room_id;

    public Savemoodbutton() {
    }

    public Savemoodbutton(int i, int i2, String str, String str2) {
        this.room_id = i;
        this.mood_id = i2;
        this.mood_name = str;
        this.mood_icon = str2;
    }
}
