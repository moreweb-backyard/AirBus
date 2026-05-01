package com.bigkoo.alertview;

/* loaded from: classes.dex */
public class AlertAnimateUtil {
    private static final int INVALID = -1;

    static int getAnimationResource(int i, boolean z) {
        if (i == 17) {
            return z ? R.anim.fade_in_center : R.anim.fade_out_center;
        }
        if (i != 80) {
            return -1;
        }
        return z ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
    }
}
