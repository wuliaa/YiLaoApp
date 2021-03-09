package com.example.yilaoapp.service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface image_service {
    @Multipart
    @POST("users/{mobile}/resources")
    Call<ResponseBody> send_photo(@Path("mobile")String mobile, @Query("token")String token, @Query("appid")String app, @PartMap Map<String,RequestBody> image);
    //   RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), bytes);
    @Headers({"Connection:close"})
    @GET("users/{mobile}/resources/{uuid}")
    Call<ResponseBody> load_photo(@Path("mobile") BigInteger mobile, @Path("uuid")String uuid, @Query("appid")String appid);

}
