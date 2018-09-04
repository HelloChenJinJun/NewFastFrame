package com.example.chat.mvp.person;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/12     17:55
 * QQ:         1981367757
 */

public class PersonPresenter extends RxBasePresenter<IView<Object>,DefaultModel>{


    public PersonPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }
}
