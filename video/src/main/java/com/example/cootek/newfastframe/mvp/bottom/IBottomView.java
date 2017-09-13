package com.example.cootek.newfastframe.mvp.bottom;

import com.example.commonlibrary.mvp.view.IView;

/**
 * Created by COOTEK on 2017/8/14.
 */

public interface IBottomView<T> extends IView<T> {

    public void updateMaxProgress(int max);

    public void updatePlayStatus(boolean isPlaying);

    public void updateAlbum(String uri);

    public void updateProgress();

    public void updateSongName(String name);

    public void updateArtistName(String name);

}
