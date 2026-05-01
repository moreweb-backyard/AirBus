package com.shima.smartbushome.centercontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.EnergyAdapter;
import com.shima.smartbushome.database.Saveenergy;
import com.shima.smartbushome.founction_command.energycontrol;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SystemUIUtil;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/* loaded from: classes.dex */
public class EnergyActivity extends AppCompatActivity {
    RadioGroup channelgroup;
    TextView current;
    private LineChartData data;
    AlertView deletealter;
    RelativeLayout detaillayout;
    EditText dev;
    private GridView deviceview;
    private LineChartView lineChart;
    EnergyAdapter madapter;
    EditText name;
    RelativeLayout noenergyinfo;
    float[][] olddata;
    AlertView pairalter;
    private boolean pointsHaveDifferentColor;
    TextView powerresult;
    float[][] randomNumbersTab;
    AlertView setIDdialog;
    AlertView settingDialog;
    EditText sub;
    EditText voltagevalue;
    Handler getdata = new Handler();
    Handler getct24data = new Handler();
    energycontrol ec = new energycontrol();
    boolean deletemode = false;
    Saveenergy thisenergy = new Saveenergy();
    List<String> timelist = new ArrayList();
    RadioButton[] rb_channel = new RadioButton[25];
    int step = 0;
    boolean touchChart = false;
    boolean startcatchdata = false;
    int selectChannel = 24;
    public RadioGroup.OnCheckedChangeListener channel_change = new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.1
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int i2 = 0;
            while (true) {
                if (i2 >= 25) {
                    break;
                }
                if (i == EnergyActivity.this.rb_channel[i2].getId()) {
                    EnergyActivity.this.selectChannel = i2;
                    break;
                }
                i2++;
            }
            if (EnergyActivity.this.timelist.size() > 0) {
                EnergyActivity.this.timelist.clear();
            }
            if (EnergyActivity.this.mAxisXValues.size() > 0) {
                EnergyActivity.this.mAxisXValues.clear();
            }
            EnergyActivity.this.numberOfPoints = 1;
            EnergyActivity.this.generateValues();
            String str = new SimpleDateFormat("hh:mm:ss").format(new Date());
            EnergyActivity.this.timelist.add(str);
            EnergyActivity.this.mAxisXValues.add(new AxisValue(EnergyActivity.this.numberOfPoints).setLabel(str));
            EnergyActivity.this.generateData();
        }
    };
    List<Saveenergy> allenergy = new ArrayList();
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (EnergyActivity.this.allenergy.size() > 0) {
                EnergyActivity.this.allenergy.clear();
            }
            if (EnergyActivity.this.timelist.size() > 0) {
                EnergyActivity.this.timelist.clear();
            }
            if (EnergyActivity.this.mAxisXValues.size() > 0) {
                EnergyActivity.this.mAxisXValues.clear();
            }
            EnergyActivity.this.allenergy = MainActivity.mgr.queryenergy();
            switch (EnergyActivity.this.allenergy.size()) {
                case 0:
                    EnergyActivity.this.noenergyinfo.setVisibility(0);
                    EnergyActivity.this.detaillayout.setVisibility(8);
                    EnergyActivity.this.deviceview.setVisibility(8);
                    break;
                case 1:
                    EnergyActivity.this.noenergyinfo.setVisibility(8);
                    EnergyActivity.this.detaillayout.setVisibility(0);
                    EnergyActivity.this.deviceview.setVisibility(8);
                    EnergyActivity.this.thisenergy = EnergyActivity.this.allenergy.get(0);
                    EnergyActivity.this.madapter = new EnergyAdapter(EnergyActivity.this, EnergyActivity.this.allenergy);
                    EnergyActivity.this.deviceview.setAdapter((ListAdapter) EnergyActivity.this.madapter);
                    EnergyActivity.this.generateValues();
                    String str = new SimpleDateFormat("hh:mm:ss").format(new Date());
                    EnergyActivity.this.timelist.add(str);
                    EnergyActivity.this.mAxisXValues.add(new AxisValue(EnergyActivity.this.numberOfPoints).setLabel(str));
                    EnergyActivity.this.generateData();
                    EnergyActivity.this.startcatchdata = true;
                    EnergyActivity.this.getct24data.postDelayed(EnergyActivity.this.getct24datarun, 0L);
                    break;
                default:
                    EnergyActivity.this.noenergyinfo.setVisibility(8);
                    EnergyActivity.this.detaillayout.setVisibility(8);
                    EnergyActivity.this.deviceview.setVisibility(0);
                    EnergyActivity.this.madapter = new EnergyAdapter(EnergyActivity.this, EnergyActivity.this.allenergy);
                    EnergyActivity.this.deviceview.setAdapter((ListAdapter) EnergyActivity.this.madapter);
                    EnergyActivity.this.deviceview.setOnItemClickListener(EnergyActivity.this.deviceclick);
                    EnergyActivity.this.thisenergy = EnergyActivity.this.allenergy.get(0);
                    break;
            }
        }
    };
    Runnable getct24datarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.3
        @Override // java.lang.Runnable
        public void run() {
            EnergyActivity.this.ec.ReadChannelValue((byte) EnergyActivity.this.thisenergy.subnetID, (byte) EnergyActivity.this.thisenergy.deviceID, MainActivity.mydupsocket);
            EnergyActivity.this.getct24data.postDelayed(EnergyActivity.this.getct24datarun, 3000L);
        }
    };
    public AdapterView.OnItemClickListener deviceclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.4
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            EnergyActivity.this.thisenergy = EnergyActivity.this.madapter.getselected(i);
            EnergyActivity.this.noenergyinfo.setVisibility(8);
            EnergyActivity.this.detaillayout.setVisibility(0);
            EnergyActivity.this.deviceview.setVisibility(8);
            EnergyActivity.this.step = 1;
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.5
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == EnergyActivity.this.setIDdialog) {
                switch (i) {
                    case -1:
                        EnergyActivity.this.setIDdialog.dismiss();
                        break;
                    case 0:
                        Saveenergy saveenergy = new Saveenergy();
                        saveenergy.energy_id = EnergyActivity.this.getenergyID();
                        saveenergy.subnetID = Integer.parseInt(EnergyActivity.this.sub.getText().toString());
                        saveenergy.deviceID = Integer.parseInt(EnergyActivity.this.dev.getText().toString());
                        saveenergy.energyname = EnergyActivity.this.name.getText().toString();
                        saveenergy.channel1_name = "1";
                        saveenergy.channel2_name = "2";
                        saveenergy.channel3_name = "3";
                        saveenergy.channel4_name = "4";
                        saveenergy.channel5_name = "5";
                        saveenergy.channel6_name = "6";
                        saveenergy.channel7_name = "7";
                        saveenergy.channel8_name = "8";
                        saveenergy.channel9_name = "9";
                        saveenergy.channel10_name = "10";
                        saveenergy.channel11_name = "11";
                        saveenergy.channel12_name = "12";
                        saveenergy.channel13_name = "13";
                        saveenergy.channel14_name = "14";
                        saveenergy.channel15_name = "15";
                        saveenergy.channel16_name = "16";
                        saveenergy.channel17_name = "17";
                        saveenergy.channel18_name = "18";
                        saveenergy.channel19_name = "19";
                        saveenergy.channel20_name = "20";
                        saveenergy.channel21_name = "21";
                        saveenergy.channel22_name = "22";
                        saveenergy.channel23_name = "23";
                        saveenergy.channel24_name = "24";
                        MainActivity.mgr.addenergy(saveenergy);
                        EnergyActivity.this.getdata.postDelayed(EnergyActivity.this.getdatarun, 20L);
                        break;
                }
            }
            if (obj == EnergyActivity.this.settingDialog) {
                switch (i) {
                    case -1:
                        EnergyActivity.this.settingDialog.dismiss();
                        break;
                    case 0:
                        Saveenergy saveenergy2 = new Saveenergy();
                        saveenergy2.energy_id = EnergyActivity.this.thisenergy.energy_id;
                        saveenergy2.subnetID = Integer.parseInt(EnergyActivity.this.sub.getText().toString());
                        saveenergy2.deviceID = Integer.parseInt(EnergyActivity.this.dev.getText().toString());
                        saveenergy2.energyname = EnergyActivity.this.name.getText().toString();
                        MainActivity.mgr.updateenergy(saveenergy2);
                        EnergyActivity.this.getdata.postDelayed(EnergyActivity.this.getdatarun, 20L);
                        break;
                }
            }
        }
    };
    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 1;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private List<AxisValue> mAxisXValues = new ArrayList();
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (udp_socket.ACTION_DATA_IN.equals(intent.getAction())) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    EnergyActivity.this.RunReceiveData(byteArrayExtra);
                }
            }
        }
    };
    boolean doing = false;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_energy);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.energytoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Energy");
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
        this.voltagevalue = (EditText) findViewById(R.id.voltagevalue);
        this.powerresult = (TextView) findViewById(R.id.powerresult);
        this.current = (TextView) findViewById(R.id.current);
        this.lineChart = (LineChartView) findViewById(R.id.line_chart);
        this.deviceview = (GridView) findViewById(R.id.ct24device_view);
        this.noenergyinfo = (RelativeLayout) findViewById(R.id.noenergyinfo);
        this.detaillayout = (RelativeLayout) findViewById(R.id.detaillayout);
        this.channelgroup = (RadioGroup) findViewById(R.id.radioGroup3);
        this.channelgroup.setOnCheckedChangeListener(this.channel_change);
        this.rb_channel[0] = (RadioButton) findViewById(R.id.radioButton);
        this.rb_channel[1] = (RadioButton) findViewById(R.id.radioButton2);
        this.rb_channel[2] = (RadioButton) findViewById(R.id.radioButton3);
        this.rb_channel[3] = (RadioButton) findViewById(R.id.radioButton4);
        this.rb_channel[4] = (RadioButton) findViewById(R.id.radioButton5);
        this.rb_channel[5] = (RadioButton) findViewById(R.id.radioButton6);
        this.rb_channel[6] = (RadioButton) findViewById(R.id.radioButton7);
        this.rb_channel[7] = (RadioButton) findViewById(R.id.radioButton8);
        this.rb_channel[8] = (RadioButton) findViewById(R.id.radioButton9);
        this.rb_channel[9] = (RadioButton) findViewById(R.id.radioButton10);
        this.rb_channel[10] = (RadioButton) findViewById(R.id.radioButton11);
        this.rb_channel[11] = (RadioButton) findViewById(R.id.radioButton12);
        this.rb_channel[12] = (RadioButton) findViewById(R.id.radioButton13);
        this.rb_channel[13] = (RadioButton) findViewById(R.id.radioButton14);
        this.rb_channel[14] = (RadioButton) findViewById(R.id.radioButton15);
        this.rb_channel[15] = (RadioButton) findViewById(R.id.radioButton16);
        this.rb_channel[16] = (RadioButton) findViewById(R.id.radioButton17);
        this.rb_channel[17] = (RadioButton) findViewById(R.id.radioButton18);
        this.rb_channel[18] = (RadioButton) findViewById(R.id.radioButton19);
        this.rb_channel[19] = (RadioButton) findViewById(R.id.radioButton20);
        this.rb_channel[20] = (RadioButton) findViewById(R.id.radioButton21);
        this.rb_channel[21] = (RadioButton) findViewById(R.id.radioButton22);
        this.rb_channel[22] = (RadioButton) findViewById(R.id.radioButton23);
        this.rb_channel[23] = (RadioButton) findViewById(R.id.radioButton24);
        this.rb_channel[24] = (RadioButton) findViewById(R.id.radioButtonall);
        this.getdata.postDelayed(this.getdatarun, 20L);
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        registerReceiver(this.mGattUpdateReceiver, makeUpdateIntentFilter());
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.getct24data.removeCallbacks(this.getct24datarun);
    }

    public int getenergyID() {
        if (this.allenergy.size() > 0) {
            return this.allenergy.get(this.allenergy.size() - 1).energy_id + 1;
        }
        return 0;
    }

    public void addenergy(View view) {
        this.setIDdialog = new AlertView("Setting", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
        this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
        this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
        this.name = (EditText) viewInflate.findViewById(R.id.acremark);
        this.sub.setText("0");
        this.dev.setText("0");
        this.name.setText("CT24:NO" + getenergyID());
        this.setIDdialog.addExtView(viewInflate);
        this.setIDdialog.setCancelable(false);
        this.setIDdialog.show();
    }

    public void startgetdata(View view) {
        if (this.startcatchdata) {
            Toast.makeText(this, "already started", 0).show();
            return;
        }
        this.startcatchdata = true;
        this.getct24data.postDelayed(this.getct24datarun, 3000L);
        Toast.makeText(this, "start getting data", 0).show();
    }

    public void stopgetdata(View view) {
        if (this.startcatchdata) {
            this.startcatchdata = false;
            this.getct24data.removeCallbacks(this.getct24datarun);
            Toast.makeText(this, "stop getting data", 0).show();
            return;
        }
        Toast.makeText(this, "already stop", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void generateValues() {
        this.randomNumbersTab = (float[][]) Array.newInstance((Class<?>) float.class, this.maxNumberOfLines, this.numberOfPoints);
        for (int i = 0; i < this.numberOfLines; i++) {
            for (int i2 = 0; i2 < this.numberOfPoints; i2++) {
                this.randomNumbersTab[i][i2] = 0.0f;
            }
        }
        this.olddata = (float[][]) Array.newInstance((Class<?>) float.class, this.maxNumberOfLines, this.numberOfPoints);
        this.olddata = this.randomNumbersTab;
    }

    private void addnewvalue(int i) {
        this.randomNumbersTab = (float[][]) Array.newInstance((Class<?>) float.class, this.maxNumberOfLines, this.numberOfPoints);
        for (int i2 = 0; i2 < this.olddata.length; i2++) {
            for (int i3 = 0; i3 < this.olddata[i2].length; i3++) {
                this.randomNumbersTab[i2][i3] = this.olddata[i2][i3];
            }
            this.randomNumbersTab[i2][this.numberOfPoints - 1] = i;
        }
        this.olddata = (float[][]) Array.newInstance((Class<?>) float.class, this.maxNumberOfLines, this.numberOfPoints);
        this.olddata = this.randomNumbersTab;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void generateData() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.numberOfLines; i++) {
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < this.numberOfPoints; i2++) {
                arrayList2.add(new PointValue(i2, this.randomNumbersTab[i][i2]));
            }
            Line line = new Line(arrayList2);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(this.shape);
            line.setCubic(this.isCubic);
            line.setFilled(this.isFilled);
            line.setHasLabels(this.hasLabels);
            line.setHasLabelsOnlyForSelected(this.hasLabelForSelected);
            line.setHasLines(this.hasLines);
            line.setHasPoints(this.hasPoints);
            if (this.pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            arrayList.add(line);
        }
        this.data = new LineChartData(arrayList);
        if (this.hasAxes) {
            Axis axis = new Axis();
            Axis hasLines = new Axis().setHasLines(true);
            if (this.hasAxesNames) {
                axis.setName("Time/s");
                hasLines.setName("I/mA");
                axis.setHasTiltedLabels(true);
                axis.setValues(this.mAxisXValues);
            }
            this.data.setAxisXBottom(axis);
            this.data.setAxisYLeft(hasLines);
        } else {
            this.data.setAxisXBottom(null);
            this.data.setAxisYLeft(null);
        }
        this.data.setBaseValue(Float.NEGATIVE_INFINITY);
        this.lineChart.setLineChartData(this.data);
    }

    public void RunReceiveData(byte[] bArr) {
        int i;
        int i2 = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (this.doing) {
            return;
        }
        this.doing = true;
        if ((bArr[17] & 255) == this.thisenergy.subnetID && (bArr[18] & 255) == this.thisenergy.deviceID && i2 == 337) {
            int[] iArr = new int[24];
            for (int i3 = 0; i3 < 24; i3++) {
                int i4 = i3 * 2;
                iArr[i3] = (bArr[i4 + 25] << 8) + (bArr[i4 + 26] & 255);
            }
            this.numberOfPoints++;
            if (this.selectChannel != 24) {
                i = iArr[this.selectChannel];
            } else {
                int i5 = 0;
                for (int i6 = 0; i6 < 24; i6++) {
                    i5 += iArr[i6];
                }
                i = i5;
            }
            addnewvalue(i);
            String str = new SimpleDateFormat("hh:mm:ss").format(new Date());
            this.timelist.add(str);
            this.mAxisXValues.add(new AxisValue(this.numberOfPoints).setLabel(str));
            try {
                this.current.setText("current: " + i + "mA");
                TextView textView = this.powerresult;
                StringBuilder sb = new StringBuilder();
                sb.append("instantaneous power: ");
                double d = Integer.parseInt(this.voltagevalue.getText().toString()) * i;
                Double.isNaN(d);
                sb.append(d / 1000.0d);
                sb.append("W");
                textView.setText(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            generateData();
        }
        this.doing = false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.energy_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String[] strArr = new String[this.allenergy.size()];
        for (int i = 0; i < this.allenergy.size(); i++) {
            strArr[i] = this.allenergy.get(i).energyname;
        }
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                switch (this.step) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        this.noenergyinfo.setVisibility(8);
                        this.detaillayout.setVisibility(8);
                        this.deviceview.setVisibility(0);
                        this.step = 0;
                        break;
                }
            case R.id.energy_add /* 2131231072 */:
                this.setIDdialog = new AlertView("Setting", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
                View viewInflate = getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                this.name = (EditText) viewInflate.findViewById(R.id.acremark);
                this.sub.setText("0");
                this.dev.setText("0");
                this.name.setText("CT24:NO" + getenergyID());
                this.setIDdialog.addExtView(viewInflate);
                this.setIDdialog.setCancelable(false);
                this.setIDdialog.show();
                this.getdata.postDelayed(this.getdatarun, 20L);
                break;
            case R.id.energy_delete /* 2131231073 */:
                this.deletealter = new AlertView("Select CT24 to Delete", null, "CANCEL", null, null, this, AlertView.Style.Alert, this.itemclick);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.listdialog, (ViewGroup) null);
                ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                listView.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, strArr));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.7
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        MainActivity.mgr.deleteenergy("energy", EnergyActivity.this.madapter.getselected(i2));
                        EnergyActivity.this.getdata.postDelayed(EnergyActivity.this.getdatarun, 20L);
                        Toast.makeText(EnergyActivity.this, "Delete Succeed", 0).show();
                        EnergyActivity.this.deletealter.dismiss();
                    }
                });
                this.deletealter.addExtView(viewGroup);
                this.deletealter.show();
                break;
            case R.id.energypair /* 2131231075 */:
                this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, this, AlertView.Style.Alert, this.itemclick);
                View viewInflate2 = getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                ListView listView2 = (ListView) viewInflate2.findViewById(R.id.listView4);
                listView2.setAdapter((ListAdapter) new DeviceListAdapter(this, MainActivity.netdeviceList));
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.EnergyActivity.8
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        Saveenergy saveenergy = new Saveenergy();
                        saveenergy.energy_id = EnergyActivity.this.thisenergy.energy_id;
                        saveenergy.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                        saveenergy.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                        saveenergy.energyname = EnergyActivity.this.thisenergy.energyname;
                        MainActivity.mgr.updateenergy(saveenergy);
                        EnergyActivity.this.getdata.postDelayed(EnergyActivity.this.getdatarun, 20L);
                        Toast.makeText(EnergyActivity.this, "apir " + MainActivity.netdeviceList.get(i2).get("devicename") + " succeed", 0).show();
                        EnergyActivity.this.pairalter.dismiss();
                    }
                });
                this.pairalter.addExtView(viewInflate2);
                this.pairalter.show();
                break;
            case R.id.energysetting /* 2131231076 */:
                this.settingDialog = new AlertView("Setting", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
                View viewInflate3 = getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                this.sub = (EditText) viewInflate3.findViewById(R.id.acsubedit);
                this.dev = (EditText) viewInflate3.findViewById(R.id.acdevedit);
                this.name = (EditText) viewInflate3.findViewById(R.id.acremark);
                this.sub.setText(String.valueOf(this.thisenergy.subnetID));
                this.dev.setText(String.valueOf(this.thisenergy.deviceID));
                this.name.setText(this.thisenergy.energyname);
                this.settingDialog.addExtView(viewInflate3);
                this.settingDialog.setCancelable(false);
                this.settingDialog.show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        return intentFilter;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        switch (this.step) {
            case 0:
                finish();
                break;
            case 1:
                this.noenergyinfo.setVisibility(8);
                this.detaillayout.setVisibility(8);
                this.deviceview.setVisibility(0);
                this.step = 0;
                break;
        }
    }
}
