package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/16     16:34
 */
public class VideoTagAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_qq_video_detail;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, String data) {
        holder.setText(R.id.tv_item_activity_qq_video_detail_tag, data);

    }
}
