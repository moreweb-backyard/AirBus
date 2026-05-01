package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* loaded from: classes.dex */
final class ImmutableAsList<E> extends RegularImmutableList<E> {
    private final transient ImmutableCollection<E> collection;

    ImmutableAsList(Object[] objArr, ImmutableCollection<E> immutableCollection) {
        super(objArr, 0, objArr.length);
        this.collection = immutableCollection;
    }

    @Override // com.google.common.collect.RegularImmutableList, com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return this.collection.contains(obj);
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableCollection<?> collection;

        SerializedForm(ImmutableCollection<?> immutableCollection) {
            this.collection = immutableCollection;
        }

        Object readResolve() {
            return this.collection.asList();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.collection);
    }
}
