package com.shima.smartbushome.centercontrol;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savenfc;
import com.shima.smartbushome.selflayout.NFCLayout;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class NFCActivity extends AppCompatActivity {
    MenuItem add;
    MenuItem delete;
    private NfcAdapter mAdapter;
    private AlertDialog mDialog;
    private NdefMessage mNdefPushMessage;
    private PendingIntent mPendingIntent;
    private LinearLayout nfclinearlayout;
    private ScrollView nfcview;
    private RelativeLayout nonfclayout;
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    public static String ACTION_UPDATE_nfc = "com.smarthome.updatenfc";
    Handler getdatahandler = new Handler();
    List<Savenfc> allnfcdata = new ArrayList();
    List<NFCLayout> layoutlist = new ArrayList();
    boolean deletemode = false;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.NFCActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (NFCActivity.ACTION_UPDATE_nfc.equals(intent.getAction())) {
                NFCActivity.this.getdatahandler.postDelayed(NFCActivity.this.getdatarun, 20L);
            }
        }
    };
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.NFCActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (NFCActivity.this.allnfcdata.size() > 0) {
                NFCActivity.this.allnfcdata.clear();
            }
            if (NFCActivity.this.layoutlist.size() > 0) {
                NFCActivity.this.layoutlist.clear();
            }
            NFCActivity.this.nfclinearlayout.removeAllViews();
            NFCActivity.this.allnfcdata = MainActivity.mgr.querynfc();
            if (NFCActivity.this.allnfcdata.size() > 0) {
                NFCActivity.this.nonfclayout.setVisibility(8);
                NFCActivity.this.nfcview.setVisibility(0);
                for (int i = 0; i < NFCActivity.this.allnfcdata.size(); i++) {
                    NFCActivity.this.addspecView(NFCActivity.this.allnfcdata.get(i));
                }
            } else {
                NFCActivity.this.nonfclayout.setVisibility(0);
                NFCActivity.this.nfcview.setVisibility(8);
            }
            NFCActivity.this.getdatahandler.removeCallbacks(NFCActivity.this.getdatarun);
        }
    };
    private String hexString = "0123456789ABCDEF";

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.nfc_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("NFC");
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
        this.nonfclayout = (RelativeLayout) findViewById(R.id.nonfclayout);
        this.nfcview = (ScrollView) findViewById(R.id.nfcview);
        this.nfclinearlayout = (LinearLayout) findViewById(R.id.nfclinearlayout);
        this.mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", (DialogInterface.OnClickListener) null).create();
        this.mAdapter = NfcAdapter.getDefaultAdapter(this);
        this.mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        this.mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord("", Locale.ENGLISH, true)});
        this.getdatahandler.postDelayed(this.getdatarun, 20L);
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        registerReceiver(this.mGattUpdateReceiver, makeUpdateIntentFilter());
        if (this.mAdapter == null) {
            try {
                if (this.mAdapter.isEnabled()) {
                    return;
                }
                showWirelessSettingsDialog();
                return;
            } catch (Exception unused) {
                showMessage("error", "No NFC found on this device");
                return;
            }
        }
        if (!this.mAdapter.isEnabled()) {
            Toast.makeText(this, "please open the NFC funcion", 0).show();
            return;
        }
        if (this.mAdapter != null) {
            try {
                this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, null, (String[][]) null);
                this.mAdapter.enableForegroundNdefPush(this, this.mNdefPushMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
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
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_setting_menu, menu);
        this.add = menu.findItem(R.id.light_add);
        this.delete = menu.findItem(R.id.light_remove);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            switch (itemId) {
                case R.id.light_add /* 2131231274 */:
                    if (this.deletemode) {
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.deletemode = false;
                        for (int i = 0; i < this.layoutlist.size(); i++) {
                            this.layoutlist.get(i).setdeletevisable(false);
                        }
                        break;
                    } else {
                        startActivity(new Intent(this, (Class<?>) NFCAddActivity.class));
                        break;
                    }
                case R.id.light_remove /* 2131231275 */:
                    this.deletemode = !this.deletemode;
                    if (this.deletemode) {
                        this.add.setTitle("CANCLE DELETE");
                        this.delete.setTitle("DELETE");
                        for (int i2 = 0; i2 < this.layoutlist.size(); i2++) {
                            this.layoutlist.get(i2).setdeletevisable(true);
                        }
                        break;
                    } else {
                        for (int i3 = 0; i3 < this.layoutlist.size(); i3++) {
                            if (this.layoutlist.get(i3).getIfneedtoDelete()) {
                                MainActivity.mgr.deletenfc(this.layoutlist.get(i3).getnfcid());
                            }
                        }
                        this.getdatahandler.postDelayed(this.getdatarun, 20L);
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.deletemode = false;
                        break;
                    }
            }
        } else {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Savenfc savenfc) {
        NFCLayout nFCLayout = new NFCLayout(this);
        nFCLayout.setcontan(savenfc);
        this.nfclinearlayout.addView(nFCLayout);
        this.layoutlist.add(nFCLayout);
    }

    public void addnfc(View view) {
        startActivity(new Intent(this, (Class<?>) NFCAddActivity.class));
    }

    public String decode(String str) {
        if (str.length() != 30) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.length() / 2);
        for (int i = 0; i < str.length(); i += 2) {
            byteArrayOutputStream.write((this.hexString.indexOf(str.charAt(i)) << 4) | this.hexString.indexOf(str.charAt(i + 1)));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    private static String bytesToHexString(byte[] bArr, boolean z) {
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append("0x");
        }
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        char[] cArr = new char[2];
        for (int i = 0; i < bArr.length; i++) {
            cArr[0] = Character.toUpperCase(Character.forDigit((bArr[i] >>> 4) & 15, 16));
            cArr[1] = Character.toUpperCase(Character.forDigit(bArr[i] & 15, 16));
            System.out.println(cArr);
            sb.append(cArr);
        }
        return sb.toString();
    }

    private void showMessage(String str, String str2) {
        this.mDialog.setTitle(str);
        this.mDialog.setMessage(str2);
        this.mDialog.show();
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

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("NFC is not enabled. Please go to the wireless settings to enable it.");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NFCActivity.this.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NFCActivity.this.finish();
            }
        });
        builder.create().show();
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(ACTION_UPDATE_nfc);
        return intentFilter;
    }
}
