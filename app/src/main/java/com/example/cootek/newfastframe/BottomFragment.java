package com.example.cootek.newfastframe;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.cootek.newfastframe.dagger.BottomFragmentModule;
import com.example.cootek.newfastframe.dagger.DaggerBottomFragmentComponent;
import com.example.cootek.newfastframe.mvp.BottomPresenter;
import com.example.cootek.newfastframe.mvp.IBottomView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class BottomFragment extends BaseFragment implements SlidingUpPanelLayout.PanelSlideListener, IBottomView {


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
    @BindView(R.id.iv_fragment_bottom_like)
    ImageView like;
    @BindView(R.id.iv_fragment_bottom_list)
    ImageView list;
    @BindView(R.id.rl_fragment_bottom_icon_container)
    RelativeLayout iconContainer;
    @BindView(R.id.sb_fragment_bottom_seek)
    SeekBar seekBar;
    @BindView(R.id.rl_fragment_bottom_container)
    RelativeLayout container;
    Unbinder unbinder;
    private IntEvaluator intEvaluator;
    private int endSongName;
    private int endArtistName;
    private int endPlay;
    private FloatEvaluator floatEvaluator;
    private int screenHeight;
    @Inject
    BottomPresenter bottomPresenter;
    private Runnable progressRun;

    @Override
    public void updateData(Object o) {

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
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) root.getParent().getParent();
        slidingUpPanelLayout.addPanelSlideListener(this);
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
                CommonLogger.e("进度" + progress);
                if (MusicManager.getInstance().isPlaying()) {
                    seekBar.postDelayed(this, 50);
                } else {
                    seekBar.removeCallbacks(this);
                }
            }
        };
        bottomPresenter.registerEvent(MusicStatusEvent.class, new Consumer<MusicStatusEvent>() {
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
                    }
                }

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.removeCallbacks(progressRun);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (MusicManager.getInstance().isPlaying()) {
                    MusicManager.getInstance().playOrPause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                    释放手时调用
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
        if (content.getId() != 0) {
            updateAlbum(MusicUtil.getAlbumArtUri(content.getId()).toString());
        }
        CommonLogger.e("更新最大进度" + content.getMaxProgress());
        updateMaxProgress((int) content.getMaxProgress());
        updateProgress();
    }

    @Override
    protected void updateView() {
        seekBar.setSecondaryProgress(seekBar.getMax());
        intEvaluator = new IntEvaluator();
        floatEvaluator = new FloatEvaluator();
        int sameWidth = playOrPause.getLayoutParams().width;
        int screenWidth = DensityUtil.getScreenWidth(getContext());
        screenHeight = DensityUtil.getScreenHeight(getContext());
        int margin = ((RelativeLayout.LayoutParams) playOrPause.getLayoutParams()).rightMargin;
        endPlay = screenWidth / 2 - (sameWidth * 2 + margin) + sameWidth / 2;
    }

    public static Fragment newInstance() {
        return new BottomFragment();
    }


    @OnClick({R.id.iv_fragment_bottom_next, R.id.iv_fragment_bottom_previous, R.id.iv_fragment_bottom_play, R.id.iv_fragment_bottom_like, R.id.iv_fragment_bottom_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fragment_bottom_next:
                bottomPresenter.next();
                break;
            case R.id.iv_fragment_bottom_previous:
                bottomPresenter.previous();
                break;
            case R.id.iv_fragment_bottom_play:
                String result = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.song.getInfos&";
                String str = "songid=" + "549122778" + "&ts=" + System.currentTimeMillis();
                String e = AESTools.encrpty(str);
                CommonLogger.e(result + str + "&e=" + e);
//                bottomPresenter.playOrPause();
                break;
            case R.id.iv_fragment_bottom_like:
                break;
            case R.id.iv_fragment_bottom_list:
                break;
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        int result = intEvaluator.evaluate(slideOffset, DensityUtil.dip2px(getContext(), 60), DensityUtil.getScreenWidth(getContext()));
        album.getLayoutParams().width = result;
        album.getLayoutParams().height = result;
        album.requestLayout();
        songName.setTranslationX(intEvaluator.evaluate(slideOffset, 0, endSongName));
        artistName.setTranslationX(intEvaluator.evaluate(slideOffset, 0, endArtistName));
        next.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.dip2px(getContext(), 15)));
        like.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.dip2px(getContext(), 30)));
        previous.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay + DensityUtil.dip2px(getContext(), 15)));
        list.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay - DensityUtil.dip2px(getContext(), 30)));
        playOrPause.setTranslationX(-intEvaluator.evaluate(slideOffset, 0, endPlay));
        like.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        list.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        iconContainer.setTranslationY(intEvaluator.evaluate(slideOffset, 0, screenHeight - (2 * iconContainer.getHeight())));
        seekBar.setTranslationY(intEvaluator.evaluate(slideOffset, 0, (int) (screenHeight - (2.5 * iconContainer.getHeight()))));
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            like.setVisibility(View.VISIBLE);
        } else if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            like.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void updateMusicContent(File file) {

    }

    @Override
    public void updateMaxProgress(int max) {
        seekBar.setMax(max);
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
            Glide.with(getContext()).load(uri).error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(album);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
