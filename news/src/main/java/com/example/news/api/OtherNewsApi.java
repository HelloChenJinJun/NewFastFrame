package com.example.news.api;

import com.example.news.bean.NewInfoBean;
import com.example.news.bean.OtherNewsDetailBean;
import com.example.news.bean.PhotoSetBean;
import com.example.news.bean.PictureBean;
import com.example.news.bean.RawSpecialNewsBean;
import com.example.news.util.NewsUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      16:56
 * QQ:             1981367757
 */

public interface OtherNewsApi {


    @Headers({NewsUtil.HEADER_AGENT
    ,NewsUtil.CACHE_CONTROL})
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewInfoBean>>> getNewsList(@Path("type") String type, @Path("id") String id,
                                                           @Path("startPage") int startPage);
    @Headers({NewsUtil.HEADER_AGENT
            ,NewsUtil.CACHE_CONTROL})
    @GET("nc/special/{specialId}.html")
    Observable<Map<String, RawSpecialNewsBean>> getSpecialNewsData(@Path("specialId") String specialIde);

    @Headers({NewsUtil.HEADER_AGENT
            ,NewsUtil.CACHE_CONTROL})
    @GET("nc/article/{newsId}/full.html")
    Observable<Map<String, OtherNewsDetailBean>> getNewsDetail(@Path("newsId") String newsId);

    @Headers({NewsUtil.HEADER_AGENT
            ,NewsUtil.CACHE_CONTROL})
    @GET("photo/api/set/{photoId}.json")
    Observable<PhotoSetBean> getPhotoSetData(@Path("photoId") String photoId);



    @GET
    Observable<PictureBean> getPhotoListData(@Url String url);

}


