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

    private CustomPopWindow(Builder builder) {
        contentView = builder.contentView;
        initData(contentView.getContext());
        setContentView(builder.contentView);
        update();
    }


    private void initData(Context context) {
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        this.setBackgroundDrawable(dw);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        setAnimationStyle(R.style.CustomPopWindow);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //        this.setTouchInterceptor((v, event) -> {
        //            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
        //                dismiss();
        //                return true;
        //            }
        //            return false;
        //        });
    }


    public static Builder newBuild() {
        return new Builder();
    }


    public static final class Builder {
        private View contentView;

        public Builder() {
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }


        public CustomPopWindow build() {
            return new CustomPopWindow(this);
        }
    }
}
