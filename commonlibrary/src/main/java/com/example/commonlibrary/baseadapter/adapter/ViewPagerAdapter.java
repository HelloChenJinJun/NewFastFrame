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

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> titleList;
    private List<BaseFragment> fragments;
    private FragmentManager manager;
    private boolean shouldRefresh=false;

    public boolean isShouldRefresh() {
        return false;
    }

    public void setShouldRefresh(boolean shouldRefresh) {
        this.shouldRefresh = shouldRefresh;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.manager=fm;
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



    private void removeFragment(ViewGroup container,int index) {
        String tag = getFragmentTag(container.getId(), index);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null)
            return;
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }
    private String getFragmentTag(int viewId, int index) {
        try {
            Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
            Class<?>[] parameterTypes = { int.class, long.class };
            Method method = cls.getDeclaredMethod("makeFragmentName",
                    parameterTypes);
            method.setAccessible(true);
            String tag = (String) method.invoke(this, viewId, index);
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (isShouldRefresh()) {
            removeFragment(container,position);
        }
        return super.instantiateItem(container, position);
    }

    public void setTitleAndFragments(List<String> titleList, List<BaseFragment> fragments) {
        this.titleList = titleList;
        this.fragments = fragments;
    }




    @Override
    public int getItemPosition(Object object) {
//        触发销毁对象以及重建对象
        if (isShouldRefresh()) {
            return PagerAdapter.POSITION_NONE;
        }else {
            return super.getItemPosition(object);
        }
    }
}