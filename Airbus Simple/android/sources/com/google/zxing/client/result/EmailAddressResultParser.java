package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Map;

/* loaded from: classes.dex */
public final class EmailAddressResultParser extends ResultParser {
    @Override // com.google.zxing.client.result.ResultParser
    public EmailAddressParsedResult parse(Result result) {
        String str;
        String massagedText = getMassagedText(result);
        String str2 = null;
        if (massagedText.startsWith("mailto:") || massagedText.startsWith("MAILTO:")) {
            String strSubstring = massagedText.substring(7);
            int iIndexOf = strSubstring.indexOf(63);
            if (iIndexOf >= 0) {
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            String strUrlDecode = urlDecode(strSubstring);
            Map<String, String> nameValuePairs = parseNameValuePairs(massagedText);
            if (nameValuePairs != null) {
                if (strUrlDecode.isEmpty()) {
                    strUrlDecode = nameValuePairs.get("to");
                }
                str2 = nameValuePairs.get("subject");
                str = nameValuePairs.get("body");
            } else {
                str = null;
            }
            return new EmailAddressParsedResult(strUrlDecode, str2, str, massagedText);
        }
        if (!EmailDoCoMoResultParser.isBasicallyValidEmailAddress(massagedText)) {
            return null;
        }
        return new EmailAddressParsedResult(massagedText, null, null, "mailto:" + massagedText);
    }
}
