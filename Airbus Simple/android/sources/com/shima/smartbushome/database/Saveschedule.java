package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Saveschedule {
    public String alarm_time;
    public int last_status;
    public int marco_ID;
    public int repeat_option1;
    public int repeat_option2;
    public String schedule_icon;
    public int schedule_id;
    public String schedule_name;

    public Saveschedule() {
    }

    public Saveschedule(int i, String str, String str2, int i2, String str3, int i3, int i4, int i5) {
        this.schedule_id = i;
        this.schedule_name = str;
        this.schedule_icon = str2;
        this.marco_ID = i2;
        this.alarm_time = str3;
        this.repeat_option1 = i3;
        this.repeat_option2 = i4;
        this.last_status = i5;
    }
}
