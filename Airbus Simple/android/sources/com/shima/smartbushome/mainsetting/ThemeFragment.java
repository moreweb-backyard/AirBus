package com.shima.smartbushome.mainsetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MusicBGAdapter;
import com.shima.smartbushome.assist.holocolorpicker.ColorPicker;
import com.shima.smartbushome.assist.holocolorpicker.SVBar;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class ThemeFragment extends Fragment implements View.OnClickListener {
    TextView curtainbgcolor;
    TextView fanbgcolor;
    TextView hvacbgcolor;
    AlertView iconalter;
    TextView lightbgcolor;
    TextView mainbgcolor;
    TextView mediabgcolor;
    TextView moodbgcolor;
    private AlertView mpcolorAlertViewExt;
    TextView musicbgcolor;
    TextView niobgcolor;
    TextView otherbgcolor;
    ColorPicker picker;
    TextView roombgcolor;
    TextView selecttextview;
    Button themesave;
    View view;
    int[] color = new int[11];
    String musicbg = "";
    int bg_position = 0;
    public OnItemClickListener alterclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.ThemeFragment.2
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == ThemeFragment.this.mpcolorAlertViewExt) {
                if (i == -1) {
                    ThemeFragment.this.mpcolorAlertViewExt.dismiss();
                } else if (i == 0) {
                    ThemeFragment.this.setcolor(ThemeFragment.this.picker.getColor(), ThemeFragment.this.selecttextview.getId());
                    ThemeFragment.this.selecttextview.setBackgroundColor(ThemeFragment.this.picker.getColor());
                }
            }
        }
    };

    public static ThemeFragment newInstance() {
        return new ThemeFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_theme, viewGroup, false);
        this.mainbgcolor = (TextView) this.view.findViewById(R.id.mainbgcolor);
        this.roombgcolor = (TextView) this.view.findViewById(R.id.roombgcolor);
        this.lightbgcolor = (TextView) this.view.findViewById(R.id.lightbgcolor);
        this.hvacbgcolor = (TextView) this.view.findViewById(R.id.hvacbgcolor);
        this.fanbgcolor = (TextView) this.view.findViewById(R.id.fanbgcolor);
        this.curtainbgcolor = (TextView) this.view.findViewById(R.id.curtainbgcolor);
        this.musicbgcolor = (TextView) this.view.findViewById(R.id.musicbgcolor);
        this.moodbgcolor = (TextView) this.view.findViewById(R.id.moodbgcolor);
        this.otherbgcolor = (TextView) this.view.findViewById(R.id.otherbgcolor);
        this.mediabgcolor = (TextView) this.view.findViewById(R.id.mediabgcolor);
        this.niobgcolor = (TextView) this.view.findViewById(R.id.niobgcolor);
        this.themesave = (Button) this.view.findViewById(R.id.themesave);
        FragmentActivity activity = getActivity();
        getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("pagesbgcolor", 0);
        this.color[0] = sharedPreferences.getInt("mainbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[1] = sharedPreferences.getInt("roombgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[2] = sharedPreferences.getInt("lightbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[3] = sharedPreferences.getInt("hvacbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[4] = sharedPreferences.getInt("fanbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[5] = sharedPreferences.getInt("curtainbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.musicbg = sharedPreferences.getString("musicbg", "music_bg10");
        this.color[7] = sharedPreferences.getInt("moodbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[8] = sharedPreferences.getInt("otherbgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[9] = sharedPreferences.getInt("mediabgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.color[10] = sharedPreferences.getInt("niobgcolor", ViewCompat.MEASURED_STATE_MASK);
        this.mainbgcolor.setBackgroundColor(this.color[0]);
        this.roombgcolor.setBackgroundColor(this.color[1]);
        this.lightbgcolor.setBackgroundColor(this.color[2]);
        this.hvacbgcolor.setBackgroundColor(this.color[3]);
        this.fanbgcolor.setBackgroundColor(this.color[4]);
        this.curtainbgcolor.setBackgroundColor(this.color[5]);
        if (Build.VERSION.SDK_INT >= 16) {
            this.musicbgcolor.setBackground(getResources().getDrawable(getResourdIdByResourdName(this.musicbg)));
        } else {
            this.musicbgcolor.setBackgroundDrawable(getResources().getDrawable(getResourdIdByResourdName(this.musicbg)));
        }
        this.moodbgcolor.setBackgroundColor(this.color[7]);
        this.otherbgcolor.setBackgroundColor(this.color[8]);
        this.mediabgcolor.setBackgroundColor(this.color[9]);
        this.niobgcolor.setBackgroundColor(this.color[10]);
        this.mainbgcolor.setOnClickListener(this);
        this.roombgcolor.setOnClickListener(this);
        this.lightbgcolor.setOnClickListener(this);
        this.hvacbgcolor.setOnClickListener(this);
        this.fanbgcolor.setOnClickListener(this);
        this.curtainbgcolor.setOnClickListener(this);
        this.musicbgcolor.setOnClickListener(this);
        this.moodbgcolor.setOnClickListener(this);
        this.otherbgcolor.setOnClickListener(this);
        this.mediabgcolor.setOnClickListener(this);
        this.niobgcolor.setOnClickListener(this);
        this.themesave.setOnClickListener(this);
        return this.view;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.musicbgcolor) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            this.iconalter = new AlertView("BG Selection", null, "CANCEL", new String[]{"SAVE"}, null, getContext(), AlertView.Style.Alert, null);
            View viewInflate = layoutInflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
            GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
            gridView.setAdapter((ListAdapter) new MusicBGAdapter(getContext()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.ThemeFragment.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                    ThemeFragment.this.bg_position = i + 1;
                    ThemeFragment.this.musicbg = "music_bg" + ThemeFragment.this.bg_position;
                    if (Build.VERSION.SDK_INT >= 16) {
                        ThemeFragment.this.musicbgcolor.setBackground(ThemeFragment.this.getResources().getDrawable(ThemeFragment.this.getResourdIdByResourdName(ThemeFragment.this.musicbg)));
                    } else {
                        ThemeFragment.this.musicbgcolor.setBackgroundDrawable(ThemeFragment.this.getResources().getDrawable(ThemeFragment.this.getResourdIdByResourdName(ThemeFragment.this.musicbg)));
                    }
                    ThemeFragment.this.iconalter.dismiss();
                }
            });
            this.iconalter.addExtView(viewInflate);
            this.iconalter.show();
            return;
        }
        if (id == R.id.themesave) {
            savecolorInfo(getActivity(), this.color);
            savemusicbgInfo(getContext(), this.musicbg);
            Toast.makeText(getActivity(), "saved", 0).show();
            return;
        }
        this.selecttextview = (TextView) this.view.findViewById(view.getId());
        this.mpcolorAlertViewExt = new AlertView("Color Selection", null, "CANCEL", null, new String[]{"SAVE"}, getActivity(), AlertView.Style.Alert, this.alterclick);
        this.mpcolorAlertViewExt.setCancelable(false);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.view_pickcolor, (ViewGroup) null);
        this.picker = (ColorPicker) viewGroup.findViewById(R.id.view5);
        this.picker.addSVBar((SVBar) viewGroup.findViewById(R.id.view7));
        ColorDrawable colorDrawable = (ColorDrawable) this.selecttextview.getBackground();
        if (colorDrawable != null) {
            this.picker.setColor(colorDrawable.getColor());
        }
        this.mpcolorAlertViewExt.addExtView(viewGroup);
        this.mpcolorAlertViewExt.show();
    }

    public void setcolor(int i, int i2) {
        char c = 0;
        switch (i2) {
            case R.id.curtainbgcolor /* 2131230964 */:
                c = 5;
                break;
            case R.id.fanbgcolor /* 2131231114 */:
                c = 4;
                break;
            case R.id.hvacbgcolor /* 2131231176 */:
                c = 3;
                break;
            case R.id.lightbgcolor /* 2131231276 */:
                c = 2;
                break;
            case R.id.mediabgcolor /* 2131231382 */:
                c = '\t';
                break;
            case R.id.moodbgcolor /* 2131231430 */:
                c = 7;
                break;
            case R.id.musicbgcolor /* 2131231470 */:
                c = 6;
                break;
            case R.id.niobgcolor /* 2131231499 */:
                c = '\n';
                break;
            case R.id.otherbgcolor /* 2131231548 */:
                c = '\b';
                break;
            case R.id.roombgcolor /* 2131231709 */:
                c = 1;
                break;
        }
        this.color[c] = i;
    }

    public void savecolorInfo(Context context, int[] iArr) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("pagesbgcolor", 0).edit();
        editorEdit.putInt("mainbgcolor", iArr[0]);
        editorEdit.putInt("roombgcolor", iArr[1]);
        editorEdit.putInt("lightbgcolor", iArr[2]);
        editorEdit.putInt("hvacbgcolor", iArr[3]);
        editorEdit.putInt("fanbgcolor", iArr[4]);
        editorEdit.putInt("curtainbgcolor", iArr[5]);
        editorEdit.putInt("moodbgcolor", iArr[7]);
        editorEdit.putInt("otherbgcolor", iArr[8]);
        editorEdit.putInt("mediabgcolor", iArr[9]);
        editorEdit.putInt("niobgcolor", iArr[10]);
        editorEdit.commit();
    }

    public void savemusicbgInfo(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("pagesbgcolor", 0).edit();
        editorEdit.putString("musicbg", str);
        editorEdit.commit();
    }

    public int getResourdIdByResourdName(String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }
}
