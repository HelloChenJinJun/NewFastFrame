package com.example.commonlibrary.baseadapter.foot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.commonlibrary.R;


public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private TextView mToTopTV;
    private OnRetryListener mOnRetryListener;

    private RecyclerFooterViewClickListener listener ;
    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public void setBottomViewClickListener(RecyclerFooterViewClickListener listener){
        this.listener = listener ;
    }
    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_superrecyclerview_load_more_footer_view, this, true);
        mLoadingView = findViewById(R.id.loadingView);
        mErrorView = findViewById(R.id.errorView);
        mTheEndView =  findViewById(R.id.theEndView);
        mToTopTV = (TextView) findViewById(R.id.to_list_top_textview);
        mToTopTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != listener)
                    listener.onBottomViewClickListener(v);
            }
        });
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    setStatus(Status.LOADING);
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        mStatus=Status.GONE;
        change();
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        if (mStatus!=status) {
            this.mStatus = status;
            change();
        }
    }

    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
    }

    private void change() {
        switch (mStatus) {
            case GONE:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                hideLoading();
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                showLoading();
                break;

            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                hideLoading();
                break;

            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                hideLoading();
                break;
        }
    }


    private void hideLoading() {
        ((ViewGroup) mLoadingView).getChildAt(0).clearAnimation();
    }

    private void showLoading() {
        RotateAnimation rt = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rt.setInterpolator(new LinearInterpolator());
        rt.setRepeatMode(Animation.RESTART);
        rt.setRepeatCount(Animation.INFINITE);
        rt.setDuration(1000);
        ((ViewGroup) mLoadingView).getChildAt(0).startAnimation(rt);
    }

    public enum Status {
        GONE, LOADING, ERROR, THE_END
    }

    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }

}
