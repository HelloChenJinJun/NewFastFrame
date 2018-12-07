package com.example.commonlibrary.manager.video;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.TimeUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/22     11:24
 */
public class DefaultVideoController extends VideoController implements View.OnClickListener {
    private ImageView battery;
    private ImageView screen;
    private ImageView bottomPlay;
    private LinearLayout topContainer, bottomContainer, finishContainer, brightContainer, volumeContainer, errorContainer, positionContainer, loadingContainer;
    private TextView title;
    private TextView batteryTime;
    private TextView loading;
    private TextView middlePosition;
    private TextView startTime;
    private TextView endTime;
    private TextView endTimeRight;
    private TextView clarity;
    private TextView timeDelta;
    private TextView retry;
    private ProgressBar brightProgress, volumeProgress, positionProgress;
    private SeekBar bottomSeek;
    private RelativeLayout middlePlay;
    private Disposable disposable, batteryDisposable;
    private RelativeLayout clarityContainer;
    private SuperRecyclerView clarityDisplay;
    private BroadcastReceiver batteryBroadcastReceiver;
    private ImageView bg;
    private ImageView back;
    private ClarityAdapter clarityAdapter;

    public DefaultVideoController(@NonNull Context context) {
        super(context);
        initDefaultView();
    }

    @Override
    protected void hideVolumeContainer() {
        volumeContainer.setVisibility(GONE);
    }

    @Override
    protected void hideBrightContainer() {
        brightContainer.setVisibility(GONE);
    }

    @Override
    protected void hideMiddlePosition() {
        positionContainer.setVisibility(GONE);
        startTimer();
    }

    @Override
    protected void updateVolume(int maxVolume, int volume) {
        volumeContainer.setVisibility(VISIBLE);
        volumeProgress.setProgress((int) ((volume / (maxVolume * 1.0f)) * 100));
    }

    @Override
    protected void updateBrightness(float maxBrightness, float brightness) {
        brightContainer.setVisibility(VISIBLE);
        brightProgress.setProgress((int) ((brightness / (maxBrightness * 1.0f)) * 100));
    }

    @Override
    protected void updatePosition(long duration, long currentPosition) {
        positionContainer.setVisibility(VISIBLE);
        middlePosition.setText(TimeUtil.formatTime(currentPosition));
        positionProgress.setProgress((int) ((currentPosition / (duration * 1.0f)) * 100));
        bottomSeek.setProgress((int) ((currentPosition / (duration * 1.0f)) * 100));
        cancelTimer();
    }

