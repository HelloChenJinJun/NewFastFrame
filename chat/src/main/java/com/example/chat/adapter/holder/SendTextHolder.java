package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:33
 * QQ:             1981367757
 */

public class SendTextHolder extends BaseChatHolder {
        public SendTextHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                if (isShowTime) {
                        setText(R.id.tv_chat_send_text_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_send_text_item_time, isShowTime);

                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_send_text_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_send_text_item_name, true);
                        setText(R.id.tv_chat_send_text_item_name, baseMessage.getBelongNick());
                }
                setImageUrl(R.id.iv_chat_send_text_item_avatar, baseMessage.getBelongAvatar());
                setText(R.id.tv_chat_send_text_item_message, FaceTextUtil.toSpannableString(getContext(), baseMessage.getContent()));
                setOnClickListener(R.id.iv_chat_send_text_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), true);
                                }
                        }
                });
                int sendStatus = baseMessage.getSendStatus();
                int readStatus = baseMessage.getReadStatus();
                if (sendStatus == Constant.SEND_STATUS_SUCCESS) {
//                         发送状态成功
                        setVisible(R.id.pb_chat_send_text_item_load, false);
                        setVisible(R.id.iv_chat_send_text_item_resend, false);
                        setVisible(R.id.tv_chat_send_text_item_readed, true);
                        if (readStatus == Constant.READ_STATUS_READED) {
                                setText(R.id.tv_chat_send_text_item_readed, "已阅读");
                        } else {
                                setText(R.id.tv_chat_send_text_item_readed, "已发送");
                        }
                } else if (sendStatus == Constant.SEND_STATUS_FAILED) {
                        //                        发送失败
                        setVisible(R.id.pb_chat_send_text_item_load, false);
                        setVisible(R.id.iv_chat_send_text_item_resend, true);
                        setVisible(R.id.tv_chat_send_text_item_readed, false);
                } else {
                        setVisible(R.id.pb_chat_send_text_item_load, true);
                        setVisible(R.id.iv_chat_send_text_item_resend, false);
                        setVisible(R.id.tv_chat_send_text_item_readed, false);
                }
                if (getView(R.id.iv_chat_send_text_item_resend).getVisibility() == View.VISIBLE) {
                        setOnClickListener(R.id.iv_chat_send_text_item_resend, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onItemResendClick(v, baseMessage, getAdapterPosition());
                                        }
                                }
                        });
                }
                setOnClickListener(R.id.tv_chat_send_text_item_message, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onMessageClick(v, getAdapterPosition());
                                }
                        }
                });


        }


}
