package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.TVPlayBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/16     16:39
 */
public class VideoTVItemAdapter extends BaseRecyclerAdapter<TVPlayBean, BaseWrappedViewHolder> {


    private int selectedPosition = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_qq_video_detail_tv;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, TVPlayBean data) {
        holder.setText(R.id.tv_item_activity_qq_video_detail_tv_title, data.getTitle())
                .setOnItemClickListener(v -> {
                    if (selectedPosition != holder.getAdapterPosition() - getItemUpCount()) {
                        change(((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(holder.getAdapterPosition() - getItemUpCount())), true);
                        change(((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(selectedPosition)), false);
                        selectedPosition = holder.getAdapterPosition() - getItemUpCount();
                        if (getOnItemClickListener() != null) {
                            getOnItemClickListener().onItemClick(selectedPosition, v);
                        }
                    }

                });
        if (selectedPosition == holder.getAdapterPosition()) {
            change(holder, true);
        } else {
            change(holder, false);
        }
    }


    private void change(BaseWrappedViewHolder holder, boolean isSelected) {
        if (holder != null) {
            if (isSelected) {
                holder.getView(R.id.tv_item_activity_qq_video_detail_tv_title).setSelected(true);
            } else {
                holder.getView(R.id.tv_item_activity_qq_video_detail_tv_title).setSelected(false);
            }
        }
    }
}
