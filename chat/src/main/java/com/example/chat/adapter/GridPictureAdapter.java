package com.example.chat.adapter;

import android.util.SparseArray;

import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:37
 * QQ:             1981367757
 */
public class GridPictureAdapter extends BaseMultipleRecyclerAdapter<ImageItem, BaseWrappedViewHolder> {
    public GridPictureAdapter(List<ImageItem> data, int layoutId) {
        super(data, layoutId);
    }

    public GridPictureAdapter() {
        this(null, 0);
    }

    @Override
    protected SparseArray<Integer> getLayoutIdMap() {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        sparseArray.put(ImageItem.ITEM_CAMERA, R.layout.grid_picture_item);
        sparseArray.put(ImageItem.ITEM_NORMAL, R.layout.grid_picture_item);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ImageItem data) {
        if (holder.getItemViewType() == ImageItem.ITEM_CAMERA) {
            holder.setImageResource(R.id.iv_grid_picture_item_display, R.drawable.selector_image_add);
        } else {
            holder.setImageUrl(R.id.iv_grid_picture_item_display, data.getPath());
        }
        holder.setOnItemChildClickListener(R.id.iv_grid_picture_item_display);
    }
}
