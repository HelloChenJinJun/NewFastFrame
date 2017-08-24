package com.example.cootek.newfastframe;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.Closeable;
import com.example.commonlibrary.baseadapter.swipeview.OnSwipeMenuItemClickListener;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenu;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuCreator;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuItem;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.cusotomview.CustomPopWindow;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.imageloader.GlideImageLoaderConfig;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.cootek.newfastframe.api.DownLoadMusicBean;
import com.example.cootek.newfastframe.dagger.BottomFragmentModule;
import com.example.cootek.newfastframe.dagger.DaggerBottomFragmentComponent;
import com.example.cootek.newfastframe.lrc.LrcRow;
import com.example.cootek.newfastframe.lrc.LrcView;
import com.example.cootek.newfastframe.mvp.BottomPresenter;
import com.example.cootek.newfastframe.mvp.IBottomView;
import com.example.cootek.newfastframe.slidingpanel.SlidingPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class BottomFragment extends BaseFragment<DownLoadMusicBean, BottomPresenter> implements SlidingPanelLayout.PanelSlideListener, IBottomView<DownLoadMusicBean>, LrcView.OnSeekToListener, View.OnClickListener {


    @BindView(R.id.riv_fragment_bottom_album)
    RoundAngleImageView album;
    @BindView(R.id.tv_fragment_bottom_song_name)
    TextView songName;
    @BindView(R.id.tv_fragment_bottom_artist_name)
    TextView artistName;
    @BindView(R.id.iv_fragment_bottom_next)
    ImageView next;
    @BindView(R.id.iv_fragment_bottom_previous)
    ImageView previous;
    @BindView(R.id.iv_fragment_bottom_play)
    ImageView playOrPause;
    @BindView(R.id.iv_fragment_bottom_mode)
    ImageView playMode;
    @BindView(R.id.iv_fragment_bottom_list)
    ImageView list;
    @BindView(R.id.rl_fragment_bottom_icon_container)
    RelativeLayout iconContainer;
    @BindView(R.id.sb_fragment_bottom_seek)
    SeekBar seekBar;
    @BindView(R.id.rl_fragment_bottom_container)
    RelativeLayout container;
    @BindView(R.id.lv_fragment_bottom_lrc)
    LrcView lrcView;
    @BindView(R.id.iv_fragment_bottom_bg)
    ImageView bg;
    @BindView(R.id.ll_fragment_bottom_seek_container)
    LinearLayout seekContainer;
    @BindView(R.id.tv_fragment_bottom_start_time)
    TextView startTime;
    @BindView(R.id.tv_fragment_bottom_end_time)
    TextView endTime;
    @BindView(R.id.lv_fragment_bottom_lrc_bottom)
    LrcView bottomLrc;
    @BindView(R.id.iv_fragment_bottom_back)
    ImageView back;
    @BindView(R.id.iv_fragment_bottom_comment)
    ImageView comment;
    @BindView(R.id.cl_fragment_bottom_comment_layout)
    CommentLayout commentLayout;

    @BindView(R.id.rl_fragment_bottom_lrc_container)
    RelativeLayout lrcContainer;
    private IntEvaluator intEvaluator;
    private int endSongName;
    private int endArtistName;
    private int endPlay;
    private FloatEvaluator floatEvaluator;
    private int screenHeight;
    private Runnable progressRun;
    private SlidingPanelLayout slidingUpPanelLayout;
    private CustomPopWindow customPopWindow;
    private int mode = 0;
    private boolean isShow = true;

    @Override
    public void updateData(DownLoadMusicBean o) {

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
    protected int getContentLayout() {

        return R.layout.fragment_bottom;
    }

    @Override
    protected void initView() {
        slidingUpPanelLayout = (SlidingPanelLayout) root.getParent().getParent();
        slidingUpPanelLayout.addPanelSlideListener(this);
        lrcView.setOnSeekToListener(this);
        lrcContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonLogger.e("点击容器");
                updateLrcView();
            }
        });
        lrcView.setOnLrcClickListener(new LrcView.OnLrcClickListener() {
            @Override
            public void onClick() {
                CommonLogger.e("点击容器");
                updateLrcView();
            }
        });
    }

    private void updateLrcView() {
        isShow = !isShow;
        if (lrcView.getVisibility() == View.VISIBLE) {
            lrcView.setVisibility(View.INVISIBLE);
            bottomLrc.setVisibility(View.VISIBLE);
        } else {
            lrcView.setVisibility(View.VISIBLE);
            bottomLrc.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initData() {
        DaggerBottomFragmentComponent.builder().mainComponent(MainApplication.getMainComponent())
                .bottomFragmentModule(new BottomFragmentModule(this)).build().inject(this);
        progressRun = new Runnable() {
            @Override
            public void run() {
                int progress = (int) MusicManager.getInstance().getCurrentProgress();
                seekBar.setProgress(progress);
                startTime.setText(MusicUtil.makeLrcTime(progress));
                if (MusicManager.getInstance().isPlaying()) {
                    seekBar.postDelayed(this, 50);
                } else {
                    seekBar.removeCallbacks(this);
                }
            }
        };
        presenter.registerEvent(MusicStatusEvent.class, new Consumer<MusicStatusEvent>() {
            @Override
            public void accept(@NonNull MusicStatusEvent musicStatusEvent) throws Exception {
                if (musicStatusEvent != null && musicStatusEvent.getCurrentStatus() != null) {
                    switch (musicStatusEvent.getCurrentStatus()) {
                        case MusicStatusEvent.META_CHANGED:
                            updateContent(musicStatusEvent.getMusicContent());
                            break;
                        case MusicStatusEvent.PLAYSTATE_CHANGED:
                            CommonLogger.e("这里playStatus改变");
                            if (musicStatusEvent.getMusicContent().isPlaying()) {
                                playOrPause.setImageResource(R.drawable.ic_pause_black_24dp);
                            } else {
                                playOrPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            }
                            updateProgress();
                            break;
                        case MusicStatusEvent.REFRESH_CHANGED:
                            CommonLogger.e("接收到刷新消息啦啦");
                            reLoadMusic();
                            break;
                        case MusicStatusEvent.BUFFER_UPDATE_CHANGED:
                            CommonLogger.e("这里到了吗");
//                            int progress = musicStatusEvent.getMusicContent().getSecondProgress();
                            seekBar.setSecondaryProgress(musicStatusEvent.getMusicContent().getSecondProgress());
//                            重置
//                            if (progress == seekBar.getMax()) {
//                                CommonLogger.e("重置了吗?");
//                                seekBar.setSecondaryProgress(0);
//                            }
                            break;
                    }
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isShow) {
                    lrcView.seekTo(progress, true, fromUser);
                } else {
                    bottomLrc.seekTo(progress, true, fromUser);
                }
                startTime.setText(MusicUtil.makeLrcTime(progress));
                if (fromUser) {
                    seekBar.removeCallbacks(progressRun);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setThumb(seekBar.getResources().getDrawable(R.drawable.thumb_music_pressed));
                if (MusicManager.getInstance().isPlaying()) {
                    MusicManager.getInstance().playOrPause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                    释放手时调用
                seekBar.setThumb(seekBar.getResources().getDrawable(R.drawable.thumb_music_normal));
                MusicManager.getInstance().seekTo(seekBar.getProgress());
                if (!MusicManager.getInstance().isPlaying()) {
                    MusicManager.getInstance().playOrPause();
                }
                seekBar.postDelayed(progressRun, 5);
            }
        });
        seekBar.setPadding(0, 0, 0, 0);
    }

    private void updateContent(MusicStatusEvent.MusicContent content) {
        updatePlayStatus(content.isPlaying());
        if (content.getSongName() != null) {
            updateSongName(content.getSongName());
        }
        if (content.getArtistName() != null) {
            updateArtistName(content.getArtistName());
        }
        if (content.getAlbumUrl() != null) {
            updateAlbum(content.getAlbumUrl());
        }
        updatePlayMode(content.getMode());
        updateMusicContent(new File(MusicUtil.getLyricPath(content.getId())));
        CommonLogger.e("更新最大进度" + content.getMaxProgress());
        updateMaxProgress((int) content.getMaxProgress());

        updateProgress();
    }

    @Override
    protected void updateView() {
        intEvaluator = new IntEvaluator();
        floatEvaluator = new FloatEvaluator();
        int sameWidth = playOrPause.getLayoutParams().width;
        int screenWidth = DensityUtil.getScreenWidth(getContext());
        screenHeight = DensityUtil.getScreenHeight(getContext());
        int margin = ((RelativeLayout.LayoutParams) playOrPause.getLayoutParams()).rightMargin;
        endPlay = screenWidth / 2 - (sameWidth * 2 + margin) + sameWidth / 2;
    }

    private void reLoadMusic() {
        presenter.refresh();
    }

    public static Fragment newInstance() {
        return new BottomFragment();
    }


    private CustomPopWindow playModeWindow;


    @OnClick({R.id.iv_fragment_bottom_next, R.id.iv_fragment_bottom_previous, R.id.iv_fragment_bottom_play, R.id.iv_fragment_bottom_mode, R.id.iv_fragment_bottom_list, R.id.iv_fragment_bottom_back,
            R.id.iv_fragment_bottom_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fragment_bottom_next:
                presenter.next();
                break;
            case R.id.iv_fragment_bottom_previous:
                presenter.previous();
                break;
            case R.id.iv_fragment_bottom_play:
                presenter.playOrPause();
                break;
            case R.id.iv_fragment_bottom_mode:
                if (playModeWindow == null) {
                    View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_bottom_pop_window_mode, null);
                    LinearLayout normal = (LinearLayout) contentView.findViewById(R.id.ll_view_fragment_bottom_pop_window_mode_normal);
                    LinearLayout shuffle = (LinearLayout) contentView.findViewById(R.id.ll_view_fragment_bottom_pop_window_mode_shuffle);
                    LinearLayout repeat = (LinearLayout) contentView.findViewById(R.id.ll_view_fragment_bottom_pop_window_mode_repeat);
                    normal.setOnClickListener(this);
                    shuffle.setOnClickListener(this);
                    repeat.setOnClickListener(this);
                    playModeWindow = new CustomPopWindow.Builder().contentView(contentView).parentView(view).build();
                    int[] result = DensityUtil.getViewWidthAndHeight(contentView);
                    playModeWindow.show(DensityUtil.getScreenWidth(contentView.getContext()) - result[0], 20);
                } else {
                    playModeWindow.show(DensityUtil.getScreenWidth(getContext()) - DensityUtil.getViewWidthAndHeight(playModeWindow.getContentView())[0], 20);
                }
                break;
            case R.id.iv_fragment_bottom_list:
                if (customPopWindow == null) {
                    View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_bottom_pop_window, null);
                    playNum = (TextView) contentView.findViewById(R.id.tv_view_fragment_bottom_pop_window_num);
                    SuperRecyclerView popDisplay = (SuperRecyclerView) contentView.findViewById(R.id.srcv_view_fragment_bottom_pop_window_display);
                    popDisplay.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
                    popupWindowAdapter = new PopupWindowAdapter();
                    popupWindowAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            customPopWindow.dismiss();
                            MusicManager.getInstance().play(popupWindowAdapter.getData(), position, mode);
                        }

                        @Override
                        public void onItemChildClick(int position, View view, int id) {
                            popupWindowAdapter.removeData(position);
                            presenter.remove(position);
                        }
                    });
                    updatePopData();
                    popDisplay.setAdapter(popupWindowAdapter);
                    customPopWindow = new CustomPopWindow.Builder().parentView(view).contentView(contentView).build();
                    customPopWindow.show(20, 20);
                } else {
                    updatePopData();
                    customPopWindow.show(20, 20);
                }
                break;
            case R.id.iv_fragment_bottom_back:
                CommonLogger.e("这里收缩");
                slidingUpPanelLayout.setPanelState(SlidingPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.iv_fragment_bottom_comment:
                CommonLogger.e("点击啦啦啦");
                commentLayout.setData(getData());
                commentLayout.start();
                break;
        }
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("测试" + i);
        }
        return list;
    }


    private TextView playNum;
    private PopupWindowAdapter popupWindowAdapter;
    private long[] lastIdList;

    private void updatePopData() {
        long[] idList = MusicManager.getInstance().getQueue();

        if (idList != null && idList.length > 0) {
            if (Arrays.equals(idList, lastIdList)) {
                return;
//                数据一样不更新
            }
            playNum.setText("播放队列   " + idList.length);
            popupWindowAdapter.clearAllData();
            List<MusicPlayBean> list = new ArrayList<>();
            MusicPlayBeanDao musicPlayBeanDao = MainApplication.getMainComponent().getDaoSession().getMusicPlayBeanDao();
            for (long anIdList : idList) {
                list.addAll(musicPlayBeanDao.queryBuilder().where(MusicPlayBeanDao.Properties.SongId.eq(anIdList))
                        .build().list());
            }
            popupWindowAdapter.addData(list);
        } else {
            popupWindowAdapter.clearAllData();
            popupWindowAdapter.notifyDataSetChanged();
        }
        lastIdList = idList;
    }


    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        int result = intEvaluator.evaluate(slideOffset, DensityUtil.dip2px(getContext(), 50), DensityUtil.getScreenWidth(getContext()));
        album.getLayoutParams().width = result;
        album.getLayoutParams().height = result;
        album.requestLayout();
        songName.setTranslationX(intEvaluator.evaluate(slideOffset, 0, endSongName));
        artistName.setTranslationX(intEvaluator.evaluate(slideOffset, 0, endArtistName));
        next.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.dip2px(getContext(), 15)));
        playMode.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.dip2px(getContext(), 30)));
        previous.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.dip2px(getContext(), 15)));
        list.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.dip2px(getContext(), 30)));
        playOrPause.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay));
        playMode.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        list.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        bg.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        startTime.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        endTime.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        lrcView.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        bottomLrc.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        back.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        comment.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        iconContainer.setTranslationY(intEvaluator.evaluate(slideOffset, 0, screenHeight - (2 * iconContainer.getHeight())));
        comment.setTranslationY(intEvaluator.evaluate(slideOffset, 0, screenHeight - (3 * iconContainer.getHeight())));
        bottomLrc.setTranslationY(intEvaluator.evaluate(slideOffset, 0, (screenHeight - (4 * iconContainer.getHeight()))));
        seekContainer.setTranslationY(intEvaluator.evaluate(slideOffset, 0, (int) (screenHeight - (2.5 * iconContainer.getHeight()))));
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingPanelLayout.PanelState previousState, SlidingPanelLayout.PanelState newState) {
        if (previousState == SlidingPanelLayout.PanelState.COLLAPSED && newState == SlidingPanelLayout.PanelState.DRAGGING) {
            lrcContainer.setVisibility(View.VISIBLE);
            playMode.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            if (isShow) {
                bottomLrc.setVisibility(View.INVISIBLE);
                lrcView.setVisibility(View.VISIBLE);
            } else {
                bottomLrc.setVisibility(View.VISIBLE);
                lrcView.setVisibility(View.INVISIBLE);
            }
            comment.setVisibility(View.VISIBLE);
            bg.setVisibility(View.VISIBLE);
            startTime.setVisibility(View.VISIBLE);
            endTime.setVisibility(View.VISIBLE);
            seekBar.setPadding(10, 0, 10, 0);
        } else if (previousState == SlidingPanelLayout.PanelState.DRAGGING && newState == SlidingPanelLayout.PanelState.COLLAPSED) {
            playMode.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            commentLayout.stop();
            comment.setVisibility(View.INVISIBLE);
            lrcContainer.setVisibility(View.INVISIBLE);
            if (!isShow) {
                bottomLrc.setVisibility(View.INVISIBLE);
                lrcView.setVisibility(View.VISIBLE);
            } else {
                bottomLrc.setVisibility(View.VISIBLE);
                lrcView.setVisibility(View.INVISIBLE);
            }
            if (startTime.getVisibility() == View.VISIBLE) {
                startTime.setVisibility(View.GONE);
            }
            if (endTime.getVisibility() == View.VISIBLE) {
                endTime.setVisibility(View.GONE);
            }
            seekBar.setPadding(0, 0, 0, 0);
            bg.setVisibility(View.INVISIBLE);
            slidingUpPanelLayout.setTouchEnabled(true);
        } else if (previousState == SlidingPanelLayout.PanelState.EXPANDED && newState == SlidingPanelLayout.PanelState.DRAGGING) {
            startTime.setVisibility(View.GONE);
            endTime.setVisibility(View.GONE);
            seekBar.setPadding(0, 0, 0, 0);
        } else if (previousState == SlidingPanelLayout.PanelState.DRAGGING && newState == SlidingPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setTouchEnabled(false);
        }
    }


    public void updateMusicContent(File file) {
        if (file.exists()) {
            List<LrcRow> result = MusicUtil.parseLrcContent(file);
            if (result != null && result.size() > 0) {
                lrcView.setLrcRows(result);
                bottomLrc.setLrcRows(result);
                if (isShow) {
                    lrcView.setVisibility(View.VISIBLE);
                    bottomLrc.setVisibility(View.INVISIBLE);
                } else {
                    lrcView.setVisibility(View.INVISIBLE);
                    bottomLrc.setVisibility(View.VISIBLE);
                }
                CommonLogger.e("获取得到了歌词啦啦");
            } else {
                CommonLogger.e("歌词为空");
            }
        } else {
            CommonLogger.e("文件不存在？");
            lrcView.reset();
            bottomLrc.reset();
            lrcView.setVisibility(View.INVISIBLE);
            bottomLrc.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateMaxProgress(int max) {
        if (seekBar.getMax() != 0 && seekBar.getMax() != max) {
            seekBar.setSecondaryProgress(max);
            seekBar.setMax(max);
            endTime.setText(MusicUtil.makeLrcTime(max));
        }
    }

    @Override
    public void updatePlayStatus(boolean isPlaying) {
        if (isPlaying) {
            playOrPause.setImageResource(R.drawable.ic_pause_black_24dp);
            seekBar.post(progressRun);
        } else {
            playOrPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            seekBar.removeCallbacks(progressRun);
        }
    }

    @Override
    public void updateAlbum(String uri) {
        if (getContext() != null) {
            MainApplication.getAppComponent().getImageLoader().loadImage(getContext(), new GlideImageLoaderConfig.Builder().imageView(bg)
                    .url(uri)
                    .bitmapTransformation(new BlurTransformation(getContext())).build());
            MainApplication.getAppComponent().getImageLoader().loadImage(getContext(), new GlideImageLoaderConfig.Builder()
                    .imageView(album).url(uri).build());
        }
    }

    @Override
    public void updateProgress() {
        seekBar.postDelayed(progressRun, 10);
    }

    @Override
    public void updateSongName(String name) {
        songName.setText(name);
        Rect bound = new Rect();
        songName.getPaint().getTextBounds(songName.getText().toString(), 0, songName.getText().length(), bound);
        endSongName = (DensityUtil.getScreenWidth(getContext()) - bound.width()) / 2 - DensityUtil.dip2px(getContext(), 80);
    }

    @Override
    public void updateArtistName(String name) {
        artistName.setText(name);
        Rect bound = new Rect();
        artistName.getPaint().getTextBounds(artistName.getText().toString(), 0, artistName.getText().length(), bound);
        endArtistName = (DensityUtil.getScreenWidth(getContext()) - bound.width()) / 2 - DensityUtil.dip2px(getContext(), 80);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSeekTo(int progress) {
        MusicManager.getInstance().seekTo(progress);
    }

    @Override
    public void onClick(View v) {
        int currentMode;
        if (v.getId() == R.id.ll_view_fragment_bottom_pop_window_mode_repeat) {
            currentMode = MusicService.MODE_LOOP;
            playMode.setImageResource(R.drawable.ic_repeat_one_white_24dp);
        } else if (v.getId() == R.id.ll_view_fragment_bottom_pop_window_mode_normal) {
            currentMode = MusicService.MODE_NORMAL;
            playMode.setImageResource(R.drawable.ic_compare_arrows_white_24dp);
        } else {
            currentMode = MusicService.MODE_SHUFFLE;
            playMode.setImageResource(R.drawable.ic_shuffle_white_24dp);
        }
        playModeWindow.dismiss();
        updatePlayMode(currentMode);
    }

    private void updatePlayMode(int currentMode) {
        if (currentMode != mode) {
            mode = currentMode;
            presenter.setMode(mode);
        }
    }
}
