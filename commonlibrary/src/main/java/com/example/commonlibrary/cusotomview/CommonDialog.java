package com.example.commonlibrary.cusotomview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.R;

import androidx.annotation.NonNull;


/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/11/12     16:44
 */
public class CommonDialog extends Dialog implements View.OnClickListener {


    private Builder mBuilder;
    private TextView right, left, title;


    private CommonDialog(@NonNull Context context, Builder builder) {
        this(context, R.style.dialog_custom_style, builder);
    }

    private CommonDialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        initView(builder);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }


    public TextView getRight() {
        return right;
    }


    public TextView getLeft() {
        return left;
    }


    public TextView getTitle() {
        return title;
    }

    private void initView(Builder builder) {
        this.mBuilder = builder;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_common, null, false);
        view.findViewById(R.id.view_view_dialog_common_delete).setOnClickListener(this);
        title = view.findViewById(R.id.tv_view_dialog_common_title);
        left = view.findViewById(R.id.tv_view_dialog_common_left);
        right = view.findViewById(R.id.tv_view_dialog_common_right);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        left.setText(builder.leftName);
        right.setText(builder.rightName);
        title.setText(builder.title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        LinearLayout linearLayout = view.findViewById(R.id.ll_view_dialog_common_content_container);
        linearLayout.addView(builder.contentView);
        if (builder.rightName == null) {
            right.setVisibility(View.GONE);
        }
        if (builder.leftName == null) {
            left.setVisibility(View.GONE);
        }
        setContentView(view);
    }

    public static CommonDialog.Builder newBuild(Context context) {
        return new CommonDialog.Builder(context);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view_view_dialog_common_delete) {
            dismiss();
            if (mBuilder.deleteOnClick != null) {
                mBuilder.deleteOnClick.onClick(v);
            }
        } else if (id == R.id.tv_view_dialog_common_left) {
            dismiss();
            if (mBuilder.leftListener != null) {
                mBuilder.leftListener.onClick(v);
            }

        } else if (id == R.id.tv_view_dialog_common_right) {
            dismiss();
            if (mBuilder.rightListener != null) {
                mBuilder.rightListener.onClick(v);
            }

        }
    }


    public static class Builder {
        private View contentView;
        private String title;
        private String leftName, rightName;
        private View.OnClickListener leftListener, rightListener, deleteOnClick;
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }


        public CommonDialog.Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }


        public CommonDialog.Builder setInfo(String info) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(mContext);
            layoutParams.gravity = Gravity.CENTER;
            textView.setText(info);
            textView.setTextSize(9);
            textView.setTextColor(Color.parseColor("#ff333333"));
            textView.setLayoutParams(layoutParams);
            this.contentView = textView;
            return this;
        }


        public CommonDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public CommonDialog.Builder setLeftButton(String name, View.OnClickListener leftListener) {
            this.leftName = name;
            this.leftListener = leftListener;
            return this;
        }


        public CommonDialog.Builder setRightButton(String name, View.OnClickListener rightListener) {
            this.rightName = name;
            this.rightListener = rightListener;
            return this;
        }


        public CommonDialog build() {
            return new CommonDialog(contentView.getContext(), this);
        }

        public Builder setOnDeleteClick(View.OnClickListener listener) {
            this.deleteOnClick = listener;
            return this;
        }
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


}
