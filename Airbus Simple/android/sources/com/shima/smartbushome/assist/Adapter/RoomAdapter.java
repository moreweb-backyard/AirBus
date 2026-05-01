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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Saveroom;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class RoomAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList();
    private List<String> iconarray = new ArrayList();
    private List<String> iconbgarray = new ArrayList();
    private List<Saveroom> roomdata = new ArrayList();
    private int size = 0;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public RoomAdapter(Context context) {
        this.inflater = null;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void clear() {
        this.stringarray.clear();
        this.iconarray.clear();
        this.iconbgarray.clear();
        this.mView.clear();
        this.size = 0;
        this.roomdata = new ArrayList();
    }

    public void add(String str, String str2, String str3) {
        this.stringarray.add(str);
        this.iconarray.add(str2);
        this.iconbgarray.add(str3);
        this.size++;
    }

    public void setRoomList(List<Saveroom> list) {
        this.roomdata = list;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewInflate = this.mView.get(Integer.valueOf(i));
        if (viewInflate == null) {
            viewInflate = this.inflater.inflate(R.layout.adapter_draggable_grid_item2, (ViewGroup) null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.textView12);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView8);
            ImageView imageView2 = (ImageView) viewInflate.findViewById(R.id.iv_roomBackground);
            textView.setText(this.stringarray.get(i));
            imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, this.iconarray.get(i))));
            try {
                Glide.with(this.context).load(this.roomdata.get(i).room_icon_bg).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(300, 300)).into(imageView2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int i2 = Build.VERSION.SDK_INT;
            this.mView.put(Integer.valueOf(i), viewInflate);
        }
        return viewInflate;
    }

    public void setdrawable(View view, int i) throws Resources.NotFoundException, NoSuchMethodException, SecurityException {
        GradientDrawable gradientDrawable;
        Drawable drawable = this.context.getResources().getDrawable(R.drawable.mainitem);
        Drawable.ConstantState constantState = ((StateListDrawable) drawable).getConstantState();
        try {
            Method method = constantState.getClass().getMethod("getChildren", new Class[0]);
            method.setAccessible(true);
            Drawable[] drawableArr = (Drawable[]) method.invoke(constantState, new Object[0]);
            for (int i2 = 0; i2 < drawableArr.length && (gradientDrawable = (GradientDrawable) drawableArr[i2]) != null; i2++) {
                if (i2 == 0) {
                    gradientDrawable.setColor(ToColor(this.iconbgarray.get(i)));
                } else {
                    gradientDrawable.setColor(ToColor(this.iconbgarray.get(i)));
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

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
