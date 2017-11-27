package com.example.cootek.newfastframe.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.ViewPagerIndicator;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.MainActivity;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class HolderFragment extends BaseFragment implements View.OnClickListener {


    private ViewPagerIndicator viewPagerIndicator;
    private WrappedViewPager display;
    private View expend;

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
        return R.layout.fragment_holder;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.vp_fragment_holder_display);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setCustomView(getToolBarView());
        setToolBar(toolBarOption);
        ((BaseActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));
    }

    private View getToolBarView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_holder_header_view, null, false);
        viewPagerIndicator = (ViewPagerIndicator) view.findViewById(R.id.vpi_view_fragment_holder_header_view_indicator);
        expend = view.findViewById(R.id.iv_view_Fragment_holder_header_view_expend);
        expend.setOnClickListener(this);
        view.findViewById(R.id.iv_view_Fragment_holder_header_view_search).setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        List<String> titleList = new ArrayList<>();
        titleList.add("听");
        titleList.add("看");
        titleList.add("唱");
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(ListenerFragment.newInstance());
        fragments.add(RankFragment.newInstance());
        fragments.add(MainFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(titleList, fragments);
        display.setOffscreenPageLimit(2);
        display.setAdapter(viewPagerAdapter);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((MainActivity) getActivity()).notifyIntercept(false);
                } else {
                    ((MainActivity) getActivity()).notifyIntercept(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerIndicator.setTabItemTitles(titleList);
        viewPagerIndicator.setViewPager(display, 0);
    }

    @Override
    protected void updateView() {
        display.setCurrentItem(0);
    }

    public static HolderFragment newInstance() {

        return new HolderFragment();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_view_activity_singer_info_header_search) {

        } else if (v.getId() == R.id.iv_view_Fragment_holder_header_view_expend) {
            ((MainActivity) getActivity()).switchMenu();
        }
    }

    public void onDrag(float delta) {
        ViewHelper.setAlpha(expend, (1-delta));
    }
}
