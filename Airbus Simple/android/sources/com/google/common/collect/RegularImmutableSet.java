package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class RegularImmutableSet<E> extends ImmutableSet.ArrayImmutableSet<E> {
    private final transient int hashCode;
    private final transient int mask;

    @VisibleForTesting
    final transient Object[] table;

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return true;
    }

    RegularImmutableSet(Object[] objArr, int i, Object[] objArr2, int i2) {
        super(objArr);
        this.table = objArr2;
        this.mask = i2;
        this.hashCode = i;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        int iSmear = Hashing.smear(obj.hashCode());
        while (true) {
            Object obj2 = this.table[this.mask & iSmear];
            if (obj2 == null) {
                return false;
            }
            if (obj2.equals(obj)) {
                return true;
            }
            iSmear++;
        }
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.hashCode;
    }
}
