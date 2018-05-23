package com.example.chat.mvp.skin;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.SkinListAdapter;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.dagger.skin.DaggerSkinListComponent;
import com.example.chat.dagger.skin.SkinListModule;
import com.example.chat.mvp.skin.skinDetail.SkinDetailActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:39
 */

public class SkinListActivity extends SlideBaseActivity<List<SkinEntity>, SkinListPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;

    @Inject
    SkinListAdapter skinListAdapter;
    private int currentPosition;


    @Override
    public void updateData(List<SkinEntity> skinBeans) {
        if (refresh.isRefreshing()) {
            skinListAdapter.refreshData(skinBeans);
        } else {
            skinListAdapter.addData(skinBeans);
        }
    }

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
        return R.layout.activity_skin_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_skin_list_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_skin_list_refresh);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerSkinListComponent.builder().chatMainComponent(getChatMainComponent())
                .skinListModule(new SkinListModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedGridLayoutManager(this, 3));
        display.addItemDecoration(new GridSpaceDecoration(3, 5, false));
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        display.setAdapter(skinListAdapter);
        skinListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                currentPosition=position;
                SkinDetailActivity.start(SkinListActivity.this, skinListAdapter.getData(position));
            }
        });
        runOnUiThread(() -> presenter.getSkinData(true));
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("皮肤中心");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    @Override
    public void onRefresh() {
        presenter.getSkinData(true);
    }

    @Override
    public void loadMore() {
        presenter.getSkinData(false);
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            for (SkinEntity item :
                    skinListAdapter.getData()) {
                if (item.getHasSelected()) {
                    item.setHasSelected(false);
                }
            }
            skinListAdapter.getData(currentPosition).setHasSelected(true);
            skinListAdapter.notifyDataSetChanged();
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SkinListActivity.class);
        activity.startActivity(intent);
    }
}
