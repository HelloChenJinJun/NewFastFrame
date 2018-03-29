package com.example.chat.adapter.holder.chat;

import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.MessageContent;
import com.example.chat.util.FaceTextUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.UserEntity;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:33
 * QQ:             1981367757
 */

public class SendTextHolder extends BaseChatHolder {
        public SendTextHolder(View itemView) {
                super(itemView);
                ViewStub viewStub= (ViewStub) getView(R.id.vs_item_activity_chat_send_view_stub);
                viewStub.setLayoutResource(R.layout.item_activity_chat_send_text);
                 viewStub.inflate();
        }

        @Override
        public void bindData(BaseMessage baseMessage, UserEntity userEntity) {
                MessageContent messageContent= BaseApplication
                        .getAppComponent().getGson().fromJson(baseMessage.getContent(),MessageContent.class);
                setText(R.id.tv_item_activity_chat_send_text,FaceTextUtil.toSpannableString(getContext(),messageContent.getContent()));
        }


}
