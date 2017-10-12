package com.example.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.chat.util.PixelUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/24      14:30
 * QQ:             1981367757
 */

public class CircleLoadView extends View {
        private Paint mPaint;
        private Paint bgPaint;
        private int paddingSize;
        private RectF rect;

        public CircleLoadView(Context context) {
                this(context, null);
        }

        public CircleLoadView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public CircleLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                setBackgroundColor(Color.TRANSPARENT);
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.WHITE);
                paddingSize = PixelUtil.dp2px(3);
                mPaint.setStrokeWidth(paddingSize);
                bgPaint = new Paint();
                bgPaint.setAntiAlias(true);
                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setColor(Color.WHITE);
        }


        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
        }

        private float startAngle = -90f;
        private float endAngle = 0f;


        @Override
        public void draw(Canvas canvas) {
                rect = new RectF(2 * paddingSize, 2 * paddingSize, getMeasuredWidth() - 2 * paddingSize, getMeasuredWidth() - 2 * paddingSize);
                super.draw(canvas);
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
//                画一个空心圆
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2, mPaint);
//                一个实心圆
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2 - paddingSize, bgPaint);
                canvas.drawArc(rect, startAngle, endAngle, true, bgPaint);
        }


        public void updateProgress(int progress) {
                endAngle = (float) (360 / 100.0 * progress);
                invalidate();
        }
}
