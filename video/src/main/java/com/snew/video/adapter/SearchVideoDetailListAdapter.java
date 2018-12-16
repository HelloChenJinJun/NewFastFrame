package com.snew.video.adapter;

import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.snew.video.R;
import com.snew.video.bean.SearchVideoDetailListBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/15     11:43
 */
public class SearchVideoDetailListAdapter extends BaseRecyclerAdapter<SearchVideoDetailListBean, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_fragment_search_video_detail_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SearchVideoDetailListBean data) {

    }
}
