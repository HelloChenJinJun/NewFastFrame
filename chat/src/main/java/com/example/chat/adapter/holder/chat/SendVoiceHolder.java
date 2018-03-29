package com.example.chat.adapter.holder.chat;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.bean.BaseMessage;
import com.example.chat.listener.OnDownLoadFileListener;
import com.example.chat.listener.VoiceRecordPlayListener;
import com.example.commonlibrary.bean.chat.UserEntity;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:13
 * QQ:             1981367757
 */

public class SendVoiceHolder extends BaseChatHolder {

        public SendVoiceHolder(View itemView) {
                super(itemView);
                ViewStub viewStub= (ViewStub) getView(R.id.vs_item_activity_chat_send_view_stub);
                viewStub.setLayoutResource(R.layout.item_activity_chat_send_voice);
                viewStub.inflate();
        }

        @Override
        public void bindData(BaseMessage baseMessage, UserEntity userEntity) {
                setOnItemChildClickListener(R.id.iv_item_activity_chat_send_voice_volume
                ,new VoiceRecordPlayListener(getContext(), (ImageView) getView(R.id.iv_item_activity_chat_send_voice_volume),baseMessage, new OnDownLoadFileListener() {
                                @Override
                                public void onStart() {
                                        setVisible(R.id.pb_item_activity_chat_send_loading,true)
                                                .setVisible(R.id.iv_item_activity_chat_send_retry,false);
                                }

                                @Override
                                public void onProgress(int value) {
                                        setVisible(R.id.pb_item_activity_chat_send_loading,false);
                                }

                                @Override
                                public void onSuccess(String localPath) {
                                        setVisible(R.id.pb_item_activity_chat_send_loading,false);
                                }

                                @Override
                                public void onFailed(BmobException e) {
                                        setVisible(R.id.pb_item_activity_chat_send_loading,false)
                                                .setVisible(R.id.iv_item_activity_chat_send_retry,true);
                                }
                        }));

        }





}
