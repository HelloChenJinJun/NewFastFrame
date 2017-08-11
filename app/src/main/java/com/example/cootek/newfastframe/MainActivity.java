package com.example.cootek.newfastframe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.baseadapter.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.RecyclerFooterViewClickListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.dagger.DaggerMainActivityComponent;
import com.example.cootek.newfastframe.dagger.MainActivityModule;
import com.example.cootek.newfastframe.mvp.MainPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MainActivity extends BaseActivity<List<Music>> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srcv_activity_main_display)
    SuperRecyclerView display;
    @BindView(R.id.riv_activity_main_bottom_image)
    RoundAngleImageView image;
    @BindView(R.id.tv_activity_main_bottom_name)
    TextView bottomName;
    @BindView(R.id.tv_activity_main_bottom_description)
    TextView bottomDescription;
    @BindView(R.id.iv_activity_main_bottom_play)
    ImageView bottomPlay;
    @BindView(R.id.iv_activity_main_bottom_previous)
    ImageView bottomPrevious;
    @BindView(R.id.iv_activity_main_bottom_next)
    ImageView bottomNext;
    @BindView(R.id.ll_activity_main_bottom_container)
    LinearLayout bottomContainer;
    @BindView(R.id.refresh_activity_main_refresh)
    SwipeRefreshLayout refresh;
    @Inject
    MainAdapter mainAdapter;
    @Inject
    MainPresenter mainPresenter;

    private LoadMoreFooterView loadMoreFooterView;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        display.setLayoutManager(new LinearLayoutManager(this));
        loadMoreFooterView = new LoadMoreFooterView(this);
        loadMoreFooterView.setBottomViewClickListener(new RecyclerFooterViewClickListener() {
            @Override
            public void onBottomViewClickListener(View view) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                display.scrollToPosition(0);
            }
        });
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerMainActivityComponent.builder().mainComponent(MainApplication.getMainComponent()).mainActivityModule(new MainActivityModule(this)).build().inject(this);
        mainAdapter.setEnableAnimator(true);
        mainAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                MusicManager.getInstance().play(MainActivity.this, mainAdapter.getIds(), position, -1, MusicIdType.NORMAL, false);
            }
        });
        display.setIAdapter(mainAdapter);
        display.post(new Runnable() {
            @Override
            public void run() {
                getData(true);
            }
        });
    }

    private void getData(boolean isRefresh) {
        mainPresenter.getAllMusic(isRefresh);
    }

    @OnClick({R.id.iv_activity_main_bottom_play, R.id.iv_activity_main_bottom_previous, R.id.iv_activity_main_bottom_next, R.id.ll_activity_main_bottom_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_main_bottom_play:
                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        boolean isPlaying = MusicManager.getInstance().isPlaying();
                        MusicManager.getInstance().playOrPause();
                        return isPlaying;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
//                            更新为暂停状态
                            Toast.makeText(MainActivity.this, "已经为停止状态", Toast.LENGTH_SHORT).show();
                        } else {
//                            更新为播放状态
                            Toast.makeText(MainActivity.this, "已经为播放状态", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.iv_activity_main_bottom_previous:
                Toast.makeText(this, "前一首", Toast.LENGTH_SHORT).show();
                MusicManager.getInstance().previous(true);
                break;
            case R.id.iv_activity_main_bottom_next:
                Toast.makeText(this, "下一首", Toast.LENGTH_SHORT).show();
                MusicManager.getInstance().next();
                break;
            case R.id.ll_activity_main_bottom_container:
                Toast.makeText(this, "展示歌词界面", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void loadMore() {
        getData(false);
    }

    @Override
    public void onRefresh() {
        getData(true);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
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
    public void updateData(List<Music> musics) {
        if (musics != null) {
            CommonLogger.e("数据到这里" + musics.size());
        }
        mainAdapter.addData(musics);
    }
}
