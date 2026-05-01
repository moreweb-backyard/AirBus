package com.litesuits.http.request.content;

import com.litesuits.http.data.Consts;
import com.litesuits.http.data.NameValuePair;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class UrlEncodedFormBody extends StringBody {
    public UrlEncodedFormBody(String str) {
        super(str, "application/x-www-form-urlencoded", "UTF-8");
    }

    public UrlEncodedFormBody(String str, String str2) {
        super(str, "application/x-www-form-urlencoded", str2);
    }

    public UrlEncodedFormBody(List<NameValuePair> list) {
        super(handleListValue(list, "UTF-8"), "application/x-www-form-urlencoded", "UTF-8");
    }

    public UrlEncodedFormBody(List<NameValuePair> list, String str) {
        super(handleListValue(list, str), "application/x-www-form-urlencoded", str);
    }

    public UrlEncodedFormBody(Map<String, String> map) {
        super(handleMapValue(map, "UTF-8"), "application/x-www-form-urlencoded", "UTF-8");
    }

    public UrlEncodedFormBody(Map<String, String> map, String str) {
        super(handleMapValue(map, str), "application/x-www-form-urlencoded", str);
    }

    private static String handleMapValue(Map<String, String> map, String str) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), str));
                sb.append(Consts.EQUALS);
                sb.append(URLEncoder.encode(entry.getValue(), str));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String handleListValue(List<NameValuePair> list, String str) {
        if (list == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (NameValuePair nameValuePair : list) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(nameValuePair.getName(), str));
                sb.append(Consts.EQUALS);
                sb.append(URLEncoder.encode(nameValuePair.getValue(), str));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override // com.litesuits.http.request.content.StringBody
    public String toString() {
        return "StringEntity{string='" + this.string + "', charset='" + this.charset + "', contentType='" + this.contentType + "'}";
    }
}
