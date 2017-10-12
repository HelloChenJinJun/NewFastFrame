package com.example.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/27      10:45
 * QQ:             1981367757
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> mTitleList;

        public ViewPageAdapter(List<Fragment> fragmentList, List<String> titleList, FragmentManager fm) {
                super(fm);
                mFragmentList = fragmentList;
                mTitleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
                return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
                return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
        }
}
