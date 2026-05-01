package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.adapter.PlayBackAdapter;
import com.pzj.ipcdemo.bean.PlayBackBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.shima.smartbushome.assist.CrashHandle.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class PlayBackTFActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, BridgeService.PlayBackTFInterface {
    private Button btnBack;
    private EditText editDateBegin;
    private EditText editDateEnd;
    private ListView listView;
    private Button loadMoreButton;
    public View loadMoreView;
    private PlayBackAdapter mAdapter;
    private ProgressDialog progressDialog;
    private String strDID;
    private String strName;
    private TextView tvNoVideo;
    private TextView tvTitle;
    private int TIMEOUT = 2000;
    private final int PARAMS = 1;
    private boolean successFlag = false;
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            PlayBackTFActivity.this.successFlag = true;
            if (PlayBackTFActivity.this.progressDialog.isShowing()) {
                PlayBackTFActivity.this.progressDialog.cancel();
            }
            PlayBackTFActivity.this.mAdapter.notifyDataSetChanged();
        }
    };
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (PlayBackTFActivity.this.successFlag) {
                return;
            }
            PlayBackTFActivity.this.progressDialog.dismiss();
            if (PlayBackTFActivity.this.mAdapter.getCount() > 0) {
                Log.i("info", "Video");
                PlayBackTFActivity.this.listView.setVisibility(0);
                PlayBackTFActivity.this.tvNoVideo.setVisibility(8);
            } else {
                Log.i("info", "noVideo");
                PlayBackTFActivity.this.tvNoVideo.setVisibility(0);
                PlayBackTFActivity.this.listView.setVisibility(8);
            }
        }
    };
    private Handler handler = new Handler();
    private int fileTFCount = 0;
    private int totalSize = 0;
    private int getCurrentPageIndex = 0;
    private int TotalPageSize = 0;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getDataFromOther();
        setContentView(R.layout.playbacktf);
        findView();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.remote_video_getparams));
        this.progressDialog.show();
        this.mHandler.postDelayed(this.runnable, this.TIMEOUT);
        setListener();
        this.tvTitle.setText(this.strName);
        initDate();
        this.mAdapter = new PlayBackAdapter(this, this);
        BridgeService.setPlayBackTFInterface(this);
        NativeCaller.PPPPGetSDCardRecordFileList(this.strDID, 0, 500);
        this.listView.setAdapter((ListAdapter) this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    private void initDate() {
        int actualMaximum;
        int i;
        int i2;
        Calendar calendar = Calendar.getInstance();
        int i3 = calendar.get(1);
        int i4 = calendar.get(2);
        int i5 = calendar.get(5);
        if (i5 == 1) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(1), calendar.get(2) - 1, 1);
            i = gregorianCalendar.get(1);
            i2 = gregorianCalendar.get(2);
            actualMaximum = gregorianCalendar.getActualMaximum(5);
        } else {
            actualMaximum = i5 - 1;
            i = i3;
            i2 = i4;
        }
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar(i, i2, actualMaximum);
        GregorianCalendar gregorianCalendar3 = new GregorianCalendar(i3, i4, i5);
        Date time = gregorianCalendar2.getTime();
        Date time2 = gregorianCalendar3.getTime();
        time.getTime();
        time2.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT_DATE);
        String str = simpleDateFormat.format(time);
        String str2 = simpleDateFormat.format(time2);
        this.editDateBegin.setText(str);
        this.editDateEnd.setText(str2);
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        this.strName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
        this.strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        Log.d("info", "PlayBackTFActivity  strName:" + this.strName + " strDID:" + this.strDID + " strPwd:" + intent.getStringExtra(ContentCommon.STR_CAMERA_PWD) + " strUser:" + intent.getStringExtra(ContentCommon.STR_CAMERA_USER));
    }

    @Override // android.app.Activity
    protected void onPause() {
        overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        super.onPause();
    }

    private void setListener() {
        this.listView.setOnItemClickListener(this);
        this.btnBack.setOnClickListener(this);
        this.editDateBegin.setOnClickListener(this);
        this.editDateEnd.setOnClickListener(this);
        this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
    }

    private void findView() {
        this.btnBack = (Button) findViewById(R.id.back);
        this.listView = (ListView) findViewById(R.id.listview);
        this.tvNoVideo = (TextView) findViewById(R.id.no_video);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.editDateBegin = (EditText) findViewById(R.id.edit_date_begin);
        this.editDateEnd = (EditText) findViewById(R.id.edit_date_end);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.top);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        relativeLayout.setBackgroundDrawable(bitmapDrawable);
        this.loadMoreView = getLayoutInflater().inflate(R.layout.loadmorecount, (ViewGroup) null);
        this.loadMoreButton = (Button) this.loadMoreView.findViewById(R.id.btn_load);
        this.loadMoreView.setVisibility(8);
        System.out.println("!1111111111111111111111111111111");
        this.listView.addFooterView(this.loadMoreView);
        this.loadMoreButton.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PlayBackTFActivity.this.fileTFCount - PlayBackTFActivity.this.mAdapter.getCount() > 0) {
                    PlayBackTFActivity.this.LoadMoreData();
                } else {
                    PlayBackTFActivity.this.loadMoreView.setVisibility(8);
                }
            }
        });
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Log.d("playBackTFActivity...", "!!!!!!!!!!" + i);
        if (i < this.mAdapter.arrayList.size()) {
            PlayBackBean playBean = this.mAdapter.getPlayBean(i);
            String path = playBean.getPath();
            String strSubstring = path.substring(0, 14);
            Intent intent = new Intent(this, (Class<?>) PlayBackActivity.class);
            intent.putExtra(DatabaseUtil.KEY_DID, playBean.getDid());
            intent.putExtra(DatabaseUtil.KEY_FILEPATH, playBean.getPath());
            intent.putExtra("videotime", strSubstring);
            intent.putExtra("filesize", playBean.getVideofilesize());
            Log.i("info", "filepath:" + path + "---mess:" + strSubstring + "---");
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (this.fileTFCount - this.mAdapter.getCount() > 0) {
            this.handler.postDelayed(new Runnable() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.5
                @Override // java.lang.Runnable
                public void run() {
                    PlayBackTFActivity.this.LoadMoreData();
                }
            }, 2000L);
        } else {
            this.loadMoreView.setVisibility(8);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
            overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        } else if (id == R.id.edit_date_begin) {
            date(this.editDateBegin.getText().toString(), true);
        } else if (id == R.id.edit_date_end) {
            date(this.editDateEnd.getText().toString(), false);
        }
    }

    private void date(String str, final boolean z) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT_DATE);
        try {
            Date date = simpleDateFormat.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.pzj.ipcdemo.PlayBackTFActivity.6
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    Date time = new GregorianCalendar(i, i2, i3).getTime();
                    String str2 = simpleDateFormat.format(time);
                    if (z) {
                        int iCompareTo = str2.compareTo(PlayBackTFActivity.this.editDateEnd.getText().toString());
                        Log.d("tag", "\ue78a result:" + iCompareTo);
                        if (iCompareTo > 0) {
                            PlayBackTFActivity.this.showToast(R.string.remote_start_prompt);
                            return;
                        } else {
                            time.getTime();
                            PlayBackTFActivity.this.editDateBegin.setText(str2);
                            return;
                        }
                    }
                    if (str2.compareTo(PlayBackTFActivity.this.editDateBegin.getText().toString()) < 0) {
                        PlayBackTFActivity.this.showToast(R.string.remote_end_prompt);
                    } else {
                        time.getTime();
                        PlayBackTFActivity.this.editDateEnd.setText(str2);
                    }
                }
            }, calendar.get(1), calendar.get(2), calendar.get(5)).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    public void showToast(int i) {
        Toast.makeText(this, getResources().getString(i), 1).show();
    }

    public void LoadMoreData() {
        int count = this.mAdapter.getCount();
        if (count + 500 <= this.fileTFCount && this.getCurrentPageIndex + 1 <= this.TotalPageSize) {
            this.getCurrentPageIndex++;
            NativeCaller.PPPPGetSDCardRecordFileList(this.strDID, this.getCurrentPageIndex, 500);
            this.mAdapter.notifyDataSetChanged();
            this.loadMoreButton.setText("获取更多视频文件...");
            return;
        }
        int i = this.fileTFCount - count;
        String str = this.strDID;
        int i2 = this.getCurrentPageIndex;
        this.getCurrentPageIndex = i2 + 1;
        NativeCaller.PPPPGetSDCardRecordFileList(str, i2, i);
        this.mAdapter.notifyDataSetChanged();
        this.loadMoreButton.setText("已经加载完毕");
        this.loadMoreButton.setVisibility(8);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayBackTFInterface
    public void callBackRecordFileSearchResult(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6) {
        Log.d("info", "CallBack_RecordFileSearchResult did: " + str + " filename: " + str2 + " size: " + i + " recordcount :" + i2 + "pagecount: " + i3 + "pageindex:" + i4 + "pagesize: " + i5 + "bEnd:" + i6);
        if (this.strDID.equals(str)) {
            this.fileTFCount = i2;
            this.getCurrentPageIndex = i4;
            this.totalSize = i;
            this.TotalPageSize = i5;
            PlayBackBean playBackBean = new PlayBackBean();
            playBackBean.setDid(str);
            playBackBean.setPath(str2);
            playBackBean.setVideofilesize(i);
            this.mAdapter.addPlayBean(playBackBean);
            int i7 = this.TotalPageSize % 500;
            if (i6 == 1) {
                this.mHandler.sendEmptyMessage(1);
            }
        }
    }
}
