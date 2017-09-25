package com.example.news.adapter;

import android.util.SparseArray;

import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.SpecialNewsBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      20:08
 * QQ:             1981367757
 */

public class SpecialNewsAdapter extends BaseMultipleRecyclerAdapter<SpecialNewsBean,BaseWrappedViewHolder>{

    @Override
    protected SparseArray<Integer> getLayoutIdMap() {
        SparseArray<Integer>  sparseArray=new SparseArray<>();
        sparseArray.put(SpecialNewsBean.TYPE_HEADER, R.layout.item_activity_special_news_header);
        sparseArray.put(SpecialNewsBean.TYPE_NORMAL,R.layout.item_activity_special_news_normal);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SpecialNewsBean data) {
        if (data.getItemViewType() == SpecialNewsBean.TYPE_HEADER) {
            holder.setText(R.id.tv_item_activity_special_news_header_title,data.getTitle());
        }else {
            holder.setText(R.id.tv_item_activity_special_news_normal_title,data.getBean().getTitle())
                    .setText(R.id.tv_item_activity_special_news_normal_time,data.getBean().getPtime())
                    .setText(R.id.tv_item_activity_special_news_normal_from,data.getBean().getSource())
                    .setImageUrl(R.id.iv_item_activity_special_news_normal_display,data.getBean().getImgsrc());
        }
//        if (NewsUtil.SPECIAL_TITLE.equals(data.getBean().getSkipType())) {
//            holder.setVisible(R.id.lv_item_activity_special_news_normal_label,true);
//        }else {
//            holder.setVisible(R.id.lv_item_activity_special_news_normal_label,false);
//        }
    }
}
