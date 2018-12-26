package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.ArtistInfo;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     11:33
 */
public class SingerListAdapter extends BaseRecyclerAdapter<ArtistInfo, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_singer_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ArtistInfo data) {
        holder.setImageUrl(R.id.iv_item_fragment_singer_list_image, data.getAvatar_big())
                .setText(R.id.tv_item_fragment_singer_list_name, data.getName())
                .setText(R.id.tv_item_fragment_singer_list_song_num, data.getSongs_total())
                .setText(R.id.tv_item_fragment_singer_list_album_num, data.getAlbums_total())
                .setOnItemClickListener();

    }
}
