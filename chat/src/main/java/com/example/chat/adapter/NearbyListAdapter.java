package com.example.chat.adapter;

import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.NearbyListBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;


/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:30
 * QQ:         1981367757
 *
 * 附近地址列表适配器
 */

public class NearbyListAdapter extends BaseRecyclerAdapter<NearbyListBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_nearby_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, NearbyListBean data) {
        if (data.isCheck()) {
            holder.setVisible(R.id.rb_item_activity_nearby_list_check, true);
        } else {
            holder.setVisible(R.id.rb_item_activity_nearby_list_check, false);
        }
        holder.setOnItemClickListener();
        TextView textView = (TextView) holder.getView(R.id.tv_item_activity_nearby_list_location);
        textView.setText(data.getLocationEvent().getTitle());
        if (data.getLocationEvent().getTitle().equals("不显示位置")) {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.blue_500));
            holder.setVisible(R.id.tv_item_activity_nearby_list_detail, false);
        } else if (data.getLocationEvent().getTitle().equals(data.getLocationEvent().getCity())) {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.black_transparency_800));
            holder.setVisible(R.id.tv_item_activity_nearby_list_detail, false);
        } else {
            textView.setTextColor(textView.getResources().getColor(R.color.black_transparency_800));
            holder.setVisible(R.id.tv_item_activity_nearby_list_detail, true)
                    .setText(R.id.tv_item_activity_nearby_list_detail, data.getLocationEvent().getLocation());
        }
    }
}
