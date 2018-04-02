package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      0:17
 * QQ:             1981367757
 */

public class BlackAdapter extends BaseRecyclerAdapter<UserEntity, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return R.layout.black_list_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, UserEntity data) {
                holder.setText(R.id.tv_black_list_item_nick, data.getNick())
                        .setImageUrl(R.id.riv_black_list_item_avatar, data.getAvatar());
        }
}
