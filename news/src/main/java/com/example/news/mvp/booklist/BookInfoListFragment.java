package com.example.news.mvp.booklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.NewsApplication;
import com.example.news.NewsContentActivity;
import com.example.news.R;
import com.example.news.adapter.BookInfoListAdapter;
import com.example.news.bean.BookInfoBean;
import com.example.news.dagger.booklist.BookInfoListModule;
import com.example.news.dagger.booklist.DaggerBookInfoListComponent;
import com.example.news.event.LibraryLoginEvent;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.util.NewsUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      10:55
 * QQ:             1981367757
 */

public class BookInfoListFragment extends BaseFragment<Object, BookInfoListPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refreshLayout;

    @Inject
    BookInfoListAdapter bookInfoListAdapter;


    private LoadMoreFooterView loadMoreFooterView;
    private String title;

    @Override
    public void updateData(Object object) {

        if (object instanceof String) {
            String message = (String) object;
            dismissLoadDialog();
            if (message.equals("续借成功")) {
                hideBaseDialog();
            }
            ToastUtils.showShortToast(((String) object));
        } else if (object instanceof Bitmap) {
            verify.setImageBitmap(((Bitmap) object));
        } else {
            List<BookInfoBean> list = null;
            if (object != null) {
                list = (List<BookInfoBean>) object;
            }
            if (refreshLayout.isRefreshing()) {
                bookInfoListAdapter.refreshData(list);
            } else {
                bookInfoListAdapter.addData(list);
                if (bookInfoListAdapter.getData().size() == 0) {
                    if (loadMoreFooterView != null) {
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    }
                }
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
                Intent intent = new Intent(getContext(), NewsContentActivity.class);
                intent.putExtra(NewsUtil.TITLE, bookInfoListAdapter.getData(position).getBookName());
                intent.putExtra(NewsUtil.URL, bookInfoListAdapter.getData(position).getContentUrl());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position, View view) {
                if (!title.equals("历史")) {
                    final BookInfoBean bean = bookInfoListAdapter.getData(position);
                    showCustomDialog("输入验证码", getCustomView()
                            , "取消", "确定",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    hideBaseDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!TextUtils.isEmpty(input.getText().toString().trim())) {
                                        hideBaseDialog();
                                        showLoadDialog("正在续借......");
                                        presenter.borrowBook(input.getText().toString().trim(), bean
                                                .getNumber(), bean.getCheck());
                                    } else {
                                        ToastUtils.showShortToast("验证码不能为空");
                                    }
                                }
                            });
                    presenter.getVerifyImage();
                }
                return true;
            }
        });
        display.setAdapter(bookInfoListAdapter);
        presenter.registerEvent(LibraryLoginEvent.class, new Consumer<LibraryLoginEvent>() {
            @Override
            public void accept(@NonNull LibraryLoginEvent libraryLoginEvent) throws Exception {
                if (getContext() != null) {
                    LibraryLoginActivity.start(getContext(), libraryLoginEvent.getInfo());
                    getActivity().finish();
                }
            }
        });
    }


    private ImageView verify;
    private EditText input;

    private View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_book_info_list, null);
        verify = view.findViewById(R.id.iv_view_fragment_book_info_list_verify);
        verify.setOnClickListener(this);
        input = view.findViewById(R.id.et_view_fragment_book_info_list_input);
        return view;
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
        presenter.getBorrowBookInfo(false, false, true);
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

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_view_fragment_book_info_list_verify) {
            presenter.getVerifyImage();
        }
    }
}
