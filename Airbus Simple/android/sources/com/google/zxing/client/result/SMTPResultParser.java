package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes.dex */
public final class SMTPResultParser extends ResultParser {
    @Override // com.google.zxing.client.result.ResultParser
    public EmailAddressParsedResult parse(Result result) {
        String strSubstring;
        String massagedText = getMassagedText(result);
        String strSubstring2 = null;
        if (!massagedText.startsWith("smtp:") && !massagedText.startsWith("SMTP:")) {
            return null;
        }
        String strSubstring3 = massagedText.substring(5);
        int iIndexOf = strSubstring3.indexOf(58);
        if (iIndexOf >= 0) {
            String strSubstring4 = strSubstring3.substring(iIndexOf + 1);
            strSubstring3 = strSubstring3.substring(0, iIndexOf);
            int iIndexOf2 = strSubstring4.indexOf(58);
            if (iIndexOf2 >= 0) {
                strSubstring2 = strSubstring4.substring(iIndexOf2 + 1);
                strSubstring = strSubstring4.substring(0, iIndexOf2);
            } else {
                strSubstring = strSubstring4;
            }
        } else {
            strSubstring = null;
        }
        return new EmailAddressParsedResult(strSubstring3, strSubstring, strSubstring2, "mailto:" + strSubstring3);
    }
}
