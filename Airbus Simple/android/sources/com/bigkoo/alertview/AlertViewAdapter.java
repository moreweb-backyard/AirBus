package com.bigkoo.alertview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/* loaded from: classes.dex */
public class AlertViewAdapter extends BaseAdapter {
    private List<String> mDatas;
    private List<String> mDestructive;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public AlertViewAdapter(List<String> list, List<String> list2) {
        this.mDatas = list;
        this.mDestructive = list2;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDatas.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mDatas.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holderCreatHolder;
        String str = this.mDatas.get(i);
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alertbutton, (ViewGroup) null);
            holderCreatHolder = creatHolder(view);
            view.setTag(holderCreatHolder);
        } else {
            holderCreatHolder = (Holder) view.getTag();
        }
        holderCreatHolder.UpdateUI(viewGroup.getContext(), str, i);
        return view;
    }

    public Holder creatHolder(View view) {
        return new Holder(view);
    }

    class Holder {
        private TextView tvAlert;

        public Holder(View view) {
            this.tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }

        public void UpdateUI(Context context, String str, int i) {
            this.tvAlert.setText(str);
            if (AlertViewAdapter.this.mDestructive != null && AlertViewAdapter.this.mDestructive.contains(str)) {
                this.tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_destructive));
            } else {
                this.tvAlert.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_others));
            }
        }
    }
}
