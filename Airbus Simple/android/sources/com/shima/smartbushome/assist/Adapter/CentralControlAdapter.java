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
public class CentralControlAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> stringarray = new ArrayList();
    private List<String> iconbgarray = new ArrayList();
    private String[] colorarray = {"ff9962cc", "ffe18d06", "ff599ade", "ff2ead58", "ffa99950", "ff396879", "ffBA5252", "ff00dba1", "ffcbe471", "ffaeaeae", "FF37ACBC", "FFCEBBE0"};
    private String[] namearray = {"MACRO", "LIGHT", "HVAC", "MUSIC", "CURTAIN", "OTHER", "FAN", "SECURITY", "ENERGY", "STATUS", "CAMERA", "NFC"};
    private String[] iconarray = {"marco", "light", "hvac", "music", "curtain", "other", "fan", "security", "energy", "status", "camera_ipc", "nfc"};
    private int size = 12;
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public CentralControlAdapter(Context context) {
        this.inflater = null;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
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
        View viewInflate = this.mView.get(Integer.valueOf(i));
        if (viewInflate == null) {
            viewInflate = this.inflater.inflate(R.layout.adapter_centralcontrol_grid_item, (ViewGroup) null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.centraltext);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.centralimg);
            textView.setText(this.namearray[i]);
            imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, this.iconarray[i])));
            if (Build.VERSION.SDK_INT >= 16) {
                setdrawable(viewInflate, i);
                viewInflate.setBackgroundColor(this.context.getResources().getColor(R.color.title_transparent_black));
            } else {
                viewInflate.setBackgroundColor(this.context.getResources().getColor(R.color.title_transparent_black));
            }
            this.mView.put(Integer.valueOf(i), viewInflate);
        }
        return viewInflate;
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
