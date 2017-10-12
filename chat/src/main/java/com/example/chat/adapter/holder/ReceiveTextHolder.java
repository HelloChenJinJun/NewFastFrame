package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      19:07
 * QQ:             1981367757
 */

public class ReceiveTextHolder extends BaseChatHolder {
        public ReceiveTextHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                if (isShowTime) {
                        setText(R.id.tv_chat_receive_text_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_receive_text_item_time, isShowTime);
                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_receive_text_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_receive_text_item_name, true);
                        setText(R.id.tv_chat_receive_text_item_name, baseMessage.getBelongNick());
                }
                String content = baseMessage.getContent();
                setText(R.id.tv_chat_receive_text_item_message, FaceTextUtil.toSpannableString(getContext(), content));
                setImageUrl(R.id.iv_chat_receive_text_item_avatar, baseMessage.getBelongAvatar());
                setOnClickListener(R.id.iv_chat_receive_text_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), false);
                                }
                        }
                });
                setOnClickListener(R.id.tv_chat_receive_text_item_message, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onMessageClick(v, getAdapterPosition());
                                }
                        }
                });
        }
}
