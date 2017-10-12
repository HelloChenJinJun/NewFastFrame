package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      19:16
 * QQ:             1981367757
 */

public class ReceiveLocationHolder extends BaseChatHolder {
        public ReceiveLocationHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                if (isShowTime) {
                        setText(R.id.tv_chat_receive_location_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_receive_location_item_time, isShowTime);

                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_receive_location_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_receive_location_item_name, true);
                        setText(R.id.tv_chat_receive_location_item_name, baseMessage.getBelongNick());
                }
                setImageUrl(R.id.iv_chat_receive_location_item_avatar, baseMessage.getBelongAvatar());
                String[] result = baseMessage.getContent().split(",");
                for (String aResult : result) {
                        LogUtil.e("接受内容如下" + aResult);
                }
                setImageUrl(R.id.iv_chat_receive_location_item_display, result[0]);
                setText(R.id.tv_chat_receive_location_item_address, result[3]);
                setOnClickListener(R.id.rl_chat_receive_location_item_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onMessageClick(v, getAdapterPosition());
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_receive_location_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), false);
                                }
                        }
                });
//                setOnClickListener(R.id.iv_chat_receive_location_item_resend, this);
        }
}
