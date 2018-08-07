package com.example.cootek.newfastframe.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.cootek.newfastframe.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      20:47
 * QQ:             1981367757
 */

public class LocalMusicAdapter extends BaseMultipleRecyclerAdapter<MusicPlayBean,BaseWrappedViewHolder> {


    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_local_music;
    }

    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray layoutMap=new SparseIntArray();
        layoutMap.put(MusicPlayBean.NORMAL,R.layout.item_activity_local_music);
        layoutMap.put(MusicPlayBean.DETAIL,R.layout.item_activity_local_music_detail);
        layoutMap.put(MusicPlayBean.ACTION,R.layout.item_activity_local_music_action);
        return layoutMap;
    }

    @Override
    protected void convert(final BaseWrappedViewHolder holder, MusicPlayBean data) {
        if (holder.getItemViewType() == MusicPlayBean.NORMAL) {
            holder.setText(R.id.tv_item_activity_local_music_song_name,data.getSongName())
                    .setText(R.id.tv_item_activity_local_music_artist_name,data.getArtistName())
                    .setOnItemChildClickListener(R.id.iv_item_activity_local_music_add)
                    .setOnItemChildClickListener(R.id.rl_item_activity_local_music_normal)
                    .setOnItemChildClickListener(R.id.iv_item_activity_local_music_more);
        }else if (holder.getItemViewType()==MusicPlayBean.DETAIL){
            holder.setImageUrl(R.id.iv_item_activity_local_music_detail_avatar,data.getAlbumUrl(),R.drawable.ic_people
            ,R.drawable.ic_people)
                    .setText(R.id.tv_item_activity_local_music_detail_name,data.getArtistName())
                    .setOnItemChildClickListener(R.id.iv_item_activity_local_music_detail_more);
        }else {
//
        }

    }

    public int getPositionFromId(long id) {
        int size=data.size();
        for (int i = 0; i < size; i++) {
            MusicPlayBean item=data.get(i);
            if (item.getSongId() == id && item.getType() == MusicPlayBean.NORMAL) {
                return i;
            }
        }
        return -1;
    }
}
