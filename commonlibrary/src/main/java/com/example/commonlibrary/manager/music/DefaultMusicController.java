package com.example.commonlibrary.manager.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     11:37
 */
public abstract class DefaultMusicController extends FrameLayout implements IMusicController {
    public DefaultMusicController(@NonNull Context context) {
        this(context, null);
    }

    public DefaultMusicController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultMusicController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
