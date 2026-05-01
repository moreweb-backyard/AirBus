package com.litesuits.http.utils;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class UriUtil {
    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            return Collections.emptySet();
        }
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery == null) {
            return Collections.emptySet();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i = 0;
        do {
            int iIndexOf = encodedQuery.indexOf(38, i);
            if (iIndexOf == -1) {
                iIndexOf = encodedQuery.length();
            }
            int iIndexOf2 = encodedQuery.indexOf(61, i);
            if (iIndexOf2 > iIndexOf || iIndexOf2 == -1) {
                iIndexOf2 = iIndexOf;
            }
            linkedHashSet.add(encodedQuery.substring(i, iIndexOf2));
            i = iIndexOf + 1;
        } while (i < encodedQuery.length());
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public static List<String> getQueryParameters(Uri uri, String str) {
        if (uri.isOpaque()) {
            return Collections.emptyList();
        }
        if (str == null) {
            throw new NullPointerException("key");
        }
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int iIndexOf = encodedQuery.indexOf(38, i);
            int length = iIndexOf != -1 ? iIndexOf : encodedQuery.length();
            int iIndexOf2 = encodedQuery.indexOf(61, i);
            if (iIndexOf2 > length || iIndexOf2 == -1) {
                iIndexOf2 = length;
            }
            if (iIndexOf2 - i == str.length() && encodedQuery.regionMatches(i, str, 0, str.length())) {
                if (iIndexOf2 == length) {
                    arrayList.add("");
                } else {
                    arrayList.add(encodedQuery.substring(iIndexOf2 + 1, length));
                }
            }
            if (iIndexOf == -1) {
                return Collections.unmodifiableList(arrayList);
            }
            i = iIndexOf + 1;
        }
    }
}
