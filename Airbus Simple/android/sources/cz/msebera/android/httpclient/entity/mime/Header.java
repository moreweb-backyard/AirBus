package cz.msebera.android.httpclient.entity.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class Header implements Iterable<MinimalField> {
    private final List<MinimalField> fields = new LinkedList();
    private final Map<String, List<MinimalField>> fieldMap = new HashMap();

    public void addField(MinimalField minimalField) {
        if (minimalField == null) {
            return;
        }
        String lowerCase = minimalField.getName().toLowerCase(Locale.ROOT);
        List<MinimalField> linkedList = this.fieldMap.get(lowerCase);
        if (linkedList == null) {
            linkedList = new LinkedList<>();
            this.fieldMap.put(lowerCase, linkedList);
        }
        linkedList.add(minimalField);
        this.fields.add(minimalField);
    }

    public List<MinimalField> getFields() {
        return new ArrayList(this.fields);
    }

    public MinimalField getField(String str) {
        if (str == null) {
            return null;
        }
        List<MinimalField> list = this.fieldMap.get(str.toLowerCase(Locale.ROOT));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<MinimalField> getFields(String str) {
        if (str == null) {
            return null;
        }
        List<MinimalField> list = this.fieldMap.get(str.toLowerCase(Locale.ROOT));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList(list);
    }

    public int removeFields(String str) {
        if (str == null) {
            return 0;
        }
        List<MinimalField> listRemove = this.fieldMap.remove(str.toLowerCase(Locale.ROOT));
        if (listRemove == null || listRemove.isEmpty()) {
            return 0;
        }
        this.fields.removeAll(listRemove);
        return listRemove.size();
    }

    public void setField(MinimalField minimalField) {
        if (minimalField == null) {
            return;
        }
        List<MinimalField> list = this.fieldMap.get(minimalField.getName().toLowerCase(Locale.ROOT));
        if (list == null || list.isEmpty()) {
            addField(minimalField);
            return;
        }
        list.clear();
        list.add(minimalField);
        Iterator<MinimalField> it = this.fields.iterator();
        int i = -1;
        int i2 = 0;
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(minimalField.getName())) {
                it.remove();
                if (i == -1) {
                    i = i2;
                }
            }
            i2++;
        }
        this.fields.add(i, minimalField);
    }

    @Override // java.lang.Iterable
    public Iterator<MinimalField> iterator() {
        return Collections.unmodifiableList(this.fields).iterator();
    }

    public String toString() {
        return this.fields.toString();
    }
}
