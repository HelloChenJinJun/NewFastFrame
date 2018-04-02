package com.example.chat.mvp.nearbyList;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;


import com.example.chat.R;
import com.example.chat.adapter.NearbyListAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.NearbyListBean;
import com.example.chat.dagger.nearbyList.DaggerNearbyListComponent;
import com.example.chat.dagger.nearbyList.NearbyListModule;
import com.example.chat.events.LocationEvent;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.base.SlideBaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:15
 * QQ:         1981367757
 */

public class NearbyListActivity extends SlideBaseActivity<List<NearbyListBean>, NearbyListPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;
    @Inject
    NearbyListAdapter nearbyListAdapter;
    private LocationEvent locationEvent;
    private int selectedPosition;


    @Override
    public void updateData(List<NearbyListBean> listBeans) {
        nearbyListAdapter.addData(listBeans);
        if (!nearbyListAdapter.getData(selectedPosition).isCheck()) {
            nearbyListAdapter.getData(selectedPosition).setCheck(true);
            nearbyListAdapter.notifyItemChanged(selectedPosition + nearbyListAdapter.getItemUpCount());
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
        return R.layout.activity_nearby_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_nearby_list_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_nearby_list_refresh);
    }

    @Override
    protected void initData() {
        DaggerNearbyListComponent.builder().chatMainComponent(getChatMainComponent())
                .nearbyListModule(new NearbyListModule(this)).build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration(this));
        display.setAdapter(nearbyListAdapter);
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        nearbyListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                NearbyListBean nearbyListBean = nearbyListAdapter.getData(position);
                Intent intent = new Intent();
                intent.putExtra(Constant.LOCATION, nearbyListBean.getLocationEvent());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        locationEvent = (LocationEvent) getIntent().getSerializableExtra(Constant.LOCATION);
        if (locationEvent == null) {
            presenter.registerEvent(LocationEvent.class, locationEvent1 -> {
//                获取位置后取消订阅
                presenter.unDispose();
                locationEvent = locationEvent1;
                refreshData(true);
            });
            presenter.getLocation();
        } else {
            refreshData(false);
        }
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("附近位置搜索");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    private void refreshData(boolean isSelected) {
        List<NearbyListBean> listBeans = new ArrayList<>();
        NearbyListBean item = new NearbyListBean();
        LocationEvent event = new LocationEvent();
        item.setCheck(false);
        event.setTitle("不显示位置");
        item.setLocationEvent(event);
        listBeans.add(item);
        NearbyListBean item1 = new NearbyListBean();
        item1.setCheck(false);
        LocationEvent locationEvent1 = new LocationEvent();
        locationEvent1.setTitle(locationEvent.getCity());
        locationEvent1.setCity(locationEvent.getCity());
        locationEvent1.setProvince(locationEvent.getProvince());
        locationEvent1.setLatitude(locationEvent.getLatitude());
        locationEvent1.setLongitude(locationEvent.getLongitude());
        locationEvent1.setLocation(locationEvent1.getCity());
        item1.setLocationEvent(locationEvent1);
        listBeans.add(item1);
        if (locationEvent.getTitle().equals(locationEvent.getCity())) {
            item1.setCheck(true);
            selectedPosition = 1;
        } else if (!isSelected) {
            NearbyListBean nearbyListBean = new NearbyListBean();
            nearbyListBean.setCheck(true);
            nearbyListBean.setLocationEvent(locationEvent);
            listBeans.add(nearbyListBean);
            selectedPosition = 2;
        } else {
            selectedPosition = 0;
            item.setCheck(true);
        }
        nearbyListAdapter.addData(0, listBeans);

        runOnUiThread(() ->
                onRefresh());
    }

    public static void start(Activity activity, LocationEvent locationEvent, int requestCode) {
        Intent intent = new Intent(activity, NearbyListActivity.class);
        intent.putExtra(Constant.LOCATION, locationEvent);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void loadMore() {
        presenter.getNearbyListData(locationEvent, false);
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getNearbyListData(locationEvent, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewLocationManager.getInstance().clear();
    }
}
