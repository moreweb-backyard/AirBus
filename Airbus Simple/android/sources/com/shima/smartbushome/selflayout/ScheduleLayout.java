package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kyleduo.switchbutton.SwitchButton;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.scheduleutil.DateUtils;
import com.shima.smartbushome.centercontrol.ScheduleAddActivity;
import com.shima.smartbushome.database.Saveschedule;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/* loaded from: classes.dex */
public class ScheduleLayout extends LinearLayout implements View.OnLongClickListener {
    CheckBox cb_delete;
    private String[] colorarray;
    boolean deletemode;
    LayoutInflater inflater;
    boolean init;
    Context rootcontext;
    RelativeLayout schedule_selflayout;
    SwitchButton schedule_switch;
    Saveschedule schedulecontent;
    TextView tv_actiontime;
    TextView tv_name;
    TextView tv_repeat;
    View view;

    public ScheduleLayout(Context context) {
        super(context);
        this.deletemode = false;
        this.init = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        initview(context);
    }

    public ScheduleLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.init = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.schedule_layout, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        initUI();
    }

    public void initUI() {
        this.tv_actiontime = (TextView) this.view.findViewById(R.id.schedule_time);
        this.tv_repeat = (TextView) this.view.findViewById(R.id.schedule_state);
        this.tv_name = (TextView) this.view.findViewById(R.id.schedule_name);
        this.cb_delete = (CheckBox) this.view.findViewById(R.id.schedule_delete);
        this.schedule_switch = (SwitchButton) this.view.findViewById(R.id.schedule_switch);
        this.schedule_selflayout = (RelativeLayout) this.view.findViewById(R.id.schedule_selflayout);
        this.tv_actiontime.setOnLongClickListener(this);
        this.schedule_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.ScheduleLayout.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (ScheduleLayout.this.init) {
                    if (z) {
                        ScheduleLayout.this.schedulecontent.last_status = 0;
                        MainActivity.mgr.updateschedule_status(ScheduleLayout.this.schedulecontent);
                        ScheduleLayout.this.updatealarmtime(ScheduleLayout.this.schedulecontent);
                    } else {
                        ScheduleLayout.this.schedulecontent.last_status = 1;
                        MainActivity.mgr.updateschedule_status(ScheduleLayout.this.schedulecontent);
                    }
                }
            }
        });
    }

    private void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.schedule_time) {
            String[] strArr = getop2();
            Intent intent = new Intent(getContext(), (Class<?>) ScheduleAddActivity.class);
            intent.putExtra("updatedata", true);
            intent.putExtra("scheduleID", this.schedulecontent.schedule_id);
            intent.putExtra("length", strArr.length);
            intent.putExtra(DatabaseUtil.KEY_NAME, this.schedulecontent.schedule_name);
            intent.putExtra("time", this.schedulecontent.alarm_time);
            intent.putExtra("repeat", this.tv_repeat.getText().toString());
            intent.putExtra("op1", this.schedulecontent.repeat_option1);
            intent.putExtra("marcoid", this.schedulecontent.marco_ID);
            this.rootcontext.startActivity(intent);
        }
        return true;
    }

    private String[] getop2() {
        int i = this.schedulecontent.repeat_option2;
        String[] strArr = new String[this.tv_repeat.getText().toString().split(Consts.SECOND_LEVEL_SPLIT).length];
        int i2 = 0;
        if ((i & 2) == 2) {
            strArr[0] = "1";
            i2 = 1;
        }
        if ((i & 4) == 4) {
            strArr[i2] = "2";
            i2++;
        }
        if ((i & 8) == 8) {
            strArr[i2] = "3";
            i2++;
        }
        if ((i & 16) == 16) {
            strArr[i2] = "4";
            i2++;
        }
        if ((i & 32) == 32) {
            strArr[i2] = "5";
            i2++;
        }
        if ((i & 64) == 64) {
            strArr[i2] = "6";
            i2++;
        }
        if ((i & 128) == 128) {
            strArr[i2] = "7";
        }
        return strArr;
    }

    public void setcontan(Saveschedule saveschedule) {
        this.schedulecontent = saveschedule;
        setname(saveschedule.schedule_name);
        setTime(saveschedule.alarm_time);
        setrepeat(saveschedule.repeat_option1, saveschedule.repeat_option2);
        if (saveschedule.last_status == 0) {
            this.schedule_switch.setChecked(true);
        } else if (saveschedule.last_status == 1) {
            this.schedule_switch.setChecked(false);
        }
        this.schedule_selflayout.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.init = true;
    }

    public void setTime(String str) {
        String str2 = str.split(":")[3];
        this.tv_actiontime.setText(str2.substring(0, 2) + ":" + str2.substring(2, 4));
    }

    public void setrepeat(int i, int i2) {
        switch (i) {
            case 1:
                this.tv_repeat.setText("Only One Time");
                break;
            case 2:
                this.tv_repeat.setText("EveryDay");
                break;
            case 3:
                if ((i2 & 128) == 128) {
                    this.tv_repeat.append("Sun,");
                }
                if ((i2 & 64) == 64) {
                    this.tv_repeat.append("Sat,");
                }
                if ((i2 & 32) == 32) {
                    this.tv_repeat.append("Fri,");
                }
                if ((i2 & 16) == 16) {
                    this.tv_repeat.append("Thu,");
                }
                if ((i2 & 8) == 8) {
                    this.tv_repeat.append("Wed,");
                }
                if ((i2 & 4) == 4) {
                    this.tv_repeat.append("Tue,");
                }
                if ((i2 & 2) == 2) {
                    this.tv_repeat.append("Mon");
                    break;
                }
                break;
        }
    }

    public void setname(String str) {
        this.tv_name.setText(str);
    }

    public void setdeletevisable(boolean z) {
        if (z) {
            this.cb_delete.setVisibility(0);
            this.deletemode = true;
            this.cb_delete.setChecked(false);
        } else {
            this.cb_delete.setVisibility(4);
            this.deletemode = false;
        }
    }

    public boolean getIfneedtoDelete() {
        return this.cb_delete.isChecked();
    }

    public int getscheduleid() {
        return this.schedulecontent.schedule_id;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatealarmtime(Saveschedule saveschedule) {
        Saveschedule saveschedule2 = new Saveschedule();
        saveschedule2.schedule_id = saveschedule.schedule_id;
        saveschedule2.schedule_icon = "";
        saveschedule2.schedule_name = saveschedule.schedule_name;
        this.tv_repeat.setText("");
        setrepeat(saveschedule.repeat_option1, saveschedule.repeat_option2);
        saveschedule2.alarm_time = getTargettime(saveschedule.repeat_option1, saveschedule.repeat_option2, saveschedule.alarm_time.split(":")[3]);
        saveschedule2.last_status = 0;
        saveschedule2.marco_ID = saveschedule.marco_ID;
        saveschedule2.repeat_option1 = saveschedule.repeat_option1;
        saveschedule2.repeat_option2 = saveschedule.repeat_option2;
        MainActivity.mgr.updateschedule(saveschedule2);
    }

    private String getTargettime(int i, int i2, String str) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HHmm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date(System.currentTimeMillis());
        String str3 = simpleDateFormat.format(date);
        switch (i) {
            case 1:
            case 2:
                if (Integer.parseInt(str3.split(":")[3]) < Integer.parseInt(str)) {
                    return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + str;
                }
                Date date2 = new Date();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date2);
                gregorianCalendar.add(5, 1);
                return simpleDateFormat2.format(gregorianCalendar.getTime()) + ":" + str;
            case 3:
                int week = DateUtils.getWeek(simpleDateFormat2.format(date));
                String[] strArrSplit = this.tv_repeat.getText().toString().split(Consts.SECOND_LEVEL_SPLIT);
                String str4 = "";
                switch (week) {
                    case 1:
                        str4 = "Sun";
                        break;
                    case 2:
                        str4 = "Mon";
                        break;
                    case 3:
                        str4 = "Tue";
                        break;
                    case 4:
                        str4 = "Wed";
                        break;
                    case 5:
                        str4 = "Thu";
                        break;
                    case 6:
                        str4 = "Fri";
                        break;
                    case 7:
                        str4 = "Sat";
                        break;
                }
                int i3 = 999;
                for (int i4 = 0; i4 < strArrSplit.length; i4++) {
                    if (str4.equals(strArrSplit[i4])) {
                        i3 = i4;
                    }
                }
                if (i3 != 999) {
                    if (Integer.parseInt(str3.split(":")[3]) < Integer.parseInt(str)) {
                        return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + str;
                    }
                    if (strArrSplit.length == 1) {
                        Date date3 = new Date();
                        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                        gregorianCalendar2.setTime(date3);
                        gregorianCalendar2.add(5, 7);
                        return simpleDateFormat2.format(gregorianCalendar2.getTime()) + ":" + str;
                    }
                    int i5 = i3 - 1;
                    if (i5 >= 0) {
                        str2 = strArrSplit[i5];
                    } else {
                        str2 = strArrSplit[strArrSplit.length - 1];
                    }
                    Date date4 = new Date();
                    GregorianCalendar gregorianCalendar3 = new GregorianCalendar();
                    gregorianCalendar3.setTime(date4);
                    gregorianCalendar3.add(5, getweektime(str4, str2));
                    return simpleDateFormat2.format(gregorianCalendar3.getTime()) + ":" + str;
                }
                int iMin = 9;
                for (String str5 : strArrSplit) {
                    iMin = Math.min(iMin, getweektime(str4, str5));
                }
                Date date5 = new Date();
                GregorianCalendar gregorianCalendar4 = new GregorianCalendar();
                gregorianCalendar4.setTime(date5);
                gregorianCalendar4.add(5, iMin);
                return simpleDateFormat2.format(gregorianCalendar4.getTime()) + ":" + str;
            default:
                return "";
        }
    }

    private int getweektime(String str, String str2) {
        int i = getweekint(str);
        int i2 = getweekint(str2);
        if (i > i2) {
            return 7 - (i - i2);
        }
        if (i < i2) {
            return i2 - i;
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getweekint(java.lang.String r9) {
        /*
            r8 = this;
            int r0 = r9.hashCode()
            r1 = 6
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            r7 = 0
            switch(r0) {
                case 70909: goto L4b;
                case 77548: goto L41;
                case 82886: goto L37;
                case 83500: goto L2d;
                case 84065: goto L23;
                case 84452: goto L19;
                case 86838: goto Lf;
                default: goto Le;
            }
        Le:
            goto L55
        Lf:
            java.lang.String r0 = "Wed"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 3
            goto L56
        L19:
            java.lang.String r0 = "Tue"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 2
            goto L56
        L23:
            java.lang.String r0 = "Thu"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 4
            goto L56
        L2d:
            java.lang.String r0 = "Sun"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 0
            goto L56
        L37:
            java.lang.String r0 = "Sat"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 6
            goto L56
        L41:
            java.lang.String r0 = "Mon"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 1
            goto L56
        L4b:
            java.lang.String r0 = "Fri"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 5
            goto L56
        L55:
            r9 = -1
        L56:
            switch(r9) {
                case 0: goto L65;
                case 1: goto L63;
                case 2: goto L61;
                case 3: goto L5f;
                case 4: goto L5d;
                case 5: goto L5b;
                case 6: goto L66;
                default: goto L59;
            }
        L59:
            r1 = 0
            goto L66
        L5b:
            r1 = 5
            goto L66
        L5d:
            r1 = 4
            goto L66
        L5f:
            r1 = 3
            goto L66
        L61:
            r1 = 2
            goto L66
        L63:
            r1 = 1
            goto L66
        L65:
            r1 = 7
        L66:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.ScheduleLayout.getweekint(java.lang.String):int");
    }
}
