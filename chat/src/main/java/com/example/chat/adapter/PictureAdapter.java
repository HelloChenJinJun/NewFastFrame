package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.PictureBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:03
 * QQ:             1981367757
 */
public class PictureAdapter extends BaseRecyclerAdapter<PictureBean, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return R.layout.fragment_picture_item_layout;
        }


        @Override
        protected void convert(BaseWrappedViewHolder holder, PictureBean data) {
                holder.setText(R.id.tv_fragment_picture_item_description, data.getDesc())
                        .setOnItemChildClickListener(R.id.iv_fragment_picture_item_picture)
                        .setOnItemChildClickListener(R.id.iv_fragment_picture_item_share)
                        .setImageUrl(R.id.iv_fragment_picture_item_picture, data.getUrl());
        }
}
