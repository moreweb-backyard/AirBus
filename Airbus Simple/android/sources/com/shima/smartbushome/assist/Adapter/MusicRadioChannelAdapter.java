package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MusicRadioChannelAdapter extends BaseAdapter {
    public static final int select = 2097325033;
    private LayoutInflater mInflator;
    private List<HashMap<String, String>> mLeDevices;
    private int selectItem = -1;
    private String[] colorarray = {"5f000000", "5f000000"};

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MusicRadioChannelAdapter(Context context, List<HashMap<String, String>> list) {
        this.mLeDevices = new ArrayList();
        this.mLeDevices = list;
        this.mInflator = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void addDevice(HashMap<String, String> map) {
        if (this.mLeDevices.contains(map)) {
            return;
        }
        this.mLeDevices.add(map);
    }

    public HashMap<String, String> getDevice(int i) {
        return this.mLeDevices.get(i);
    }

    public void clear() {
        this.mLeDevices.clear();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mLeDevices.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mLeDevices.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        radioViewHolder radioviewholder;
        if (view == null) {
            view = this.mInflator.inflate(R.layout.music_radio_items, (ViewGroup) null);
            radioviewholder = new radioViewHolder();
            radioviewholder.channelName = (TextView) view.findViewById(R.id.tvradioname);
            radioviewholder.channelvalue = (TextView) view.findViewById(R.id.radiovalue);
            view.setTag(radioviewholder);
        } else {
            radioviewholder = (radioViewHolder) view.getTag();
        }
        if (i == this.selectItem) {
            view.setBackgroundColor(2097325033);
        } else {
            view.setBackgroundColor(ToColor(this.colorarray[i % 2]));
        }
        HashMap<String, String> map = this.mLeDevices.get(i);
        String str = map.get("channelname");
        String str2 = map.get("channelvalue");
        String str3 = str2.substring(0, str2.length() - 1) + FileUtil.FILE_EXTENSION_SEPARATOR + str2.substring(str2.length() - 1, str2.length()) + " MHZ";
        radioviewholder.channelName.setText(str);
        radioviewholder.channelvalue.setText(str3);
        return view;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
    }

    public int getSelectItem() {
        return this.selectItem;
    }
}
