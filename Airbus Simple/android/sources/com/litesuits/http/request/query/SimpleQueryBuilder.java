package com.litesuits.http.request.query;

import com.litesuits.http.data.Consts;
import com.litesuits.http.request.param.HttpCustomParam;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public class SimpleQueryBuilder extends ModelQueryBuilder {
    @Override // com.litesuits.http.request.query.ModelQueryBuilder
    protected CharSequence buildSencondaryValue(Object obj) throws IllegalAccessException, IllegalArgumentException {
        try {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if ((obj instanceof Collection) || (obj instanceof Object[])) {
                Object[] array = obj instanceof Collection ? ((Collection) obj).toArray() : (Object[]) obj;
                buildUriKey(sb, null).append(Consts.ARRAY_ECLOSING_LEFT);
                int length = array.length;
                int length2 = array.length;
                int i2 = 0;
                while (i < length2) {
                    i2++;
                    buildMoreLevelValue(sb, null, array[i], i2 == length ? "" : Consts.SECOND_LEVEL_SPLIT);
                    i++;
                }
                sb.append(Consts.ARRAY_ECLOSING_RIGHT);
            } else if (obj instanceof Map) {
                Map map = (Map) obj;
                buildUriKey(sb, null).append(Consts.KV_ECLOSING_LEFT);
                int size = map.size();
                for (Map.Entry entry : map.entrySet()) {
                    if ((entry.getKey() instanceof CharSequence) || (entry.getKey() instanceof Character)) {
                        i++;
                        buildMoreLevelValue(sb, entry.getKey().toString(), entry.getValue(), i == size ? "" : Consts.SECOND_LEVEL_SPLIT);
                    } else {
                        i++;
                        buildMoreLevelValue(sb, entry.getKey().getClass().getSimpleName(), entry.getValue(), i == size ? "" : Consts.SECOND_LEVEL_SPLIT);
                    }
                }
                sb.append(Consts.KV_ECLOSING_RIGHT);
            } else {
                ArrayList<Field> allDeclaredFields = getAllDeclaredFields(obj.getClass());
                int size2 = allDeclaredFields.size() - 1;
                while (i <= size2) {
                    Field field = allDeclaredFields.get(i);
                    field.setAccessible(true);
                    String name = field.getName();
                    Object obj2 = field.get(obj);
                    if (obj2 != null) {
                        sb.append(Consts.KV_ECLOSING_LEFT);
                        buildMoreLevelValue(sb, name, obj2, i == size2 ? "" : Consts.SECOND_LEVEL_SPLIT);
                        sb.append(Consts.KV_ECLOSING_RIGHT);
                    }
                    i++;
                }
            }
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void buildMoreLevelValue(StringBuilder sb, String str, Object obj, String str2) throws IllegalAccessException, SecurityException, IllegalArgumentException, UnsupportedEncodingException, InvocationTargetException {
        if (obj == null) {
            return;
        }
        if ((obj instanceof Number) || (obj instanceof CharSequence) || (obj instanceof Character)) {
            StringBuilder sbBuildUriKey = buildUriKey(sb, str);
            sbBuildUriKey.append(encode(obj.toString()));
            sbBuildUriKey.append(str2);
            return;
        }
        int i = 0;
        if (obj instanceof HttpCustomParam) {
            for (Method method : HttpCustomParam.class.getDeclaredMethods()) {
                if (method.getAnnotation(HttpCustomParam.CustomValueBuilder.class) != null) {
                    method.setAccessible(true);
                    Object objInvoke = method.invoke(obj, new Object[0]);
                    if (objInvoke != null) {
                        StringBuilder sbBuildUriKey2 = buildUriKey(sb, str);
                        sbBuildUriKey2.append(encode(objInvoke.toString()));
                        sbBuildUriKey2.append(str2);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        boolean z = obj instanceof Collection;
        if (z || (obj instanceof Object[])) {
            Object[] array = z ? ((Collection) obj).toArray() : (Object[]) obj;
            buildUriKey(sb, str).append(Consts.ARRAY_ECLOSING_LEFT);
            int length = array.length;
            int length2 = array.length;
            int i2 = 0;
            while (i < length2) {
                i2++;
                buildMoreLevelValue(sb, null, array[i], i2 == length ? "" : Consts.SECOND_LEVEL_SPLIT);
                i++;
            }
            sb.append(Consts.ARRAY_ECLOSING_RIGHT);
            sb.append(str2);
            return;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            buildUriKey(sb, str).append(Consts.KV_ECLOSING_LEFT);
            int size = map.size();
            for (Map.Entry entry : map.entrySet()) {
                if ((entry.getKey() instanceof CharSequence) || (entry.getKey() instanceof Character)) {
                    i++;
                    buildMoreLevelValue(sb, entry.getKey().toString(), entry.getValue(), i == size ? "" : Consts.SECOND_LEVEL_SPLIT);
                } else {
                    i++;
                    buildMoreLevelValue(sb, entry.getKey().getClass().getSimpleName(), entry.getValue(), i == size ? "" : Consts.SECOND_LEVEL_SPLIT);
                }
            }
            sb.append(Consts.KV_ECLOSING_RIGHT);
            sb.append(str2);
            return;
        }
        buildUriKey(sb, str);
        sb.append(Consts.KV_ECLOSING_LEFT);
        ArrayList<Field> allDeclaredFields = getAllDeclaredFields(obj.getClass());
        int size2 = allDeclaredFields.size() - 1;
        while (i <= size2) {
            Field field = allDeclaredFields.get(i);
            field.setAccessible(true);
            String name = field.getName();
            Object obj2 = field.get(obj);
            if (obj2 != null) {
                sb.append(Consts.KV_ECLOSING_LEFT);
                buildMoreLevelValue(sb, name, obj2, i == size2 ? "" : Consts.SECOND_LEVEL_SPLIT);
                sb.append(Consts.KV_ECLOSING_RIGHT);
            }
            i++;
        }
        sb.append(Consts.KV_ECLOSING_RIGHT);
        sb.append(str2);
    }
}
