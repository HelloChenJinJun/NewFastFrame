package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.CityInfoBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/29     10:56
 * QQ:         1981367757
 */

public class AddressListAdapter extends BaseRecyclerAdapter<CityInfoBean,BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_address_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, CityInfoBean data) {
        holder.setText(R.id.tv_item_activity_address_list_address,data.getName())
        .setOnItemClickListener();
    }
}
