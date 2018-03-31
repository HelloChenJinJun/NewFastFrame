package com.example.chat.adapter;


import com.example.chat.R;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonUtil;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     16:05
 * QQ:         1981367757
 */

public class FriendsAdapter extends BaseRecyclerAdapter<UserEntity, BaseWrappedViewHolder> {



    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_friends;
    }

    private boolean hasSelected;


    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, UserEntity data) {
        holder.setText(R.id.tv_item_fragment_friends_name, data.getNick())
                .setImageUrl(R.id.iv_item_fragment_friends_avatar, data.getAvatar());
        if (hasSelected){
            holder.setVisible(R.id.cb_item_fragment_friends_check,true)
                    .setOnItemChildClickListener(R.id.cb_item_fragment_friends_check);
        }else {
            holder.setVisible(R.id.cb_item_fragment_friends_check,false)
                    .setOnItemClickListener();
        }
        if (isShouldShowTag(holder.getAdapterPosition() - getItemUpCount())) {
            holder.setVisible(R.id.tv_item_fragment_friends_tab, true)
                    .setText(R.id.tv_item_fragment_friends_tab, AppUtil.getSortedKey(data.getNick()));
        } else {
            holder.setVisible(R.id.tv_item_fragment_friends_tab, false);
        }
    }

    private boolean isShouldShowTag(int position) {
        if (position == 0) {
            return true;
        }
        UserEntity preUser = getData(position - 1);
        return !AppUtil.getSortedKey(preUser.getNick()).equals(AppUtil.getSortedKey(getData(position).getNick()));
    }

    public void deleteFriendById(String uid) {
        int size=data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i).getUid().equals(uid)) {
                removeData(i);
                return;
            }
        }
    }
}
