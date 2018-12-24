package com.example.chat.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.customview.widget.ViewDragHelper;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/24     10:27
 */
public class DragFrameLayout extends FrameLayout {
    public static final String TAG = "DragFramlayout";

    private ViewDragHelper mDragHelper;

    //#00000000
    public int DEF_BG_COLOR = 0xff000000;

    /**
     * 拖动是否进行缩放变化
     */
    private boolean mDragScale = true;

    /**
     * 拖拽时是否进行缩放操作，默认TRUE
     *
     * @param dragScale TRUE：是 FALSE：否
     */
    public void setDragScale(boolean dragScale) {
        mDragScale = dragScale;
    }

    public DragFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).getWindow().getDecorView().setBackgroundColor(DEF_BG_COLOR);
        }
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //允许子视图进行拖拽,这里默认都允许
                return true;
            }

            /**是否允许所有方向拖拽*/
            boolean needDrag;

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (needDrag) {
                    return top;
                }
                if (top < 0) {//只允许向下拖拽
                    top = 0;
                } else if (top > 100) {//向下拖拽超过100px后，释放允许任何方向拖拽
                    needDrag = true;
                }
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return needDrag ? left : 0;
            }

            /**
             *
             * @param changedView   被拖动的View
             * @param left          水平拖动距离
             * @param top           垂直拖动距离
             * @param dx            每次拖拽产生的水平距离x2-x1
             * @param dy            每次拖拽产生的垂直距离y2-y1
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                float present = 1 - (top * 1.0f) / (getHeight());

                if (getContext() instanceof Activity) {
                    int alpah = Math.min((int) (255 * present), 255);
                    ((Activity) getContext()).getWindow().getDecorView().setBackgroundColor(Color.argb(alpah, 0, 0, 0));
                }

                float maxScale = Math.min(present, 1.0f);//Max,1.0f
                float minScale = Math.max(0.5f, maxScale);//Min,5.0f;

                changedView.setScaleX(minScale);
                changedView.setScaleY(minScale);
            }

            boolean mNeedRelease;

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (mNeedRelease) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).onBackPressed();
                    }
                } else {
                    needDrag = false;
                    //让视图归位
                    mDragHelper.settleCapturedViewAt(finalLeft, finalTop);
                    releasedChild.setScaleX(1.0f);
                    releasedChild.setScaleY(1.0f);
                    invalidate();
                }
            }
        });

        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                if (mNeedRelease) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).onBackPressed();
                    }
                } else {
                    needDrag = false;
                    //让视图归位,
                    mDragHelper.settleCapturedViewAt(finalLeft, finalTop);
                    if (mDragScale) {
                        releasedChild.setScaleX(1.0f);
                        releasedChild.setScaleY(1.0f);
                    }
                    invalidate();
                }
            }

            boolean mNeedRelease;

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                mNeedRelease = top > getHeight() * 0.25;//Release

                //ChangeBg
                float present = 1 - (top * 1.0f) / (getHeight());
                if (mOnDragListener != null) {
                    mOnDragListener.onDrag(present);
                }
                //ChangeScale
                if (mDragScale) {
                    float maxScale = Math.min(present, 1.0f);//Max,1.0f
                    float minScale = Math.max(0.5f, maxScale);//Min,5.0f;

                    //                if(present>1.0f){
                    //                    present=1.0f;
                    //                }else if(present<0.5f){
                    //                    present=0.5f;
                    //                }
                    changedView.setScaleX(minScale);
                    changedView.setScaleY(minScale);
                }


                Log.i(TAG, "Top=" + top + "Release=" + getHeight() * 0.25 + "Present=" + present);
            }

            boolean needDrag;

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (needDrag) {
                    return top;
                }
                if (top < 0) {
                    top = 0;
                } else if (top > 100) {//释放允许任何方向拖拽
                    needDrag = true;
                }
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return needDrag ? left : 0;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getHeight() / 2;
            }
        });
    }


    public interface OnDragListener {
        public void onDrag(float value);
    }


    private OnDragListener mOnDragListener;

    public void setOnDragListener(OnDragListener onDragListener) {
        mOnDragListener = onDragListener;
    }

    int finalLeft;
    int finalTop;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        finalLeft = getChildAt(0).getLeft();
        finalTop = getChildAt(0).getTop();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
