package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.ActorDetailInfoBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     16:20
 */
public class ActorVideoDetailAdapter extends BaseRecyclerAdapter<ActorDetailInfoBean.ActorVideoDetailBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_qq_video_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ActorDetailInfoBean.ActorVideoDetailBean data) {
        holder.setVisible(R.id.tv_item_fragment_qq_video_list_desc, false)
                .setVisible(R.id.tv_item_fragment_qq_video_list_score, false)
                .setText(R.id.tv_item_fragment_qq_video_list_title, data.getTitle())
                .setImageUrl(R.id.iv_item_fragment_qq_video_list_display, data.getImage())
                .setOnItemClickListener();

    }
}
