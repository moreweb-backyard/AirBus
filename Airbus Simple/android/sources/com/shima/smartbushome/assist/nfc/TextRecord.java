package com.shima.smartbushome.assist.nfc;

import android.nfc.NdefRecord;
import com.google.common.base.Preconditions;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/* loaded from: classes.dex */
public class TextRecord implements ParsedNdefRecord {
    private final String mLanguageCode;
    private final String mText;

    private TextRecord(String str, String str2) {
        this.mLanguageCode = (String) Preconditions.checkNotNull(str);
        this.mText = (String) Preconditions.checkNotNull(str2);
    }

    @Override // com.shima.smartbushome.assist.nfc.ParsedNdefRecord
    public String getViewText() {
        return this.mText;
    }

    public String getText() {
        return this.mText;
    }

    public String getLanguageCode() {
        return this.mLanguageCode;
    }

    public static TextRecord parse(NdefRecord ndefRecord) {
        Preconditions.checkArgument(ndefRecord.getTnf() == 1);
        Preconditions.checkArgument(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT));
        try {
            byte[] payload = ndefRecord.getPayload();
            if (payload.length <= 0) {
                ndefRecord.getType();
                return null;
            }
            String str = (payload[0] & 128) == 0 ? "UTF-8" : "UTF-16";
            int i = payload[0] & 63;
            return new TextRecord(new String(payload, 1, i, "US-ASCII"), new String(payload, i + 1, (payload.length - i) - 1, str));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isText(NdefRecord ndefRecord) {
        try {
            parse(ndefRecord);
            return true;
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }
}
