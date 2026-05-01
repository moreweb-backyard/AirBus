package com.pzj.ipcdemo.entity;

import java.io.Serializable;

/* loaded from: classes.dex */
public class VStarCamera implements Serializable {
    private byte[] bImage = null;
    private String id;
    private String name;
    private String password;
    private String username;

    public VStarCamera(String str, String str2, String str3, String str4) {
        this.name = str;
        this.id = str2;
        this.username = str3;
        this.password = str4;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public byte[] getbImage() {
        return this.bImage;
    }

    public void setbImage(byte[] bArr) {
        this.bImage = bArr;
    }

    public String toString() {
        return "VStarCamera{name='" + this.name + "', id='" + this.id + "', username='" + this.username + "', password='" + this.password + "'}";
    }
}
