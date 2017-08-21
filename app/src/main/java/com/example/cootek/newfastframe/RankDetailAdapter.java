package com.example.cootek.newfastframe;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.cootek.newfastframe.api.DownLoadMusicBean;
import com.example.cootek.newfastframe.api.RankListBean;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankDetailAdapter extends BaseRecyclerAdapter<DownLoadMusicBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_rank_detail;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, DownLoadMusicBean data) {
        holder.setText(R.id.tv_item_fragment_rank_detail_song_name, data.getSonginfo().getTitle())
                .setOnItemClickListener();

    }
}
