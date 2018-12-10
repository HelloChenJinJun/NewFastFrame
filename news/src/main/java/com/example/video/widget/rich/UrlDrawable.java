package com.example.video.widget.rich;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/6     9:24
 */
public class UrlDrawable extends BitmapDrawable implements Drawable.Callback{
    private Drawable drawable;

    @SuppressWarnings("deprecation")
    public UrlDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawable != null)
            drawable.draw(canvas);
    }

    public Drawable getDrawable() {
        return drawable;
    }
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }
    @Override
    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
