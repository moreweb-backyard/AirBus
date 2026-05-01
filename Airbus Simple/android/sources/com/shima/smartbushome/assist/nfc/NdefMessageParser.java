package com.shima.smartbushome.assist.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class NdefMessageParser {
    private static String hexString = "0123456789ABCDEF";

    private NdefMessageParser() {
    }

    public static List<ParsedNdefRecord> parse(NdefMessage ndefMessage) {
        return getRecords(ndefMessage.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] ndefRecordArr) {
        ArrayList arrayList = new ArrayList();
        for (final NdefRecord ndefRecord : ndefRecordArr) {
            if (UriRecord.isUri(ndefRecord)) {
                arrayList.add(UriRecord.parse(ndefRecord));
            } else if (TextRecord.isText(ndefRecord)) {
                arrayList.add(TextRecord.parse(ndefRecord));
            } else if (SmartPoster.isPoster(ndefRecord)) {
                arrayList.add(SmartPoster.parse(ndefRecord));
            } else {
                arrayList.add(new ParsedNdefRecord() { // from class: com.shima.smartbushome.assist.nfc.NdefMessageParser.1
                    @Override // com.shima.smartbushome.assist.nfc.ParsedNdefRecord
                    public String getViewText() {
                        return new String(ndefRecord.getPayload()) + IOUtils.LINE_SEPARATOR_UNIX;
                    }
                });
            }
        }
        return arrayList;
    }

    public static String decode(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length() / 2);
        for (int i = 0; i < str.length(); i += 2) {
            byteArrayOutputStream.write((hexString.indexOf(str.charAt(i)) << 4) | hexString.indexOf(str.charAt(i + 1)));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    private static String bytesToHexString(byte[] bArr, boolean z) {
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append("0x");
        }
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        char[] cArr = new char[2];
        for (int i = 0; i < bArr.length; i++) {
            cArr[0] = Character.toUpperCase(Character.forDigit((bArr[i] >>> 4) & 15, 16));
            cArr[1] = Character.toUpperCase(Character.forDigit(bArr[i] & 15, 16));
            System.out.println(cArr);
            sb.append(cArr);
        }
        return sb.toString();
    }
}
