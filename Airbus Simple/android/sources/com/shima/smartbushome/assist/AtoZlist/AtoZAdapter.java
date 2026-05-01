package com.shima.smartbushome.assist.AtoZlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.util.List;

/* loaded from: classes.dex */
public class AtoZAdapter extends BaseAdapter implements SectionIndexer {
    public static final int select = 2097325033;
    private List<SortModel> list;
    private Context mContext;
    private int size;
    private int selectItem = -1;
    private String[] colorarray = {"5f000000", "5f000000"};

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        return null;
    }

    public AtoZAdapter(Context context, List<SortModel> list) {
        this.list = null;
        this.mContext = context;
        this.list = list;
        this.size = list.size();
    }

    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate;
        ViewHolder viewHolder;
        SortModel sortModel = this.list.get(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.atoz_listitem, (ViewGroup) null);
            viewHolder.tvTitle = (TextView) viewInflate.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) viewInflate.findViewById(R.id.catalog);
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i == getPositionForSection(getSectionForPosition(i))) {
            viewHolder.tvLetter.setVisibility(0);
            viewHolder.tvLetter.setText(sortModel.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(8);
        }
        viewHolder.tvTitle.setText(this.list.get(i).getName());
        if (i == this.selectItem) {
            viewHolder.tvTitle.setBackgroundColor(2097325033);
        } else {
            viewHolder.tvTitle.setBackgroundColor(ToColor(this.colorarray[i % 2]));
        }
        return viewInflate;
    }

    static final class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;

        ViewHolder() {
        }
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
    }

    public int getSelectItem() {
        return this.selectItem;
    }

    public void setNextorBack(String str) {
        if (str.equals("next")) {
            this.selectItem++;
            if (this.selectItem > this.size - 1) {
                this.selectItem = 0;
                return;
            }
            return;
        }
        if (str.equals("back")) {
            this.selectItem--;
            if (this.selectItem < 0) {
                this.selectItem = this.size - 1;
            }
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public String getselectSongname() {
        return this.list.get(this.selectItem).getName();
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int i) {
        return this.list.get(i).getSortLetters().charAt(0);
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int i) {
        for (int i2 = 0; i2 < getCount(); i2++) {
            if (this.list.get(i2).getSortLetters().toUpperCase().charAt(0) == i) {
                return i2;
            }
        }
        return -1;
    }
}
