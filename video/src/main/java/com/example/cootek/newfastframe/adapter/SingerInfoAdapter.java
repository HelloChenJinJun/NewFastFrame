package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.MusicPlayBean;
import com.example.commonlibrary.bean.SingerListBean;
import com.example.cootek.newfastframe.R;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SingerInfoAdapter extends BaseRecyclerAdapter<MusicPlayBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_singer_info;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayBean data) {
        holder.setImageUrl(R.id.riv_item_activity_singer_info_image, data.getAlbumUrl())
                .setText(R.id.tv_item_activity_singer_info_name, data.getSongName())
                .setText(R.id.tv_item_activity_singer_info_description, data.getAlbumName())
                .setOnItemClickListener();

    }
}
