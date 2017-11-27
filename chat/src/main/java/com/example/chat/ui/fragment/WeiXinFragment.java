package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.WeiXinAdapter;
import com.example.chat.bean.WinXinBean;
import com.example.chat.db.ChatDB;
import com.example.chat.mvp.WinXinInfoTask.WinXinInfoContacts;
import com.example.chat.mvp.WinXinInfoTask.WinXinInfoModel;
import com.example.chat.mvp.WinXinInfoTask.WinXinInfoPresenter;
import com.example.chat.ui.EditShareMessageActivity;
import com.example.chat.ui.WeiXinNewsActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      23:03
 * QQ:             1981367757
 */
public class WeiXinFragment extends BaseFragment<List<WinXinBean>,WinXinInfoPresenter> implements WinXinInfoContacts.View<List<WinXinBean>>,OnLoadMoreListener {
        private SuperRecyclerView display;
        private SwipeRefreshLayout refresh;
        private WeiXinAdapter mAdapter;
        private WinXinInfoPresenter mWinXinInfoPresenter;


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
                return R.layout.wei_xin_layout;
        }

        @Override
        public void initView() {
                display = (SuperRecyclerView) findViewById(R.id.rcv_wei_xin_display);
                refresh = (SwipeRefreshLayout) findViewById(R.id.swl_wei_xin_refresh);
                refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                                currentPage = 1;
                                mAdapter.clear();
                                mWinXinInfoPresenter.getWinXinInfo(currentPage,false);
                        }
                });
        }


        private int currentPage = 1;


        @Override
        public void initData() {
                display.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                mAdapter = new WeiXinAdapter();
                mWinXinInfoPresenter = new WinXinInfoPresenter(this,new WinXinInfoModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
                display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
                display.setOnLoadMoreListener(this);
                mAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
//                                ((TextView) baseWrappedViewHolder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(getResources().getColor(R.color.base_color_text_grey));
                                WinXinBean bean = mAdapter.getData(position);
                                ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 1);
                                Intent intent = new Intent(getActivity(), WeiXinNewsActivity.class);
                                intent.putExtra("bean", bean);
                                startActivity(intent);
                        }

                        @Override
                        public void onItemChildClick(int position, View view, int id) {
                                if (id == R.id.btn_wei_xin_fragment_right) {
                                        final WinXinBean bean = mAdapter.getData(position);
                                        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                                        popupMenu.getMenuInflater().inflate(R.menu.wei_xin_fragment_item_menu, popupMenu.getMenu());
                                        if (ChatDB.create().getWeixinInfoReadStatus(bean.getUrl()) == 1) {
                                                popupMenu.getMenu().findItem(R.id.wei_xin_fragment_item_menu_read).setTitle("标记为未读状态");
                                        } else {
                                                popupMenu.getMenu().findItem(R.id.wei_xin_fragment_item_menu_read).setTitle("标记为已读状态");
                                        }
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                        int i = item.getItemId();
                                                        if (i == R.id.wei_xin_fragment_item_menu_share) {
                                                                Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                                                intent.putExtra(Intent.EXTRA_TEXT, bean.getTitle() + "," + bean.getUrl());
                                                                intent.putExtra("share_info", bean);
                                                                intent.putExtra("type", "wei_xin");
                                                                intent.putExtra("destination", "url");
                                                                intent.setType("text/plain");
                                                                startActivity(intent);

                                                        } else if (i == R.id.wei_xin_fragment_item_menu_read) {
                                                                if (item.getTitle().equals("标记为未读状态")) {
                                                                        ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 0);
                                                                } else {
                                                                        ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 1);
                                                                }
                                                                mAdapter.notifyDataSetChanged();

                                                        }
                                                        return true;
                                                }
                                        });
                                        popupMenu.show();
                                }
                        }
                });
                display.setAdapter(mAdapter);

        }

        @Override
        protected void updateView() {
                currentPage = 1;
                mWinXinInfoPresenter.getWinXinInfo(currentPage,true);
        }

        private void loadMoreData() {
                mWinXinInfoPresenter.getWinXinInfo(currentPage,false);
        }

        @Override
        public void updateData(List<WinXinBean> data) {
                currentPage++;
                if (refresh.isRefreshing()) {
                        mAdapter.refreshData(data);
                }else {
                        mAdapter.addData(data);
                }
        }


        @Override
        public void hideLoading() {
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
                super.hideLoading();
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                mWinXinInfoPresenter.onDestroy();
        }


        @Override
        public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
                super.showError(errorMsg, listener);
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
        }

        @Override
        public void loadMore() {
                loadMoreData();
        }
}
