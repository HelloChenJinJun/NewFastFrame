package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.HappyContentBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:27
 * QQ:             1981367757
 */
public class HappyContentAdapter extends BaseRecyclerAdapter<HappyContentBean, BaseWrappedViewHolder> {





        @Override
        protected int getLayoutId() {
                return R.layout.fragment_happy_content_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, HappyContentBean data) {
                holder.setOnItemChildClickListener(R.id.iv_fragment_happy_content_item_share)
                .setText(R.id.tv_fragment_happy_content_item_content, data.getContent())
                .setText(R.id.tv_fragment_happy_content_item_time, data.getUpdatetime());
        }
}
