package com.example.cootek.newfastframe;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by COOTEK on 2017/8/10.
 */

public class MusicUtil {


    public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }
}
