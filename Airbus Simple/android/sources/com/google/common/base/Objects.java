package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.litesuits.http.data.Consts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: classes.dex */
public final class Objects {
    private Objects() {
    }

    public static boolean equal(@Nullable Object obj, @Nullable Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj);
    }

    public static class ToStringHelper {
        private static final Joiner JOINER = Joiner.on(", ");
        private final List<String> fieldString;
        private final Object instance;

        private ToStringHelper(Object obj) {
            this.fieldString = new ArrayList();
            this.instance = Preconditions.checkNotNull(obj);
        }

        public ToStringHelper add(String str, @Nullable Object obj) {
            return addValue(((String) Preconditions.checkNotNull(str)) + Consts.EQUALS + obj);
        }

        public ToStringHelper addValue(@Nullable Object obj) {
            this.fieldString.add(String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(100);
            sb.append(simpleName(this.instance.getClass()));
            sb.append('{');
            StringBuilder sbAppendTo = JOINER.appendTo(sb, (Iterable<?>) this.fieldString);
            sbAppendTo.append('}');
            return sbAppendTo.toString();
        }

        @VisibleForTesting
        static String simpleName(Class<?> cls) {
            String name = cls.getName();
            int iLastIndexOf = name.lastIndexOf(36);
            if (iLastIndexOf == -1) {
                iLastIndexOf = name.lastIndexOf(46);
            }
            return name.substring(iLastIndexOf + 1);
        }
    }
}
