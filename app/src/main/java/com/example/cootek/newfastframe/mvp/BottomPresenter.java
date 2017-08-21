package com.example.cootek.newfastframe.mvp;

import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.MusicManager;

/**
 * Created by COOTEK on 2017/8/14.
 */

public class BottomPresenter extends BaseBottomPresenter<IBottomView, BottomModel> {


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
    public void refresh() {
        MusicManager.getInstance().refresh();
    }


}
