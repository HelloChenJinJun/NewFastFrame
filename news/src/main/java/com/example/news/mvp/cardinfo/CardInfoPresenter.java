package com.example.news.mvp.cardinfo;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.CugCardApi;
import com.example.news.bean.BankAccountItem;
import com.example.news.bean.CardPayHistoryBean;
import com.example.news.bean.CardPayResultBean;
import com.example.news.bean.CardPersonInfoBean;
import com.example.news.bean.Item;
import com.example.news.util.NewsUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
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

public class CardInfoPresenter extends BasePresenter<IView<Object>, CardInfoModel> {
    public CardInfoPresenter(IView<Object> iView, CardInfoModel baseModel) {
        super(iView, baseModel);
    }


    public void getPersonCardInfo() {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .getPersonCardInfo(NewsUtil.CARD_PAGE_INFO_URL, NewsUtil.getPageRequestBody())
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
                            Document document = Jsoup.parse(responseBody.string());

                            Elements elements = document.select(".user-base-info");
                            if (elements != null && elements.size() > 0 && elements.get(0).children() != null
                                    && elements.get(0).children().size() > 0) {
                                CardPersonInfoBean cardPersonInfoBean = new CardPersonInfoBean();
                                cardPersonInfoBean.setName(elements.get(0).children().get(0).text());
                                cardPersonInfoBean.setSex(elements.get(0).children().get(1).text());
                                cardPersonInfoBean.setCertType(elements.get(0).children().get(2).text());
                                cardPersonInfoBean.setCollege(elements.get(0).children().get(3).text());
                                cardPersonInfoBean.setCertNumber(elements.get(0).children().get(4).text());
                                cardPersonInfoBean.setUnit(elements.get(0).children().get(5).text());
                                iView.updateData(cardPersonInfoBean);
                                getBankAccountInfo();
                            } else {
                                iView.updateData(null);
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


    private void getBankAccountInfo() {
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .getBankAccountInfo(NewsUtil.CARD_BANK_INFO_URL, NewsUtil.getBankAccountRequestBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Item>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull Item item) {
                        try {
                            JSONObject jsonObject = new JSONObject(item.getMsg());
                            String info = jsonObject.getString("query_card");
                            BankAccountItem bankAccountItem = new Gson().fromJson(info, BankAccountItem.class);
                            BaseApplication.getAppComponent().getSharedPreferences().edit()
                                    .putString(NewsUtil.ACCOUNT, bankAccountItem.getCard().get(0).getAccount())
                                    .apply();
                            iView.updateData(bankAccountItem);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            CommonLogger.e("1json解析出错");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonLogger.e("json解析出错");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

    public void pay(String money) {
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .pay(NewsUtil.PAY_URL, NewsUtil.getPayRequestBody(money))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardPayResultBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull CardPayResultBean cardPayResultBean) {
                        iView.updateData(cardPayResultBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }




    public void getPayHistory(final boolean isShowLoading, final boolean isRefresh){
        baseModel.getRepositoryManager().getApi(CugCardApi.class)
                .getPayHistoryData(NewsUtil.PAY_HISTORY_URL,NewsUtil.getPayHistoryRequestBody(0,0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardPayHistoryBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull CardPayHistoryBean cardPayHistoryBean) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getPayHistory(isShowLoading, isRefresh);
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
