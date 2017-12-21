package com.example.news.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.CommonUtil;
import com.example.news.R;
import com.example.news.bean.ConsumeQueryBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:10
 * QQ:         1981367757
 */

public class ConsumeQueryAdapter extends BaseRecyclerAdapter<ConsumeQueryBean.ListBean,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_consume_query;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ConsumeQueryBean.ListBean data) {
            holder.setText(R.id.tv_item_activity_consume_query_time, CommonUtil.getTime(data.getJYSJ(),"yyyy-MM-dd HH:mm:ss"))
                    .setText(R.id.tv_item_activity_consume_query_consume,String.valueOf(data.getJYJE()/100.0))
                    .setText(R.id.tv_item_activity_consume_query_money,String.valueOf(data.getSYJE()/100.0))
                    .setText(R.id.tv_item_activity_consume_query_type,data.getJYMC())
                    .setText(R.id.tv_item_activity_consume_query_address,data.getSH_NAME());
    }
}
