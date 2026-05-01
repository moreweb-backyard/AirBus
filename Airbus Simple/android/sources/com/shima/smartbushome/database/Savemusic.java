package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savemusic {
    public int deviceID;
    public int music_id;
    public String music_remark;
    public int room_id;
    public int subnetID;

    public Savemusic() {
    }

    public Savemusic(int i, int i2, int i3, int i4) {
        this.room_id = i;
        this.music_id = i2;
        this.deviceID = i4;
        this.subnetID = i3;
    }

    public Savemusic(int i, int i2, int i3, int i4, String str) {
        this.room_id = i;
        this.music_id = i2;
        this.deviceID = i4;
        this.subnetID = i3;
        this.music_remark = str;
    }
}
