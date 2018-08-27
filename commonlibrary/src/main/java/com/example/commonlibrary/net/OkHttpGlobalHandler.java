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


}
