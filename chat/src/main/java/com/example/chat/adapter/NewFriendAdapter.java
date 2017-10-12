package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.InvitationMsg;
import com.example.commonlibrary.baseadapter.adapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/15      20:30
 * QQ:             1981367757
 */

public class NewFriendAdapter extends BaseSwipeRecyclerAdapter<InvitationMsg, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return  R.layout.activity_new_friend_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, InvitationMsg data, boolean isSwipeItem) {
                holder.setImageUrl(R.id.iv_new_friend_item_avatar, data.getAvatar())
                        .setText(R.id.tv_new_friend_item_name, data.getName());
                if (data.getReadStatus().equals(Constant.RECEIVE_UNREAD)) {
                        holder.setVisible(R.id.tv_new_friend_item_agree, false);
                        holder.setVisible(R.id.btn_new_friend_item_agree, true);
                } else if (data.getReadStatus().equals(Constant.READ_STATUS_READED)) {
                        holder.setVisible(R.id.btn_new_friend_item_agree, false);
                        holder.setVisible(R.id.tv_new_friend_item_agree, true);
                }
                holder.setOnItemChildClickListener(R.id.btn_new_friend_item_agree);
        }


        @Override
        public void addData(int position, InvitationMsg newData) {
                if (data.contains(newData)) {
                        int index = data.indexOf(newData);
                        data.set(index, newData);
                        notifyDataSetChanged();
                } else {
                        super.addData(position, newData);
                }
        }
}
