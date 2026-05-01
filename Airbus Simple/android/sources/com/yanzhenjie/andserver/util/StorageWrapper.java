package com.yanzhenjie.andserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StorageWrapper {
    public List<String> scanFile(String str) {
        ArrayList arrayList = new ArrayList(2);
        File file = new File(str);
        if (file.isFile()) {
            arrayList.add(str);
        } else {
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles != null && fileArrListFiles.length > 0) {
                for (File file2 : fileArrListFiles) {
                    if (file2.isFile()) {
                        arrayList.add(file2.getAbsolutePath());
                    } else {
                        List<String> listScanFile = scanFile(file2.getAbsolutePath());
                        if (listScanFile.size() > 0) {
                            arrayList.addAll(listScanFile);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
