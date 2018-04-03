package com.example.chat.adapter.holder.chat;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:14
 * QQ:             1981367757
 */

public abstract class BaseChatHolder extends BaseWrappedViewHolder {
        public BaseChatHolder(View itemView) {
                super(itemView);
        }









        public void initCommonData(BaseMessage baseMessage, boolean isShowTime){
                UserEntity userEntity;
                if (baseMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())){
//                        初始化本用户的界面数据
                         userEntity=UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser());
                        if (isShowTime) {
                                setText(R.id.tv_item_activity_chat_send_time, TimeUtil.getTime(baseMessage.getCreateTime()));
                        }
                        setVisible(R.id.tv_item_activity_chat_send_time,isShowTime)
                                .setImageUrl(R.id.iv_item_activity_chat_send_avatar,userEntity.getAvatar())
                        .setText(R.id.tv_item_activity_chat_send_name,userEntity.getName())
                        .setOnItemChildClickListener(R.id.iv_item_activity_chat_send_avatar)
                        .setOnItemChildClickListener(R.id.iv_item_activity_chat_send_retry);
                        int sendStatus = baseMessage.getSendStatus();
                        int readStatus = baseMessage.getReadStatus();
                        if (sendStatus == Constant.SEND_STATUS_SUCCESS) {
//                         发送状态成功
                                setVisible(R.id.pb_item_activity_chat_send_loading, false)
                                .setVisible(R.id.iv_item_activity_chat_send_retry, false)
                                .setVisible(R.id.tv_item_activity_chat_send_read, true);
                                if (readStatus == Constant.READ_STATUS_READED) {
                                        setText(R.id.tv_item_activity_chat_send_read, "已阅读");
                                } else {
                                        setText(R.id.tv_item_activity_chat_send_read, "已发送");
                                }
                        } else if (sendStatus == Constant.SEND_STATUS_FAILED) {
                                //                        发送失败
                                setVisible(R.id.pb_item_activity_chat_send_loading, false);
                                setVisible(R.id.iv_item_activity_chat_send_retry, true);
                                setVisible(R.id.tv_item_activity_chat_send_read, false);
                        } else {
                                setVisible(R.id.pb_item_activity_chat_send_loading, true);
                                setVisible(R.id.iv_item_activity_chat_send_retry, false);
                                setVisible(R.id.tv_item_activity_chat_send_read, false);
                        }
                }else {
                        userEntity= UserDBManager.getInstance().getUser(baseMessage.getBelongId());
                        if (isShowTime) {
                                setText(R.id.tv_item_activity_chat_receive_time, TimeUtil.getTime(baseMessage.getCreateTime()));
                        }
                        setVisible(R.id.tv_item_activity_chat_receive_time,isShowTime)
                                .setImageUrl(R.id.iv_item_activity_chat_receive_avatar,userEntity.getAvatar())
                                .setText(R.id.tv_item_activity_chat_receive_name,userEntity.getName())
                                .setOnItemChildClickListener(R.id.iv_item_activity_chat_receive_avatar);
                }
                bindData(baseMessage,userEntity);
        }

        public abstract void bindData(BaseMessage baseMessage,UserEntity userEntity);
}
