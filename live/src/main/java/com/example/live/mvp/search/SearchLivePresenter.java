package com.example.live.mvp.search;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.live.api.LiveApi;
import com.example.live.bean.SearchLiveBean;
import com.example.live.bean.SearchRequestBody;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      12:35
 * QQ:             1981367757
 */

public class SearchLivePresenter extends BasePresenter<IView<List<SearchLiveBean.DataBean.LiveInfo>>,SearchLiveModel>{
    private int num;

    public SearchLivePresenter(IView<List<SearchLiveBean.DataBean.LiveInfo>> iView, SearchLiveModel baseModel) {
        super(iView, baseModel);
        num=0;
    }

    public void search(final boolean isShowLoading, final boolean isRefresh, final String text) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num=0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(LiveApi.class).search(SearchRequestBody.getInstance(new SearchRequestBody.P(num,text,20)))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchLiveBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull SearchLiveBean searchLiveBean) {
                        if (searchLiveBean != null && searchLiveBean.getData() != null) {
                            CommonLogger.e("数据"+searchLiveBean.toString());
                            iView.updateData(searchLiveBean.getData().getItems());
                            if (searchLiveBean.getData().getItems() == null || searchLiveBean.getData().getItems().size() == 0) {
                                num--;
                            }
                        }else {
                            iView.updateData(null);
                            CommonLogger.e("数据空");
                            num--;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                            iView.showError(null, new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                    search(isShowLoading,isRefresh,text);
                                }
                            });
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                            iView.hideLoading();
                    }
                });
    }
}
