package com.shima.smartbushome.centercontrol;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.pickerview.TimePickerView;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.scheduleutil.DateUtils;
import com.shima.smartbushome.assist.scheduleutil.SelectRemindCyclePopup;
import com.shima.smartbushome.database.Savemarcobutton;
import com.shima.smartbushome.database.Saveschedule;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/* loaded from: classes.dex */
public class ScheduleAddActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout allLayout;
    private TextView date_tv;
    private EditText ed_echedule_name;
    AlertView marcolistalert;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl;
    private RelativeLayout ring_rl;
    private Button set_btn;
    private TextView tv_repeat_value;
    private TextView tv_ring_value;
    private String time = "";
    private int cycle = 9999;
    private int ring = 0;
    private boolean updatedata = false;
    int secheduleid = 0;
    int shcedulelength = 0;
    int op1 = 0;
    String repeat_update = "";
    int repeat_op1 = 0;
    int repeat_op2 = 0;
    List<Savemarcobutton> listmarco = new ArrayList();
    List<String> stringlist = new ArrayList();
    private int marcoid = 99999999;

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_schedule_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scheduleadd_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Schedule Add");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        this.allLayout = (LinearLayout) findViewById(R.id.all_layout);
        this.set_btn = (Button) findViewById(R.id.set_btn);
        this.set_btn.setOnClickListener(this);
        this.date_tv = (TextView) findViewById(R.id.date_tv);
        this.repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        this.repeat_rl.setOnClickListener(this);
        this.ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        this.ring_rl.setOnClickListener(this);
        this.tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        this.tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        this.ed_echedule_name = (EditText) findViewById(R.id.ed_echedule_name);
        this.pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() { // from class: com.shima.smartbushome.centercontrol.ScheduleAddActivity.1
            @Override // com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener
            public void onTimeSelect(Date date, View view) {
                ScheduleAddActivity.this.time = ScheduleAddActivity.getTime(date);
                ScheduleAddActivity.this.date_tv.setText(ScheduleAddActivity.this.time);
            }
        }).setType(TimePickerView.Type.HOURS_MINS).setCancelText("Cancel").setSubmitText("Comfirn").setOutSideCancelable(false).isCyclic(false).setDate(Calendar.getInstance()).setLabel("year", "month", "day", "hours", "mins", "seconds").build();
        this.date_tv.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.ScheduleAddActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ScheduleAddActivity.this.pvTime.show();
            }
        });
        Intent intent = getIntent();
        if (intent.getBooleanExtra("updatedata", false)) {
            this.updatedata = true;
            this.secheduleid = intent.getIntExtra("scheduleID", 0);
            this.shcedulelength = intent.getIntExtra("length", 0);
            this.ed_echedule_name.setText(intent.getStringExtra(DatabaseUtil.KEY_NAME));
            this.time = intent.getStringExtra("time");
            setTime(this.time);
            this.set_btn.setText("Update");
            this.repeat_update = intent.getStringExtra("repeat");
            this.tv_repeat_value.setText(this.repeat_update);
            this.op1 = intent.getIntExtra("op1", 0);
            this.marcoid = intent.getIntExtra("marcoid", 0);
            return;
        }
        this.updatedata = false;
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public void setTime(String str) {
        String str2 = str.split(":")[3];
        String str3 = str2.substring(0, 2) + ":" + str2.substring(2, 4);
        this.date_tv.setText(str3);
        this.time = str3;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.repeat_rl) {
            selectRemindCycle();
            return;
        }
        if (id == R.id.ring_rl) {
            selectRingWay();
        } else {
            if (id != R.id.set_btn) {
                return;
            }
            if (this.updatedata) {
                updateClock();
            } else {
                setClock();
            }
        }
    }

    private void updateClock() {
        if (this.cycle != 9999) {
            if (this.time != null && this.time.length() > 0) {
                this.time.split(":");
                if (this.cycle == 0) {
                    this.repeat_op1 = 2;
                } else if (this.cycle == -1) {
                    this.repeat_op1 = 1;
                } else {
                    this.repeat_op1 = 3;
                    String[] strArrSplit = parseRepeat(this.cycle, 1).split(Consts.SECOND_LEVEL_SPLIT);
                    this.repeat_op2 = 0;
                    for (String str : strArrSplit) {
                        setop2(Integer.parseInt(str));
                    }
                }
            }
        } else if (this.time != null && this.time.length() > 0) {
            this.time.split(":");
            if (this.op1 == 2) {
                this.repeat_op1 = 2;
            } else if (this.op1 == 1) {
                this.repeat_op1 = 1;
            } else if (this.op1 == 3) {
                this.repeat_op1 = 3;
                String[] strArrSplit2 = this.repeat_update.split(Consts.SECOND_LEVEL_SPLIT);
                this.repeat_op2 = 0;
                for (String str2 : strArrSplit2) {
                    setop2(Integer.parseInt(str2));
                }
            }
        }
        String strTrim = this.ed_echedule_name.getText().toString().trim();
        if (strTrim.length() > 0) {
            if (this.time.equals("")) {
                Toast.makeText(this, "please select time", 0).show();
                return;
            }
            Saveschedule saveschedule = new Saveschedule();
            saveschedule.schedule_id = this.secheduleid;
            saveschedule.schedule_icon = "";
            saveschedule.schedule_name = strTrim;
            saveschedule.alarm_time = getTargettime(this.repeat_op1, this.repeat_op2, this.time);
            saveschedule.last_status = 0;
            saveschedule.marco_ID = this.marcoid;
            saveschedule.repeat_option1 = this.repeat_op1;
            saveschedule.repeat_option2 = this.repeat_op2;
            MainActivity.mgr.updateschedule(saveschedule);
            broadcastUpdate(ScheduleActivity.ACTION_UPDATE_Schedule);
            finish();
            return;
        }
        Toast.makeText(this, "please enter a name", 0).show();
    }

    private void setClock() {
        int i = getscheduleID();
        if (this.time != null && this.time.length() > 0) {
            this.time.split(":");
            if (this.cycle == 0) {
                this.repeat_op1 = 2;
            } else if (this.cycle == -1) {
                this.repeat_op1 = 1;
            } else {
                this.repeat_op1 = 3;
                String[] strArrSplit = parseRepeat(this.cycle, 1).split(Consts.SECOND_LEVEL_SPLIT);
                this.repeat_op2 = 0;
                for (String str : strArrSplit) {
                    setop2(Integer.parseInt(str));
                }
            }
        }
        if (this.cycle != 9999) {
            String strTrim = this.ed_echedule_name.getText().toString().trim();
            if (strTrim.length() > 0) {
                Saveschedule saveschedule = new Saveschedule();
                saveschedule.schedule_id = i;
                saveschedule.schedule_icon = "";
                saveschedule.schedule_name = strTrim;
                saveschedule.alarm_time = getTargettime(this.repeat_op1, this.repeat_op2, this.time);
                saveschedule.last_status = 0;
                saveschedule.marco_ID = this.marcoid;
                saveschedule.repeat_option1 = this.repeat_op1;
                saveschedule.repeat_option2 = this.repeat_op2;
                MainActivity.mgr.addschedule(saveschedule);
                broadcastUpdate(ScheduleActivity.ACTION_ADD_Schedule);
                finish();
                return;
            }
            Toast.makeText(this, "please enter a name", 0).show();
            return;
        }
        Toast.makeText(this, "you need to select repeat", 0).show();
    }

    private void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
    }

    public void setop2(int i) {
        switch (i) {
            case 1:
                this.repeat_op2 |= 2;
                break;
            case 2:
                this.repeat_op2 |= 4;
                break;
            case 3:
                this.repeat_op2 |= 8;
                break;
            case 4:
                this.repeat_op2 |= 16;
                break;
            case 5:
                this.repeat_op2 |= 32;
                break;
            case 6:
                this.repeat_op2 |= 64;
                break;
            case 7:
                this.repeat_op2 |= 128;
                break;
        }
    }

    private int getscheduleID() {
        List<Saveschedule> listQueryschedule = MainActivity.mgr.queryschedule();
        if (listQueryschedule.size() == 0) {
            return 1;
        }
        return 1 + listQueryschedule.get(listQueryschedule.size() - 1).schedule_id;
    }

    private String getTargettime(int i, int i2, String str) {
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append(str.split(":")[0]);
        sb.append(str.split(":")[1]);
        String string = sb.toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HHmm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date(System.currentTimeMillis());
        String str3 = simpleDateFormat.format(date);
        switch (i) {
            case 1:
            case 2:
                if (Integer.parseInt(str3.split(":")[3]) <= Integer.parseInt(string)) {
                    return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + string;
                }
                Date date2 = new Date();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date2);
                gregorianCalendar.add(5, 1);
                return simpleDateFormat2.format(gregorianCalendar.getTime()) + ":" + string;
            case 3:
                int week = DateUtils.getWeek(simpleDateFormat2.format(date));
                String[] strArrSplit = this.tv_repeat_value.getText().toString().split(Consts.SECOND_LEVEL_SPLIT);
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
                    if (Integer.parseInt(str3.split(":")[3]) <= Integer.parseInt(string)) {
                        return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + string;
                    }
                    if (strArrSplit.length == 1) {
                        Date date3 = new Date();
                        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                        gregorianCalendar2.setTime(date3);
                        gregorianCalendar2.add(5, 7);
                        return simpleDateFormat2.format(gregorianCalendar2.getTime()) + ":" + string;
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
                    return simpleDateFormat2.format(gregorianCalendar3.getTime()) + ":" + string;
                }
                int iMin = 9;
                for (String str5 : strArrSplit) {
                    iMin = Math.min(iMin, getweektime(str4, str5));
                }
                Date date5 = new Date();
                GregorianCalendar gregorianCalendar4 = new GregorianCalendar();
                gregorianCalendar4.setTime(date5);
                gregorianCalendar4.add(5, iMin);
                return simpleDateFormat2.format(gregorianCalendar4.getTime()) + ":" + string;
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
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.centercontrol.ScheduleAddActivity.getweekint(java.lang.String):int");
    }

    public void selectRemindCycle() {
        final SelectRemindCyclePopup selectRemindCyclePopup = new SelectRemindCyclePopup(this);
        selectRemindCyclePopup.showPopup(this.allLayout);
        selectRemindCyclePopup.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup.SelectRemindCyclePopupOnClickListener() { // from class: com.shima.smartbushome.centercontrol.ScheduleAddActivity.3
            @Override // com.shima.smartbushome.assist.scheduleutil.SelectRemindCyclePopup.SelectRemindCyclePopupOnClickListener
            public void obtainMessage(int i, String str) {
                switch (i) {
                    case 7:
                        int iIntValue = Integer.valueOf(str).intValue();
                        ScheduleAddActivity.this.tv_repeat_value.setText(ScheduleAddActivity.parseRepeat(iIntValue, 0));
                        ScheduleAddActivity.this.cycle = iIntValue;
                        selectRemindCyclePopup.dismiss();
                        break;
                    case 8:
                        ScheduleAddActivity.this.tv_repeat_value.setText("EveryDay");
                        ScheduleAddActivity.this.cycle = 0;
                        selectRemindCyclePopup.dismiss();
                        break;
                    case 9:
                        ScheduleAddActivity.this.tv_repeat_value.setText("Only One Time");
                        ScheduleAddActivity.this.cycle = -1;
                        selectRemindCyclePopup.dismiss();
                        break;
                }
            }
        });
    }

    public void selectRingWay() {
        this.listmarco = MainActivity.mgr.querymarcobutton();
        if (this.stringlist.size() > 0) {
            this.stringlist.clear();
        }
        for (int i = 0; i < this.listmarco.size(); i++) {
            this.stringlist.add(this.listmarco.get(i).marco_remark);
        }
        this.marcolistalert = new AlertView("Select Marco to Act", null, "CANCEL", null, null, this, AlertView.Style.Alert, null);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.listdialog, (ViewGroup) null);
        ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
        viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
        listView.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.stringlist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.ScheduleAddActivity.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                ScheduleAddActivity.this.tv_ring_value.setText(ScheduleAddActivity.this.listmarco.get(i2).marco_remark);
                ScheduleAddActivity.this.marcoid = ScheduleAddActivity.this.listmarco.get(i2).marco_id;
                ScheduleAddActivity.this.marcolistalert.dismiss();
            }
        });
        this.marcolistalert.addExtView(viewGroup);
        this.marcolistalert.show();
    }

    public static String parseRepeat(int i, int i2) {
        String str = "";
        String str2 = "";
        if (i == 0) {
            i = TransportMediator.KEYCODE_MEDIA_PAUSE;
        }
        if (i % 2 == 1) {
            str = "Mon";
            str2 = "1";
        }
        if (i % 4 >= 2) {
            if ("".equals(str)) {
                str = "Tue";
                str2 = "2";
            } else {
                str = str + ",Tue";
                str2 = str2 + ",2";
            }
        }
        if (i % 8 >= 4) {
            if ("".equals(str)) {
                str = "Wed";
                str2 = "3";
            } else {
                str = str + ",Wed";
                str2 = str2 + ",3";
            }
        }
        if (i % 16 >= 8) {
            if ("".equals(str)) {
                str = "Thu";
                str2 = "4";
            } else {
                str = str + ",Thu";
                str2 = str2 + ",4";
            }
        }
        if (i % 32 >= 16) {
            if ("".equals(str)) {
                str = "Fri";
                str2 = "5";
            } else {
                str = str + ",Fri";
                str2 = str2 + ",5";
            }
        }
        if (i % 64 >= 32) {
            if ("".equals(str)) {
                str = "Sat";
                str2 = "6";
            } else {
                str = str + ",Sat";
                str2 = str2 + ",6";
            }
        }
        if (i / 64 == 1) {
            if ("".equals(str)) {
                str = "Sun";
                str2 = "7";
            } else {
                str = str + ",Sun";
                str2 = str2 + ",7";
            }
        }
        return i2 == 0 ? str : str2;
    }

    public static String getTime(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
