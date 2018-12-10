package com.example.live.adapter;


import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.live.R;
import com.example.live.bean.SearchLiveBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      10:56
 * QQ:             1981367757
 */

public class SearchLiveAdapter extends BaseRecyclerAdapter<SearchLiveBean.DataBean.LiveInfo,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recommend_live_item;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SearchLiveBean.DataBean.LiveInfo data) {
        holder.setImageUrl(R.id.iv_item_fragment_recommend_live_item_image,data.getThumb()
                ,R.drawable.live_default,R.drawable.live_default)
                .setText(R.id.tv_item_fragment_recommend_live_item_description,data.getTitle())
                .setText(R.id.tv_item_fragment_recommend_live_item_name,data.getNick())
                .setText(R.id.tv_item_fragment_recommend_live_item_count,data.getView())
                .setOnItemClickListener();

    }
}
