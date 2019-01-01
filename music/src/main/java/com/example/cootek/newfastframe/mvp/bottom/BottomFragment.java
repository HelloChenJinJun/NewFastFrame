package com.example.cootek.newfastframe.mvp.bottom;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.manager.music.PlayData;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.FileUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.dagger.bottom.BottomFragmentModule;
import com.example.cootek.newfastframe.dagger.bottom.DaggerBottomFragmentComponent;
import com.example.cootek.newfastframe.event.DragEvent;
import com.example.cootek.newfastframe.event.ProgressEvent;
import com.example.cootek.newfastframe.event.ServiceStateEvent;
import com.example.cootek.newfastframe.mvp.lrc.LrcListFragment;
import com.example.cootek.newfastframe.mvp.songdetail.SongDetailFragment;
import com.example.cootek.newfastframe.mvp.main.MainActivity;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.example.cootek.newfastframe.view.lrc.LrcRow;
import com.example.cootek.newfastframe.view.lrc.LrcView;
import com.example.cootek.newfastframe.view.slide.SlidingPanelLayout;
import com.nineoldandroids.view.ViewHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class BottomFragment extends MusicBaseFragment<Object, BottomPresenter> implements SlidingPanelLayout.PanelSlideListener, LrcView.OnSeekToListener, View.OnClickListener {


    private RoundAngleImageView album;
    private TextView songName;
    private TextView artistName;
    private RoundAngleImageView next;
    private RoundAngleImageView previous;
    private RoundAngleImageView playOrPause;
    private RoundAngleImageView playMode;
    private RoundAngleImageView list;
    private RelativeLayout iconContainer;
    private SeekBar seekBar;
    private ImageView bg;
    private LinearLayout seekContainer;
    private TextView startTime;
    private TextView endTime;
    private LrcView bottomLrc;
    //    private ImageView back;
    private TextView comment;
    private IntEvaluator intEvaluator;
    private int endSongName;
    private int endArtistName;
    private int endPlay;
    private FloatEvaluator floatEvaluator;
    private int screenHeight;
    private SlidingPanelLayout slidingUpPanelLayout;
    private int mode = 0;
    private SharedPreferences sharedPreferences;
    private int screenWidth;
    private int playMaxSize = 60;
    private int playMinSize = 40;
    private Disposable disposable;
    private MusicPlayBean musicPlayBean;
    private ObjectAnimator mRotateAnimator;
    private WrappedViewPager display;


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
        return R.layout.fragment_bottom;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.wvp_fragment_bottom_display);
        comment = findViewById(R.id.tv_fragment_bottom_comment);
        //        back = findViewById(R.id.iv_fragment_bottom_back);
        bottomLrc = findViewById(R.id.lv_fragment_bottom_lrc_bottom);
        bottomLrc.setTouchEnable(false);
        endTime = findViewById(R.id.tv_fragment_bottom_end_time);
        startTime = findViewById(R.id.tv_fragment_bottom_start_time);
        seekContainer = findViewById(R.id.ll_fragment_bottom_seek_container);
        bg = findViewById(R.id.iv_fragment_bottom_bg);
        seekBar = findViewById(R.id.sb_fragment_bottom_seek);
        iconContainer = findViewById(R.id.rl_fragment_bottom_icon_container);
        list = findViewById(R.id.riv_fragment_bottom_list);
        playMode = findViewById(R.id.riv_fragment_bottom_mode);
        playOrPause = findViewById(R.id.riv_fragment_bottom_play);
        previous = findViewById(R.id.riv_fragment_bottom_previous);
        next = findViewById(R.id.riv_fragment_bottom_next);
        artistName = findViewById(R.id.tv_fragment_bottom_artist_name);
        album = findViewById(R.id.riv_fragment_bottom_album);
        songName = findViewById(R.id.tv_fragment_bottom_song_name);
        next.setOnClickListener(this);
        //        back.setOnClickListener(this);
        previous.setOnClickListener(this);
        playOrPause.setOnClickListener(this);
        playMode.setOnClickListener(this);
        list.setOnClickListener(this);
        comment.setOnClickListener(this);
        slidingUpPanelLayout = ((MainActivity) getActivity()).getSlidingPanelLayout();
        slidingUpPanelLayout.addPanelSlideListener(this);
    }


    private void startTimer() {
        CommonLogger.e("startTimer");
        if (disposable == null || disposable.isDisposed()) {
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> updateProgress());
        }
    }


    private void cancelTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    protected void initData() {
        DaggerBottomFragmentComponent.builder().bottomFragmentModule(new BottomFragmentModule(this))
                .mainComponent(getMainComponent()).build().inject(this);
        MusicManager.getInstance().bindService(getContext());
        initViewPager();
        updatePlayMode(getAppComponent().getSharedPreferences()
                .getInt(MusicUtil.PLAY_MODE, PlayData.PLAY_MODE_ORDER));
        sharedPreferences = getActivity().getSharedPreferences(MusicUtil.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        presenter.registerEvent(PlayStateEvent.class, playStateEvent -> {
            CommonLogger.e("playState:" + playStateEvent.getPlayState());
            if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PLAYING) {
                playOrPause.setImageResource(R.drawable.ic_pause_black_24dp);
                updateMaxProgress(MusicPlayerManager.getInstance().getDuration());
                resumeAnimation();
                startTimer();
            } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARING) {
                updateContent();
                startAnimation();
            } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_ERROR || playStateEvent
                    .getPlayState() == MusicPlayerManager.PLAY_STATE_FINISH ||
                    playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PAUSE) {
                playOrPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                cancelTimer();
                pauseAnimation();
            } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARED) {
                if (musicPlayBean.getDuration() == 0) {
                    musicPlayBean.setDuration(MusicPlayerManager.getInstance().getDuration());
                    getAppComponent().getDaoSession().getMusicPlayBeanDao()
                            .update(musicPlayBean);
                }
            } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_IDLE) {
                getActivity().finish();
            }
        });
        presenter.registerEvent(ServiceStateEvent.class, serviceStateEvent -> {
            if (serviceStateEvent.isConnected()) {
                if (MusicPlayerManager.getInstance().getCurrentState() == MusicPlayerManager
                        .PLAY_STATE_PLAYING) {
                    updateContent();
                    updateMaxProgress(MusicPlayerManager.getInstance().getDuration());
                    playOrPause.setImageResource(R.drawable.ic_pause_black_24dp);
                    startTimer();
                    startAnimation();
                } else if (MusicPlayerManager.getInstance().getCurrentState() == MusicPlayerManager.PLAY_STATE_PAUSE) {
                    updateContent();
                    updateMaxProgress(MusicPlayerManager.getInstance().getDuration());
                    cancelTimer();
                    pauseAnimation();
                    updateProgress();
                    playOrPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    presenter.loadData();
                }


            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    bottomLrc.seekTo(progress, true, false);
                    RxBusManager.getInstance().post(new ProgressEvent(progress));
                }
                startTime.setText(MusicUtil.makeLrcTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //                    释放手时调用
                MusicManager.getInstance().seekTo(seekBar.getProgress());
                startTimer();
            }
        });
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SongDetailFragment.newInstance());
        fragmentList.add(new Fragment());
        fragmentList.add(LrcListFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(null, fragmentList);
        display.setAdapter(viewPagerAdapter);
        display.setCurrentItem(1);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CommonLogger.e("position::" + position + "::::offset::" + positionOffset);
                if (position == 0) {
                    ViewHelper.setAlpha(album, position + positionOffset);
                    ViewHelper.setAlpha(bottomLrc, position + positionOffset);
                } else if (position == 1) {
                    ViewHelper.setAlpha(album, position - positionOffset);
                    ViewHelper.setAlpha(bottomLrc, position - positionOffset);
                } else if (position == 2) {
                    ViewHelper.setAlpha(album, positionOffset);
                    ViewHelper.setAlpha(bottomLrc, positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    slidingUpPanelLayout.setTouchEnabled(true);
                } else {
                    slidingUpPanelLayout.setTouchEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void startAnimation() {
        mRotateAnimator = ObjectAnimator.ofFloat(album, "rotation", 0f, 360f);
        mRotateAnimator.setDuration(10000);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnimator.start();
    }


    private void cancelAnimation() {
        if (mRotateAnimator != null) {
            mRotateAnimator.cancel();
        }
    }


    private long animationPlayTime = 0;

    private void pauseAnimation() {
        animationPlayTime = mRotateAnimator.getCurrentPlayTime();
        mRotateAnimator.cancel();
    }


    private void resumeAnimation() {
        mRotateAnimator.start();
        mRotateAnimator.setCurrentPlayTime(animationPlayTime);
    }

    private void updateContent() {
        musicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
        if (musicPlayBean != null) {
            CommonLogger.e(musicPlayBean.toString());
        } else {
            CommonLogger.e("更新的音乐数据未空");
        }
        if (musicPlayBean != null) {
            updateSongName(musicPlayBean.getSongName());
            updateArtistName(musicPlayBean.getArtistName());
            updateAlbum(musicPlayBean.getAlbumUrl());
            if (!musicPlayBean.getIsLocal()) {
                if (FileUtil.isFileExist(MusicUtil.getLyricPath(musicPlayBean.getSongId()))) {
                    updateMusicContent(new File(MusicUtil.getLyricPath(musicPlayBean.getSongId())));
                } else {
                    presenter.getLrcContent(musicPlayBean.getSongId(), musicPlayBean.getLrcUrl());
                }
            }
        }
    }

    @Override
    protected void updateView() {
        intEvaluator = new IntEvaluator();
        floatEvaluator = new FloatEvaluator();
        int sameWidth = playOrPause.getLayoutParams().width;
        screenWidth = DensityUtil.getScreenWidth(getContext());
        screenHeight = DensityUtil.getScreenHeight(getContext());
        int margin = ((RelativeLayout.LayoutParams) playOrPause.getLayoutParams()).rightMargin;
        endPlay = screenWidth / 2 - (sameWidth * 2 + margin) + sameWidth / 2;
    }


    public static BottomFragment newInstance() {
        return new BottomFragment();
    }


    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        int playResult = intEvaluator.evaluate(slideOffset, DensityUtil.toDp(playMinSize), DensityUtil.toDp(playMaxSize));
        playOrPause.getLayoutParams().width = playResult;
        playOrPause.getLayoutParams().height = playResult;
        playOrPause.requestLayout();
        ((RelativeLayout.LayoutParams) songName.getLayoutParams()).leftMargin = intEvaluator
                .evaluate(slideOffset, DensityUtil.toDp(60), endSongName);
        songName.requestLayout();
        artistName.setTranslationX(intEvaluator.evaluate(slideOffset, 0, endArtistName));
        next.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.toDp(15)));
        playMode.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.toDp(30)));
        previous.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.toDp(15)));
        list.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.toDp(30)));
        playOrPause.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay));
        float value = floatEvaluator.evaluate(slideOffset, 0, 1);
        int albumSize = intEvaluator.evaluate(slideOffset, DensityUtil.toDp(50), DensityUtil.toDp(200));
        album.getLayoutParams().width = albumSize;
        album.getLayoutParams().height = albumSize;
        album.requestLayout();
        album.setTranslationX(intEvaluator.evaluate(slideOffset, 0, DensityUtil.getScreenWidth(getContext()) / 2 - DensityUtil.toDp(100)));
        album.setTranslationY(intEvaluator.evaluate(slideOffset, 0, DensityUtil.toDp(100)));
        playMode.setAlpha(value);
        list.setAlpha(value);
        startTime.setAlpha(value);
        endTime.setAlpha(value);
        bottomLrc.setAlpha(value);
        //        back.setAlpha(value);
        //        comment.setAlpha(value);
        iconContainer.setTranslationY(intEvaluator.evaluate(slideOffset, 0, screenHeight - (2 * iconContainer.getHeight())));
        //        comment.setTranslationY(intEvaluator.evaluate(slideOffset, 0, screenHeight - (3 * iconContainer.getHeight())));
        //        bottomLrc.setTranslationY(intEvaluator.evaluate(slideOffset, 0, (screenHeight - (4 * iconContainer.getHeight()))));
        seekContainer.setTranslationY(intEvaluator.evaluate(slideOffset, 0, (int) (screenHeight - (2.5 * iconContainer.getHeight()))));
        ((RelativeLayout.LayoutParams) seekContainer.getLayoutParams()).leftMargin = intEvaluator.evaluate(slideOffset, DensityUtil.toDp(70), 0);
        seekContainer.requestLayout();
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingPanelLayout.PanelState previousState, SlidingPanelLayout.PanelState newState) {
        if (previousState == SlidingPanelLayout.PanelState.COLLAPSED && newState == SlidingPanelLayout.PanelState.DRAGGING) {
            endPlay = (int) (DensityUtil.getScreenWidth(getContext()) / 2 - (1.5 * playMode.getLayoutParams().width) - ((RelativeLayout.LayoutParams) playOrPause.getLayoutParams()).rightMargin) - (DensityUtil.dip2px(getContext(), 8));
            playMode.setVisibility(View.VISIBLE);
            //            back.setVisibility(View.VISIBLE);
            bottomLrc.setVisibility(View.VISIBLE);
        } else if (previousState == SlidingPanelLayout.PanelState.DRAGGING && newState == SlidingPanelLayout.PanelState.COLLAPSED) {
            playMode.setVisibility(View.INVISIBLE);
            //            back.setVisibility(View.INVISIBLE);
            //            comment.setVisibility(View.INVISIBLE);
            bottomLrc.setVisibility(View.INVISIBLE);
            startTime.setVisibility(View.GONE);
            endTime.setVisibility(View.GONE);
        } else if (previousState == SlidingPanelLayout.PanelState.EXPANDED && newState == SlidingPanelLayout.PanelState.DRAGGING) {
            display.setVisibility(View.GONE);
            RxBusManager.getInstance().post(new DragEvent(false));
            if (display.getCurrentItem() != 1) {
                display.setCurrentItem(1, false);
            }
        } else if (previousState == SlidingPanelLayout.PanelState.DRAGGING && newState == SlidingPanelLayout.PanelState.EXPANDED) {
            display.setVisibility(View.VISIBLE);
            RxBusManager.getInstance().post(new DragEvent(true));
            startTime.setVisibility(View.VISIBLE);
            endTime.setVisibility(View.VISIBLE);
        }
    }


    private void updateMusicContent(File file) {
        if (file.exists()) {
            List<LrcRow> result = MusicUtil.parseLrcContent(file);
            if (result != null && result.size() > 0) {
                bottomLrc.setLrcRows(result);
                bottomLrc.setVisibility(View.VISIBLE);
            } else {
                ToastUtils.showShortToast("暂时没有找到歌词");
                file.delete();
                bottomLrc.reset();
                bottomLrc.setVisibility(View.INVISIBLE);
            }
        } else {
            bottomLrc.reset();
            bottomLrc.setVisibility(View.INVISIBLE);
        }
    }

    private void updateMaxProgress(int max) {
        if (seekBar.getMax() != 0 && seekBar.getMax() != max) {
            seekBar.setMax(max);
            endTime.setText(MusicUtil.makeLrcTime(max));
        }
    }


    private void updateAlbum(String uri) {
        if (uri != null && uri.startsWith("http")) {
            uri = MusicUtil.getRealUrl(uri, DensityUtil.toDp(50));
        }
        if (getContext() != null) {
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    bg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, getContext(), 20));
                }
            });

            BaseApplication.getAppComponent().getImageLoader().loadImage(getContext(), new GlideImageLoaderConfig.Builder()
                    .imageView(album).url(uri).build());
        }
    }

    private void updateProgress() {
        int progress = (int) MusicManager.getInstance().getCurrentProgress();
        seekBar.setProgress(progress);
        if (musicPlayBean != null) {
            seekBar.setSecondaryProgress((int) (MusicPlayerManager.getInstance().getBufferedPercentage() * 1.0f / 100 * musicPlayBean.getDuration()));
        }
        startTime.setText(MusicUtil.makeLrcTime(progress));
    }

    private void updateSongName(String name) {
        songName.setText(name);
        Rect bound = new Rect();
        songName.getPaint().getTextBounds(songName.getText().toString(), 0, songName.getText().length(), bound);
        endSongName = (DensityUtil.getScreenWidth(getContext()) - bound.width()) / 2;
    }

    private void updateArtistName(String name) {
        artistName.setText(name);
        Rect bound = new Rect();
        artistName.getPaint().getTextBounds(artistName.getText().toString(), 0, artistName.getText().length(), bound);
        endArtistName = (DensityUtil.getScreenWidth(getContext()) - bound.width()) / 2 - ((RelativeLayout.LayoutParams) artistName.getLayoutParams()).leftMargin;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSeekTo(int progress) {
        MusicManager.getInstance().seekTo(progress);
    }


    private int modeIndex = 1;

    private int[] playModeResId = new int[]{R.drawable.ic_repeat_one_white_24dp
            , R.drawable.ic_compare_arrows_white_24dp, R.drawable.ic_shuffle_white_24dp};

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fragment_bottom_comment) {
        } else if (id == R.id.iv_fragment_bottom_back) {
            slidingUpPanelLayout.setPanelState(SlidingPanelLayout.PanelState.COLLAPSED);
        } else if (id == R.id.riv_fragment_bottom_list) {

        } else if (id == R.id.riv_fragment_bottom_mode) {
            modeIndex++;
            modeIndex %= 3;
            playMode.setImageResource(playModeResId[modeIndex]);
            if (modeIndex == 0) {
                updatePlayMode(PlayData.PLAY_MODE_RECYCLER);
            } else if (modeIndex == 1) {
                updatePlayMode(PlayData.PLAY_MODE_ORDER);
            } else {
                updatePlayMode(PlayData.PLAY_MODE_RANDOM);
            }
        } else if (id == R.id.riv_fragment_bottom_play) {
            presenter.playOrPause();
        } else if (id == R.id.riv_fragment_bottom_previous) {
            presenter.previous();
        } else if (id == R.id.riv_fragment_bottom_next) {
            presenter.next();
        }
    }

    private void updatePlayMode(int currentMode) {
        getAppComponent().getSharedPreferences()
                .edit().putInt(MusicUtil.PLAY_MODE, currentMode).apply();
        presenter.setMode(currentMode);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicManager.getInstance().unBindService(getContext());
        cancelAnimation();
    }


    @Override
    public void updateData(Object o) {
        if (o instanceof Long) {
            Long songId = (Long) o;
            if (musicPlayBean != null && songId.equals(musicPlayBean.getSongId())) {
                updateMusicContent(new File(MusicUtil.getLyricPath(songId)));
            }
        }
    }
}
