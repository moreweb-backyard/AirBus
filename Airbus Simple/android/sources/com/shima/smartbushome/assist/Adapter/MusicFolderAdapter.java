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
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MusicFolderAdapter extends BaseAdapter {
    public static final int select = 2097325033;
    private int adapterType;
    private Context context;
    private LayoutInflater inflater;
    private List<String> namearray;
    private int size;
    private int selectItem = -1;
    private String[] colorarray = {"5f000000", "5f000000"};
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MusicFolderAdapter(Context context, List<String> list, int i) {
        this.inflater = null;
        this.adapterType = 0;
        this.context = context;
        this.namearray = list;
        this.size = this.namearray.size();
        this.adapterType = i;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.namearray.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = this.mView.get(Integer.valueOf(i));
        if (viewInflate == null) {
            viewInflate = this.inflater.inflate(R.layout.adapter_music_file, (ViewGroup) null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.adapter_musicfile_name);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.adapter_musicfile_img);
            textView.setText(this.namearray.get(i));
            if (this.adapterType == 1) {
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.musicmenu_folder));
            } else if (this.adapterType == 2) {
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.mp3icon));
            }
            this.mView.put(Integer.valueOf(i), viewInflate);
        }
        if (i == this.selectItem) {
            viewInflate.setBackgroundColor(2097325033);
        } else {
            viewInflate.setBackgroundColor(ToColor(this.colorarray[i % 2]));
        }
        return viewInflate;
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
    }

    public int getSelectItem() {
        return this.selectItem;
    }

    public void setNextorBack(String str) {
        if (str.equals("next")) {
            this.selectItem++;
            if (this.selectItem > this.size - 1) {
                this.selectItem = 0;
                return;
            }
            return;
        }
        if (str.equals("back")) {
            this.selectItem--;
            if (this.selectItem < 0) {
                this.selectItem = this.size - 1;
            }
        }
    }

    public String getselectSongname() {
        return this.namearray.get(this.selectItem);
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
