package com.snew.video.mvp.video;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.video.VideoTabBean;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.google.android.material.tabs.TabLayout;
import com.snew.video.R;
import com.snew.video.base.VideoBaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;


/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/30     18:00
 */
public class VideoFragment extends VideoBaseFragment {

    private WrappedViewPager display;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mViewPagerAdapter;


    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wcp_fragment_video_display);
        mTabLayout = (TabLayout) findViewById(R.id.tb_fragment_video_display);
    }

    @Override
    protected void initData() {
        init();
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("短视频");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    private void init() {
        List<VideoTabBean> list = getComponent()
                .getRepositoryManager()
                .getDaoSession().getVideoTabBeanDao()
                .queryBuilder().list();

        List<String> titleList = new ArrayList<>(list.size());
        List<Fragment> baseFragments = new ArrayList<>();
        for (VideoTabBean item :
                list) {
            titleList.add(item.getName());
            baseFragments.add(VideoListFragment.newInstance(item.getCategory()));
        }
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPagerAdapter.setTitleAndFragments(titleList, baseFragments);
        mTabLayout.setupWithViewPager(display);
        display.setAdapter(mViewPagerAdapter);
    }

    @Override
    protected void updateView() {
        display.setCurrentItem(0);
    }

    @Override
    public void updateData(Object o) {

    }
}
