package com.example.chat.adapter;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.baseadapter.adapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.ChatMessageEntity;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/15      20:30
 * QQ:             1981367757
 *
 * 邀请列表适配器
 *
 */

public class NewFriendAdapter extends BaseSwipeRecyclerAdapter<ChatMessageEntity, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return  R.layout.activity_new_friend_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, ChatMessageEntity data, boolean isSwipeItem) {
                UserManager.getInstance().findUserById(data.getBelongId(), new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                                if (list != null && list.size() > 0) {
                                        holder.setImageUrl(R.id.iv_new_friend_item_avatar, list.get(0).getAvatar())
                                                .setText(R.id.tv_new_friend_item_name, list.get(0).getName());
                                }
                        }
                });
                if (data.getReadStatus()==Constant.READ_STATUS_READED) {
                        holder.setVisible(R.id.tv_new_friend_item_agree, true);
                        holder.setVisible(R.id.btn_new_friend_item_agree, false);
                } else if (data.getReadStatus()==Constant.READ_STATUS_READED) {
                        holder.setVisible(R.id.btn_new_friend_item_agree, false);
                        holder.setVisible(R.id.tv_new_friend_item_agree, true);
                }
                holder.setOnItemChildClickListener(R.id.btn_new_friend_item_agree);
        }



}
