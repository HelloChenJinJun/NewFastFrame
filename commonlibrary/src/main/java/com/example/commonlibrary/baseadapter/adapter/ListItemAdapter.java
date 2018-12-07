package com.example.commonlibrary.baseadapter.adapter;

import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     16:53
 */
public class ListItemAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, String data) {
        holder.setText(R.id.tv_item_list_content, data)
                .setOnItemClickListener();
    }
}
