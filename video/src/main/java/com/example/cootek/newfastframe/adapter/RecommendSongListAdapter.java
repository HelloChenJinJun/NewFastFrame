package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.RecommendSongBean;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class RecommendSongListAdapter extends BaseRecyclerAdapter<RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX
        .DiyBean.ResultBeanXXXXXXXXX, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recommend_song_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.DiyBean.ResultBeanXXXXXXXXX data) {
        holder.setText(R.id.tv_item_fragment_recommend_song_list_description, data.getTitle())
                .setText(R.id.tv_item_fragment_recommend_song_list_count, data.getListenum() + "")
                .setImageUrl(R.id.iv_item_fragment_recommend_song_list_image, data.getPic())
                .setOnItemChildClickListener(R.id.iv_item_fragment_recommend_song_list_image);
    }
}
