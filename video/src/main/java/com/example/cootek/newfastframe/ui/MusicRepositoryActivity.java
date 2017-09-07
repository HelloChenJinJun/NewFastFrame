package com.example.cootek.newfastframe.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.commonlibrary.baseadapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.mvp.MainBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class MusicRepositoryActivity extends MainBaseActivity {
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
        return R.layout.activity_music_repository;
    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tl_activity_music_repository_tab);
        display = (ViewPager) findViewById(R.id.vp_activity_music_repository_display);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("音乐世界" +
                "");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("排行");
//        list.add("歌手");
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(RecommendFragment.newInstance());
        fragmentList.add(RankFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(list, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
        display.setOffscreenPageLimit(1);
        display.setCurrentItem(0);
    }
}
