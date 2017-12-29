package com.example.news.mvp.searchlibrary;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.NewsApplication;
import com.example.news.NewsContentActivity;
import com.example.news.R;
import com.example.news.adapter.LibraryAdapter;
import com.example.news.adapter.NavigationAdapter;
import com.example.news.bean.SearchLibraryBean;
import com.example.news.dagger.searchlibrary.DaggerLibraryComponent;
import com.example.news.dagger.searchlibrary.LibraryModule;
import com.example.news.util.NewsUtil;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      17:41
 * QQ:             1981367757
 */

public class LibraryFragment extends BaseFragment<List<SearchLibraryBean>, LibraryPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;
    private LoadMoreFooterView loadMoreFooterView;
    private EditText input;
    private boolean isClassSearch;
    @Inject
    LibraryAdapter libraryAdapter;
    private NavigationAdapter typeAdapter, placeAdapter, classAdapter, timeAdapter;
    private SuperRecyclerView typeDisplay, placeDisplay, classDisplay, timeDisplay;

    private int preTypePosition = 0;
    private int prePlacePosition = 0;
    private int preTimePosition = 0;
    private int preClassPosition = 0;
    private WrappedGridLayoutManager typeManager, placeManager, timeManager, classManager;
    private DrawerLayout drawerLayout;


    @Override
    public void updateData(List<SearchLibraryBean> beanList) {
        if (refresh.isRefreshing()) {
            libraryAdapter.refreshData(beanList);
        } else {
            libraryAdapter.addData(beanList);
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_library;
    }

    @Override
    protected void initView() {
        ImageView search = (ImageView) findViewById(R.id.iv_fragment_library_search);
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_library_refresh);
        input = (EditText) findViewById(R.id.et_fragment_library_input);
        typeDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_right_type_display);
        placeDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_right_place_display);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_fragment_library_container);
        classDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_right_class_display);
        timeDisplay = (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_right_time_display);
        findViewById(R.id.btn_fragment_library_right_confirm).setOnClickListener(this);
        search.setOnClickListener(this);
        findViewById(R.id.iv_fragment_library_divide).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerLibraryComponent.builder().libraryModule(new LibraryModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        refresh.setOnRefreshListener(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        loadMoreFooterView = new LoadMoreFooterView(getContext());
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        display.setAdapter(libraryAdapter);
        libraryAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(view.getContext(), NewsContentActivity.class);
                SearchLibraryBean dataEntity = libraryAdapter.getData(position);
                intent.putExtra(NewsUtil.URL, dataEntity.getContentUrl());
                intent.putExtra(NewsUtil.TITLE, dataEntity.getBookName());
                startActivity(intent);

            }
        });
