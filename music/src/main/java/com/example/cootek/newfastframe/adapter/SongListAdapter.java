package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListAdapter extends BaseRecyclerAdapter<DownLoadMusicBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_song_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, DownLoadMusicBean data) {
        holder.setText(R.id.tv_item_fragment_song_list_name, data.getSonginfo().getTitle())
                .setOnItemClickListener();

    }
}
