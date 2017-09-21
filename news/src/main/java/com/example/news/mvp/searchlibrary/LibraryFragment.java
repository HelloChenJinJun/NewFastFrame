package com.example.news.mvp.searchlibrary;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.adapter.LibraryAdapter;
import com.example.news.NewsApplication;
import com.example.news.NewsContentActivity;
import com.example.news.R;
import com.example.news.bean.SearchLibraryBean;
import com.example.news.dagger.DaggerLibraryComponent;
import com.example.news.dagger.searchlibrary.LibraryModule;
import com.example.news.util.NewsUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      17:41
 * QQ:             1981367757
 */

public class LibraryFragment extends BaseFragment<List<SearchLibraryBean>,LibraryPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;
    private LoadMoreFooterView loadMoreFooterView;
    private EditText input;
    private TextView search;
    @Inject
    LibraryAdapter libraryAdapter;


    @Override
    public void updateData(List<SearchLibraryBean> beanList) {
        if (loadMoreFooterView.getStatus()!= LoadMoreFooterView.Status.LOADING) {
            libraryAdapter.clearAllData();
            libraryAdapter.notifyDataSetChanged();
            libraryAdapter.addData(beanList);
        }else {
            libraryAdapter.addData(beanList);
            if (libraryAdapter.getData().size() == 0) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }
        }
    }

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
        return R.layout.fragment_library;
    }

    @Override
    protected void initView() {
        search= (TextView) findViewById(R.id.tv_fragment_library_search);
        display= (SuperRecyclerView) findViewById(R.id.srcv_fragment_library_display);
        refresh= (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_library_refresh);
        input= (EditText) findViewById(R.id.et_fragment_library_input);
        search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerLibraryComponent.builder().libraryModule(new LibraryModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        refresh.setOnRefreshListener(this);
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMoreFooterView = new LoadMoreFooterView(getContext());
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        display.setAdapter(libraryAdapter);
        libraryAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent=new Intent(view.getContext(),NewsContentActivity.class);
                SearchLibraryBean dataEntity=libraryAdapter.getData(position);
                intent.putExtra(NewsUtil.URL,dataEntity.getContentUrl());
                intent.putExtra(NewsUtil.TITLE,dataEntity.getBookName());
                startActivity(intent);
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("搜索");
        setToolBar(toolBarOption);
    }

    @Override
    protected void updateView() {
//        presenter.searchBook(false, true,"");

    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public void loadMore() {
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.searchBook(false, false, input.getText().toString().trim());
        }
    }

    @Override
    public void onRefresh() {
        if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        }
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.searchBook(false, true, input.getText().toString().trim());
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.searchBook(false, true, input.getText().toString().trim());
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
        if (libraryAdapter.getData().size()>0) {
            super.hideLoading();
        }else {
            showEmptyView();
        }
        refresh.setRefreshing(false);
    }
}
