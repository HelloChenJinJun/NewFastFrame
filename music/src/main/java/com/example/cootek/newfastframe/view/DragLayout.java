package com.example.cootek.newfastframe.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibrary.utils.DensityUtil;
import com.nineoldandroids.view.ViewHelper;


/**
 * 项目名称:    FilpperLayoutDemo
 * 创建人:        陈锦军
 * 创建时间:    2016/11/4      12:25
 * QQ:             1981367757
 */

public class DragLayout extends ViewGroup {


    /**
     * 正在拖拽
     */
    public static final int DRAG_STATE_DRAGING = 1;
    /**
     * 关闭状态
     */
    public static final int DRAG_STATE_CLOSE = 0;
    /**
     * 打开状态
     */
    public static final int DRAG_STATE_OPEN = 2;
    /**
     * 左遍的菜单布局
     */
    private ViewGroup menu;
    /**
     * 右边的主界面布局
     */
    private ViewGroup content;


    private int range;
    private int menuRange;


    private OnDragDeltaChangeListener mListener;
    private boolean isOnClick = false;
    private boolean intercept = false;

    public void setListener(OnDragDeltaChangeListener listener) {
        mListener = listener;
    }

    /**
     * 手势监听器
     */
    private GestureDetector mGestureDetector;


    /**
     * 拖拽子view的辅助类
     */
    private ViewDragHelper mViewDragHelper;


    private int width;
    private int height;
    private boolean isUseAnimation = true;


    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                                如果水平方向的偏移量大于竖直方向的偏移量，就消化该事件,
                return Math.abs(distanceX) > Math.abs(distanceY) || super.onScroll(e1, e2, distanceX, distanceY);

            }
        });
