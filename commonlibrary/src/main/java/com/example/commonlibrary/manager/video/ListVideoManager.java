package com.example.commonlibrary.manager.video;

import com.example.commonlibrary.manager.IVideoPlayer;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/26     9:23
 */
public class ListVideoManager {
    private static ListVideoManager sInstance;
    private IVideoPlayer currentPlayer;

    public static ListVideoManager getInstance() {
        if (sInstance == null) {
            sInstance = new ListVideoManager();
        }
        return sInstance;
    }


    public void setCurrentPlayer(IVideoPlayer currentPlayer) {
        if (this.currentPlayer == null) {
            this.currentPlayer = currentPlayer;
        } else if (!currentPlayer.equals(this.currentPlayer)) {
            this.currentPlayer.release();
            this.currentPlayer = currentPlayer;
        }

    }

    private ListVideoManager() {

    }


    public boolean onBackPressed() {
        if (currentPlayer != null && currentPlayer.getWindowState() == DefaultVideoPlayer.WINDOW_STATE_FULL) {
            currentPlayer.setWindowState(DefaultVideoPlayer.WINDOW_STATE_LIST);
            return true;
        }
        return false;
    }


    public void release() {
        currentPlayer.release();
        currentPlayer = null;
    }

    public void error() {
        if (currentPlayer != null) {
            currentPlayer.getController().onPlayStateChanged(DefaultVideoPlayer.PLAY_STATE_ERROR);
        }
    }

    public void updateUrl(String url) {
        currentPlayer.setUp(url, null);
        currentPlayer.start();
    }

    public IVideoPlayer getCurrentPlayer() {
        return currentPlayer;
    }
}
