package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.MusicPlayBean;
import com.example.commonlibrary.utils.CommonLogger;
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
        CommonLogger.e("数据啦啦啦" + data.toString());
        holder.setImageUrl(R.id.riv_item_fragment_local_list_image, data.getAlbumUrl())
                .setText(R.id.tv_item_fragment_local_list_name, data.getSongName())
                .setText(R.id.tv_item_fragment_local_list_description, data.getArtistName() + "," + data.getAlbumName())
                .setOnItemClickListener().setOnItemLongClickListener();
    }
}
