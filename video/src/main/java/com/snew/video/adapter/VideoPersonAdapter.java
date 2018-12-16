package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.VideoPlayDetailBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/16     16:36
 */
public class VideoPersonAdapter extends BaseRecyclerAdapter<VideoPlayDetailBean.VideoPlayPerson, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_qq_video_detail_person;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, VideoPlayDetailBean.VideoPlayPerson data) {
        holder.setText(R.id.tv_item_activity_qq_video_detail_person_title, data.getName())
                .setImageUrl(R.id.riv_item_activity_qq_video_detail_person_avatar, data.getAvatar());
    }
}
