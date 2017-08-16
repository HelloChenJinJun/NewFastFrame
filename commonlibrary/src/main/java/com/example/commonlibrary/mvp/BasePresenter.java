package com.example.commonlibrary.mvp;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class BasePresenter<V extends IView, M extends BaseModel> implements IPresenter {
    private CompositeDisposable mCompositeDisposable;
    protected V iView;
    protected M baseModel;


    public BasePresenter(V iView, M baseModel) {
        this.iView = iView;
        this.baseModel = baseModel;
    }

    @Override
    public void onDestroy() {
        unDispose();
        if (iView != null) {
            iView = null;
        }
        if (baseModel != null) {
            baseModel.onDestroy();
        }
        this.mCompositeDisposable = null;
    }

    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有disposable放入,集中处理
    }

    private void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证activity结束时取消所有正在执行的订阅
        }
    }


}
