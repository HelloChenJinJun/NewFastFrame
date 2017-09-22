package com.example.news;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.news.bean.CenterBean;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.util.NewsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      14:58
 * QQ:             1981367757
 */

public class CenterFragment extends BaseFragment {

    private SuperRecyclerView display;
    private CenterAdapter centerAdapter;

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
        return R.layout.fragment_center;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_center_display);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new GridLayoutManager(getContext(), 3));
        centerAdapter = new CenterAdapter();
        display.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) == null) {
                        LibraryLoginActivity.start(getContext(), null);
                    } else {
                        LibraryInfoActivity.start(getContext());
                    }
                } else if (position == 1) {
                    if (BaseApplication.getAppComponent().getSharedPreferences()
                            .getString(NewsUtil.CARD_POST_LOGIN_COOKIE, null) == null) {
                        CardLoginActivity.start(getContext(), null);
                    } else {
                        CardInfoActivity.start(getContext());
                    }
                }
            }
        });
    }

    @Override
    protected void updateView() {
        centerAdapter.addData(getDefaultData());
    }

    private List<CenterBean> getDefaultData() {
        List<CenterBean> result = new ArrayList<>();
        CenterBean library = new CenterBean();
        library.setTitle("图书馆系统");
        library.setResId(R.mipmap.ic_launcher);
        result.add(library);
        CenterBean card = new CenterBean();
        card.setTitle("一卡通系统");
        card.setResId(R.mipmap.ic_launcher);
        result.add(card);
        for (int i = 0; i < 5; i++) {
            CenterBean centerBean = new CenterBean();
            centerBean.setTitle("标题" + i);
            centerBean.setResId(R.mipmap.ic_launcher);
            result.add(centerBean);
        }
        return result;
    }


    public static CenterFragment newInstance() {
        return new CenterFragment();
    }
}
