package com.example.live.adapter;

import android.support.v7.widget.RecyclerView;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.live.R;
import com.example.live.bean.ListLiveBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      21:11
 * QQ:             1981367757
 */

public class ListLiveAdapter extends BaseRecyclerAdapter<ListLiveBean.DataEntity,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recommend_live_item;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ListLiveBean.DataEntity data) {

        RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (holder.getAdapterPosition()%2==0) {
            layoutParams.setMargins(4,0,2,0);
        }else {
            layoutParams.setMargins(2,0,4,0);
        }
        holder.itemView.requestLayout();
        holder.setImageUrl(R.id.iv_item_fragment_recommend_live_item_image,data.getThumb()
            ,R.drawable.live_default,R.drawable.live_default)
                    .setText(R.id.tv_item_fragment_recommend_live_item_description,data.getTitle())
                    .setText(R.id.tv_item_fragment_recommend_live_item_name,data.getNick())
                    .setText(R.id.tv_item_fragment_recommend_live_item_count,data.getView())
                    .setOnItemClickListener();
    }
}
