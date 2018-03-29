package com.example.chat.adapter;

import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.manager.UserDBManager;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.DensityUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/5      15:27
 * QQ:             1981367757
 */
public class MenuDisplayAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {



        @Override
        protected int getLayoutId() {
                return R.layout.menu_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
                holder.setText(R.id.tv_menu_item, data).setOnItemClickListener();
                int position = holder.getAdapterPosition();
                long size;
                if (position == 0) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_chat_blue_grey_900_24dp), null, null, null);
                        if ((size = UserDBManager.getInstance().getUnReadMessageSize()) > 0) {
                                holder.setVisible(R.id.tv_menu_item_tips, true)
                                        .setText(R.id.tv_menu_item_tips, size + "");
                        } else {
                                holder.setVisible(R.id.tv_menu_item_tips, false);
                        }

                } else if (position == 1) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_people_blue_grey_900_24dp), null, null, null);
                } else if (position == 2) {
                        if ((size=UserDBManager.getInstance().getAddInvitationMessageSize()) > 0) {
                                holder.setVisible(R.id.tv_menu_item_tips, true)
                                        .setText(R.id.tv_menu_item_tips, size + "");
                        } else {
                                holder.setVisible(R.id.tv_menu_item_tips, false);
                        }
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_insert_invitation_blue_grey_900_24dp), null, null, null);
                } else if (position == 3) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_fiber_new_blue_grey_900_24dp), null, null, null);
                }
                ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablePadding(DensityUtil.toDp(10));
        }
}
