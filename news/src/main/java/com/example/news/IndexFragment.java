package com.example.news;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.mvp.news.NewsListFragment;
import com.example.news.util.NewsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      17:32
 * QQ:             1981367757
 */

public class IndexFragment extends BaseFragment {
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager display;

    @Override
    public void updateData(Object o) {

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
        return R.layout.fragment_index;
    }

    @Override
    protected void initView() {
        display = (ViewPager) findViewById(R.id.vp_fragment_index_display);
        tabLayout = (TabLayout) findViewById(R.id.tl_fragment_index_tab);
    }

    @Override
    protected void initData() {
        List<String> titleList = new ArrayList<>();
        titleList.add("要闻");
        titleList.add("公告");
        titleList.add("学术");
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_NEWS));
        fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_NOTIFY));
        fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_TECHNOLOGY));
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
        display.setCurrentItem(0);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("地大新闻");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    @Override
    protected void updateView() {

    }

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }
}
