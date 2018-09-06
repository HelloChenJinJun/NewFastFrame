package com.example.commonlibrary.baseadapter.empty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.R;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:39
 * QQ:             1981367757
 */

public class EmptyLayout extends FrameLayout implements View.OnClickListener {


    private RelativeLayout errorLayout,emptyLayout,loadingLayout;
    private ImageView loadingImage;
    private View contentView;

    public EmptyLayout(@NonNull Context context) {
        this(context, null);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    private void initView(Context context, AttributeSet attributeSet) {
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.empty_layout);
        View view = View.inflate(context, R.layout.empty_layout, this);
        errorLayout = view.findViewById(R.id.rl_empty_layout_error);
        emptyLayout= view.findViewById(R.id.rl_empty_layout_empty);
        loadingLayout = view.findViewById(R.id.rl_empty_layout_loading);
        loadingImage= view.findViewById(R.id.iv_empty_loading_image);
        view.findViewById(R.id.rl_empty_layout_error).setOnClickListener(this);
        view.findViewById(R.id.rl_empty_layout_empty).setOnClickListener(this);
        typedArray.recycle();
        updateViewVisible();
    }

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_NO_NET = 2;
    public static final int STATUS_NO_DATA = 3;
    public static final int STATUS_HIDE = 4;
    private int currentStatus = STATUS_HIDE;


    public int getCurrentStatus() {
        return currentStatus;
    }



    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        updateViewVisible();
    }

    private void updateViewVisible() {
        if (currentStatus != STATUS_HIDE) {
            if (getVisibility() == GONE) {
                setVisibility(VISIBLE);
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
                if (contentView!=null) {
                    contentView.setVisibility(VISIBLE);
                }
            }
            return;
        }
        switch (currentStatus) {
            case STATUS_HIDE:
                contentView.setVisibility(VISIBLE);
                break;
            case STATUS_NO_DATA:
                errorLayout.setVisibility(GONE);
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(VISIBLE);
                contentView.setVisibility(GONE);
                break;
            case STATUS_NO_NET:
                errorLayout.setVisibility(VISIBLE);
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(GONE);
                contentView.setVisibility(GONE);
                break;
            case STATUS_LOADING:
                Glide.with(getContext()).load(R.drawable.loading_animation)
                        .into(loadingImage);
                errorLayout.setVisibility(GONE);
                emptyLayout.setVisibility(GONE);
                loadingLayout.setVisibility(VISIBLE);
                contentView.setVisibility(GONE);
                break;
            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRetryListener != null) {
            mOnRetryListener.onRetry();
        }
    }

    public void setContentView(View contentView) {
        this.contentView=contentView;
    }


    public interface OnRetryListener {
        void onRetry();
    }


    private OnRetryListener mOnRetryListener;


    public void setOnRetryListener(OnRetryListener onRetryListener) {
        mOnRetryListener = onRetryListener;
    }



}
