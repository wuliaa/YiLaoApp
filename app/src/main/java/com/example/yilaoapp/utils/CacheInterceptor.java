package com.example.yilaoapp.utils;

import android.content.Context;

import com.example.yilaoapp.utils.judge_net;

import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    Context context;
    public CacheInterceptor(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws
            IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Connection", "close")
                .build();
        judge_net net=new judge_net();
        if (!net.detect(context)) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        Response responseLatest;
        if (net.detect(context)) {
            int maxAge = 5; //有网失效一分钟
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 6; // 没网失效6小时
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return responseLatest;

    }
}