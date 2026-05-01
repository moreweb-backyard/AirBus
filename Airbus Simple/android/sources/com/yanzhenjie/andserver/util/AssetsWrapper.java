package com.yanzhenjie.andserver.util;

import android.content.res.AssetManager;
import android.text.TextUtils;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AssetsWrapper {
    private AssetManager mAssetManager;

    public AssetsWrapper(AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    public InputStream getInputStream(String str) {
        try {
            return this.mAssetManager.open(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public boolean isFile(String str) {
        return getInputStream(str) != null;
    }

    public String[] fileList(String str) {
        try {
            return this.mAssetManager.list(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public List<String> scanFile(String str) {
        ArrayList arrayList = new ArrayList(2);
        if (isFile(str)) {
            arrayList.add(str);
        } else {
            String[] strArrFileList = fileList(str);
            if (strArrFileList != null && strArrFileList.length > 0) {
                for (String str2 : strArrFileList) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TextUtils.isEmpty(str) ? "" : str + File.separator);
                    sb.append(str2);
                    String string = sb.toString();
                    if (isFile(string)) {
                        arrayList.add(string);
                    } else {
                        List<String> listScanFile = scanFile(string);
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
