package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class FounctionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList();
    private String[] colorarray = {"FFe18d06", "FF599ade", "FFBA5252", "FFa99950", "FF2ead58", "FFae4bb9", "FF396879", "FF7DE2B8", "FFB7FF6E"};
    private String[] namearray = {"Light", "HVAC", "Fan", "Curtain", "Music", "Mood", "Other", "Media", "9 in 1", "Floor Heat"};
    private int size = 0;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public FounctionAdapter(Context context) {
        this.inflater = null;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void clear() {
        this.stringarray.clear();
        this.size = 0;
    }

    public void add(String str) {
        this.stringarray.add(str);
        this.size++;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.stringarray.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) throws Resources.NotFoundException, NoSuchMethodException, SecurityException {
        if (view == null) {
            view = this.inflater.inflate(R.layout.adapter_draggable_grid_item_type2, (ViewGroup) null);
            TextView textView = (TextView) view.findViewById(R.id.dragtype2tv);
            ImageView imageView = (ImageView) view.findViewById(R.id.dragtype2imageView8);
            textView.setText(this.stringarray.get(i));
            if (this.stringarray.get(i).equals("9 in 1")) {
                imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "nio")));
            } else if (this.stringarray.get(i).equals("Floor Heat")) {
                imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "fh")));
            } else {
                imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, this.stringarray.get(i).toLowerCase())));
            }
            int i2 = 0;
            while (true) {
                if (i2 >= 10) {
                    break;
                }
                if (!this.stringarray.get(i).equals(this.namearray[i2])) {
                    i2++;
                } else if (Build.VERSION.SDK_INT >= 16) {
                    setdrawable(view, i2);
                } else {
                    view.setBackgroundColor(this.context.getResources().getColor(R.color.title_transparent_black));
                }
            }
        }
        return view;
    }

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public void setdrawable(View view, int i) throws Resources.NotFoundException, NoSuchMethodException, SecurityException {
        GradientDrawable gradientDrawable;
        Drawable drawable = this.context.getResources().getDrawable(R.drawable.roomitem);
        Drawable.ConstantState constantState = ((StateListDrawable) drawable).getConstantState();
        try {
            Method method = constantState.getClass().getMethod("getChildren", new Class[0]);
            method.setAccessible(true);
            Drawable[] drawableArr = (Drawable[]) method.invoke(constantState, new Object[0]);
            for (int i2 = 0; i2 < drawableArr.length && (gradientDrawable = (GradientDrawable) drawableArr[i2]) != null; i2++) {
                if (i2 == 0) {
                    gradientDrawable.setColor(this.context.getResources().getColor(R.color.title_transparent_black));
                } else {
                    gradientDrawable.setColor(this.context.getResources().getColor(R.color.title_transparent_black));
                }
            }
            view.setBackground(drawable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
