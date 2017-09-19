package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.utils.AppUtil;
import com.example.news.bean.BookInfoBean;
import com.example.news.dagger.booklist.BookInfoListModule;
import com.example.news.dagger.booklist.DaggerBookInfoListComponent;
import com.example.news.util.NewsUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      10:55
 * QQ:             1981367757
 */

public class BookInfoListFragment extends BaseFragment<List<BookInfoBean>, BookInfoListPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refreshLayout;

    @Inject
    BookInfoListAdapter bookInfoListAdapter;


    private LoadMoreFooterView loadMoreFooterView;
    private String title;

    @Override
    public void updateData(List<BookInfoBean> list) {
        if (refreshLayout.isRefreshing()) {
            bookInfoListAdapter.clearAllData();
            bookInfoListAdapter.notifyDataSetChanged();
            bookInfoListAdapter.addData(list);
        } else {
            bookInfoListAdapter.addData(list);
            if (bookInfoListAdapter.getData().size() == 0) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }
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
        return R.layout.fragment_book_info_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_book_info_list_display);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_book_info_list_refresh);
    }

    @Override
    protected void initData() {
        title = getArguments().getString(NewsUtil.TITLE);
        DaggerBookInfoListComponent.builder().bookInfoListModule(new BookInfoListModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        refreshLayout.setOnRefreshListener(this);
        if (title.equals("历史")) {
            display.setLoadMoreFooterView(loadMoreFooterView = new LoadMoreFooterView(getContext()));
            display.setOnLoadMoreListener(this);
        }
        bookInfoListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent=new Intent(getContext(),NewsContentActivity.class);
                intent.putExtra(NewsUtil.TITLE,bookInfoListAdapter.getData(position).getBookName());
                intent.putExtra(NewsUtil.URL,bookInfoListAdapter.getData(position).getContentUrl());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position, View view) {
                return true;
            }
        });
        display.setAdapter(bookInfoListAdapter);

    }

    @Override
    protected void updateView() {
        if (title.equals("历史")) {
            presenter.getBorrowBookInfo(true, true, true);
        } else {
            presenter.getBorrowBookInfo(true, true, false);
        }
    }

    public static BookInfoListFragment newInstance(String title) {
        BookInfoListFragment bookInfoListFragment = new BookInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NewsUtil.TITLE, title);
        bookInfoListFragment.setArguments(bundle);
        return bookInfoListFragment;
    }

    @Override
    public void loadMore() {
        presenter.getBorrowBookInfo(false, true, true);
    }

    @Override
    public void onRefresh() {
        if (title.equals("历史")) {
            presenter.getBorrowBookInfo(false, true, true);
        } else {
            presenter.getBorrowBookInfo(false, true, false);
        }
    }


    @Override
    public void hideLoading() {
        if (bookInfoListAdapter.getData().size() > 0) {
            super.hideLoading();
        } else {
            showEmptyView();
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refreshLayout.setRefreshing(false);
        if (AppUtil.isNetworkAvailable(getContext())) {
            Intent intent=new Intent(getContext(),LibraryLoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }


}
