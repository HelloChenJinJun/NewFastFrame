package com.example.chat.mvp.main.recent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.chat.adapter.RecentListAdapter;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.Constant;
import com.example.chat.events.GroupTableEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.mvp.main.HomeFragment;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.Closeable;
import com.example.commonlibrary.baseadapter.swipeview.OnSwipeMenuItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuItem;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.bean.chat.RecentMessageEntity;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;



/**
 *
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      12:18
 * QQ:             1981367757
 */


/**
 * 最近会话列表fragment
 */
public class RecentFragment extends AppBaseFragment implements SwipeRefreshLayout.OnRefreshListener {
        private RecentListAdapter mAdapter;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private LinearLayoutManager mLinearLayoutManager;


        @Override
        protected boolean isNeedHeadLayout() {
                return false;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }


        @Override
        protected boolean needStatusPadding() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.fragment_recent;
        }

        @Override
        public void initView() {
                SwipeMenuRecyclerView display = (SwipeMenuRecyclerView) findViewById(R.id.rcv_recent_display);
                mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_recent_container);
                display.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(getActivity()));
                display.setItemAnimator(new DefaultItemAnimator());
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                display.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
                        int width = getActivity().getResources().getDimensionPixelSize(R.dimen.recent_top_height);
                        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//                                 添加左右侧菜单
                        {
                                SwipeMenuItem topItem = new SwipeMenuItem(getActivity());
                                topItem.setBackgroundDrawable(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                                        0xCE))).setText("置顶").setTextColor(Color.WHITE).setWidth(width).setHeight(height);
                                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                                deleteItem.setBackgroundDrawable(new ColorDrawable(Color.rgb(0xF9,
                                        0x3F, 0x25))).setText("删除").setTextColor(Color.WHITE).setHeight(height).setWidth(width);
                                swipeRightMenu.addMenuItem(topItem);
                                swipeRightMenu.addMenuItem(deleteItem);
                        }
                });
                mSwipeRefreshLayout.setOnRefreshListener(this);
                display.setAdapter(mAdapter = new RecentListAdapter());
                mAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                                RecentMessageEntity msg;
                                msg = mAdapter.getData(position);
                                if (msg.getType()==RecentMessageEntity.TYPE_GROUP) {
                                        ChatActivity.start(getActivity(), Constant.TYPE_GROUP,msg.getId());
                                }else {
                                        ChatActivity.start(getActivity(), Constant.TYPE_PERSON,msg.getId());
                                }
                        }
                });
                display.setSwipeMenuItemClickListener(new MySwipeItemClickListener());
        }


        @Override
        public void initData() {
                if (mLinearLayoutManager.findViewByPosition(0) != null) {
                        mLinearLayoutManager.findViewByPosition(0).setVisibility(View.GONE);
                }
                registerRxBus();
        }


        private void registerRxBus() {
                addDisposable(RxBusManager.getInstance().registerEvent(GroupTableEvent.class, groupTableEvent -> {
//                                刷新过来的，更新下群结构消息
                        if (groupTableEvent.getType()== GroupTableEvent.TYPE_GROUP_NUMBER
                                &&groupTableEvent.getAction()==GroupTableEvent.ACTION_DELETE
                                && UserManager.getInstance()
                                .getCurrentUserObjectId().equals(groupTableEvent.getUid())){
                                String content=groupTableEvent.getGroupId();
                                RecentMessageEntity recentMsg=new RecentMessageEntity();
                                recentMsg.setId(content);
                                mAdapter.removeData(recentMsg);
                        }
                }));
                addDisposable(RxBusManager.getInstance().registerEvent(RecentEvent.class, recentEvent -> {
                        RecentMessageEntity recentMessageEntity= UserDBManager
                                .getInstance().getRecentMessage(recentEvent.getId());
                        if (recentEvent.getAction()==RecentEvent.ACTION_ADD) {
                                mAdapter.addData(0,recentMessageEntity);
                        }else {
                                mAdapter.removeData(recentMessageEntity);
                        }
                }));

        }




        @Override
        protected void updateView() {
                mAdapter.addData(UserDBManager.getInstance().getAllRecentMessage());
        }


        @Override
        public void onResume() {
                super.onResume();
                onHiddenChanged(false);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
                super.onHiddenChanged(hidden);
                if (!hidden) {
                        ((HomeFragment) getParentFragment()).updateTitle("聊天");
                }
        }



        @Override
        public void onRefresh() {
//                这里进行更新操作
                LogUtil.e("主界面刷新拉");
                mAdapter.refreshData(UserDBManager.getInstance().getAllRecentMessage());
                mSwipeRefreshLayout.setRefreshing(false);
        }



        @Override
        public void updateData(Object o) {

        }
        private class MySwipeItemClickListener implements OnSwipeMenuItemClickListener {
                @Override
                public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
//                        删除操作
                        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                                if (menuPosition == 0) {
                                        ToastUtils.showShortToast("置顶操作");
//                                        置顶操作
                                } else if (menuPosition == 1) {
//                                        删除操作，1、最近会话删除
                                        RecentMessageEntity msg = mAdapter.getData(adapterPosition);
                                        UserDBManager.getInstance().deleteRecentMessage(msg.getId());
//                                        删除操作，2、聊天消息删除
                                        mAdapter.removeData(msg);
                                }
                        }
                }
        }
}
