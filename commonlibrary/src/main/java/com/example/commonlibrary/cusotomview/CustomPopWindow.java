package com.example.commonlibrary.cusotomview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.commonlibrary.R;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.DensityUtil;

/**
 * Created by COOTEK on 2017/8/22.
 */

public class CustomPopWindow extends PopupWindow {


    private View view, contentView;
    private Activity activity;

    private CustomPopWindow(Builder builder) {
        view = builder.parentView;
        activity = builder.activity;
        contentView = builder.contentView;
        initData(contentView.getContext());
        setContentView(builder.contentView);
        update();
    }


    public void show(int offsetX, int offsetY) {
        if (!isShowing()) {
            int[] location = calculatePopWindowPos(view, contentView);
            view.post(new Runnable() {
                @Override
                public void run() {
                    BlurBitmapUtil.blur(activity, contentView, 25, 8, 300);
                }
            });
            showAtLocation(view, Gravity.START | Gravity.TOP, location[0] - offsetX, location[1] - offsetY);
        } else {
            dismiss();
        }
    }


    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (contentView.getMeasuredWidth() == 0) {
                    contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                }
                BlurBitmapUtil.blur(activity, contentView, 25, 8, 200);
            }
        });
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public void show() {
        show(0, 0);
    }

    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = DensityUtil.getScreenHeight(anchorView.getContext());
        final int screenWidth = DensityUtil.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }


    private void initData(Context context) {
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        this.setBackgroundDrawable(dw);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }


    public static final class Builder {
        private View contentView;
        private View parentView;
        private Activity activity;

        public Builder() {
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder parentView(View parent) {
            this.parentView = parent;
            return this;
        }


        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public CustomPopWindow build() {
            return new CustomPopWindow(this);
        }
    }
}
