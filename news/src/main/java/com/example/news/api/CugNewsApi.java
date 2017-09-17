package com.example.news.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      17:08
 * QQ:             1981367757
 */

public interface CugNewsApi {
    @GET
    public Observable<ResponseBody>  getCugNewsData(@Url String url);

    @GET
    public Observable<ResponseBody>  searchBook(@Url String url);
}
