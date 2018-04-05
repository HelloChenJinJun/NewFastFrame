package com.example.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.utils.DensityUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/4      17:15
 * QQ:             1981367757
 */

public class UrlTextView extends LinearLayout {
        private int clickColor;
        private int defaultColor;
        private TextView contentText;
        private TextView cover;
        private int maxLine;

        private boolean isExpand;

        public boolean isExpand() {
                return isExpand;
        }

        public void setExpand(boolean expand) {
                isExpand = expand;
        }

        public UrlTextView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public UrlTextView(Context context) {
                this(context, null);
        }

        public UrlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UrlTextView);
                clickColor = typedArray.getColor(R.styleable.UrlTextView_url_click_bg_color, getResources().getColor(R.color.liker_click_default_bg_color));
                defaultColor = typedArray.getColor(R.styleable.UrlTextView_url_default_bg_color, getResources().getColor(R.color.liker_default_bg_color));
                maxLine = typedArray.getInt(R.styleable.UrlTextView_maxLine, 5);
                if (maxLine <= 0) {
                        maxLine = 5;
                }
                typedArray.recycle();
                initView();
        }


        public interface OnTextExpandStatusChangeListener {
                void onExpandStatusChanged(boolean isExpand);
        }


        private OnTextExpandStatusChangeListener mOnTextExpandStatusChangeListener;

        public void setOnTextExpandStatusChangeListener(OnTextExpandStatusChangeListener onTextExpandStatusChangeListener) {
                mOnTextExpandStatusChangeListener = onTextExpandStatusChangeListener;
        }

        private void initView() {
                setOrientation(VERTICAL);
                LayoutInflater.from(getContext()).inflate(R.layout.share_fragment_item_content_layout, this);
                contentText = (TextView) findViewById(R.id.tv_url_text_content);
                contentText.setTextSize(DensityUtil.dip2sp(getContext(),16));
                contentText.setTextColor(getResources().getColor(R.color.base_color_text_black));
                cover = (TextView) findViewById(R.id.tv_url_text_cover);
                cover.setTextColor(getResources().getColor(R.color.base_color_text_black));
                contentText.setMaxLines(maxLine);
                cover.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (cover.getText().toString().trim().equals("全文")) {
                                        contentText.setMaxLines(Integer.MAX_VALUE);
                                        cover.setText("收起");
                                        if (mOnTextExpandStatusChangeListener != null) {
                                                mOnTextExpandStatusChangeListener.onExpandStatusChanged(false);
                                        }
                                } else if (cover.getText().toString().trim().equals("收起")) {
                                        contentText.setMaxLines(maxLine);
                                        cover.setText("全文");
                                        if (mOnTextExpandStatusChangeListener != null) {
                                                mOnTextExpandStatusChangeListener.onExpandStatusChanged(true);
                                        }
                                }
                        }
                });
        }

        public void setText(CharSequence text) {
                contentText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                                contentText.getViewTreeObserver().removeOnPreDrawListener(this);
//                                在这里判断设置text之后行数是否大于maxLine
                                if (contentText.getLineCount() > maxLine) {
                                        LogUtil.e("设置的内容大于maxLine");
                                        if (isExpand) {
                                                contentText.setMaxLines(Integer.MAX_VALUE);
                                                cover.setText("收起");
                                        } else {
                                                contentText.setMaxLines(maxLine);
                                                cover.setVisibility(VISIBLE);
                                                cover.setText("全文");
                                        }
                                } else {
                                        LogUtil.e("设置的内容小于maxLine");
                                        cover.setVisibility(GONE);
                                }
                                return true;
                        }
                });
                contentText.setText(text);
                contentText.setMovementMethod(new CustomMoveMethod());
        }


}
