package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.litesuits.http.data.Consts;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible
/* loaded from: classes.dex */
public final class Predicates {
    private static final Joiner commaJoiner = Joiner.on(Consts.SECOND_LEVEL_SPLIT);

    enum AlwaysFalsePredicate implements Predicate<Object> {
        INSTANCE;

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            return false;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "AlwaysFalse";
        }
    }

    enum AlwaysTruePredicate implements Predicate<Object> {
        INSTANCE;

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            return true;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "AlwaysTrue";
        }
    }

    private enum IsNullPredicate implements Predicate<Object> {
        INSTANCE;

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            return obj == null;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "IsNull";
        }
    }

    private enum NotNullPredicate implements Predicate<Object> {
        INSTANCE;

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            return obj != null;
        }

        @Override // java.lang.Enum
        public String toString() {
            return "NotNull";
        }
    }

    private Predicates() {
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <T> Predicate<T> alwaysTrue() {
        return AlwaysTruePredicate.INSTANCE;
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <T> Predicate<T> alwaysFalse() {
        return AlwaysFalsePredicate.INSTANCE;
    }

    public static <T> Predicate<T> isNull() {
        return IsNullPredicate.INSTANCE;
    }

    public static <T> Predicate<T> notNull() {
        return NotNullPredicate.INSTANCE;
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return new NotPredicate(predicate);
    }

    public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> iterable) {
        return new AndPredicate(defensiveCopy(iterable));
    }

    public static <T> Predicate<T> and(Predicate<? super T>... predicateArr) {
        return new AndPredicate(defensiveCopy(predicateArr));
    }

    public static <T> Predicate<T> and(Predicate<? super T> predicate, Predicate<? super T> predicate2) {
        return new AndPredicate(asList((Predicate) Preconditions.checkNotNull(predicate), (Predicate) Preconditions.checkNotNull(predicate2)));
    }

    public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> iterable) {
        return new OrPredicate(defensiveCopy(iterable));
    }

    public static <T> Predicate<T> or(Predicate<? super T>... predicateArr) {
        return new OrPredicate(defensiveCopy(predicateArr));
    }

    public static <T> Predicate<T> or(Predicate<? super T> predicate, Predicate<? super T> predicate2) {
        return new OrPredicate(asList((Predicate) Preconditions.checkNotNull(predicate), (Predicate) Preconditions.checkNotNull(predicate2)));
    }

    public static <T> Predicate<T> equalTo(@Nullable T t) {
        return t == null ? isNull() : new IsEqualToPredicate(t);
    }

    @GwtIncompatible("Class.isInstance")
    public static Predicate<Object> instanceOf(Class<?> cls) {
        return new InstanceOfPredicate(cls);
    }

    public static <T> Predicate<T> in(Collection<? extends T> collection) {
        return new InPredicate(collection);
    }

    public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
        return new CompositionPredicate(predicate, function);
    }

    private static class NotPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private NotPredicate(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            return !this.predicate.apply(t);
        }

        public int hashCode() {
            return this.predicate.hashCode() ^ (-1);
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (obj instanceof NotPredicate) {
                return this.predicate.equals(((NotPredicate) obj).predicate);
            }
            return false;
        }

        public String toString() {
            return "Not(" + this.predicate.toString() + ")";
        }
    }

    private static class AndPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Iterable<? extends Predicate<? super T>> components;

        private AndPredicate(Iterable<? extends Predicate<? super T>> iterable) {
            this.components = iterable;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            Iterator<? extends Predicate<? super T>> it = this.components.iterator();
            while (it.hasNext()) {
                if (!it.next().apply(t)) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            Iterator<? extends Predicate<? super T>> it = this.components.iterator();
            int iHashCode = -1;
            while (it.hasNext()) {
                iHashCode &= it.next().hashCode();
            }
            return iHashCode;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (obj instanceof AndPredicate) {
                return Predicates.iterableElementsEqual(this.components, ((AndPredicate) obj).components);
            }
            return false;
        }

        public String toString() {
            return "And(" + Predicates.commaJoiner.join(this.components) + ")";
        }
    }

    private static class OrPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Iterable<? extends Predicate<? super T>> components;

        private OrPredicate(Iterable<? extends Predicate<? super T>> iterable) {
            this.components = iterable;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            Iterator<? extends Predicate<? super T>> it = this.components.iterator();
            while (it.hasNext()) {
                if (it.next().apply(t)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            Iterator<? extends Predicate<? super T>> it = this.components.iterator();
            int iHashCode = 0;
            while (it.hasNext()) {
                iHashCode |= it.next().hashCode();
            }
            return iHashCode;
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (obj instanceof OrPredicate) {
                return Predicates.iterableElementsEqual(this.components, ((OrPredicate) obj).components);
            }
            return false;
        }

        public String toString() {
            return "Or(" + Predicates.commaJoiner.join(this.components) + ")";
        }
    }

    private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final T target;

        private IsEqualToPredicate(T t) {
            this.target = t;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            return this.target.equals(t);
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                return this.target.equals(((IsEqualToPredicate) obj).target);
            }
            return false;
        }

        public String toString() {
            return "IsEqualTo(" + this.target + ")";
        }
    }

    private static class InstanceOfPredicate implements Predicate<Object>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<?> clazz;

        private InstanceOfPredicate(Class<?> cls) {
            this.clazz = (Class) Preconditions.checkNotNull(cls);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            return Platform.isInstance(this.clazz, obj);
        }

        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            return (obj instanceof InstanceOfPredicate) && this.clazz == ((InstanceOfPredicate) obj).clazz;
        }

        public String toString() {
            return "IsInstanceOf(" + this.clazz.getName() + ")";
        }
    }

    private static class InPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Collection<?> target;

        private InPredicate(Collection<?> collection) {
            this.target = (Collection) Preconditions.checkNotNull(collection);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            try {
                return this.target.contains(t);
            } catch (ClassCastException unused) {
                return false;
            } catch (NullPointerException unused2) {
                return false;
            }
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (obj instanceof InPredicate) {
                return this.target.equals(((InPredicate) obj).target);
            }
            return false;
        }

        public int hashCode() {
            return this.target.hashCode();
        }

        public String toString() {
            return "In(" + this.target + ")";
        }
    }

    private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
        private static final long serialVersionUID = 0;
        final Function<A, ? extends B> f;
        final Predicate<B> p;

        private CompositionPredicate(Predicate<B> predicate, Function<A, ? extends B> function) {
            this.p = (Predicate) Preconditions.checkNotNull(predicate);
            this.f = (Function) Preconditions.checkNotNull(function);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(A a) {
            return this.p.apply(this.f.apply(a));
        }

        @Override // com.google.common.base.Predicate
        public boolean equals(Object obj) {
            if (!(obj instanceof CompositionPredicate)) {
                return false;
            }
            CompositionPredicate compositionPredicate = (CompositionPredicate) obj;
            return this.f.equals(compositionPredicate.f) && this.p.equals(compositionPredicate.p);
        }

        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }

        public String toString() {
            return this.p.toString() + "(" + this.f.toString() + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean iterableElementsEqual(Iterable<?> iterable, Iterable<?> iterable2) {
        Iterator<?> it = iterable.iterator();
        Iterator<?> it2 = iterable2.iterator();
        while (it.hasNext()) {
            if (!it2.hasNext() || !it.next().equals(it2.next())) {
                return false;
            }
        }
        return !it2.hasNext();
    }

    private static <T> List<Predicate<? super T>> asList(Predicate<? super T> predicate, Predicate<? super T> predicate2) {
        return Arrays.asList(predicate, predicate2);
    }

    private static <T> List<T> defensiveCopy(T... tArr) {
        return defensiveCopy(Arrays.asList(tArr));
    }

    static <T> List<T> defensiveCopy(Iterable<T> iterable) {
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(Preconditions.checkNotNull(it.next()));
        }
        return arrayList;
    }
}
