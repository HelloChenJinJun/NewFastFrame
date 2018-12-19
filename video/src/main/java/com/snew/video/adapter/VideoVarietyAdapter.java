package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.VarietyVideoDetailBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/18     17:01
 */
public class VideoVarietyAdapter extends BaseRecyclerAdapter<VarietyVideoDetailBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_qq_video_detail_variety;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, VarietyVideoDetailBean data) {
        holder.setImageUrl(R.id.iv_item_activity_qq_video_detail_variety_image, data.getImage())
                .setText(R.id.tv_item_activity_qq_video_detail_variety_time, data.getTime())
                .setText(R.id.tv_item_activity_qq_video_detail_variety_title, data.getTitle())
                .setText(R.id.tv_item_activity_qq_video_detail_variety_hot, data.getHot() + "")
                .setOnItemClickListener();
    }
}
