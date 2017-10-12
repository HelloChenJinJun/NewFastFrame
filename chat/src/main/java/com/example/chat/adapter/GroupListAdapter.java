package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.GroupTableMessage;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/10      10:09
 * QQ:             1981367757
 */
public class GroupListAdapter extends BaseRecyclerAdapter<GroupTableMessage, BaseWrappedViewHolder> {



        @Override
        protected int getLayoutId() {
                return R.layout.group_list_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, GroupTableMessage data) {
                holder.setImageUrl(R.id.riv_group_list_item_avatar, data.getGroupAvatar())
                        .setText(R.id.tv_group_list_item_nick, data.getGroupName())
                .setOnItemClickListener();
        }
}
