package com.example.chat.adapter.holder.chat;

import android.view.View;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.MessageContent;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     21:56
 * QQ:         1981367757
 */

public class ReceiveVideoHolder extends BaseChatHolder {
    public ReceiveVideoHolder(View itemView) {
        super(itemView);
        ViewStub viewStub = (ViewStub) getView(R.id.vs_item_activity_chat_receive_view_stub);
        viewStub.setLayoutResource(R.layout.item_activity_chat_receive_video);
        viewStub.inflate();
    }

    @Override
    public void bindData(BaseMessage baseMessage, UserEntity userEntity) {
        MessageContent messageContent = BaseApplication
                .getAppComponent().getGson().fromJson(baseMessage.getContent(), MessageContent.class);
        DefaultVideoPlayer defaultVideoPlayer = (DefaultVideoPlayer) getView(R.id.dvp_item_activity_chat_receive_video);
        for (String item :
                messageContent.getUrlList()) {
            if (item.endsWith(".mp4")) {
                defaultVideoPlayer.setUp(item, null);
            } else {
                defaultVideoPlayer.setImageCover(item);
            }
        }
    }
}
