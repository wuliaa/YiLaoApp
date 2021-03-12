package com.example.yilaoapp.service;

import android.content.Context;

import com.example.yilaoapp.utils.CacheInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUser {
    public  Retrofit get(Context context){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        CacheInterceptor interceptor=new CacheInterceptor(context);
        OkHttpClient client=new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .cache(provideOkHttpCache(context))
                .addInterceptor(interceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.yilao.tk:15000/v1.0/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();//增加返回值为实体类的支持
        //创建service
        return retrofit;

    }
    Cache provideOkHttpCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }

}