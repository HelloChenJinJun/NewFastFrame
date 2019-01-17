package com.snew.video;

import android.view.View;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.ViewPagerIndicator;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.mvp.preview.PreViewVideoFragment;
import com.snew.video.mvp.qq.QQVideoFragment;
import com.snew.video.mvp.search.SearchVideoActivity;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     11:12
 */
public class VideoActivity extends VideoBaseActivity implements View.OnClickListener {
    private WrappedViewPager display;
    private ViewPagerIndicator viewPagerIndicator;


    @Override
    protected boolean isNeedHeadLayout() {
        return true;
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
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.wvp_activity_video_display);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setCustomView(getCustomView());
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<Fragment> list = new ArrayList<>();
        list.add(QQVideoFragment.newInstance(VideoUtil.VIDEO_URL_TYPE_QQ));
        list.add(PreViewVideoFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(Arrays.asList("腾讯", "最新"), list);
        display.setAdapter(viewPagerAdapter);
        viewPagerIndicator.setViewPager(display, 0);
    }


    @Override
    protected boolean needSlide() {
        return false;
    }

    private View getCustomView() {
        View view = getLayoutInflater().inflate(R.layout.view_activity_video_header, null, false);
        viewPagerIndicator = view.findViewById(R.id.vpi_view_activity_video_header_view_indicator);
        view.findViewById(R.id.iv_view_activity_video_header_view_search).setOnClickListener(this);
        return view;
    }

    @Override
    public void updateData(Object o) {

    }


    private long mExitTime;

    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            super.onBackPressed();
        } else if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showShortToast("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_view_activity_video_header_view_search) {
            SearchVideoActivity.start(this);
        }
    }


}