//                永远要记得，该工具实现的拖拉是基于控件位置的改变来实现的
        mViewDragHelper = ViewDragHelper.create(this, new DragViewCallBack());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && getCurrentState() == DRAG_STATE_OPEN && isShouldIntercept(ev)) {
            isOnClick = true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP && getCurrentState() == DRAG_STATE_OPEN && isShouldIntercept(ev)) {
            if (isOnClick) {
                isOnClick = false;
                closeMenu();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldIntercept(MotionEvent ev) {
        if (mode == LEFT_MODE) {
            return ev.getX() > range;
        } else {
            return ev.getX() < (width - range);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//                如果符合条件直接截获事件由自己在onTouchEvent方法中处理
//                如果在释放自动滑动的过程中点击，会导致停止动画
        if (isIntercept()) {
            return false;
        }
        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
//                这里是为了防止在滑动的过程中,突然点击,导致被点击的页面停止滑动的的情况发生

        if (!result && mViewDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING && ev.getAction() == MotionEvent.ACTION_UP) {
            if (mode == LEFT_MODE) {
                if (content.getLeft() < range / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
            } else {
                if (content.getRight() < range / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//                在这里直接交给viewDragHelper处理
        mViewDragHelper.processTouchEvent(event);
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView() {
        if (menu != null) {
            return;
        }
        menu = (ViewGroup) getChildAt(0);
        if (mode == RIGHT_MODE) {
            menu.setPadding(DensityUtil.dip2px(getContext(),80), DensityUtil.dip2px(getContext(),20),DensityUtil.dip2px(getContext(),20),DensityUtil.dip2px(getContext(),20));
        }else {
            menu.setPadding(DensityUtil.dip2px(getContext(),20), DensityUtil.dip2px(getContext(),20),DensityUtil.dip2px(getContext(),80),DensityUtil.dip2px(getContext(),20));
        }
        content = (ViewGroup) getChildAt(1);
        menu.setClickable(true);
        content.setClickable(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize(w,h);
        initView();

    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initSize(getMeasuredWidth(),getMeasuredHeight());
        initView();
        for (int i = 0; i < getChildCount(); i++) {
            if (i == 0) {
//                MeasureSpec.makeMeasureSpec(range, MeasureSpec.EXACTLY)
                getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
            } else {
                getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    private void initSize(int measuredWidth, int measuredHeight) {
        width = measuredWidth;
        height = measuredHeight;
        range = (int) (width * 0.8f);
        menuRange = width / 4;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menu.layout(menu.getLeft(), 0, width + menu.getLeft(), height);
        content.layout(content.getLeft(), 0, width + content.getLeft(), height);
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }

    public boolean isIntercept() {
        return intercept;
    }

    public void switchMenu() {
        if (getCurrentState() == DRAG_STATE_OPEN) {
            closeMenu();
        } else if (getCurrentState() == DRAG_STATE_CLOSE) {
            openMenu();
        }
    }

    public int getDistance() {
        if (width==0) {
            return (int) ((DensityUtil.getScreenWidth(getContext()))*0.2f);
        }else {
            return width-range;
        }
    }


    private class DragViewCallBack extends ViewDragHelper.Callback {
        /**
         * 是否捕获子view
         *
         * @param child     子view
         * @param pointerId 被拖拽或真正做动画的子iewID
         * @return 结果
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 这里进行边界检查,默认是距离父布局左边为0的，即不允许拖拽
         *
         * @param child 子view
         * @param left  最新的子view控件的左边距离父布局的控件的左边
         * @param dx    偏移量
         * @return 适合的距离父布局左边的距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int result;
            if (mode == LEFT_MODE) {
                if (child.equals(content)) {
                    if (left < 0) {
                        result = 0;
                    } else if (left > range) {
                        result = range;
                    } else {
                        result = left;
                    }
                } else {
                    if (content.getLeft() + dx > range) {
                        result = range;
                    } else if (content.getLeft() + dx < 0) {
                        result = 0;
                    } else {
                        result = content.getLeft() + dx;
                    }
                }
            } else {
                if (child.equals(content)) {
                    if (left > 0) {
                        result = 0;
                    } else if (left < -range) {
                        result = -range;
                    } else {
                        result = left;
                    }
                } else {
                    if (content.getLeft() + dx > 0) {
                        result = 0;
                    } else if (content.getLeft() + dx < -range) {
                        result = -range;
                    } else {
                        result = content.getLeft() + dx;
                    }
                }
            }
            return result;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);

        }

        /**
         * 设置水平移动的范围
         *
         * @param child 子view
         * @return 移动的最大距离
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        /**
         * 被拖拽的子view释放的瞬间回调
         *
         * @param releasedChild 被拖拽的子view
         * @param xvel          x轴上的速度
         * @param yvel          y轴上的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel > 0) {
//                                向右滑动的趋势
                if (mode == LEFT_MODE) {
                    openMenu();
                } else {
                    closeMenu();
                }
            } else if (xvel < 0) {
//                                向左滑动的趋势
                if (mode == LEFT_MODE) {
                    closeMenu();
                } else {
                    openMenu();
                }
            } else {
                if (mode == LEFT_MODE) {
                    if (content.getLeft() < range / 2) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                } else {
                    if (content.getRight() < range / 2) {
                        openMenu();
                    } else {
                        closeMenu();
                    }
                }
            }
        }


        /**
         * 子view拖拽过程中位置改变的回调
         *
         * @param changedView 被拖拽的子view
         * @param left        距离父布局左边的距离
         * @param top         距离父布局上面的距离
         * @param dx          x方向的偏移量
         * @param dy          y方向的偏移量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (dx == 0) {
                return;
            }
            float percent = 0;
            if (mode == LEFT_MODE) {
                percent = left / (float) range;
                if (changedView.equals(menu)) {
                    content.layout(left, 0, left + width, height);
                }
                float desWidth = (menuRange * (percent - 1));
                menu.layout((int) desWidth, 0, (int) (desWidth + width), height);
            } else {
                percent = (-left) / (float) range;

                if (changedView.equals(menu)) {
                    content.layout(left, 0, left + width, height);
                }
                float desWidth = (menuRange * (1 - percent));
                menu.layout((int) desWidth, 0, (int) (desWidth + width), height);

            }
//                        0~~1之间
            int dragState = getCurrentState();
            if (dragState == DRAG_STATE_OPEN) {
                if (mListener != null) {
                    mListener.onOpenMenu();
                }
            } else if (dragState == DRAG_STATE_CLOSE) {
                if (mListener != null) {
                    mListener.onCloseMenu();
                }
            } else {
                ViewHelper.setScaleX(content, (1 - percent * 0.20f));
                ViewHelper.setScaleY(content, (1 - percent * 0.20f));
                ViewHelper.setScaleX(menu, (0.8f + percent * 0.20f));
                ViewHelper.setScaleY(menu, (0.8f + percent * 0.20f));
                if (mListener != null) {
                    mListener.onDrag(changedView, percent);
                }
            }
        }
    }


    public static final int LEFT_MODE = 0;
    public static final int RIGHT_MODE = 1;
    private int mode = LEFT_MODE;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 根据偏移量来判断目前的状态
     *
     * @return 状态
     */
    public int getCurrentState() {
        int marginLeft = content.getLeft();
        if (marginLeft == 0) {
            return DRAG_STATE_CLOSE;
        } else if ((marginLeft == range&&mode==LEFT_MODE)||(marginLeft==(-range)&&mode==RIGHT_MODE)) {
            return DRAG_STATE_OPEN;
        } else {
            return DRAG_STATE_DRAGING;
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isUseAnimation) {
//                        如果content内容界面的控件的左偏移量和动画最终的位置不一致就return true(也就是可以滑动的情况下),内部是采用Scroller进行计算的，所以需要重绘
            if (mViewDragHelper.smoothSlideViewTo(content, 0, 0)) {
//                                兼容版本,重绘会调用computeScroll()
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
//                        如果没有动画就直接设布局到终点
            content.layout(0, 0, width, height);
            if (mListener != null) {
                mListener.onCloseMenu();
            }
        }
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        int newRange = range;
        if (mode == RIGHT_MODE) {
            newRange = -range;
        }
        if (isUseAnimation) {
            if (mViewDragHelper.smoothSlideViewTo(content, newRange, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
//                        没有使用动画
            content.layout(newRange, 0, newRange + width, height);
            if (mListener != null) {
                mListener.onOpenMenu();
            }
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
//                调用该方法，内部进行view的动画效果的重绘,return true 表明还在进行中，false表明已经完成了,这里进行控件位置的移动
        if (mViewDragHelper.continueSettling(true)) {
//                        不断调用computeScroll
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
