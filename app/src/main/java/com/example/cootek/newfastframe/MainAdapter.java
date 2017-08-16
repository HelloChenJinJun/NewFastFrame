package com.example.cootek.newfastframe;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.CommonLogger;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/9.
 */

public class MainAdapter extends BaseRecyclerAdapter<Music, BaseWrappedViewHolder> {


    @Inject
    public MainAdapter() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_main;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, Music data) {
        CommonLogger.e("数据啦啦啦"+data.toString());
        holder.setImageUrl(R.id.riv_item_activity_main_image, data.getPath())
                .setText(R.id.tv_item_activity_main_name, data.getSongTitle())
                .setText(R.id.tv_item_activity_main_description, data.getArtistName() + "," + data.getAlbumName())
                .setOnItemClickListener();
    }

    public long[] getIds() {
        int size = data.size();
        long[] result = new long[0];
        if (size > 0) {
            result = new long[size];
            for (int i = 0; i < size; i++) {
                result[i] = data.get(i).getSongId();
            }
        }
        return result;
    }
}
