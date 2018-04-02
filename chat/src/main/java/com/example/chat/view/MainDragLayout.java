package com.example.chat.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.listener.OnDragDeltaChangeListener;
import com.example.chat.util.LogUtil;
import com.nineoldandroids.view.ViewHelper;


/**
 * 项目名称:    FilpperLayoutDemo
 * 创建人:        陈锦军
 * 创建时间:    2016/11/4      12:25
 * QQ:             1981367757
 */

public class MainDragLayout extends ViewGroup {


        private static final boolean SHADOW = true;
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
        private ContentView content;


        private int range;


        private OnDragDeltaChangeListener mListener;
        private ImageView ivShadow;
        private boolean isOnClick = false;

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


        public MainDragLayout(Context context) {
                this(context, null);
        }

        public MainDragLayout(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public MainDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
                if (ev.getAction() == MotionEvent.ACTION_DOWN && getCurrentState() == DRAG_STATE_OPEN && ev.getX() > range) {
                        isOnClick = true;
                }
                if (ev.getAction() == MotionEvent.ACTION_UP && getCurrentState() == DRAG_STATE_OPEN && ev.getX() > range) {
                        if (isOnClick) {
                                isOnClick = false;
                                closeMenu();
                                return true;
                        }
                }
                return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
//                如果符合条件直接截获事件由自己在onTouchEvent方法中处理
//                如果在释放自动滑动的过程中点击，会导致停止动画
                boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
//                这里是为了防止在滑动的过程中,突然点击,导致被点击的页面停止滑动的的情况发生
                if (!result && mViewDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING && ev.getAction() == MotionEvent.ACTION_UP) {
                        if (getChildAt(2).getLeft() < range / 2) {
                                closeMenu();
                        } else {
                                openMenu();
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
                menu = (ViewGroup) getChildAt(0);
                View view = getChildAt(getChildCount() - 1);
                if (view instanceof ContentView) {
                        content = (ContentView) view;
                        LogUtil.e("range" + range);
                        LogUtil.e("range1" + getWidth() * 0.8);
                        content.setRange((int) (getWidth() * 0.8));
                }
                if (SHADOW) {
                        ivShadow = new ImageView(getContext());
                        LogUtil.e("这里执行了吗");
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                        ivShadow.setImageResource(R.drawable.shadow);
                        addView(ivShadow, 1, layoutParams);
                }
                menu.setClickable(true);
                content.setClickable(true);
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                width = w;
                height = h;
                range = (int) (width * 0.8f);
                if (content != null) {
                        content.setRange(range);
                }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
                }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
                int size = getChildCount();
                menu.layout(0, 0, width, height);
                int left = getChildAt(2).getLeft();
                for (int i = 1; i < size; i++) {
                        getChildAt(i).layout(left, 0, left + width, height);
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
                        if (child.equals(content)) {
                                if (left < 0) {
                                        result = 0;
                                } else if (left > range) {
                                        result = range;
                                } else {
                                        result = left;
                                }
                        } else {
                                if (getChildAt(2).getLeft() + dx > range) {
                                        result = range;
                                } else if (getChildAt(2).getLeft() + dx < 0) {
                                        result = 0;
                                } else {
                                        result = getChildAt(2).getLeft() + dx;
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
                        LogUtil.e("onViewReleased");
                        if (xvel > 0) {
//                                向右滑动的趋势
                                openMenu();
                        } else if (xvel < 0) {
//                                向左滑动的趋势
                                closeMenu();
                        } else {
                                if (getChildAt(2).getLeft() < range / 2) {
                                        closeMenu();
                                } else {
                                        openMenu();
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
                        if (changedView.equals(menu)) {
//                                如果拖拽的是menu，布局不变
                                menu.layout(0, 0, width, height);
//                                content.layout(marginLeft, 0, marginLeft + width, height);
//                                这里如果在屏幕上手指按定，就会出现dx=0,dy=1的情况
                                content.layout(left, 0, left + width, height);
                        }
//                                如果是内容布局，就随拖拽位移而变化,因为在smoothSlideViewTo中会进行控件边缘和内容边缘之间的移动,所以这里content要随时改变布局
//                                content.layout(marginLeft, 0, marginLeft + width, height);
//                        0~~1之间
                        float percent = left / (float) range;
                        float desWidth = (float) (width / 4.0 * (percent - 1));
//                        进行内容移动，本身控件位置不变
                        ViewHelper.setTranslationX(menu, desWidth);
                        if (SHADOW) {
//                                ivShadow.layout(marginLeft, 0, marginLeft + width, height);
                                ivShadow.layout(left, 0, left + width, height);
//                                ivShadow.setAlpha(percent * 225);
//                                ViewHelper.setAlpha(ivShadow, percent * 255);
//                                阴影效果
                                float f1 = 1 - percent * 0.5f;
                                ViewHelper.setScaleX(ivShadow, f1 * 1.2f * (1 - percent * 0.10f));
                                ViewHelper.setScaleY(ivShadow, f1 * 1.85f * (1 - percent * 0.10f));
                        }
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
                                if (mListener != null) {
                                        mListener.onDrag(changedView, percent);
                                }
                        }
                }
        }

        /**
         * 根据偏移量来判断目前的状态
         *
         * @return 状态
         */
        public int getCurrentState() {
                int marginLeft = getChildAt(2).getLeft();
                if (marginLeft == 0) {
                        return DRAG_STATE_CLOSE;
                } else if (marginLeft == range) {
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
                if (isUseAnimation) {
                        if (mViewDragHelper.smoothSlideViewTo(content, range, 0)) {
                                ViewCompat.postInvalidateOnAnimation(this);
                        }
                } else {
//                        没有使用动画
                        content.layout(range, 0, range + width, height);
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
