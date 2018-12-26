package com.example.cootek.newfastframe.mvp.singer;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     15:04
 */
public class SingerDetailPresenter extends RxBasePresenter<IView<BaseBean>,DefaultModel> {

    public SingerDetailPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }
}
