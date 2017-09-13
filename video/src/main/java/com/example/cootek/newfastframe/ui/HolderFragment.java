package com.example.cootek.newfastframe.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.BaseFragment;
import com.example.cootek.newfastframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class HolderFragment extends BaseFragment {


    private TabLayout tab;
    private ViewPager display;

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
        return R.layout.fragment_holder;
    }

    @Override
    protected void initView() {
        display = (ViewPager) findViewById(R.id.vp_fragment_holder_display);
        tab = (TabLayout) findViewById(R.id.tb_fragment_holder_tab);
    }

    @Override
    protected void initData() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        List<String> titleList = new ArrayList<>();
        titleList.add("首页");
        titleList.add("排行榜");
        titleList.add("测试");
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(LocalListFragment.newInstance());
        fragments.add(RankFragment.newInstance());
        fragments.add(MainFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(titleList, fragments);
        tab.setupWithViewPager(display);
        display.setOffscreenPageLimit(2);
        display.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void updateView() {
        display.setCurrentItem(0);
    }

    public static HolderFragment newInstance() {
        return new HolderFragment();
    }
}
