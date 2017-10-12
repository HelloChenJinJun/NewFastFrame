package com.example.chat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.listener.OnDownLoadFileListener;
import com.example.chat.listener.VoiceRecordPlayListener;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:13
 * QQ:             1981367757
 */

public class SendVoiceHolder extends BaseChatHolder implements OnDownLoadFileListener {
        private Context mContext;

        public SendVoiceHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
        }


        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                LogUtil.e("声音如下");
                if (baseMessage instanceof ChatMessage) {
                        LogUtil.e((ChatMessage) baseMessage);
                }else {
                        LogUtil.e((GroupChatMessage) baseMessage);
                }
                if (isShowTime) {
                        setText(R.id.tv_chat_send_voice_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_send_voice_item_time, isShowTime);
                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_send_voice_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_send_voice_item_name, true);
                        setText(R.id.tv_chat_send_voice_item_name, baseMessage.getBelongNick());
                }
                setImageUrl(R.id.iv_chat_send_voice_item_avatar, baseMessage.getBelongAvatar());
                int sendStatus = baseMessage.getSendStatus();
                if (sendStatus == Constant.SEND_STATUS_START || sendStatus == Constant.SEND_STATUS_SENDING) {
                        setVisible(R.id.pb_chat_send_voice_item_load, true)
                                .setVisible(R.id.tv_chat_send_voice_item_readed, false)
                                .setVisible(R.id.iv_chat_send_voice_item_resend, false);
                } else if (sendStatus == Constant.SEND_STATUS_FAILED) {
                        setVisible(R.id.pb_chat_send_voice_item_load, false)
                                .setVisible(R.id.tv_chat_send_voice_item_readed, false)
                                .setVisible(R.id.iv_chat_send_voice_item_resend, true);
                } else {
//                        发送成功
                        setVisible(R.id.pb_chat_send_voice_item_load, false)
                                .setVisible(R.id.tv_chat_send_voice_item_readed, true)
                                .setVisible(R.id.iv_chat_send_voice_item_resend, false);
                        int readStatus = baseMessage.getReadStatus();
                        if (readStatus == Constant.READ_STATUS_UNREAD) {
                                setText(R.id.tv_chat_send_voice_item_readed, "已发送");
                        } else {
                                setText(R.id.tv_chat_send_voice_item_readed, "已阅读");
                        }
                }
                setOnClickListener(R.id.iv_chat_send_voice_item_resend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onItemResendClick(v, baseMessage, getAdapterPosition());
                                }
                        }
                })
               . setOnClickListener(R.id.iv_chat_send_voice_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), true);
                                }
                        }
                })
                .setImageResource(R.id.iv_chat_send_voice_item_volume, R.drawable.voice_left3)
                .setOnClickListener(R.id.iv_chat_send_voice_item_volume, new VoiceRecordPlayListener(mContext, (ImageView) getView(R.id.iv_chat_send_voice_item_volume), baseMessage,this));
        }

        @Override
        public void onStart() {
                setVisible(R.id.pb_chat_send_voice_item_load, true);
                setVisible(R.id.iv_chat_send_voice_item_resend, false);
        }

        @Override
        public void onProgress(int value) {

        }

        @Override
        public void onSuccess(String localPath) {
                setVisible(R.id.pb_chat_send_voice_item_load, false);
        }

        @Override
        public void onFailed(BmobException e) {
                LogUtil.e("语音失败的原因" + e.getMessage() + e.getErrorCode());
                setVisible(R.id.pb_chat_send_voice_item_load, false);
                setVisible(R.id.iv_chat_send_voice_item_resend, true);

        }
}
