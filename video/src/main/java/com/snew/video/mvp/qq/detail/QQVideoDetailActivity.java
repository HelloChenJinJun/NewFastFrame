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
import com.snew.video.bean.CommonVideoBean;
import com.snew.video.bean.VideoPlayDetailBean;
import com.snew.video.dagger.qq.detail.DaggerQQVideoDetailComponent;
import com.snew.video.dagger.qq.detail.QQVideoDetailModule;
import com.snew.video.manager.VideoUpLoadManager;
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

    private SuperRecyclerView person, tag, tv, variety, subTitle;
    private TextView title, desc, score;


    @Inject
    VideoTagAdapter mVideoTagAdapter;
    @Inject
    VideoPersonAdapter mVideoPersonAdapter;

    @Inject
    VideoTVItemAdapter mVideoTVItemAdapter;
    private CommonVideoBean data;


    @Inject
    VideoVarietyAdapter mVideoVarietyAdapter;


    @Inject
    VideoSubTitleAdapter mVideoSubTitleAdapter;

    public static void start(Activity activity, CommonVideoBean commonVideoBean) {
        Intent intent = new Intent(activity, QQVideoDetailActivity.class);
        intent.putExtra(VideoUtil.DATA, commonVideoBean);
        activity.startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data = (CommonVideoBean) intent.getSerializableExtra(VideoUtil.DATA);
        if (data.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_CAMERA) {
            tv.setVisibility(View.GONE);
        }
        display.setTitle(data.getTitle())
                .setImageCover(data.getImage());
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
        data = (CommonVideoBean) getIntent().getSerializableExtra(VideoUtil.DATA);
        display.setTitle(data.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_TV || data.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_CARTOON ? data.getTitle() + " 第" + data.getPlayNumber() + "集" : data.getTitle())
                .setImageCover(data.getImage());

        if (data.getVideoType() == VideoUtil.VIDEO_TYPE_QQ_CAMERA) {
            tv.setVisibility(View.GONE);
        }
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
                data.setTitle(mVideoVarietyAdapter.getData(position).getTitle());
                display.release();
                display.setTitle(mVideoVarietyAdapter.getData(position).getTitle());
                display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
                presenter.getDetailData(mVideoVarietyAdapter.getData(position).getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
            }
        });


        mVideoTVItemAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                data.setUrl(mVideoTVItemAdapter.getData(position).getUrl());
                display.release();
                display.setTitle(data.getTitle() + " 第" + mVideoTVItemAdapter.getData(position).getTitle() + "集");
                data.setPlayNumber(Integer.parseInt(mVideoTVItemAdapter.getData(position).getTitle()));
                display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
                presenter.getDetailData(mVideoTVItemAdapter.getData(position).getUrl(), ((DefaultVideoController) display.getController()).isSwitchUrl());
            }
        });

        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(data.getTitle());
        setToolBar(toolBarOption);
        ((DefaultVideoController) display.getController()).setOnSwitchUrlListener(isSwitch -> {
            display.release();
            display.setState(DefaultVideoPlayer.PLAY_STATE_PREPARING);
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
        presenter.getDetailInfo(data.getId());
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
            if (o.getCode() == 200) {
                if (o.getExtraInfo() != null && o.getExtraInfo().equals(data.getUrl())) {
                    String url = (String) o.getData();
                    display.setUp(url, null);
                    display.start();
                    VideoUpLoadManager.getInstance().uploadVideoBean(data.getUrl(), getUpLoadTitle(), url);
                }
            } else {
                display.setState(DefaultVideoPlayer.PLAY_STATE_ERROR);
            }
        }
    }

    private String getUpLoadTitle() {
        return display.getTitle();
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
