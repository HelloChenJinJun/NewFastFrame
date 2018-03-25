package com.example.news.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.news.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      16:01
 * QQ:             1981367757
 */

public class PopWindowAdapter extends BaseRecyclerAdapter<OtherNewsTypeBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_view_fragment_index_pop;
    }


    @Override
    protected void convert(BaseWrappedViewHolder holder, OtherNewsTypeBean data) {

        holder.setText(R.id.item_view_fragment_index_pop_content, data.getName().trim())
                .setOnItemClickListener();
        if (data.getTypeId() == null
                || data.getTypeId().equals("T1348647909107")
                || data.getTypeId().equals("TYPE_DD")) {
            ((TextView) (holder.getView(R.id.item_view_fragment_index_pop_content)))
            .setTextColor(holder.itemView.getContext().getResources().getColor(R.color.grey_500));
        }
    }


    @Override
    protected Animator[] getItemAnimator(BaseWrappedViewHolder holder) {
        return new Animator[]{ObjectAnimator.ofFloat(holder.itemView, "translationX", DensityUtil.getScreenWidth(holder.getContext()) - holder.itemView.getLeft(), 0)};
    }
}