//        ToolBarOption toolBarOption = new ToolBarOption();
//        toolBarOption.setTitle("搜索");
//        setToolBar(toolBarOption);
        typeDisplay.setLayoutManager(typeManager = new WrappedGridLayoutManager(getContext(), 3));
        placeDisplay.setLayoutManager(placeManager = new WrappedGridLayoutManager(getContext(), 3));
        timeDisplay.setLayoutManager(timeManager = new WrappedGridLayoutManager(getContext(), 3));
        classDisplay.setLayoutManager(classManager = new WrappedGridLayoutManager(getContext(), 3));
        typeDisplay.setNestedScrollingEnabled(false);
        placeDisplay.setNestedScrollingEnabled(false);
        timeDisplay.setNestedScrollingEnabled(false);
        classDisplay.setNestedScrollingEnabled(false);
        typeAdapter = new NavigationAdapter();
        placeAdapter = new NavigationAdapter();
        classAdapter = new NavigationAdapter();
        timeAdapter = new NavigationAdapter();
        classDisplay.setAdapter(classAdapter);
        typeDisplay.setAdapter(typeAdapter);
        placeDisplay.setAdapter(placeAdapter);
        timeDisplay.setAdapter(timeAdapter);
        placeAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                TextView pre = (TextView) placeManager.findViewByPosition(prePlacePosition).findViewById(id);
                pre.setTextColor(getContext().getResources().getColor(R.color.base_color_text_grey));
                pre.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_normal));
                TextView textView = (TextView) view;
                textView.setTextColor(getContext().getResources().getColor(R.color.base_color_text_blue));
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_selected));
                prePlacePosition = position;
            }
        });
        typeAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                TextView pre = (TextView) typeManager.findViewByPosition(preTypePosition).findViewById(id);
                pre.setTextColor(getContext().getResources().getColor(R.color.base_color_text_grey));
                pre.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_normal));
                TextView textView = (TextView) view;
                textView.setTextColor(getContext().getResources().getColor(R.color.base_color_text_blue));
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_selected));
                preTypePosition = position;
            }
        });
        timeAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                TextView pre = (TextView) timeManager.findViewByPosition(preTimePosition).findViewById(id);
                pre.setTextColor(getContext().getResources().getColor(R.color.base_color_text_grey));
                pre.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_normal));
                TextView textView = (TextView) view;
                textView.setTextColor(getContext().getResources().getColor(R.color.base_color_text_blue));
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_selected));
                preTimePosition = position;
            }
        });
        classAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                TextView pre = (TextView) classManager.findViewByPosition(preClassPosition).findViewById(id);
                pre.setTextColor(getContext().getResources().getColor(R.color.base_color_text_grey));
                pre.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_normal));
                TextView textView = (TextView) view;
                textView.setTextColor(getContext().getResources().getColor(R.color.base_color_text_blue));
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.tab_btn_bg_selected));
                preClassPosition = position;
            }
        });
    }


    @Override
    protected void updateView() {
        timeAdapter.addData(Arrays.asList(getResources().getStringArray(R.array.time_name)));
        placeAdapter.addData(Arrays.asList(getResources().getStringArray(R.array.place_name)));
        typeAdapter.addData(Arrays.asList(getResources().getStringArray(R.array.type_name)));
        classAdapter.addData(Arrays.asList(getResources().getStringArray(R.array.class_name)));
    }


    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public void loadMore() {
        if (!isClassSearch) {
            if (TextUtils.isEmpty(input.getText().toString().trim())) {
                ToastUtils.showShortToast("内容不能为空哦");
            } else {
                presenter.searchBook(false, false, input.getText().toString().trim());
            }
        } else {
            presenter.searchNewBook(false, false, timeAdapter.getData(preTimePosition).split("/")[1]
                    , typeAdapter.getData(preTypePosition).split("/")[1]
                    , placeAdapter.getData(prePlacePosition).split("/")[1]
                    , classAdapter.getData(preClassPosition));
        }
    }

    @Override
    public void onRefresh() {
        if (!isClassSearch) {
            if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }
            if (TextUtils.isEmpty(input.getText().toString().trim())) {
                ToastUtils.showShortToast("内容不能为空哦");
            } else {
                presenter.searchBook(false, true, input.getText().toString().trim());
            }
        } else {
            presenter.searchNewBook(false, true, timeAdapter.getData(preTimePosition).split("/")[1]
                    , typeAdapter.getData(preTypePosition).split("/")[1]
                    , placeAdapter.getData(prePlacePosition).split("/")[1]
                    , classAdapter.getData(preClassPosition));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_fragment_library_right_confirm) {
            ToastUtils.showShortToast("确定");
            drawerLayout.closeDrawer(GravityCompat.END);
            refresh.setRefreshing(true);
            isClassSearch = true;
            presenter.searchNewBook(false, true, timeAdapter.getData(preTimePosition).split("/")[1]
                    , typeAdapter.getData(preTypePosition).split("/")[1]
                    , placeAdapter.getData(prePlacePosition).split("/")[1]
                    , classAdapter.getData(preClassPosition));
        } else if (v.getId()==R.id.iv_fragment_library_search){
            if (TextUtils.isEmpty(input.getText().toString().trim())) {
                ToastUtils.showShortToast("内容不能为空哦");
            } else {
                isClassSearch = false;
                refresh.setRefreshing(true);
                presenter.searchBook(false, true, input.getText().toString().trim());
            }
        }else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
        } else {
            super.showError(errorMsg, listener);
        }
        refresh.setRefreshing(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

}
