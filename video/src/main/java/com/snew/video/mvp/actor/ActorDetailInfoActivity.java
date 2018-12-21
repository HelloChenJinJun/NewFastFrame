package com.snew.video.mvp.actor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.snew.video.R;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.ActorDetailInfoBean;
import com.snew.video.dagger.actor.ActorDetailInfoModule;
import com.snew.video.dagger.actor.DaggerActorDetailInfoComponent;
import com.snew.video.mvp.actor.list.ActorVideoListFragment;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     15:15
 */
public class ActorDetailInfoActivity extends VideoBaseActivity<BaseBean, ActorDetailInfoPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {


    private CustomSwipeRefreshLayout refresh;


    private ImageView avatar;
    private RelativeLayout imageBg;
    private String data;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private WrappedViewPager display;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;

    private FloatingActionButton mFloatingActionButton;

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
        display = findViewById(R.id.wvp_activity_actor_detail_info_display);
        mTabLayout = findViewById(R.id.tb_activity_actor_detail_info_tab);
        refresh = findViewById(R.id.refresh_activity_actor_detail_info_refresh);
        avatar = findViewById(R.id.riv_activity_actor_detail_info_avatar);
        imageBg = findViewById(R.id.rl_activity_actor_detail_info_image_bg);
        mToolbar = findViewById(R.id.tb_activity_actor_detail_info_title);
        mFloatingActionButton = findViewById(R.id.fab_activity_actor_detail_info_button);
        findViewById(R.id.iv_activity_actor_detail_info_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refresh.setOnRefreshListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("svsc");
        mAppBarLayout = findViewById(R.id.al_activity_actor_detail_info_bar);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                refresh.setEnabled(true);
            } else {
                refresh.setEnabled(false);
            }
        });
        mToolbar.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mToolbar.getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mToolbar.getLayoutParams().height += StatusBarUtil.getStatusBarHeight(ActorDetailInfoActivity.this);
                mToolbar.requestLayout();
            }
        });
    }

    @Override
    protected void initData() {
        DaggerActorDetailInfoComponent.builder().actorDetailInfoModule(new ActorDetailInfoModule(this))
                .videoComponent(getComponent()).build().inject(this);
        data = getIntent().getStringExtra(VideoUtil.DATA);
        mTabLayout.setupWithViewPager(display);
        runOnUiThread(() -> onRefresh());
    }


    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getCode() == 200) {
            if (baseBean.getType() == VideoUtil.BASE_TYPE_ACTOR_VIDEO_DATA) {
                ActorDetailInfoBean actorDetailInfoBean = (ActorDetailInfoBean) baseBean.getData();
                Glide.with(this).asBitmap().load(actorDetailInfoBean.getAvatar()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageBg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, ActorDetailInfoActivity.this, 20));
                    }
                });
                Glide.with(this).load(actorDetailInfoBean.getAvatar()).into(avatar);
                getSupportActionBar().setTitle(actorDetailInfoBean.getName());

                if (actorDetailInfoBean.getActorVideoWrappedDetailBeanList() != null && actorDetailInfoBean.getActorVideoWrappedDetailBeanList().size() > 0) {
                    if (mViewPagerAdapter == null) {
                        List<String> titleList = new ArrayList<>();
                        List<Fragment> listFragments = new ArrayList<>();
                        for (ActorDetailInfoBean.ActorVideoWrappedDetailBean item :
                                actorDetailInfoBean.getActorVideoWrappedDetailBeanList()) {
                            if (item.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_CAMERA) {
                                titleList.add("电影");
                            } else if (item.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_TV) {
                                titleList.add("连续剧");
                            } else if (item.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_VARIETY) {
                                titleList.add("综艺");
                            } else {
                                titleList.add("音乐");
                            }
                            listFragments.add(ActorVideoListFragment.newInstance(item));
                        }
                        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                        mViewPagerAdapter.setTitleAndFragments(titleList, listFragments);
                        display.setAdapter(mViewPagerAdapter);
                        display.setCurrentItem(0);
                    } else {

                    }
                }
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
