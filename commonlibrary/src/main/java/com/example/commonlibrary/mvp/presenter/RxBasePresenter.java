package com.example.commonlibrary.mvp.presenter;

import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

public class RxBasePresenter<V extends IView, M extends BaseModel> extends BasePresenter<V, M> {


    public RxBasePresenter(V iView, M baseModel) {
        super(iView, baseModel);
    }


    public <T> void registerEvent(Class<T> type, Consumer<T> consumer) {
        Disposable disposable = RxBusManager.getInstance().registerEvent(type, consumer, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                CommonLogger.e("rx事件出错啦" + throwable.getMessage());
            }
        });
        addDispose(disposable);
    }
}
