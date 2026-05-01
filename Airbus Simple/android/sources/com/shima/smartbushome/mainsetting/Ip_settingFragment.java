package com.shima.smartbushome.mainsetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import com.shima.smartbushome.udp.udp_socket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class Ip_settingFragment extends Fragment implements View.OnClickListener {
    TextView ams_ddnsipvalue;
    Button ams_getddnsip;
    CheckBox cha;
    CheckBox ddnscha;
    EditText ddnstarget;
    EditText local;
    Button save;
    EditText target;
    View view;
    int c1 = 0;
    int c2 = 0;
    public CompoundButton.OnCheckedChangeListener usetarip = new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.mainsetting.Ip_settingFragment.1
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (Ip_settingFragment.this.c1 == 1) {
                Ip_settingFragment.this.c1 = 0;
            } else if (Ip_settingFragment.this.ddnscha.isChecked()) {
                Ip_settingFragment.this.c2 = 1;
                Ip_settingFragment.this.ddnscha.setChecked(false);
            }
        }
    };
    public CompoundButton.OnCheckedChangeListener useddns = new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.mainsetting.Ip_settingFragment.2
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (Ip_settingFragment.this.c2 == 1) {
                Ip_settingFragment.this.c2 = 0;
            } else if (Ip_settingFragment.this.cha.isChecked()) {
                Ip_settingFragment.this.c1 = 1;
                Ip_settingFragment.this.cha.setChecked(false);
            }
        }
    };

    public static Ip_settingFragment newInstance() {
        return new Ip_settingFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_ip_setting, viewGroup, false);
        this.local = (EditText) this.view.findViewById(R.id.ams_localip_ed);
        this.target = (EditText) this.view.findViewById(R.id.ams_targetip_ed);
        this.ams_ddnsipvalue = (TextView) this.view.findViewById(R.id.ams_ddnsipvalue);
        this.cha = (CheckBox) this.view.findViewById(R.id.ams_cb);
        this.ddnscha = (CheckBox) this.view.findViewById(R.id.ams_useddns);
        this.ddnstarget = (EditText) this.view.findViewById(R.id.ams_ddnssite);
        this.cha.setOnCheckedChangeListener(this.usetarip);
        this.ddnscha.setOnCheckedChangeListener(this.useddns);
        try {
            if (udp_socket.mansetip) {
                this.local.setText(bytetostring(udp_socket.manlocalip));
                this.target.setText(bytetostring(udp_socket.mantargetip));
                this.cha.setChecked(true);
            } else {
                this.local.setText(bytetostring(MainActivity.mydupsocket.GetLocalIP()));
                this.target.setText(bytetostring(MainActivity.mydupsocket.GetTargetIP(MainActivity.mydupsocket.GetLocalIP())));
                this.cha.setChecked(false);
            }
            if (udp_socket.ddnssetip) {
                FragmentActivity activity = getActivity();
                getActivity();
                this.ddnstarget.setText(activity.getSharedPreferences("ddnsconfig", 0).getString("ddnswebsite", ""));
                this.ams_ddnsipvalue.setText(bytetostring(udp_socket.ddnstargetip));
                this.ddnscha.setChecked(true);
            } else {
                this.ddnscha.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.save = (Button) this.view.findViewById(R.id.ams_save);
        this.ams_getddnsip = (Button) this.view.findViewById(R.id.ams_getddnsip);
        this.save.setOnClickListener(this);
        this.ams_getddnsip.setOnClickListener(this);
        return this.view;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ams_getddnsip) {
            if (this.ddnstarget.getText().toString().trim().length() < 1) {
                Toast.makeText(getActivity(), "please enter website name", 0).show();
                return;
            } else {
                new Thread(new getDDNSIP()).start();
                return;
            }
        }
        if (id != R.id.ams_save) {
            return;
        }
        if (this.cha.isChecked()) {
            udp_socket.manlocalip = stringtobyte(this.local.getText().toString().trim());
            udp_socket.mantargetip = stringtobyte(this.target.getText().toString().trim());
            savevalueInfo(getActivity(), "ipconfig", "manlocalip", this.local.getText().toString().trim());
            savevalueInfo(getActivity(), "ipconfig", "mantargetip", this.target.getText().toString().trim());
        }
        if (this.ddnscha.isChecked()) {
            udp_socket.ddnstargetip = stringtobyte(this.ams_ddnsipvalue.getText().toString().trim());
            savevalueInfo(getActivity(), "ddnsconfig", "ddnswebsite", this.ddnstarget.getText().toString().trim());
            savevalueInfo(getActivity(), "ddnsconfig", "ddnstargetip", this.ams_ddnsipvalue.getText().toString().trim());
        }
        if (this.cha.isChecked() || this.ddnscha.isChecked()) {
            saveLoginInfo(getActivity(), "httpconfig", "ipset", false);
            udp_socket.rsipip = false;
        }
        saveLoginInfo(getActivity(), "ipconfig", "ipset", this.cha.isChecked());
        saveLoginInfo(getActivity(), "ddnsconfig", "ipset", this.ddnscha.isChecked());
        Toast.makeText(getActivity(), "save succeed", 0).show();
        udp_socket.mansetip = this.cha.isChecked();
        udp_socket.ddnssetip = this.ddnscha.isChecked();
    }

    public void saveLoginInfo(Context context, String str, String str2, boolean z) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putBoolean(str2, z);
        editorEdit.commit();
    }

    public void savevalueInfo(Context context, String str, String str2, String str3) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        editorEdit.putString(str2, str3);
        editorEdit.commit();
    }

    public String bytetostring(byte[] bArr) {
        return String.valueOf(bArr[0] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + String.valueOf(bArr[1] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + String.valueOf(bArr[2] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + String.valueOf(bArr[3] & 255);
    }

    public byte[] stringtobyte(String str) {
        String[] strArrSplit = str.split("\\.");
        return new byte[]{(byte) Integer.parseInt(strArrSplit[0]), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit[3])};
    }

    public class getDDNSIP implements Runnable {
        public getDDNSIP() {
        }

        @Override // java.lang.Runnable
        public void run() throws UnknownHostException {
            try {
                Ip_settingFragment.this.ams_ddnsipvalue.setText(InetAddress.getByName(new URL("http://" + Ip_settingFragment.this.ddnstarget.getText().toString()).getHost()).getHostAddress());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
