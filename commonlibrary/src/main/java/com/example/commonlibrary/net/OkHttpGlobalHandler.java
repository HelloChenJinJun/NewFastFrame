package com.example.commonlibrary.net;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by COOTEK on 2017/7/28.
 */

public interface OkHttpGlobalHandler {


    public Response onResultResponse(String printResult, Interceptor.Chain chain, Response response);

    public Request onRequestBefore(Interceptor.Chain chain, Request request);


    //    内部实现一个默认的中断处理handler
    public static OkHttpGlobalHandler DEFAULT_HANDLER = new OkHttpGlobalHandler() {
        @Override
        public Response onResultResponse(String printResult, Interceptor.Chain chain, Response response) {
            return response;
        }

        @Override
        public Request onRequestBefore(Interceptor.Chain chain, Request request) {
            return request;
        }
    };


}
