package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.PictureAdapter;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.PictureBean;
import com.example.chat.db.ChatDB;
import com.example.chat.mvp.PictureInfoTask.PictureContacts;
import com.example.chat.mvp.PictureInfoTask.PictureModel;
import com.example.chat.mvp.PictureInfoTask.PicturePresenter;
import com.example.chat.ui.BasePreViewActivity;
import com.example.chat.ui.EditShareMessageActivity;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:02
 * QQ:             1981367757
 */

public class PictureFragment extends BaseFragment<List<PictureBean>,PicturePresenter> implements SwipeRefreshLayout.OnRefreshListener, PictureContacts.View<List<PictureBean>>,OnLoadMoreListener {
        private SwipeRefreshLayout refresh;
        private SuperRecyclerView display;
        private PictureAdapter mAdapter;
        private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
        private PicturePresenter mHappyPresenter;
        private int currentPage = 1;
        private PictureModel mPictureModel;
//        private int visibleCount;
//        private int[] firstVisiblePosition;
//        private int itemCount;
//        private boolean isLoading = false;


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
                mHappyPresenter.getPictureInfo(currentPage);
        }

        @Override
        public void initData() {
                display.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new PicturePresenter(this,new PictureModel(ChatApplication
                .getChatMainComponent().getMainRepositoryManager()));
                mAdapter = new PictureAdapter();
               mAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                       @Override
                       public void onItemChildClick(int position, View view, int id) {
                               PictureBean bean = mAdapter.getData(position);
                               if (id == R.id.iv_fragment_picture_item_share) {

                                       Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                       intent.setAction(Intent.ACTION_SEND);
                                       intent.putExtra(Intent.EXTRA_TEXT, bean.getUrl());
                                       intent.putExtra("share_info", bean);
                                       intent.putExtra("type", "picture");
                                       intent.putExtra("destination", "url");
                                       intent.setType("text/plain");
                                       startActivity(intent);

                               } else if (id == R.id.iv_fragment_picture_item_picture) {
                                       ChatDB.create().updatePictureReaded(bean.getUrl(), 1);
                                       List<PictureBean> data = mAdapter.getData();
                                       List<ImageItem> itemList = new ArrayList<>();
                                       for (PictureBean item :
                                               data) {
                                               ImageItem imageItem = new ImageItem();
                                               imageItem.setPath(item.getUrl());
                                               itemList.add(imageItem);
                                       }
                                       BasePreViewActivity.startBasePreview(getActivity(), itemList, position);
                               }
                       }
               });
               display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
                display.setOnLoadMoreListener(this);
                display.setAdapter(mAdapter);
        }

        @Override
        protected void updateView() {
                onRefresh();
        }


        @Override
        public void hideLoading() {

                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
//                isLoading = false;
                super.hideLoading();
        }

        @Override
        public void updateData(List<PictureBean> pictureBeen) {

        }


        @Override
        public void onRefresh() {
                mAdapter.clearAllData();
                currentPage = 1;
                loadMoreData(currentPage);
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
                mHappyPresenter.onDestroy();
        }


        @Override
        public void onUpdatePictureInfo(List<PictureBean> data) {
                currentPage++;
                mAdapter.addData(data);
        }

        @Override
        public void loadMore() {
                loadMoreData(currentPage);
        }
}
