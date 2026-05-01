package com.yanzhenjie.andserver.website;

import com.yanzhenjie.andserver.RequestHandler;
import java.util.Map;

/* loaded from: classes.dex */
public interface WebSite {
    void onRegister(Map<String, RequestHandler> map);
}
