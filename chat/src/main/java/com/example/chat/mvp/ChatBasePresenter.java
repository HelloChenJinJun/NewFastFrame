package com.example.chat.mvp;

import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     15:04
 * QQ:         1981367757
 */

public abstract class ChatBasePresenter<V extends IView, M extends BaseModel> extends RxBasePresenter<V,M> {
    protected Set<Subscription> subscriptionSet=new HashSet<>();
    public ChatBasePresenter(V iView, M baseModel) {
        super(iView, baseModel);
    }

    public   void addSubscription(Subscription subscription){
        if (subscription!=null) {
            subscriptionSet.add(subscription);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriptionSet != null) {
            for (Subscription item :
                    subscriptionSet) {
                if (!item.isUnsubscribed()) {
                    item.unsubscribe();
                }
            }
            subscriptionSet.clear();
            subscriptionSet=null;
        }
    }
}
