package com.example.chat.adapter;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.MessageContent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.adapter.BaseSwipeRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.bean.chat.RecentMessageEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.google.gson.Gson;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      9:06
 * QQ:             1981367757
 */

public class RecentListAdapter extends BaseSwipeRecyclerAdapter<RecentMessageEntity, BaseWrappedViewHolder> {

       private Gson gson= BaseApplication.getAppComponent().getGson();

        @Override
        protected int getLayoutId() {
                return R.layout.fragment_recent_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, RecentMessageEntity data, boolean isSwipeItem) {
               String avatar;
               String name;
                long unReadCount;
                if (data.getType()==RecentMessageEntity.TYPE_PERSON){
                        UserEntity userEntity= UserDBManager.getInstance().getUser(data.getId());
                        avatar=userEntity.getAvatar();
                        name=userEntity.getName();
                        unReadCount=UserDBManager.getInstance()
                                .getUnReadChatMessageSize(userEntity.getUid());
                }else {
                        GroupTableEntity groupTableEntity=UserDBManager
                                .getInstance().getGroupTableEntity(data.getId());
                        avatar=groupTableEntity.getGroupAvatar();
                        name=groupTableEntity.getGroupName();
                        unReadCount=UserDBManager.getInstance().getUnReadGroupChatMessageSize(groupTableEntity
                        .getGroupId());
                }
                holder.setText(R.id.tv_recent_name, name);
                        holder.setText(R.id.tv_recent_time, TimeUtil.getRecentTime(data.getCreatedTime()))
                        .setImageUrl(R.id.riv_recent_avatar,avatar)
                .setOnItemClickListener();
                int contentType = data.getContentType();
                boolean isFailed=(data.getSendStatus()==Constant.SEND_STATUS_FAILED);
            if (isFailed) {
                ((TextView) holder.getView(R.id.tv_recent_content))
                        .setTextColor(holder
                        .getContext().getResources().getColor(R.color.red_500));
            }
                if (contentType == Constant.TAG_MSG_TYPE_LOCATION) {
                        holder.setText(R.id.tv_recent_content,getContent(isFailed,"[位置]"));
                } else if (contentType == Constant.TAG_MSG_TYPE_IMAGE) {
                        holder.setText(R.id.tv_recent_content,getContent(isFailed,"[图片]"));
                } else if (contentType == Constant.TAG_MSG_TYPE_VOICE) {
                        holder.setText(R.id.tv_recent_content, getContent(isFailed,"[语音]"));
                } else if (contentType == Constant.TAG_MSG_TYPE_TEXT) {
                    MessageContent messageContent=gson.fromJson(data.getContent(),MessageContent.class);
                    holder.setText(R.id.tv_recent_content, getContent(isFailed,FaceTextUtil.toSpannableString(holder.itemView.getContext(), messageContent.getContent())));
                } else if (contentType == Constant.TAG_MSG_TYPE_VIDEO) {
                    holder.setText(R.id.tv_recent_content,getContent(isFailed,"[视频]"));
                }
                if (unReadCount > 0) {
                        holder.setVisible(R.id.tv_recent_unread, true);
                        holder.setText(R.id.tv_recent_unread, unReadCount + "");
                } else {
                        holder.setVisible(R.id.tv_recent_unread, false);
                }
        }

    private CharSequence getContent(boolean isFailed, CharSequence content) {
            SpannableStringBuilder stringBuilder=new SpannableStringBuilder();
        if (isFailed) {
            stringBuilder.append("[失败]");
        }
        stringBuilder.append(content);
            return stringBuilder;
    }



}
