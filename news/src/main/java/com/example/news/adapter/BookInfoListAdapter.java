package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.BookInfoBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      11:04
 * QQ:             1981367757
 */

public class BookInfoListAdapter extends BaseRecyclerAdapter<BookInfoBean,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_book_info_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, BookInfoBean data) {
        holder.setText(R.id.tv_item_Fragment_book_info_list_name,"书名:"+data.getBookName())
                .setText(R.id.tv_item_Fragment_book_info_list_start,"借阅日期:"+data.getStartTime())
                .setText(R.id.tv_item_Fragment_book_info_list_end,"归还日期:"+data.getEndTime())
                .setText(R.id.tv_item_Fragment_book_info_list_num,"续借量:"+data.getEnableNum())
                .setOnItemLongClickListener().setOnItemClickListener();

    }
}
