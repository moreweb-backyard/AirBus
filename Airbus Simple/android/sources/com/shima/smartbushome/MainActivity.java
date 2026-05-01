package com.shima.smartbushome;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shima.smartbushome.assist.AutoUpdate.updatemain;
import com.shima.smartbushome.assist.MusicNotification;
import com.shima.smartbushome.assist.MusicNotifyReceiver;
import com.shima.smartbushome.assist.nfc.NFCRun;
import com.shima.smartbushome.assist.scheduleutil.ScheduleRestartService;
import com.shima.smartbushome.assist.scheduleutil.ScheduleServer;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savenfc;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SystemUIUtil;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    public static final String ACTION_BACKPRESS = "com.example.main.BACKPRESS";
    public static boolean islockchangeid = false;
    public static boolean islockshake = true;
    public static TextView locationid;
    public static Context maincontext;
    public static DBManager mgr;
    public static udp_socket mydupsocket;
    private BottomNavigationBar bottomNavigationBar;
    private int lastSelectedPosition;
    private NfcAdapter mAdapter;
    private FragmentManager mFragmentManager;
    private MainFragment mHomeFragment;
    private NdefMessage mNdefPushMessage;
    private PendingIntent mPendingIntent;
    MusicNotifyReceiver mReceiver;
    private CenterFragment mcenterFragment;
    private NFCRun revnfc;
    Toolbar toolbar;
    updatemain update;
    Window window;
    public static List<HashMap<String, String>> netdeviceList = new ArrayList();
    public static int REQUEST_IGNORE_BATTERY_CODE = 11;
    private List<Savenfc> thisnfclist = new ArrayList();
    private boolean hadnfc = false;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        SystemUIUtil.setSystemUIVisible(this, false);
        this.toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        this.toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        this.toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(this.toolbar);
        if (Build.VERSION.SDK_INT >= 19) {
            this.toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        this.window = getWindow();
        this.window.addFlags(67108864);
        this.window.addFlags(Integer.MIN_VALUE);
        locationid = (TextView) findViewById(R.id.locationid);
        locationid.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.buttonitem);
        buttonitemInit();
        initCheckPermission();
        this.mAdapter = NfcAdapter.getDefaultAdapter(this);
        this.mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        this.mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord("", Locale.ENGLISH, true)});
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        System.out.println("nfc newin");
        if (getIntent() != null) {
            List<Savenfc> listQuerynfc = mgr.querynfc();
            if (this.thisnfclist.size() > 0) {
                this.thisnfclist.clear();
            }
            Parcelable parcelableExtra = getIntent().getParcelableExtra("android.nfc.extra.TAG");
            if (parcelableExtra != null) {
                String strDumpTagData = dumpTagData((Tag) parcelableExtra);
                for (int i = 0; i < listQuerynfc.size(); i++) {
                    if (listQuerynfc.get(i).nfc_content.equals(strDumpTagData)) {
                        this.thisnfclist.add(listQuerynfc.get(i));
                    }
                }
            }
            this.revnfc.run(this.thisnfclist);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SystemUIUtil.setSystemUIVisible(this, false);
        if (this.mAdapter == null) {
            try {
                this.mAdapter.isEnabled();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (this.mAdapter.isEnabled() && this.mAdapter != null) {
            try {
                this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, null, (String[][]) null);
                this.mAdapter.enableForegroundNdefPush(this, this.mNdefPushMessage);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        if (this.mAdapter != null) {
            try {
                this.mAdapter.disableForegroundDispatch(this);
                this.mAdapter.disableForegroundNdefPush(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregeisterReceiver();
        new Intent(this, (Class<?>) ScheduleServer.class);
        new Intent(this, (Class<?>) ScheduleRestartService.class);
        mydupsocket.StopAllThread();
        if (mgr != null) {
            mgr.closeDB();
        }
    }

    public void initcreate() {
        maincontext = this;
        this.mReceiver = new MusicNotifyReceiver();
        mgr = new DBManager(maincontext);
        mydupsocket = new udp_socket(maincontext);
        SharedPreferences sharedPreferences = getSharedPreferences("ipconfig", 0);
        udp_socket.mansetip = sharedPreferences.getBoolean("ipset", false);
        if (udp_socket.mansetip) {
            udp_socket.manlocalip = stringtobyte(sharedPreferences.getString("manlocalip", ""));
            udp_socket.mantargetip = stringtobyte(sharedPreferences.getString("mantargetip", ""));
        }
        SharedPreferences sharedPreferences2 = getSharedPreferences("ddnsconfig", 0);
        udp_socket.ddnssetip = sharedPreferences2.getBoolean("ipset", false);
        if (udp_socket.ddnssetip) {
            udp_socket.ddnstargetip = stringtobyte(sharedPreferences2.getString("ddnstargetip", ""));
        }
        SharedPreferences sharedPreferences3 = getSharedPreferences("httpconfig", 0);
        udp_socket.rsipip = sharedPreferences3.getBoolean("ipset", false);
        SharedPreferences sharedPreferences4 = getSharedPreferences("lockinfo", 0);
        islockshake = sharedPreferences4.getBoolean("lockshake", true);
        islockchangeid = sharedPreferences4.getBoolean("lockchangeid", false);
        if (udp_socket.rsipip) {
            String string = sharedPreferences3.getString("MacAddress", "");
            if (!string.equals("")) {
                udp_socket.MacAddress = stringtoMACbyte(string);
            }
        }
        ((RelativeLayout) findViewById(R.id.mainacbg)).setBackgroundColor(getSharedPreferences("pagesbgcolor", 0).getInt("mainbgcolor", ViewCompat.MEASURED_STATE_MASK));
        if (mydupsocket != null) {
            mydupsocket.initprocess();
        } else {
            Toast.makeText(this, "init process fail", 0).show();
        }
        intiReceiver();
        this.revnfc = new NFCRun(this);
        if (!isWorked("com.shima.smartbushome.ScheduleServer")) {
            new Intent(this, (Class<?>) ScheduleServer.class);
            new Intent(this, (Class<?>) ScheduleRestartService.class);
            System.out.println("服务没有启动！！");
            return;
        }
        System.out.println("服务已经启动了！！");
    }

    private void buttonitemInit() {
        this.bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() { // from class: com.shima.smartbushome.MainActivity.1
            @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
            public void onTabReselected(int i) {
            }

            @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
            public void onTabUnselected(int i) {
            }

            @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
            public void onTabSelected(int i) {
                MainActivity.this.lastSelectedPosition = i;
                FragmentTransaction fragmentTransactionBeginTransaction = MainActivity.this.mFragmentManager.beginTransaction();
                MainActivity.this.hideFragment(fragmentTransactionBeginTransaction);
                switch (i) {
                    case 0:
                        if (MainActivity.this.mHomeFragment != null) {
                            fragmentTransactionBeginTransaction.show(MainActivity.this.mHomeFragment);
                        } else {
                            MainActivity.this.mHomeFragment = MainFragment.newInstance("mHomeFragment");
                            fragmentTransactionBeginTransaction.add(R.id.tb, MainActivity.this.mHomeFragment);
                        }
                        MainActivity.this.toolbar.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.title_transparent_black));
                        if (Build.VERSION.SDK_INT >= 21) {
                            MainActivity.this.window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
                            break;
                        }
                        break;
                    case 1:
                        if (MainActivity.this.mcenterFragment != null) {
                            fragmentTransactionBeginTransaction.show(MainActivity.this.mcenterFragment);
                        } else {
                            MainActivity.this.mcenterFragment = CenterFragment.newInstance("mToolFragment");
                            fragmentTransactionBeginTransaction.add(R.id.tb, MainActivity.this.mcenterFragment);
                        }
                        MainActivity.this.toolbar.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.title_transparent_black));
                        if (Build.VERSION.SDK_INT >= 21) {
                            MainActivity.this.window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
                            break;
                        }
                        break;
                }
                fragmentTransactionBeginTransaction.commit();
            }
        });
        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "Room");
        bottomNavigationItem.setActiveColor("#5f000000").setInActiveColor("#ffffff");
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem(R.mipmap.ic_launch_white_24dp, "Center");
        bottomNavigationItem2.setActiveColor("#5f000000").setInActiveColor("#ffffff");
        this.bottomNavigationBar.addItem(bottomNavigationItem).addItem(bottomNavigationItem2).setMode(0).setBackgroundStyle(2).initialise();
        setDefaultFragment();
    }

    private void intiReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicNotification.ACTION_Button);
        intentFilter.addAction(MusicNotification.ACTION_notify_close);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
    }

    private void initautoupdate() {
        this.update = new updatemain(this);
        this.update.checkUpdate(true);
    }

    private boolean isWorked(String str) {
        ArrayList arrayList = (ArrayList) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningServices(30);
        for (int i = 0; i < arrayList.size(); i++) {
            if (((ActivityManager.RunningServiceInfo) arrayList.get(i)).service.getClassName().toString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void setDefaultFragment() {
        this.mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionBeginTransaction = this.mFragmentManager.beginTransaction();
        MainFragment mainFragment = this.mHomeFragment;
        this.mHomeFragment = MainFragment.newInstance("mHomeFragment");
        fragmentTransactionBeginTransaction.add(R.id.tb, this.mHomeFragment);
        fragmentTransactionBeginTransaction.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFragment(FragmentTransaction fragmentTransaction) {
        if (this.mHomeFragment != null) {
            fragmentTransaction.hide(this.mHomeFragment);
        }
        if (this.mcenterFragment != null) {
            fragmentTransaction.hide(this.mcenterFragment);
        }
    }

    private void unregeisterReceiver() {
        if (this.mReceiver != null) {
            getApplicationContext().unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
    }

    private String dumpTagData(Parcelable parcelable) {
        return (getHex(((Tag) parcelable).getId())).trim();
    }

    private String getHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (int length = bArr.length - 1; length >= 0; length--) {
            int i = bArr[length] & 255;
            if (i < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(i));
            if (length > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private NdefRecord newTextRecord(String str, Locale locale, boolean z) {
        Charset charsetForName;
        byte[] bytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        if (z) {
            charsetForName = Charset.forName("UTF-8");
        } else {
            charsetForName = Charset.forName("UTF-16");
        }
        byte[] bytes2 = str.getBytes(charsetForName);
        char length = (char) ((z ? 0 : 128) + bytes.length);
        byte[] bArr = new byte[bytes.length + 1 + bytes2.length];
        bArr[0] = (byte) length;
        System.arraycopy(bytes, 0, bArr, 1, bytes.length);
        System.arraycopy(bytes2, 0, bArr, bytes.length + 1, bytes2.length);
        return new NdefRecord((short) 1, NdefRecord.RTD_TEXT, new byte[0], bArr);
    }

    public byte[] stringtobyte(String str) {
        String[] strArrSplit = str.split("\\.");
        return new byte[]{(byte) Integer.parseInt(strArrSplit[0]), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit[3])};
    }

    public byte[] stringtoMACbyte(String str) {
        String[] strArrSplit = str.split("\\.");
        return new byte[]{(byte) Integer.parseInt(strArrSplit[0], 16), (byte) Integer.parseInt(strArrSplit[1], 16), (byte) Integer.parseInt(strArrSplit[2], 16), (byte) Integer.parseInt(strArrSplit[3], 16), (byte) Integer.parseInt(strArrSplit[4], 16), (byte) Integer.parseInt(strArrSplit[5], 16), (byte) Integer.parseInt(strArrSplit[6], 16), (byte) Integer.parseInt(strArrSplit[7], 16)};
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        broadcastUpdate(ACTION_BACKPRESS);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 124) {
            HashMap map = new HashMap();
            map.put("android.permission.WRITE_EXTERNAL_STORAGE", 0);
            for (int i2 = 0; i2 < strArr.length; i2++) {
                map.put(strArr[i2], Integer.valueOf(iArr[i2]));
            }
            if (((Integer) map.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0) {
                initcreate();
                return;
            } else {
                Toast.makeText(this, "Some Permission is Denied", 0).show();
                showMessageOKCancel("You can open Permission in app setting", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.MainActivity.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                        MainActivity.this.startActivity(intent);
                    }
                });
                return;
            }
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @TargetApi(23)
    public static boolean isIgnoringBatteryOptimizations(Activity activity) {
        return ((PowerManager) activity.getSystemService("power")).isIgnoringBatteryOptimizations(activity.getPackageName());
    }

    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                if (((PowerManager) activity.getSystemService("power")).isIgnoringBatteryOptimizations(packageName)) {
                    return;
                }
                intent.setAction("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS");
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + packageName));
                activity.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            int i3 = REQUEST_IGNORE_BATTERY_CODE;
        } else if (i2 == 0 && i == REQUEST_IGNORE_BATTERY_CODE) {
            Toast.makeText(this, "please confirm to ignore battery saving", 0).show();
        }
    }

    public void initCheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            isIgnoringBatteryOptimizations(this);
            insertDummyContactWrapper();
        } else {
            initcreate();
        }
    }

    private void insertDummyContactWrapper() {
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        if (!addPermission(arrayList2, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            arrayList.add("Write Storage");
        }
        if (arrayList2.size() > 0) {
            if (arrayList.size() > 0) {
                String str = "You need to grant access to " + ((String) arrayList.get(0));
                for (int i = 1; i < arrayList.size(); i++) {
                    str = str + ", " + ((String) arrayList.get(i));
                }
                showMessageOKCancel(str, new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.MainActivity.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        MainActivity.this.requestPermissions((String[]) arrayList2.toArray(new String[arrayList2.size()]), 124);
                    }
                });
                return;
            }
            requestPermissions((String[]) arrayList2.toArray(new String[arrayList2.size()]), 124);
            return;
        }
        initcreate();
    }

    private boolean addPermission(List<String> list, String str) {
        if (checkSelfPermission(str) == 0) {
            return true;
        }
        list.add(str);
        return shouldShowRequestPermissionRationale(str);
    }

    private void showMessageOKCancel(String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this).setMessage(str).setPositiveButton("OK", onClickListener).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).create().show();
    }
}
