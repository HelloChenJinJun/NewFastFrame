package com.example.cootek.newfastframe;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/22.
 */

class PopupWindowAdapter extends BaseRecyclerAdapter<MusicPlayBean, BaseWrappedViewHolder> {


    @Override
    protected int getLayoutId() {
        return R.layout.item_view_pop_window_fragment_bottom;
    }


    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayBean data) {
        holder.setImageUrl(R.id.riv_item_view_pop_window_fragment_bottom_avatar, data.getAlbumUrl())
                .setText(R.id.tv_item_view_pop_window_fragment_bottom_artist_song_name, data.getSongName())
                .setText(R.id.tv_item_view_pop_window_fragment_bottom_artist_name, data.getArtistName())
                .setOnItemClickListener()
                .setOnItemChildClickListener(R.id.iv_item_view_pop_window_fragment_bottom_artist_delete);
    }
}
