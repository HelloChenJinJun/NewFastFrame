package com.example.live.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.live.R;
import com.example.live.bean.RecommendLiveBean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:51
 * QQ:             1981367757
 */

public class RecommendLiveViewHolder extends BaseWrappedViewHolder {
    private ReCommendLiveItemAdapter reCommendLiveItemAdapter;

    public RecommendLiveViewHolder(View itemView) {
        super(itemView);
        initData(((SuperRecyclerView) getView(R.id.srcv_item_fragment_recommend_live_display)));
    }

    private void initData(SuperRecyclerView view) {
        view.setLayoutManager(new WrappedGridLayoutManager(view.getContext(), 2));
        reCommendLiveItemAdapter = new ReCommendLiveItemAdapter();
        view.setNestedScrollingEnabled(false);
        view.setAdapter(reCommendLiveItemAdapter);
        reCommendLiveItemAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(view,position, reCommendLiveItemAdapter.getData(position));
                }
            }
        });
    }

    public RecommendLiveViewHolder setData(List<RecommendLiveBean.RoomBean.ListBean> data) {
        reCommendLiveItemAdapter.clearAllData();
        reCommendLiveItemAdapter.addData(data);
        return this;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position, RecommendLiveBean.RoomBean.ListBean bean);
    }


    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public RecommendLiveViewHolder setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        return this;
    }


    private class ReCommendLiveItemAdapter extends BaseRecyclerAdapter<RecommendLiveBean.RoomBean.ListBean, BaseWrappedViewHolder> {
        @Override
        protected int getLayoutId() {
            return R.layout.item_fragment_recommend_live_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, RecommendLiveBean.RoomBean.ListBean data) {
            RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (holder.getAdapterPosition()%2==0) {
                layoutParams.setMargins(4,0,2,0);
            }else {
                layoutParams.setMargins(2,0,4,0);
            }
            holder.itemView.requestLayout();
            holder.setImageUrl(R.id.iv_item_fragment_recommend_live_item_image, data.getThumb(), R.drawable.live_default, R.drawable.live_default)
                    .setText(R.id.tv_item_fragment_recommend_live_item_name, data.getNick())
                    .setText(R.id.tv_item_fragment_recommend_live_item_description, data.getTitle())
                    .setText(R.id.tv_item_fragment_recommend_live_item_count,data.getView())
                    .setOnItemClickListener();
        }
    }
}
