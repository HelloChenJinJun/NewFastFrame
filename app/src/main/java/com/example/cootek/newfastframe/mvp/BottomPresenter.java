package com.example.cootek.newfastframe.mvp;

import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.utils.FileUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicUtil;

/**
 * Created by COOTEK on 2017/8/14.
 */

public class BottomPresenter extends BaseBottomPresenter<IBottomView,BottomModel> {


    public BottomPresenter(IBottomView iView, BottomModel baseModel) {
        super(iView, baseModel);
    }

    @Override
    public void playOrPause() {
        MusicManager.getInstance().playOrPause();
        iView.updatePlayStatus(MusicManager.getInstance().isPlaying());
        ToastUtils.showShortToast("现在是" + MusicManager.getInstance().isPlaying());
    }

    @Override
    public void next() {
        MusicManager.getInstance().next();
    }

    @Override
    public void previous() {
        MusicManager.getInstance().previous(true);
    }

    @Override
    public void loadMusicContent(String songName, String artistName, long duration) {
        if (FileUtil.isFileExist(MusicUtil.getLyricPath(songName, artistName))) {
            iView.updateMusicContent(FileUtil.getLocalFile(MusicUtil.getLyricPath(songName, artistName)));
        } else {
//            baseModel.getRepositoryManager().getApi()
        }

    }
}
