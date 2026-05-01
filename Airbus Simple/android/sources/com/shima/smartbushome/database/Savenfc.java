package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savenfc {
    public int action_type;
    public String call_num;
    public int delaytime;
    public int marco_ID;
    public String marco_name;
    public String message;
    public String nfc_content;
    public String nfc_icon;
    public int nfc_id;
    public String nfc_name;
    public int resume1;
    public String resume2;
    public int state;

    public Savenfc() {
    }

    public Savenfc(int i, String str, String str2, String str3, int i2, int i3, int i4, String str4, String str5, String str6, int i5, String str7, int i6) {
        this.nfc_id = i;
        this.nfc_name = str;
        this.nfc_icon = str2;
        this.nfc_content = str3;
        this.state = i2;
        this.action_type = i3;
        this.marco_ID = i4;
        this.marco_name = str4;
        this.call_num = str5;
        this.message = str6;
        this.resume1 = i5;
        this.resume2 = str7;
        this.delaytime = i6;
    }
}
