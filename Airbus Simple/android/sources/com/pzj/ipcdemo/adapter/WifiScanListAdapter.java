package com.pzj.ipcdemo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.bean.WifiScanBean;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class WifiScanListAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private ArrayList<WifiScanBean> list = new ArrayList<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public WifiScanListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) throws Resources.NotFoundException {
        if (view == null) {
            view = this.inflater.inflate(R.layout.settingwifi_list_item, (ViewGroup) null);
            this.holder = new ViewHolder();
            this.holder.ssid = (TextView) view.findViewById(R.id.ssid);
            this.holder.safe = (TextView) view.findViewById(R.id.wifi_scan_listitem_tv_safe);
            this.holder.signal = (TextView) view.findViewById(R.id.wifi_scan_listitem_tv_signal_strong);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        if (this.list.size() == 0) {
            view.setBackgroundResource(R.drawable.listitem_one_pressed_selector);
        } else if (i == 0) {
            view.setBackgroundResource(R.drawable.listitem_pressed_top_corner_selector);
        } else if (i == this.list.size() - 1) {
            view.setBackgroundResource(R.drawable.listitem_pressed_bottom_corner_selector);
        } else {
            view.setBackgroundResource(R.drawable.listitem_pressed_selector);
        }
        WifiScanBean wifiScanBean = this.list.get(i);
        this.holder.ssid.setText(wifiScanBean.getSsid());
        this.holder.signal.setText(wifiScanBean.getDbm0() + "%");
        String string = "";
        switch (wifiScanBean.getSecurity()) {
            case 0:
                string = this.context.getResources().getString(R.string.wifi_no_safe);
                break;
            case 1:
                string = "WEP";
                break;
            case 2:
                string = "WPA_PSK(AES)";
                break;
            case 3:
                string = "WPA_PSK(TKIP)";
                break;
            case 4:
                string = "WPA2_PSK(AES)";
                break;
            case 5:
                string = "WPA2_PSK(TKIP)";
                break;
        }
        this.holder.safe.setText(string);
        return view;
    }

    public void addWifiScan(WifiScanBean wifiScanBean) {
        this.list.add(wifiScanBean);
    }

    public void clearWifi() {
        this.list.clear();
    }

    public WifiScanBean getWifiScan(int i) {
        return this.list.get(i);
    }

    public ArrayList<WifiScanBean> getWifiScanBeans() {
        return this.list;
    }

    private class ViewHolder {
        TextView safe;
        TextView signal;
        TextView ssid;

        private ViewHolder() {
        }
    }
}
