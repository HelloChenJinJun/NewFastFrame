package com.snew.video.adapter;

import android.graphics.Color;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.HotVideoItemBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     19:37
 */
public class HotVideoAdapter extends BaseRecyclerAdapter<HotVideoItemBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_hot_video_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, HotVideoItemBean data) {
        holder.setText(R.id.tv_item_fragment_hot_video_list_title, data.getTitle())
                .setText(R.id.tv_item_Fragment_hot_video_list_order, (holder.getAdapterPosition() + 1) + "")
                .setOnItemClickListener();
        if (holder.getAdapterPosition() > 2) {
            holder.setTextColor(R.id.tv_item_Fragment_hot_video_list_order, Color.GRAY);
            holder.setBgColor(R.id.tv_item_Fragment_hot_video_list_order, holder.getContext().getResources().getColor(R.color.grey_200));
        } else {
            holder.setTextColor(R.id.tv_item_Fragment_hot_video_list_order, Color.WHITE);
            switch (holder.getAdapterPosition()) {
                case 0:
                    holder.setBgColor(R.id.tv_item_Fragment_hot_video_list_order, holder.getContext().getResources().getColor(R.color.red_500));
                    break;
                case 1:
                    holder.setBgColor(R.id.tv_item_Fragment_hot_video_list_order, holder.getContext().getResources().getColor(R.color.orange_300));
                    break;
                case 2:
                    holder.setBgColor(R.id.tv_item_Fragment_hot_video_list_order, holder.getContext().getResources().getColor(R.color.yellow_300));
                    break;
            }
        }
    }
}
