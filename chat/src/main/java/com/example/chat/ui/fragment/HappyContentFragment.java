package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.HappyContentAdapter;
import com.example.chat.bean.HappyContentBean;
import com.example.chat.mvp.HappyContentInfoTask.HappyContentContacts;
import com.example.chat.mvp.HappyContentInfoTask.HappyContentModel;
import com.example.chat.mvp.HappyContentInfoTask.HappyContentPresenter;
import com.example.chat.ui.EditShareMessageActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.cusotomview.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:26
 * QQ:             1981367757
 */

public class HappyContentFragment extends BaseFragment<List<HappyContentBean>,HappyContentPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, HappyContentContacts.View {
        private SuperRecyclerView display;
        private HappyContentAdapter mHappyAdapter;
        private List<HappyContentBean> data = new ArrayList<>();
        private HappyContentPresenter mHappyPresenter;
        private int currentPage = 1;
        private SwipeRefreshLayout refresh;


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
                return R.layout.happy_layout;
        }

        @Override
        public void initView() {
                 refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_happy_fragment);
                display = (SuperRecyclerView) findViewById(R.id.rcv_happy_fragment_display);
                refresh.setOnRefreshListener(this);
        }



        private void loadMoreData(int currentPage) {
                mHappyPresenter.getHappyContentInfo(currentPage,false);
        }

        @Override
        public void initData() {
                display.setLayoutManager( new LinearLayoutManager(getActivity()));
                display.addItemDecoration(new ListViewDecoration(getActivity()));
//                display.setHasFixedSize(true);
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new HappyContentPresenter(this,new HappyContentModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
                mHappyAdapter = new HappyContentAdapter();
               mHappyAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                       @Override
                       public void onItemChildClick(int position, View view, int id) {
                               if (id == R.id.iv_fragment_happy_content_item_share) {
                                       HappyContentBean bean = mHappyAdapter.getData(position);
                                       Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                       intent.setAction(Intent.ACTION_SEND);
                                       intent.putExtra(Intent.EXTRA_TEXT, bean.getContent());
                                       intent.putExtra("share_info", bean);
                                       intent.putExtra("type", "happy_content");
                                       intent.putExtra("destination", "url");
                                       intent.setType("text/plain");
                                       startActivity(intent);
                               }
                       }
               });
                display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
                display.setOnLoadMoreListener(this);
                display.setAdapter(mHappyAdapter);
                mHappyAdapter.addData(data);
        }

        @Override
        protected void updateView() {
                mHappyPresenter.getHappyContentInfo(currentPage,true);
        }




        @Override
        public void onRefresh() {
                data.clear();
                currentPage = 1;
                loadMoreData(currentPage);
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
//                mHappyPresenter.onDestroy();

        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                mHappyPresenter.onDestroy();
        }

        @Override
        public void loadMore() {
                loadMoreData(currentPage);
        }


        @Override
        public void hideLoading() {
                super.hideLoading();
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
        }

        @Override
        public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
                super.showError(errorMsg, listener);
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
        }

        @Override
    public void updateData(List<HappyContentBean> happyContentBeen) {
                currentPage++;
            if (refresh.isRefreshing()) {
                    mHappyAdapter.refreshData(happyContentBeen);
            }else {
                    mHappyAdapter.addData(happyContentBeen);

            }
    }
}
