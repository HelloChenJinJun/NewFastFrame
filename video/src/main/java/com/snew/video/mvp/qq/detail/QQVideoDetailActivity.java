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
import com.snew.video.adapter.VideoTVItemAdapter;
import com.snew.video.adapter.VideoTagAdapter;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.VideoBean;
import com.snew.video.bean.VideoPlayDetailBean;
import com.snew.video.dagger.qq.detail.DaggerQQVideoDetailComponent;
import com.snew.video.dagger.qq.detail.QQVideoDetailModule;
import com.snew.video.util.VideoUtil;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:29
 */
public class QQVideoDetailActivity extends VideoBaseActivity<BaseBean, QQVideoDetailPresenter> implements DefaultVideoController.OnItemClickListener {


    private DefaultVideoPlayer display;
    private String id;

    private SuperRecyclerView person, tag, tv;
    private TextView title, subTitle, desc, score;


    @Inject
    VideoTagAdapter mVideoTagAdapter;
    @Inject
    VideoPersonAdapter mVideoPersonAdapter;

    @Inject
    VideoTVItemAdapter mVideoTVItemAdapter;
    private VideoBean data;

    public static void start(Activity activity, VideoBean data) {
        Intent intent = new Intent(activity, QQVideoDetailActivity.class);
        intent.putExtra(VideoUtil.DATA, data);
        activity.startActivity(intent);
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
        title = findViewById(R.id.tv_activity_qq_video_detail_title);
        subTitle = findViewById(R.id.tv_activity_qq_video_detail_sub_title);
        desc = findViewById(R.id.tv_activity_qq_video_detail_desc);
        score = findViewById(R.id.tv_activity_qq_video_detail_score);

    }


    @Override
    protected void initData() {
        DaggerQQVideoDetailComponent.builder().qQVideoDetailModule(new QQVideoDetailModule(this))
                .videoComponent(getComponent()).build().inject(this);
        data = (VideoBean) getIntent().getSerializableExtra(VideoUtil.DATA);
        id = VideoUtil.getIdFromUrl(data.getUrl());
        display.setTitle(data.getTitle())
                .setImageCover(data.getImageCover())
                .setUp(data.getUrl(), null);
        ((DefaultVideoController) display.getController()).setOnItemClickListener(this);
        person.setNestedScrollingEnabled(false);
        tag.setNestedScrollingEnabled(false);
        tv.setNestedScrollingEnabled(false);


        person.setLayoutManager(new WrappedLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL));
        person.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(10)));
        person.setAdapter(mVideoPersonAdapter);

        tag.setLayoutManager(new WrappedLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL));
        tag.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
        tag.setAdapter(mVideoTagAdapter);

        tv.setLayoutManager(new WrappedGridLayoutManager(this, 5));
        tv.addItemDecoration(new GridSpaceDecoration(5, DensityUtil.toDp(5), true));
        tv.setAdapter(mVideoTVItemAdapter);
        mVideoTVItemAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                data.setUrl(mVideoTVItemAdapter.getData(position).getUrl());
                ListVideoManager.getInstance().updateUrl(data.getUrl());
            }
        });

        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(data.getTitle());
        setToolBar(toolBarOption);
        runOnUiThread(() -> {
            display.start();
            presenter.getDetailInfo(id);
        });
    }

    @Override
    public void updateData(BaseBean o) {
        if (o.getType() == VideoUtil.BASE_TYPE_VIDEO_DETAIL_INFO) {
            VideoPlayDetailBean videoPlayDetailBean = (VideoPlayDetailBean) o.getData();
            title.setText(videoPlayDetailBean.getTitle());
            subTitle.setText(videoPlayDetailBean.getSubTitle());
            score.setText(videoPlayDetailBean.getScore());
            desc.setText(videoPlayDetailBean.getDesc());
            mVideoTagAdapter.refreshData(videoPlayDetailBean.getTagList());
            mVideoPersonAdapter.refreshData(videoPlayDetailBean.getVideoPlayPeople());
            mVideoTVItemAdapter.refreshData(videoPlayDetailBean.getTVPlayBeans());

        }

    }

    @Override
    public boolean onStartClick(View view, String url) {
        if (url.contains("html")) {
            presenter.getDetailData(url);
            return false;
        } else {
            return true;
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
