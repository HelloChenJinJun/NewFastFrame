package com.example.chat.adapter.holder.chat;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.bean.BaseMessage;
import com.example.chat.listener.OnDownLoadFileListener;
import com.example.chat.listener.VoiceRecordPlayListener;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      19:21
 * QQ:             1981367757
 */

public class ReceiveVoiceHolder extends BaseChatHolder {

        public ReceiveVoiceHolder(View itemView) {
                super(itemView);
                ViewStub viewStub= (ViewStub) getView(R.id.vs_item_activity_chat_receive_view_stub);
                viewStub.setLayoutResource(R.layout.item_activity_chat_receive_voice);
                viewStub.inflate();
        }

        @Override
        public void bindData(BaseMessage baseMessage, UserEntity userEntity) {
                setOnItemChildClickListener(R.id.iv_item_activity_chat_receive_voice_volume
                ,new VoiceRecordPlayListener(getContext(), (ImageView) getView(R.id.iv_item_activity_chat_receive_voice_volume),baseMessage, new OnDownLoadFileListener() {
                                @Override
                                public void onStart() {
                                      ToastUtils.showShortToast("加载语音中........");
                                }

                                @Override
                                public void onProgress(int value) {
                                }

                                @Override
                                public void onSuccess(String localPath) {
//                                        setVisible(R.id.pb_item_activity_chat_receive_loading,false);
                                }

                                @Override
                                public void onFailed(BmobException e) {
                                    ToastUtils.showShortToast("加载失败"+e.toString());
//                                        setVisible(R.id.pb_item_activity_chat_receive_loading,false)
//                                                .setVisible(R.id.iv_item_activity_chat_receive_retry,true);
                                }
                        }));

        }
}
