package com.example.cootek.newfastframe.mvp.lrc;

import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.FileUtil;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.dagger.lrc.DaggerLrcListFragmentComponent;
import com.example.cootek.newfastframe.dagger.lrc.LrcListFragmentModule;
import com.example.cootek.newfastframe.event.ProgressEvent;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.example.cootek.newfastframe.view.lrc.LrcRow;
import com.example.cootek.newfastframe.view.lrc.LrcView;

import java.io.File;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     17:38
 */
public class LrcListFragment extends MusicBaseFragment<Object, LrcListPresenter> implements LrcView.OnSeekToListener {

    private TextView error;
    private LrcView view;


    public static LrcListFragment newInstance() {
        return new LrcListFragment();
    }

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
        return R.layout.fragment_lrc_list;
    }

    @Override
    protected void initView() {
        error = findViewById(R.id.tv_fragment_lrc_list_error);
        view = findViewById(R.id.lv_fragment_lrc_list_display);
        view.setOnSeekToListener(this);

    }


    @Override
    protected void initData() {
        DaggerLrcListFragmentComponent.builder().lrcListFragmentModule(new LrcListFragmentModule(this))
                .mainComponent(getMainComponent()).build().inject(this);
        addDisposable(RxBusManager.getInstance().registerEvent(ProgressEvent.class, progressEvent -> {
            if (view != null && view.getVisibility() == View.VISIBLE && getUserVisibleHint()) {
                view.seekTo(progressEvent.getProgress(), true, false);
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(PlayStateEvent.class, playStateEvent -> {
            if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARED) {
                getData();
            }
        }));
    }

    private void getData() {
        bean = MusicPlayerManager.getInstance().getMusicPlayBean();
        if (bean != null) {
            updateLrcContent(bean);
        }
    }


    private MusicPlayBean bean;

    @Override
    protected void updateView() {
        if (bean == null) {
            getData();
        }
    }

    private void updateLrcContent(MusicPlayBean musicPlayBean) {
        if (!musicPlayBean.getIsLocal()) {
            if (FileUtil.isFileExist(MusicUtil.getLyricPath(musicPlayBean.getSongId()))) {
                updateMusicContent(new File(MusicUtil.getLyricPath(musicPlayBean.getSongId())));
            } else {
                presenter.getLrcContent(musicPlayBean.getSongId(), musicPlayBean.getLrcUrl());
            }
        }
    }

    private void updateMusicContent(File file) {
        if (file.exists()) {
            List<LrcRow> result = MusicUtil.parseLrcContent(file);
            if (result != null && result.size() > 0) {
                view.setLrcRows(result);
                view.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                file.delete();
                view.reset();
            }
        } else {
            view.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            view.reset();
        }
    }

    @Override
    public void updateData(Object o) {
        if (o instanceof Long) {
            Long songId = (Long) o;
            if (bean != null && songId.equals(bean.getSongId())) {
                updateMusicContent(new File(MusicUtil.getLyricPath(songId)));
            }
        }
    }

    @Override
    public void onSeekTo(int progress) {
        MusicManager.getInstance().seekTo(progress);
    }
}
