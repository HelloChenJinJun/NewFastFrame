package com.snew.video.mvp.actor.list;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/21     9:31
 */
public class ActorVideoListPresenter extends RxBasePresenter<IView<BaseBean>,DefaultModel> {

    public ActorVideoListPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getData(boolean b) {

    }
}
