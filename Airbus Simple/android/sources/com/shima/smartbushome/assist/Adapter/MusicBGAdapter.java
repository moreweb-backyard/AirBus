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
public class MusicBGAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList<String>() { // from class: com.shima.smartbushome.assist.Adapter.MusicBGAdapter.1
        {
            add("music_bg1");
            add("music_bg2");
            add("music_bg3");
            add("music_bg4");
            add("music_bg5");
            add("music_bg6");
            add("music_bg7");
            add("music_bg8");
            add("music_bg9");
            add("music_bg10");
        }
    };
    private int size = 10;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MusicBGAdapter(Context context) {
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
        ((ImageView) viewInflate.findViewById(R.id.imageView6)).setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.stringarray.get(i))));
        this.mView.put(Integer.valueOf(i), viewInflate);
        return viewInflate;
    }

    public int getResourdIdByResourdName(String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }
}
