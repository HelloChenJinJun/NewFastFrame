package com.example.chat.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/20      12:27
 * QQ:             1981367757
 */

public class ContactsAdapter extends BaseRecyclerAdapter<User, BaseWrappedViewHolder> {

        private SectionIndexer mSectionIndexer;








        public interface OnItemCheckListener{

                public void onItemChecked(boolean isCheck, User user, BaseWrappedViewHolder holder);



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
        protected void convert(final BaseWrappedViewHolder holder, final User data) {
                int position = holder.getAdapterPosition();
                int selection = mSectionIndexer.getSectionForPosition(position);
                LogUtil.e("selection:" + selection);
                LogUtil.e("position:" + position);
                if (position == mSectionIndexer.getPositionForSection(selection)) {
//                                首节的一个用户
                        holder.setVisible(R.id.tv_contacts_item_bg, true)
                                .setVisible(R.id.iv_contacts_item_divider, false)
                                .setText(R.id.tv_contacts_item_bg, (String) mSectionIndexer.getSections()[selection]);
                } else {
                        holder.setVisible(R.id.tv_contacts_item_bg, false)
                                .setVisible(R.id.iv_contacts_item_divider, true);
                }
                if (data.getNick() == null || data.getNick().equals("")) {
                        holder.setText(R.id.tv_fragment_contacts_item_name, data.getUsername());
                } else {
                        holder.setText(R.id.tv_fragment_contacts_item_name, data.getNick());
                }
                holder.setImageUrl(R.id.iv_fragment_contacts_item_avatar, data.getAvatar())
                .setOnItemClickListener();

                if (mOnCheckedChangeListener != null) {
                        holder.setVisible(R.id.cb_fragment_contacts_item_check,true);
                        ((CheckBox) holder.getView(R.id.cb_fragment_contacts_item_check))
                                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                mOnCheckedChangeListener.onItemChecked(isChecked,data,holder);
                                        }
                                });
                }else {
                        holder.setVisible(R.id.cb_fragment_contacts_item_check,false);
                }
        }

        public void setSectionIndexer(SectionIndexer indexer) {
                this.mSectionIndexer = indexer;
        }


        @Override
        public void addData(int position, User newData) {
                if (data.contains(newData)) {
                        int index = data.indexOf(newData);
                        data.set(index, newData);
                        notifyDataSetChanged();
                } else {
                        super.addData(position,newData);
                }
        }
}
