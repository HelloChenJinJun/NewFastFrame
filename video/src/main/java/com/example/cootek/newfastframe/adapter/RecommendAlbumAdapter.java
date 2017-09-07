package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.RecommendSongBean;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class RecommendAlbumAdapter extends BaseRecyclerAdapter<RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.Mix1Bean
        .ResultBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recommend_album;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, RecommendSongBean.ResultBeanXXXXXXXXXXXXXXXX.Mix1Bean.ResultBean data) {
        holder.setText(R.id.tv_item_fragment_recommend_album_description, data.getDesc())
                .setImageUrl(R.id.iv_item_fragment_recommend_album_image, data.getPic())
                .setOnItemChildClickListener(R.id.iv_item_fragment_recommend_album_image);
    }
}
