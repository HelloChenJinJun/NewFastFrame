package com.example.cootek.newfastframe;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;

/**
 * Created by COOTEK on 2017/8/9.
 */

public class MainAdapter extends BaseRecyclerAdapter<MusicPlayInfo, BaseWrappedViewHolder> {

    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_main;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MusicPlayInfo data) {
//        holder.setImageUrl(R.id.riv_item_activity_main_image,)
    }
}
