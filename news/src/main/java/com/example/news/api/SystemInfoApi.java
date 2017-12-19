package com.example.news.api;

import com.example.news.bean.CardLoginBean;
import com.example.news.bean.ScoreBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     15:05
 * QQ:         1981367757
 */

public interface SystemInfoApi {


//    http://sfrz.cug.edu.cn/tpass/login?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F
    @GET
    public Observable<ResponseBody>  getCookie(@Url String url);


    @POST
    public Observable<ResponseBody>  login(@Url String url, @Body RequestBody requestBody);



    @POST
    public Observable<ScoreBean>   getScore(@Url String url,@Body RequestBody requestBody);

}
