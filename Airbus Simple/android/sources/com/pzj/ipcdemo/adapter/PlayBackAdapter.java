package com.pzj.ipcdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pzj.ipcdemo.PlayBackTFActivity;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.bean.PlayBackBean;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PlayBackAdapter extends BaseAdapter {
    public ArrayList<PlayBackBean> arrayList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    PlayBackTFActivity playback;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public PlayBackAdapter(PlayBackTFActivity playBackTFActivity, Context context) {
        this.context = context;
        this.playback = playBackTFActivity;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.arrayList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            viewInflate = this.inflater.inflate(R.layout.playbacktf_listitem, (ViewGroup) null);
            viewHolder.tvName = (TextView) viewInflate.findViewById(R.id.tv_name);
            viewHolder.imgtip = (ImageView) viewInflate.findViewById(R.id.img_tip);
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        String path = this.arrayList.get(i).getPath();
        viewHolder.tvName.setText(getTime(path));
        String tip = getTip(path);
        System.out.println(tip + "#########");
        if (getModel(tip).equals("a")) {
            viewHolder.imgtip.setBackgroundResource(R.drawable.icon_gpio);
        } else if (getModel(tip).equals("b")) {
            viewHolder.imgtip.setBackgroundResource(R.drawable.icon_motion);
        } else if (getModel(tip).equals("c")) {
            viewHolder.imgtip.setBackgroundResource(R.drawable.icon_rec);
        }
        if (i > 5000) {
            this.playback.loadMoreView.setVisibility(0);
        }
        return viewInflate;
    }

    public String getTip(String str) {
        return str.substring(str.length() - 8, str.length() - 5);
    }

    public String getModel(String str) {
        System.out.println(str + ">>>>>>>>>>>>>>>>>>>>>>>>>!!!!!!!!!!!!!!!!!!!");
        return str.substring(str.length() - 1, str.length()).equals("1") ? "a" : str.substring(1, 2).equals("1") ? "b" : "c";
    }

    public String getTime(String str) {
        String strSubstring = str.substring(0, 14);
        str.substring(15, str.length());
        return strSubstring.substring(0, 4) + "-" + strSubstring.substring(4, 6) + "-" + strSubstring.substring(6, 8) + " " + strSubstring.substring(8, 10) + ":" + strSubstring.substring(10, 12) + ":" + strSubstring.substring(12, 14);
    }

    private class ViewHolder {
        ImageView imgtip;
        TextView tvName;

        private ViewHolder() {
        }
    }

    public void addPlayBean(PlayBackBean playBackBean) {
        if (this.arrayList.contains(playBackBean.getPath())) {
            return;
        }
        this.arrayList.add(playBackBean);
    }

    public PlayBackBean getPlayBean(int i) {
        return this.arrayList.get(i);
    }
}
