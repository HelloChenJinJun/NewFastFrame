package com.example.news.api;

import com.example.news.bean.CardLoginBean;
import com.example.news.bean.ConsumeQueryBean;
import com.example.news.bean.CourseQueryBean;
import com.example.news.bean.ResetPwResult;
import com.example.news.bean.ScoreBean;
import com.example.news.bean.SystemUserBean;

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
    public Observable<ResponseBody> getCookie(@Url String url);


    @POST
    public Observable<ResponseBody> login(@Url String url, @Body RequestBody requestBody);


    @POST
    public Observable<SystemUserBean> getUserInfo(@Url String url, @Body RequestBody requestBody);


    @POST
    public Observable<ScoreBean> getScore(@Url String url, @Body RequestBody requestBody);


    @POST
    public Observable<ConsumeQueryBean> getConsumeData(@Url String url, @Body RequestBody requestBody);


    //    http://sfrz.cug.edu.cn/tpass
// /login?service=http%3A%2F%2F202.114.207.137%3A80%2Fssoserver%2Flogin%3Fywxt%3Djw
    @GET
    public Observable<ResponseBody> verifyAccount(@Url String url);


    //    http://202.114.207.137:80/ssoserver/login?ywxt=jw&ticket=ST-1289-DwGFQfa3xMLBkVSyifiP-tpass
    @GET
    public Observable<ResponseBody> getTempJsessionIdByTicket(@Url String url);


    //    http://202.114.207.137/ssoserver/login;jsessionid=B2D411448CC60F02C5E82297CCE564E5?ywxt=jw
    @GET
    public Observable<ResponseBody> getVerifyUrl(@Url String url);

//http://jwgl.cug.edu.cn/jwglxt/zfssologin
// ?verify=E05058412978C941EBB01C669277F5DF&userName=20141000341&
// strSysDatetime=2017-12-2520:19:26&jsName=teacher&openType=_blank&
// url=xtgl%2Findex_initMenu.html

    @GET
    public Observable<ResponseBody> getRealIdByVerifyUrl(@Url String url);


    @POST
    public Observable<CourseQueryBean> getCourseQueryData(@Url String url, @Body RequestBody requestBody);

    @GET
    public Observable<ResponseBody> getOtherUserInfo(@Url String url);


    @POST
    public Observable<ResetPwResult> resetPw(@Url String url, @Body RequestBody resetPwRequestBody);
}
