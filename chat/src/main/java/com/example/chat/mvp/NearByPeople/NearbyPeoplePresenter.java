package com.example.chat.mvp.NearByPeople;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/4/4     9:58
 * QQ:         1981367757
 */

public class NearbyPeoplePresenter extends AppBasePresenter<IView<List<User>>,DefaultModel>{

    private int page=0;

    public NearbyPeoplePresenter(IView<List<User>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void queryNearbyPeople(boolean isRefresh, int flag) {
        if (isRefresh) {
            iView.showLoading(null);
            page=0;
        }
        page++;
        UserManager.getInstance().queryNearbyPeople(isRefresh?0:page*10, flag, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    iView.updateData(list);
                    iView.hideLoading();
                }else {
                    iView.showError(e.toString(), () -> queryNearbyPeople(isRefresh, flag));
                }
            }
        });


    }
}
