package com.example.news;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.CugCardApi;
import com.example.news.bean.BankCardInfoBean;
import com.example.news.bean.CardPersonInfoBean;
import com.example.news.util.NewsUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
 * 创建时间:    2017/9/19      23:21
 * QQ:             1981367757
 */

public class CardInfoPresenter extends BasePresenter<IView<Object>,CardInfoModel>{
    public CardInfoPresenter(IView<Object> iView, CardInfoModel baseModel) {
        super(iView, baseModel);
    }


    public void getPersonCardInfo() {
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .getPersonCardInfo(NewsUtil.CARD_PAGE_INFO_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Document document= Jsoup.parse(responseBody.string());
                            Elements elements=document.select(".user-base-info");
                            if (elements != null&&elements.size()>0&&elements.get(0).children()!=null
                                    &&elements.get(0).children().size()>0) {
                                CardPersonInfoBean cardPersonInfoBean=new CardPersonInfoBean();
                                cardPersonInfoBean.setName(elements.get(0).children().get(0).text());
                                cardPersonInfoBean.setSex(elements.get(0).children().get(1).text());
                                cardPersonInfoBean.setCertType(elements.get(0).children().get(2).text());
                                cardPersonInfoBean.setCollege(elements.get(0).children().get(3).text());
                                cardPersonInfoBean.setCertNumber(elements.get(0).children().get(4).text());
                                cardPersonInfoBean.setUnit(elements.get(0).children().get(5).text());
                                iView.updateData(cardPersonInfoBean);
                            }else {
                                onError(null);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getPersonCardInfo();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    public void getBankAccountInfo(){
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .getBankAccountInfo(NewsUtil.CARD_BANK_INFO_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankCardInfoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull BankCardInfoBean bankCardInfoBean) {
                        if (bankCardInfoBean.isIsSucceed()) {
                            iView.updateData(bankCardInfoBean);
                        }else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getBankAccountInfo();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                            iView.hideLoading();
                    }
                });
    }

}
