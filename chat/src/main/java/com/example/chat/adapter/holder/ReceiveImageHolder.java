package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.util.TimeUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      19:10
 * QQ:             1981367757
 */

public class ReceiveImageHolder extends BaseChatHolder {
        public ReceiveImageHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {

                if (isShowTime) {
                        setText(R.id.tv_chat_receive_image_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_receive_image_item_time, isShowTime);
                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_receive_image_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_receive_image_item_name, true);
                        setText(R.id.tv_chat_receive_image_item_name, baseMessage.getBelongNick());
                }
                setImageUrl(R.id.iv_chat_receive_image_item_avatar, baseMessage.getBelongAvatar());
//                发送过来的图片文本消息的内容是服务器上该图片的URL
                setVisible(R.id.pb_chat_receive_image_item_load, true);
//                setImageResource(R.id)
                setImageUrl(R.id.iv_chat_receive_image_item_picture, baseMessage.getContent());
                setVisible(R.id.pb_chat_receive_image_item_load, false);
                setOnClickListener(R.id.iv_chat_receive_image_item_picture, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onPictureClick(v, baseMessage.getContent(), getAdapterPosition());
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_receive_image_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                listener.onAvatarClick(v, getAdapterPosition(), false);
                        }
                });
        }
}
