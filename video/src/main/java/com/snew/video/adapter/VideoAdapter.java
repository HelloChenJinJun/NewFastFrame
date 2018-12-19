package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.manager.video.DefaultVideoController;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.snew.video.R;
import com.snew.video.bean.VideoBean;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/23     11:06
 */
public class VideoAdapter extends BaseRecyclerAdapter<VideoBean, BaseWrappedViewHolder> {



    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_video_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, VideoBean data) {
        DefaultVideoPlayer defaultVideoPlayer = (DefaultVideoPlayer) holder.getView(R.id.dvp_item_activity_video_list_display);
        defaultVideoPlayer.setTitle(data.getTitle()).setClarity(data.getClarities())
                .setImageCover(data.getImageCover())
                .setUp(data.getUrl(), data.getHeaders());
    }


    @Override
    public void onViewRecycled(BaseWrappedViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getView(R.id.dvp_item_activity_video_list_display) != null) {
            ((DefaultVideoPlayer) holder.getView(R.id.dvp_item_activity_video_list_display)).release();
        }
    }
}
