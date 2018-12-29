package com.example.cootek.newfastframe.mvp.lock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.example.cootek.newfastframe.view.lrc.LrcRow;
import com.example.cootek.newfastframe.view.lrc.LrcView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/27     19:47
 */
public class LockScreenActivity extends BaseActivity implements View.OnClickListener {

    private TextView time, week, title, name;
    private ImageView pre, next, play;
    private LrcView bottomLrc;
    public static boolean is_lock = false;
    private RelativeLayout container;

    public static void start(Context context) {
        if (!is_lock) {
            Intent lockScreenIntent = new Intent(context, LockScreenActivity.class);
            lockScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockScreenIntent);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
        return R.layout.activity_lock_screen;
    }

    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected void initView() {
        container = findViewById(R.id.rl_activity_lock_screen_container);
        time = findViewById(R.id.tv_activity_lock_screen_time);
        week = findViewById(R.id.tv_activity_lock_screen_week);
        title = findViewById(R.id.tv_activity_lock_screen_title);
        name = findViewById(R.id.tv_activity_lock_screen_name);
        pre = findViewById(R.id.iv_activity_lock_screen_pre);
        play = findViewById(R.id.iv_activity_lock_screen_play);
        next = findViewById(R.id.iv_activity_lock_screen_next);
        bottomLrc = findViewById(R.id.lv_activity_lock_screen_lrc);
        bottomLrc.setTouchEnable(false);
        pre.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        addDisposable(RxBusManager.getInstance().registerEvent(PlayStateEvent.class, new Consumer<PlayStateEvent>() {
            @Override
            public void accept(PlayStateEvent playStateEvent) throws Exception {
                CommonLogger.e("LOCK:::playState:" + playStateEvent.getPlayState());

                if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PLAYING) {
                    play.setImageResource(R.drawable.ic_pause_black_24dp);

                } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARING) {

                } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_ERROR || playStateEvent
                        .getPlayState() == MusicPlayerManager.PLAY_STATE_FINISH ||
                        playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PAUSE) {
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_PREPARED) {
                    mMusicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
                    updateContent();
                } else if (playStateEvent.getPlayState() == MusicPlayerManager.PLAY_STATE_IDLE) {
                    is_lock = false;
                    finish();
                }
            }
        }));

    }


    private MusicPlayBean mMusicPlayBean;

    @Override
    protected void initData() {
        is_lock = true;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMusicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
        updateContent();
        addDisposable(Observable.interval(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm-MM月dd日 E", Locale.CHINESE);
                    String date[] = simpleDateFormat.format(new Date()).split("-");
                    time.setText(date[0]);
                    week.setText(date[1]);
                    bottomLrc.seekTo(MusicPlayerManager.getInstance().getPosition(), false, false);
                }));
    }

    @Override
    public void updateData(Object o) {

    }


    private void updateLrcContent() {
        if (mMusicPlayBean != null) {
            File file = new File(MusicUtil.getLyricPath(mMusicPlayBean.getSongId()));
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
    }


    private void updateContent() {
        updateLrcContent();
        if (mMusicPlayBean != null) {
            name.setText(mMusicPlayBean.getArtistName());
            title.setText(mMusicPlayBean.getSongName());
            Glide.with(this).asBitmap().load(mMusicPlayBean.getAlbumUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    container.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, LockScreenActivity.this, 20));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_activity_lock_screen_pre) {
            MusicPlayerManager.getInstance().pre();
        } else if (id == R.id.iv_activity_lock_screen_play) {
            if (MusicPlayerManager.getInstance().getCurrentState() == MusicPlayerManager.PLAY_STATE_PLAYING) {
                MusicPlayerManager.getInstance().pause();
            } else {
                MusicPlayerManager.getInstance().play(0);
            }
        } else if (id == R.id.iv_activity_lock_screen_next) {
            MusicPlayerManager.getInstance().next();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_lock = false;
    }
}
