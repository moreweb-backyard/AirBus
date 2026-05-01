package com.yanzhenjie.andserver.website;

import android.text.TextUtils;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.handler.StorageRequestHandler;
import com.yanzhenjie.andserver.util.StorageWrapper;
import java.io.File;
import java.util.Map;

/* loaded from: classes.dex */
public class StorageWebsite extends BasicWebsite {
    private String mRootPath;
    private StorageWrapper mStorageWrapper;

    public StorageWebsite(String str) {
        super(str);
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("The RootPath can not be null.");
        }
        this.mRootPath = trimSlash(str);
        this.mStorageWrapper = new StorageWrapper();
    }

    @Override // com.yanzhenjie.andserver.website.WebSite
    public void onRegister(Map<String, RequestHandler> map) {
        StorageRequestHandler storageRequestHandler = new StorageRequestHandler(this.INDEX_HTML);
        map.put("", storageRequestHandler);
        map.put(this.mRootPath, storageRequestHandler);
        map.put(this.mRootPath + File.separator, storageRequestHandler);
        map.put(this.mRootPath + File.separator + this.INDEX_HTML, storageRequestHandler);
        for (String str : this.mStorageWrapper.scanFile(getHttpPath(this.mRootPath))) {
            map.put(str, new StorageRequestHandler(str));
        }
    }
}
