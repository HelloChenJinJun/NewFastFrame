package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:55
 * QQ:             1981367757
 */

public class SendLocationHolder extends BaseChatHolder {
        public SendLocationHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                if (isShowTime) {
                        setText(R.id.tv_chat_send_location_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_send_location_item_time, isShowTime);


                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_send_location_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_send_location_item_name, true);
                        setText(R.id.tv_chat_send_location_item_name, baseMessage.getBelongNick());
                }
//                setImageResource(R.id.iv_chat_send_location_item_avatar, baseMessage.getBelongAvatar(), true, R.drawable.head);
                setImageUrl(R.id.iv_chat_send_location_item_avatar, baseMessage.getBelongAvatar());
                int sendStatus = baseMessage.getSendStatus();
                if (sendStatus == Constant.SEND_STATUS_FAILED) {
//                        发送失败
                        setVisible(R.id.iv_chat_send_location_item_resend, true);
                        setVisible(R.id.pb_chat_send_location_item_load, false);
                        setVisible(R.id.tv_chat_send_location_item_readed, false);
                } else if (sendStatus == Constant.SEND_STATUS_START || sendStatus == Constant.SEND_STATUS_SENDING) {
//                        发送中 ........
                        setVisible(R.id.pb_chat_send_location_item_load, true);
                        setVisible(R.id.tv_chat_send_location_item_readed, false);
                        setVisible(R.id.iv_chat_send_location_item_resend, false);
                } else {
//                        发送成功
                        setVisible(R.id.pb_chat_send_location_item_load, false);
                        setVisible(R.id.tv_chat_send_location_item_readed, true);
                        setVisible(R.id.iv_chat_send_location_item_resend, false);
                        int readStatus = baseMessage.getReadStatus();
                        if (readStatus == Constant.READ_STATUS_UNREAD) {
                                setText(R.id.tv_chat_send_location_item_readed, "已发送");
                        } else {
                                setText(R.id.tv_chat_send_location_item_readed, "已阅读");
                        }
                }
                String[] result = baseMessage.getContent().split(",");
                for (int i = 0; i < result.length; i++) {
                        LogUtil.e("内容如下" + result[i]);
                }
                setImageUrl(R.id.iv_chat_send_location_item_display, result[0]);
                int length = result.length;
                setText(R.id.tv_chat_send_location_item_address, result[length - 1]);
                setOnClickListener(R.id.iv_chat_send_location_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), true);
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_send_location_item_resend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {

                                        listener.onItemResendClick(v, baseMessage, getAdapterPosition());
                                }
                        }
                });
                setOnClickListener(R.id.rl_chat_send_location_item_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onMessageClick(v, getAdapterPosition());
                                }
                        }
                });
        }
}
