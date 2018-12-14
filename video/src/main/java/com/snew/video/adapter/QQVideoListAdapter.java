package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.QQVideoListBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:08
 */
public class QQVideoListAdapter extends BaseRecyclerAdapter<QQVideoListBean.JsonvalueBean.ResultsBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_qq_video_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, QQVideoListBean.JsonvalueBean.ResultsBean data) {
        holder.setImageUrl(R.id.iv_item_fragment_qq_video_list_display, data.getFields()
                .getVertical_pic_url())
                .setText(R.id.tv_item_fragment_qq_video_list_title, data.getFields().getTitle())
                .setText(R.id.tv_item_fragment_qq_video_list_desc, data.getFields()
                        .getSecond_title())
                .setText(R.id.tv_item_fragment_qq_video_list_score, data.getFields().getOpinion_score() + "")
                .setOnItemClickListener();
    }
}
