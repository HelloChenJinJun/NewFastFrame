package com.example.chat.api;

import com.example.chat.bean.TxResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      21:17
 * QQ:             1981367757
 */

public interface TxApi {
@GET
        Observable<TxResponse> getWinXinInfo(@Url String url);
}
