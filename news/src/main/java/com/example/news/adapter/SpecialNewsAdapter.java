package com.example.news.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.RawSpecialNewsBean;
import com.example.news.bean.SpecialNewsBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      20:08
 * QQ:             1981367757
 */

public class SpecialNewsAdapter extends BaseMultipleRecyclerAdapter<SpecialNewsBean, BaseWrappedViewHolder> {

    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(SpecialNewsBean.TYPE_HEADER, R.layout.item_activity_special_news_header);
        sparseArray.put(SpecialNewsBean.TYPE_NORMAL, R.layout.item_activity_special_news_normal);
        sparseArray.put(SpecialNewsBean.TYPE_PHOTO_SET, R.layout.item_activity_special_news_photo);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SpecialNewsBean data) {
        if (data.getItemViewType() == SpecialNewsBean.TYPE_HEADER) {
            holder.setText(R.id.tv_item_activity_special_news_header_title, data.getTitle());
        } else {
            holder.setText(R.id.tv_item_activity_special_news_title, data.getBean().getTitle())
                    .setText(R.id.tv_item_activity_special_news_time, data.getBean().getPtime())
                    .setText(R.id.tv_item_activity_special_news_from, data.getBean().getSource())
                    .setImageUrl(R.id.iv_item_activity_special_news_display, data.getBean().getImgsrc())
                    .setOnItemClickListener();
            if (data.getItemViewType() == SpecialNewsBean.TYPE_PHOTO_SET) {
                if (data.getBean().getImgextra() != null) {
                    int size = data.getBean().getImgextra().size();
                    for (int i = 0; i < size; i++) {
                        RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity imageEntity = data.getBean().getImgextra().get(i);
                        if (i == 0) {
                            holder.setImageUrl(R.id.iv_item_activity_special_news_two
                                    , imageEntity.getImgsrc());
                        } else if (i == 1) {
                            holder.setImageUrl(R.id.iv_item_activity_special_news_three
                                    , imageEntity.getImgsrc());
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    public int getPositionFromTitle(String title) {
        int size=data.size();
        int result=-1;
        for (int i = 0; i < size; i++) {
            SpecialNewsBean bean=data.get(i);
            if (bean.getItemViewType() == SpecialNewsBean.TYPE_HEADER && bean.getTitle().equals(title)) {
                result=i+getItemUpCount();
            }
        }
        return result;
    }
}
