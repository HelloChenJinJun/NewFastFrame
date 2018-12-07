package com.example.commonlibrary.cusotomview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/9/21     16:31
 */
public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //设置接口
    public interface OnScrollListener {
        void onScroll(int l, int t, int oldl, int oldt);
    }


    private OnScrollListener scrollListener;


    public void setScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    //重写原生onScrollChanged方法，将参数传递给接口，由接口传递出去
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            //这里我只传了垂直滑动的距离
            scrollListener.onScroll(l, t, oldl, oldt);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
