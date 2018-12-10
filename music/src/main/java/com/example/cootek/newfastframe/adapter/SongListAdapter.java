package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.cootek.newfastframe.R;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListAdapter extends BaseRecyclerAdapter<MusicPlayBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_song_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayBean data) {
            holder.setText(R.id.tv_item_fragment_song_list_name,data.getSongName()).setOnItemClickListener();

    }
}
