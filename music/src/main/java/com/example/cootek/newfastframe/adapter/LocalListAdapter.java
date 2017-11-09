package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.cootek.newfastframe.R;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/9.
 */

public class LocalListAdapter extends BaseRecyclerAdapter<MusicPlayBean, BaseWrappedViewHolder> {


    @Inject
    public LocalListAdapter() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_local_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayBean data) {
        holder.setImageUrl(R.id.riv_item_fragment_local_list_image, data.getAlbumUrl(), R.drawable.icon_album_default, R.drawable.icon_album_default)
                .setText(R.id.tv_item_fragment_local_list_name, data.getSongName())
                .setText(R.id.tv_item_fragment_local_list_description, data.getArtistName() + "," + data.getAlbumName())
                .setOnItemClickListener().setOnItemLongClickListener();
    }
}
