package com.example.news;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.LoginEvent;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.adapter.PopWindowAdapter;
import com.example.news.event.TypeNewsEvent;
import com.example.news.mvp.news.NewsListFragment;
import com.example.news.mvp.news.college.CollegeNewsMainFragment;
import com.example.news.mvp.news.othernew.OtherNewsListFragment;
import com.example.news.mvp.news.othernew.photolist.PhotoListFragment;
import com.example.news.util.NewsUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      17:32
 * QQ:             1981367757
 */

public class IndexFragment extends BaseFragment implements View.OnClickListener {
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager display;
    private ImageView expend;
    private List<String> titleList;
    private List<BaseFragment> fragmentList;
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
    protected void initView() {
        display = (ViewPager) findViewById(R.id.vp_fragment_index_display);
        tabLayout = (TabLayout) findViewById(R.id.tl_fragment_index_tab);
        expend = (ImageView) findViewById(R.id.iv_fragment_index_expend_list);
        expend.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        final List<OtherNewsTypeBean> result = NewsApplication
                .getNewsComponent().getRepositoryManager()
                .getDaoSession()
                .getOtherNewsTypeBeanDao()
                .queryBuilder().where(OtherNewsTypeBeanDao.Properties.HasSelected.eq(Boolean.TRUE))
                .build().list();
        initFragment(result);
        viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
//        前面不需要刷新
        viewPagerAdapter.setShouldRefresh(true);
        display.setOffscreenPageLimit(1);
        display.setCurrentItem(0);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setBgColor(getResources().getColor(R.color.base_color_text_grey));
        toolBarOption.setTitle("地大新闻");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
        addDisposable(RxBusManager.getInstance().registerEvent(TypeNewsEvent.class, new Consumer<TypeNewsEvent>() {
            @Override
            public void accept(@NonNull TypeNewsEvent typeNewsEvent) throws Exception {
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
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                ToastUtils.showShortToast("接受模块调整信息失败" + throwable.getMessage());
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(LoginEvent.class, new Consumer<LoginEvent>() {
            @Override
            public void accept(LoginEvent loginEvent) throws Exception {
                UserInfoEvent userInfoEvent = loginEvent.getUserInfoEvent();
                if (userInfoEvent == null) {
                    return;
                }
                String type = NewsUtil.getTypeFromName(userInfoEvent.getCollege());
                OtherNewsTypeBean newsTypeBean=null;
                if (type != null) {
                    newsTypeBean = NewsApplication.getNewsComponent()
                            .getRepositoryManager().getDaoSession().getOtherNewsTypeBeanDao()
                            .queryBuilder().where(OtherNewsTypeBeanDao.Properties.TypeId.eq(type))
                            .build().list().get(0);
                }
                if (newsTypeBean == null) {
                    return;
                }
                TypeNewsEvent event;
                if (loginEvent.isSuccess()) {
                    newsTypeBean.setHasSelected(true);
                    event = new TypeNewsEvent(TypeNewsEvent.ADD);
                } else {
                    newsTypeBean.setHasSelected(false);
                    event = new TypeNewsEvent(TypeNewsEvent.DELETE);
                }
                NewsApplication
                        .getNewsComponent().getRepositoryManager()
                        .getDaoSession().getOtherNewsTypeBeanDao()
                        .update(newsTypeBean);
                event.setTypeId(newsTypeBean.getTypeId());
                RxBusManager.getInstance().post(event);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showShortToast("接受注册信息失败" + throwable.getMessage());
            }
        }));
    }

    private void initFragment(List<OtherNewsTypeBean> list) {
        for (OtherNewsTypeBean bean :
                list) {
            titleList.add(bean.getName());
            if (bean.getTypeId().startsWith("TYPE")) {
                fragmentList.add(CollegeNewsMainFragment.newInstance(bean.getTypeId()));
            } else if (TextUtils.isEmpty(bean.getTypeId())) {
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
                customPopWindow = new CustomPopWindow.Builder().parentView(v).activity(getActivity()).contentView(getContentView())
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
                customPopWindow.showAsDropDown(v);
            } else {
                customPopWindow.dismiss();
            }
        } else if (id == R.id.btn_view_fragment_index_pop_adjust) {
            AdjustNewsTypeActivity.start(getActivity());
            customPopWindow.dismiss();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }


    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_index_pop_window, null);
        Button adjust = contentView.findViewById(R.id.btn_view_fragment_index_pop_adjust);
        final SuperRecyclerView display = contentView.findViewById(R.id.srcv_view_fragment_index_pop_display);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 5));
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
