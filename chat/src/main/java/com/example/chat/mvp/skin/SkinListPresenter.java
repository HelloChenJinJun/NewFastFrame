package com.example.chat.mvp.skin;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.SkinBean;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:48
 */

public class SkinListPresenter extends AppBasePresenter<IView<List<SkinBean>>,SkinListModel>{
    public SkinListPresenter(IView<List<SkinBean>> iView, SkinListModel baseModel) {
        super(iView, baseModel);
    }
}
