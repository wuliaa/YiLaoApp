package com.example.yilaoapp.service;
import com.example.yilaoapp.bean.errand_order;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface errand_service {
    @POST("users/{mobile}/orders")
    Call<ResponseBody> new_order(@Path("mobile")String mobile, @Query("token")String token, @Query("appid")String app, @Body errand_order oreder);
    @GET("public_orders")
    Call<ResponseBody> get_orders(@Query("type")String type);
}
