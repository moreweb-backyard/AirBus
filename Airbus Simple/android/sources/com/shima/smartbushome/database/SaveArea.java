package com.shima.smartbushome.database;

/* loaded from: classes.dex */
public class SaveArea {
    private String area_bg;
    private String area_icon;
    private String area_name;
    private String area_remark;
    private int id;

    public SaveArea() {
    }

    public SaveArea(String str, String str2, String str3, String str4) {
        this.area_name = str;
        this.area_icon = str2;
        this.area_bg = str3;
        this.area_remark = str4;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getArea_name() {
        return this.area_name;
    }

    public void setArea_name(String str) {
        this.area_name = str;
    }

    public String getArea_icon() {
        return this.area_icon;
    }

    public void setArea_icon(String str) {
        this.area_icon = str;
    }

    public String getArea_bg() {
        return this.area_bg;
    }

    public void setArea_bg(String str) {
        this.area_bg = str;
    }

    public String getArea_remark() {
        return this.area_remark;
    }

    public void setArea_remark(String str) {
        this.area_remark = str;
    }
}
