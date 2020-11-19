package com.example.yilaoapp.user;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUser {
    private UserService service;
    public  UserService getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.yilao.tk:5000/v1.0/users/$mobile/tokens")
                .addConverterFactory(GsonConverterFactory.create())
                .build();//增加返回值为实体类的支持
        //创建service
        return retrofit.create(UserService.class);

    }
}
