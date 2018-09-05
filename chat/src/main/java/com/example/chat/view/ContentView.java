package com.example.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.chat.util.LogUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/4      19:03
 * QQ:             1981367757
 */

public class ContentView extends RelativeLayout {
        private int range = 0;

        public ContentView(Context context) {
                this(context, null);
        }

        public ContentView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }


        public void setRange(int range) {
                this.range = range;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
//                if (ev.getRawX() >= range && getLeft() >= range) {
//                        LogUtil.e("中断");
//                } else {
//                        LogUtil.e("不中断");
//                }
                return ev.getRawX() >= range && getLeft() >= range;
        }
}
