package com.example.cootek.newfastframe;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by COOTEK on 2017/8/10.
 */

public class MusicUtil {


    public static final Integer[] RANK_TYPE_LIST = new Integer[]{
            1,2,6,7,8,9,11,14,20,21,22,23,24,25
    };

    public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }


    public static String musicLyricDir = Environment.getExternalStorageDirectory().toString() + "Music/Lyric/";

    public static String getLyricPath(String songName, String artistName) {
        return musicLyricDir + songName + "_" + artistName + ".lrc";
    }
}
