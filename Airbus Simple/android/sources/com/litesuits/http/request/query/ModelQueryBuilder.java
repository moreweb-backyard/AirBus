package com.litesuits.http.request.query;

import com.litesuits.http.data.Consts;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.request.param.HttpCustomParam;
import com.litesuits.http.request.param.HttpParam;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.litesuits.http.request.param.NonHttpParam;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class ModelQueryBuilder {
    protected String charSet = "UTF-8";

    protected abstract CharSequence buildSencondaryValue(Object obj);

    public LinkedList<NameValuePair> buildPrimaryPairSafely(HttpParamModel httpParamModel) {
        try {
            return buildPrimaryPair(httpParamModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList<NameValuePair> buildPrimaryPair(HttpParamModel httpParamModel) throws IllegalAccessException, SecurityException, IllegalArgumentException, UnsupportedEncodingException, InvocationTargetException {
        LinkedHashMap<String, String> linkedHashMapBuildPrimaryMap = buildPrimaryMap(httpParamModel);
        LinkedList<NameValuePair> linkedList = new LinkedList<>();
        for (Map.Entry<String, String> entry : linkedHashMapBuildPrimaryMap.entrySet()) {
            linkedList.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        return linkedList;
    }

    public LinkedHashMap<String, String> buildPrimaryMap(HttpParamModel httpParamModel) throws IllegalAccessException, SecurityException, IllegalArgumentException, UnsupportedEncodingException, InvocationTargetException {
        if (httpParamModel == null) {
            return null;
        }
        ArrayList<Field> allDeclaredFields = getAllDeclaredFields(httpParamModel.getClass());
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(allDeclaredFields.size());
        int size = allDeclaredFields.size();
        for (int i = 0; i < size; i++) {
            Field field = allDeclaredFields.get(i);
            field.setAccessible(true);
            HttpParam httpParam = (HttpParam) field.getAnnotation(HttpParam.class);
            String strValue = httpParam != null ? httpParam.value() : field.getName();
            Object obj = field.get(httpParamModel);
            if (obj != null) {
                if (isPrimitive(obj)) {
                    linkedHashMap.put(strValue, obj.toString());
                } else if (obj instanceof HttpCustomParam) {
                    Method[] declaredMethods = HttpCustomParam.class.getDeclaredMethods();
                    int length = declaredMethods.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 < length) {
                            Method method = declaredMethods[i2];
                            if (method.getAnnotation(HttpCustomParam.CustomValueBuilder.class) != null) {
                                method.setAccessible(true);
                                Object objInvoke = method.invoke(obj, new Object[0]);
                                if (objInvoke != null) {
                                    linkedHashMap.put(strValue, objInvoke.toString());
                                }
                            } else {
                                i2++;
                            }
                        }
                    }
                } else {
                    CharSequence charSequenceBuildSencondaryValue = buildSencondaryValue(obj);
                    if (charSequenceBuildSencondaryValue != null) {
                        linkedHashMap.put(strValue, charSequenceBuildSencondaryValue.toString());
                    }
                }
            }
        }
        return linkedHashMap;
    }

    protected StringBuilder buildUriKey(StringBuilder sb, String str) throws UnsupportedEncodingException {
        if (str != null) {
            sb.append(encode(str));
            sb.append(Consts.EQUALS);
        }
        return sb;
    }

    public String decode(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, this.charSet);
    }

    public String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, this.charSet);
    }

    protected static boolean isInvalidField(Field field) {
        return (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) || field.getAnnotation(NonHttpParam.class) != null || field.isSynthetic();
    }

    protected static boolean isPrimitive(Object obj) {
        return (obj instanceof CharSequence) || (obj instanceof Number) || (obj instanceof Boolean) || (obj instanceof Character);
    }

    protected static ArrayList<Field> getAllDeclaredFields(Class<?> cls) {
        ArrayList<Field> arrayList = new ArrayList<>();
        while (cls != null && cls != HttpRichParamModel.class && cls != Object.class) {
            for (Field field : cls.getDeclaredFields()) {
                if (!isInvalidField(field)) {
                    arrayList.add(field);
                }
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }
}
