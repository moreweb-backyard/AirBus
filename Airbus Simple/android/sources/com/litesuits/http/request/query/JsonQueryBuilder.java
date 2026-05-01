package com.litesuits.http.request.query;

import com.litesuits.http.data.Json;

/* loaded from: classes.dex */
public class JsonQueryBuilder extends ModelQueryBuilder {
    @Override // com.litesuits.http.request.query.ModelQueryBuilder
    protected CharSequence buildSencondaryValue(Object obj) {
        try {
            return Json.get().toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
