package com.example.news;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.bean.CenterBean;
import com.example.news.mvp.systemcenter.SystemCenterActivity;

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
        return true;
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
        display.addItemDecoration(new GridSpaceDecoration(3, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        display.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    ARouter.getInstance()
                            .build("/music/main").navigation();
                } else if (position == 1) {
                    ARouter.getInstance()
                            .build("/live/main").navigation();
                } else if (position == 2) {
                    ToastUtils.showShortToast("暂时不开放");
//                        SystemCenterActivity.start(getActivity());
                }
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("应用中心");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    @Override
    protected void updateView() {
        centerAdapter.addData(getDefaultData());
    }

    private List<CenterBean> getDefaultData() {
        List<CenterBean> result = new ArrayList<>();
        CenterBean library = new CenterBean();
        library.setTitle("听一听");
        library.setResId(R.drawable.ic_demo_one);
        result.add(library);
        CenterBean card = new CenterBean();
        card.setTitle("看一看");
        card.setResId(R.drawable.ic_demo_two);
        result.add(card);
        CenterBean system = new CenterBean();
        system.setTitle("系统");
        system.setResId(R.drawable.ic_demo_three);
        result.add(system);
        return result;
    }


    public static CenterFragment newInstance() {
        return new CenterFragment();
    }
}
