package com.example.commonlibrary.cusotomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      10:35
 * QQ:             1981367757
 */

public class ViewPagerIndicator extends LinearLayout {
    private int spaceHeight;
    private Paint mPaint;
    private Path mPath;
    private int mLineWidth;
    private float mTranslationX = 0;
    private int mVisibleTabCount;
    private int tabSelectedColor;
    private int tabNormalColor;
    private int tabTextSize;
    private ViewPager mViewPager;
    private int lineHeight = 2;
    private int margin;


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取可见的tab数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleTabCount = a.getInt(R.styleable.ViewPagerIndicator_visibleTabCount, 3);
        tabSelectedColor = a.getColor(R.styleable.ViewPagerIndicator_tabSelectedColor, getResources().getColor(R.color.orange_red_300));
        spaceHeight = (int) a.getDimension(R.styleable.ViewPagerIndicator_spaceHeight, 2);
        tabNormalColor = a.getColor(R.styleable.ViewPagerIndicator_tabNormalColor, getResources().getColor(R.color.black_transparency_500));
        lineHeight = (int) a.getDimension(R.styleable.ViewPagerIndicator_lineHeight, 2);
        tabTextSize = (int) a.getDimension(R.styleable.ViewPagerIndicator_tabTextSize, 9);
        margin = (int) a.getDimension(R.styleable.ViewPagerIndicator_margin, 0);
        int lineColor = a.getColor(R.styleable.ViewPagerIndicator_lineColor, getResources().getColor(R.color.orange_500));
        if (mVisibleTabCount < 0) {
            mVisibleTabCount = 3;
        }
        a.recycle();
        mPath = new Path();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
        setOrientation(HORIZONTAL);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mTranslationX, getHeight() - lineHeight + spaceHeight);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 通过布局添加Tab
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0) {
            return;
        }

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 1;
            lp.width = 0;
        }

        setItemClickEvent();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    /**
     * 指示器跟随手指进行滚动
     */
    public void scroll(int position, float offset) {
        if (getChildAt(position + 1) != null) {
            TextView end = (TextView) getChildAt(position + 1);
            Rect bound = new Rect();
            end.getPaint().getTextBounds(end.getText().toString(), 0, end.getText().length(), bound);
            int endLength = bound.width() + 20;
            int endTranslationX = (end.getMeasuredWidth() - endLength) / 2 + end.getLeft();
            TextView start = (TextView) getChildAt(position);
            start.getPaint().getTextBounds(start.getText().toString(), 0, start.getText().length(), bound);
            int startLength = bound.width() + 20;
            int startTranslationX = (start.getMeasuredWidth() - startLength) / 2 + start.getLeft();
            mTranslationX = startTranslationX + (endTranslationX - startTranslationX) * offset;
            float value = (endLength - startLength) * offset;
            mPath.reset();
            mPath.moveTo(0, 0);
            mPath.lineTo(value + startLength, 0);
            mPath.lineTo(value + startLength, lineHeight);
            mPath.lineTo(0, value + startLength);
            mPath.close();
        }
        invalidate();
    }

    private void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            removeAllViews();
            requestLayout();
            for (int i = 0; i < titles.size(); i++) {
                addView(generateTextView(titles.get(i), i, titles.size()));
            }
        }
        setItemClickEvent();
    }


    /**
     * 根据title创建tab
     */
    private View generateTextView(String title, int index, int size) {
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
        LayoutParams lp;
        if (margin == 0) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (index != size) {
                lp.setMargins(0, 0, margin, 0);
            }
        }
        tv.setLayoutParams(lp);
        if (mLineWidth == 0) {
            Rect bound = new Rect();
            tv.getPaint().getTextBounds(tv.getText().toString(), 0, tv.getText().length(), bound);
            mLineWidth = bound.width();
            mLineWidth += 20;
            mPath = new Path();
            mPath.moveTo(0, 0);
            mPath.lineTo(mLineWidth, 0);
            mPath.lineTo(mLineWidth, lineHeight);
            mPath.lineTo(0, lineHeight);
            mPath.close();
        }
        return tv;
    }


    private boolean needAllBold = false;


    public void setNeedAllBold(boolean needAllBold) {
        this.needAllBold = needAllBold;
    }

    /**
     * 设置可见Tab数量
     */
    public void setVisibleTabCount(int count) {
        mVisibleTabCount = count;
    }

    /**
     * 设置关联的ViewPager
     */
    public void setViewPager(ViewPager viewPager, int position) {
        mViewPager = viewPager;
        if (mViewPager.getAdapter() != null) {
            List<String> titleList = new ArrayList<>(mViewPager.getAdapter().getCount());
            for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
                titleList.add((String) mViewPager.getAdapter().getPageTitle(i));
            }
            setTabItemTitles(titleList);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);

            }

            @Override
            public void onPageSelected(int position) {
                highLightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        highLightTextView(position);
    }


    /**
     * 高亮显示选中tab文本
     */
    public void highLightTextView(int position) {
        TextView view;
        for (int i = 0; i < getChildCount(); i++) {
            if (i != position) {
                view = (TextView) getChildAt(i);
                view.setTextColor(tabNormalColor);
                //设置不为加粗
                if (!needAllBold) {
                    view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }
        }
        view = (TextView) getChildAt(position);
        view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        view.setTextColor(tabSelectedColor);
    }

    /**
     * 设置Tab的点击事件
     */
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(view1 -> mViewPager.setCurrentItem(j));
        }
    }
}
