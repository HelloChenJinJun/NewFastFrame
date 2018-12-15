package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.SearchVideoBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     22:55
 */
public class SearchVideoDetailAdapter extends BaseRecyclerAdapter<SearchVideoBean.ItemBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_search_video_detail;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SearchVideoBean.ItemBean data) {
        holder.setText(R.id.tv_item_fragment_search_video_detail_type, data.getClassX())
                .setText(R.id.tv_item_fragment_search_video_detail_title, data.getTt())
                .setImageUrl(R.id.iv_item_fragment_search_video_detail_image, data.getDc())
                .setText(R.id.tv_item_fragment_search_video_detail_num, null)
                .setText(R.id.tv_item_fragment_search_video_detail_desc, data.getPa())
                .setOnItemChildClickListener(R.id.tv_item_fragment_search_video_detail_play);

    }
}
