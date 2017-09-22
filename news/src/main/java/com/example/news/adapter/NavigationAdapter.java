package com.example.news.adapter;

import android.widget.TextView;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/22      15:10
 * QQ:             1981367757
 */

public class NavigationAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_library_navigation;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, String data) {
        if (holder.getAdapterPosition() == 0) {
            TextView textView = ((TextView) holder.getView(R.id.tv_item_fragment_library_navigation_display));
            textView.setTextColor(holder.getContext().getResources().getColor(R.color.base_color_text_blue));
            textView.setBackground(holder.getContext().getResources().getDrawable(R.drawable.tab_btn_bg_selected));
        }
        holder.setText(R.id.tv_item_fragment_library_navigation_display, data.contains("/") ? data.split("/")[0] : data)
                .setOnItemChildClickListener(R.id.tv_item_fragment_library_navigation_display);
    }
}
