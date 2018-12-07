package com.example.commonlibrary.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;


public class SoftHideBoardUtil {
    public static void assistActivity(Activity activity,KeyBoardCallBack callBack) {
        new SoftHideBoardUtil(activity,callBack);
    }


    public static void assistActivity(Activity activity ) {
        new SoftHideBoardUtil(activity,null);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    //为适应华为小米等手机键盘上方出现黑条或不适配
    private int contentHeight;//获取setContentView本来view的高度
    private boolean isfirst = true;//只用获取一次
    private int statusBarHeight;//状态栏高度

    private SoftHideBoardUtil(Activity activity, KeyBoardCallBack callBack) {
        //1､找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        FrameLayout content = activity.findViewById(android.R.id.content);
        //2､获取到setContentView放进去的View
        mChildOfContent = content.getChildAt(0);
        //3､给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        //4､软键盘弹起会使GlobalLayout发生变化
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (isfirst) {
                contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                isfirst = false;
            }
            //5､当前布局发生变化时，对Activity的xml布局进行重绘
            possiblyResizeChildOfContent(callBack);
        });
        //6､获取到Activity的xml布局的放置参数
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }
    // 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度

    private void possiblyResizeChildOfContent(KeyBoardCallBack callBack) {
        //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow = computeUsableHeight();
        //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3､获取Activity中xml中布局在当前界面显示的高度
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();


            //4､Activity中xml布局的高度-当前可用高度
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            //5､高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
                if (callBack!=null) {
                    callBack.callBack(false);
                }
                frameLayoutParams.height+=statusBarHeight;
            } else {
                if (callBack!=null) {
                    callBack.callBack(true);
                }
                frameLayoutParams.height = contentHeight;
            }
            //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }





    public interface KeyBoardCallBack{
        public void callBack(boolean isHide);
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r); // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        return (r.bottom - r.top);
    }
}