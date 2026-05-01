package com.pzj.ipcdemo.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.adapter.SearchListAdapter;
import com.pzj.ipcdemo.entity.VStarCamera;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SearchAnyLayerView implements BridgeService.AddCameraInterface {
    public static final String TAG = "SearchAnyLayerView";
    private Context context;
    private RelativeLayout layout_dialog;
    private OnSelectSearchListener onSelectSearchListener;
    private ProgressBar pb_search;
    private RecyclerView rv_searchList;
    private SearchListAdapter searchListAdapter;
    private AnyLayer anyLayer = null;
    private List<VStarCamera> searchList = new ArrayList();
    int count = 0;
    Handler searchHandler = new Handler() { // from class: com.pzj.ipcdemo.view.SearchAnyLayerView.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            VStarCamera vStarCamera = (VStarCamera) message.getData().getSerializable("search");
            Iterator it = SearchAnyLayerView.this.searchList.iterator();
            while (it.hasNext()) {
                if (((VStarCamera) it.next()).getId().equals(vStarCamera.getId())) {
                    SearchAnyLayerView.this.count++;
                    if (SearchAnyLayerView.this.count == 2) {
                        NativeCaller.StopSearch();
                        SearchAnyLayerView.this.pb_search.setVisibility(4);
                        return;
                    }
                    return;
                }
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(SearchAnyLayerView.this.searchList);
            arrayList.add(vStarCamera);
            SearchAnyLayerView.this.searchList.clear();
            SearchAnyLayerView.this.searchList.addAll(arrayList);
            SearchAnyLayerView.this.searchListAdapter.notifyDataSetChanged();
        }
    };

    public interface OnSelectSearchListener {
        void onSelectSearchListener(VStarCamera vStarCamera);
    }

    public SearchAnyLayerView(Context context) {
        this.context = context;
    }

    public void initView(AnyLayer anyLayer) {
        this.rv_searchList = (RecyclerView) anyLayer.getView(R.id.rv_searchList);
        this.pb_search = (ProgressBar) anyLayer.getView(R.id.pb_search);
        this.layout_dialog = (RelativeLayout) anyLayer.getView(R.id.layout_dialog);
        this.rv_searchList.setLayoutManager(new GridLayoutManager(this.context, 1));
        this.rv_searchList.setHasFixedSize(true);
    }

    public void dismiss() {
        try {
            this.anyLayer.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if (this.anyLayer != null) {
            if (this.anyLayer.isShow()) {
                return;
            }
            this.anyLayer.show();
            return;
        }
        this.anyLayer = AnyLayer.with(this.context).contentView(R.layout.dialog_search_camera_list).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.view.SearchAnyLayerView.1
            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long inAnim(View view) {
                AnimHelper.startTopAlphaInAnim(view, 350L);
                return 350L;
            }

            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long outAnim(View view) {
                AnimHelper.startTopAlphaOutAnim(view, 350L);
                return 350L;
            }
        });
        initView(this.anyLayer);
        BridgeService.setAddCameraInterface(this);
        startSearch();
        this.searchListAdapter = new SearchListAdapter(this.context, this.searchList);
        this.rv_searchList.setAdapter(this.searchListAdapter);
        this.searchListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() { // from class: com.pzj.ipcdemo.view.SearchAnyLayerView.2
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchAnyLayerView.this.onSelectSearchListener.onSelectSearchListener((VStarCamera) SearchAnyLayerView.this.searchList.get(i));
            }
        });
        this.layout_dialog.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.view.SearchAnyLayerView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    if (SearchAnyLayerView.this.anyLayer.isShow()) {
                        SearchAnyLayerView.this.anyLayer.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.anyLayer.show();
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.AddCameraInterface
    public void callBackSearchResultData(int i, String str, String str2, String str3, String str4, int i2) {
        VStarCamera vStarCamera = new VStarCamera(str2, str3, ContentCommon.DEFAULT_USER_NAME, "");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putSerializable("search", vStarCamera);
        message.setData(bundle);
        this.searchHandler.sendMessage(message);
        Log.d(TAG, vStarCamera.toString());
    }

    public void startSearch() {
        this.searchList.clear();
        new Thread(new SearchThread()).start();
    }

    private class SearchThread implements Runnable {
        private SearchThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d(SearchAnyLayerView.TAG, "startSearch");
            NativeCaller.StartSearch();
        }
    }

    public void setOnSelectSearchListener(OnSelectSearchListener onSelectSearchListener) {
        this.onSelectSearchListener = onSelectSearchListener;
    }
}
