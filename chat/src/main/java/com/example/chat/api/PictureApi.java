package com.example.chat.api;

import com.example.chat.bean.PictureResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:36
 * QQ:             1981367757
 */
public interface PictureApi {

        @GET("/api/data/福利/10/{page}")
        Observable<PictureResponse> getPictureInfo(@Path("page") int page);
}
