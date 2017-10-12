package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:50
 * QQ:             1981367757
 */

public class SendImageHolder extends BaseChatHolder {
        public SendImageHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {

                if (isShowTime) {
                        setText(R.id.tv_chat_send_image_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_send_image_item_time, isShowTime);

                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_send_image_item_name, false);
                } else {                        setText(R.id.tv_chat_send_image_item_name, baseMessage.getBelongNick());

                        setVisible(R.id.tv_chat_send_image_item_name, true);
                }
                setImageUrl(R.id.iv_chat_send_image_item_avatar, baseMessage.getBelongAvatar());
                String content = baseMessage.getContent();
                final String imageUrl;
                if (content.contains("&")) {
                        imageUrl = content.split("&")[0];
                } else {
                        imageUrl = content;
                }
                setImageUrl(R.id.iv_chat_send_image_item_picture, imageUrl);
                int sendStatus = baseMessage.getSendStatus();
                int readStatus = baseMessage.getReadStatus();
                if (sendStatus == Constant.SEND_STATUS_FAILED) {
//                        发送失败
                        setVisible(R.id.tv_chat_send_image_item_readed, false);
                        setVisible(R.id.pb_chat_send_image_item_load, false);
                        setVisible(R.id.iv_chat_send_image_item_resend, true);
                } else if (sendStatus == Constant.SEND_STATUS_SENDING || sendStatus == Constant.SEND_STATUS_START) {
//                        发送的过程中
                        setVisible(R.id.pb_chat_send_image_item_load, true);
                        setVisible(R.id.tv_chat_send_image_item_readed, false);
                        setVisible(R.id.iv_chat_send_image_item_resend, false);
                } else {
//                        发送成功
                        setVisible(R.id.pb_chat_send_image_item_load, false);
                        setVisible(R.id.tv_chat_send_image_item_readed, true);
                        setVisible(R.id.iv_chat_send_image_item_resend, false);
                        if (readStatus == Constant.READ_STATUS_READED) {
                                setText(R.id.tv_chat_send_image_item_readed, "已阅读");
                        } else {
                                setText(R.id.tv_chat_send_image_item_readed, "已发送");
                        }
                }
                setOnClickListener(R.id.iv_chat_send_image_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), true);
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_send_image_item_picture, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onPictureClick(v, imageUrl, getAdapterPosition());
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_send_image_item_resend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onItemResendClick(v, baseMessage, getAdapterPosition());
                                }
                        }
                });
        }
}
