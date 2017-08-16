package com.example.cootek.newfastframe.mvp;

import android.graphics.Bitmap;

import com.example.commonlibrary.mvp.IView;

import java.io.File;

/**
 * Created by COOTEK on 2017/8/14.
 */

public interface IBottomView extends IView {
    public void updateMusicContent(File file);

    public void updateMaxProgress(int max);

    public void updatePlayStatus(boolean isPlaying);

    public void updateAlbum(String uri);

    public void updateProgress();

    public void updateSongName(String name);

    public void updateArtistName(String name);

}
