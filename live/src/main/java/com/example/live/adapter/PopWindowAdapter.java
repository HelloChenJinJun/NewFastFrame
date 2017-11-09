package com.example.live.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.live.R;
import com.example.live.bean.CategoryLiveBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      23:06
 * QQ:             1981367757
 */

public class PopWindowAdapter extends BaseRecyclerAdapter<CategoryLiveBean,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_view_activity_main_pop_window;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, CategoryLiveBean data) {
        holder.setImageUrl(R.id.riv_item_view_activity_main_pop_window_icon,data.getIcon_image())
                .setText(R.id.tv_item_view_activity_main_pop_window_title,data.getName())
                .setOnItemClickListener();

    }
}
