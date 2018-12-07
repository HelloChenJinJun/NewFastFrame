package com.example.news.widget.rich;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/6     9:23
 */
public class RichText extends AppCompatTextView implements Drawable.Callback, View.OnAttachStateChangeListener {

    private OnRichTextImageClickListener onRichTextImageClickListener;//图片点击回调
    private GlideImageGeter glideImageGeter;

    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    /**
     * 设置富文本
     *
     * @param text 富文本
     */
    public void setRichText(String text) {
        glideImageGeter = new GlideImageGeter(getContext(), this);
        Spanned spanned = Html.fromHtml(text, glideImageGeter, null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }
        // 处理图片得点击事件
        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList<>();
        for (int i = 0, size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);
            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onRichTextImageClickListener != null) {
                        onRichTextImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnRichTextImageClickListener(OnRichTextImageClickListener onRichTextImageClickListener) {
        this.onRichTextImageClickListener = onRichTextImageClickListener;
    }

    public interface OnRichTextImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        void imageClicked(List<String> imageUrls, int position);
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        glideImageGeter.recycle();
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            invalidateOutline();
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }

}
