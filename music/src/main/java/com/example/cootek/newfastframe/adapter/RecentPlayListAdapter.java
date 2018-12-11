package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.util.MusicUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/11     13:30
 */
public class RecentPlayListAdapter extends BaseRecyclerAdapter<MusicPlayBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recent_play_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayBean data) {
        holder.setText(R.id.tv_item_fragment_recent_play_list_name, data.getArtistName())
                .setText(R.id.tv_item_fragment_recent_play_list_song, data.getSongName())
                .setText(R.id.tv_item_fragment_recent_play_list_duration, MusicUtil.makeLrcTime(data.getDuration()))
                .setOnItemClickListener();
    }
}
