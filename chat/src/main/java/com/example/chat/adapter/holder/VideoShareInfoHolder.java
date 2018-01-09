package com.example.chat.adapter.holder;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.ui.ImageDisplayActivity;
import com.example.chat.view.ListImageView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/8     17:46
 * QQ:         1981367757
 */

public class VideoShareInfoHolder extends BaseShareInfoViewHolder {
    public VideoShareInfoHolder(View view) {
        super(view);
        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
        viewStub.setLayoutResource(R.layout.item_fragment_share_info_video);
        viewStub.inflate();
    }


    @Override
    protected void initData(PostDataBean data) {
        if (data.getImageList() != null && data.getImageList().size() > 1) {
            setImageUrl(R.id.iv_item_fragment_share_info_video_display, data.getImageList()
                    .get(0))
                    .setOnItemChildClickListener(R.id.iv_item_fragment_share_info_video_display);
        }


    }
}
