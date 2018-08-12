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
    protected boolean needStatusPadding() {
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
                case NewsUtil.COLLEGE_TYPE_VOICE:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_VOICE_INDEX));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_VOICE_NOTIFY));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_VOICE_IMAGE));
                    titleList.set(2,"图片");
                    break;
                case NewsUtil.COLLEGE_TYPE_DD:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_NEWS));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_NOTIFY));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CUG_TECHNOLOGY));
                    break;
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
                case NewsUtil.COLLEGE_TYPE_DY:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DY_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DY_PUBLIC_URL));
                    titleList.set(2,"公示");
                    break;
                case NewsUtil.COLLEGE_TYPE_XY:
                    titleList.clear();
                    titleList.add("要闻");
                    titleList.add("科研");
                    titleList.add("教学");
                    titleList.add("研究生");
                    titleList.add("就业");
                    titleList.add("动态");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_TECH_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_GRADUATE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_JOB_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.XY_STUDENT_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_ZDH:
                    titleList.add("本科生");
                    titleList.add("研究生");
                    titleList.add("就业");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_STUDENT_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_GRADUATE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZDH_JOB_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_ZY:
                    titleList.remove(1);
                    titleList.add("学生动态");
                    titleList.add("本科生");
                    titleList.add("研究生");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZY_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZY_STUDENT_NEWS_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZY_STUDENT_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.ZY_GRADUATE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_CH:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CH_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CH_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.CH_SCIENCE_URL));
                break;
                case NewsUtil.COLLEGE_TYPE_GC:
                    titleList.set(2,"党建");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GC_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GC_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.GC_WORK_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_HJ:
                    titleList.remove(1);
                    titleList.add("本科生");
                    titleList.add("研究生");
                    titleList.add("学生动态");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HJ_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HJ_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HJ_STUDENT_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HJ_GRADUATE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HJ_STUDENT_WORK_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_DWK:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DWK_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DWK_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.DWK_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_JD:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JD_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JD_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.JD_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_HY:
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HY_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.HY_SCIENCE_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_SL:
                    titleList.add("教学");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.SL_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.SL_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.SL_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.SL_TECH_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_YM:
                    titleList.set(2,"学术");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.YM_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.YM_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.YM_STUDENT_WORK_URL));
                    break;
                case NewsUtil.COLLEGE_TYPE_MY:
                    titleList.add("本科生");
                    titleList.add("研究生");
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.MY_INDEX_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.MY_NOTICE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.MY_SCIENCE_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.MY_STUDENT_URL));
                    fragmentList.add(NewsListFragment.newInstance(NewsUtil.MY_GRADUATE_URL));
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
