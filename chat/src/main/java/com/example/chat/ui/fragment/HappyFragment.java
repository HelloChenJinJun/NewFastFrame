package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.HappyAdapter;
import com.example.chat.bean.HappyBean;
import com.example.chat.bean.ImageItem;
import com.example.chat.db.ChatDB;
import com.example.chat.mvp.HappyInfoTask.HappyContacts;
import com.example.chat.mvp.HappyInfoTask.HappyInfoModel;
import com.example.chat.mvp.HappyInfoTask.HappyPresenter;
import com.example.chat.ui.BasePreViewActivity;
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
 * 创建时间:    2017/1/7      18:26
 * QQ:             1981367757
 */
public class HappyFragment extends BaseFragment<List<HappyBean>,HappyPresenter> implements HappyContacts.View<List<HappyBean>>, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
        private SwipeRefreshLayout refresh;
        private SuperRecyclerView display;
        private HappyAdapter mHappyAdapter;
        private HappyPresenter mHappyPresenter;
        private int currentPage = 1;
        private HappyInfoModel mHappyInfoModel;


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
                mHappyPresenter.getHappyInfo(currentPage);
        }

        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(getActivity()));
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new HappyPresenter(this,new HappyInfoModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
                mHappyAdapter = new HappyAdapter();
                mHappyAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                        @Override
                        public void onItemChildClick(int position, View view, int id) {
                                HappyBean happyBean = mHappyAdapter.getData(position);

                                if (id == R.id.iv_fragment_happy_item_picture) {
                                        List<ImageItem> list=new ArrayList<>();
                                        for (HappyBean bean :
                                                mHappyAdapter.getData()) {
                                                ImageItem imageItem = new ImageItem();
                                                imageItem.setPath(bean.getUrl());
                                                list.add(imageItem);
                                        }
                                        ChatDB.create().updateHappyInfoReaded(happyBean.getUrl(), 1);
                                        BasePreViewActivity.startBasePreview(getActivity(),list,position);
//                                        ImageDisplayActivity.start(getActivity(), view, happyBean.getUrl());
                                } else if (id == R.id.iv_fragment_happy_item_share) {
                                        Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, happyBean.getContent() + " " + happyBean.getUrl());
                                        intent.putExtra("share_info", happyBean);
                                        intent.putExtra("type", "happy");
                                        intent.putExtra("destination", "url");
                                        intent.setType("text/plain");
                                        startActivity(intent);
                                }
                        }
                });
               display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
                display.setOnLoadMoreListener(this);
                display.setAdapter(mHappyAdapter);

        }

        @Override
        protected void updateView() {
                onRefresh();
        }

        @Override
        public void onUpdateHappyInfo(List<HappyBean> data) {
                if (refresh.isRefreshing()) {
                        mHappyAdapter.refreshData(data);
                }else {
                        mHappyAdapter.addData(data);
                        currentPage++;
                }
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
        public void onRefresh() {
                currentPage = 1;
                loadMoreData(currentPage);
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
                mHappyPresenter.onDestroy();
        }

        @Override
        public void updateData(List<HappyBean> happyBeen) {

        }

        @Override
        public void loadMore() {
                loadMoreData(currentPage);
        }
}
