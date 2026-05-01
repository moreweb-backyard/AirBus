package cz.msebera.android.httpclient.cookie;

import cz.msebera.android.httpclient.annotation.Immutable;
import java.io.Serializable;
import java.util.Comparator;

@Immutable
/* loaded from: classes.dex */
public class CookieIdentityComparator implements Serializable, Comparator<Cookie> {
    private static final long serialVersionUID = 4466565437490631532L;

    @Override // java.util.Comparator
    public int compare(Cookie cookie, Cookie cookie2) {
        int iCompareTo = cookie.getName().compareTo(cookie2.getName());
        if (iCompareTo == 0) {
            String domain = cookie.getDomain();
            if (domain == null) {
                domain = "";
            } else if (domain.indexOf(46) == -1) {
                domain = domain + ".local";
            }
            String domain2 = cookie2.getDomain();
            if (domain2 == null) {
                domain2 = "";
            } else if (domain2.indexOf(46) == -1) {
                domain2 = domain2 + ".local";
            }
            iCompareTo = domain.compareToIgnoreCase(domain2);
        }
        if (iCompareTo != 0) {
            return iCompareTo;
        }
        String path = cookie.getPath();
        if (path == null) {
            path = "/";
        }
        String path2 = cookie2.getPath();
        if (path2 == null) {
            path2 = "/";
        }
        return path.compareTo(path2);
    }
}
