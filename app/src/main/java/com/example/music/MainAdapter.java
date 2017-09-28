package com.example.music;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/28      10:06
 * QQ:             1981367757
 */

public class MainAdapter extends BaseRecyclerAdapter<MainItemBean,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_main;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, MainItemBean data) {
        holder.setText(R.id.tv_item_activity_main_name,data.getName())
                .setImageResource(R.id.iv_item_main_activity_image,data.getResId())
                .setOnItemClickListener();
    }


    @Override
    protected Animator[] getItemAnimator(BaseWrappedViewHolder holder) {
        if (holder.getAdapterPosition() % 2 == 0) {
            return new Animator[]{ObjectAnimator.ofFloat(holder.itemView,"translationX",holder.itemView.getLeft()+holder.itemView.getWidth(),0)};
        }else {
            return new Animator[]{ObjectAnimator.ofFloat(holder.itemView,"translationX",holder.itemView.getWidth(),0)};
        }
    }
}
