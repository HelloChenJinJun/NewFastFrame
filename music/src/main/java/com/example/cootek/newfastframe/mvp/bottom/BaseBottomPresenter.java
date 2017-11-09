package com.example.cootek.newfastframe.mvp.bottom;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/14.
 */

public abstract class BaseBottomPresenter<V extends IBottomView, M extends BaseModel> extends RxBasePresenter<V, M> {


    public BaseBottomPresenter(V iView, M baseModel) {
        super(iView, baseModel);
    }

    public abstract void playOrPause();

    public abstract void next();

    public abstract void previous();

    public abstract void refresh();

    public abstract void play(List<MusicPlayBean> list, int position, int mode);

    public abstract void remove(int position);

    public abstract void setMode(int mode);

}
