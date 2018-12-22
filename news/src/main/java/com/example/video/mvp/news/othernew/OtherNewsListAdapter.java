package com.example.video.mvp.news.othernew;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.SparseIntArray;

import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.video.R;
import com.example.video.bean.NewInfoBean;
import com.example.video.util.NewsUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      17:04
 * QQ:             1981367757
 */

public class OtherNewsListAdapter extends BaseMultipleRecyclerAdapter<NewInfoBean, BaseWrappedViewHolder> {

    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(NewInfoBean.TYPE_NORMAL, R.layout.item_fragment_other_news_list);
        sparseArray.put(NewInfoBean.TYPE_PHOTO, R.layout.item_fragment_other_news_list_photo);
        return sparseArray;
    }

    @Override
    protected boolean isApplySkin() {
        return true;

    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, NewInfoBean data) {
        holder.setText(R.id.tv_item_fragment_other_news_list_title, data.getTitle())
                .setText(R.id.tv_item_fragment_other_news_list_time, data.getMtime())
                .setText(R.id.tv_item_fragment_other_news_list_from, data.getSource())
                .setOnItemClickListener();
        if (data.getItemViewType() == NewInfoBean.TYPE_PHOTO) {
            if (data.getImgextra() != null) {
                for (int i = 0; i < data.getImgextra().size(); i++) {
                    NewInfoBean.ImgextraEntity imgextraEntity = data.getImgextra().get(i);
                    if (i == 0) {
                        holder.setImageUrl(R.id.iv_item_fragment_other_news_list_photo_one
                                , imgextraEntity.getImgsrc());
                    } else if (i == 1) {
                        holder.setImageUrl(R.id.iv_item_fragment_other_news_list_photo_two
                                , imgextraEntity.getImgsrc());
                    } else if (i == 2) {
                        holder.setImageUrl(R.id.iv_item_fragment_other_news_list_photo_three
                                , imgextraEntity.getImgsrc());
                        break;
                    }
                }
            }
        } else {
            holder.setImageUrl(R.id.iv_item_fragment_other_news_list_display, data.getImgsrc());
        }
        if (NewsUtil.SPECIAL_TITLE.equals(data.getSkipType())) {
            holder.setVisible(R.id.lv_item_fragment_other_news_list_label, true);
        } else if (NewInfoBean.TYPE_PHOTO != data.getItemViewType()) {
            holder.setVisible(R.id.lv_item_fragment_other_news_list_label, false);
        }
    }


    @Override
    protected Animator[] getItemAnimator(BaseWrappedViewHolder holder) {
        return new Animator[]{ObjectAnimator.ofFloat(holder.itemView, "translationX", holder.itemView.getWidth(), 0)};
    }
}
