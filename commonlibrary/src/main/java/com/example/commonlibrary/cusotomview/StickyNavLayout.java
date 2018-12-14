package com.example.commonlibrary.cusotomview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.commonlibrary.R;
import com.example.commonlibrary.utils.CommonLogger;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;


public class StickyNavLayout extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "StickyNavLayout";


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll");
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    private int TOP_CHILD_FLING_THRESHOLD = 0;

    private Map<Integer, SoftReference<RecyclerView.OnScrollListener>> mMap = new HashMap<>();


    @Override
    public boolean onNestedFling(View target, float velocityX, final float velocityY, boolean consumed) {
        //如果是recyclerView 根据判断第一个元素是哪个位置可以判断是否消耗
        //这里判断如果第一个元素的位置是大于TOP_CHILD_FLING_THRESHOLD的
        //认为已经被消耗，在animateScroll里不会对velocityY<0时做处理
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            if (mMap.get(recyclerView.hashCode()) == null || mMap.get(recyclerView.hashCode()).get() == null) {
                RecyclerView.OnScrollListener item = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        View firstChild = recyclerView.getChildAt(0);
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
                        if (childAdapterPosition == TOP_CHILD_FLING_THRESHOLD && firstChild.getY() >= 0) {
                            if (recyclerView.getScrollState() == SCROLL_STATE_SETTLING) {
                                if (dy < 0) {
                                    animateScroll(velocityY, computeDuration(0), false);
                                }
                            } else {
                                scrollBy(0, dy);
                            }
                        }
                    }
                };
                mMap.put(recyclerView.hashCode(), new SoftReference<>(item));
                recyclerView.addOnScrollListener(item);
            }
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            if (velocityY < 0) {
                consumed = !(childAdapterPosition == TOP_CHILD_FLING_THRESHOLD && firstChild.getY() >= 0);
            } else {
                consumed = getScrollY() >= mTopViewHeight;
            }
        }
        CommonLogger.e("ve" + velocityY + "consume:" + consumed);
        if (!consumed) {
            animateScroll(velocityY, computeDuration(0), consumed);
        } else {
            animateScroll(velocityY, computeDuration(velocityY), consumed);
        }
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        final RecyclerView recyclerView = (RecyclerView) target;
        final View firstChild = recyclerView.getChildAt(0);
        final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
        Log.e("onNestedPreFling", "position:" + childAdapterPosition + "    x:" + firstChild.getY() + "");
        //不做拦截 可以传递给子View
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    /**
     * 根据速度计算滚动动画持续时间
     *
     * @param velocityY
     * @return
     */
    private int computeDuration(float velocityY) {
        final int distance;
        if (velocityY > 0) {
            distance = Math.abs(mTop.getHeight() - getScrollY());
        } else {
            distance = Math.abs(mTop.getHeight() - (mTop.getHeight() - getScrollY()));
        }


        final int duration;
        velocityY = Math.abs(velocityY);
        if (velocityY > 0) {
            duration = 3 * Math.round(1000 * (distance / velocityY));
        } else {
            final float distanceRatio = (float) distance / getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }

        return duration;

    }

    private void animateScroll(float velocityY, final int duration, boolean consumed) {
        final int currentOffset = getScrollY();
        final int topHeight = mTop.getHeight();
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(mInterpolator);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    CommonLogger.e(":value:" + animation.getAnimatedValue());
                    if (animation.getAnimatedValue() instanceof Integer) {
                        scrollTo(0, (Integer) animation.getAnimatedValue());
                    }
                }
            });
        } else {
            mOffsetAnimator.cancel();
            CommonLogger.e("cancel");
        }
        mOffsetAnimator.setDuration(Math.min(duration, 600));

        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(currentOffset, topHeight);
            CommonLogger.e("currentOffset:" + currentOffset + "   topHeight:" + topHeight);
            mOffsetAnimator.start();
        } else {
            //如果子View没有消耗down事件 那么就让自身滑倒0位置
            if (!consumed) {
                mOffsetAnimator.setIntValues(currentOffset, 0);
                mOffsetAnimator.start();
            }

        }
    }

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;

    private int mTopViewHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private ValueAnimator mOffsetAnimator;
    private Interpolator mInterpolator;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private boolean mDragging;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    //    @Override
    //    public boolean onTouchEvent(MotionEvent event)
    //    {
    //        initVelocityTrackerIfNotExists();
    //        mVelocityTracker.addMovement(event);
    //        int action = event.getAction();
    //        float y = event.getY();
    //
    //        switch (action)
    //        {
    //            case MotionEvent.ACTION_DOWN:
    //                if (!mScroller.isFinished())
    //                    mScroller.abortAnimation();
    //                mLastY = y;
    //                return true;
    //            case MotionEvent.ACTION_MOVE:
    //                float dy = y - mLastY;
    //
    //                if (!mDragging && Math.abs(dy) > mTouchSlop)
    //                {
    //                    mDragging = true;
    //                }
    //                if (mDragging)
    //                {
    //                    scrollBy(0, (int) -dy);
    //                }
    //
    //                mLastY = y;
    //                break;
    //            case MotionEvent.ACTION_CANCEL:
    //                mDragging = false;
    //                recycleVelocityTracker();
    //                if (!mScroller.isFinished())
    //                {
    //                    mScroller.abortAnimation();
    //                }
    //                break;
    //            case MotionEvent.ACTION_UP:
    //                mDragging = false;
    //                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
    //                int velocityY = (int) mVelocityTracker.getYVelocity();
    //                if (Math.abs(velocityY) > mMinimumVelocity)
    //                {
    //                    fling(-velocityY);
    //                }
    //                recycleVelocityTracker();
    //                break;
    //        }
    //
    //        return super.onTouchEvent(event);
    //    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.sticky_header);
        mNav = findViewById(R.id.sticky_tab);
        View view = findViewById(R.id.sticky_display);


        if (!(view instanceof ViewPager)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        CommonLogger.e(":y:" + y);
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }


}
