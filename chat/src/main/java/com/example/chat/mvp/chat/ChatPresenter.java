package com.example.chat.mvp.chat;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.MessageContent;
import com.example.chat.listener.OnCreateChatMessageListener;
import com.example.chat.manager.MsgManager;
import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     10:29
 * QQ:         1981367757
 */

public class ChatPresenter extends AppBasePresenter<IView<BaseMessage>,ChatModel> {
    public ChatPresenter(IView<BaseMessage> iView, ChatModel baseModel) {
        super(iView, baseModel);
    }


    public void  sendChatMessage(ChatMessage chatMessage){
        MsgManager.getInstance().sendChatMessage(chatMessage, new OnCreateChatMessageListener() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                iView.updateData(baseMessage);
            }

            @Override
            public void onFailed(String errorMsg, BaseMessage baseMessage) {
                    baseMessage.setSendStatus(Constant.SEND_STATUS_FAILED);
                    iView.updateData(baseMessage);
            }
        });

    }

    public void sendGroupChatMessage(GroupChatMessage groupChatMessage){
        MsgManager.getInstance().sendGroupChatMessage(groupChatMessage, new OnCreateChatMessageListener() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                       iView.updateData(baseMessage);
            }

            @Override
            public void onFailed(String errorMsg, BaseMessage baseMessage) {
                        baseMessage.setSendStatus(Constant.SEND_STATUS_FAILED);
                        iView.updateData(baseMessage);
            }
        });
    }

}
