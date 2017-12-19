package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.news.R;
import com.example.news.bean.ScoreBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     14:57
 * QQ:         1981367757
 */

public class ScoreQueryAdapter extends BaseRecyclerAdapter<ScoreBean.ListBean,BaseWrappedViewHolder> {

    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_score_query;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ScoreBean.ListBean data) {
                holder.setText(R.id.tv_item_activity_score_query_time,data.getXN())
                        .setText(R.id.tv_item_activity_score_query_course_name,data.getKCMC())
                        .setText(R.id.tv_item_activity_score_query_course_number,data.getKCH())
                        .setText(R.id.tv_item_activity_score_query_course_score,data.getKCCJ()+"")
                        .setText(R.id.tv_item_activity_score_query_exam,data.getXQM())
                        .setText(R.id.tv_item_activity_score_query_jd,data.getJD()+"")
                        .setText(R.id.tv_item_activity_score_query_xf,data.getXF()+"");
    }
}
