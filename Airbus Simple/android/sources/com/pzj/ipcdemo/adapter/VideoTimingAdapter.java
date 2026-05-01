package com.pzj.ipcdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pzj.ipcdemo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class VideoTimingAdapter extends BaseAdapter {
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;
    public ArrayList<Map<Integer, Integer>> sdtiming = new ArrayList<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public VideoTimingAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.sdtiming.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.inflater.inflate(R.layout.timing_video_item, (ViewGroup) null);
            this.holder = new ViewHolder();
            this.holder.tv_timing_time = (TextView) view.findViewById(R.id.tv_timing_time);
            this.holder.tv_timing_week = (TextView) view.findViewById(R.id.tv_timing_week);
            view.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) view.getTag();
        }
        Map<Integer, Integer> map = this.sdtiming.get(i);
        int iIntValue = map.entrySet().iterator().next().getValue().intValue();
        this.holder.tv_timing_week.setText(getWeekPlan(iIntValue));
        this.holder.tv_timing_time.setText(getTime(iIntValue & 2047) + "-" + getTime((iIntValue >> 12) & 2047));
        this.sdtiming.get(i).put(Integer.valueOf(map.entrySet().iterator().next().getKey().intValue()), Integer.valueOf(map.entrySet().iterator().next().getValue().intValue()));
        return view;
    }

    private class ViewHolder {
        TextView tv_timing_time;
        TextView tv_timing_week;

        private ViewHolder() {
        }
    }

    private String getWeekPlan(int i) {
        String str = "";
        for (int i2 = 24; i2 < 31; i2++) {
            if (((i >> i2) & 1) == 1) {
                switch (i2) {
                    case 24:
                        str = str + this.mContext.getResources().getString(R.string.plug_seven) + " ";
                        break;
                    case 25:
                        str = str + this.mContext.getResources().getString(R.string.plug_one) + " ";
                        break;
                    case 26:
                        str = str + this.mContext.getResources().getString(R.string.plug_two) + " ";
                        break;
                    case 27:
                        str = str + this.mContext.getResources().getString(R.string.plug_three) + " ";
                        break;
                    case 28:
                        str = str + this.mContext.getResources().getString(R.string.plug_four) + " ";
                        break;
                    case 29:
                        str = str + this.mContext.getResources().getString(R.string.plug_five) + " ";
                        break;
                    case 30:
                        str = str + this.mContext.getResources().getString(R.string.plug_six) + " ";
                        break;
                }
            }
        }
        return str;
    }

    private String getTime(int i) {
        if (i < 60) {
            if (i < 10) {
                return "00:0" + i;
            }
            return "00:" + i;
        }
        int i2 = i / 60;
        int i3 = i - (i2 * 60);
        if (i2 < 10 && i3 < 10) {
            return "0" + i2 + ":0" + i3;
        }
        if (i2 > 9 && i3 < 10) {
            return i2 + ":0" + i3;
        }
        if (i2 < 10 && i3 > 9) {
            return "0" + i2 + ":" + i3;
        }
        return i2 + ":" + i3;
    }

    public void addPlan(int i, int i2) {
        HashMap map = new HashMap();
        map.put(Integer.valueOf(i), Integer.valueOf(i2));
        this.sdtiming.add(map);
        int size = this.sdtiming.size();
        for (int i3 = 0; i3 < size - 1; i3++) {
            for (int i4 = 1; i4 < size - i3; i4++) {
                int i5 = i4 - 1;
                if (this.sdtiming.get(i5).entrySet().iterator().next().getKey().compareTo(this.sdtiming.get(i4).entrySet().iterator().next().getKey()) > 0) {
                    Map<Integer, Integer> map2 = this.sdtiming.get(i5);
                    this.sdtiming.set(i5, this.sdtiming.get(i4));
                    this.sdtiming.set(i4, map2);
                }
            }
        }
    }

    public void notify(int i, int i2) {
        int size = this.sdtiming.size();
        for (int i3 = 0; i3 < size; i3++) {
            Map<Integer, Integer> map = this.sdtiming.get(i3);
            if (map.containsKey(Integer.valueOf(i))) {
                map.put(Integer.valueOf(i), Integer.valueOf(i2));
                return;
            }
        }
    }

    public void removePlan(int i) {
        int size = this.sdtiming.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (this.sdtiming.get(i2).containsKey(Integer.valueOf(i))) {
                this.sdtiming.remove(i2);
                return;
            }
        }
    }
}
