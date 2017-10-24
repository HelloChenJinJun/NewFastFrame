package com.example.chat.api;


import com.example.chat.bean.HappyContentResponse;
import com.example.chat.bean.HappyResponse;
import com.example.chat.bean.PictureResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:18
 * QQ:             1981367757
 */

public interface HappyApi {

    //        @GET("/joke/img/text.from?key=" + Constant.JU_HE_KEY)
    @GET
    public Observable<HappyResponse> getHappyInfo(@Url String url);

    @GET
    public Observable<HappyContentResponse> getHappyContentInfo(@Url String url);

    public Observable<PictureResponse> getPictureInfo(int page, int i);

}
