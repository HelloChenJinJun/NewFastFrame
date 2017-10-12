package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.HappyBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      18:49
 * QQ:             1981367757
 */
public class HappyAdapter extends BaseRecyclerAdapter<HappyBean, BaseWrappedViewHolder> {




        @Override
        protected int getLayoutId() {
                return R.layout.fragment_happy_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, HappyBean data) {
                holder.setText(R.id.tv_fragment_happy_item_content, data.getContent())
                        .setText(R.id.tv_fragment_happy_item_time, data.getUpdatetime())
                        .setOnItemChildClickListener(R.id.iv_fragment_happy_item_picture)
                        .setOnItemChildClickListener(R.id.iv_fragment_happy_item_share)
                        .setImageUrl(R.id.iv_fragment_happy_item_picture, data.getUrl());
        }

}
