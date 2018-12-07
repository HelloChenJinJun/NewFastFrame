package com.example.commonlibrary.baseadapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreScrollListener;
import com.example.commonlibrary.baseadapter.refresh.OnRefreshListener;
import com.example.commonlibrary.baseadapter.refresh.RefreshHeaderLayout;
import com.example.commonlibrary.baseadapter.refresh.RefreshTrigger;
import com.example.commonlibrary.baseadapter.swipeview.SwipeMenuRecyclerView;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class SuperRecyclerView extends SwipeMenuRecyclerView {
    private static final String TAG = SuperRecyclerView.class.getSimpleName();

    public static final int STATUS_DEFAULT = 0;

    public static final int STATUS_SWIPING_TO_REFRESH = 1;

    public static final int STATUS_RELEASE_TO_REFRESH = 2;

    public static final int STATUS_REFRESHING = 3;

    private static final boolean DEBUG = false;

    private int mStatus;

    private boolean mIsAutoRefreshing;

    private boolean mRefreshEnabled;

    private boolean mLoadMoreEnabled;

    private int mRefreshFinalMoveOffset;

    private OnRefreshListener mOnRefreshListener;

    private OnLoadMoreListener mOnLoadMoreListener;

    private RefreshHeaderLayout mRefreshHeaderContainer;

    private FrameLayout mLoadMoreFooterContainer;

    private LinearLayout mHeaderViewContainer;

    private LinearLayout mFooterViewContainer;

    private View mRefreshHeaderView;

    private View mLoadMoreFooterView;
    private LinearLayout mEmptyViewContainer;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperRecyclerView, defStyle, 0);
        @LayoutRes int refreshHeaderLayoutRes = -1;
        @LayoutRes int loadMoreFooterLayoutRes = -1;
        int refreshFinalMoveOffset = -1;
        boolean refreshEnabled;
        boolean loadMoreEnabled;

        try {
            refreshEnabled = a.getBoolean(R.styleable.SuperRecyclerView_refreshEnabled, false);
            loadMoreEnabled = a.getBoolean(R.styleable.SuperRecyclerView_loadMoreEnabled, false);
            refreshHeaderLayoutRes = a.getResourceId(R.styleable.SuperRecyclerView_refreshHeaderLayout, -1);
            loadMoreFooterLayoutRes = a.getResourceId(R.styleable.SuperRecyclerView_loadMoreFooterLayout, -1);
            refreshFinalMoveOffset = a.getDimensionPixelOffset(R.styleable.SuperRecyclerView_refreshFinalMoveOffset, -1);
        } finally {
            a.recycle();
        }

        setRefreshEnabled(refreshEnabled);

        setLoadMoreEnabled(loadMoreEnabled);

        if (refreshHeaderLayoutRes != -1) {
            setRefreshHeaderView(refreshHeaderLayoutRes);
        }
        if (loadMoreFooterLayoutRes != -1) {
            setLoadMoreFooterView(loadMoreFooterLayoutRes);
        }
        if (refreshFinalMoveOffset != -1) {
            setRefreshFinalMoveOffset(refreshFinalMoveOffset);
        }
        setStatus(STATUS_DEFAULT);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (mRefreshHeaderView != null) {
            if (mRefreshHeaderView.getMeasuredHeight() > mRefreshFinalMoveOffset) {
                mRefreshFinalMoveOffset = 0;
            }
        }
    }


    public int getStatus() {
        return mStatus;
    }

    public void setRefreshEnabled(boolean enabled) {
        this.mRefreshEnabled = enabled;
    }

    public void setLoadMoreEnabled(boolean enabled) {
        this.mLoadMoreEnabled = enabled;
        if (mLoadMoreEnabled) {
            removeOnScrollListener(mOnLoadMoreScrollListener);
            addOnScrollListener(mOnLoadMoreScrollListener);
        } else {
            removeOnScrollListener(mOnLoadMoreScrollListener);
        }
    }


    public void setLoadMoreStatus(LoadMoreFooterView.Status status) {
        if (mLoadMoreFooterView != null) {
            ((LoadMoreFooterView) mLoadMoreFooterView).setStatus(status);
        }
    }


    public boolean ismLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        setLoadMoreEnabled(true);
        this.mOnLoadMoreListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        //        TLog.e(SuperRecyclerView.class, "SuperRecyclerView_setRefreshing");
        if (mStatus == STATUS_DEFAULT && refreshing) {
            this.mIsAutoRefreshing = true;
            setStatus(STATUS_SWIPING_TO_REFRESH);
            //            TLog.e(SuperRecyclerView.class, "自动刷新");
            startScrollDefaultStatusToRefreshingStatus();
        } else if (mStatus == STATUS_REFRESHING && !refreshing) {
            this.mIsAutoRefreshing = false;
            startScrollRefreshingStatusToDefaultStatus();
        } else {
            this.mIsAutoRefreshing = false;
            Log.w(TAG, "isRefresh = " + refreshing + " current status = " + mStatus);
        }
    }

    public void setRefreshFinalMoveOffset(int refreshFinalMoveOffset) {
        this.mRefreshFinalMoveOffset = refreshFinalMoveOffset;
    }

    public void setRefreshHeaderView(View refreshHeaderView) {
        if (!(refreshHeaderView instanceof RefreshTrigger)) {
            throw new ClassCastException("Refresh header view must be an implement of RefreshTrigger");
        }

        if (mRefreshHeaderView != null) {
            removeRefreshHeaderView();
        }
        if (mRefreshHeaderView != refreshHeaderView) {
            this.mRefreshHeaderView = refreshHeaderView;
            ensureRefreshHeaderContainer();
            mRefreshHeaderContainer.addView(refreshHeaderView);
        }
    }

    public void setRefreshHeaderView(@LayoutRes int refreshHeaderLayoutRes) {
        ensureRefreshHeaderContainer();
        final View refreshHeader = LayoutInflater.from(getContext()).inflate(refreshHeaderLayoutRes, mRefreshHeaderContainer, false);
        if (refreshHeader != null) {
            setRefreshHeaderView(refreshHeader);
        }
    }

    public void setLoadMoreFooterView(View loadMoreFooterView) {
        if (mLoadMoreFooterView != null) {
            removeLoadMoreFooterView();
        }
        if (mLoadMoreFooterView != loadMoreFooterView) {
            this.mLoadMoreFooterView = loadMoreFooterView;
            ensureLoadMoreFooterContainer();
            mLoadMoreFooterContainer.addView(loadMoreFooterView);
        }
        if (loadMoreFooterView instanceof LoadMoreFooterView) {
            addBottomListener(((LoadMoreFooterView) loadMoreFooterView));
            addOnRetryListener(((LoadMoreFooterView) loadMoreFooterView));
        }
    }

    private void addOnRetryListener(LoadMoreFooterView loadMoreFooterView) {
        loadMoreFooterView.setOnRetryListener(view -> {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
        });
    }

    private void addBottomListener(final LoadMoreFooterView loadMoreFooterView) {
        loadMoreFooterView.setBottomViewClickListener(view -> {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            scrollToPosition(0);
        });
    }

    public void setLoadMoreFooterView(@LayoutRes int loadMoreFooterLayoutRes) {
        ensureLoadMoreFooterContainer();
        final View loadMoreFooter = LayoutInflater.from(getContext()).inflate(loadMoreFooterLayoutRes, mLoadMoreFooterContainer, false);
        if (loadMoreFooter != null) {
            setLoadMoreFooterView(loadMoreFooter);
        }
    }

    public View getRefreshHeaderView() {
        return mRefreshHeaderView;
    }

    public View getLoadMoreFooterView() {
        return mLoadMoreFooterView;
    }

    public LinearLayout getHeaderContainer() {
        ensureHeaderViewContainer();
        return mHeaderViewContainer;
    }

    public LinearLayout getFooterContainer() {
        ensureFooterViewContainer();
        return mFooterViewContainer;
    }

    public void addHeaderView(View headerView) {
        ensureHeaderViewContainer();
        mHeaderViewContainer.addView(headerView);
    }


    public void addEmptyView(View emptyView) {
        ensureEmptyViewContainer();
        if (mEmptyViewContainer.getChildCount() > 0) {
            mEmptyViewContainer.removeAllViews();
        }
        mEmptyViewContainer.addView(emptyView);
        setLoadMoreEnabled(false);
    }


    public void removeEmptyView() {
        if (mEmptyViewContainer != null) {
            mEmptyViewContainer.removeAllViews();
            mEmptyViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mEmptyViewContainer.requestLayout();
        }
        setLoadMoreEnabled(true);
    }


    public LinearLayout getEmptyViewContainer() {
        return mEmptyViewContainer;
    }

    public void addFooterView(View footerView) {
        ensureFooterViewContainer();
        mFooterViewContainer.addView(footerView);
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(adapter.getItemCount() - 3);
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        ensureRefreshHeaderContainer();
        ensureHeaderViewContainer();
        ensureFooterViewContainer();
        ensureEmptyViewContainer();
        ensureLoadMoreFooterContainer();
        if (adapter instanceof BaseRecyclerAdapter) {
            BaseRecyclerAdapter baseRecyclerAdapter = (BaseRecyclerAdapter) adapter;
            baseRecyclerAdapter.setFooterContainer(mFooterViewContainer);
            baseRecyclerAdapter.setHeaderContainer(mHeaderViewContainer);
            baseRecyclerAdapter.setRefreshHeaderContainer(mRefreshHeaderContainer);
            baseRecyclerAdapter.setLoadMoreFooterContainer(mLoadMoreFooterContainer);
            baseRecyclerAdapter.bindManager(getLayoutManager());
            super.setAdapter(adapter);
        }
    }


    private void ensureRefreshHeaderContainer() {
        if (mRefreshHeaderContainer == null) {
            mRefreshHeaderContainer = new RefreshHeaderLayout(getContext());
            mRefreshHeaderContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }
    }

    private void ensureLoadMoreFooterContainer() {
        if (mLoadMoreFooterContainer == null) {
            mLoadMoreFooterContainer = new FrameLayout(getContext());
            mLoadMoreFooterContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void ensureHeaderViewContainer() {
        if (mHeaderViewContainer == null) {
            mHeaderViewContainer = new LinearLayout(getContext());
            mHeaderViewContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    private void ensureEmptyViewContainer() {
        if (mEmptyViewContainer == null) {
            mEmptyViewContainer = new LinearLayout(getContext());
            mEmptyViewContainer.setOrientation(LinearLayout.VERTICAL);
            mEmptyViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void ensureFooterViewContainer() {
        if (mFooterViewContainer == null) {
            mFooterViewContainer = new LinearLayout(getContext());
            mFooterViewContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void removeRefreshHeaderView() {
        if (mRefreshHeaderContainer != null) {
            mRefreshHeaderContainer.removeView(mRefreshHeaderView);
        }
    }

    private void removeLoadMoreFooterView() {
        if (mLoadMoreFooterContainer != null) {
            mLoadMoreFooterContainer.removeView(mLoadMoreFooterView);
        }
    }

    private int mActivePointerId = -1;
    private int mLastTouchX = 0;
    private int mLastTouchY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mRefreshEnabled) {
            final int action = MotionEventCompat.getActionMasked(e);
            final int actionIndex = MotionEventCompat.getActionIndex(e);
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                    mLastTouchX = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                    mLastTouchY = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                }
                break;
                case MotionEvent.ACTION_POINTER_DOWN: {
                    mActivePointerId = MotionEventCompat.getPointerId(e, actionIndex);
                    mLastTouchX = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                    mLastTouchY = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                }
                break;
                case MotionEventCompat.ACTION_POINTER_UP: {
                    onPointerUp(e);
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mRefreshEnabled) {
            final int action = MotionEventCompat.getActionMasked(e);
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    final int index = MotionEventCompat.getActionIndex(e);
                    mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                    mLastTouchX = getMotionEventX(e, index);
                    mLastTouchY = getMotionEventY(e, index);
                }
                break;

                case MotionEvent.ACTION_MOVE: {
                    //                TLog.e(SuperRecyclerView.class, "截获到滚动事件");
                    final int index = MotionEventCompat.findPointerIndex(e, mActivePointerId);
                    if (index < 0) {
                        Log.e(TAG, "Error processing scroll; pointer index for id " + index + " not found. Did any MotionEvents get skipped?");
                        return false;
                    }
                    final int x = getMotionEventX(e, index);
                    final int y = getMotionEventY(e, index);

                    final int dx = x - mLastTouchX;
                    final int dy = y - mLastTouchY;

                    mLastTouchX = x;
                    mLastTouchY = y;

                    final boolean triggerCondition = isEnabled() && mRefreshEnabled && mRefreshHeaderView != null && isFingerDragging() && canTriggerRefresh();
                    if (DEBUG) {
                        Log.i(TAG, "triggerCondition = " + triggerCondition + "; mStatus = " + mStatus + "; dy = " + dy);
                    }
                    if (triggerCondition) {

                        final int refreshHeaderContainerHeight = mRefreshHeaderContainer.getMeasuredHeight();
                        final int refreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();

                        if (dy > 0 && mStatus == STATUS_DEFAULT) {
                            setStatus(STATUS_SWIPING_TO_REFRESH);
                            //                        TLog.e(SuperRecyclerView.class, "滚动刷新");
                            mRefreshTrigger.onStart(false, refreshHeaderViewHeight, mRefreshFinalMoveOffset);
                        } else if (dy < 0) {
                            if (mStatus == STATUS_SWIPING_TO_REFRESH && refreshHeaderContainerHeight <= 0) {
                                setStatus(STATUS_DEFAULT);
                            }
                            if (mStatus == STATUS_DEFAULT) {
                                break;
                            }
                        }

                        if (mStatus == STATUS_SWIPING_TO_REFRESH || mStatus == STATUS_RELEASE_TO_REFRESH) {
                            if (refreshHeaderContainerHeight >= refreshHeaderViewHeight) {
                                setStatus(STATUS_RELEASE_TO_REFRESH);
                            } else {
                                //                            TLog.e(SuperRecyclerView.class, "这里开始下拉刷新");
                                setStatus(STATUS_SWIPING_TO_REFRESH);
                            }
                            fingerMove(dy);
                            return true;
                        }
                    }
                }
                break;

                case MotionEventCompat.ACTION_POINTER_DOWN: {
                    final int index = MotionEventCompat.getActionIndex(e);
                    mActivePointerId = MotionEventCompat.getPointerId(e, index);
                    mLastTouchX = getMotionEventX(e, index);
                    mLastTouchY = getMotionEventY(e, index);
                }
                break;

                case MotionEventCompat.ACTION_POINTER_UP: {
                    onPointerUp(e);
                }
                break;

                case MotionEvent.ACTION_UP: {
                    onFingerUpStartAnimating();
                }
                break;

                case MotionEvent.ACTION_CANCEL: {
                    onFingerUpStartAnimating();
                }
                break;
            }
        }
        return super.onTouchEvent(e);
    }

    private boolean isFingerDragging() {
        return getScrollState() == SCROLL_STATE_DRAGGING;
    }

    public boolean canTriggerRefresh() {
        final Adapter adapter = getAdapter();
        if (adapter == null || adapter.getItemCount() <= 0) {
            return true;
        }
        View firstChild = getChildAt(0);
        int position = getChildLayoutPosition(firstChild);
        if (position == 0) {
            if (firstChild.getTop() == mRefreshHeaderContainer.getTop()) {
                return true;
            }
        }
        return false;
    }

    private int getMotionEventX(MotionEvent e, int pointerIndex) {
        return (int) (MotionEventCompat.getX(e, pointerIndex) + 0.5f);
    }

    private int getMotionEventY(MotionEvent e, int pointerIndex) {
        return (int) (MotionEventCompat.getY(e, pointerIndex) + 0.5f);
    }

    private void onFingerUpStartAnimating() {
        if (mStatus == STATUS_RELEASE_TO_REFRESH) {
            startScrollReleaseStatusToRefreshingStatus();
        } else if (mStatus == STATUS_SWIPING_TO_REFRESH) {
            startScrollSwipingToRefreshStatusToDefaultStatus();
        }
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        if (MotionEventCompat.getPointerId(e, actionIndex) == mActivePointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(e, newIndex);
            mLastTouchX = getMotionEventX(e, newIndex);
            mLastTouchY = getMotionEventY(e, newIndex);
        }
    }

    private void fingerMove(int dy) {
        int ratioDy = (int) (dy * 0.5f + 0.5f);
        int offset = mRefreshHeaderContainer.getMeasuredHeight();
        int finalDragOffset = mRefreshFinalMoveOffset;

        int nextOffset = offset + ratioDy;
        if (finalDragOffset > 0) {
            if (nextOffset > finalDragOffset) {
                ratioDy = finalDragOffset - offset;
            }
        }

        if (nextOffset < 0) {
            ratioDy = -offset;
        }
        move(ratioDy);
    }

    private void move(int dy) {
        if (dy != 0) {
            //            TLog.e(SuperRecyclerView.class, "核心刷新" + dy);
            int height = mRefreshHeaderContainer.getMeasuredHeight() + dy;
            //            TLog.e(SuperRecyclerView.class, "核心刷新偏移:" + dy + "刷新高度" + height);
            setRefreshHeaderContainerHeight(height);
            mRefreshTrigger.onMove(false, false, height);
        }
    }

    private void setRefreshHeaderContainerHeight(int height) {
        mRefreshHeaderContainer.getLayoutParams().height = height;
        mRefreshHeaderContainer.requestLayout();
    }

    private void startScrollDefaultStatusToRefreshingStatus() {
        //        TLog.e(SuperRecyclerView.class, "自动刷新");
        mRefreshTrigger.onStart(true, mRefreshHeaderView.getMeasuredHeight(), mRefreshFinalMoveOffset);

        int targetHeight = mRefreshHeaderView.getMeasuredHeight();
        int currentHeight = mRefreshHeaderContainer.getMeasuredHeight();
        startScrollAnimation(400, new AccelerateInterpolator(), currentHeight, targetHeight);
    }

    private void startScrollSwipingToRefreshStatusToDefaultStatus() {
        final int targetHeight = 0;
        final int currentHeight = mRefreshHeaderContainer.getMeasuredHeight();
        startScrollAnimation(300, new DecelerateInterpolator(), currentHeight, targetHeight);
    }

    private void startScrollReleaseStatusToRefreshingStatus() {
        mRefreshTrigger.onRelease();

        final int targetHeight = mRefreshHeaderView.getMeasuredHeight();
        final int currentHeight = mRefreshHeaderContainer.getMeasuredHeight();
        startScrollAnimation(300, new DecelerateInterpolator(), currentHeight, targetHeight);
    }

    private void startScrollRefreshingStatusToDefaultStatus() {
        mRefreshTrigger.onComplete();

        final int targetHeight = 0;
        final int currentHeight = mRefreshHeaderContainer.getMeasuredHeight();
        startScrollAnimation(400, new DecelerateInterpolator(), currentHeight, targetHeight);
    }

    private ValueAnimator mScrollAnimator;

    private void startScrollAnimation(final int time, final Interpolator interpolator, int value, int toValue) {
        if (mScrollAnimator == null) {
            mScrollAnimator = new ValueAnimator();
        }
        //cancel
        mScrollAnimator.removeAllUpdateListeners();
        mScrollAnimator.removeAllListeners();
        mScrollAnimator.cancel();
        //reset new value
        mScrollAnimator.setIntValues(value, toValue);
        mScrollAnimator.setDuration(time);
        mScrollAnimator.setInterpolator(interpolator);
        mScrollAnimator.addUpdateListener(mAnimatorUpdateListener);
        mScrollAnimator.addListener(mAnimationListener);
        mScrollAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final int height = (Integer) animation.getAnimatedValue();
            setRefreshHeaderContainerHeight(height);
            switch (mStatus) {
                case STATUS_SWIPING_TO_REFRESH: {
                    mRefreshTrigger.onMove(false, true, height);
                }
                break;

                case STATUS_RELEASE_TO_REFRESH: {
                    mRefreshTrigger.onMove(false, true, height);
                }
                break;

                case STATUS_REFRESHING: {
                    mRefreshTrigger.onMove(true, true, height);
                }
                break;
            }

        }
    };

    private Animator.AnimatorListener mAnimationListener = new SimpleAnimatorListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
            int lastStatus = mStatus;

            switch (mStatus) {
                case STATUS_SWIPING_TO_REFRESH: {
                    if (mIsAutoRefreshing) {
                        mRefreshHeaderContainer.getLayoutParams().height = mRefreshHeaderView.getMeasuredHeight();
                        mRefreshHeaderContainer.requestLayout();
                        setStatus(STATUS_REFRESHING);
                        if (mOnRefreshListener != null) {
                            if (mLoadMoreFooterView != null && mLoadMoreFooterView instanceof LoadMoreFooterView) {
                                ((LoadMoreFooterView) mLoadMoreFooterView).setStatus(LoadMoreFooterView.Status.GONE);
                            }
                            mOnRefreshListener.onRefresh();
                            mRefreshTrigger.onRefresh();
                        }
                    } else {
                        mRefreshHeaderContainer.getLayoutParams().height = 0;
                        mRefreshHeaderContainer.requestLayout();
                        setStatus(STATUS_DEFAULT);
                    }
                }
                break;
                case STATUS_RELEASE_TO_REFRESH: {
                    mRefreshHeaderContainer.getLayoutParams().height = mRefreshHeaderView.getMeasuredHeight();
                    mRefreshHeaderContainer.requestLayout();
                    setStatus(STATUS_REFRESHING);
                    if (mOnRefreshListener != null) {
                        if (mLoadMoreFooterView != null && mLoadMoreFooterView instanceof LoadMoreFooterView) {
                            ((LoadMoreFooterView) mLoadMoreFooterView).setStatus(LoadMoreFooterView.Status.GONE);
                        }
                        mOnRefreshListener.onRefresh();
                        mRefreshTrigger.onRefresh();
                    }
                }
                break;
                case STATUS_REFRESHING: {
                    mIsAutoRefreshing = false;
                    mRefreshHeaderContainer.getLayoutParams().height = 0;
                    mRefreshHeaderContainer.requestLayout();
                    setStatus(STATUS_DEFAULT);
                    mRefreshTrigger.onReset();
                }
                break;
            }

        }
    };

    private RefreshTrigger mRefreshTrigger = new RefreshTrigger() {
        @Override
        public void onStart(boolean automatic, int headerHeight, int finalHeight) {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onStart(automatic, headerHeight, finalHeight);
            }
        }

        @Override
        public void onMove(boolean finished, boolean automatic, int moved) {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onMove(finished, automatic, moved);
            }
        }

        @Override
        public void onRefresh() {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onRefresh();
            }
        }

        @Override
        public void onRelease() {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onRelease();
            }
        }

        @Override
        public void onComplete() {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onComplete();
            }
        }

        @Override
        public void onReset() {
            if (mRefreshHeaderView != null && mRefreshHeaderView instanceof RefreshTrigger) {
                RefreshTrigger trigger = (RefreshTrigger) mRefreshHeaderView;
                trigger.onReset();
            }
        }
    };

    private OnLoadMoreScrollListener mOnLoadMoreScrollListener = new OnLoadMoreScrollListener() {
        @Override
        public void onLoadMore(RecyclerView recyclerView) {
            if (mLoadMoreEnabled && mOnLoadMoreListener != null && !isRefreshing()) {
                if (mLoadMoreFooterView != null && mLoadMoreFooterView instanceof LoadMoreFooterView
                        && ((LoadMoreFooterView) mLoadMoreFooterView).getStatus() != LoadMoreFooterView.Status.LOADING) {
                    ((LoadMoreFooterView) mLoadMoreFooterView).setStatus(LoadMoreFooterView.Status.LOADING);
                }
                mOnLoadMoreListener.loadMore();
            }
        }
    };

    private boolean isRefreshing() {
        if (getParent() != null && (getParent() instanceof SwipeRefreshLayout || getParent() instanceof CustomSwipeRefreshLayout)) {
            if (getParent() instanceof SwipeRefreshLayout) {
                SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) getParent();
                return refreshLayout.isRefreshing();
            } else {
                return ((CustomSwipeRefreshLayout) getParent()).isRefreshing();
            }
        } else {
            return mStatus != STATUS_DEFAULT;
        }
    }

    private void setStatus(int status) {
        this.mStatus = status;

    }


    private static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}
