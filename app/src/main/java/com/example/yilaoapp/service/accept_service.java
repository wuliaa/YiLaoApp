package com.example.yilaoapp.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface accept_service {
    @PATCH("users/{mobile}/orders/{order_id}")
    Call<ResponseBody> accept_order(@Path("mobile")String mobile, @Path("order_id")int id,
                                    @Query("token")String token,@Query("appid")String app,@Query("receive")String receive);
    @PATCH("users/{mobile}/orders/{order_id}")
    Call<ResponseBody> cancel_order(@Path("mobile")String mobile, @Path("order_id")int id,
                                    @Query("token")String token,@Query("appid")String app,@Query("close")String close);
}