    private void initDefaultView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_video_control, this, true);
        setOnClickListener(v -> {
            if (mIVideoPlayer
                    .getCurrentState() == DefaultVideoPlayer
                    .PLAY_STATE_BUFFERING_PLAYING
                    || mIVideoPlayer.getCurrentState()
                    == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE
                    || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_PAUSE
                    || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_PLAYING) {
                dealTop();
                dealBottom();
            }
        });
        bg = view.findViewById(R.id.iv_view_video_control_bg);
        timeDelta = view.findViewById(R.id.tv_view_video_control_time_delta);
        back = view.findViewById(R.id.iv_view_video_control_top_back);
        battery = view.findViewById(R.id.iv_view_video_control_top_battery);
        screen = view.findViewById(R.id.iv_view_video_control_bottom_screen);
        topContainer = view.findViewById(R.id.ll_view_video_control_top_container);
        bottomContainer = view.findViewById(R.id.ll_view_video_control_bottom_container);
        finishContainer = view.findViewById(R.id.ll_view_video_control_finish);
        brightContainer = view.findViewById(R.id.ll_view_video_control_brightness);
        volumeContainer = view.findViewById(R.id.ll_view_video_control_volume);
        errorContainer = view.findViewById(R.id.ll_view_video_control_error);
        retry = view.findViewById(R.id.tv_view_video_control_error_retry);
        positionContainer = view.findViewById(R.id.ll_view_video_control_position);
        loadingContainer = view.findViewById(R.id.ll_view_video_control_loading);
        title = view.findViewById(R.id.tv_view_video_control_top_title);
        batteryTime = view.findViewById(R.id.tv_view_video_control_top_battery_time);
        loading = view.findViewById(R.id.tv_view_video_control_loading_content);
        middlePosition = view.findViewById(R.id.tv_view_video_control_position_time);
        startTime = view.findViewById(R.id.tv_view_video_control_bottom_start);
        endTime = view.findViewById(R.id.tv_view_video_control_bottom_end);
        endTimeRight = view.findViewById(R.id.tv_view_video_control_bottom_end_right);
        clarity = view.findViewById(R.id.tv_view_video_control_bottom_clarity);
        TextView restart = view.findViewById(R.id.ll_view_video_control_restart);
        TextView share = view.findViewById(R.id.ll_view_video_control_share);
        brightProgress = view.findViewById(R.id.pb_view_video_control_brightness);
        volumeProgress = view.findViewById(R.id.pb_view_video_control_volume);
        positionProgress = view.findViewById(R.id.pb_view_video_control_position_progress);
        bottomSeek = view.findViewById(R.id.sb_view_video_control_bottom_seek);
        middlePlay = view.findViewById(R.id.rl_view_video_control_middle_play);
        bottomPlay = view.findViewById(R.id.iv_view_video_control_bottom_play);
        clarityContainer = view.findViewById(R.id.rl_view_video_control_clarity_container);
        clarityDisplay = view.findViewById(R.id.srcv_view_video_control_clarity);
        clarityDisplay.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        clarityDisplay.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
        back.setOnClickListener(this);
        screen.setOnClickListener(this);
        retry.setOnClickListener(this);
        middlePlay.setOnClickListener(this);
        bottomPlay.setOnClickListener(this);
        clarity.setOnClickListener(this);
        restart.setOnClickListener(this);
        share.setOnClickListener(this);
        clarityContainer.setOnClickListener(this);
        bottomSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long position = (long) (mIVideoPlayer.getDuration() * progress / 100f);
                middlePosition.setText(TimeUtil.formatTime(position));
                startTime.setText(TimeUtil.formatTime(position));
                positionProgress.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelTimer();
                positionContainer.setVisibility(VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                positionContainer.setVisibility(GONE);
                long position = (long) (mIVideoPlayer.getDuration() * seekBar.getProgress() / 100f);
                mIVideoPlayer.seekTo((int) position);
                startTimer();
            }
        });


    }

    private void dealBottom() {
        if (bottomContainer.getTranslationY() == bottomContainer.getHeight()) {
            bottomIn();
        } else if (bottomContainer.getTranslationY() == 0) {
            bottomOut();
        }
    }

    private void bottomIn() {
        if (bottomContainer.getTranslationY() != 0) {
            ObjectAnimator.ofFloat(bottomContainer, "translationY", bottomContainer.getTranslationY(), 0)
                    .setDuration(300).start();
        }
    }

    private void bottomOut() {
        if (bottomContainer.getTranslationY() != bottomContainer.getHeight()) {
            ObjectAnimator.ofFloat(bottomContainer, "translationY", bottomContainer.getTranslationY(), bottomContainer.getHeight())
                    .setDuration(300).start();
        }
    }


    private void topIn() {
        if (topContainer.getTranslationY() != 0) {
            ObjectAnimator.ofFloat(topContainer, "translationY", topContainer.getTranslationY(), 0).
                    setDuration(300).start();
        }
    }

    private void topOut() {
        if (topContainer.getTranslationY() != -topContainer.getHeight()) {
            ObjectAnimator.ofFloat(topContainer, "translationY", topContainer.getTranslationY(), -topContainer.getHeight())
                    .setDuration(300).start();
        }
    }

    private void dealTop() {
        if (topContainer.getTranslationY() == (-topContainer.getHeight())) {
            topIn();
        } else if (topContainer.getTranslationY() == 0) {
            topOut();
        }
    }


    @Override
    public void onPlayStateChanged(int state) {
        CommonLogger.e("state:" + state);
        switch (state) {
            case DefaultVideoPlayer.PLAY_STATE_IDLE:
                reset();
                break;
            case DefaultVideoPlayer.PLAY_STATE_PREPARING:
                bg.setVisibility(GONE);
                middlePlay.setVisibility(GONE);
                loadingContainer.setVisibility(VISIBLE);
                loading.setText("加载资源中...");
                break;
            case DefaultVideoPlayer.PLAY_STATE_PREPARED:
                startTimer();
                break;
            case DefaultVideoPlayer.PLAY_STATE_BUFFERING_PLAYING:
                loadingContainer.setVisibility(VISIBLE);
                loading.setText("正在缓冲中...");
                bottomContainer.setVisibility(VISIBLE);
                bottomPlay.setImageResource(R.drawable.ic_player_pause);
                break;
            case DefaultVideoPlayer.PLAY_STATE_PLAYING:
                loadingContainer.setVisibility(GONE);
                bottomPlay.setImageResource(R.drawable.ic_player_pause);
                bottomContainer.setVisibility(VISIBLE);
                topContainer.setVisibility(VISIBLE);
                endTime.setText(TimeUtil.formatTime(mIVideoPlayer.getDuration()));
                endTimeRight.setText(TimeUtil.formatTime(mIVideoPlayer.getDuration()));
                break;
            case DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE:
                bottomPlay.setImageResource(R.drawable.ic_player_start);
                break;
            case DefaultVideoPlayer.PLAY_STATE_PAUSE:
                bottomPlay.setImageResource(R.drawable.ic_player_start);
                break;
            case DefaultVideoPlayer.PLAY_STATE_FINISH:
                cancelTimer();
                bg.setVisibility(VISIBLE);
                finishContainer.setVisibility(VISIBLE);
                bottomContainer.setVisibility(GONE);
                topContainer.setVisibility(GONE);
                middlePlay.setVisibility(GONE);
                break;
            case DefaultVideoPlayer.PLAY_STATE_ERROR:
                errorContainer.setVisibility(VISIBLE);
                middlePlay.setVisibility(GONE);
                break;
        }
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

    private void updateProgress() {
        CommonLogger.e("updateProgress");
        int currentPosition = mIVideoPlayer.getPosition();
        int duration = mIVideoPlayer.getDuration();
        int buffered = mIVideoPlayer.getBufferedPercentage();
        bottomSeek.setProgress((int) ((currentPosition * 1.0f / duration) * 100));
        bottomSeek.setSecondaryProgress(buffered);
        startTime.setText(TimeUtil.formatTime(currentPosition));
        String str = TimeUtil.formatTime(duration);
        endTimeRight.setText(str);
        endTime.setText(str);
    }

    @Override
    public void onWindowStateChanged(int windowState) {
        CommonLogger.e("windowState:" + windowState);
        if (windowState == DefaultVideoPlayer.WINDOW_STATE_TINY) {
            bottomContainer.setVisibility(GONE);
            topContainer.setVisibility(GONE);
            positionContainer.setVisibility(GONE);
            volumeContainer.setVisibility(GONE);
            brightContainer.setVisibility(GONE);
            if (batteryBroadcastReceiver != null) {
                getContext().unregisterReceiver(batteryBroadcastReceiver);
                batteryBroadcastReceiver = null;
            }
            cancelBatteryTime();
        } else if (windowState == DefaultVideoPlayer.WINDOW_STATE_LIST) {
            screen.setImageResource(R.drawable.ic_player_enlarge);
            endTime.setVisibility(VISIBLE);
            timeDelta.setVisibility(VISIBLE);
            endTimeRight.setVisibility(GONE);
            clarity.setVisibility(GONE);
            back.setVisibility(GONE);
            if (clarityAdapter != null) {
                for (Clarity item : clarityAdapter.getData()
                        ) {
                    if (item.getVideoUrl().equals(mIVideoPlayer.getUrl())) {
                        clarity.setText(item.getGrade());
                    }
                }
            }
            cancelBatteryTime();
            bottomIn();
            topIn();
            if (batteryBroadcastReceiver != null) {
                getContext().unregisterReceiver(batteryBroadcastReceiver);
                batteryBroadcastReceiver = null;
            }
        } else if (windowState == DefaultVideoPlayer.WINDOW_STATE_FULL) {
            startBatteryTime();
            screen.setImageResource(R.drawable.ic_player_shrink);
            clarity.setVisibility(VISIBLE);
            endTime.setVisibility(GONE);
            timeDelta.setVisibility(GONE);
            endTimeRight.setVisibility(VISIBLE);
            back.setVisibility(VISIBLE);
            bottomOut();
            topOut();
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            if (batteryBroadcastReceiver == null) {
                batteryBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        dealBattery(intent);
                    }
                };
                getContext().registerReceiver(batteryBroadcastReceiver, intentFilter);
            }
        }
    }


    private void startBatteryTime() {
        batteryDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> batteryTime.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date())));
    }

    private void cancelBatteryTime() {
        if (batteryDisposable != null && !batteryDisposable.isDisposed()) {
            batteryDisposable.dispose();
        }
    }

    private void dealBattery(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            // 充电中
            battery.setImageResource(R.drawable.battery_charging);
        } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
            // 充电完成
            battery.setImageResource(R.drawable.battery_full);
        } else {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int percentage = (int) (((float) level / scale) * 100);
            if (percentage <= 10) {
                battery.setImageResource(R.drawable.battery_10);
            } else if (percentage <= 20) {
                battery.setImageResource(R.drawable.battery_20);
            } else if (percentage <= 50) {
                battery.setImageResource(R.drawable.battery_50);
            } else if (percentage <= 80) {
                battery.setImageResource(R.drawable.battery_80);
            } else if (percentage <= 100) {
                battery.setImageResource(R.drawable.battery_100);
            }
        }
    }

    @Override
    public void reset() {
        CommonLogger.e("reset");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        errorContainer.setVisibility(GONE);
        loadingContainer.setVisibility(GONE);
        finishContainer.setVisibility(GONE);
        bottomContainer.setVisibility(GONE);
        positionContainer.setVisibility(GONE);
        volumeContainer.setVisibility(GONE);
        brightContainer.setVisibility(GONE);
        bg.setVisibility(VISIBLE);
        middlePlay.setVisibility(VISIBLE);
        cancelTimer();
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setTotalLength(long length) {
        endTime.setText(TimeUtil.formatTime(length));
        endTimeRight.setText(TimeUtil.formatTime(length));
    }

    @Override
    public void setClarity(List<Clarity> urlList) {
        clarityAdapter = new ClarityAdapter();
        clarityDisplay.setAdapter(clarityAdapter);
        clarityAdapter.refreshData(urlList);
        clarityAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                clarityContainer.setVisibility(GONE);
                Clarity clarity = clarityAdapter.getData(position);
                mIVideoPlayer.setUp(clarity.getVideoUrl(), clarity.getHeaders());
                mIVideoPlayer.release();
                mIVideoPlayer.start();
            }
        });
    }

    @Override
    public void setImageCover(String imageUrl) {
        Glide.with(getContext()).load(imageUrl).into(bg);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_view_video_control_top_back) {
            if (getContext() instanceof AppCompatActivity) {
                ((AppCompatActivity) getContext()).onBackPressed();
            }
        } else if (id == R.id.iv_view_video_control_bottom_screen) {
            if (mIVideoPlayer.getWindowState() == DefaultVideoPlayer.WINDOW_STATE_FULL) {
                mIVideoPlayer.setWindowState(DefaultVideoPlayer.WINDOW_STATE_LIST);
            } else if (mIVideoPlayer.getWindowState() == DefaultVideoPlayer.WINDOW_STATE_LIST) {
                mIVideoPlayer.setWindowState(DefaultVideoPlayer.WINDOW_STATE_FULL);
            }
        } else if (id == R.id.rl_view_video_control_middle_play) {
            mIVideoPlayer.start();
        } else if (id == R.id.iv_view_video_control_bottom_play) {
            if (mIVideoPlayer.getCurrentState() == DefaultVideoPlayer
                    .PLAY_STATE_PAUSE
                    || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE) {
                mIVideoPlayer.start();
            } else if (mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PLAYING
                    || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_PLAYING) {
                mIVideoPlayer.pause();
            }
        } else if (id == R.id.tv_view_video_control_bottom_clarity) {
            clarityContainer.setVisibility(VISIBLE);
            clarityAdapter.notifyDataSetChanged();
        } else if (id == R.id.ll_view_video_control_restart) {
            finishContainer.setVisibility(GONE);
            mIVideoPlayer.start();
        } else if (id == R.id.ll_view_video_control_share) {
            ToastUtils.showShortToast("分享功能");
        } else if (id == R.id.rl_view_video_control_clarity_container) {
            clarityContainer.setVisibility(GONE);
        } else if (id == R.id.tv_view_video_control_error_retry) {
            if (AppUtil.isNetworkAvailable()) {
                mIVideoPlayer.start();
            } else {
                ToastUtils.showShortToast("网络连接失败，请检查网络配置");
            }
        }
    }


    public interface OnItemClickListener {
        boolean onStartClick(View view, String url);
    }


    private OnItemClickListener mOnItemClickListener;


    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private class ClarityAdapter extends BaseRecyclerAdapter<Clarity, BaseWrappedViewHolder> {

        @Override
        protected int getLayoutId() {
            return R.layout.item_view_video_control_clarity;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, Clarity data) {
            holder.setText(R.id.tv_item_view_video_control_clarity_content
                    , getContent(data)).setOnItemClickListener();

            if (data.getVideoUrl().equals(mIVideoPlayer.getUrl())) {
                holder.getView(R.id.tv_item_view_video_control_clarity_content).setSelected(true);
            } else {
                holder.getView(R.id.tv_item_view_video_control_clarity_content).setSelected(false);
            }
        }

        private CharSequence getContent(Clarity data) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(data.getGrade()).append("  ").append(data.getP());
            return stringBuilder;
        }
    }
}
