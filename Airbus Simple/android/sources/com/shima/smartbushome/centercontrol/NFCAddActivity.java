package com.shima.smartbushome.centercontrol;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.shima.smartbushome.BuildConfig;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savemarcobutton;
import com.shima.smartbushome.database.Savenfc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class NFCAddActivity extends AppCompatActivity implements View.OnClickListener {
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private NfcAdapter mAdapter;
    private AlertDialog mDialog;
    private NdefMessage mNdefPushMessage;
    private PendingIntent mPendingIntent;
    AlertView marcolistalert;
    private NdefMessage ndefMessage;
    private TextView nfcadd_actiondetail;
    private EditText nfcadd_detail;
    private EditText nfcadd_name;
    private Button nfcadd_save;
    private Spinner nfcadd_spinner;
    private EditText nfcadd_tagid;
    ArrayAdapter<String> spinneradapter;
    Savenfc thissavenfc = new Savenfc();
    Handler initdata = new Handler();
    List<String> spinnerdata = new ArrayList();
    int marcoID = 99999999;
    boolean writesucceed = false;
    String writeTagId = "";
    String newwriteTagId = "";
    List<Savemarcobutton> listmarco = new ArrayList();
    List<String> stringlist = new ArrayList();
    private String hexString = "0123456789ABCDEF";

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_nfcadd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.nfcadd_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("NFC Add");
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
        resolveIntent(getIntent());
        this.mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", (DialogInterface.OnClickListener) null).create();
        this.mAdapter = NfcAdapter.getDefaultAdapter(this);
        this.mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        this.mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord("", Locale.ENGLISH, true)});
        this.spinnerdata.clear();
        this.spinnerdata.add("Marco");
        this.spinnerdata.add("Calling");
        this.spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.spinnerdata);
        this.spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initView();
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    private void initView() {
        this.nfcadd_tagid = (EditText) findViewById(R.id.nfcadd_tagid);
        this.nfcadd_actiondetail = (TextView) findViewById(R.id.nfcadd_actiondetail);
        this.nfcadd_save = (Button) findViewById(R.id.nfcadd_save);
        this.nfcadd_save.setOnClickListener(this);
        this.nfcadd_detail = (EditText) findViewById(R.id.nfcadd_detail);
        this.nfcadd_detail.setOnClickListener(this);
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.nfcadd_detail.getWindowToken(), 0);
        this.nfcadd_name = (EditText) findViewById(R.id.nfcadd_name);
        this.nfcadd_spinner = (Spinner) findViewById(R.id.nfcadd_spinner);
        this.nfcadd_spinner.setAdapter((SpinnerAdapter) this.spinneradapter);
        this.thissavenfc.action_type = 0;
        this.nfcadd_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shima.smartbushome.centercontrol.NFCAddActivity.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                switch (i) {
                    case 0:
                        NFCAddActivity.this.nfcadd_actiondetail.setText("Marco:");
                        NFCAddActivity.this.thissavenfc.action_type = 1;
                        break;
                    case 1:
                        NFCAddActivity.this.nfcadd_actiondetail.setText("Phone Num:");
                        NFCAddActivity.this.thissavenfc.action_type = 2;
                        break;
                    case 2:
                        NFCAddActivity.this.nfcadd_actiondetail.setText("Message:");
                        NFCAddActivity.this.thissavenfc.action_type = 3;
                        break;
                }
            }
        });
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mAdapter == null) {
            try {
                if (this.mAdapter.isEnabled()) {
                    return;
                }
                showWirelessSettingsDialog();
                return;
            } catch (Exception unused) {
                showMessage("error", "No NFC found on this device");
                this.nfcadd_tagid.setText("Your device did not support NFC！");
                return;
            }
        }
        if (!this.mAdapter.isEnabled()) {
            this.nfcadd_tagid.setText("Please open your NFC function！");
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
                this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, null, (String[][]) null);
                this.mAdapter.enableForegroundNdefPush(this, this.mNdefPushMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.nfcadd_detail) {
            if (this.thissavenfc.action_type == 1) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.nfcadd_detail.getWindowToken(), 0);
                if (this.stringlist.size() > 0) {
                    this.stringlist.clear();
                }
                this.listmarco = MainActivity.mgr.querymarcobutton();
                for (int i = 0; i < this.listmarco.size(); i++) {
                    this.stringlist.add(this.listmarco.get(i).marco_remark);
                }
                this.marcolistalert = new AlertView("Select Marco to Act", null, "CANCEL", null, null, this, AlertView.Style.Alert, null);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.listdialog, (ViewGroup) null);
                ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                listView.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.stringlist));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCAddActivity.2
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view2, int i2, long j) {
                        NFCAddActivity.this.nfcadd_detail.setText(NFCAddActivity.this.listmarco.get(i2).marco_remark);
                        NFCAddActivity.this.marcoID = NFCAddActivity.this.listmarco.get(i2).marco_id;
                        NFCAddActivity.this.marcolistalert.dismiss();
                    }
                });
                this.marcolistalert.addExtView(viewGroup);
                this.marcolistalert.show();
                return;
            }
            return;
        }
        if (id != R.id.nfcadd_save) {
            return;
        }
        String strTrim = this.nfcadd_name.getText().toString().trim();
        List<Savenfc> listQuerynfc = MainActivity.mgr.querynfc();
        int i2 = listQuerynfc.size() > 0 ? listQuerynfc.get(listQuerynfc.size() - 1).nfc_id + 1 : 0;
        if (strTrim.length() > 0) {
            this.thissavenfc.nfc_name = strTrim;
            String strTrim2 = this.nfcadd_tagid.getText().toString().trim();
            if (strTrim2.length() > 0) {
                this.thissavenfc.nfc_content = strTrim2;
                switch (this.thissavenfc.action_type) {
                    case 0:
                        Toast.makeText(this, "please select 1 control type", 0).show();
                        break;
                    case 1:
                        if (this.marcoID != 99999999) {
                            this.thissavenfc.nfc_id = i2;
                            this.thissavenfc.nfc_icon = "";
                            this.thissavenfc.state = 0;
                            this.thissavenfc.call_num = "";
                            this.thissavenfc.message = "";
                            this.thissavenfc.resume1 = 0;
                            this.thissavenfc.resume2 = "";
                            this.thissavenfc.delaytime = 0;
                            this.thissavenfc.marco_ID = this.marcoID;
                            this.thissavenfc.marco_name = this.nfcadd_detail.getText().toString().trim();
                            MainActivity.mgr.addnfc(this.thissavenfc);
                            broadcastUpdate(NFCActivity.ACTION_UPDATE_nfc);
                            finish();
                            break;
                        } else {
                            Toast.makeText(this, "please select 1 marco", 0).show();
                            break;
                        }
                    case 2:
                        String strTrim3 = this.nfcadd_detail.getText().toString().trim();
                        if (strTrim3.length() > 0) {
                            this.thissavenfc.call_num = strTrim3;
                            this.thissavenfc.nfc_id = i2;
                            this.thissavenfc.nfc_icon = "";
                            this.thissavenfc.state = 0;
                            this.thissavenfc.message = "";
                            this.thissavenfc.resume1 = 0;
                            this.thissavenfc.resume2 = "";
                            this.thissavenfc.delaytime = 0;
                            this.thissavenfc.marco_ID = 0;
                            this.thissavenfc.marco_name = "";
                            MainActivity.mgr.addnfc(this.thissavenfc);
                            broadcastUpdate(NFCActivity.ACTION_UPDATE_nfc);
                            finish();
                            break;
                        } else {
                            Toast.makeText(this, "please input your phone num", 0).show();
                            break;
                        }
                    case 3:
                        if (this.nfcadd_detail.getText().toString().trim().length() <= 0) {
                            Toast.makeText(this, "please input your message", 0).show();
                            break;
                        }
                        break;
                }
                return;
            }
            Toast.makeText(this, "please put your NFC card on you phone", 0).show();
            return;
        }
        Toast.makeText(this, "please input your name", 0).show();
    }

    private void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
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
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCAddActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NFCAddActivity.this.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCAddActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                NFCAddActivity.this.finish();
            }
        });
        builder.create().show();
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if ("android.nfc.action.TAG_DISCOVERED".equals(action) || "android.nfc.action.TECH_DISCOVERED".equals(action) || "android.nfc.action.NDEF_DISCOVERED".equals(action)) {
            Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
            if (parcelableArrayExtra != null) {
                NdefMessage[] ndefMessageArr = new NdefMessage[parcelableArrayExtra.length];
                for (int i = 0; i < parcelableArrayExtra.length; i++) {
                    ndefMessageArr[i] = (NdefMessage) parcelableArrayExtra[i];
                }
                return;
            }
            this.nfcadd_tagid.setText(dumpTagData(intent.getParcelableExtra("android.nfc.extra.TAG")));
        }
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

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) throws IOException, FormatException {
        setIntent(intent);
        Parcelable parcelableExtra = intent.getParcelableExtra("android.nfc.extra.TAG");
        this.newwriteTagId = dumpTagData(parcelableExtra);
        if (this.newwriteTagId.equals(this.writeTagId)) {
            if (!this.writesucceed) {
                onWriteNFC((Tag) parcelableExtra);
            }
        } else {
            this.writeTagId = this.newwriteTagId;
            onWriteNFC((Tag) parcelableExtra);
        }
        this.nfcadd_tagid.setText(dumpTagData(parcelableExtra));
    }

    public void onWriteNFC(final Tag tag) throws IOException, FormatException {
        if (tag == null) {
            Toast.makeText(this, "did not find TAG", 0).show();
            return;
        }
        Toast.makeText(this, "tag had value", 0).show();
        this.ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord.createApplicationRecord(BuildConfig.APPLICATION_ID)});
        int length = this.ndefMessage.toByteArray().length;
        Toast.makeText(this, "length need to write" + length, 0).show();
        Ndef ndef = Ndef.get(tag);
        if (ndef != null) {
            try {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "NFC did not support write data", 0).show();
                    return;
                } else {
                    if (ndef.getMaxSize() < length) {
                        Toast.makeText(this, "NFC card did not have enough space", 0).show();
                        return;
                    }
                    ndef.writeNdefMessage(this.ndefMessage);
                    Toast.makeText(this, "NFC card write succeed", 0).show();
                    this.writesucceed = true;
                    return;
                }
            } catch (FormatException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, "this card is not ndef format", 0).show();
        new AlertDialog.Builder(this).setTitle("do you want to format to ndef and write the package_name?").setPositiveButton("YES", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.NFCAddActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws IOException, FormatException {
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable == null) {
                    Toast.makeText(NFCAddActivity.this, "this card can not format into ndef", 0).show();
                    return;
                }
                try {
                    ndefFormatable.connect();
                    ndefFormatable.format(NFCAddActivity.this.ndefMessage);
                    Toast.makeText(NFCAddActivity.this, "NFC car write succeed!", 0).show();
                } catch (FormatException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }).setNegativeButton("NO", (DialogInterface.OnClickListener) null).create().show();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
