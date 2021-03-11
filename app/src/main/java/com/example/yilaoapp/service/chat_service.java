package com.example.yilaoapp.service;

import com.example.yilaoapp.bean.chat_task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface chat_service {
   @POST("users/{mobile}/dialogs")
    Call<ResponseBody> send_message(@Path("mobile")String mobile, @Query("token")String token, @Query("appid")String app, @Body chat_task chat);
   @GET("users/{mobile}/dialogs_with/{another_user}")
    Call<ResponseBody> get_message(@Path("mobile")String mobile,@Path("another_user")String user, @Query("token")String token,@Query("appid")String app);
}