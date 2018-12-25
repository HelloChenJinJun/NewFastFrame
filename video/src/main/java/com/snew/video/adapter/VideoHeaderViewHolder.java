package com.snew.video.adapter;

import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.utils.DensityUtil;
import com.snew.video.R;
import com.snew.video.bean.QQVideoTabListBean;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     11:09
 */
public class VideoHeaderViewHolder extends BaseWrappedViewHolder {
    SuperRecyclerView display;

    public VideoHeaderViewHolder(View itemView) {
        super(itemView);
        display = itemView.findViewById(R.id.srcv_item_video_fragment_video_header_display);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL));
        display.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
    }


    VideoListHeaderAdapter bindData(QQVideoTabListBean.IndexBean data) {
        VideoListHeaderAdapter videoListHeaderAdapter = new VideoListHeaderAdapter();
        if (data != null && data.getOption() != null && data.getOption().size() > 0) {
            for (int i = 0; i < data.getOption().size(); i++) {
                String itemId = data.getOption().get(i).getValue();
                if (Integer.parseInt(itemId) == data.getDefault_value()) {
                    videoListHeaderAdapter.setCurrentPosition(i);
                    break;
                }
            }
        }
        display.setAdapter(videoListHeaderAdapter);
        videoListHeaderAdapter.refreshData(data.getOption());
        return videoListHeaderAdapter;
    }
}
