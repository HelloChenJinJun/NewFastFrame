package com.example.chat.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/30      21:35
 * QQ:             1981367757
 */

public class PreviewViewPager extends ViewPager {
        public PreviewViewPager(Context context) {
                this(context, null);
        }

        public PreviewViewPager(Context context, AttributeSet attrs) {
                super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
                try {
                        return super.onTouchEvent(ev);
                } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                }
                return false;
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
                try {
                        return super.onInterceptTouchEvent(ev);
                } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                }
                return false;
        }
}
