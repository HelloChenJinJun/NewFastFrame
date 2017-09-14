package com.example.cootek.newfastframe.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.bean.RankListBean;


/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankAdapter extends BaseRecyclerAdapter<RankListBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_rank;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, RankListBean data) {
        holder.setText(R.id.tv_item_fragment_rank_one, "1." + data.getSong_list().get(0).getTitle() + " - " + data.getSong_list().get(0).getArtist_name())
                .setText(R.id.tv_item_fragment_rank_two, "2." + data.getSong_list().get(1).getTitle() + " - " + data.getSong_list().get(1).getArtist_name())
                .setText(R.id.tv_item_fragment_rank_three, "3." + data.getSong_list().get(2).getTitle() + " - " + data.getSong_list().get(2).getArtist_name())
                .setImageUrl(R.id.iv_item_fragment_rank_display, getRealUrl(data.getBillboard().getPic_s192()))
                .setOnItemClickListener();
    }

    private String getRealUrl(String url) {
        CommonLogger.e("地址:"+url);
        return url;
    }
}
