package com.example.commonlibrary.cusotomview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.R;


/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/11      17:15
 * QQ:             1981367757
 */

public class HeaderLayout extends FrameLayout {

        private ImageView back;
        private ImageView icon;
        private TextView title;
        private TextView rightText;
        private ImageView rightImage;

        public HeaderLayout(@NonNull Context context) {
                this(context, null);
        }

        public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initView();
        }

        private void initView() {
                View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_layout, null);
                addView(headerView);
        }


}
