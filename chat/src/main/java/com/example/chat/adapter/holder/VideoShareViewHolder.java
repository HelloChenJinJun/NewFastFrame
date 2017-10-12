package com.example.chat.adapter.holder;

import android.view.View;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.adapter.OnShareMessageItemClickListener;
import com.example.chat.bean.SharedMessage;
import com.example.chat.util.CommonUtils;
import com.example.chat.view.ListImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/27      19:04
 * QQ:             1981367757
 */

public class VideoShareViewHolder extends BaseShareMessageViewHolder {
        private ListImageView display;

        public VideoShareViewHolder(View itemView) {
                super(itemView);
                ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.vs_share_fragment_item_main);
                viewStub.setLayoutResource(R.layout.share_fragment_item_image_layout);
                display = (ListImageView) viewStub.inflate().findViewById(R.id.liv_share_fragment_item_main_image_display);
        }


        @Override
        public void initCommonData(final SharedMessage shareMessage, final OnShareMessageItemClickListener listener) {
                super.initCommonData(shareMessage, listener);

        }

        public void bindData(final SharedMessage sharedMessage, final OnShareMessageItemClickListener listener) {
                List<String> list = new ArrayList<>();
                String wrappedUrl = CommonUtils.list2Content(sharedMessage.getImageList());
                list.add(wrappedUrl);
                display.bindData(list);
                display.setOnImageViewItemClickListener(new ListImageView.OnImageViewItemClickListener() {
                        @Override
                        public void onImageClick(View view, int position, String url) {
                                if (listener != null) {
                                        listener.onPhotoItemClick(view, sharedMessage.getObjectId(), position, url);
                                }
                        }
                });
        }
}
