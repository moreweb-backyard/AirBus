package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class DeviceListAdapter extends BaseAdapter {
    private LayoutInflater mInflator;
    private List<HashMap<String, String>> mLeDevices;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public DeviceListAdapter(Context context, List<HashMap<String, String>> list) {
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
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflator.inflate(R.layout.adapter_devicetype, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.devicetype_name);
            viewHolder.subnetid = (TextView) view.findViewById(R.id.sub);
            viewHolder.deviceid = (TextView) view.findViewById(R.id.dev);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        HashMap<String, String> map = this.mLeDevices.get(i);
        viewHolder.deviceName.setText(map.get("devicename"));
        viewHolder.subnetid.setText("subnetID:" + map.get("subnetID"));
        viewHolder.deviceid.setText("deviceID:" + map.get("deviceID"));
        return view;
    }
}
