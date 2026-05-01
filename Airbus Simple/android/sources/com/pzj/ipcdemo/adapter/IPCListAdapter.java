package com.pzj.ipcdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.os.EnvironmentCompat;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.entity.VStarCamera;
import com.pzj.ipcdemo.utils.ImageDispose;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class IPCListAdapter extends BaseItemDraggableAdapter {
    public static final int PPPP_STATUS_CONNECTING = 0;
    public static final int PPPP_STATUS_CONNECT_FAILED = 3;
    public static final int PPPP_STATUS_CONNECT_TIMEOUT = 7;
    public static final int PPPP_STATUS_DEVICE_NOT_ON_LINE = 6;
    public static final int PPPP_STATUS_DISCONNECT = 4;
    public static final int PPPP_STATUS_INITIALING = 1;
    public static final int PPPP_STATUS_INVALID_ID = 5;
    public static final int PPPP_STATUS_ON_LINE = 2;
    public static final int PPPP_STATUS_WRONGPWD_RIGHTUSER = 9;
    public static final int PPPP_STATUS_WRONGPWD_WRONGUSER = 10;
    public static final int PPPP_STATUS_WRONGUSER_RIGHTPWD = 8;
    private Context context;
    private List<Integer> ipcStates;

    public IPCListAdapter(Context context, List<VStarCamera> list) {
        super(R.layout.view_ipc_item, list);
        this.context = context;
        this.ipcStates = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            this.ipcStates.add(-1);
        }
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    protected void convert(BaseViewHolder baseViewHolder, Object obj) {
        VStarCamera vStarCamera = (VStarCamera) obj;
        baseViewHolder.setText(R.id.tv_ipcName, vStarCamera.getName());
        switch (this.ipcStates.get(baseViewHolder.getAdapterPosition()).intValue()) {
            case 0:
                baseViewHolder.setText(R.id.tv_status, "connecting");
                break;
            case 1:
                baseViewHolder.setText(R.id.tv_status, "initializing");
                break;
            case 2:
                baseViewHolder.setText(R.id.tv_status, "On-line");
                break;
            case 3:
                baseViewHolder.setText(R.id.tv_status, "connection failed");
                break;
            case 4:
                baseViewHolder.setText(R.id.tv_status, "connection closed");
                break;
            case 5:
                baseViewHolder.setText(R.id.tv_status, "invalid uid");
                break;
            case 6:
                baseViewHolder.setText(R.id.tv_status, "not online");
                break;
            case 7:
                baseViewHolder.setText(R.id.tv_status, "connection timed out");
                break;
            case 8:
            case 9:
            case 10:
                baseViewHolder.setText(R.id.tv_status, "password error");
                break;
            default:
                baseViewHolder.setText(R.id.tv_status, EnvironmentCompat.MEDIA_UNKNOWN);
                break;
        }
        if (vStarCamera.getbImage() != null) {
            try {
                Glide.with(this.context).load(ImageDispose.getPicFromBytes(vStarCamera.getbImage(), new BitmapFactory.Options())).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into((ImageView) baseViewHolder.getView(R.id.iv_ipc));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        baseViewHolder.addOnClickListener(R.id.iv_ipcSetting);
        baseViewHolder.addOnClickListener(R.id.iv_ipcUpdate);
        baseViewHolder.addOnClickListener(R.id.iv_ipcDelete);
        baseViewHolder.addOnClickListener(R.id.iv_item);
        baseViewHolder.addOnLongClickListener(R.id.iv_ipcSetting);
        baseViewHolder.addOnLongClickListener(R.id.iv_ipcUpdate);
        baseViewHolder.addOnLongClickListener(R.id.iv_ipcDelete);
        baseViewHolder.addOnLongClickListener(R.id.iv_item);
    }

    public List<Integer> getIpcStates() {
        return this.ipcStates;
    }

    public void setIpcStates(List<Integer> list) {
        this.ipcStates = list;
        notifyDataSetChanged();
    }

    public void setIpcStatesIndex(int i, int i2) {
        this.ipcStates.remove(i2);
        this.ipcStates.add(i2, Integer.valueOf(i));
        notifyDataSetChanged();
    }
}
