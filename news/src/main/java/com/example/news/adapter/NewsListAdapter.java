package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.NewListBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:54
 * QQ:             1981367757
 */

public class NewsListAdapter extends BaseRecyclerAdapter<NewListBean.NewsItem,BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_news_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, NewListBean.NewsItem data) {
        holder.setImageUrl(R.id.iv_item_fragment_news_list_image,data.getThumb(),R.drawable.cug_index
        ,R.drawable.cug_index)
                .setText(R.id.tv_item_fragment_news_list_title,data.getTitle())
                .setText(R.id.tv_item_fragment_news_list_time_from,data.getFrom())
                .setText(R.id.tv_item_fragment_news_list_time,data.getTime())
                .setOnItemClickListener();
    }
}
