package com.shima.smartbushome.mainsetting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.kyleduo.switchbutton.SwitchButton;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.DatabaseContext;
import com.shima.smartbushome.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class DB_settingFragment extends Fragment implements View.OnClickListener {
    Button bt_delete;
    Button bt_export;
    Button bt_import;
    Button bt_share;
    AlertView deletealter;
    EditText factoryreset;
    AlertView idpassword;
    AlertView importalter;
    AlertView importconfirmalter;
    AlertView importdeletealter;
    SwitchButton lockchangeid;
    TextView lockchangeidDetail;
    SwitchButton lockshake;
    EditText newpwd1;
    EditText newpwd2;
    EditText oldpwd;
    EditText pwd;
    ProgressDialog searching;
    View view;
    String passqord = "123456";
    boolean changefalsepwdwrong = false;
    boolean init = true;
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.4
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i == -1) {
                DB_settingFragment.this.changefalsepwdwrong = true;
                DB_settingFragment.this.lockchangeid.setChecked(true);
            } else if (i == 0) {
                if (DB_settingFragment.this.pwd.getText().toString().equals(DB_settingFragment.this.passqord)) {
                    DB_settingFragment.this.savevalueInfo(DB_settingFragment.this.getActivity(), "lockchangeid", false);
                    MainActivity.islockchangeid = false;
                } else {
                    DB_settingFragment.this.changefalsepwdwrong = true;
                    DB_settingFragment.this.lockchangeid.setChecked(true);
                    Toast.makeText(DB_settingFragment.this.getActivity(), "wrong password", 0).show();
                }
            }
        }
    };
    public OnItemClickListener factoryclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.5
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i != -1 && i == 0) {
                if (DB_settingFragment.this.factoryreset.getText().toString().equals("smarthomeG4")) {
                    DB_settingFragment.this.saveidInfo(DB_settingFragment.this.getActivity(), "123456");
                    DB_settingFragment.this.passqord = "123456";
                    Toast.makeText(DB_settingFragment.this.getActivity(), "factory reset succeed", 0).show();
                    return;
                }
                Toast.makeText(DB_settingFragment.this.getActivity(), "wrong factory code", 0).show();
            }
        }
    };
    Handler closehandler = new Handler();
    Runnable run = new Runnable() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.6
        @Override // java.lang.Runnable
        public void run() {
            DB_settingFragment.this.importalter.dismiss();
        }
    };
    String importpath = "";
    String deletepath = "";
    private OnItemClickListener lockclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.7
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i != -1 && i == 0) {
                String string = DB_settingFragment.this.oldpwd.getText().toString();
                String string2 = DB_settingFragment.this.newpwd1.getText().toString();
                String string3 = DB_settingFragment.this.newpwd2.getText().toString();
                if (string.equals(DB_settingFragment.this.passqord)) {
                    if (string2.equals(string3) && string2.length() == 6) {
                        DB_settingFragment.this.saveidInfo(DB_settingFragment.this.getActivity(), string2);
                        DB_settingFragment.this.passqord = string2;
                        Toast.makeText(DB_settingFragment.this.getActivity(), "succeed", 0).show();
                        return;
                    }
                    Toast.makeText(DB_settingFragment.this.getActivity(), "please make sure 6 number and the newpassdword is the same", 0).show();
                    return;
                }
                Toast.makeText(DB_settingFragment.this.getActivity(), "old password wrong", 0).show();
            }
        }
    };
    private OnItemClickListener exitclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.8
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i != -1 && i == 0) {
                DB_settingFragment.this.deleteFile();
            }
        }
    };
    private OnItemClickListener importclick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.9
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) throws IOException {
            if (i != -1 && i == 0) {
                if (obj == DB_settingFragment.this.importdeletealter) {
                    File file = new File(DB_settingFragment.this.deletepath);
                    if (file.exists()) {
                        if (file.isFile()) {
                            file.delete();
                        }
                        file.delete();
                        Toast.makeText(DB_settingFragment.this.getActivity(), "database delete succeed", 0).show();
                        DB_settingFragment.this.closehandler.postDelayed(DB_settingFragment.this.run, 50L);
                        return;
                    }
                    Toast.makeText(DB_settingFragment.this.getActivity(), "database not exist", 0).show();
                    return;
                }
                DB_settingFragment.this.closehandler.postDelayed(DB_settingFragment.this.run, 50L);
                DB_settingFragment.this.deleteFile(DatabaseContext.dbDir + "/sbus.db-shm");
                DB_settingFragment.this.deleteFile(DatabaseContext.dbDir + "/sbus.db-wal");
                DB_settingFragment.this.copyFile(DB_settingFragment.this.importpath, DatabaseContext.dbPath);
                Toast.makeText(DB_settingFragment.this.getActivity(), "Import succeed,Please re-open this app again.", 0).show();
            }
        }
    };
    List<HashMap<String, String>> searchresult = new ArrayList();
    List<String> searchname = new ArrayList();
    private final Handler mHandler = new Handler() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.10
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 5) {
                return;
            }
            for (int i = 0; i < DB_settingFragment.this.searchresult.size(); i++) {
                DB_settingFragment.this.searchname.add(DB_settingFragment.this.searchresult.get(i).get("fileName"));
            }
            DB_settingFragment.this.importalter = new AlertView("Database list", null, "CANCEL", null, null, DB_settingFragment.this.getActivity(), AlertView.Style.Alert, null);
            ListView listView = new ListView(DB_settingFragment.this.getActivity());
            listView.setAdapter((ListAdapter) new ArrayAdapter(DB_settingFragment.this.getActivity(), R.layout.simplelistitem, DB_settingFragment.this.searchname));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.10.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                    DB_settingFragment.this.importpath = DB_settingFragment.this.searchresult.get(i2).get("path");
                    DB_settingFragment.this.importconfirmalter = new AlertView("Warning", "Are you sure to import " + DB_settingFragment.this.searchname.get(i2) + " as your database?", "CANCEL", new String[]{"YES"}, null, DB_settingFragment.this.getActivity(), AlertView.Style.Alert, DB_settingFragment.this.importclick);
                    DB_settingFragment.this.importconfirmalter.setCancelable(false);
                    DB_settingFragment.this.importconfirmalter.show();
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.10.2
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i2, long j) {
                    DB_settingFragment.this.deletepath = DB_settingFragment.this.searchresult.get(i2).get("path");
                    DB_settingFragment.this.importdeletealter = new AlertView("Warning", "Do you want to delete this file ? :" + DB_settingFragment.this.searchname.get(i2), "CANCEL", new String[]{"YES"}, null, DB_settingFragment.this.getActivity(), AlertView.Style.Alert, DB_settingFragment.this.importclick);
                    DB_settingFragment.this.importdeletealter.setCancelable(false);
                    DB_settingFragment.this.importdeletealter.show();
                    return true;
                }
            });
            DB_settingFragment.this.importalter.addExtView(listView);
            DB_settingFragment.this.importalter.show();
        }
    };

    public static DB_settingFragment newInstance() {
        return new DB_settingFragment();
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_db_setting_, viewGroup, false);
        this.bt_export = (Button) this.view.findViewById(R.id.ms_export);
        this.bt_import = (Button) this.view.findViewById(R.id.ms_import);
        this.bt_delete = (Button) this.view.findViewById(R.id.ms_delete);
        this.bt_share = (Button) this.view.findViewById(R.id.ms_share);
        this.lockchangeidDetail = (TextView) this.view.findViewById(R.id.textView59);
        this.lockchangeidDetail.setOnClickListener(this);
        this.lockchangeidDetail.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                AlertView alertView = new AlertView("Enter FactoryCode", null, "CANCEL", new String[]{"YES"}, null, DB_settingFragment.this.getActivity(), AlertView.Style.Alert, DB_settingFragment.this.factoryclick);
                DB_settingFragment.this.factoryreset = new EditText(DB_settingFragment.this.getActivity());
                alertView.addExtView(DB_settingFragment.this.factoryreset);
                alertView.show();
                return true;
            }
        });
        this.lockchangeid = (SwitchButton) this.view.findViewById(R.id.sw_lockchangeid);
        this.lockchangeid.setChecked(MainActivity.islockchangeid);
        this.lockchangeid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (DB_settingFragment.this.changefalsepwdwrong) {
                    DB_settingFragment.this.changefalsepwdwrong = false;
                    return;
                }
                if (z) {
                    DB_settingFragment.this.savevalueInfo(DB_settingFragment.this.getActivity(), "lockchangeid", DB_settingFragment.this.lockchangeid.isChecked());
                    MainActivity.islockchangeid = true;
                    return;
                }
                AlertView alertView = new AlertView("Enter Password", null, "CANCEL", new String[]{"YES"}, null, DB_settingFragment.this.getActivity(), AlertView.Style.Alert, DB_settingFragment.this.itemclick);
                DB_settingFragment.this.pwd = new EditText(DB_settingFragment.this.getActivity());
                DB_settingFragment.this.pwd.setInputType(2);
                DB_settingFragment.this.pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                DB_settingFragment.this.pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                DB_settingFragment.this.pwd.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                alertView.addExtView(DB_settingFragment.this.pwd);
                alertView.show();
            }
        });
        this.lockshake = (SwitchButton) this.view.findViewById(R.id.sw_lockshake);
        this.lockshake.setChecked(MainActivity.islockshake);
        this.lockshake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.mainsetting.DB_settingFragment.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                DB_settingFragment.this.savevalueInfo(DB_settingFragment.this.getActivity(), "lockshake", DB_settingFragment.this.lockshake.isChecked());
                MainActivity.islockshake = DB_settingFragment.this.lockshake.isChecked();
            }
        });
        this.bt_export.setOnClickListener(this);
        this.bt_import.setOnClickListener(this);
        this.bt_delete.setOnClickListener(this);
        this.bt_share.setOnClickListener(this);
        this.searching = new ProgressDialog(getContext());
        this.searching.setCancelable(true);
        this.searching.setCanceledOnTouchOutside(false);
        this.searching.setMessage("Searching Database...");
        this.searching.setProgressStyle(0);
        this.passqord = getActivity().getSharedPreferences("lockinfo", 0).getString("lockidPwd", "123456");
        return this.view;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws IOException {
        int id = view.getId();
        if (id == R.id.ms_share) {
            startActivity(new Intent("com.shima.smartbushome.databaseactivity"));
            return;
        }
        if (id != R.id.textView59) {
            switch (id) {
                case R.id.ms_delete /* 2131231440 */:
                    this.deletealter = new AlertView("Warning", "Are you sure to delete the database ?", "CANCEL", new String[]{"YES"}, null, getActivity(), AlertView.Style.Alert, this.exitclick);
                    this.deletealter.setCancelable(false);
                    this.deletealter.show();
                    Toast.makeText(getContext(), "" + this.deletepath, 0).show();
                    break;
                case R.id.ms_export /* 2131231441 */:
                    String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis()));
                    String str2 = Environment.getExternalStorageDirectory().getPath() + "/smartbus_backup/database";
                    String str3 = DatabaseContext.dbPath;
                    String str4 = Environment.getExternalStorageDirectory().getPath() + "/smartbus_backup/database/smartbus_dbbackup_" + str + ".db";
                    File file = new File(str2);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    copyFile(str3, str4);
                    Toast.makeText(getActivity(), "export database to ../smartbus_backup/database/.. successfully", 0).show();
                    break;
                case R.id.ms_import /* 2131231442 */:
                    if (this.searchresult.size() > 0) {
                        this.searchresult.clear();
                    }
                    if (this.searchname.size() > 0) {
                        this.searchname.clear();
                    }
                    new Thread(new scanfile()).start();
                    this.searching.show();
                    break;
            }
            return;
        }
        this.idpassword = new AlertView("SetID password", null, "CANCEL", new String[]{"YES"}, null, getActivity(), AlertView.Style.Alert, this.lockclick);
        View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_lockid, (ViewGroup) null);
        this.oldpwd = (EditText) viewInflate.findViewById(R.id.ed_oldidpassword);
        this.newpwd1 = (EditText) viewInflate.findViewById(R.id.ed_newidpassword1);
        this.newpwd2 = (EditText) viewInflate.findViewById(R.id.ed_newpassword2);
        this.idpassword.addExtView(viewInflate);
        this.idpassword.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void savevalueInfo(Context context, String str, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("lockinfo", 0).edit();
        editorEdit.putBoolean(str, z);
        editorEdit.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveidInfo(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("lockinfo", 0).edit();
        editorEdit.putString("lockidPwd", str);
        editorEdit.commit();
    }

    public void copyFile(String str, String str2) throws IOException {
        FileUtil.copyFile(str, str2);
    }

    public void deleteFile() {
        File file = new File(DatabaseContext.dbPath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            file.delete();
            Toast.makeText(getActivity(), "database delete succeed", 0).show();
            getActivity().finish();
            return;
        }
        Toast.makeText(getActivity(), "database not exist", 0).show();
    }

    public void deleteFile(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class scanfile implements Runnable {
        public scanfile() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DB_settingFragment.this.toSearchFiles(new File(Environment.getExternalStorageDirectory().getPath()));
            DB_settingFragment.this.mHandler.obtainMessage(5, 1, -1, "finish").sendToTarget();
            DB_settingFragment.this.searching.dismiss();
        }
    }

    public void toSearchFiles(File file) {
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                if (file2.canRead()) {
                    toSearchFiles(file2);
                }
            } else {
                try {
                    if (file2.getName().indexOf("smartbus_dbbackup") > -1) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("fileName", file2.getName());
                        map.put("path", file2.getPath());
                        this.searchresult.add(map);
                    }
                } catch (Exception unused) {
                    Toast.makeText(getActivity(), "error", 0).show();
                }
            }
        }
    }
}
