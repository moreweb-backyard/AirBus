package com.shima.smartbushome.assist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class rollingTextview extends TextView {
    @Override // android.view.View
    public boolean isFocused() {
        return true;
    }

    public rollingTextview(Context context) {
        super(context);
    }

    public rollingTextview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public rollingTextview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
