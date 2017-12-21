package com.example.news;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.news.bean.CenterBean;
import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.mvp.systeminfo.SystemInfoLoginActivity;

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
                    LibraryLoginActivity.start(getContext(), null);
                } else if (position == 1) {
                    CardLoginActivity.start(getContext(), null);
                } else if (position == 2) {
                    SystemInfoLoginActivity.start(getActivity());
                } else if (position == 3) {
//                    VoiceCenterActivity.start(getActivity());
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
        CenterBean system = new CenterBean();
        system.setTitle("系统");
        system.setResId(R.mipmap.ic_launcher);
        result.add(system);
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
