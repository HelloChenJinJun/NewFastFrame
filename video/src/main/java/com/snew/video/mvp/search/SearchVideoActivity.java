package com.snew.video.mvp.search;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.cusotomview.CustomEditText;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.SystemUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.snew.video.R;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.HotVideoBean;
import com.snew.video.bean.HotVideoItemBean;
import com.snew.video.bean.SearchVideoBean;
import com.snew.video.mvp.search.detail.SearchVideoDetailFragment;
import com.snew.video.mvp.search.hot.HotVideoListFragment;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     18:16
 */
public class SearchVideoActivity extends VideoBaseActivity<BaseBean, SearchVideoPresenter> implements View.OnClickListener {
    private SuperRecyclerView searchHistory;
    private CustomSwipeRefreshLayout refresh;
    private WrappedViewPager display;
    private TabLayout mTabLayout;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private ViewPagerAdapter viewPagerAdapter;
    private CustomEditText search;
    private SearchVideoDetailFragment searchFragment;


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
        return R.layout.activity_search_video;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_activity_search_video_refresh);
        search = findViewById(R.id.cet_activity_search_video_search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    String content = search.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showShortToast("输入内容不能为空");
                        return true;
                    }
                    if (searchFragment == null) {
                        searchFragment = SearchVideoDetailFragment.newInstance(content);
                        addBackStackFragment(searchFragment, R.id.fl_activity_search_video_container);
                    } else {
                        searchFragment.notifyDataChanged(content);
                    }
                    SystemUtil.hideSoftInput(SearchVideoActivity.this, search);
                    return true;
                }
                return false;
            }
        });
        refresh.setOnChildScrollUpCallback((parent, child) -> child.getScrollY() > 0);
        mTabLayout = findViewById(R.id.sticky_tab);
        display = findViewById(R.id.sticky_display);
        findViewById(R.id.iv_activity_search_video_finish).setOnClickListener(this);
        mTabLayout.setupWithViewPager(display);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getHotVideoData();
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getType() == VideoUtil.BASE_TYPE_SEARCH_HOT) {
            HotVideoBean hotVideoBean = (HotVideoBean) baseBean.getData();
            updateView(hotVideoBean);
            viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
            display.setAdapter(viewPagerAdapter);
            display.setCurrentItem(0);
        } else if (baseBean.getType() == VideoUtil.BASE_TYPE_SEARCH_CONTENT) {
            SearchVideoBean searchVideoBean = (SearchVideoBean) baseBean.getData();
            //            updateSearchContent(searchVideoBean);
        }

    }


    private void updateView(HotVideoBean hotVideoBean) {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();


        //        0
        titleList.add(hotVideoBean.getData().getMapResult().get_$0().getChannelTitle());
        ArrayList<HotVideoItemBean> list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$0Bean.ListInfoBean item :
                hotVideoBean.getData().getMapResult().get_$0().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        1
        titleList.add(hotVideoBean.getData().getMapResult().get_$1().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$1Bean.ListInfoBeanX item :
                hotVideoBean.getData().getMapResult().get_$1().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        2
        titleList.add(hotVideoBean.getData().getMapResult().get_$2().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$2Bean.ListInfoBeanXX item :
                hotVideoBean.getData().getMapResult().get_$2().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));

        //        3
        titleList.add(hotVideoBean.getData().getMapResult().get_$3().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$3Bean.ListInfoBeanXXX item :
                hotVideoBean.getData().getMapResult().get_$3().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));

        //      5
        titleList.add(hotVideoBean.getData().getMapResult().get_$5().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$5Bean.ListInfoBeanXXXX item :
                hotVideoBean.getData().getMapResult().get_$5().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));

        //        6
        titleList.add(hotVideoBean.getData().getMapResult().get_$6().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$6Bean.ListInfoBeanXXXXX item :
                hotVideoBean.getData().getMapResult().get_$6().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));

        //        9
        titleList.add(hotVideoBean.getData().getMapResult().get_$9().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$9Bean.ListInfoBeanXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$9().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        10

        titleList.add(hotVideoBean.getData().getMapResult().get_$10().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$10Bean.ListInfoBeanXXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$10().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        22
        titleList.add(hotVideoBean.getData().getMapResult().get_$22().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$22Bean.ListInfoBeanXXXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$22().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        106
        titleList.add(hotVideoBean.getData().getMapResult().get_$106().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$106Bean.ListInfoBeanXXXXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$106().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //       556
        titleList.add(hotVideoBean.getData().getMapResult().get_$556().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$556Bean.ListInfoBeanXXXXXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$556().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


        //        10001
        titleList.add(hotVideoBean.getData().getMapResult().get_$10001().getChannelTitle());
        list = new ArrayList<>();
        for (HotVideoBean.DataBean.MapResultBean._$10001Bean.ListInfoBeanXXXXXXXXXXX item :
                hotVideoBean.getData().getMapResult().get_$10001().getListInfo()) {
            HotVideoItemBean hotVideoItemBean = new HotVideoItemBean();
            hotVideoItemBean.setId(item.getId());
            hotVideoItemBean.setTitle(item.getTitle());
            list.add(hotVideoItemBean);
        }
        fragmentList.add(HotVideoListFragment.newInstance(list));


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_activity_search_video_finish) {
            finish();
        }
    }
}
