package com.example.chat.adapter.holder.chat;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.adapter.holder.chat.BaseChatHolder;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.MessageContent;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.UserEntity;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:55
 * QQ:             1981367757
 */

public class SendLocationHolder extends BaseChatHolder {
        public SendLocationHolder(View itemView) {
                super(itemView);
                ViewStub viewStub= (ViewStub) getView(R.id.vs_item_activity_chat_send_view_stub);
                viewStub.setLayoutResource(R.layout.item_activity_chat_send_location);
                viewStub.inflate();
        }

        @Override
        public void bindData(BaseMessage baseMessage, UserEntity userEntity) {
                MessageContent messageContent= BaseApplication
                        .getAppComponent().getGson().fromJson(baseMessage.getContent(),MessageContent.class);
                setImageUrl(R.id.iv_item_activity_chat_send_location_thumb,messageContent.getUrlList().get(0))
                .setText(R.id.tv_item_activity_chat_send_location_content,messageContent.getAddress())
                .setOnItemChildClickListener(R.id.rl_chat_send_location_item_content);
        }

}
