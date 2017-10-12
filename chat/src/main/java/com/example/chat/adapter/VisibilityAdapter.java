package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.manager.UserCacheManager;
import com.example.commonlibrary.baseadapter.adapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/4      21:06
 * QQ:             1981367757
 */
public class VisibilityAdapter extends BaseSwipeRecyclerAdapter<String, BaseWrappedViewHolder> {
//        public VisibilityAdapter(List<String> data, int layoutId) {
//                super(data, layoutId);
//        }

        @Override
        protected int getLayoutId() {
                return R.layout.edit_share_message_visibility_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data, boolean isSwipeItem) {
                User user = UserCacheManager.getInstance().getUser(data);
                if (user != null) {
                        holder.setText(R.id.tv_edit_share_message_visibility_item_name, user.getNick())
                                .setText(R.id.tv_edit_share_message_visibility_item_signature, user.getSignature())
                                .setImageUrl(R.id.riv_edit_share_message_visibility_item_avatar, user.getAvatar());
                }
        }
}
