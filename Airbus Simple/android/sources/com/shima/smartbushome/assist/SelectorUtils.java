package com.shima.smartbushome.assist;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class SelectorUtils {
    public static void changeViewColor(StateListDrawable stateListDrawable, int[] iArr) throws NoSuchMethodException, SecurityException {
        GradientDrawable gradientDrawable;
        Drawable.ConstantState constantState = stateListDrawable.getConstantState();
        if (iArr.length < 2) {
            return;
        }
        try {
            Method method = constantState.getClass().getMethod("getChildren", new Class[0]);
            method.setAccessible(true);
            Drawable[] drawableArr = (Drawable[]) method.invoke(constantState, new Object[0]);
            for (int i = 0; i < drawableArr.length && (gradientDrawable = (GradientDrawable) drawableArr[i]) != null; i++) {
                if (i == 0) {
                    gradientDrawable.setColor(iArr[0]);
                } else {
                    gradientDrawable.setColor(iArr[1]);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }
}
