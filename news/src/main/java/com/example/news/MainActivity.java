package com.example.news;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager display;
    private TabLayout tabLayout;


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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        display= (ViewPager) findViewById(R.id.vp_activity_main_display);
        tabLayout= (TabLayout) findViewById(R.id.tl_activity_main_tab);
    }

    @Override
    protected void initData() {
        List<String> titleList=new ArrayList<>();
        titleList.add("地大");
        titleList.add("要闻");
        List<BaseFragment>  fragmentList=new ArrayList<>();
        fragmentList.add(NewsListFragment.newInstance());
        fragmentList.add(NewsListFragment.newInstance());
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList,fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
        display.setCurrentItem(0);
    }
}
