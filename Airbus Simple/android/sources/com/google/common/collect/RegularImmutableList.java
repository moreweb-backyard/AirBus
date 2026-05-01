package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
class RegularImmutableList<E> extends ImmutableList<E> {
    private final transient Object[] array;
    private final transient int offset;
    private final transient int size;

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return false;
    }

    RegularImmutableList(Object[] objArr, int i, int i2) {
        this.offset = i;
        this.size = i2;
        this.array = objArr;
    }

    RegularImmutableList(Object[] objArr) {
        this(objArr, 0, objArr.length);
    }

    @Override // java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public UnmodifiableIterator<E> iterator() {
        return Iterators.forArray(this.array, this.offset, this.size);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] objArr = new Object[size()];
        Platform.unsafeArrayCopy(this.array, this.offset, objArr, 0, this.size);
        return objArr;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.size) {
            tArr = (T[]) ObjectArrays.newArray(tArr, this.size);
        } else if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        Platform.unsafeArrayCopy(this.array, this.offset, tArr, 0, this.size);
        return tArr;
    }

    @Override // java.util.List
    public E get(int i) {
        Preconditions.checkElementIndex(i, this.size);
        return (E) this.array[i + this.offset];
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        for (int i = this.offset; i < this.offset + this.size; i++) {
            if (this.array[i].equals(obj)) {
                return i - this.offset;
            }
        }
        return -1;
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public int lastIndexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        int i = this.offset + this.size;
        do {
            i--;
            if (i < this.offset) {
                return -1;
            }
        } while (!this.array[i].equals(obj));
        return i - this.offset;
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, this.size);
        return i == i2 ? ImmutableList.of() : new RegularImmutableList(this.array, this.offset + i, i2 - i);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(final int i) {
        Preconditions.checkPositionIndex(i, this.size);
        return new ListIterator<E>() { // from class: com.google.common.collect.RegularImmutableList.1
            int index;

            {
                this.index = i;
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public boolean hasNext() {
                return this.index < RegularImmutableList.this.size;
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return this.index > 0;
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.index;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.index - 1;
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public E next() {
                try {
                    E e = (E) RegularImmutableList.this.get(this.index);
                    this.index++;
                    return e;
                } catch (IndexOutOfBoundsException unused) {
                    throw new NoSuchElementException();
                }
            }

            @Override // java.util.ListIterator
            public E previous() {
                try {
                    this.index--;
                    return (E) RegularImmutableList.this.get(this.index - 1);
                } catch (IndexOutOfBoundsException unused) {
                    throw new NoSuchElementException();
                }
            }

            @Override // java.util.ListIterator
            public void set(E e) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public void add(E e) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List list = (List) obj;
        if (size() != list.size()) {
            return false;
        }
        int i = this.offset;
        if (obj instanceof RegularImmutableList) {
            RegularImmutableList regularImmutableList = (RegularImmutableList) obj;
            int i2 = regularImmutableList.offset;
            while (i2 < regularImmutableList.offset + regularImmutableList.size) {
                int i3 = i + 1;
                if (!this.array[i].equals(regularImmutableList.array[i2])) {
                    return false;
                }
                i2++;
                i = i3;
            }
        } else {
            Iterator<E> it = list.iterator();
            while (it.hasNext()) {
                int i4 = i + 1;
                if (!this.array[i].equals(it.next())) {
                    return false;
                }
                i = i4;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int iHashCode = 1;
        for (int i = this.offset; i < this.offset + this.size; i++) {
            iHashCode = (iHashCode * 31) + this.array[i].hashCode();
        }
        return iHashCode;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public String toString() {
        StringBuilder sb = new StringBuilder(size() * 16);
        sb.append('[');
        sb.append(this.array[this.offset]);
        int i = this.offset;
        while (true) {
            i++;
            if (i < this.offset + this.size) {
                sb.append(", ");
                sb.append(this.array[i]);
            } else {
                sb.append(']');
                return sb.toString();
            }
        }
    }

    int offset() {
        return this.offset;
    }

    Object[] array() {
        return this.array;
    }
}
