package com.shima.smartbushome.assist;

import com.shima.smartbushome.database.Savemarco;
import java.util.Comparator;

/* loaded from: classes.dex */
public class marcoCompare implements Comparator<Savemarco> {
    @Override // java.util.Comparator
    public int compare(Savemarco savemarco, Savemarco savemarco2) {
        if (savemarco.sentorder > savemarco2.sentorder) {
            return 1;
        }
        return savemarco.sentorder == savemarco2.sentorder ? 0 : -1;
    }
}
