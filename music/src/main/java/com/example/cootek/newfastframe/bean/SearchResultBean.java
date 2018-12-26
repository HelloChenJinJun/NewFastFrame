package com.example.cootek.newfastframe.bean;

import com.example.commonlibrary.bean.music.MusicPlayBean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     11:05
 */
public class SearchResultBean {
    List<MusicPlayBean> mMusicPlayBeanList;
    List<AlbumWrappedBean> mAlbumBeans;
    List<ArtistInfo> artistInfoList;


    public List<MusicPlayBean> getMusicPlayBeanList() {
        return mMusicPlayBeanList;
    }

    public void setMusicPlayBeanList(List<MusicPlayBean> musicPlayBeanList) {
        mMusicPlayBeanList = musicPlayBeanList;
    }

    public List<AlbumWrappedBean> getAlbumBeans() {
        return mAlbumBeans;
    }


    public void setAlbumBeans(List<AlbumWrappedBean> albumBeans) {
        mAlbumBeans = albumBeans;
    }

    public List<ArtistInfo> getArtistInfoList() {
        return artistInfoList;
    }

    public void setArtistInfoList(List<ArtistInfo> artistInfoList) {
        this.artistInfoList = artistInfoList;
    }
}
