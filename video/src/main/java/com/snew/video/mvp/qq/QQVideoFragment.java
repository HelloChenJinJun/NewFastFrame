package com.snew.video.mvp.qq;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.google.android.material.tabs.TabLayout;
import com.snew.video.R;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.QQVideoTabBean;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     15:58
 */
public class QQVideoFragment extends VideoBaseFragment {

    private WrappedViewPager display;
    private TabLayout mTabLayout;


    public static QQVideoFragment newInstance() {
        return new QQVideoFragment();
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
        return R.layout.fragment_qq_video;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wcp_fragment_qq_video_display);
        mTabLayout = (TabLayout) findViewById(R.id.tb_fragment_qq_video_display);
    }

    @Override
    protected void initData() {
        init();
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("腾讯视频");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    private void init() {
        List<QQVideoTabBean> tabBeanList;
        tabBeanList = getDefaultData();
        List<Fragment> baseFragments = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (QQVideoTabBean item :
                tabBeanList) {
            titleList.add(item.getTitle());
            baseFragments.add(QQVideoListFragment.newInstance(item.getType()));
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, baseFragments);
        mTabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
    }

    private List<QQVideoTabBean> getDefaultData() {
        List<QQVideoTabBean> list = new ArrayList<>();
        QQVideoTabBean one = new QQVideoTabBean();
        one.setTitle("最新");
        one.setType(19);
        QQVideoTabBean two = new QQVideoTabBean();
        two.setType(17);
        two.setTitle("最热");
        list.add(one);
        list.add(two);
        return list;
    }

    @Override
    protected void updateView() {
        display.setCurrentItem(0);
    }

    @Override
    public void updateData(Object o) {

    }
}
