package com.example.video.mvp.index;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.bean.news.OtherNewsTypeBeanDao;
import com.example.commonlibrary.cusotomview.CustomPopWindow;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.video.NewsApplication;
import com.example.video.R;
import com.example.video.adapter.PopWindowAdapter;
import com.example.video.event.TypeNewsEvent;
import com.example.video.mvp.news.othernew.OtherNewsListFragment;
import com.example.video.mvp.news.othernew.photolist.PhotoListFragment;
import com.example.video.mvp.newsType.AdjustNewsTypeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      17:32
 * QQ:             1981367757
 */

public class IndexFragment extends BaseFragment implements View.OnClickListener {
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private WrappedViewPager display;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private PopWindowAdapter popWindowAdapter;


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
        return R.layout.fragment_index;
    }


    @Override
    protected boolean needStatusPadding() {
        return true;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.vp_fragment_index_display);
        tabLayout = (TabLayout) findViewById(R.id.tl_fragment_index_tab);
        ImageView expend = (ImageView) findViewById(R.id.iv_fragment_index_expend_list);
        expend.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initFragment();
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("前沿新闻");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
        addDisposable(RxBusManager.getInstance().registerEvent(TypeNewsEvent.class, typeNewsEvent -> {
            OtherNewsTypeBean newsTypeBean = NewsApplication.getNewsComponent()
                    .getRepositoryManager().getDaoSession().getOtherNewsTypeBeanDao()
                    .queryBuilder().where(OtherNewsTypeBeanDao.Properties.TypeId.eq(typeNewsEvent.getTypeId()))
                    .build().list().get(0);
            if (typeNewsEvent.getType() == TypeNewsEvent.ADD) {
                OtherNewsListFragment otherNewsListFragment = OtherNewsListFragment.newInstance(newsTypeBean);
                fragmentList.add(otherNewsListFragment);
                titleList.add(newsTypeBean.getName());
            } else {
                int index = titleList.indexOf(newsTypeBean.getName());
                if (index < 0) {
                    return;
                }
                fragmentList.remove(index);
                titleList.remove(newsTypeBean.getName());
                display.setCurrentItem(0);
            }
            viewPagerAdapter.notifyDataSetChanged();
        }));

    }

    private void initFragment() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        List<OtherNewsTypeBean> list = NewsApplication
                .getNewsComponent().getRepositoryManager()
                .getDaoSession()
                .getOtherNewsTypeBeanDao()
                .queryBuilder().where(OtherNewsTypeBeanDao.Properties.HasSelected.eq(Boolean.TRUE))
                .build().list();
        List<OtherNewsTypeBean> tempList = new ArrayList<>();
        List<OtherNewsTypeBean> otherList = new ArrayList<>();

        for (OtherNewsTypeBean bean :
                list) {
            if (bean.getName().equals("福利") || bean.getName().equals("头条")
                    ) {
                tempList.add(bean);
            } else {
                otherList.add(bean);
            }
        }
        otherList.addAll(0, tempList);
        for (OtherNewsTypeBean bean :
                otherList) {
            titleList.add(bean.getName());
            if (TextUtils.isEmpty(bean.getTypeId())) {
                fragmentList.add(PhotoListFragment.newInstance());
            } else {
                fragmentList.add(OtherNewsListFragment.newInstance(bean));
            }
        }
    }

    private CustomPopWindow customPopWindow;


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_fragment_index_expend_list) {
            if (customPopWindow == null) {
                customPopWindow = new CustomPopWindow.Builder().contentView(getContentView())
                        .build();
            }
            List<OtherNewsTypeBean> result = new ArrayList<>();
            for (String title :
                    titleList) {
                OtherNewsTypeBean item = new OtherNewsTypeBean();
                item.setName(title);
                result.add(item);
            }
            popWindowAdapter.refreshData(result);
            if (!customPopWindow.isShowing()) {
                customPopWindow.showAtLocation(display, Gravity.END, 0, 0);
            } else {
                customPopWindow.dismiss();
            }
        } else if (id == R.id.btn_view_fragment_index_pop_adjust) {
            AdjustNewsTypeActivity.start(getActivity());
            customPopWindow.dismiss();
        }
    }


    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_index_pop_window, null);
        Button adjust = contentView.findViewById(R.id.btn_view_fragment_index_pop_adjust);
        final SuperRecyclerView display = contentView.findViewById(R.id.srcv_view_fragment_index_pop_display);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 5));
        display.addItemDecoration(new GridSpaceDecoration(5, 0, DensityUtil.toDp(10), true));
        popWindowAdapter = new PopWindowAdapter();
        display.setAdapter(popWindowAdapter);
        popWindowAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                IndexFragment.this.display.setCurrentItem(position);
                customPopWindow.dismiss();
            }
        });
        adjust.setOnClickListener(this);
        return contentView;
    }


    @Override
    protected void updateView() {

    }

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }
}
