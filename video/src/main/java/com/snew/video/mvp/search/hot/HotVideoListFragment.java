package com.snew.video.mvp.search.hot;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.snew.video.R;
import com.snew.video.adapter.HotVideoAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.CommonVideoBean;
import com.snew.video.bean.HotVideoItemBean;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     19:35
 */
public class HotVideoListFragment extends VideoBaseFragment {
    private SuperRecyclerView display;


    private HotVideoAdapter mHotVideoAdapter;

    public static HotVideoListFragment newInstance(ArrayList<HotVideoItemBean> data) {
        HotVideoListFragment hotVideoListFragment = new HotVideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoUtil.DATA, data);
        hotVideoListFragment.setArguments(bundle);
        return hotVideoListFragment;
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_hot_video_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_hot_video_list_display);
    }

    @Override
    protected void initData() {
        mHotVideoAdapter = new HotVideoAdapter();
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(mHotVideoAdapter);
        mHotVideoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                HotVideoItemBean hotVideoItemBean = mHotVideoAdapter.getData(position);
                CommonVideoBean commonVideoBean = new CommonVideoBean();
                commonVideoBean.setId(hotVideoItemBean.getId());
                commonVideoBean.setVideoType(hotVideoItemBean.getVideoType());
                commonVideoBean.setTitle(hotVideoItemBean.getTitle());
                commonVideoBean.setUrl(hotVideoItemBean.getUrl());
                QQVideoDetailActivity.start(getActivity(), commonVideoBean);
            }
        });
    }

    @Override
    protected void updateView() {
        mHotVideoAdapter.refreshData((List<HotVideoItemBean>) getArguments().getSerializable(VideoUtil.DATA));
    }

    @Override
    public void updateData(Object o) {

    }
}
