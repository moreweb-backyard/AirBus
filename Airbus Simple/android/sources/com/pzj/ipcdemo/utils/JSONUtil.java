package com.pzj.ipcdemo.utils;

import android.content.ContentValues;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class JSONUtil {
    private static final String TAG = "JSONUtil";
    private static Gson gson = new Gson();

    private JSONUtil() {
    }

    public static String getHeadContext(String str, String str2) {
        try {
            return new JSONObject(str).get(str2).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String objectToJson(T t) {
        if (t instanceof String) {
            return t.toString();
        }
        return gson.toJson(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T jsonToObject(String str, Class<T> cls) {
        return cls == String.class ? str : (T) gson.fromJson(str, (Class) cls);
    }

    public static <T> String listToJson(List<T> list) {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            try {
                try {
                    try {
                        jSONArray.put(new JSONObject(objectToJson(list.get(i))));
                    } catch (JSONException e) {
                        e = e;
                        e.printStackTrace();
                        return jSONArray.toString();
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (JSONException e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        return jSONArray.toString();
    }

    public static <T> String arrayToJson(T[] tArr) {
        JSONArray jSONArray = new JSONArray();
        for (T t : tArr) {
            try {
                try {
                    try {
                        jSONArray.put(new JSONObject(objectToJson(t)));
                    } catch (JSONException e) {
                        e = e;
                        e.printStackTrace();
                        return jSONArray.toString();
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (JSONException e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        return jSONArray.toString();
    }

    public static <T> T getJsonObjectValue(String str, String str2, Class<T> cls) {
        try {
            return (T) getJsonObjectValue(new JSONObject(str), str2, cls);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T getJsonObjectValue(JSONObject jSONObject, String str, Class<T> cls) throws JSONException {
        Object objValueOf;
        T t = null;
        try {
            if (cls == Integer.class) {
                objValueOf = Integer.valueOf(jSONObject.getInt(str));
            } else if (cls == Boolean.class) {
                objValueOf = Boolean.valueOf(jSONObject.getBoolean(str));
            } else if (cls == String.class) {
                objValueOf = String.valueOf(jSONObject.getString(str));
            } else if (cls == Double.class) {
                objValueOf = Double.valueOf(jSONObject.getDouble(str));
            } else if (cls == JSONObject.class) {
                objValueOf = jSONObject.getJSONObject(str);
            } else if (cls == JSONArray.class) {
                objValueOf = jSONObject.getJSONArray(str);
            } else {
                if (cls != Long.class) {
                    return null;
                }
                objValueOf = Long.valueOf(jSONObject.getLong(str));
            }
            t = objValueOf;
            return t;
        } catch (JSONException e) {
            e.printStackTrace();
            return t;
        }
    }

    public static ContentValues jsonToContentValues(String str) throws JSONException {
        ContentValues contentValues = new ContentValues();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String string = itKeys.next().toString();
                Object obj = jSONObject.get(string);
                String string2 = obj.toString();
                if (obj instanceof String) {
                    contentValues.put(string, string2);
                } else if (obj instanceof Integer) {
                    contentValues.put(string, Integer.valueOf(string2));
                } else if (obj instanceof Long) {
                    contentValues.put(string, Long.valueOf(string2));
                } else if (obj instanceof Double) {
                    contentValues.put(string, Double.valueOf(string2));
                } else if (obj instanceof Float) {
                    contentValues.put(string, Float.valueOf(string2));
                } else if (obj instanceof Boolean) {
                    contentValues.put(string, Boolean.valueOf(string2));
                }
            }
            return contentValues;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error("Json字符串不合法：" + str);
        }
    }

    public static String gsonString(Object obj) {
        if (gson != null) {
            return gson.toJson(obj);
        }
        return null;
    }

    public static <T> T gsonToBean(String str, Class<T> cls) {
        if (gson != null) {
            return (T) gson.fromJson(str, (Class) cls);
        }
        return null;
    }

    public static <T> List<T> gsonToList(String str, Class<T> cls) {
        if (gson != null) {
            return (List) gson.fromJson(str, new TypeToken<List<T>>() { // from class: com.pzj.ipcdemo.utils.JSONUtil.1
            }.getType());
        }
        return null;
    }

    public static <T> List<T> jsonToList(String str, Class<T> cls) {
        Gson gson2 = new Gson();
        ArrayList arrayList = new ArrayList();
        try {
            Iterator<JsonElement> it = new JsonParser().parse(str).getAsJsonArray().iterator();
            while (it.hasNext()) {
                arrayList.add(gson2.fromJson(it.next(), (Class) cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static <T> List<Map<String, T>> gsonToListMaps(String str) {
        if (gson != null) {
            return (List) gson.fromJson(str, new TypeToken<List<Map<String, T>>>() { // from class: com.pzj.ipcdemo.utils.JSONUtil.2
            }.getType());
        }
        return null;
    }

    public static <T> Map<String, T> gsonToMaps(String str) {
        if (gson != null) {
            return (Map) gson.fromJson(str, new TypeToken<Map<String, T>>() { // from class: com.pzj.ipcdemo.utils.JSONUtil.3
            }.getType());
        }
        return null;
    }

    public static String getNoteJsonString(String str, String str2) throws JsonSyntaxException {
        if (TextUtils.isEmpty(str)) {
            throw new RuntimeException("json字符串");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new RuntimeException("note标签不能为空");
        }
        JsonElement jsonElement = new JsonParser().parse(str);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return jsonElement.getAsJsonObject().get(str2).toString();
    }

    public static <T> List<T> parserJsonToArrayBeans(String str, String str2, Class<T> cls) {
        return parserJsonToArrayBeans(getNoteJsonString(str, str2), cls);
    }

    public static <T> List<T> parserJsonToArrayBeans(String str, Class<T> cls) throws JsonSyntaxException {
        if (TextUtils.isEmpty(str)) {
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(str);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        if (!jsonElement.isJsonArray()) {
            throw new RuntimeException("json字符不是一个数组对象集合");
        }
        JsonArray asJsonArray = jsonElement.getAsJsonArray();
        ArrayList arrayList = new ArrayList();
        Iterator<JsonElement> it = asJsonArray.iterator();
        while (it.hasNext()) {
            arrayList.add(new Gson().fromJson(it.next(), (Class) cls));
        }
        return arrayList;
    }

    public static <T> T parserJsonToArrayBean(String str, Class<T> cls) throws JsonSyntaxException {
        if (TextUtils.isEmpty(str)) {
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(str);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("json字符串为空");
        }
        if (!jsonElement.isJsonObject()) {
            throw new RuntimeException("json不是一个对象");
        }
        return (T) new Gson().fromJson(jsonElement, (Class) cls);
    }

    public static <T> T parserJsonToArrayBean(String str, String str2, Class<T> cls) {
        return (T) parserJsonToArrayBean(getNoteJsonString(str, str2), cls);
    }

    public static String toJsonString(Object obj) {
        if (obj != null) {
            return new Gson().toJson(obj);
        }
        throw new RuntimeException("对象不能为空");
    }
}
