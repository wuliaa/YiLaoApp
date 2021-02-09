package com.example.yilaoapp.user;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUser {
    public  Retrofit get(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.yilao.tk:5000/v1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();//增加返回值为实体类的支持
        //创建service
        return retrofit;

    }
}
