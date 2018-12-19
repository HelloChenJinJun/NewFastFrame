package com.snew.video.mvp.qq.detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.cusotomview.GridSpaceDecoration;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.manager.video.DefaultVideoController;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.utils.DensityUtil;
import com.snew.video.R;
import com.snew.video.adapter.VideoPersonAdapter;
import com.snew.video.adapter.VideoSubTitleAdapter;
import com.snew.video.adapter.VideoTVItemAdapter;
import com.snew.video.adapter.VideoTagAdapter;
import com.snew.video.adapter.VideoVarietyAdapter;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.VideoBean;
import com.snew.video.bean.VideoPlayDetailBean;
import com.snew.video.dagger.qq.detail.DaggerQQVideoDetailComponent;
import com.snew.video.dagger.qq.detail.QQVideoDetailModule;
import com.snew.video.mvp.actor.ActorDetailInfoActivity;
import com.snew.video.util.VideoUtil;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:29
 */
public class QQVideoDetailActivity extends VideoBaseActivity<BaseBean, QQVideoDetailPresenter> {


    private DefaultVideoPlayer display;
    private String id;

    private SuperRecyclerView person, tag, tv, variety, subTitle;
    private TextView title, desc, score;


    @Inject
    VideoTagAdapter mVideoTagAdapter;
    @Inject
    VideoPersonAdapter mVideoPersonAdapter;

    @Inject
    VideoTVItemAdapter mVideoTVItemAdapter;
    private VideoBean data;


    @Inject
    VideoVarietyAdapter mVideoVarietyAdapter;


    @Inject
    VideoSubTitleAdapter mVideoSubTitleAdapter;

    public static void start(Activity activity, VideoBean data, String id) {
        Intent intent = new Intent(activity, QQVideoDetailActivity.class);
        intent.putExtra(VideoUtil.DATA, data);
        intent.putExtra(VideoUtil.VIDEO_ID, id);
        activity.startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data = (VideoBean) intent.getSerializableExtra(VideoUtil.DATA);
        id = intent.getStringExtra(VideoUtil.VIDEO_ID);
        display.setTitle(data.getTitle())
                .setImageCover(data.getImageCover());
        updateTitle(data.getTitle());
        deal();
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
        return R.layout.activity_qq_video_detail;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.dvp_activity_qq_video_detail_display);
        person = findViewById(R.id.srcv_activity_qq_video_detail_person);
        tag = findViewById(R.id.srcv_activity_qq_video_detail_tag);
        tv = findViewById(R.id.srcv_activity_qq_video_detail_tv);
        variety = findViewById(R.id.srcv_activity_qq_video_detail_variety);
        title = findViewById(R.id.tv_activity_qq_video_detail_title);
        subTitle = findViewById(R.id.srcv_activity_qq_video_detail_sub_title);
        desc = findViewById(R.id.tv_activity_qq_video_detail_desc);
        score = findViewById(R.id.tv_activity_qq_video_detail_score);
    }


    @Override
    protected void initData() {
        DaggerQQVideoDetailComponent.builder().qQVideoDetailModule(new QQVideoDetailModule(this))
                .videoComponent(getComponent()).build().inject(this);
        data = (VideoBean) getIntent().getSerializableExtra(VideoUtil.DATA);
        id = getIntent().getStringExtra(VideoUtil.VIDEO_ID);
        display.setTitle(data.getTitle())
                .setImageCover(data.getImageCover());
        person.setNestedScrollingEnabled(false);
        tag.setNestedScrollingEnabled(false);
        tv.setNestedScrollingEnabled(false);
        variety.setNestedScrollingEnabled(false);
        subTitle.setNestedScrollingEnabled(false);

        subTitle.setLayoutManager(new WrappedGridLayoutManager(this, 2));
        subTitle.addItemDecoration(new GridSpaceDecoration(2, 0, DensityUtil.toDp(10), false));
        subTitle.setAdapter(mVideoSubTitleAdapter);

        person.setLayoutManager(new WrappedLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL));
        person.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(15)));
        person.setAdapter(mVideoPersonAdapter);
        mVideoPersonAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ActorDetailInfoActivity.start(QQVideoDetailActivity.this, mVideoPersonAdapter.getData(position).getUrl());
            }
        });

        tag.setLayoutManager(new WrappedLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL));
        tag.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
        tag.setAdapter(mVideoTagAdapter);

        tv.setLayoutManager(new WrappedGridLayoutManager(this, 5));
        tv.addItemDecoration(new GridSpaceDecoration(5, DensityUtil.toDp(5), true));
        tv.setAdapter(mVideoTVItemAdapter);

        variety.setLayoutManager(new WrappedLinearLayoutManager(this));
        variety.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
        variety.setAdapter(mVideoVarietyAdapter);
        mVideoVarietyAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                data.setUrl(mVideoVarietyAdapter.getData(position).getUrl());
                display.release();
                display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
                presenter.getDetailData(mVideoVarietyAdapter.getData(position).getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
            }
        });


        mVideoTVItemAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                data.setUrl(mVideoTVItemAdapter.getData(position).getUrl());
                display.release();
                display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
                presenter.getDetailData(mVideoTVItemAdapter.getData(position).getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
            }
        });

        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(data.getTitle());
        setToolBar(toolBarOption);
        ((DefaultVideoController) display.getController()).setOnSwitchUrlListener(isSwitch -> {
            presenter.getDetailData(data.getUrl(), isSwitch);
            return false;
        });
        runOnUiThread(this::deal);
    }

    private void deal() {
        display.release();
        display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
        if (data.getUrl() != null) {
            presenter.getDetailData(data.getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
        }
        presenter.getDetailInfo(id);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (display.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE
                || display.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_PAUSE) {
            display.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        display.pause();
    }

    @Override
    public void updateData(BaseBean o) {
        if (o.getType() == VideoUtil.BASE_TYPE_VIDEO_DETAIL_INFO) {
            VideoPlayDetailBean videoPlayDetailBean = (VideoPlayDetailBean) o.getData();
            title.setText(videoPlayDetailBean.getTitle());
            //            subTitle.setText(videoPlayDetailBean.getSubTitle());
            mVideoSubTitleAdapter.refreshData(videoPlayDetailBean.getSubTitleList());
            score.setText(videoPlayDetailBean.getScore());
            desc.setText(videoPlayDetailBean.getDesc());
            mVideoTagAdapter.refreshData(videoPlayDetailBean.getTagList());
            mVideoPersonAdapter.refreshData(videoPlayDetailBean.getVideoPlayPeople());
            mVideoTVItemAdapter.refreshData(videoPlayDetailBean.getTVPlayBeans());
            mVideoVarietyAdapter.refreshData(videoPlayDetailBean.getVarietyVideoDetailBeans());
            if (data.getUrl() == null && videoPlayDetailBean.getVarietyVideoDetailBeans() != null) {
                data.setUrl(videoPlayDetailBean.getVarietyVideoDetailBeans().get(0).getUrl());
                presenter.getDetailData(data.getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
            }
        } else if (o.getType() == VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL) {
            if (o.getExtraInfo() != null && o.getExtraInfo().equals(data.getUrl())) {
                String url = (String) o.getData();
                display.setUp(url, null);
                display.start();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        ListVideoManager.getInstance().release();
    }
}
