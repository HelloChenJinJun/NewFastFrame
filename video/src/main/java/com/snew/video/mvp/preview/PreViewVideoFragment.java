package com.snew.video.mvp.preview;

import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.snew.video.R;
import com.snew.video.adapter.QQVideoListAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.CommonVideoBean;
import com.snew.video.bean.QQVideoListBean;
import com.snew.video.dagger.preview.DaggerPreViewVideoComponent;
import com.snew.video.dagger.preview.PreViewVideoModule;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.util.VideoUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/9     16:13
 */
public class PreViewVideoFragment extends VideoBaseFragment<BaseBean, PreViewVideoPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {
    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;

    @Inject
    QQVideoListAdapter mVideoAdapter;

    public static PreViewVideoFragment newInstance() {
        return new PreViewVideoFragment();
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
        return R.layout.activity_preview_video;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_activity_preview_video_refresh);
        display = findViewById(R.id.srcv_activity_preview_video_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerPreViewVideoComponent.builder().preViewVideoModule(new PreViewVideoModule(this))
                .videoComponent(getComponent()).build().inject(this);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(3), DensityUtil.toDp(15), true));
        display.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CommonVideoBean commonVideoBean = new CommonVideoBean();
                QQVideoListBean.JsonvalueBean.ResultsBean videoBean = mVideoAdapter.getData(position);
                commonVideoBean.setUrl(videoBean.getId());
                commonVideoBean.setImage(videoBean.getFields().getVertical_pic_url());
                commonVideoBean.setVideoType(VideoUtil.VIDEO_TYPE_QQ_CAMERA);
                commonVideoBean.setTitle(videoBean.getFields().getTitle());
                QQVideoDetailActivity.start(getActivity(), commonVideoBean);
            }
        });
    }

    @Override
    protected void updateView() {
        presenter.getPreVideoData();
    }

    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getCode() == 200) {
            mVideoAdapter.refreshData((List<QQVideoListBean.JsonvalueBean.ResultsBean>) baseBean.getData());
        } else {
            ToastUtils.showShortToast(baseBean.getDesc());
            CommonLogger.e(baseBean.getDesc());
            refresh.setRefreshing(false);
        }
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getPreVideoData();
    }
}
