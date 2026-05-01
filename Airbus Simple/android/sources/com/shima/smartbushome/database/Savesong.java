package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class Savesong {
    public String album_name;
    public int album_num;
    public int like;
    public int music_id;
    public int room_id;
    public String song_name;
    public int song_num;

    public Savesong() {
    }

    public Savesong(int i, int i2, String str, int i3, String str2, int i4, int i5) {
        this.room_id = i;
        this.album_num = i2;
        this.album_name = str;
        this.song_num = i3;
        this.song_name = str2;
        this.like = i4;
        this.music_id = i5;
    }
}
