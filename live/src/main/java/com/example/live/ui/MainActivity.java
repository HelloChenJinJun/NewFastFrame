package com.example.live.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.CustomPopWindow;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.live.LiveApplication;
import com.example.live.R;
import com.example.live.adapter.PopWindowAdapter;
import com.example.live.bean.CategoryLiveBean;
import com.example.live.dagger.main.DaggerMainActivityComponent;
import com.example.live.dagger.main.MainActivityModules;
import com.example.live.mvp.main.MainPresenter;
import com.example.live.ui.fragment.ListLiveFragment;
import com.example.live.ui.fragment.RecommendLiveFragment;
import com.example.live.util.LiveUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
@Route(path = "/live/main")
public class MainActivity extends BaseActivity<List<CategoryLiveBean>, MainPresenter> implements View.OnClickListener {
    private ViewPager display;
    private TabLayout tab;
    private ImageView list;
    @Inject
    ViewPagerAdapter viewPagerAdapter;
    private List<CategoryLiveBean> data = new ArrayList<>();


    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_live;
    }

    @Override
    protected void initView() {
        display = (ViewPager) findViewById(R.id.vp_activity_main_display);
        tab = (TabLayout) findViewById(R.id.tl_activity_main_tab);
        list = (ImageView) findViewById(R.id.iv_activity_main_expend_list);
        list.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerMainActivityComponent.builder().mainActivityModules(new MainActivityModules(this))
                .mainComponent(LiveApplication.getMainComponent()).build().inject(this);
        display.post(new Runnable() {
            @Override
            public void run() {
                presenter.getAllCategories();
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("全民直播");
        toolBarOption.setBgColor(getResources().getColor(R.color.base_color_text_grey));
        setToolBar(toolBarOption);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.live_menu_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.live_menu_item_search) {
            ToastUtils.showLongToast("启动搜索界面");
            SearchLiveActivity.start(this);
        }
        return true;
    }

    @Override
    public void updateData(List<CategoryLiveBean> categoryLiveBeen) {
        data.clear();
        data.addAll(categoryLiveBeen);
        if (data != null && data.size() > 0) {
            List<String> titleList = new ArrayList<>();
            List<BaseFragment> fragmentList = new ArrayList<>();
            titleList.add("推荐");
            fragmentList.add(RecommendLiveFragment.newInstance());
            for (CategoryLiveBean bean :
                    data) {
                ListLiveFragment listLiveFragment = ListLiveFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString(LiveUtil.SLUG, bean.getSlug());
                listLiveFragment.setArguments(bundle);
                fragmentList.add(listLiveFragment);
                titleList.add(bean.getName());
            }
            tab.setupWithViewPager(display);
            viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
            display.setAdapter(viewPagerAdapter);
            display.setCurrentItem(0);
        }
    }


    @Override
    public void hideLoading() {

        if (data != null && data.size() > 0) {
            super.hideLoading();
        } else {
            showEmptyView();
        }
    }


    private CustomPopWindow customPopWindow;
    private PopWindowAdapter popWindowAdapter;


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_activity_main_expend_list) {
            if (customPopWindow == null) {
                customPopWindow = new CustomPopWindow.Builder().parentView(v).contentView(getContentView()).activity(this)
                        .build();
            }
            if (!customPopWindow.isShowing()) {
                customPopWindow.showAsDropDown(v);
            } else {
                customPopWindow.dismiss();
            }
        }
    }

    private View getContentView() {
        View contentView = getLayoutInflater().inflate(R.layout.view_activity_main_pop_window, null);
        SuperRecyclerView display = (SuperRecyclerView) contentView.findViewById(R.id.srcv_view_activity_main_pop_window_display);
        display.setLayoutManager(new WrappedGridLayoutManager(this, 5));
        popWindowAdapter = new PopWindowAdapter();
        display.setAdapter(popWindowAdapter);
        List<CategoryLiveBean> list = new ArrayList<>();
        CategoryLiveBean categoryLiveBean = new CategoryLiveBean();
        categoryLiveBean.setIcon_image("");
        categoryLiveBean.setName("推荐");
        list.add(categoryLiveBean);
        list.addAll(data);
        popWindowAdapter.addData(list);
        popWindowAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                customPopWindow.dismiss();
                MainActivity.this.display.setCurrentItem(position);
            }
        });
        return contentView;
    }
}
