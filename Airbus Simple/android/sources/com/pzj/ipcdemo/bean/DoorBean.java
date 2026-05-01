package com.pzj.ipcdemo.bean;

import com.litesuits.http.data.Consts;

/* loaded from: classes.dex */
public class DoorBean {
    public String name;
    public String sensoridTag;
    public int status;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getSensoridTag() {
        return this.sensoridTag;
    }

    public void setSensoridTag(String str) {
        this.sensoridTag = str;
    }

    public String toString() {
        return "DoorBean [name=" + this.name + ", status=" + this.status + ", sensoridTag=" + this.sensoridTag + Consts.ARRAY_ECLOSING_RIGHT;
    }
}
