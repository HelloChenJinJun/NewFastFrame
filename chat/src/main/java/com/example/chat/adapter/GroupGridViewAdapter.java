package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.GroupNumberInfo;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/1      16:52
 * QQ:             1981367757
 *
 *
 * 群成员头像网格适配器
 */
public class GroupGridViewAdapter extends BaseRecyclerAdapter<GroupNumberInfo, BaseWrappedViewHolder> {





        @Override
        protected int getLayoutId() {
                return R.layout.group_info_avatar_item_layout;
        }


        @Override
        public void addData(List<GroupNumberInfo> newData) {
                super.addData(newData);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, GroupNumberInfo data) {
                holder.setText(R.id.tv_group_info_avatar_item_layout_name, data.getGroupNick())
                        .setOnItemChildClickListener(R.id.riv_group_info_avatar_item_layout_avatar);
                if (data.getUser() != null) {
                        holder.setImageUrl(R.id.riv_group_info_avatar_item_layout_avatar, data.getUser().getAvatar());
                }
        }
}
