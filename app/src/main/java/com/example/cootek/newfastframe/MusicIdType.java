package com.example.cootek.newfastframe;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by COOTEK on 2017/8/7.
 */

public interface MusicIdType {


    public static final int ARTIST = 0;
    public static final int ALBUM = 1;
    public static final int PLAY_LIST = 2;
    public static final int NORMAL = 3;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({ARTIST, ALBUM, PLAY_LIST, NORMAL})
    public @interface IdType {
    }


}
