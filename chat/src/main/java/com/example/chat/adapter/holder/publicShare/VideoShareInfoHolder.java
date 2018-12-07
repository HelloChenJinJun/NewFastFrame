package com.example.chat.adapter.holder.publicShare;

import android.view.View;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.bean.post.PostDataBean;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/8     17:46
 * QQ:         1981367757
 */

public class VideoShareInfoHolder extends BaseShareInfoViewHolder {
    private DefaultVideoPlayer mDefaultVideoPlayer;

    public VideoShareInfoHolder(View view) {
        super(view);
        ViewStub viewStub = itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
        viewStub.setLayoutResource(R.layout.item_fragment_share_info_video);
        mDefaultVideoPlayer = viewStub.inflate().findViewById(R.id.dvp_item_fragment_share_info_video_container);
    }


    @Override
    protected void initData(PostDataBean data) {
        if (data.getImageList() != null && data.getImageList().size() > 1) {
            for (String item :
                    data.getImageList()) {
                if (item.endsWith(".mp4")) {
                    mDefaultVideoPlayer.setUp(item, null);
                } else {
                    mDefaultVideoPlayer.setImageCover(item);
                }
            }
        }
    }
}
