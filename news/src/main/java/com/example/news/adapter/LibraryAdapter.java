package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.SearchLibraryBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      18:08
 * QQ:             1981367757
 */

public class LibraryAdapter extends BaseRecyclerAdapter<SearchLibraryBean,BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_library;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SearchLibraryBean data) {
            holder.setText(R.id.tv_item_fragment_library_book_name,data.getBookName())
                    .setText(R.id.tv_item_fragment_library_author,data.getAuthor())
                    .setText(R.id.tv_item_fragment_library_total,data.getTotalNum())
                    .setText(R.id.tv_item_fragment_library_enable,data.getEnableNum())
                    .setText(R.id.tv_item_fragment_library_from,data.getFrom())
                    .setOnItemClickListener();
    }
}
