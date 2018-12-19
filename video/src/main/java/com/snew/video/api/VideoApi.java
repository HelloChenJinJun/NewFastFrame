package com.snew.video.api;

import com.snew.video.bean.HotVideoBean;
import com.snew.video.bean.OtherVideoDetailBean;
import com.snew.video.bean.QQTVVideoDetailBean;
import com.snew.video.bean.QQVideoDetailBean;
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

    @GET
    Observable<ResponseBody> getContent(@Url String url);


    @POST
    Observable<ResponseBody> getOtherCookie(@Url String url, @Body RequestBody requestBody);


    @GET
    Observable<ResponseBody> getQQVideoListBean(@Url String url);


    @POST
    Observable<QQVideoDetailBean> postUrlInfo(@Url String url, @Body RequestBody requestBody);


    @POST
        //    $.post("api.php", {"id": "1006_43dc5ba04ae343ff97111d83b437f2f4","type": "qqmtv","siteuser": '',"md5": sign($('#hdMd5').val()),"hd":"","lg":""},
    Observable<QQTVVideoDetailBean> postQQTVUrlInfo(@Url String url, @Body RequestBody requestBody);

    @GET
    Observable<ResponseBody> getVideoListHeaderData(@Url String url);

    @GET
    Observable<HotVideoBean> getHotVideoData(@Url String url);

    @GET
    Observable<ResponseBody> searchVideo(@Url String url);


    @POST
    Observable<OtherVideoDetailBean> postBaiYuUrl(@Url String url, @Body RequestBody requestBody);

    @GET
    Observable<ResponseBody> getVarietyDetailInfo(@Url String url);
}
