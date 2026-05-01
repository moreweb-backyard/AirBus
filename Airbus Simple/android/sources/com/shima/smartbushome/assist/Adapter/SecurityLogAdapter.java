package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.seclogdata;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class SecurityLogAdapter extends BaseAdapter {
    public static final int select = 1648509506;
    private Context context;
    AlertView deletealter;
    private LayoutInflater inflater;
    private List<seclogdata> logarray;
    private int size;
    private int selectItem = -1;
    int delete_item_position = 0;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public SecurityLogAdapter(Context context, List<seclogdata> list) {
        this.inflater = null;
        this.context = context;
        this.logarray = list;
        this.size = list.size();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.logarray.get(i);
    }

    public void remove(Savemarco savemarco) {
        this.logarray.remove(savemarco);
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = this.mView.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewInflate = this.inflater.inflate(R.layout.adapter_securitylogitem, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.logitem_date);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.logitem_time);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.logitem_add);
        TextView textView4 = (TextView) viewInflate.findViewById(R.id.logitem_chan);
        TextView textView5 = (TextView) viewInflate.findViewById(R.id.logitem_type);
        textView.setText(this.logarray.get(i).Date);
        textView2.setText(this.logarray.get(i).Time);
        textView3.setText(this.logarray.get(i).Address);
        textView4.setText(this.logarray.get(i).Channel);
        textView5.setText(this.logarray.get(i).Type);
        this.mView.put(Integer.valueOf(i), viewInflate);
        return viewInflate;
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
    }

    public int getSelectItem() {
        return this.selectItem;
    }
}
