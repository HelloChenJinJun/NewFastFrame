package com.example.commonlibrary.baseadapter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.example.commonlibrary.BaseFragment;

import java.lang.reflect.Method;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<BaseFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setTitleAndFragments(List<String> titleList, List<BaseFragment> fragments) {
        this.titleList = titleList;
        this.fragments = fragments;
    }

}