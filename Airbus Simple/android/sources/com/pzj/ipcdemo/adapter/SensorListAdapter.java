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
import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes.dex */
public class SensorListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public ArrayList<Map<String, String>> listItems;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public SensorListAdapter(Context context, ArrayList<Map<String, String>> arrayList) {
        this.inflater = null;
        this.listItems = new ArrayList<>();
        this.context = context;
        this.listItems = arrayList;
        this.inflater = LayoutInflater.from(context);
    }

    class sensorlist {
        public ImageView getnext;
        public TextView sensordis;
        public TextView sensorid;
        public TextView sensorname;
        public ImageView sensortype;

        sensorlist() {
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.listItems.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) throws NumberFormatException {
        View viewInflate;
        sensorlist sensorlistVar;
        if (view == null) {
            sensorlistVar = new sensorlist();
            viewInflate = this.inflater.inflate(R.layout.sensorlist_item_sensortype_item, (ViewGroup) null);
            sensorlistVar.sensorname = (TextView) viewInflate.findViewById(R.id.sensor_name);
            sensorlistVar.sensortype = (ImageView) viewInflate.findViewById(R.id.sensor_type);
            sensorlistVar.sensordis = (TextView) viewInflate.findViewById(R.id.sensor_dic);
            sensorlistVar.getnext = (ImageView) viewInflate.findViewById(R.id.imageView1);
            viewInflate.setTag(sensorlistVar);
        } else {
            viewInflate = view;
            sensorlistVar = (sensorlist) view.getTag();
        }
        int i2 = Integer.parseInt(this.listItems.get(i).get("sensortype"));
        Log.i("info", "aaa传感器类型aaaaaaaa" + i2);
        if (i2 == 1) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_door));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_door_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensorlist_item_magnetic);
        } else if (i2 == 2) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_infrared));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_infrerad_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensorlist_item_infrared);
        } else if (i2 == 3) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_smoke));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_smoke_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensorlist_item_smoke);
        } else if (i2 == 4) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_gas));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_gas_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensorlist_item_gas);
        } else if (i2 == 7) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_remote));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_control_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensorlist_item_control);
        } else if (i2 == 8) {
            sensorlistVar.sensorname.setText(this.context.getString(R.string.sensor_type_siren));
            sensorlistVar.sensordis.setText(this.context.getString(R.string.add_sensor_siren_desc));
            sensorlistVar.sensortype.setBackgroundResource(R.drawable.sensor_siren_icon);
        }
        return viewInflate;
    }
}
