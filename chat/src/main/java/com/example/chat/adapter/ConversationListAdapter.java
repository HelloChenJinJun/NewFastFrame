package com.example.chat.adapter;

import android.text.TextUtils;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.RecentMsg;
import com.example.chat.db.ChatDB;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      9:06
 * QQ:             1981367757
 */

public class ConversationListAdapter extends BaseSwipeRecyclerAdapter<RecentMsg, BaseWrappedViewHolder> {

//        public ConversationListAdapter(List<RecentMsg> data, int layoutId) {
//                super(data, layoutId);
//        }

        @Override
        protected int getLayoutId() {
                return R.layout.fragment_recent_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, RecentMsg data, boolean isSwipeItem) {
                LogUtil.e(data);
                if (TextUtils.isEmpty(data.getNick())) {
                        holder.setText(R.id.tv_recent_name, data.getName());
                } else {
                        holder.setText(R.id.tv_recent_name, data.getNick());
                }
                holder.setText(R.id.tv_recent_content, FaceTextUtil.toSpannableString(holder.itemView.getContext(), data.getLastMsgContent()))
                        .setText(R.id.tv_recent_time, TimeUtil.getTime(Long.parseLong(data.getTime())))
                        .setImageUrl(R.id.riv_recent_avatar, data.getAvatar())
                .setOnItemClickListener();
                int msgType = data.getMsgType();
                if (msgType == Constant.TAG_MSG_TYPE_LOCATION) {
                        holder.setText(R.id.tv_recent_content, "[位置]");
                } else if (msgType == Constant.TAG_MSG_TYPE_IMAGE) {
                        holder.setText(R.id.tv_recent_content, "[图片]");
                } else if (msgType == Constant.TAG_MSG_TYPE_VOICE) {
                        holder.setText(R.id.tv_recent_content, "[语音]");
                } else if (msgType == Constant.TAG_MSG_TYPE_TEXT) {
                        holder.setText(R.id.tv_recent_content, FaceTextUtil.toSpannableString(holder.itemView.getContext(), data.getLastMsgContent()));
                } else {
                        LogUtil.e("msgType：" + msgType);
                        LogUtil.e("belongId：" + data.getBelongId());
                        LogUtil.e("name:" + data.getName());
                        holder.setText(R.id.tv_recent_content, "[未知类型]");
                }
                int unReadCount = 0;
                if (MessageCacheManager.getInstance().getGroupTableMessage(data.getBelongId()) == null) {
                        unReadCount = ChatDB.create().queryRecentConversationUnreadCount(data.getBelongId(), UserManager.getInstance().getCurrentUserObjectId());
                } else {
                        unReadCount = ChatDB.create().queryGroupChatMessageUnreadCount(data.getBelongId());
                }
                if (unReadCount > 0) {
                        holder.setVisible(R.id.tv_recent_unread, true);
                        holder.setText(R.id.tv_recent_unread, unReadCount + "");
                } else {
                        holder.setVisible(R.id.tv_recent_unread, false);
                }
        }



        @Override
        public void addData(int position, RecentMsg newData) {
                if (data.contains(newData)) {
                        int index = data.indexOf(newData);
                        data.set(index, newData);
                        notifyDataSetChanged();
                } else {
                        super.addData(position, newData);
                }
        }
}
