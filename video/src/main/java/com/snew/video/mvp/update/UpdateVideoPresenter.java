package com.snew.video.mvp.update;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/17     16:28
 */
public class UpdateVideoPresenter extends RxBasePresenter<IView<BaseBean>,DefaultModel> {

    public UpdateVideoPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }
}
