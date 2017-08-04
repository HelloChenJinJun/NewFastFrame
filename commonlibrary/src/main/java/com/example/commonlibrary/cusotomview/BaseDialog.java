package com.example.commonlibrary.cusotomview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.commonlibrary.R;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/2      10:31
 * QQ:             1981367757
 */
public class BaseDialog extends Dialog {
        private LinearLayout topLayout;
        private LinearLayout middleLayout;
        private LinearLayout bottomLayout;
        private TextView message;
        private TextView title;
        private Button right;
        private Button left;

        public BaseDialog(Context context) {
                this(context, R.style.dialog_default_style);
        }

        private BaseDialog(Context context, int themeResId) {
                super(context, themeResId);
                setContentView(R.layout.common_dialog);
                setCanceledOnTouchOutside(true);
                initView();
                initListener();
        }


        private void initView() {
                topLayout = (LinearLayout) findViewById(R.id.ll_base_dialog_top);
                middleLayout = (LinearLayout) findViewById(R.id.ll_base_dialog_middle);
                bottomLayout = (LinearLayout) findViewById(R.id.ll_base_dialog_bottom);
                title = (TextView) findViewById(R.id.tv_base_dialog_title);
                message = (TextView) findViewById(R.id.tv_base_dialog_message);
                left = (Button) findViewById(R.id.btn_base_dialog_left);
                right = (Button) findViewById(R.id.btn_base_dialog_right);
        }

        private void initListener() {
        }


        /**
         * 设置对话框的内容布局
         *
         * @param layoutId 布局ID
         */
        public BaseDialog setDialogContentView(int layoutId) {
                if (middleLayout.getChildCount() > 0) {
                        middleLayout.removeAllViews();
                }
                middleLayout.addView(getLayoutInflater().inflate(layoutId, null));
                return this;
        }

        public BaseDialog setDialogContentView(View view) {
                if (middleLayout.getChildCount() > 0) {
                        middleLayout.removeAllViews();
                }
                middleLayout.addView(view);
                return this;
        }

        /**
         * 设置对话框标题，默认是不设置标题的
         *
         * @param title 标题
         */
        public BaseDialog setTitle(String title) {
                if (title == null) {
                        topLayout.setVisibility(View.GONE);
                } else {
                        topLayout.setVisibility(View.VISIBLE);
                        this.title.setText(title);
                }
                return this;
        }


        /**
         * 设置对话框头部布局
         *
         * @param layoutId 头部布局ID
         */
        public BaseDialog setDialogTopView(int layoutId) {
                if (topLayout.getVisibility() == View.GONE) {
                        topLayout.setVisibility(View.VISIBLE);
                }
                if (topLayout.getChildCount() > 0) {
                        topLayout.removeAllViews();
                }
                topLayout.addView(getLayoutInflater().inflate(layoutId, null));
                return this;
        }


        /**
         * 设置对话框底部布局
         *
         * @param layoutId 底部布局ID
         */
        public BaseDialog setDialogBottomView(int layoutId) {
                if (bottomLayout.getChildCount() > 0) {
                        bottomLayout.removeAllViews();
                }
                bottomLayout.addView(getLayoutInflater().inflate(layoutId, null));
                return this;
        }


        public BaseDialog setMessage(String message) {
                this.message.setText(message);
                return this;
        }


        public BaseDialog setLeftButton(String name, View.OnClickListener listener) {
                left.setText(name);
                left.setOnClickListener(listener);
                return this;
        }

        public BaseDialog setRightButton(String name, View.OnClickListener listener) {
                right.setText(name);
                right.setOnClickListener(listener);
                return this;
        }


        /**
         * 设置编辑列表VIEW
         *
         * @param names 编辑view 的name
         * @return this
         */
        public BaseDialog setEditViewsName(List<String> names) {
                if (middleLayout.getChildCount() > 0) {
                        middleLayout.removeAllViews();
                }

                for (String name :
                        names) {
                        TextView textView = new TextView(getContext());
                        textView.setText(name);
                        textView.setTextColor(getContext().getResources().getColor(R.color.base_color_text_black));
                         AppCompatEditText editText = new AppCompatEditText(getContext(),null, android.R.attr.editTextStyle);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        editText.setHint("请输入" + name);
                        editText.setPadding(10, 0, 0, 0);
                        editText.setHintTextColor(Color.BLUE);
                        LinearLayout child = new LinearLayout(getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        child.setOrientation(LinearLayout.HORIZONTAL);
                        child.setGravity(Gravity.CENTER_VERTICAL);
                        child.setLayoutParams(params);
                        child.addView(textView);
                        child.addView(editText);
                        middleLayout.addView(child);
                }
                return this;
        }


        public LinearLayout getMiddleLayout() {
                return middleLayout;
        }

        public BaseDialog setCheckBoxName(List<String> list) {
                if (middleLayout.getChildCount() > 0) {
                        middleLayout.removeAllViews();
                }
                for (String title :
                        list) {
                        TextView textView = new TextView(getContext());
                        textView.setGravity(Gravity.START);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.weight = 1;
                        textView.setLayoutParams(layoutParams);
                        textView.setText(title);
                        final CheckBox checkBox = new CheckBox(getContext());
                        checkBox.setGravity(Gravity.END);
                        LinearLayout.LayoutParams checkBoxLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        checkBoxLayout.weight = 1;
                        checkBox.setLayoutParams(checkBoxLayout);
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                        linearLayout.setWeightSum(2);
                        linearLayout.setLayoutParams(linearLayoutParam);
                        linearLayout.addView(textView);
                        linearLayout.addView(checkBox);
                        middleLayout.addView(linearLayout);
                }
                return this;
        }


        @Override
        public void show() {
                if (getContext() instanceof Activity) {
                        if (((Activity) getContext()).isFinishing()) {
                                return;
                        }
                }
                super.show();
        }


        @Override
        public void dismiss() {
                if (getContext() instanceof Activity) {
                        if (((Activity) getContext()).isFinishing()) {
                                return;
                        }
                }
                super.dismiss();
        }

        public BaseDialog setBottomLayoutVisible(boolean visible) {
                bottomLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
                return this;
        }
}
