package com.example.news.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      15:34
 * QQ:             1981367757
 */

public interface CugLibraryApi {
    @GET
    public Observable<ResponseBody> getCookie(@Url String url);

    @POST
    public Observable<ResponseBody> verify(@Url String url);

    @GET
    public Observable<ResponseBody> getBorrowBookInfo(@Url String url);


    @GET
    public Observable<ResponseBody> getBorrowBookHistoryInfo(@Url String url);

    @GET
    public Observable<ResponseBody> getVerifyImage(@Url String url);


    @GET
    public Observable<ResponseBody> getNewBook(@Url String url);


    @GET
    public Observable<ResponseBody> getNewsBookNumberInfo(@Url String url);

    @GET
    public Observable<ResponseBody> borrowBook(String url);
}
