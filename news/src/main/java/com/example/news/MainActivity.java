package com.example.news;


import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.BaseActivity;
import com.example.news.mvp.searchlibrary.LibraryFragment;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/news/main")
public class MainActivity extends BaseActivity {

    private RadioGroup bottomContainer;
    private List<Fragment> fragmentList;


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
        return R.layout.activity_main_news;
    }

    @Override
    protected void initView() {
       bottomContainer= (RadioGroup) findViewById(R.id.rg_activity_main_bottom_container);
        bottomContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_activity_main_bottom_index) {
                        addOrReplaceFragment(fragmentList.get(0));
                } else if (checkedId == R.id.rb_activity_main_bottom_library) {
                    addOrReplaceFragment(fragmentList.get(1));
                } else if (checkedId == R.id.rb_activity_main_bottom_center) {
                    addOrReplaceFragment(fragmentList.get(2));
                }
            }
        });
    }

    @Override
    protected void initData() {
        fragmentList=new ArrayList<>();
        fragmentList.add(IndexFragment.newInstance());
        fragmentList.add(LibraryFragment.newInstance());
        fragmentList.add(CenterFragment.newInstance());
       addOrReplaceFragment(fragmentList.get(0),R.id.fl_activity_main_container);
    }
}
