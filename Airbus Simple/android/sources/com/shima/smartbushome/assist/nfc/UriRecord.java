package com.shima.smartbushome.assist.nfc;

import android.net.Uri;
import android.nfc.NdefRecord;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.primitives.Bytes;
import com.shima.smartbushome.udp.CONST;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class UriRecord implements ParsedNdefRecord {
    public static final String RECORD_TYPE = "UriRecord";
    private static final String TAG = "UriRecord";
    private final Uri mUri;
    private static final BiMap<Byte, String> URI_PREFIX_MAP = ImmutableBiMap.builder().put((ImmutableBiMap.Builder) (byte) 0, (byte) "").put((ImmutableBiMap.Builder) (byte) 1, (byte) "http://www.").put((ImmutableBiMap.Builder) (byte) 2, (byte) "https://www.").put((ImmutableBiMap.Builder) (byte) 3, (byte) "http://").put((ImmutableBiMap.Builder) (byte) 4, (byte) "https://").put((ImmutableBiMap.Builder) (byte) 5, (byte) "tel:").put((ImmutableBiMap.Builder) (byte) 6, (byte) "mailto:").put((ImmutableBiMap.Builder) (byte) 7, (byte) "ftp://anonymous:anonymous@").put((ImmutableBiMap.Builder) (byte) 8, (byte) "ftp://ftp.").put((ImmutableBiMap.Builder) (byte) 9, (byte) "ftps://").put((ImmutableBiMap.Builder) (byte) 10, (byte) "sftp://").put((ImmutableBiMap.Builder) (byte) 11, (byte) "smb://").put((ImmutableBiMap.Builder) (byte) 12, (byte) "nfs://").put((ImmutableBiMap.Builder) Byte.valueOf(MultipartStream.CR), (Byte) "ftp://").put((ImmutableBiMap.Builder) (byte) 14, (byte) "dav://").put((ImmutableBiMap.Builder) (byte) 15, (byte) "news:").put((ImmutableBiMap.Builder) Byte.valueOf(CONST.CONST_START_PST_OF_LEN_OF_DATA_IN_FULL_PACKETS), (Byte) "telnet://").put((ImmutableBiMap.Builder) (byte) 17, (byte) "imap:").put((ImmutableBiMap.Builder) (byte) 18, (byte) "rtsp://").put((ImmutableBiMap.Builder) (byte) 19, (byte) "urn:").put((ImmutableBiMap.Builder) (byte) 20, (byte) "pop:").put((ImmutableBiMap.Builder) (byte) 21, (byte) "sip:").put((ImmutableBiMap.Builder) (byte) 22, (byte) "sips:").put((ImmutableBiMap.Builder) (byte) 23, (byte) "tftp:").put((ImmutableBiMap.Builder) (byte) 24, (byte) "btspp://").put((ImmutableBiMap.Builder) Byte.valueOf(CONST.CONST_START_PST_OF_ADDITIONAL_DATA_IN_FULL_PACKETS), (Byte) "btl2cap://").put((ImmutableBiMap.Builder) (byte) 26, (byte) "btgoep://").put((ImmutableBiMap.Builder) (byte) 27, (byte) "tcpobex://").put((ImmutableBiMap.Builder) (byte) 28, (byte) "irdaobex://").put((ImmutableBiMap.Builder) (byte) 29, (byte) "file://").put((ImmutableBiMap.Builder) (byte) 30, (byte) "urn:epc:id:").put((ImmutableBiMap.Builder) (byte) 31, (byte) "urn:epc:tag:").put((ImmutableBiMap.Builder) (byte) 32, (byte) "urn:epc:pat:").put((ImmutableBiMap.Builder) (byte) 33, (byte) "urn:epc:raw:").put((ImmutableBiMap.Builder) (byte) 34, (byte) "urn:epc:").put((ImmutableBiMap.Builder) (byte) 35, (byte) "urn:nfc:").build();
    private static final byte[] EMPTY = new byte[0];

    private UriRecord(Uri uri) {
        this.mUri = (Uri) Preconditions.checkNotNull(uri);
    }

    @Override // com.shima.smartbushome.assist.nfc.ParsedNdefRecord
    public String getViewText() {
        return this.mUri.toString();
    }

    public Uri getUri() {
        return this.mUri;
    }

    public static UriRecord parse(NdefRecord ndefRecord) {
        short tnf = ndefRecord.getTnf();
        if (tnf == 1) {
            return parseWellKnown(ndefRecord);
        }
        if (tnf == 3) {
            return parseAbsolute(ndefRecord);
        }
        throw new IllegalArgumentException("Unknown TNF " + ((int) tnf));
    }

    private static UriRecord parseAbsolute(NdefRecord ndefRecord) {
        return new UriRecord(Uri.parse(new String(ndefRecord.getPayload(), Charset.forName("UTF-8"))));
    }

    private static UriRecord parseWellKnown(NdefRecord ndefRecord) {
        Preconditions.checkArgument(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI));
        byte[] payload = ndefRecord.getPayload();
        return new UriRecord(Uri.parse(new String(Bytes.concat(URI_PREFIX_MAP.get(Byte.valueOf(payload[0])).getBytes(Charset.forName("UTF-8")), Arrays.copyOfRange(payload, 1, payload.length)), Charset.forName("UTF-8"))));
    }

    public static boolean isUri(NdefRecord ndefRecord) {
        try {
            parse(ndefRecord);
            return true;
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }
}
