package com.example.chat.adapter;

import android.util.SparseIntArray;

import com.example.chat.R;
import com.example.chat.bean.ChatBean;
import com.example.chat.manager.UserManager;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.User;

/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     17:10
 * QQ:         1981367757
 */

public class ChatListAdapter extends BaseMultipleRecyclerAdapter<ChatBean, BaseWrappedViewHolder> {
    private static final long DELTA_TIME = 5 * 60 * 1000L;


    @Override
    protected void convert(BaseWrappedViewHolder holder, ChatBean data) {
        if (holder.getItemViewType() == ChatBean.TYPE_LEFT) {
            if (shouldShowTime(holder.getAdapterPosition())) {
                holder.setVisible(R.id.tv_item_activity_chat_list_left_time, true)
                        .setText(R.id.tv_item_activity_chat_list_left_time, data.getCreatedAt());
            } else {
                holder.setVisible(R.id.tv_item_activity_chat_list_left_time, false);
            }
            holder.setText(R.id.tv_item_activity_chat_list_left_content, data.getContent())
                    .setImageResource(R.id.riv_item_activity_chat_list_left_avatar, R.drawable.manager);
        } else {
            if (shouldShowTime(holder.getAdapterPosition())) {
                holder.setVisible(R.id.tv_item_activity_chat_list_right_time, true)
                        .setText(R.id.tv_item_activity_chat_list_right_time, data.getCreatedAt());
            } else {
                holder.setVisible(R.id.tv_item_activity_chat_list_right_time, false);
            }
            User user = UserManager.getInstance().getCurrentUser();
            holder.setImageUrl(R.id.riv_item_activity_chat_list_right_avatar, user.getAvatar())
                    .setText(R.id.tv_item_activity_chat_list_right_name, user.getNick())
                    .setText(R.id.tv_item_activity_chat_list_right_content, data.getContent())
                    .setOnItemChildLongClickListener(R.id.tv_item_activity_chat_list_right_content);
        }


    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        } else {
            long preTime = TimeUtil.getTime(data.get(position - 1).getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
            long currentTime = TimeUtil.getTime(data.get(position).getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
            return currentTime - preTime > DELTA_TIME;
        }
    }

    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(ChatBean.TYPE_LEFT, R.layout.item_activity_chat_list_left);
        sparseArray.put(ChatBean.TYPE_RIGHT, R.layout.item_activity_chat_list_right);
        return sparseArray;
    }
}
