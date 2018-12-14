package com.snew.video.adapter;

import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.snew.video.R;
import com.snew.video.bean.QQVideoTabListBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     11:06
 */
public class VideoHeaderAdapter extends BaseRecyclerAdapter<QQVideoTabListBean.IndexBean, VideoHeaderViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_view_fragment_video_header;
    }

    public interface OnInnerItemClickListener {
        public void onItemClick(int position, int innerPosition, QQVideoTabListBean.IndexBean indexBean);
    }


    private OnInnerItemClickListener mOnInnerItemClickListener;


    public void setOnInnerItemClickListener(OnInnerItemClickListener onInnerItemClickListener) {
        mOnInnerItemClickListener = onInnerItemClickListener;
    }

    @Override
    protected void convert(VideoHeaderViewHolder holder, QQVideoTabListBean.IndexBean data) {
        holder.setText(R.id.tv_item_video_fragment_video_header_title, data.getDisplay_name());
        holder.bindData(data).setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (mOnInnerItemClickListener != null) {
                    mOnInnerItemClickListener.onItemClick(holder.getAdapterPosition(), position, data);
                }
            }
        });
    }
}
