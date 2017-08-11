package com.example.cootek.newfastframe;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by COOTEK on 2017/8/10.
 */

public class MusicInfoProvider {

    public static Cursor getSongCursor(Context context, String selection, String[] selectionArgs, String sortOrder) {
        String baseSelection = "is_music=1 AND title!= ''";
        if (!TextUtils.isEmpty(selection)) {
            baseSelection = selection + " AND " + baseSelection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.AudioColumns._ID, MediaStore.Audio.AudioColumns.TITLE
                , MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA
        }, baseSelection, selectionArgs, sortOrder);
    }


    public static Observable<List<Music>> searchMusic(Context context, String searchString) {
        return getMusicForCursor(getSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                new String[]{"%" + searchString + "%", "%" + searchString + "%", "%" + searchString + "%"}));
    }

    public static Observable<List<Music>> getAllMusic(Context context) {
        return getMusicForCursor(getSongCursor(context, null, null));
    }


    public static Observable<List<Music>> getMusicForPage(Context context,int num,int pageSize){
        return getMusicForCursor(getSongCursor(context,"limit "+pageSize+" offset "+num,new String[]{num+"",pageSize+""}));
    }

    public static Observable<List<Music>> getMusicForPage(int num,int pageSize){
        return getMusicForPage(MainApplication.getInstance(),num,pageSize);
    }


    private static Observable<List<Music>> getMusicForCursor(final Cursor cursor) {
        return Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Music>> e) throws Exception {
                List<Music> list = null;
                if (cursor != null && cursor.moveToFirst()) {
                    list = new ArrayList<>();
                    do {
                        Music music = new Music();
                        music.setSongId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)));
                        music.setSongTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)));
                        music.setArtistId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)));
                        music.setArtistName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
                        music.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
                        music.setAlbumName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
                        music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)));
                        music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
                        music.setPosition(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)));
                        list.add(music);
                    } while (cursor.moveToNext());
                }
                e.onNext(list);
                e.onComplete();
            }
        });
    }


    public static Cursor getSongCursor(Context context, String selection, String[] selectionArgs) {
        String baseSelection = "is_music=1 AND title!= ''";
        if (!TextUtils.isEmpty(selection)) {
            baseSelection = baseSelection + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.AudioColumns._ID, MediaStore.Audio.AudioColumns.TITLE
                , MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA
        }, baseSelection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }


}
