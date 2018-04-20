package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.mvp.booklist.BookInfoListFragment;
import com.example.news.mvp.searchlibrary.LibraryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      16:50
 * QQ:             1981367757
 */
public class LibraryInfoActivity extends BaseActivity{
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
        return R.layout.activity_library_info;
    }

    @Override
    protected void initView() {
        display = (ViewPager) findViewById(R.id.vp_activity_library_info_display);
        tabLayout = (TabLayout) findViewById(R.id.tl_activity_library_tab);
    }

    @Override
    protected void initData() {
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        List<String> titleList=new ArrayList<>();
        List<BaseFragment>  fragmentList=new ArrayList<>();
        titleList.add("当前");
        titleList.add("历史");
        titleList.add("图书搜索");
        fragmentList.add(BookInfoListFragment.newInstance(titleList.get(0)));
        fragmentList.add(BookInfoListFragment.newInstance(titleList.get(1)));
        fragmentList.add(LibraryFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(titleList,fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
        display.setCurrentItem(0);
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("个人图书馆");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Context context) {
        Intent intent=new Intent(context,LibraryInfoActivity.class);
        context.startActivity(intent);
    }
}
