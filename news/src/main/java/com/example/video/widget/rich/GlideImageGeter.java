package com.example.video.widget.rich;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.video.R;

import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/6     9:24
 */
public class GlideImageGeter implements Html.ImageGetter {

    private HashSet<Target> targets;
    private HashSet<GifDrawable> gifDrawables;
    private final Context mContext;
    private final TextView mTextView;

    public void recycle() {
        targets.clear();
        for (GifDrawable gifDrawable : gifDrawables) {
            gifDrawable.setCallback(null);
            gifDrawable.recycle();
        }
        gifDrawables.clear();
    }

    public GlideImageGeter(Context context, TextView textView) {
        this.mContext = context;
        this.mTextView = textView;
        targets = new HashSet<>();
        gifDrawables = new HashSet<>();
        mTextView.setTag(R.id.img_tag, this);
    }

    @Override
    public Drawable getDrawable(String url) {
        final UrlDrawable urlDrawable = new UrlDrawable();
        RequestBuilder load;
        final Target target;
        if (isGif(url)) {
            load = Glide.with(mContext).asGif().load(url);
            target = new GifTarget(urlDrawable);
        } else {
            load = Glide.with(mContext).asBitmap().load(url);
            target = new BitmapTarget(urlDrawable);
        }
        targets.add(target);
        load.into(target);
        return urlDrawable;
    }

    private static boolean isGif(String path) {
        int index = path.lastIndexOf('.');
        return index > 0 && "gif".toUpperCase().equals(path.substring(index + 1).toUpperCase());
    }

    private class GifTarget extends SimpleTarget<GifDrawable> {
        private final UrlDrawable urlDrawable;


        private GifTarget(UrlDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;

        }


        @Override
        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
            int w = DensityUtil.getScreenWidth(mContext);
            int hh = resource.getIntrinsicHeight();
            int ww = resource.getIntrinsicWidth();
            int high = hh * (w - 50) / ww;
            Rect rect = new Rect(20, 20, w - 30, high);
            resource.setBounds(rect);
            urlDrawable.setBounds(rect);
            urlDrawable.setDrawable(resource);
            gifDrawables.add(resource);
            resource.setCallback(mTextView);
            resource.start();
            resource.setLoopCount(GifDrawable.LOOP_FOREVER);
            mTextView.setText(mTextView.getText());
            mTextView.invalidate();
        }
    }

    private class BitmapTarget extends SimpleTarget<Bitmap> {
        private final UrlDrawable urlDrawable;

        public BitmapTarget(UrlDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }


        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
            int w = DensityUtil.getScreenWidth(mContext);
            int hh = drawable.getIntrinsicHeight();
            int ww = drawable.getIntrinsicWidth();
            int high = hh * (w - 40) / ww;
            Rect rect = new Rect(20, 20, w - 20, high);
            drawable.setBounds(rect);
            urlDrawable.setBounds(rect);
            urlDrawable.setDrawable(drawable);
            mTextView.setText(mTextView.getText());
            mTextView.invalidate();
        }
    }
}
