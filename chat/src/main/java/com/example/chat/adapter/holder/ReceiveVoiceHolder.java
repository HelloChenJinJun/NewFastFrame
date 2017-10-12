package com.example.chat.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.listener.OnDownLoadFileListener;
import com.example.chat.listener.VoiceRecordPlayListener;
import com.example.chat.manager.DownLoadManager;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      19:21
 * QQ:             1981367757
 */

public class ReceiveVoiceHolder extends BaseChatHolder implements OnDownLoadFileListener {
        public ReceiveVoiceHolder(View itemView) {
                super(itemView);
        }

        @Override
        public void bindData(final BaseMessage baseMessage, final ChatMessageAdapter.OnItemClickListener listener, boolean isShowTime) {
                if (isShowTime) {
                        setText(R.id.tv_chat_receive_voice_item_time, TimeUtil.getTime(Long.valueOf(baseMessage.getCreateTime())));
                }
                setVisible(R.id.tv_chat_receive_voice_item_time, isShowTime);

                if (baseMessage instanceof ChatMessage) {
                        setVisible(R.id.tv_chat_receive_voice_item_name, false);
                } else {
                        setVisible(R.id.tv_chat_receive_voice_item_name, true);
                        setText(R.id.tv_chat_receive_voice_item_name, baseMessage.getBelongNick());
                }
                setImageUrl(R.id.iv_chat_receive_voice_item_avatar, baseMessage.getBelongAvatar());
                setImageResource(R.id.iv_chat_receive_voice_item_volume, R.drawable.voice_right3);
                setOnClickListener(R.id.iv_chat_receive_voice_item_volume, new VoiceRecordPlayListener(getContext(), (ImageView) getView(R.id.iv_chat_receive_voice_item_volume), baseMessage, this));
                setOnClickListener(R.id.iv_chat_receive_voice_item_avatar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (listener != null) {
                                        listener.onAvatarClick(v, getAdapterPosition(), false);
                                }
                        }
                });
                setOnClickListener(R.id.iv_chat_receive_voice_item_resend, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                DownLoadManager.getInstance().downFile(baseMessage, ReceiveVoiceHolder.this);
                        }
                });
        }

        @Override
        public void onStart() {
                setVisible(R.id.pb_chat_receive_voice_item_load, true);
                setVisible(R.id.iv_chat_receive_voice_item_resend, false);
        }

        @Override
        public void onProgress(int value) {

        }

        @Override
        public void onSuccess(String localPath) {
                setVisible(R.id.pb_chat_receive_voice_item_load, false);

        }

        @Override
        public void onFailed(BmobException e) {
                LogUtil.e("语音失败的原因" + e.getMessage() + e.getErrorCode());
                setVisible(R.id.pb_chat_receive_voice_item_load, false);
                setVisible(R.id.iv_chat_receive_voice_item_resend, true);

        }
}
