package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.manager.video.DefaultVideoController;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.manager.video.VideoController;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.snew.video.R;
import com.snew.video.bean.VideoBean;
import com.snew.video.bean.VideoDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/23     11:06
 */
public class VideoAdapter extends BaseRecyclerAdapter<VideoBean, BaseWrappedViewHolder> {

    private DefaultVideoController.OnItemClickListener mOnItemClickListener;


    public VideoAdapter() {
        super();
        RxBusManager.getInstance().registerEvent(VideoDetailBean.class, videoDetailBean -> {
            if (videoDetailBean != null) {
                if (videoDetailBean.getRetCode() != 200) {
                    ListVideoManager.getInstance().error();
                } else {
                    List<VideoController.Clarity> list = new ArrayList<>();
                    for (VideoDetailBean.DataBean.VideoBean.LinkBean dataBean :
                            videoDetailBean.getData().getVideo().getLink()) {
                        VideoController.Clarity clarity = new VideoController.Clarity(dataBean.getType().substring(0, 2)
                                , dataBean.getType().substring(2, dataBean.getType().length()), dataBean.getUrl());
                        list.add(clarity);
                    }
                    ListVideoManager.getInstance().getCurrentPlayer().setClarity(list);
                    ListVideoManager.getInstance().updateUrl(videoDetailBean.getData().getVideo().getLink().get(0).getUrl());
                }
            }
        });

    }


    public void setOnItemClickListener(DefaultVideoController.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

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
        ((DefaultVideoController) defaultVideoPlayer.getController()).setOnItemClickListener(mOnItemClickListener);
    }


    @Override
    public void onViewRecycled(BaseWrappedViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getView(R.id.dvp_item_activity_video_list_display) != null) {
            ((DefaultVideoPlayer) holder.getView(R.id.dvp_item_activity_video_list_display)).release();
        }
    }
}
