package com.example.yilaoapp.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface image_service {
    @POST("users/{mobile}/resources")
    Call<ResponseBody> send_photo(@Path("mobile")String mobile, @Query("token")String token, @Query("appid")String app, @Body RequestBody image);
    //   RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), bytes);
    @GET("users/{mobile}/resources/{uuid}")
    Call<ResponseBody> load_photo(@Path("mobile")String mobile,@Path("uuid")String uuid,@Query("token")String token,@Query("appid")String appid);
}
