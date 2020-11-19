package com.example.yilaoapp.user;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    //登录
    @POST("?passwd=$passwd")
    Call<User> login(@Query("mobile") String username,
                     @Query("passwd") String password);
}
