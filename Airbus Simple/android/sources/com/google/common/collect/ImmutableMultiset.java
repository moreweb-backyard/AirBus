package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Serialization;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
    private static final long serialVersionUID = 0;
    private transient ImmutableSet<Multiset.Entry<E>> entrySet;
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return this;
    }

    public static <E> ImmutableMultiset<E> of() {
        return EmptyImmutableMultiset.INSTANCE;
    }

    public static <E> ImmutableMultiset<E> of(E... eArr) {
        return copyOf(Arrays.asList(eArr));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableMultiset) {
            return (ImmutableMultiset) iterable;
        }
        return copyOfInternal(iterable instanceof Multiset ? (Multiset) iterable : LinkedHashMultiset.create(iterable));
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        long j = 0;
        for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
            int count = entry.getCount();
            if (count > 0) {
                builder.put(entry.getElement(), Integer.valueOf(count));
                j += count;
            }
        }
        if (j == 0) {
            return of();
        }
        return new ImmutableMultiset<>(builder.build(), (int) Math.min(j, 2147483647L));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> it) {
        LinkedHashMultiset linkedHashMultisetCreate = LinkedHashMultiset.create();
        Iterators.addAll(linkedHashMultisetCreate, it);
        return copyOfInternal(linkedHashMultisetCreate);
    }

    private static class FieldSettersHolder {
        static final Serialization.FieldSetter<ImmutableMultiset> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "map");
        static final Serialization.FieldSetter<ImmutableMultiset> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "size");

        private FieldSettersHolder() {
        }
    }

    ImmutableMultiset(ImmutableMap<E, Integer> immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    @Override // com.google.common.collect.Multiset
    public int count(@Nullable Object obj) {
        Integer num = this.map.get(obj);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public UnmodifiableIterator<E> iterator() {
        final UnmodifiableIterator<Map.Entry<E, Integer>> it = this.map.entrySet().iterator();
        return new UnmodifiableIterator<E>() { // from class: com.google.common.collect.ImmutableMultiset.1
            E element;
            int remaining;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.remaining > 0 || it.hasNext();
            }

            @Override // java.util.Iterator
            public E next() {
                if (this.remaining <= 0) {
                    Map.Entry entry = (Map.Entry) it.next();
                    this.element = (E) entry.getKey();
                    this.remaining = ((Integer) entry.getValue()).intValue();
                }
                this.remaining--;
                return this.element;
            }
        };
    }

    @Override // java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // com.google.common.collect.Multiset
    public int add(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    public int remove(Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    public int setCount(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multiset
    public boolean setCount(E e, int i, int i2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Multiset)) {
            return false;
        }
        Multiset multiset = (Multiset) obj;
        if (size() != multiset.size()) {
            return false;
        }
        for (Multiset.Entry<E> entry : multiset.entrySet()) {
            if (count(entry.getElement()) != entry.getCount()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, com.google.common.collect.Multiset
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public String toString() {
        return entrySet().toString();
    }

    @Override // com.google.common.collect.Multiset
    public Set<E> elementSet() {
        return this.map.keySet();
    }

    @Override // com.google.common.collect.Multiset
    public Set<Multiset.Entry<E>> entrySet() {
        ImmutableSet<Multiset.Entry<E>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        EntrySet entrySet = new EntrySet(this);
        this.entrySet = entrySet;
        return entrySet;
    }

    private static class EntrySet<E> extends ImmutableSet<Multiset.Entry<E>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultiset<E> multiset;

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return this;
        }

        public EntrySet(ImmutableMultiset<E> immutableMultiset) {
            this.multiset = immutableMultiset;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
            final UnmodifiableIterator<E> it = ((ImmutableMultiset) this.multiset).map.entrySet().iterator();
            return new UnmodifiableIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.ImmutableMultiset.EntrySet.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public Multiset.Entry<E> next() {
                    Map.Entry entry = (Map.Entry) it.next();
                    return Multisets.immutableEntry(entry.getKey(), ((Integer) entry.getValue()).intValue());
                }
            };
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return ((ImmutableMultiset) this.multiset).map.size();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            if (!(obj instanceof Multiset.Entry)) {
                return false;
            }
            Multiset.Entry entry = (Multiset.Entry) obj;
            return entry.getCount() > 0 && this.multiset.count(entry.getElement()) == entry.getCount();
        }

        @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return ((ImmutableMultiset) this.multiset).map.hashCode();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Serialization.writeMultiset(this, objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IllegalAccessException, ClassNotFoundException, IOException, IllegalArgumentException {
        objectInputStream.defaultReadObject();
        int i = objectInputStream.readInt();
        ImmutableMap.Builder builder = ImmutableMap.builder();
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            Object object = objectInputStream.readObject();
            int i3 = objectInputStream.readInt();
            if (i3 <= 0) {
                throw new InvalidObjectException("Invalid count " + i3);
            }
            builder.put(object, Integer.valueOf(i3));
            j += i3;
        }
        FieldSettersHolder.MAP_FIELD_SETTER.set((Serialization.FieldSetter<ImmutableMultiset>) this, (Object) builder.build());
        FieldSettersHolder.SIZE_FIELD_SETTER.set((Serialization.FieldSetter<ImmutableMultiset>) this, (int) Math.min(j, 2147483647L));
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static final class Builder<E> extends ImmutableCollection.Builder<E> {
        private final Multiset<E> contents = LinkedHashMultiset.create();

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E e) {
            this.contents.add(Preconditions.checkNotNull(e));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<E> addCopies(E e, int i) {
            this.contents.add(Preconditions.checkNotNull(e), i);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<E> setCount(E e, int i) {
            this.contents.setCount(Preconditions.checkNotNull(e), i);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Multiset) {
                for (Multiset.Entry<E> entry : ((Multiset) iterable).entrySet()) {
                    addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll((Iterable) iterable);
            }
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf(this.contents);
        }
    }
}
