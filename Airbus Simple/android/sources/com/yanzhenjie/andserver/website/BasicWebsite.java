package com.yanzhenjie.andserver.website;

import android.text.TextUtils;
import java.io.File;

/* loaded from: classes.dex */
public abstract class BasicWebsite implements WebSite {
    protected final String INDEX_HTML;

    public BasicWebsite(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            str2 = "index.html";
        } else {
            str2 = str + File.separator + "index.html";
        }
        this.INDEX_HTML = str2;
    }

    public static String trimSlash(String str) {
        while (str.startsWith(File.separator)) {
            str = str.substring(1);
        }
        while (str.endsWith(File.separator)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String getHttpPath(String str) {
        if (str.startsWith(File.separator)) {
            return str;
        }
        return File.separator + str;
    }
}
