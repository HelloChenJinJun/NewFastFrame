package com.snew.video.mvp.actor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.nineoldandroids.view.ViewHelper;
import com.snew.video.R;
import com.snew.video.adapter.ActorVideoDetailAdapter;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.ActorDetailInfoBean;
import com.snew.video.bean.VideoBean;
import com.snew.video.dagger.actor.ActorDetailInfoModule;
import com.snew.video.dagger.actor.DaggerActorDetailInfoComponent;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.util.VideoUtil;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     15:15
 */
public class ActorDetailInfoActivity extends VideoBaseActivity<BaseBean, ActorDetailInfoPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {


    private SuperRecyclerView display;
    private CustomSwipeRefreshLayout refresh;


    @Inject
    ActorVideoDetailAdapter mQQVideoListAdapter;
    private ImageView avatar;
    private TextView name;
    private View headerView;
    private String data;


    private RelativeLayout headerContainer;

    public static void start(Activity activity, String data) {
        Intent intent = new Intent(activity, ActorDetailInfoActivity.class);
        intent.putExtra(VideoUtil.DATA, data);
        activity.startActivity(intent);
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
        return R.layout.activity_actor_detail_info;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.srcv_activity_actor_detail_info_display);
        refresh = findViewById(R.id.refresh_activity_actor_detail_info_refresh);
        headerContainer = findViewById(R.id.rl_activity_actor_detail_info_header);
        findViewById(R.id.iv_activity_actor_detail_info_back).setOnClickListener(v -> finish());
        refresh.setOnRefreshListener(this);
        headerContainer.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerContainer.getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                headerContainer.getLayoutParams().height += StatusBarUtil.getStatusBarHeight(ActorDetailInfoActivity.this);
                headerContainer.requestLayout();
            }
        });
        display.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                if (position == 0) {
                    View view = recyclerView.getChildAt(0);
                    float percent = view.getTop() / (-view.getHeight() * 1.0f);
                    ViewHelper.setAlpha(headerContainer, percent);
                } else if (headerContainer.getAlpha() != 1.0f) {
                    ViewHelper.setAlpha(headerContainer, 1.0f);
                }
            }

        });
    }

    @Override
    protected void initData() {
        DaggerActorDetailInfoComponent.builder().actorDetailInfoModule(new ActorDetailInfoModule(this))
                .videoComponent(getComponent()).build().inject(this);
        data = getIntent().getStringExtra(VideoUtil.DATA);
        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
        display.addHeaderView(getHeaderView());
        display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(3), DensityUtil.toDp(15), true));
        display.setAdapter(mQQVideoListAdapter);
        mQQVideoListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ActorDetailInfoBean.ActorVideoDetailBean detailBean = mQQVideoListAdapter.getData(position);
                VideoBean videoBean = new VideoBean(detailBean.getTitle(), detailBean.getUrl());
                QQVideoDetailActivity.start(ActorDetailInfoActivity.this, videoBean, VideoUtil.getIdFromUrl(detailBean.getUrl()));
            }
        });
        runOnUiThread(this::onRefresh);
    }

    private View getHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.view_activity_detail_info_header, display.getHeaderContainer(), false);
        name = headerView.findViewById(R.id.tv_view_activity_detail_info_header_name);
        avatar = headerView.findViewById(R.id.iv_view_activity_detail_info_header_avatar);
        return headerView;
    }

    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getCode() == 200) {
            if (baseBean.getType() == VideoUtil.BASE_TYPE_ACTOR_VIDEO_DATA) {
                ActorDetailInfoBean actorDetailInfoBean = (ActorDetailInfoBean) baseBean.getData();
                mQQVideoListAdapter.refreshData(actorDetailInfoBean.getActorVideoDetailBeans());
                Glide.with(this).asBitmap().load(actorDetailInfoBean.getAvatar()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        headerView.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, ActorDetailInfoActivity.this, 20));
                    }
                });
                Glide.with(this).load(actorDetailInfoBean.getAvatar()).into(avatar);
                name.setText(actorDetailInfoBean.getName());
            }
        }

    }


    @Override
    public void showLoading(String loadMessage) {
        super.showLoading(loadMessage);
        refresh.setRefreshing(true);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        presenter.getData(true, data);
    }
}
