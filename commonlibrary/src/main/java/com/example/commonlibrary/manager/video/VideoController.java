package com.example.commonlibrary.manager.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.example.commonlibrary.manager.IVideoPlayer;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/22     10:38
 */
public abstract class VideoController extends FrameLayout implements View.OnTouchListener {

    protected IVideoPlayer mIVideoPlayer;
    private float downX;
    private float downY;
    private int threshold = 0;
    private boolean changePosition;
    private boolean changeVolume;
    private boolean changeBrightness;
    private int changeStartPosition;
    private float changeStartBrightness;
    private int changeStartVolume;
    private long currentPosition;


    public void setIVideoPlayer(IVideoPlayer IVideoPlayer) {
        mIVideoPlayer = IVideoPlayer;
    }

    public VideoController(@NonNull Context context) {
        this(context, null);
    }

    public VideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        //        threshold = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mIVideoPlayer.getWindowState() != DefaultVideoPlayer.WINDOW_STATE_FULL
                || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_FINISH
                || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_ERROR
                || mIVideoPlayer.getCurrentState() == DefaultVideoPlayer.PLAY_STATE_IDLE) {
            return false;
        }
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                changePosition = false;
                changeVolume = false;
                changeBrightness = false;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(x - downX);
                float deltaY = Math.abs(y - downY);
                if (!changePosition && !changeBrightness && !changeVolume && downY > getHeight() / 5) {
                    if (deltaY < deltaX && deltaX > threshold) {
                        changePosition = true;
                        changeStartPosition = mIVideoPlayer.getPosition();
                    } else if (deltaY > threshold) {
                        if (x > getWidth() / 2) {
                            changeVolume = true;
                            changeStartVolume = mIVideoPlayer.getVolume();
                        } else {
                            changeBrightness = true;
                            if (getContext() instanceof AppCompatActivity) {
                                changeStartBrightness = ((AppCompatActivity) getContext()).getWindow()
                                        .getAttributes().screenBrightness;
                            }
                        }
                    }
                }
                if (changePosition) {
                    long duration = mIVideoPlayer.getDuration();
                    if (x - downX > 0) {
                        currentPosition = (long) (changeStartPosition + duration * (deltaX / getWidth()));
                    } else {
                        currentPosition = (long) (changeStartPosition - duration * (deltaX / getWidth()));
                    }
                    currentPosition = Math.min(duration, currentPosition);
                    updatePosition(duration, currentPosition);
                }
                if (changeBrightness) {
                    float percent = deltaY * 3 / getHeight();
                    float newBrightness;
                    if (y - downY > 0) {
                        //                        变暗
                        newBrightness = Math.max(0, Math.min(1, changeStartBrightness - percent));
                    } else {
                        //                        变亮
                        newBrightness = Math.max(0, Math.min(1, changeStartBrightness + percent));
                    }
                    if (getContext() instanceof AppCompatActivity) {
                        WindowManager.LayoutParams layoutParams = ((AppCompatActivity) getContext()).getWindow()
                                .getAttributes();
                        layoutParams.screenBrightness = newBrightness;
                        ((AppCompatActivity) getContext()).getWindow().setAttributes(layoutParams);
                    }
                    updateBrightness(1, newBrightness);
                }
                if (changeVolume) {
                    float percent = deltaY * 3 / getHeight();
                    int newVolume;
                    int maxVolume = mIVideoPlayer.getMaxVolume();
                    if (y - downY > 0) {
                        newVolume = (int) Math.max(0, Math.min(maxVolume, changeStartVolume - maxVolume * percent));
                    } else {
                        newVolume = (int) Math.max(0, Math.min(maxVolume, changeStartVolume + (maxVolume * percent)));
                    }
                    mIVideoPlayer.setVolume(newVolume);
                    updateVolume(maxVolume, newVolume);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (changePosition) {
                    hideMiddlePosition();
                    mIVideoPlayer.seekTo((int) currentPosition);
                    return true;
                }
                if (changeBrightness) {
                    hideBrightContainer();
                    return true;
                }
                if (changeVolume) {
                    hideVolumeContainer();
                    return true;
                }
                break;
        }
        return false;
    }

    protected abstract void hideVolumeContainer();

    protected abstract void hideBrightContainer();

    protected abstract void hideMiddlePosition();

    protected abstract void updateVolume(int maxVolume, int volume);

    protected abstract void updateBrightness(float maxBrightness, float brightness);

    protected abstract void updatePosition(long duration, long currentPosition);

    public abstract void onPlayStateChanged(int state);


    public abstract void onWindowStateChanged(int windowState);

    public abstract void reset();

    public abstract void setTitle(String title);

    public abstract void setTotalLength(long length);

    public abstract void setClarity(List<Clarity> urlList);

    public abstract void setImageCover(String imageUrl);


    public static class Clarity {
        private String grade;    // 清晰度等级
        private String p;        // 270P、480P、720P、1080P、4K ...
        private String videoUrl; // 视频链接地址

        private Map<String, String> headers;

        public Clarity(String grade, String p, String videoUrl) {
            this.grade = grade;
            this.p = p;
            this.videoUrl = videoUrl;
        }


        public Clarity(String grade, String p, String videoUrl, Map<String, String> headers) {
            this.grade = grade;
            this.p = p;
            this.videoUrl = videoUrl;
            this.headers = headers;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getGrade() {
            return grade;
        }

        public String getP() {
            return p;
        }

        public String getVideoUrl() {
            return videoUrl;
        }
    }


}
