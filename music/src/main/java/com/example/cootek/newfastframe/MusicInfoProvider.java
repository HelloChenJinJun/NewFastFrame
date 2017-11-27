package com.example.cootek.newfastframe;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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


    public static Observable<List<MusicPlayBean>> searchMusic(Context context, String searchString) {
        return getMusicForCursor(getSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                new String[]{"%" + searchString + "%", "%" + searchString + "%", "%" + searchString + "%"}));
    }

    public static Observable<List<MusicPlayBean>> getAllMusic(boolean isLocal) {
        if (isLocal) {
            return getMusicForCursor(getSongCursor(BaseApplication.getInstance(), null, null));
        } else {
            return getDownLoadMusic();
        }
    }

    private static Observable<List<MusicPlayBean>> getDownLoadMusic() {
        List<MusicPlayBean> result = VideoApplication.getMainComponent().getDaoSession().getMusicPlayBeanDao().queryBuilder().where(MusicPlayBeanDao.Properties.IsLocal.eq(Boolean.FALSE)).list();
        return Observable.just(result);
    }


    public static Observable<List<MusicPlayBean>> getMusicForPage(Context context, int num, int pageSize) {
        return getMusicForCursor(getSongCursor(context, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER + " desc limit " + pageSize + " offset " + num));
    }

    public static Observable<List<MusicPlayBean>> getMusicForPage(int num, int pageSize) {
        return getMusicForPage(BaseApplication.getInstance(), num, pageSize);
    }


    private static Observable<List<MusicPlayBean>> getMusicForCursor(final Cursor cursor) {
        return Observable.create(new ObservableOnSubscribe<List<MusicPlayBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MusicPlayBean>> e) throws Exception {
                List<MusicPlayBean> list = new ArrayList<>();
                if (cursor != null && cursor.moveToFirst()) {
                        do {
                        MusicPlayBean music = new MusicPlayBean();
                        CommonLogger.e("data", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)));
                        music.setSongId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)));
                        music.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)));
                        music.setArtistId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)) + "");
                        music.setArtistName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
                        music.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)));
                        music.setAlbumName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
                        music.setAlbumUrl(MusicUtil.getAlbumArtUri(music.getAlbumId()).toString());
                        music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
                        music.setLocal(true);
                        list.add(music);
                    } while (cursor.moveToNext());
                }
                e.onNext(list);
                e.onComplete();
            }
        });
    }


    public static Cursor getSongCursor(Context context, String selection, String[] selectionArgs) {
        StringBuilder select = new StringBuilder(" 1=1 and title != ''");
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + 1048576);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + 6000);
        if (!TextUtils.isEmpty(selection)) {
            select.append(" and ").append(selection);
        }
        CommonLogger.e("这1");
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.AudioColumns._ID, MediaStore.Audio.AudioColumns.TITLE
                , MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.TRACK, MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA
        }, select.toString(), selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }


    public static Observable<List<MusicPlayBean>> getMusicForSinger(String tingId) {
        return Observable.just(VideoApplication.getMainComponent().getDaoSession().getMusicPlayBeanDao().queryBuilder()
                .where(MusicPlayBeanDao.Properties.TingId.eq(tingId)).list());
    }
}
