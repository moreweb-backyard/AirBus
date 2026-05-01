package cz.msebera.android.httpclient;

import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;

@Immutable
/* loaded from: classes.dex */
public final class HttpHost implements Cloneable, Serializable {
    public static final String DEFAULT_SCHEME_NAME = "http";
    private static final long serialVersionUID = -7529410654042457626L;
    protected final InetAddress address;
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;

    public HttpHost(String str, int i, String str2) {
        this.hostname = (String) Args.containsNoBlanks(str, "Host name");
        this.lcHostname = str.toLowerCase(Locale.ROOT);
        if (str2 != null) {
            this.schemeName = str2.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = "http";
        }
        this.port = i;
        this.address = null;
    }

    public HttpHost(String str, int i) {
        this(str, i, (String) null);
    }

    public static HttpHost create(String str) throws NumberFormatException {
        String strSubstring;
        Args.containsNoBlanks(str, "HTTP Host");
        int iIndexOf = str.indexOf("://");
        if (iIndexOf > 0) {
            strSubstring = str.substring(0, iIndexOf);
            str = str.substring(iIndexOf + 3);
        } else {
            strSubstring = null;
        }
        int i = -1;
        int iLastIndexOf = str.lastIndexOf(":");
        if (iLastIndexOf > 0) {
            try {
                i = Integer.parseInt(str.substring(iLastIndexOf + 1));
                str = str.substring(0, iLastIndexOf);
            } catch (NumberFormatException unused) {
                throw new IllegalArgumentException("Invalid HTTP host: " + str);
            }
        }
        return new HttpHost(str, i, strSubstring);
    }

    public HttpHost(String str) {
        this(str, -1, (String) null);
    }

    public HttpHost(InetAddress inetAddress, int i, String str) {
        this((InetAddress) Args.notNull(inetAddress, "Inet address"), inetAddress.getHostName(), i, str);
    }

    public HttpHost(InetAddress inetAddress, String str, int i, String str2) {
        this.address = (InetAddress) Args.notNull(inetAddress, "Inet address");
        this.hostname = (String) Args.notNull(str, "Hostname");
        this.lcHostname = this.hostname.toLowerCase(Locale.ROOT);
        if (str2 != null) {
            this.schemeName = str2.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = "http";
        }
        this.port = i;
    }

    public HttpHost(InetAddress inetAddress, int i) {
        this(inetAddress, i, (String) null);
    }

    public HttpHost(InetAddress inetAddress) {
        this(inetAddress, -1, (String) null);
    }

    public HttpHost(HttpHost httpHost) {
        Args.notNull(httpHost, "HTTP host");
        this.hostname = httpHost.hostname;
        this.lcHostname = httpHost.lcHostname;
        this.schemeName = httpHost.schemeName;
        this.port = httpHost.port;
        this.address = httpHost.address;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public String toURI() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.schemeName);
        sb.append("://");
        sb.append(this.hostname);
        if (this.port != -1) {
            sb.append(':');
            sb.append(Integer.toString(this.port));
        }
        return sb.toString();
    }

    public String toHostString() {
        if (this.port != -1) {
            StringBuilder sb = new StringBuilder(this.hostname.length() + 6);
            sb.append(this.hostname);
            sb.append(":");
            sb.append(Integer.toString(this.port));
            return sb.toString();
        }
        return this.hostname;
    }

    public String toString() {
        return toURI();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHost)) {
            return false;
        }
        HttpHost httpHost = (HttpHost) obj;
        if (this.lcHostname.equals(httpHost.lcHostname) && this.port == httpHost.port && this.schemeName.equals(httpHost.schemeName)) {
            if (this.address == null) {
                if (httpHost.address == null) {
                    return true;
                }
            } else if (this.address.equals(httpHost.address)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.lcHostname), this.port), this.schemeName);
        return this.address != null ? LangUtils.hashCode(iHashCode, this.address) : iHashCode;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
