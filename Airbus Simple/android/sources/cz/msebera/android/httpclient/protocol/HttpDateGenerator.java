package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.annotation.GuardedBy;
import cz.msebera.android.httpclient.annotation.ThreadSafe;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@ThreadSafe
/* loaded from: classes.dex */
public class HttpDateGenerator {
    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    @GuardedBy("this")
    private long dateAsLong = 0;

    @GuardedBy("this")
    private String dateAsText = null;

    @GuardedBy("this")
    private final DateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    public HttpDateGenerator() {
        this.dateformat.setTimeZone(GMT);
    }

    public synchronized String getCurrentDate() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.dateAsLong > 1000) {
            this.dateAsText = this.dateformat.format(new Date(jCurrentTimeMillis));
            this.dateAsLong = jCurrentTimeMillis;
        }
        return this.dateAsText;
    }
}
