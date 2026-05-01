package voice.decoder;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class MyArrayList<T> extends ArrayList<T> {
    MyArrayList(int i) {
        super(i);
    }

    MyArrayList() {
    }

    public T first() {
        return get(0);
    }

    public T last() {
        return get(size() - 1);
    }

    public void pollLast() {
        remove(size() - 1);
    }

    public T pollFirst() {
        return remove(0);
    }
}
