package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.SaveArea;
import java.util.List;

/* loaded from: classes.dex */
public class AreaAdapter extends BaseAdapter {
    private List<SaveArea> areaList;
    private Context context;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public AreaAdapter(Context context, List<SaveArea> list) {
        this.context = context;
        this.areaList = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.areaList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.areaList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            viewInflate = LayoutInflater.from(this.context).inflate(R.layout.adapter_draggable_grid_item3, (ViewGroup) null);
            viewHolder.tv_areaName = (TextView) viewInflate.findViewById(R.id.textView12);
            viewHolder.iv_areaBackground = (ImageView) viewInflate.findViewById(R.id.iv_roomBackground);
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        SaveArea saveArea = this.areaList.get(i);
        viewHolder.tv_areaName.setText(saveArea.getArea_name());
        Glide.with(this.context).load(saveArea.getArea_bg()).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(500, 500)).into(viewHolder.iv_areaBackground);
        return viewInflate;
    }

    private static class ViewHolder {
        ImageView iv_areaBackground;
        TextView tv_areaName;

        private ViewHolder() {
        }
    }
}
