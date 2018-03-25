package com.example.chat.adapter;

import com.example.chat.R;
import com.example.chat.bean.SystemNotifyBean;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:14
 * QQ:         1981367757
 */

public class SystemNotifyAdapter extends BaseRecyclerAdapter<SystemNotifyBean,BaseWrappedViewHolder>{


    private static final long MAX_TIME=5*60*1000L;




    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_system_notify;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SystemNotifyBean data) {
        holder.setText(R.id.tv_item_activity_system_notify_title,data.getTitle())
                .setText(R  .id.tv_item_activity_system_notify_sub_title,data.getSubTitle())
                .setImageUrl(R.id.iv_item_activity_system_notify_image,data.getImageUrl())
                .setOnItemClickListener();
        if (isShouldShowTime(holder.getAdapterPosition() - getItemUpCount())) {
            holder.setVisible(R.id.tv_item_activity_system_notify_time,true).setText(R.id.tv_item_activity_system_notify_time,data.getCreatedAt());
        }else {
            holder.setVisible(R.id.tv_item_activity_system_notify_time,false);
        }
    }

    private boolean isShouldShowTime(int position) {
        if (position == 0) {
            return true;
        }else {
            SystemNotifyBean preData=getData(position-1);
            SystemNotifyBean currentData=getData(position);
            return TimeUtil.getTime(currentData.getCreatedAt(), "yyyy-MM-dd HH:mm:ss") - TimeUtil.getTime(preData.getCreatedAt(), "yyyy-MM-dd HH:mm:ss") > MAX_TIME;
        }
    }
}
