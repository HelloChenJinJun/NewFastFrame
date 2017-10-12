package com.example.chat.adapter.holder;

import android.view.View;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.adapter.OnShareMessageItemClickListener;
import com.example.chat.bean.SharedMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/27      19:12
 * QQ:             1981367757
 */

public class LinkShareViewHolder extends BaseShareMessageViewHolder {
//        private LinearLayout display;

        public LinkShareViewHolder(View itemView) {
                super(itemView);
                ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.vs_share_fragment_item_main);
                viewStub.setLayoutResource(R.layout.share_fragment_item_link_layout);
                viewStub.inflate();
//                display = (LinearLayout) viewStub.inflate().findViewById(R.id.ll_share_fragment_item_container);
        }


        public void bindData(final SharedMessage sharedMessage, final OnShareMessageItemClickListener listener) {
                //                初始化链接的主要部分数据
                if (sharedMessage.getUrlTitle() == null) {
                        setVisible(R.id.tv_share_fragment_item_link_title,false);
                }else {
                        setVisible(R.id.tv_share_fragment_item_link_title,true)
                                .setText(R.id.tv_share_fragment_item_link_title,sharedMessage.getUrlTitle());
                }
                setVisible(R.id.tv_share_fragment_item_main_link_title,true)
                .setOnClickListener(R.id.ll_share_fragment_item_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onLinkViewClick(sharedMessage);
                                        }
                                }
                        });
                if (sharedMessage.getUrlList().size()==0) {
                        setText(R.id.tv_share_fragment_item_main_link_title, "分享一个精选笑话");
                        setVisible(R.id.iv_share_fragment_item_link_avatar, false);
                } else {
                        setVisible(R.id.iv_share_fragment_item_link_avatar, true)
                       .setImageUrl(R.id.iv_share_fragment_item_link_avatar,sharedMessage.getUrlList().get(0));
                        if (sharedMessage.getUrlList().size() == 1) {
                                if (sharedMessage.getUrlTitle() == null) {
                                        setText(R.id.tv_share_fragment_item_main_link_title, "分享一个美女图片");
                                }else {
                                        setText(R.id.tv_share_fragment_item_main_link_title, "分享一个精选趣图");
                                }
                        } else {
                                setText(R.id.tv_share_fragment_item_main_link_title, "分享一个精选微信");
                        }
                }
        }
}
