package com.example.news;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:54
 * QQ:             1981367757
 */

public class NewsListAdapter extends BaseRecyclerAdapter<Object,BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_news_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, Object data) {

    }
}
