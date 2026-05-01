package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savemusic;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MusicAdapter extends BaseAdapter {
    private List<Savemusic> buttonarray;
    private Context context;
    private LayoutInflater inflater;
    private HashMap<Integer, View> mView = new HashMap<>();
    private int size;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MusicAdapter(Context context, List<Savemusic> list) {
        this.buttonarray = new ArrayList();
        this.inflater = null;
        this.context = context;
        this.size = list.size();
        this.buttonarray = list;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.buttonarray.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = this.mView.get(Integer.valueOf(i));
        if (viewInflate == null) {
            viewInflate = this.inflater.inflate(R.layout.marcobutton, (ViewGroup) null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.textView69);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView18);
            if (this.buttonarray.get(i).music_remark.trim().equals("")) {
                textView.setText(this.buttonarray.get(i).subnetID + "_" + this.buttonarray.get(i).deviceID);
            } else {
                textView.setText(this.buttonarray.get(i).music_remark);
            }
            imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "music")));
            viewInflate.setBackground(this.context.getResources().getDrawable(R.drawable.control_back_10));
            this.mView.put(Integer.valueOf(i), viewInflate);
        }
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

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
