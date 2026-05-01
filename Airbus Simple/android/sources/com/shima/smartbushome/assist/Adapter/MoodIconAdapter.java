package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.shima.smartbushome.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MoodIconAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList<String>() { // from class: com.shima.smartbushome.assist.Adapter.MoodIconAdapter.1
        {
            add("mood_icon1");
            add("mood_icon2");
            add("mood_icon3");
            add("mood_icon4");
            add("mood_icon5");
            add("mood_icon6");
            add("mood_icon7");
            add("mood_icon8");
            add("mood_icon9");
            add("mood_icon10");
        }
    };
    private int size = 10;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MoodIconAdapter(Context context) {
        this.inflater = null;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.stringarray.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = this.mView.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewInflate = this.inflater.inflate(R.layout.mood_icon_select_item, (ViewGroup) null);
        ((ImageView) viewInflate.findViewById(R.id.imageView6)).setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, this.stringarray.get(i))));
        this.mView.put(Integer.valueOf(i), viewInflate);
        return viewInflate;
    }

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }
}
