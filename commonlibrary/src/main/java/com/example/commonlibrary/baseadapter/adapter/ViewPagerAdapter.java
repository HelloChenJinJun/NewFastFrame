package com.example.commonlibrary.baseadapter.adapter;

import com.example.commonlibrary.BaseFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
        if (fragments != null) {
            return fragments.size();
        } else {
            return 0;
        }
    }

    public void setTitleAndFragments(List<String> titleList, List<BaseFragment> fragments) {
        this.titleList = titleList;
        this.fragments = fragments;
    }

}