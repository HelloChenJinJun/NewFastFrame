package com.example.live.api;

import com.example.live.bean.CategoryLiveBean;
import com.example.live.bean.ListLiveBean;
import com.example.live.bean.LiveRoomBean;
import com.example.live.bean.RecommendLiveBean;
import com.example.live.bean.SearchLiveBean;
import com.example.live.bean.SearchRequestBody;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      16:39
 * QQ:             1981367757
 */

public interface LiveApi {
//    /**
//     * 获取App启动页信息
//     * @return
//     */
//    @GET("json/page/app-data/info.json?v=3.0.1&os=1&ver=4")
//    Observable<AppStart> getAppStartInfo();
//
    /**
     * 获取分类列表
     * @return
     *
     * categories/list.json
     */
    @GET("json/app/index/category/info-android.json?v=3.0.1&os=1&ver=4")
    Observable<List<CategoryLiveBean>> getAllCategories();

    /**
     * 获取推荐列表
     * @return
     */
    @GET("json/app/index/recommend/list-android.json?v=3.0.1&os=1&ver=4")
    Observable<RecommendLiveBean> getRecommendLiveData();

//    /**
//     * 获取直播列表
//     * @return
//     */
//    @GET("json/play/list.json?v=3.0.1&os=1&ver=4")
//    Observable<LiveListResult> getLiveListResult();

//
    @GET("json/categories/{slug}/list.json?v=3.0.1&os=1&ver=4")
    Observable<ListLiveBean> getCategoryItemData(@Path("slug") String slug);
//
    /**
     * 进入房间
     * @param uid
     * @return
     */
    @GET("json/rooms/{uid}/info.json?v=3.0.1&os=1&ver=4")
    Observable<LiveRoomBean> enterRoom(@Path("uid")String uid);

    /**
     * 搜索
     * @param searchRequestBody
     * @return
     */
    @POST("site/search")
    Observable<SearchLiveBean> search(@Body SearchRequestBody searchRequestBody);
}
