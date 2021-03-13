package com.example.yilaoapp.service;

import com.example.yilaoapp.bean.bul_order;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface bur_service {
    @POST("users/{mobile}/orders")
    Call<ResponseBody> new_order(@Path("mobile")String mobile, @Query("token")String token, @Query("appid")String app, @Body bul_order order);
    @GET("public_orders")
    Call<ResponseBody> get_orders(@Query("mobile")String mobile,@Query("type")String type,@Query("category")String category);
}
