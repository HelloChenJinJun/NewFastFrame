package com.example.cootek.newfastframe.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.SingerInfoAdapter;
import com.example.cootek.newfastframe.dagger.singerinfo.DaggerSingerInfoComponent;
import com.example.cootek.newfastframe.dagger.singerinfo.SingerInfoModule;
import com.example.cootek.newfastframe.mvp.singerinfo.SingerInfoPresenter;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SingerInfoActivity extends MusicBaseActivity<List<MusicPlayBean>, SingerInfoPresenter> implements View.OnClickListener {
    private SuperRecyclerView display;
    @Inject
    SingerInfoAdapter singerInfoAdapter;
    private RoundAngleImageView avatar;
    private String avatarUrl;
    private String tingId;


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
        return R.layout.activity_singer_info;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_singer_info_display);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("歌手");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        tingId = getIntent().getStringExtra(MusicUtil.TING_UID);
        DaggerSingerInfoComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .singerInfoModule(new SingerInfoModule(this)).build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setAdapter(singerInfoAdapter);
        display.addHeaderView(getHeaderView());
        avatarUrl = getIntent().getStringExtra(MusicUtil.SINGER_AVATAR);
        if (avatarUrl != null) {
            BaseApplication.getAppComponent().getImageLoader().loadImage(this,
                    new GlideImageLoaderConfig.Builder().imageView(avatar).url(avatarUrl).build());
        }
        singerInfoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MusicManager.getInstance().play(singerInfoAdapter.getData(), position, getSharedPreferences(MusicUtil.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(MusicUtil.PLAY_MODE, 0));
            }
        });

        display.post(new Runnable() {
            @Override
            public void run() {
                presenter.getLocalSingerMusic(tingId);
            }
        });
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_activity_singer_info_header, null);
        avatar = (RoundAngleImageView) headerView.findViewById(R.id.riv_view_activity_singer_info_header_avatar);
        headerView.findViewById(R.id.rl_view_activity_singer_info_header_index_container).setOnClickListener(this);
        return headerView;
    }

    @Override
    public void updateData(List<MusicPlayBean> musicPlayBeen) {
        singerInfoAdapter.addData(musicPlayBeen);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_view_activity_singer_info_header_index_container) {
            Intent intent = new Intent(SingerInfoActivity.this, SongListActivity.class);
            intent.putExtra(MusicUtil.FROM, MusicUtil.FROM_SINGER);
            intent.putExtra(MusicUtil.TING_UID, tingId);
            startActivity(intent);
        }
    }
}
