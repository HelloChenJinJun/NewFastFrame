package com.example.chat.adapter;

import com.example.chat.R;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/2     23:52
 * QQ:         1981367757
 */

public class CommentListAdapter extends BaseRecyclerAdapter<Object,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, Object data) {

    }
}
