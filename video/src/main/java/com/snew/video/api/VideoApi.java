package com.snew.video.api;

import com.snew.video.bean.VideoDetailBean;
import com.snew.video.bean.VideoListBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/27     16:08
 */
public interface VideoApi {
    //    https://www.365yg.com/api/pc/feed/?max_behot_time=1543302402&category=video_new&utm_source=toutiao&widen=1&tadrequire=true&as=A165BB5FCC4F26B&cp=5BFC1F12B6CB8E1&_signature=z668wxAUlFtlgfxDyzcLmM-uvN
    @GET
    Observable<VideoListBean> getVideoListData(@Url String url);

    @POST
    Observable<VideoDetailBean> getVideoDetailData(@Url String url, @Body RequestBody requestBody);


    @GET
    Observable<ResponseBody> getCookie(@Url String url);


    @POST
    Observable<ResponseBody> getOtherCookie(@Url String url, @Body RequestBody requestBody);
}
