package per.goweii.anylayer;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class InputMethodUtils {
    InputMethodUtils() {
    }

    public static void disableShowSoftInput(EditText editText) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            Method method = EditText.class.getMethod("setShowSoftInputOnFocus", Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception unused) {
        }
        try {
            Method method2 = EditText.class.getMethod("setSoftInputShownOnFocus", Boolean.TYPE);
            method2.setAccessible(true);
            method2.invoke(editText, false);
        } catch (Exception unused2) {
        }
    }

    public static void hide(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
        if (inputMethodManager == null || !inputMethodManager.isActive()) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 2);
    }

    public static void show(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
        if (inputMethodManager == null || inputMethodManager.isActive()) {
            return;
        }
        inputMethodManager.showSoftInput(view, 2);
    }

    public static boolean isShow(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
        return inputMethodManager != null && inputMethodManager.isActive();
    }
}
