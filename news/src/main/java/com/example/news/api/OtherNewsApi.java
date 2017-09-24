package com.example.news.api;

import com.example.news.bean.NewInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      16:56
 * QQ:             1981367757
 */

public interface OtherNewsApi {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewInfoBean>>> getNewsList(@Path("type") String type, @Path("id") String id,
                                                           @Path("startPage") int startPage);
}
