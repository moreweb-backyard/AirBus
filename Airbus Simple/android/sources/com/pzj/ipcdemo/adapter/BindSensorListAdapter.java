package com.pzj.ipcdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class BindSensorListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Map<String, Object>> mylist = new ArrayList<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public BindSensorListAdapter(Context context) {
        this.mInflater = null;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mylist.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate;
        sensorlist sensorlistVar;
        if (view == null) {
            sensorlistVar = new sensorlist();
            viewInflate = this.mInflater.inflate(R.layout.sensoradapterlayout, (ViewGroup) null);
            sensorlistVar.sensorname = (TextView) viewInflate.findViewById(R.id.sensorname);
            sensorlistVar.sensortypeimageview = (ImageView) viewInflate.findViewById(R.id.sensor_type);
            sensorlistVar.getnext = (ImageView) viewInflate.findViewById(R.id.imageView1);
            sensorlistVar.sensornew = (ImageView) viewInflate.findViewById(R.id.sensor_new);
            viewInflate.setTag(sensorlistVar);
        } else {
            viewInflate = view;
            sensorlistVar = (sensorlist) view.getTag();
        }
        Map<String, Object> map = this.mylist.get(i);
        String strTrim = map.get("sensorname").toString().trim();
        if (strTrim.length() != 0 && !strTrim.equals("")) {
            sensorlistVar.sensorname.setText(strTrim);
            Log.i("info", "传感器名称不为空。");
        }
        ((Integer) map.get(DatabaseUtil.KEY_ID)).intValue();
        int iIntValue = ((Integer) map.get("sensorid1")).intValue();
        int iIntValue2 = ((Integer) map.get("sensorid2")).intValue();
        int iIntValue3 = ((Integer) map.get("sensorid3")).intValue();
        int iIntValue4 = ((Integer) map.get("sensortype")).intValue();
        if (((Integer) map.get("sensornew")).intValue() == 1) {
            sensorlistVar.sensornew.setVisibility(0);
        } else {
            sensorlistVar.sensornew.setVisibility(4);
        }
        String str = changeTo16(iIntValue) + changeTo16(iIntValue2) + changeTo16(iIntValue3);
        if (iIntValue4 == 1) {
            this.context.getString(R.string.sensor_type_door);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensorlist_item_magnetic);
        } else if (iIntValue4 == 2) {
            this.context.getString(R.string.sensor_type_infrared);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensorlist_item_infrared);
        } else if (iIntValue4 == 3) {
            this.context.getString(R.string.sensor_type_smoke);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensorlist_item_smoke);
        } else if (iIntValue4 == 4) {
            this.context.getString(R.string.sensor_type_gas);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensorlist_item_gas);
        } else if (iIntValue4 == 7) {
            this.context.getString(R.string.sensor_type_remote);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensorlist_item_control);
        } else if (iIntValue4 == 8) {
            this.context.getString(R.string.sensor_type_siren);
            sensorlistVar.sensortypeimageview.setBackgroundResource(R.drawable.sensor_siren_icon);
        } else if (iIntValue4 == 10) {
            this.context.getString(R.string.sensor_type_camera);
        } else if (iIntValue4 == 11) {
            this.context.getString(R.string.sensor_type_curtain);
        }
        return viewInflate;
    }

    public void addSensorToList(int i, int i2, int i3, int i4, String str, int i5, int i6) {
        Log.i("info", "add sensor");
        HashMap map = new HashMap();
        map.put("sensorid1", Integer.valueOf(i));
        map.put("sensorid2", Integer.valueOf(i2));
        map.put("sensorid3", Integer.valueOf(i3));
        map.put("sensortype", Integer.valueOf(i4));
        map.put("sensorname", str);
        map.put("sensornew", Integer.valueOf(i5));
        map.put(DatabaseUtil.KEY_ID, Integer.valueOf(i6));
        synchronized (this) {
            this.mylist.add(map);
        }
    }

    public void changeSensorName(String str, int i) {
        this.mylist.get(i).put("sensorname", str);
        Log.i("info", "changeSensorName:" + str);
    }

    public void changeSensorNew(int i) {
        this.mylist.get(i).put("sensornew", -1);
    }

    public void cleanItem() {
        this.mylist.clear();
    }

    public void removeMylist(int i) {
        this.mylist.remove(i);
    }

    public String changeTo16(int i) {
        String upperCase = Integer.toHexString(i).toUpperCase();
        if (i >= 10) {
            return upperCase;
        }
        return "0" + upperCase;
    }

    class sensorlist {
        public ImageView getnext;
        public TextView sensorname;
        public ImageView sensornew;
        public ImageView sensortypeimageview;

        sensorlist() {
        }
    }
}
