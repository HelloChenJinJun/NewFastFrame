package com.example.chat.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/20      12:27
 * QQ:             1981367757
 */

public class ContactsAdapter extends BaseRecyclerAdapter<UserEntity, BaseWrappedViewHolder> {

        private SectionIndexer mSectionIndexer;
        public interface OnItemCheckListener{
                public void onItemChecked(boolean isCheck, UserEntity user, BaseWrappedViewHolder holder);
        }


        private OnItemCheckListener mOnCheckedChangeListener;

        public void setOnCheckedChangeListener(OnItemCheckListener onCheckedChangeListener) {
                mOnCheckedChangeListener = onCheckedChangeListener;
        }


        @Override
        protected int getLayoutId() {
                return R.layout.fragment_contacts_list_item;
        }

        @Override
        protected void convert(final BaseWrappedViewHolder holder, final UserEntity data) {
                int position = holder.getAdapterPosition();
                int selection = mSectionIndexer.getSectionForPosition(position);
                if (position == mSectionIndexer.getPositionForSection(selection)) {
//                                首节的一个用户
                        holder.setVisible(R.id.tv_contacts_item_bg, true)
                                .setVisible(R.id.iv_contacts_item_divider, false)
                                .setText(R.id.tv_contacts_item_bg, (String) mSectionIndexer.getSections()[selection]);
                } else {
                        holder.setVisible(R.id.tv_contacts_item_bg, false)
                                .setVisible(R.id.iv_contacts_item_divider, true);
                }
                holder.setText(R.id.tv_fragment_contacts_item_name, data.getName());
                holder.setImageUrl(R.id.iv_fragment_contacts_item_avatar, data.getAvatar())
                .setOnItemClickListener();
                if (mOnCheckedChangeListener != null) {
                        holder.setVisible(R.id.cb_fragment_contacts_item_check,true);
                        ((CheckBox) holder.getView(R.id.cb_fragment_contacts_item_check))
                                .setOnCheckedChangeListener((buttonView, isChecked) -> mOnCheckedChangeListener.onItemChecked(isChecked,data,holder));
                }else {
                        holder.setVisible(R.id.cb_fragment_contacts_item_check,false);
                }
        }

        public void setSectionIndexer(SectionIndexer indexer) {
                this.mSectionIndexer = indexer;
        }



}
