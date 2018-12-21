package com.snew.video.mvp.actor.list;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.utils.DensityUtil;
import com.snew.video.R;
import com.snew.video.adapter.ActorVideoDetailAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.ActorDetailInfoBean;
import com.snew.video.bean.CommonVideoBean;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.util.VideoUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/21     9:30
 */
public class ActorVideoListFragment extends VideoBaseFragment {

    private SuperRecyclerView display;


    private ActorVideoDetailAdapter mQQVideoListAdapter;


    private ActorDetailInfoBean.ActorVideoWrappedDetailBean mActorVideoWrappedDetailBean;

    public static ActorVideoListFragment newInstance(ActorDetailInfoBean.ActorVideoWrappedDetailBean data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoUtil.DATA, data);
        ActorVideoListFragment actorVideoListFragment = new ActorVideoListFragment();
        actorVideoListFragment.setArguments(bundle);
        return actorVideoListFragment;
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
        return R.layout.fragment_actor_video_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_actor_video_list_display);
    }

    @Override
    protected void initData() {
        mActorVideoWrappedDetailBean = (ActorDetailInfoBean.ActorVideoWrappedDetailBean) getArguments().getSerializable(VideoUtil.DATA);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(3), DensityUtil.toDp(15), true));
        mQQVideoListAdapter = new ActorVideoDetailAdapter();
        display.setAdapter(mQQVideoListAdapter);
        mQQVideoListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ActorDetailInfoBean.ActorVideoDetailBean detailBean = mQQVideoListAdapter.getData(position);
                CommonVideoBean commonVideoBean = new CommonVideoBean();
                commonVideoBean.setId(VideoUtil.getIdFromUrl(detailBean.getUrl()));
                commonVideoBean.setUrl(detailBean.getUrl());
                commonVideoBean.setVideoType(mActorVideoWrappedDetailBean.getVideoType());
                commonVideoBean.setTitle(detailBean.getTitle());
                QQVideoDetailActivity.start(getActivity(), commonVideoBean);
            }
        });

    }

    @Override
    protected void updateView() {
        mQQVideoListAdapter.refreshData(mActorVideoWrappedDetailBean.getActorVideoDetailBeanList());
    }


    @Override
    public void updateData(Object o) {

    }
}
