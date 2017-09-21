package com.example.news.api;

import com.example.news.bean.CardLoginBean;
import com.example.news.bean.CardPayHistoryBean;
import com.example.news.bean.CardPayResultBean;
import com.example.news.bean.Item;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      19:52
 * QQ:             1981367757
 */

public interface CugCardApi {


    @GET
    public Observable<ResponseBody>  getCookie(@Url String url);
    @GET
    public Observable<ResponseBody> getVerifyImage(@Url String url);
    @POST
    public Observable<CardLoginBean>  login(@Url String url,@Body RequestBody requestBody);


    @POST
    public Observable<ResponseBody>  getPersonCardInfo(@Url String url,@Body RequestBody requestBody);



    @POST
    public Observable<Item> getBankAccountInfo(@Url String url, @Body RequestBody requestBody);


    @POST
    public Observable<CardPayResultBean> pay(@Url String url, @Body RequestBody payRequestBody);


    @POST
    public Observable<CardPayHistoryBean>  getPayHistoryData(@Url String url,@Body RequestBody requestBody);

}
