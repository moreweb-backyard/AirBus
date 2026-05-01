package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MusicOptionAdapter extends BaseAdapter {
    private Context context;
    private List<Drawable> drawarray;
    private LayoutInflater inflater;
    private List<String> stringarray;
    private String[] colorarray = {"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
    private int size = 5;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MusicOptionAdapter(final Context context, List<String> list) {
        this.inflater = null;
        this.context = context;
        this.stringarray = list;
        this.drawarray = new ArrayList<Drawable>() { // from class: com.shima.smartbushome.assist.Adapter.MusicOptionAdapter.1
            {
                add(context.getResources().getDrawable(R.drawable.musicmenu_album));
                add(context.getResources().getDrawable(R.drawable.musicmenu_allsong));
                add(context.getResources().getDrawable(R.drawable.musicmenu_like));
                add(context.getResources().getDrawable(R.drawable.musicmenu_more));
                add(context.getResources().getDrawable(R.drawable.musicmenu_folder));
            }
        };
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
        View viewInflate = this.inflater.inflate(R.layout.music_folderitem, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.txtName);
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.ad_musicop_img);
        textView.setText(this.stringarray.get(i));
        imageView.setImageDrawable(this.drawarray.get(i));
        this.mView.put(Integer.valueOf(i), viewInflate);
        return viewInflate;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
