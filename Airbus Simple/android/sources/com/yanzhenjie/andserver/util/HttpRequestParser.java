package com.yanzhenjie.andserver.util;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.litesuits.http.data.Consts;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;

/* loaded from: classes.dex */
public class HttpRequestParser {
    public static Map<String, String> parse(HttpRequest httpRequest) throws IOException {
        return parse(httpRequest, false);
    }

    public static Map<String, String> parse(HttpRequest httpRequest, boolean z) throws IOException {
        return splitHttpParams(getContent(httpRequest), z);
    }

    public static Map<String, String> splitHttpParams(String str, boolean z) {
        HashMap map = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(str, "&");
        while (stringTokenizer.hasMoreElements()) {
            String strNextToken = stringTokenizer.nextToken();
            int iIndexOf = strNextToken.indexOf(Consts.EQUALS);
            if (iIndexOf > 0) {
                String strSubstring = strNextToken.substring(0, iIndexOf);
                if (z) {
                    strSubstring = strSubstring.toLowerCase(Locale.ENGLISH);
                }
                map.put(strSubstring, strNextToken.substring(iIndexOf + 1));
            }
        }
        return map;
    }

    public static String getContent(HttpRequest httpRequest) throws IOException {
        if (isGetMethod(httpRequest)) {
            return getContentForGet(httpRequest);
        }
        return isPostMethod(httpRequest) ? getContentForPost(httpRequest) : "";
    }

    public static String getContentForPost(HttpRequest httpRequest) throws IOException {
        HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
        return EntityUtils.toString(entity, parseHeadValue(entity.getContentType().getValue(), "charset", "utf-8"));
    }

    public static String getContentForGet(HttpRequest httpRequest) {
        int i;
        String uri = httpRequest.getRequestLine().getUri();
        int iIndexOf = uri.indexOf(63);
        return (iIndexOf == -1 || (i = iIndexOf + 1) >= uri.length()) ? "" : uri.substring(i);
    }

    public static boolean isGetMethod(HttpRequest httpRequest) {
        return "GET".equalsIgnoreCase(httpRequest.getRequestLine().getMethod());
    }

    public static boolean isPostMethod(HttpRequest httpRequest) {
        return "POST".equalsIgnoreCase(httpRequest.getRequestLine().getMethod());
    }

    @Deprecated
    public static boolean isPosterMethod(HttpRequest httpRequest) {
        return "POST".equalsIgnoreCase(httpRequest.getRequestLine().getMethod());
    }

    public static String getMimeType(String str) {
        if (TextUtils.isEmpty(str)) {
            return "application/octet-stream";
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
    }

    public static String parseHeadValue(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return str3;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
        while (stringTokenizer.hasMoreElements()) {
            String strNextToken = stringTokenizer.nextToken();
            int iIndexOf = strNextToken.indexOf(61);
            if (iIndexOf > 0 && str2.equalsIgnoreCase(strNextToken.substring(0, iIndexOf).trim())) {
                return strNextToken.substring(iIndexOf + 1).trim();
            }
        }
        return str3;
    }
}
