package com.pzj.ipcdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.pzj.ipcdemo.bean.DefenseConstant;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.SensorTimeUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/* loaded from: classes.dex */
public class SCameraSetPushVideoTiming extends BaseActivity implements View.OnClickListener {
    private int absValue;
    private TreeSet<Integer> dateset;
    private int key;
    private int passEndTime;
    private int passStartTime;
    private LinearLayout timing_backlayout;
    private LinearLayout timing_edit_layout;
    private CheckBox timing_id1;
    private CheckBox timing_id2;
    private CheckBox timing_id3;
    private CheckBox timing_id4;
    private CheckBox timing_id5;
    private CheckBox timing_id6;
    private CheckBox timing_id7;
    private Button timing_save;
    private Button timing_start_delete;
    private Button timing_start_save;
    private TextView timing_textView1;
    private TextView timing_textView2;
    private TimePicker timing_timePicker1;
    private TimePicker timing_timePicker2;
    private TextView tv_camera_timingaddplan;
    private int value;
    private String startTime = "00:00";
    private String endTime = "24:00";
    private int status = 1;
    private int type = 0;
    private AlertDialog dialog = null;

    @Override // com.pzj.ipcdemo.BaseActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.videotape_timing_setting);
        getDate();
        findview();
    }

    private void getDate() {
        Intent intent = getIntent();
        this.type = intent.getIntExtra(DatabaseUtil.KEY_TYPE, 0);
        this.absValue = intent.getIntExtra("value", 0);
        this.value = Math.abs(this.absValue);
        this.key = intent.getIntExtra("key", 0);
        this.dateset = new TreeSet<>(new Comparator<Integer>() { // from class: com.pzj.ipcdemo.SCameraSetPushVideoTiming.1
            @Override // java.util.Comparator
            public int compare(Integer num, Integer num2) {
                if (num.intValue() > num2.intValue()) {
                    return -1;
                }
                return num == num2 ? 0 : 1;
            }
        });
    }

    public void findview() {
        this.timing_backlayout = (LinearLayout) findViewById(R.id.timing_backlayout);
        this.timing_textView1 = (TextView) findViewById(R.id.timing_textView1);
        this.timing_textView2 = (TextView) findViewById(R.id.timing_textView2);
        this.timing_timePicker1 = (TimePicker) findViewById(R.id.timing_timePicker1);
        this.timing_timePicker2 = (TimePicker) findViewById(R.id.timing_timePicker2);
        this.timing_id1 = (CheckBox) findViewById(R.id.timing_id1);
        this.timing_id2 = (CheckBox) findViewById(R.id.timing_id2);
        this.timing_id3 = (CheckBox) findViewById(R.id.timing_id3);
        this.timing_id4 = (CheckBox) findViewById(R.id.timing_id4);
        this.timing_id5 = (CheckBox) findViewById(R.id.timing_id5);
        this.timing_id6 = (CheckBox) findViewById(R.id.timing_id6);
        this.timing_id7 = (CheckBox) findViewById(R.id.timing_id7);
        this.timing_start_delete = (Button) findViewById(R.id.timing_start_delete);
        this.timing_start_save = (Button) findViewById(R.id.timing_start_save);
        this.timing_save = (Button) findViewById(R.id.timing_save);
        this.timing_edit_layout = (LinearLayout) findViewById(R.id.timing_edit_layout);
        this.tv_camera_timingaddplan = (TextView) findViewById(R.id.tv_camera_timingaddplan);
        this.timing_textView1.setText(getResources().getString(R.string.camera_defense_starttime) + ":" + this.startTime);
        this.timing_textView2.setText(getResources().getString(R.string.camera_defense_endtime) + ":" + this.endTime);
        this.timing_timePicker1.setIs24HourView(true);
        this.timing_timePicker1.setCurrentHour(0);
        this.timing_timePicker1.setCurrentMinute(0);
        this.timing_timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: com.pzj.ipcdemo.SCameraSetPushVideoTiming.2
            @Override // android.widget.TimePicker.OnTimeChangedListener
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                String str;
                String str2;
                if (i < 10) {
                    str = "0" + i;
                } else {
                    str = i + "";
                }
                if (i2 < 10) {
                    str2 = "0" + i2;
                } else {
                    str2 = "" + i2;
                }
                SCameraSetPushVideoTiming.this.startTime = str + ":" + str2;
                SCameraSetPushVideoTiming.this.timing_textView1.setText(SCameraSetPushVideoTiming.this.getResources().getString(R.string.camera_defense_starttime) + ":" + SCameraSetPushVideoTiming.this.startTime);
            }
        });
        this.timing_timePicker2.setIs24HourView(true);
        this.timing_timePicker2.setCurrentHour(0);
        this.timing_timePicker2.setCurrentMinute(0);
        this.timing_timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: com.pzj.ipcdemo.SCameraSetPushVideoTiming.3
            @Override // android.widget.TimePicker.OnTimeChangedListener
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                String str;
                String str2;
                if (i < 10) {
                    str = "0" + i;
                } else {
                    str = i + "";
                }
                if (i2 < 10) {
                    str2 = "0" + i2;
                } else {
                    str2 = "" + i2;
                }
                SCameraSetPushVideoTiming.this.endTime = str + ":" + str2;
                if (i == 0 && i2 == 0) {
                    SCameraSetPushVideoTiming.this.endTime = "24:00";
                }
                SCameraSetPushVideoTiming.this.timing_textView2.setText(SCameraSetPushVideoTiming.this.getResources().getString(R.string.camera_defense_endtime) + ":" + SCameraSetPushVideoTiming.this.endTime);
            }
        });
        if (this.type == 0) {
            this.tv_camera_timingaddplan.setText(getResources().getString(R.string.add_period));
        } else {
            this.timing_edit_layout.setVisibility(0);
            this.timing_save.setVisibility(8);
            this.tv_camera_timingaddplan.setText(getResources().getString(R.string.edit_valid_time));
            if (this.value != 0) {
                int i = this.value & 2047;
                int i2 = (this.value >> 12) & 2047;
                int timeHour = getTimeHour(i);
                int timeMinute = getTimeMinute(i);
                this.timing_timePicker1.setCurrentHour(Integer.valueOf(timeHour));
                this.timing_timePicker1.setCurrentMinute(Integer.valueOf(timeMinute));
                int timeHour2 = getTimeHour(i2);
                int timeMinute2 = getTimeMinute(i2);
                this.timing_timePicker2.setCurrentHour(Integer.valueOf(timeHour2));
                this.timing_timePicker2.setCurrentMinute(Integer.valueOf(timeMinute2));
            }
        }
        this.timing_backlayout.setOnClickListener(this);
        this.timing_start_delete.setOnClickListener(this);
        this.timing_start_save.setOnClickListener(this);
        this.timing_save.setOnClickListener(this);
        CheckBoxListener checkBoxListener = new CheckBoxListener();
        this.timing_id1.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id2.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id3.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id4.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id5.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id6.setOnCheckedChangeListener(checkBoxListener);
        this.timing_id7.setOnCheckedChangeListener(checkBoxListener);
        if (this.type == 1) {
            getWeekPlan(this.value);
        }
    }

    private void getWeekPlan(int i) {
        for (int i2 = 24; i2 < 31; i2++) {
            int i3 = (i >> i2) & 1;
            switch (i2) {
                case 24:
                    if (i3 == 1) {
                        this.timing_id7.setChecked(true);
                        break;
                    } else {
                        this.timing_id7.setChecked(false);
                        break;
                    }
                case 25:
                    if (i3 == 1) {
                        this.timing_id1.setChecked(true);
                        break;
                    } else {
                        this.timing_id1.setChecked(false);
                        break;
                    }
                case 26:
                    if (i3 == 1) {
                        this.timing_id2.setChecked(true);
                        break;
                    } else {
                        this.timing_id2.setChecked(false);
                        break;
                    }
                case 27:
                    if (i3 == 1) {
                        this.timing_id3.setChecked(true);
                        break;
                    } else {
                        this.timing_id3.setChecked(false);
                        break;
                    }
                case 28:
                    if (i3 == 1) {
                        this.timing_id4.setChecked(true);
                        break;
                    } else {
                        this.timing_id4.setChecked(false);
                        break;
                    }
                case 29:
                    if (i3 == 1) {
                        this.timing_id5.setChecked(true);
                        break;
                    } else {
                        this.timing_id5.setChecked(false);
                        break;
                    }
                case 30:
                    if (i3 == 1) {
                        this.timing_id6.setChecked(true);
                        break;
                    } else {
                        this.timing_id6.setChecked(false);
                        break;
                    }
            }
        }
    }

    private int getTimeMinute(int i) {
        return i < 60 ? i : i - ((i / 60) * 60);
    }

    private int getTimeHour(int i) {
        if (i < 60) {
            return 0;
        }
        return i / 60;
    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        ArrayList arrayList = new ArrayList();
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof NumberPicker) {
                    arrayList.add((NumberPicker) childAt);
                } else if (childAt instanceof LinearLayout) {
                    List<NumberPicker> listFindNumberPicker = findNumberPicker((ViewGroup) childAt);
                    if (listFindNumberPicker.size() > 0) {
                        return listFindNumberPicker;
                    }
                } else {
                    continue;
                }
            }
        }
        return arrayList;
    }

    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        CheckBoxListener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            int id = compoundButton.getId();
            if (id == R.id.timing_id1) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.timing_id1.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    SCameraSetPushVideoTiming.this.dateset.add(1);
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id1.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(1)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(1);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id2) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.timing_id2.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    SCameraSetPushVideoTiming.this.dateset.add(2);
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id2.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(2)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(2);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id3) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.dateset.add(3);
                    SCameraSetPushVideoTiming.this.timing_id3.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id3.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(3)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(3);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id4) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.dateset.add(4);
                    SCameraSetPushVideoTiming.this.timing_id4.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id4.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(4)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(4);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id5) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.dateset.add(5);
                    SCameraSetPushVideoTiming.this.timing_id5.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id5.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(5)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(5);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id6) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.dateset.add(6);
                    SCameraSetPushVideoTiming.this.timing_id6.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                    return;
                } else {
                    SCameraSetPushVideoTiming.this.timing_id6.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(6)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(6);
                        return;
                    }
                    return;
                }
            }
            if (id == R.id.timing_id7) {
                if (compoundButton.isChecked()) {
                    SCameraSetPushVideoTiming.this.dateset.add(0);
                    SCameraSetPushVideoTiming.this.timing_id7.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_startcode_bg));
                } else {
                    SCameraSetPushVideoTiming.this.timing_id7.setTextColor(SCameraSetPushVideoTiming.this.getResources().getColor(R.color.color_alarm_textcolor));
                    if (SCameraSetPushVideoTiming.this.dateset.contains(0)) {
                        SCameraSetPushVideoTiming.this.dateset.remove(0);
                    }
                }
            }
        }
    }

    private void checkTime() {
        Log.e("vst", "startTime:" + this.startTime + ",endTime:" + this.endTime + "date:" + this.dateset.toString());
        this.passStartTime = SensorTimeUtil.getPassMinutes(this.timing_timePicker1.getCurrentHour().intValue(), this.timing_timePicker1.getCurrentMinute().intValue());
        this.passEndTime = 0;
        int iIntValue = this.timing_timePicker2.getCurrentHour().intValue();
        int iIntValue2 = this.timing_timePicker2.getCurrentMinute().intValue();
        if (iIntValue == 0 && iIntValue2 == 0) {
            this.passEndTime = SensorTimeUtil.getPassMinutes(24, 0);
        } else {
            this.passEndTime = SensorTimeUtil.getPassMinutes(this.timing_timePicker2.getCurrentHour().intValue(), this.timing_timePicker2.getCurrentMinute().intValue());
        }
        Log.e("vst", "passStartTime" + this.passStartTime + "*passEndTime*" + this.passEndTime);
        int i = this.passEndTime;
        int i2 = this.passStartTime;
        if (this.dateset.size() == 0) {
            showToast(getResources().getString(R.string.defense_nodate));
        } else {
            getPlanTimerInt();
        }
    }

    private void getPlanTimerInt() {
        String strReplaceIndex = "0000000";
        if (this.dateset.size() != 0) {
            Iterator<Integer> it = this.dateset.iterator();
            while (it.hasNext()) {
                strReplaceIndex = SensorTimeUtil.replaceIndex(6 - it.next().intValue(), strReplaceIndex);
            }
        }
        Log.e("vst", "weeks" + strReplaceIndex);
        int iString32toInt = SensorTimeUtil.string32toInt(SensorTimeUtil.get2Strings("0", strReplaceIndex, SensorTimeUtil.getMinutesString(this.passEndTime, this.passStartTime)));
        Intent intent = new Intent();
        if (this.type == 1) {
            if (this.absValue < 0) {
                iString32toInt = 0 - iString32toInt;
            }
            intent.putExtra("jnitime", iString32toInt);
            intent.putExtra("key", this.key);
            setResult(2012, intent);
            finish();
            return;
        }
        intent.putExtra("jnitime", iString32toInt);
        setResult(2011, intent);
        finish();
    }

    private String getMapKey(int i) {
        switch (i) {
            case 1:
                return DefenseConstant.key_Monday;
            case 2:
                return DefenseConstant.key_Tuesday;
            case 3:
                return DefenseConstant.key_Wednesday;
            case 4:
                return DefenseConstant.key_Thursday;
            case 5:
                return DefenseConstant.key_Friday;
            case 6:
                return DefenseConstant.key_Saturday;
            case 7:
                return DefenseConstant.key_Sunday;
            default:
                return "";
        }
    }

    @Override // com.pzj.ipcdemo.BaseActivity
    public void showToast(String str) {
        Toast.makeText(this, str, 1000).show();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.timing_backlayout) {
            finish();
            return;
        }
        if (id == R.id.timing_start_delete) {
            showDefaultSetDialog();
        } else if (id == R.id.timing_start_save) {
            checkTime();
        } else if (id == R.id.timing_save) {
            checkTime();
        }
    }

    private void showDefaultSetDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.del_ok);
        builder.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.SCameraSetPushVideoTiming.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.putExtra("key", SCameraSetPushVideoTiming.this.key);
                SCameraSetPushVideoTiming.this.setResult(2013, intent);
                SCameraSetPushVideoTiming.this.finish();
            }
        });
        builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.SCameraSetPushVideoTiming.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        this.dialog = builder.create();
        this.dialog.show();
    }
}
