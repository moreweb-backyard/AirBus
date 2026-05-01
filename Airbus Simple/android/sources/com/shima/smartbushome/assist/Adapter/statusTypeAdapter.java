package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class statusTypeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList<String>() { // from class: com.shima.smartbushome.assist.Adapter.statusTypeAdapter.1
        {
            add("status1");
            add("light_type3_on");
            add("status2");
            add("status3");
            add("status4");
            add("status5");
        }
    };
    private List<String> namearray = new ArrayList<String>() { // from class: com.shima.smartbushome.assist.Adapter.statusTypeAdapter.2
        {
            add("Light");
            add("RGB LED");
            add("Temperature(9in1 or other)");
            add("HVAC");
            add("Other");
            add("4T device");
        }
    };
    private int size = 6;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public statusTypeAdapter(Context context) {
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
        if (view != null) {
            return view;
        }
        View viewInflate = this.inflater.inflate(R.layout.adapter_light_type_item, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.textView13);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView9);
        textView.setText(this.namearray.get(i));
        imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, this.stringarray.get(i))));
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
