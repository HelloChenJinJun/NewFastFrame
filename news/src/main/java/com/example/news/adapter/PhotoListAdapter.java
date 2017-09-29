package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.PictureBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      17:50
 * QQ:             1981367757
 */

public class PhotoListAdapter extends BaseRecyclerAdapter<PictureBean.PictureEntity,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_photo_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, PictureBean.PictureEntity data) {
        holder.setText(R.id.tv_item_fragment_photo_list_description, data.getDesc())
                .setOnItemChildClickListener(R.id.iv_item_fragment_photo_list_share)
                .setOnItemClickListener()
                .setImageUrl(R.id.iv_item_fragment_photo_list_picture, data.getUrl());
    }
}
