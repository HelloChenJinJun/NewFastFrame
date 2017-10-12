package com.example.chat.api;


import com.example.chat.base.Constant;
import com.example.chat.bean.HappyContentResponse;
import com.example.chat.bean.HappyResponse;
import com.example.chat.bean.PictureResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:18
 * QQ:             1981367757
 */

public interface HappyApi {

        @GET("/joke/img/text.from?key=" + Constant.JU_HE_KEY)
       public Observable<HappyResponse> getHappyInfo(@Query("page") int page, @Query("pagesize") int pagesize);

        @GET("/joke/content/text.from?key=" + Constant.JU_HE_KEY)
       public Observable<HappyContentResponse> getHappyContentInfo(@Query("page") int page, @Query("pagesize") int pagesize);

        public Observable<PictureResponse> getPictureInfo(int page, int i);

}
