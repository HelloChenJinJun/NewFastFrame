package com.example.chat.adapter;

import com.example.chat.R;
import com.example.chat.bean.SkinBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:52
 */

public class SkinListAdapter extends BaseRecyclerAdapter<SkinBean,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_skin_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SkinBean data) {
            //todo  SkinList
    }
}
