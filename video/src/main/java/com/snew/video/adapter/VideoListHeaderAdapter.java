package com.snew.video.adapter;

import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.QQVideoTabListBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     10:52
 */
public class VideoListHeaderAdapter extends BaseRecyclerAdapter<QQVideoTabListBean.IndexBean.OptionBean, BaseWrappedViewHolder> {

    private int currentPosition = 0;

    public QQVideoTabListBean.IndexBean.OptionBean getCurrentData() {
        return getData(currentPosition);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_view_fragment_video_list_header;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, QQVideoTabListBean.IndexBean.OptionBean data) {
        holder.setText(R.id.tv_view_item_fragment_video_list_header_title, data.getDisplay_name()).setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != holder.getAdapterPosition() - getItemUpCount()) {
                    change(((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(holder.getAdapterPosition())), true);
                    change(((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(currentPosition)), false);
                    currentPosition = holder.getAdapterPosition() - getItemUpCount();
                }
                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(currentPosition, v);
                }
            }
        });
        if (holder.getAdapterPosition() == currentPosition) {
            change(holder, true);
        } else {
            change(holder, false);
        }
    }


    private void change(BaseWrappedViewHolder holder, boolean isSelected) {
        if (holder != null) {
            if (isSelected) {
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
            }
        }
    }
}
