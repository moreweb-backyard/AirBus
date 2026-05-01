package com.pzj.ipcdemo.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class PlayBackBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String did;
    private String path;
    private int size;
    private int videotime;

    public String getDid() {
        return this.did;
    }

    public void setDid(String str) {
        this.did = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setVideotime(int i) {
        this.videotime = i;
    }

    public int getVideotime() {
        return this.videotime;
    }

    public int getVideofilesize() {
        return this.size;
    }

    public void setVideofilesize(int i) {
        this.size = i;
    }
}
