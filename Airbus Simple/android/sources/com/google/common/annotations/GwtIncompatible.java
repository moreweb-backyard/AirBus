package com.google.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
/* loaded from: classes.dex */
public @interface GwtIncompatible {
    String value();
}
