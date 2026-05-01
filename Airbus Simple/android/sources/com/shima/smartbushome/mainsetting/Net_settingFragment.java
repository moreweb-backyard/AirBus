package com.shima.smartbushome.mainsetting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.data.GsonImpl;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.impl.huc.HttpUrlClient;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.UrlEncodedFormBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.litesuits.http.utils.HttpUtil;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.XMLParser;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.alexbykov.nopermission.PermissionHelper;

/* loaded from: classes.dex */
public class Net_settingFragment extends Fragment implements View.OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final byte EGetIPLListSH = 3;
    public static final byte EGetURLListSH = 0;
    public static final byte ELoginFailSystemError = 8;
    public static final byte ELoginFailUsrHadLoginOnOtherPhone = 7;
    public static final byte ELoginFail_CanNotUseUser = 6;
    public static final byte ELoginFail_NoSuchUsrName = 4;
    public static final byte ELoginFail_PasswordIsWrong = 5;
    public static final byte ELoginInSH = 1;
    public static final byte ELoginOutSH = 2;
    public static final byte ELoginSuccess_Andriod = 3;
    public static final byte ELoginSuccess_IOS = 2;
    public static final byte ELoginSuccess_PC = 1;
    public static final byte ELoginSuccess_Same = 0;
    public static final byte EModifyAliasOfRSIP = 7;
    public static final byte EModifyPassword = 5;
    public static final byte EResetPassword = 4;
    public static final byte EgetCaptcha = 6;
    protected static LiteHttp liteHttp;
    Button bt_g4_server_save;
    Button bt_login;
    Button bt_logout;
    Button bt_modifypassword;
    Button bt_save;
    Button bt_select;
    CheckBox cb_usehttp;
    AlertView deletealter;
    AlertView deletewifiname;
    final StringRequest devicelistRequest;
    AlertView devicelistalter;
    EditText et_Newpassword;
    EditText et_Oldpassword;
    EditText et_Username;
    EditText et_password;
    EditText et_realm;
    EditText et_username;
    final StringRequest logoutRequest;
    final StringRequest modifyPasswordRequest;
    Button ms_ns_deletewifiname;
    Button ms_ns_savewifiname;
    TextView ms_ns_wifinametext;
    private PermissionHelper permissionHelper;
    ProgressDialog postProgress;
    final StringRequest postRequest;
    final StringRequest sentdataRequest;
    AlertView settingalter;
    View view;
    List<String> devicelistname = new ArrayList();
    List<HashMap<String, String>> devicelist = new ArrayList();
    public final int OP_LOGIN = 1;
    public final int OP_LOGOUT = 2;
    public final int OP_IP_LIST = 3;
    public final int OP_SERVER_LIST = 4;
    public String g4Realm = "www.g4cloud.ir";
    public String IP = "http://" + this.g4Realm;
    public final byte TERMINAL_TYPE = 3;
    private String wifiname = "";
    byte[] data = {-94, -112, 66, -125, 83, 77, 65, 82, 84, 67, 76, 79, 85, 68, -86, -86, 24, -69, -69, -35, -35, 0, 49, 1, -43, 1, 80, 0, 0, 2, 83, 3, 0, 0, 0, 0, 122, 87, 0, 0};
    byte[] tarip = {-94, -112, 66, -125};
    String target = this.IP + "";
    String getdevicelist = this.IP + ":8888/DDNSServerService.asmx/GetDeviceList";
    String loginUrl = this.IP + ":8888/DDNSServerService.asmx/UserLogin";
    String logoutUrl = this.IP + ":8888/DDNSServerService.asmx/UserLogout";
    String modifyPasswordUrl = this.IP + ":8888/DDNSServerService.asmx/ModifyPassword";
    String choosedevice = "";
    public OnItemClickListener saveClick = new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.7
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i == 0) {
                String string = Net_settingFragment.this.et_Username.getText().toString();
                String strTrim = Net_settingFragment.this.et_Oldpassword.getText().toString().trim();
                String strTrim2 = Net_settingFragment.this.et_Newpassword.getText().toString().trim();
                LinkedList linkedList = new LinkedList();
                linkedList.add(new NameValuePair("userName", string));
                linkedList.add(new NameValuePair("oldPassword", strTrim));
                linkedList.add(new NameValuePair("newPassword", strTrim2));
                linkedList.add(new NameValuePair("terminalType", "3"));
                Net_settingFragment.this.modifyPasswordRequest.setHttpBody(new UrlEncodedFormBody(linkedList));
                Net_settingFragment.liteHttp.getConfig().setBaseUrl(Net_settingFragment.this.modifyPasswordUrl);
                Net_settingFragment.liteHttp.executeAsync(Net_settingFragment.this.modifyPasswordRequest);
                return;
            }
            Net_settingFragment.this.settingalter.dismiss();
        }
    };

    public static Net_settingFragment newInstance() {
        return new Net_settingFragment();
    }

    public Net_settingFragment() {
        boolean z = true;
        boolean z2 = false;
        this.postRequest = (StringRequest) new StringRequest(this.loginUrl).setMethod(HttpMethods.Post).setHttpListener(new HttpListener<String>(z, z2, z) { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.3
            @Override // com.litesuits.http.listener.HttpListener
            public void onStart(AbstractRequest<String> abstractRequest) {
                super.onStart(abstractRequest);
                Net_settingFragment.this.postProgress.show();
            }

            @Override // com.litesuits.http.listener.HttpListener
            public void onSuccess(String str, Response<String> response) throws DOMException, NumberFormatException {
                String str2;
                String nodeValue = new XMLParser().getDomElement(str).getElementsByTagName("unsignedByte").item(0).getChildNodes().item(0).getNodeValue();
                if (nodeValue == null || nodeValue.equals("")) {
                    return;
                }
                byte b = Byte.parseByte(nodeValue);
                if (b == 0 || b == 1 || b == 2 || b == 3) {
                    str2 = "Login Success";
                } else if (b == 4) {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = "Login Fail User Name Error ";
                } else if (b == 5) {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = "Login Fail Password Error ";
                } else if (b == 6) {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = "Login Fail Can not use This User  ";
                } else if (b == 7) {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = "Login Fail User Had Login On Other Phone  ";
                } else if (b == 8) {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = "Login Fail System Error";
                } else {
                    Net_settingFragment.this.postProgress.dismiss();
                    str2 = " Error";
                }
                Toast.makeText(Net_settingFragment.this.getActivity(), str2, 1).show();
                if (b == 0 || b == 1 || b == 2 || b == 3) {
                    LinkedList linkedList = new LinkedList();
                    linkedList.add(new NameValuePair("userName", Net_settingFragment.this.et_username.getText().toString()));
                    linkedList.add(new NameValuePair("password", Net_settingFragment.this.et_password.getText().toString()));
                    linkedList.add(new NameValuePair("terminalType", "3"));
                    Net_settingFragment.this.devicelistRequest.setHttpBody(new UrlEncodedFormBody(linkedList));
                    Net_settingFragment.liteHttp.getConfig().setBaseUrl(Net_settingFragment.this.getdevicelist);
                    Net_settingFragment.liteHttp.executeAsync(Net_settingFragment.this.devicelistRequest);
                }
            }
        });
        this.logoutRequest = (StringRequest) new StringRequest(this.logoutUrl).setMethod(HttpMethods.Post).setHttpListener(new HttpListener<String>(z, z2, z) { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.4
            @Override // com.litesuits.http.listener.HttpListener
            public void onStart(AbstractRequest<String> abstractRequest) {
                super.onStart(abstractRequest);
            }

            @Override // com.litesuits.http.listener.HttpListener
            public void onSuccess(String str, Response<String> response) throws DOMException, NumberFormatException {
                String nodeValue = new XMLParser().getDomElement(str).getElementsByTagName("unsignedByte").item(0).getChildNodes().item(0).getNodeValue();
                if (nodeValue == null || nodeValue.equals("")) {
                    return;
                }
                byte b = Byte.parseByte(nodeValue);
                String str2 = (b == 0 || b == 1 || b == 2 || b == 3) ? "LogOut Success" : b == 4 ? "LogOut Fail User Name Error " : b == 5 ? "LogOut Fail Password Error " : b == 6 ? "LogOut Fail Can not use This User  " : b == 7 ? "LogOut Fail User Had Login On Other Phone  " : b == 8 ? "LogOut Fail System Error" : " Error";
                Toast.makeText(Net_settingFragment.this.getActivity(), str2, 1).show();
            }
        });
        this.devicelistRequest = (StringRequest) new StringRequest(this.getdevicelist).setMethod(HttpMethods.Post).setHttpListener(new HttpListener<String>(z, z2, z) { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.5
            @Override // com.litesuits.http.listener.HttpListener
            public void onSuccess(String str, Response<String> response) {
                Net_settingFragment.this.postProgress.dismiss();
                XMLParser xMLParser = new XMLParser();
                NodeList elementsByTagName = xMLParser.getDomElement(str).getElementsByTagName("DeviceList");
                Net_settingFragment.this.devicelist.clear();
                Net_settingFragment.this.devicelistname.clear();
                for (int i = 0; i < elementsByTagName.getLength(); i++) {
                    Element element = (Element) elementsByTagName.item(i);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("MACAddress", xMLParser.getValue(element, "MACAddress"));
                    map.put("Alias", xMLParser.getValue(element, "Alias"));
                    Net_settingFragment.this.devicelistname.add(xMLParser.getValue(element, "Alias"));
                    Net_settingFragment.this.devicelist.add(map);
                }
                Net_settingFragment.this.devicelistalter = new AlertView("RSIP List", null, "CANCEL", null, null, Net_settingFragment.this.getActivity(), AlertView.Style.Alert, null);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(Net_settingFragment.this.getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                listView.setAdapter((ListAdapter) new ArrayAdapter(Net_settingFragment.this.getActivity(), R.layout.simplelistitem, Net_settingFragment.this.devicelistname));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.5.1
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        Net_settingFragment.this.bt_select.setText(Net_settingFragment.this.devicelistname.get(i2));
                        Net_settingFragment.this.choosedevice = Net_settingFragment.this.devicelist.get(i2).get("MACAddress");
                        Net_settingFragment.this.devicelistalter.dismiss();
                    }
                });
                Net_settingFragment.this.devicelistalter.addExtView(viewGroup);
                Net_settingFragment.this.devicelistalter.show();
            }
        });
        this.sentdataRequest = (StringRequest) new StringRequest(this.target).setMethod(HttpMethods.Post).setHttpListener(new HttpListener<String>(z, z2, z) { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.6
            @Override // com.litesuits.http.listener.HttpListener
            public void onSuccess(String str, Response<String> response) {
                HttpUtil.showTips(Net_settingFragment.this.getActivity(), "result", response.getResult() + "");
                response.printInfo();
            }
        });
        this.modifyPasswordRequest = (StringRequest) new StringRequest(this.modifyPasswordUrl).setMethod(HttpMethods.Post).setHttpListener(new HttpListener<String>(z, z2, z) { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.8
            @Override // com.litesuits.http.listener.HttpListener
            public void onSuccess(String str, Response<String> response) {
                if ("true".equals(new XMLParser().getDomElement(str).getDocumentElement().getTextContent())) {
                    Toast.makeText(Net_settingFragment.this.getContext(), "Modify Password Success", 0).show();
                    Net_settingFragment.this.settingalter.dismiss();
                } else {
                    Toast.makeText(Net_settingFragment.this.getContext(), "Modify Password Fail", 0).show();
                }
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_net_setting, viewGroup, false);
        initView();
        inithttp();
        this.permissionHelper = new PermissionHelper(this);
        getWifiSSidPermissions();
        return this.view;
    }

    public void initView() {
        this.bt_login = (Button) this.view.findViewById(R.id.ms_ns_login);
        this.bt_logout = (Button) this.view.findViewById(R.id.ms_ns_logout);
        this.bt_modifypassword = (Button) this.view.findViewById(R.id.bt_modifypassword);
        this.bt_select = (Button) this.view.findViewById(R.id.ms_ns_select);
        this.bt_save = (Button) this.view.findViewById(R.id.ms_ns_save);
        this.bt_g4_server_save = (Button) this.view.findViewById(R.id.bt_g4_server_save);
        this.ms_ns_savewifiname = (Button) this.view.findViewById(R.id.ms_ns_savewifiname);
        this.ms_ns_deletewifiname = (Button) this.view.findViewById(R.id.ms_ns_deletewifiname);
        this.ms_ns_wifinametext = (TextView) this.view.findViewById(R.id.ms_ns_wifinametext);
        this.bt_login.setOnClickListener(this);
        this.bt_logout.setOnClickListener(this);
        this.bt_modifypassword.setOnClickListener(this);
        this.bt_select.setOnClickListener(this);
        this.bt_save.setOnClickListener(this);
        this.bt_g4_server_save.setOnClickListener(this);
        this.ms_ns_deletewifiname.setOnClickListener(this);
        this.ms_ns_savewifiname.setOnClickListener(this);
        this.et_username = (EditText) this.view.findViewById(R.id.ms_ns_etname);
        this.et_password = (EditText) this.view.findViewById(R.id.ms_ns_etpassword);
        this.et_realm = (EditText) this.view.findViewById(R.id.et_realm);
        this.cb_usehttp = (CheckBox) this.view.findViewById(R.id.ms_ns_cb);
        this.postProgress = new ProgressDialog(getActivity());
        this.postProgress.setMessage("logining");
        this.postProgress.setCancelable(true);
        this.postProgress.setProgressStyle(0);
        if (udp_socket.rsipip) {
            this.cb_usehttp.setChecked(true);
        }
        FragmentActivity activity = getActivity();
        getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("httpconfig", 0);
        this.et_username.setText(sharedPreferences.getString("username", ""));
        this.et_password.setText(sharedPreferences.getString("password", ""));
        this.bt_select.setText(sharedPreferences.getString("MacAddress", ""));
        FragmentActivity activity2 = getActivity();
        getActivity();
        this.wifiname = activity2.getSharedPreferences("wifiinfo", 0).getString("wifiname", "not set wifiname");
        this.ms_ns_wifinametext.setText("Remember your current wifi: " + this.wifiname);
        FragmentActivity activity3 = getActivity();
        getActivity();
        this.g4Realm = activity3.getSharedPreferences("remoteinfo", 0).getString("g4Realm", "g4cloud.ir");
        this.et_realm.setText(this.g4Realm);
        this.IP = "http://" + this.g4Realm;
        this.et_realm.setText("" + this.g4Realm);
    }

    public void inithttp() {
        if (liteHttp == null) {
            liteHttp = LiteHttp.build(getActivity()).setHttpClient(new HttpUrlClient()).setJsonConvertor(new GsonImpl()).setDebugged(true).setDoStatistics(true).setDetectNetwork(true).setUserAgent("Mozilla/5.0 (...)").setSocketTimeout(10000).setConnectTimeout(10000).create();
        } else {
            liteHttp.getConfig().setSocketTimeout(5000).setConnectTimeout(5000);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.ms_ns_deletewifiname) {
            switch (id) {
                case R.id.bt_g4_server_save /* 2131230843 */:
                    if (this.et_realm.getText().toString().trim().length() > 0) {
                        savevalueInfo(getActivity(), "remoteinfo", "g4Realm", this.et_realm.getText().toString().trim());
                        Toast.makeText(getActivity(), "saved", 0).show();
                        this.g4Realm = this.et_realm.getText().toString().trim();
                        this.IP = "http://" + this.g4Realm;
                        break;
                    } else {
                        this.et_realm.setText("");
                        Toast.makeText(getActivity(), "Input cannot be empty", 0).show();
                        break;
                    }
                case R.id.bt_modifypassword /* 2131230844 */:
                    showModifyPasswordView();
                    break;
                default:
                    switch (id) {
                        case R.id.ms_ns_login /* 2131231447 */:
                            if (this.et_username.getText().toString() != null && !this.et_username.getText().toString().equals("") && this.et_password.getText().toString() != null && !this.et_password.getText().toString().equals("")) {
                                LinkedList linkedList = new LinkedList();
                                linkedList.add(new NameValuePair("userName", this.et_username.getText().toString()));
                                linkedList.add(new NameValuePair("password", this.et_password.getText().toString()));
                                linkedList.add(new NameValuePair("terminalType", "3"));
                                this.postRequest.setHttpBody(new UrlEncodedFormBody(linkedList));
                                liteHttp.getConfig().setBaseUrl(this.loginUrl);
                                liteHttp.executeAsync(this.postRequest);
                                break;
                            }
                            break;
                        case R.id.ms_ns_logout /* 2131231448 */:
                            if (this.et_username.getText().toString() != null && !this.et_username.getText().toString().equals("") && this.et_password.getText().toString() != null && !this.et_password.getText().toString().equals("")) {
                                LinkedList linkedList2 = new LinkedList();
                                linkedList2.add(new NameValuePair("userName", this.et_username.getText().toString()));
                                linkedList2.add(new NameValuePair("password", this.et_password.getText().toString()));
                                linkedList2.add(new NameValuePair("terminalType", "3"));
                                this.logoutRequest.setHttpBody(new UrlEncodedFormBody(linkedList2));
                                liteHttp.getConfig().setBaseUrl(this.logoutUrl);
                                liteHttp.executeAsync(this.logoutRequest);
                            }
                            try {
                                this.et_username.setText("");
                                this.et_password.setText("");
                                savevalueInfo(getActivity(), "httpconfig", "MacAddress", "");
                                savevalueInfo(getActivity(), "httpconfig", "username", "");
                                savevalueInfo(getActivity(), "httpconfig", "password", "");
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        case R.id.ms_ns_save /* 2131231449 */:
                            if (this.cb_usehttp.isChecked()) {
                                saveLoginInfo(getActivity(), "ipconfig", "ipset", false);
                                saveLoginInfo(getActivity(), "ddnsconfig", "ipset", false);
                                if (this.choosedevice.equals("")) {
                                    this.choosedevice = this.bt_select.getText().toString().trim();
                                }
                                savevalueInfo(getActivity(), "httpconfig", "MacAddress", this.choosedevice);
                                savevalueInfo(getActivity(), "httpconfig", "username", this.et_username.getText().toString());
                                savevalueInfo(getActivity(), "httpconfig", "password", this.et_password.getText().toString());
                                udp_socket.MacAddress = stringtoMACbyte(this.choosedevice);
                                udp_socket.mansetip = false;
                                udp_socket.ddnssetip = false;
                            }
                            udp_socket.rsipip = this.cb_usehttp.isChecked();
                            saveLoginInfo(getActivity(), "httpconfig", "ipset", this.cb_usehttp.isChecked());
                            Toast.makeText(getActivity(), "saved", 0).show();
                            if (MainActivity.mydupsocket != null) {
                                MainActivity.mydupsocket.StopAllThread();
                                MainActivity.mydupsocket.initprocess();
                                break;
                            }
                            break;
                        case R.id.ms_ns_savewifiname /* 2131231450 */:
                            this.wifiname = getWIFISSID(getActivity());
                            if (this.wifiname != null && !this.wifiname.equals("")) {
                                this.deletealter = new AlertView("Save WifiName", "Your Current Wifi is " + this.wifiname + " right?", "CANCEL", new String[]{"YES"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.1
                                    @Override // com.bigkoo.alertview.OnItemClickListener
                                    public void onItemClick(Object obj, int i) {
                                        if (i != -1 && i == 0) {
                                            Net_settingFragment.this.ms_ns_wifinametext.setText("Remember your current wifi: " + Net_settingFragment.this.wifiname);
                                            Net_settingFragment.this.savevalueInfo(Net_settingFragment.this.getActivity(), "wifiinfo", "wifiname", Net_settingFragment.this.wifiname);
                                        }
                                    }
                                });
                                this.deletealter.setCancelable(true);
                                this.deletealter.show();
                                break;
                            } else {
                                Toast.makeText(getActivity(), "you are not connecting to a wifi", 0).show();
                                break;
                            }
                            break;
                        case R.id.ms_ns_select /* 2131231451 */:
                            if (this.devicelistalter != null) {
                                this.devicelistalter.show();
                                break;
                            }
                            break;
                    }
            }
        }
        this.deletewifiname = new AlertView("Delete WifiName", "You want to delete your wifiname " + this.wifiname + " right?", "CANCEL", new String[]{"YES"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.2
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i != -1 && i == 0) {
                    Net_settingFragment.this.ms_ns_wifinametext.setText("Remember your current wifi: ");
                    Net_settingFragment.this.savevalueInfo(Net_settingFragment.this.getActivity(), "wifiinfo", "wifiname", "");
                }
            }
        });
        this.deletewifiname.setCancelable(true);
        this.deletewifiname.show();
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

    public byte[] stringtoMACbyte(String str) {
        String[] strArrSplit = str.split("\\.");
        return new byte[]{(byte) Integer.parseInt(strArrSplit[0], 16), (byte) Integer.parseInt(strArrSplit[1], 16), (byte) Integer.parseInt(strArrSplit[2], 16), (byte) Integer.parseInt(strArrSplit[3], 16), (byte) Integer.parseInt(strArrSplit[4], 16), (byte) Integer.parseInt(strArrSplit[5], 16), (byte) Integer.parseInt(strArrSplit[6], 16), (byte) Integer.parseInt(strArrSplit[7], 16)};
    }

    private void showModifyPasswordView() {
        this.settingalter = new AlertView("Modify Password", null, "CANCEL", new String[]{"SUBMIT"}, null, getContext(), AlertView.Style.Alert, this.saveClick);
        this.settingalter.setShoulddismiss(false);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.setting_modifpassword, (ViewGroup) null);
        this.et_Username = (EditText) viewGroup.findViewById(R.id.et_username);
        this.et_Oldpassword = (EditText) viewGroup.findViewById(R.id.et_oldpassword);
        this.et_Newpassword = (EditText) viewGroup.findViewById(R.id.et_newpassword);
        this.settingalter.addExtView(viewGroup);
        this.settingalter.show();
    }

    public String getWIFISSID(Activity activity) {
        if (Build.VERSION.SDK_INT <= 26 || Build.VERSION.SDK_INT == 28) {
            WifiInfo connectionInfo = ((WifiManager) activity.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
            if (Build.VERSION.SDK_INT < 19) {
                return connectionInfo.getSSID();
            }
            return connectionInfo.getSSID().replace("\"", "");
        }
        if (Build.VERSION.SDK_INT == 27) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) activity.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo.isConnected() && activeNetworkInfo.getExtraInfo() != null) {
                return activeNetworkInfo.getExtraInfo().replace("\"", "");
            }
        }
        return "unknown id";
    }

    private void getWifiSSidPermissions() {
        this.permissionHelper.check("android.permission.ACCESS_FINE_LOCATION").onSuccess(new Runnable() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.11
            @Override // java.lang.Runnable
            public void run() {
                Log.d("getWifiSSidPermissions", "Android9.0申请Wifi权限成功");
            }
        }).onDenied(new Runnable() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.10
            @Override // java.lang.Runnable
            public void run() {
            }
        }).onNeverAskAgain(new Runnable() { // from class: com.shima.smartbushome.mainsetting.Net_settingFragment.9
            @Override // java.lang.Runnable
            public void run() {
            }
        }).run();
    }

    @Override // android.support.v4.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.permissionHelper.onRequestPermissionsResult(i, strArr, iArr);
    }
}
