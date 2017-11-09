package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.R;
import com.example.commonlibrary.bean.music.SingerListBean;

/**
 * Created by COOTEK on 2017/9/2.
 */

public class SingerListAdapter extends BaseRecyclerAdapter<SingerListBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_singer_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SingerListBean data) {
        holder.setText(R.id.tv_item_fragment_singer_list_name, data.getName())
                .setText(R.id.tv_item_fragment_singer_list_count, data.getCount() + "首")
                .setImageUrl(R.id.riv_item_fragment_singer_list_image, data.getAvatar(), R.drawable.icon_album_default, R.drawable.icon_album_default)
                .setOnItemClickListener();

    }
}
