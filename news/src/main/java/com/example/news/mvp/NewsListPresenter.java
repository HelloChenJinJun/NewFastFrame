package com.example.news.mvp;

import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.CugNewsApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:50
 * QQ:             1981367757
 */

public class NewsListPresenter extends BasePresenter<IView<Object>,NewsListModel> {
    public NewsListPresenter(IView<Object> iView, NewsListModel baseModel) {
        super(iView, baseModel);
    }


    public void getCugNewsData(){
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                          Document document=Jsoup.parse(responseBody.string());
                            Element element=document.getElementById("slider1");
                            Elements elements=element.children();
                            for (Element item :
                                    elements) {
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
