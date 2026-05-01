package com.pzj.ipcdemo.utils;

import android.annotation.SuppressLint;
import com.litesuits.http.data.Consts;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class MyStringUtils {
    @SuppressLint({"NewApi"})
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String getNumbers(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        return matcher.find() ? matcher.group(0) : "";
    }

    public static String spitValue(String str, String str2) {
        for (String str3 : str.split(";")) {
            String strTrim = str3.trim();
            if (strTrim.startsWith("var ")) {
                strTrim = strTrim.substring(4, strTrim.length());
            }
            if (strTrim.startsWith(str2)) {
                return strTrim.substring(strTrim.indexOf(Consts.EQUALS) + 1);
            }
        }
        return "-1";
    }
}
