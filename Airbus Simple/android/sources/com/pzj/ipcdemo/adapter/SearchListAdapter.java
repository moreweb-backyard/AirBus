package com.pzj.ipcdemo.adapter;

import android.content.Context;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.entity.VStarCamera;
import java.util.List;

/* loaded from: classes.dex */
public class SearchListAdapter extends BaseItemDraggableAdapter {
    private Context context;

    public SearchListAdapter(Context context, List<VStarCamera> list) {
        super(R.layout.view_search_camera_item, list);
        this.context = context;
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    protected void convert(BaseViewHolder baseViewHolder, Object obj) {
        VStarCamera vStarCamera = (VStarCamera) obj;
        String name = vStarCamera.getName();
        String id = vStarCamera.getId();
        baseViewHolder.setText(R.id.tv_searchName, name);
        baseViewHolder.setText(R.id.tv_searchId, id);
        baseViewHolder.addOnClickListener(R.id.layout_ipc);
    }
}
