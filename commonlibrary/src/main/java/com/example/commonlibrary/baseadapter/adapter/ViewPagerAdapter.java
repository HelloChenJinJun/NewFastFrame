package com.example.commonlibrary.baseadapter.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null) {
            return titleList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        if (fragments != null) {
            return fragments.size();
        } else {
            return 0;
        }
    }

    public void setTitleAndFragments(List<String> titleList, List<Fragment> fragments) {
        this.titleList = titleList;
        this.fragments = fragments;
    }

}