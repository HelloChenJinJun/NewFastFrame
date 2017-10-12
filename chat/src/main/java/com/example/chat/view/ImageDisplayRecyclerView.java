package com.example.chat.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.chat.adapter.ImageDisplayAdapter;
import com.example.chat.util.LogUtil;
import com.example.chat.util.PixelUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/18      16:18
 * QQ:             1981367757
 */

public class ImageDisplayRecyclerView extends RecyclerView {
        private int maxFlingVelocity = 8000;
        public int currentScrollOffset = 0;
        private CustomLinearSnapHelper mCustomLinearSnapHelper;
        private int mPadding;
        private int showCardWidth;
        private int cardWidth;
        private int currentPosition;
        private float scale = 0.9f;


        public void setScale(float scale) {
                this.scale = scale;
        }

        public void setMaxFlingVelocity(int maxFlingVelocity) {
                this.maxFlingVelocity = maxFlingVelocity;
        }

        public ImageDisplayRecyclerView(Context context) {
                this(context, null);
        }

        public ImageDisplayRecyclerView(Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);

        }

        public ImageDisplayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
                init();
        }

        private void init() {
                mCustomLinearSnapHelper = new CustomLinearSnapHelper();
                mCustomLinearSnapHelper.attachToRecyclerView(this);
                addOnScrollListener(new OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        if (currentScrollOffset == 0 || currentScrollOffset == currentPosition * cardWidth) {
                                                mCustomLinearSnapHelper.setNeedScroll(false);
                                        } else {
                                                mCustomLinearSnapHelper.setNeedScroll(true);
                                        }
                                }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                currentScrollOffset += dx;
                                computeCurrentPosition();
                                scrollChange();
                        }

                });
                post(new Runnable() {
                        @Override
                        public void run() {
                                LogUtil.e("mPadding" + mPadding);
                                LogUtil.e("cardWidth" + showCardWidth);
                                cardWidth = getWidth() - PixelUtil.dp2px(2 * (mPadding + showCardWidth));
                                LogUtil.e("cardWidth" + cardWidth);
                        }
                });
        }

        private void scrollChange() {
                int offset = currentScrollOffset - currentPosition * cardWidth;
                float percent = (float) Math.max(Math.abs(offset) * 1.0 / cardWidth, 0.0001);
                View leftView = null;
                View rightView = null;
                View currentView;
                if (currentPosition > 0) {
                        leftView = getLayoutManager().findViewByPosition(currentPosition - 1);
                }
                currentView = getLayoutManager().findViewByPosition(currentPosition);
                if (currentPosition < getAdapter().getItemCount() - 1) {
                        rightView = getLayoutManager().findViewByPosition(currentPosition + 1);
                }

                if (leftView != null) {
                        leftView.setScaleY((1 - scale) * percent + scale);
                }
                if (rightView != null) {
                        rightView.setScaleY((1 - scale) * percent + scale);
                }
                if (currentView != null) {
                        currentView.setScaleY((scale - 1) * percent + 1);
                }
        }

        private void computeCurrentPosition() {
                if (cardWidth <= 0) {
                        return;
                }
                if (Math.abs(currentScrollOffset - currentPosition * cardWidth) >= cardWidth) {
                        currentPosition = currentScrollOffset / cardWidth;
                }
        }

        @Override
        public void setAdapter(Adapter adapter) {
                if (adapter instanceof ImageDisplayAdapter) {
                        ImageDisplayAdapter imageDisplayAdapter = (ImageDisplayAdapter) adapter;
                        mPadding = imageDisplayAdapter.getPagePadding();
                        showCardWidth = imageDisplayAdapter.getShowCardWidth();
                }
                super.setAdapter(adapter);
        }


        @Override
        public boolean fling(int velocityX, int velocityY) {
                if (velocityX > maxFlingVelocity) {
                        velocityX = maxFlingVelocity;
                }
                if (velocityX < -maxFlingVelocity) {
                        velocityX = (-maxFlingVelocity);
                }
                return super.fling(velocityX, velocityY);
        }

        private class CustomLinearSnapHelper extends LinearSnapHelper {
                private boolean needScroll;

                public void setNeedScroll(boolean needScroll) {
                        this.needScroll = needScroll;
                }


                @Override
                public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View targetView) {
                        if (!needScroll) {
                                LogUtil.e("不需要" +
                                        "滚动");
                                return new int[]{0, 0};
                        }
                        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
                }
        }
}
