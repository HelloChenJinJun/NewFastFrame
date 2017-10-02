package com.example.news.mvp.news.college;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.news.R;
import com.example.news.mvp.news.NewsListFragment;
import com.example.news.util.NewsUtil;
import com.example.news.widget.WrappedViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/1      19:45
 * QQ:             1981367757
 */

public class CollegeNewsMainFragment extends BaseFragment {
    private WrappedViewPager display;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void updateData(Object o) {

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
    protected int getContentLayout() {
        return R.layout.fragment_college_news_main;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wvp_fragment_college_news_main_display);
        tabLayout = (TabLayout) findViewById(R.id.tl_fragment_college_news_main_tab);
    }

    @Override
    protected void initData() {
        List<String> titleList=new ArrayList<>();
        titleList.add("要闻");
        titleList.add("公告");
        titleList.add("学术");
        List<BaseFragment>  fragmentList=new ArrayList<>();
           String type=getArguments().getString(NewsUtil.COLLEGE_TYPE);
        if (type!=null) {
            switch (type){
                case NewsUtil.COLLEGE_TYPE_JG:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JG_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JG_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JG_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_GG:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GG_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GG_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GG_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_JSJ:
                    titleList.remove(2);
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JSJ_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JSJ_NOTICE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_DK:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DK_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DK_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DK_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_WY:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.WY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.WY_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.WY_SCIENCE_URL));
                    break;
            }
        }
        tabLayout.setupWithViewPager(display);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList,fragmentList);
    }

    @Override
    protected void updateView() {
        display.setAdapter(viewPagerAdapter);
        display.setCurrentItem(0);
    }

    public static CollegeNewsMainFragment newInstance(String type) {
        CollegeNewsMainFragment collegeNewsMainFragment=new CollegeNewsMainFragment();
        Bundle bundle=new Bundle();
        bundle.putString(NewsUtil.COLLEGE_TYPE,type);
        collegeNewsMainFragment.setArguments(bundle);
        return collegeNewsMainFragment;
    }
}
