package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
/* loaded from: classes.dex */
final class Hashing {
    private static final int CUTOFF = 536870912;
    private static final int MAX_TABLE_SIZE = 1073741824;

    static int smear(int i) {
        int i2 = i ^ ((i >>> 20) ^ (i >>> 12));
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }

    private Hashing() {
    }

    static int chooseTableSize(int i) {
        if (i < CUTOFF) {
            return Integer.highestOneBit(i) << 2;
        }
        Preconditions.checkArgument(i < MAX_TABLE_SIZE, "collection too large");
        return MAX_TABLE_SIZE;
    }
}
