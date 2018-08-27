package com.example.chat.mvp.chat;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.MessageContent;
import com.example.chat.events.RecentEvent;
import com.example.chat.listener.OnCreateChatMessageListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     10:29
 * QQ:         1981367757
 */

public class ChatPresenter extends AppBasePresenter<IView<BaseMessage>,DefaultModel> {
    public ChatPresenter(IView<BaseMessage> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }


    public void  sendChatMessage(ChatMessage chatMessage){
        addSubscription(MsgManager.getInstance().sendChatMessage(chatMessage, new OnCreateChatMessageListener() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                baseMessage.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                UserDBManager.getInstance()
                        .addOrUpdateChatMessage((ChatMessage) baseMessage);
                UserDBManager.getInstance().addOrUpdateRecentMessage(baseMessage);
                RxBusManager.getInstance().post(new RecentEvent(((ChatMessage) baseMessage).getToId(),RecentEvent.ACTION_ADD));
                iView.updateData(baseMessage);
            }

            @Override
            public void onFailed(String errorMsg, BaseMessage baseMessage) {
                baseMessage.setSendStatus(Constant.SEND_STATUS_FAILED);
                UserDBManager.getInstance()
                        .addOrUpdateChatMessage((ChatMessage) baseMessage);
                UserDBManager.getInstance().addOrUpdateRecentMessage(baseMessage);
                RxBusManager.getInstance().post(new RecentEvent(((ChatMessage) baseMessage).getToId(),RecentEvent.ACTION_ADD));
                iView.updateData(baseMessage);
            }
        }));

    }

    public void sendGroupChatMessage(GroupChatMessage groupChatMessage){
        MsgManager.getInstance().sendGroupChatMessage(groupChatMessage, new OnCreateChatMessageListener() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                baseMessage.setReadStatus(Constant.READ_STATUS_READED);
                UserDBManager.getInstance()
                        .addOrUpdateGroupChatMessage((GroupChatMessage) baseMessage);
                UserDBManager.getInstance().addOrUpdateRecentMessage(baseMessage);

                RxBusManager.getInstance().post(new RecentEvent(((GroupChatMessage) baseMessage).getGroupId(),RecentEvent.ACTION_ADD));
                iView.updateData(baseMessage);
            }

            @Override
            public void onFailed(String errorMsg, BaseMessage baseMessage) {
                        baseMessage.setSendStatus(Constant.SEND_STATUS_FAILED);
                UserDBManager.getInstance()
                        .addOrUpdateChatMessage((ChatMessage) baseMessage);
                UserDBManager.getInstance().addOrUpdateRecentMessage(baseMessage);
                        iView.updateData(baseMessage);
            }
        });
    }

}
