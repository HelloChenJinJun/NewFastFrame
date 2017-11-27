package com.example.cootek.newfastframe.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.fragment.RankFragment;
import com.example.cootek.newfastframe.ui.fragment.RecommendFragment;
import com.example.cootek.newfastframe.ui.fragment.SingerListFragment;
import com.example.cootek.newfastframe.ui.fragment.SlideMusicBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class MusicRepositoryActivity extends SlideMusicBaseActivity {
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
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("乐库");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("排行");
        list.add("歌手");
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(RecommendFragment.newInstance());
        fragmentList.add(RankFragment.newInstance());
        fragmentList.add(SingerListFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(list, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    iSlider.unlock();
                }else {
                    iSlider.lock();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        display.setAdapter(viewPagerAdapter);
        display.setOffscreenPageLimit(1);
        display.setCurrentItem(0);

    }



    public static void start(Activity activity){
        Intent intent=new Intent(activity,MusicRepositoryActivity.class);
        activity.startActivity(intent);
    }


}
