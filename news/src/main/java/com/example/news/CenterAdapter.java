package com.example.news;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.skin.SkinManager;
import com.example.news.bean.CenterBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/22      9:55
 * QQ:             1981367757
 */

public class CenterAdapter extends BaseRecyclerAdapter<CenterBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_center;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, CenterBean data) {
        holder.setText(R.id.tv_item_fragment_center_title, data.getTitle())
                .setImageDrawable(R.id.item_fragment_center_icon, SkinManager
                .getInstance().getDrawable(data.getResId()))
                .setOnItemClickListener();
    }


    @Override
    protected boolean isApplySkin() {
        return true;
    }
}
