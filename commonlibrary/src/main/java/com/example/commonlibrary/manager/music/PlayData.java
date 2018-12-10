package com.example.commonlibrary.manager.music;

import com.example.commonlibrary.bean.music.MusicPlayBean;

import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     11:42
 */
public class PlayData {

    public static final int PLAY_MODE_RANDOM = 1;
    public static final int PLAY_MODE_ORDER = 2;
    public static final int PLAY_MODE_RECYCLER = 3;
    private List<MusicPlayBean> urlList;
    private int position;
    private int playMode = PLAY_MODE_ORDER;
    private boolean needRecycler = true;


    public void setData(List<MusicPlayBean> urlList, int position) {
        this.urlList = urlList;
        this.position = position;
        randomPositionList = new Stack<>();
    }

    public MusicPlayBean getCurrentItem() {
        if (urlList != null && position >= 0 && position < urlList.size()) {
            return urlList.get(position);
        }
        return null;
    }


    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    private Stack<Integer> randomPositionList;

    public MusicPlayBean next() {
        if (urlList == null || urlList.size() == 0) {
            return null;
        }
        switch (playMode) {
            case PLAY_MODE_RANDOM:
                Random random = new Random();
                randomPositionList.add(position);
                position = random.nextInt(urlList.size());
                return urlList.get(position);
            case PLAY_MODE_ORDER:
                int newPosition = position + 1;
                if (newPosition == urlList.size()) {
                    if (needRecycler) {
                        position = (position + 1) % urlList.size();
                        return urlList.get(position);
                    } else {
                        return null;
                    }
                }
                position = newPosition;
                return urlList.get(position);
            case PLAY_MODE_RECYCLER:
                return urlList.get(position);
            default:
                return null;
        }
    }

    public MusicPlayBean pre() {
        if (urlList == null || urlList.size() == 0) {
            return null;
        }
        switch (playMode) {
            case PLAY_MODE_RANDOM:
                if (randomPositionList.size() > 0) {
                    position = randomPositionList.pop();
                }
                return urlList.get(position);
            case PLAY_MODE_ORDER:
                int newPosition = position - 1;
                if (newPosition != -1) {
                    position = newPosition;
                } else {
                    position = urlList.size() - 1;
                }
                return urlList.get(position);
            case PLAY_MODE_RECYCLER:
                return urlList.get(position);
            default:
                return null;
        }
    }

    public int getPlayMode() {
        return playMode;
    }

    public int getPosition() {
        return position;
    }
}
