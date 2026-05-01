package com.shima.smartbushome.assist.AutoUpdate;

/* loaded from: classes.dex */
public interface UpdateProgressListener {
    void error();

    void start();

    void success();

    void update(int i);
}
