package com.example.live.adapter;

import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.live.util.LiveUtil;
import com.example.live.R;
import com.example.live.adapter.holder.RecommendLiveViewHolder;
import com.example.live.ui.VideoActivity;
import com.example.live.bean.RecommendLiveBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:26
 * QQ:             1981367757
 */

public class RecommendLiveAdapter extends BaseRecyclerAdapter<RecommendLiveBean.RoomBean, RecommendLiveViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_recommend_live;
    }

    @Override
    public void convert(RecommendLiveViewHolder holder, RecommendLiveBean.RoomBean data) {
        holder.setOnRecyclerViewItemClickListener(new RecommendLiveViewHolder.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RecommendLiveBean.RoomBean.ListBean bean) {
                Intent intent = new Intent(view.getContext(), VideoActivity.class);
                intent.putExtra(LiveUtil.UID, bean.getUid()+"");
                intent.putExtra(LiveUtil.IS_FULL, LiveUtil.SHOWING.equalsIgnoreCase(bean.getCategory_slug()));
                intent.putExtra(LiveUtil.THUMB,bean.getThumb());
                view.getContext().startActivity(intent);

            }
        }).setData(data.getList()).setText(R.id.tv_item_fragment_recommend_live_title, data.getName())
                .setImageUrl(R.id.iv_item_fragment_recommend_live_icon, data.getIcon(), R.drawable.default_recommend_icon
                        , R.drawable.default_recommend_icon).setOnClickListener(R.id.tv_item_fragment_recommend_live_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
