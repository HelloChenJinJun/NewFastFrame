package com.example.news.mvp.news.othernew;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.NewInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      18:18
 * QQ:             1981367757
 */

public class OtherNewsListPresenter extends BasePresenter<IView<List<NewInfoBean>>,OtherNewsListModel>{
    private int num=0;
    public OtherNewsListPresenter(IView<List<NewInfoBean>> iView, OtherNewsListModel baseModel) {
        super(iView, baseModel);
    }

    public void getOtherNewsListData(final boolean isShowLoading, final boolean isRefresh, final String typeId) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num=0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getNewsList(typeId.equals("T1348647909107")?"headline":"list",typeId,(num-1)*20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Map<String, List<NewInfoBean>>, ObservableSource<NewInfoBean>>() {
                    @Override
                    public ObservableSource<NewInfoBean> apply(@NonNull Map<String, List<NewInfoBean>> stringListMap) throws Exception {
                        return Observable.fromIterable(stringListMap.get(typeId));
                    }
                }).toList().subscribe(new SingleObserver<List<NewInfoBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(@NonNull List<NewInfoBean> newInfoBeen) {
                iView.updateData(newInfoBeen);
                if (newInfoBeen == null || newInfoBeen.size() == 0) {
                    num--;
                }
                iView.hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (!isRefresh) {
                    num--;
                }
            iView.showError(null, new EmptyLayout.OnRetryListener() {
                @Override
                public void onRetry() {
                    getOtherNewsListData(isShowLoading, isRefresh, typeId);
                }
            });
            }
        });
    }
}
