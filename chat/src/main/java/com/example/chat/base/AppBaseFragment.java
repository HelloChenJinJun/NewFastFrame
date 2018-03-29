package com.example.chat.base;



import com.example.chat.ChatApplication;
import com.example.chat.dagger.ChatMainComponent;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.mvp.presenter.BasePresenter;

import java.util.HashSet;
import java.util.Set;

import rx.Subscription;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     11:06
 * QQ:         1981367757
 */

public  abstract class AppBaseFragment<T, P extends BasePresenter> extends BaseFragment<T,P> {
    protected Set<Subscription> subscriptionSet=new HashSet<>();



    protected void addSubscription(Subscription subscription){
        subscriptionSet.add(subscription);
    }



    protected ChatMainComponent getMainComponent(){
        return ChatApplication.getChatMainComponent();
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
