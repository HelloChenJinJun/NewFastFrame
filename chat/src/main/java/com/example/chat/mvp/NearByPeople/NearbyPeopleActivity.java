package com.example.chat.mvp.NearByPeople;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.NearbyPeopleAdapter;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.User;
import com.example.chat.dagger.nearbyPeople.DaggerNearbyPeopleComponent;
import com.example.chat.dagger.nearbyPeople.NearbyPeopleModule;
import com.example.chat.events.LocationEvent;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserInfoTask.UserInfoActivity;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.PermissionPageUtils;
import com.example.commonlibrary.utils.PermissionUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/11      12:42
 * QQ:             1981367757
 */

public class NearbyPeopleActivity extends SlideBaseActivity<List<User>, NearbyPeoplePresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, PermissionUtil.RequestPermissionCallBack {
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    NearbyPeopleAdapter mNearbyPeopleAdapter;
    private int currentPosition = 0;


    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_nearby_people;
    }


    @Override
    public void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_nearby_people_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_nearby_people_display);
        refresh.setOnRefreshListener(this);
    }


    @Override
    public void initData() {
        DaggerNearbyPeopleComponent.builder()
                .chatMainComponent(getChatMainComponent())
                .nearbyPeopleModule(new NearbyPeopleModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration(this));
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        mNearbyPeopleAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                UserInfoActivity.start(NearbyPeopleActivity.this, mNearbyPeopleAdapter
                        .getData(position).getObjectId());
            }
        });
        display.setAdapter(mNearbyPeopleAdapter);
        initActionBar();
        PermissionUtil.requestLocation(this, this);
        loadData(true);
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setTitle("附近的人");
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightResId(R.drawable.ic_list_blue_grey_900_24dp);
        toolBarOption.setRightListener(v -> {
            List<String> list = new ArrayList<>();
            list.add("查看全部");
            list.add("只查看女生");
            list.add("只查看男生");
            showChooseDialog("搜索条件", list, (parent, view, position, id) -> {
                dismissBaseDialog();
                if (currentPosition != position) {
                    currentPosition = position;
                    loadData(true);
                }
            });
        });
        setToolBar(toolBarOption);
    }

    private void loadData(boolean isRefresh) {
        presenter.queryNearbyPeople(isRefresh, currentPosition);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NearbyPeopleActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refresh.setRefreshing(false);
        super.hideLoading();
    }

    @Override
    public void updateData(List<User> list) {
        if (refresh.isRefreshing()) {
            mNearbyPeopleAdapter.refreshData(list);
        } else {
            mNearbyPeopleAdapter.addData(list);
        }
    }

    @Override
    public void loadMore() {
        loadData(false);
    }

    @Override
    public void onRequestPermissionSuccess() {
        showLoadDialog("正在获取定位......");
        presenter.registerEvent(LocationEvent.class, locationEvent -> {
            dismissLoadDialog();
            ToastUtils.showShortToast("获取定位信息成功");
            presenter.queryNearbyPeople(true, currentPosition);
        });
        NewLocationManager.getInstance().startLocation();
    }

    @Override
    public void onRequestPermissionFailure() {
        ToastUtils.showShortToast("附近人功能需要定位信息");
        showBaseDialog("权限界面操作", "是否需要打开权限界面", "取消", "确定"
                , v12 -> {
                    dismissBaseDialog();
                    finish();
                }, v1 -> {
                    dismissBaseDialog();
                    PermissionPageUtils.jumpPermissionPage(NearbyPeopleActivity.this);
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewLocationManager.getInstance().clear();
    }
}
