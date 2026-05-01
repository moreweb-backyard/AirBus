package com.shima.smartbushome.assist.nfc;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public class SmartPoster implements ParsedNdefRecord {
    private static final byte[] ACTION_RECORD_TYPE = {97, 99, 116};
    private static final byte[] TYPE_TYPE = {116};
    private final RecommendedAction mAction;
    private final TextRecord mTitleRecord;
    private final String mType;
    private final UriRecord mUriRecord;

    private SmartPoster(UriRecord uriRecord, TextRecord textRecord, RecommendedAction recommendedAction, String str) {
        this.mUriRecord = (UriRecord) Preconditions.checkNotNull(uriRecord);
        this.mTitleRecord = textRecord;
        this.mAction = (RecommendedAction) Preconditions.checkNotNull(recommendedAction);
        this.mType = str;
    }

    public UriRecord getUriRecord() {
        return this.mUriRecord;
    }

    public TextRecord getTitle() {
        return this.mTitleRecord;
    }

    public static SmartPoster parse(NdefRecord ndefRecord) {
        Preconditions.checkArgument(ndefRecord.getTnf() == 1);
        Preconditions.checkArgument(Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_SMART_POSTER));
        try {
            return parse(new NdefMessage(ndefRecord.getPayload()).getRecords());
        } catch (FormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static SmartPoster parse(NdefRecord[] ndefRecordArr) {
        try {
            List<ParsedNdefRecord> records = NdefMessageParser.getRecords(ndefRecordArr);
            return new SmartPoster((UriRecord) Iterables.getOnlyElement(Iterables.filter(records, UriRecord.class)), (TextRecord) getFirstIfExists(records, TextRecord.class), parseRecommendedAction(ndefRecordArr), parseType(ndefRecordArr));
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isPoster(NdefRecord ndefRecord) {
        try {
            parse(ndefRecord);
            return true;
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }

    private static <T> T getFirstIfExists(Iterable<?> iterable, Class<T> cls) {
        Iterable iterableFilter = Iterables.filter(iterable, cls);
        if (Iterables.isEmpty(iterableFilter)) {
            return null;
        }
        return (T) Iterables.get(iterableFilter, 0);
    }

    private enum RecommendedAction {
        UNKNOWN((byte) -1),
        DO_ACTION((byte) 0),
        SAVE_FOR_LATER((byte) 1),
        OPEN_FOR_EDITING((byte) 2);

        private static final ImmutableMap<Byte, RecommendedAction> LOOKUP;
        private final byte mAction;

        static {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            for (RecommendedAction recommendedAction : values()) {
                builder.put(Byte.valueOf(recommendedAction.getByte()), recommendedAction);
            }
            LOOKUP = builder.build();
        }

        RecommendedAction(byte b) {
            this.mAction = b;
        }

        private byte getByte() {
            return this.mAction;
        }
    }

    private static NdefRecord getByType(byte[] bArr, NdefRecord[] ndefRecordArr) {
        for (NdefRecord ndefRecord : ndefRecordArr) {
            if (Arrays.equals(bArr, ndefRecord.getType())) {
                return ndefRecord;
            }
        }
        return null;
    }

    private static RecommendedAction parseRecommendedAction(NdefRecord[] ndefRecordArr) {
        NdefRecord byType = getByType(ACTION_RECORD_TYPE, ndefRecordArr);
        if (byType == null) {
            return RecommendedAction.UNKNOWN;
        }
        byte b = byType.getPayload()[0];
        if (RecommendedAction.LOOKUP.containsKey(Byte.valueOf(b))) {
            return (RecommendedAction) RecommendedAction.LOOKUP.get(Byte.valueOf(b));
        }
        return RecommendedAction.UNKNOWN;
    }

    private static String parseType(NdefRecord[] ndefRecordArr) {
        NdefRecord byType = getByType(TYPE_TYPE, ndefRecordArr);
        if (byType == null) {
            return null;
        }
        return new String(byType.getPayload(), Charsets.UTF_8);
    }

    @Override // com.shima.smartbushome.assist.nfc.ParsedNdefRecord
    public String getViewText() {
        if (this.mTitleRecord != null) {
            return this.mTitleRecord.getText();
        }
        return this.mTitleRecord.getText();
    }
}
