package com.example.cootek.newfastframe.mvp;

import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.mvp.RxBasePresenter;

/**
 * Created by COOTEK on 2017/8/14.
 */

public abstract class BaseBottomPresenter<V extends IBottomView,M extends BaseModel> extends RxBasePresenter<V,M> {


    public BaseBottomPresenter(V iView, M baseModel) {
        super(iView, baseModel);
    }

    public abstract void playOrPause();

    public abstract void next();

    public abstract void previous();

    public abstract void loadMusicContent(String songName, String artistName, long duration);
}
