package com.yanzhenjie.andserver.website;

import android.content.res.AssetManager;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.handler.AssetsRequestHandler;
import com.yanzhenjie.andserver.util.AssetsWrapper;
import java.io.File;
import java.util.Map;

/* loaded from: classes.dex */
public class AssetsWebsite extends BasicWebsite {
    private AssetsWrapper mAssetsWrapper;
    private String mRootPath;

    public AssetsWebsite(AssetManager assetManager, String str) {
        super(str);
        this.mRootPath = trimSlash(str == null ? "" : str);
        this.mAssetsWrapper = new AssetsWrapper(assetManager);
    }

    @Override // com.yanzhenjie.andserver.website.WebSite
    public void onRegister(Map<String, RequestHandler> map) {
        AssetsRequestHandler assetsRequestHandler = new AssetsRequestHandler(this.mAssetsWrapper, this.INDEX_HTML);
        map.put("", assetsRequestHandler);
        map.put(this.mRootPath, assetsRequestHandler);
        map.put(this.mRootPath + File.separator, assetsRequestHandler);
        map.put(this.mRootPath + File.separator + this.INDEX_HTML, assetsRequestHandler);
        for (String str : this.mAssetsWrapper.scanFile(this.mRootPath)) {
            map.put(str, new AssetsRequestHandler(this.mAssetsWrapper, str));
        }
    }
}
