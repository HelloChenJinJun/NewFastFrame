package com.example.cootek.newfastframe.mvp.singer;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.StatusBarUtil;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.ArtistInfo;
import com.example.cootek.newfastframe.event.DragEvent;
import com.example.cootek.newfastframe.mvp.search.AlbumListFragment;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;
import androidx.viewpager.widget.ViewPager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     15:04
 */
public class SingerDetailFragment extends MusicBaseFragment<BaseBean, SingerDetailPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {

    private CustomSwipeRefreshLayout refresh;
    private FloatingActionButton mFloatingActionButton;
    private ImageView avatar;
    private RelativeLayout imageBg;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private WrappedViewPager display;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private TextView name;

    private ArtistInfo mArtistInfo;

    public static SingerDetailFragment newInstance(ArtistInfo data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, data);
        SingerDetailFragment singerDetailFragment = new SingerDetailFragment();
        singerDetailFragment.setArguments(bundle);
        return singerDetailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(
                    TransitionInflater.from(getContext())
                            .inflateTransition(android.R.transition.move));
        }
        super.onCreate(savedInstanceState);

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
        return R.layout.fragment_singer_detail;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.wvp_fragment_singer_detail_display);
        name = findViewById(R.id.tv_fragment_singer_detail_name);
        mTabLayout = findViewById(R.id.tb_fragment_singer_detail_tab);
        refresh = findViewById(R.id.refresh_fragment_singer_detail_refresh);
        avatar = findViewById(R.id.riv_fragment_singer_detail_avatar);
        imageBg = findViewById(R.id.rl_fragment_singer_detail_image_bg);
        mToolbar = findViewById(R.id.tb_fragment_singer_detail_title);
        mFloatingActionButton = findViewById(R.id.fab_fragment_singer_detail_button);
        findViewById(R.id.iv_fragment_singer_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        refresh.setOnRefreshListener(this);
        mAppBarLayout = findViewById(R.id.al_fragment_singer_detail_bar);
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
                mToolbar.getLayoutParams().height += StatusBarUtil.getStatusBarHeight(getContext());
                mToolbar.requestLayout();
            }
        });
        ViewCompat.setTransitionName(name, "name");
        ViewCompat.setTransitionName(avatar, "avatar");
    }

    @Override
    protected void initData() {
        mArtistInfo = (ArtistInfo) getArguments().getSerializable(Constant.DATA);
        name.setText(mArtistInfo.getName());
        Glide.with(this).load(mArtistInfo.getAvatar_s500()).into(avatar);
        Glide.with(this).asBitmap().load(mArtistInfo.getAvatar_s1000()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageBg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, getContext(), 20));
            }
        });

        List<String> titleList = new ArrayList<>();
        List<Fragment> listFragments = new ArrayList<>();
        titleList.add("单曲");
        titleList.add("专辑");
        listFragments.add(SongListFragment.newInstance(MusicUtil.FROM_SINGER, mArtistInfo.getTing_uid()));
        listFragments.add(AlbumListFragment.newInstance(mArtistInfo.getTing_uid()));
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPagerAdapter.setTitleAndFragments(titleList, listFragments);
        mTabLayout.setupWithViewPager(display);
        display.setAdapter(mViewPagerAdapter);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    RxBusManager.getInstance().post(new DragEvent(false));
                } else {
                    RxBusManager.getInstance().post(new DragEvent(true));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        display.setCurrentItem(0);
        RxBusManager.getInstance().post(new DragEvent(false));
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void updateData(BaseBean baseBean) {

    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
    }
}
