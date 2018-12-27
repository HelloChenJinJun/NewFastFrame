package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.AlbumWrappedBean;
import com.example.cootek.newfastframe.util.MusicUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     11:24
 */
public class AlbumListAdapter extends BaseRecyclerAdapter<AlbumWrappedBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_album_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, AlbumWrappedBean data) {
        holder.setImageUrl(R.id.iv_item_fragment_album_list_image, MusicUtil.getRealUrl(data.getImageUrl(), DensityUtil.toDp(150)))
                .setText(R.id.tv_item_fragment_album_list_sub_title, data.getSubTitle())
                .setText(R.id.tv_item_fragment_album_list_title, data.getTitle())
                .setOnItemClickListener();

    }
}
